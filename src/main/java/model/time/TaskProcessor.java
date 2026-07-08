package model.time;

import model.building.*;
import model.event.Event;
import model.event.EventType;
import model.player.Player;
import model.resources.ResourcesType;
import model.village.Village;
import service.alliance.Alliance;
import service.alliance.AllianceRequest;
import service.alliance.AllianceStatus;
import service.resource.ResourcesManagement;
import service.trade.TradeOffer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class TaskProcessor {

    private Village village;
    private final AtomicBoolean processing = new AtomicBoolean(false);

    public TaskProcessor(Village village) {
        this.village = village;
    }

    public void process(){

        if (!processing.compareAndSet(false, true)) {
            throw new IllegalStateException("TaskProcessor is running concurrently!");
        }

        List<TimedOperation> snapshot;
        List<UUID> removeTasks = new ArrayList<>();
        List<TaskResult> taskResults = new ArrayList<>();

        village.getLock().readLock().lock();
        try {
            snapshot = new ArrayList<>(village.getTimedOperation().values());
        } finally {
            village.getLock().readLock().unlock();
        }

        for ( TimedOperation timedOperation : snapshot){
            if(timedOperation.isFinished()){
                TaskResult result = timedOperation.execute();
                taskResults.add(result);
                removeTasks.add(timedOperation.getId());
            }
        }

        village.getLock().writeLock().lock();
        try {

            ResourcesManagement resourcesManagement = village.getResourcesManagement();
            Building building1 = null;

            for (TaskResult task : taskResults){


                village.getTimedOperation().putAll(task.getTasksToAdd());

                //village.getBuildings().putAll(task.getBuildingsToAdd());
                for (Building building : task.getBuildingsToAdd().values()){
                    if(village.getGameMap().placeBuilding(building, building.getPosition().getX(), building.getPosition().getY())){
                        village.getBuildings().put(building.getId(), building);
                    }
                }

                for (UUID uuid : task.getTasksToRemove()){
                    village.getTimedOperation().remove(uuid);
                }

                for (UUID uuid : task.getBuildingsToUpgrade()){
                    Building building;
                    building = village.getBuildings().get(uuid);

                    if (building != null && building.getBuildingStatus() == BuildingStatus.UPGRADING) {
                        task.getProductionBuildingsToReschedule().add(building.getId());
                        building.upgrade();
                    }
                }

                for (ResourcesType resourcesType : task.getResourcesToAdd().keySet()){
                    resourcesManagement.addResource(task.getResourcesToAdd().get(resourcesType), resourcesType);
                }

                for (ResourcesType resourcesType : task.getResourcesToWithdraw().keySet()){
                    resourcesManagement.withdrawResource(task.getResourcesToWithdraw().get(resourcesType), resourcesType);
                }

                for (UUID uuid : task.getProductionBuildingsToReschedule()){
                    building1 = village.getBuildings().get(uuid);

                    if(building1 == null)
                        continue;

                    System.out.println(building1.getBuildingStatus());
                    if(building1.getBuildingStatus() == BuildingStatus.ACTIVE){
                        ProductionTask nextTask = ProductionTaskFactory.buildProductionTask(building1);
                        if(nextTask != null){
                            village.getTimedOperation().put(nextTask.getId(), nextTask);
                        }
                    }
                }

                for (EventType eventType : task.getEventType()){
                    Event event = new Event(this.village);
                    if(eventType == EventType.STORM)
                        event.storm();
                    else if(eventType == EventType.DISEASE)
                        event.disease();
                    else
                        event.discovery();
                }

                for (AllianceRequest allianceRequest : task.getAllianceRequestsToApply()){
                    if (!this.checkResearchCenterLevel(allianceRequest.getSender(), allianceRequest.getReceiver())) {
                        allianceRequest.setAllianceStatus(AllianceStatus.REJECTED);
                    }else{
                        allianceRequest.getReceiver().setAlliance(new Alliance(allianceRequest.getSender(), allianceRequest.getReceiver()));
                        allianceRequest.getSender().setAlliance(new Alliance(allianceRequest.getSender(), allianceRequest.getReceiver()));
                        this.applyAllianceBonus(allianceRequest);
                    }

                }

                for (TradeOffer tradeOffer : task.getTradeOffers()){

                    if (this.checkTradeCondition(tradeOffer)){
                        for(Map.Entry<ResourcesType,Integer> entry : tradeOffer.getRequestedResources().entrySet()){
                            ResourcesType resource = entry.getKey();
                            int amount = entry.getValue();
                            tradeOffer.getSenderVillage().getResourcesManagement().addResource(amount, resource);
                        }

                        for(Map.Entry<ResourcesType,Integer> entry : tradeOffer.getOfferedResources().entrySet()){
                            ResourcesType resource = entry.getKey();
                            int amount = entry.getValue();
                            tradeOffer.getReceiverVillage().getResourcesManagement().addResource(amount, resource);
                        }
                    }
                }
            }

            for (UUID uuid : removeTasks){
                village.getTimedOperation().remove(uuid);
            }

        }finally {
            village.getLock().writeLock().unlock();
            processing.set(false);
        }
    }

    private void applyAllianceBonus(AllianceRequest allianceRequest){
        if(allianceRequest.getSender().getAllianceCounts() == 0 ){
            for (Building building : allianceRequest.getSender().getVillage().getBuildings().values()) {
                if (building instanceof ResearchCenter researchCenter) {
                    //researchCenter.setScienceLevel(researchCenter.getScienceLevel() + 1);
                    researchCenter.upgrade();
                    break;
                }
            }
        }

        if(allianceRequest.getReceiver().getAllianceCounts() == 0){
            for (Building building : allianceRequest.getReceiver().getVillage().getBuildings().values()) {
                if (building instanceof ResearchCenter researchCenter) {
                    //researchCenter.setScienceLevel(researchCenter.getScienceLevel() + 1);
                    researchCenter.upgrade();
                    break;
                }
            }
        }

        allianceRequest.getSender().setAllianceCounts(allianceRequest.getSender().getAllianceCounts() + 1);
        allianceRequest.getReceiver().setAllianceCounts(allianceRequest.getReceiver().getAllianceCounts() + 1);
        allianceRequest.setAllianceStatus(AllianceStatus.ACCEPTED);

        /*for (Plant plant : allianceRequest.getSender().getVillage().getPlant().values()) {
            plant.upgradeNeutralizationPower();
        }
        for (Plant plant : allianceRequest.getReceiver().getVillage().getPlant().values()) {
            plant.upgradeNeutralizationPower();
        }*/

    }

    private boolean checkResearchCenterLevel(Player senderPlayer, Player receiverPlayer){

        Building senderPlayerResearchCenter = null;
        Building receiverPlayerResearchCenter = null;

        for(Building building : receiverPlayer.getVillage().getBuildings().values()){
            if(building.getType() == BuildingType.RESEARCH_CENTER){
                receiverPlayerResearchCenter = building;
                break;
            }
        }

        for(Building building : senderPlayer.getVillage().getBuildings().values()){
            if(building.getType() == BuildingType.RESEARCH_CENTER) {
                senderPlayerResearchCenter = building;
                break;
            }
        }

        if (receiverPlayerResearchCenter == null || senderPlayerResearchCenter == null)
            return false;

        if(receiverPlayerResearchCenter.getLevel() >= 2 && senderPlayerResearchCenter.getLevel() >= 2)
            return true;

        return false;
    }

    private boolean checkTradeCondition(TradeOffer tradeOffer){

        Customhouse senderCustomhouse = null;
        for(Building b : tradeOffer.getSenderVillage().getBuildings().values()){
            if(b instanceof Customhouse c){senderCustomhouse = c; break;}
        }

        Customhouse receiverCustomhouse = null;
        for(Building b : tradeOffer.getReceiverVillage().getBuildings().values()){
            if(b instanceof Customhouse c){receiverCustomhouse = c; break;}
        }

        if (senderCustomhouse != null && receiverCustomhouse != null)
            return  true;


        tradeOffer.getSenderVillage().getLock().writeLock().lock();
        try {
            for(Map.Entry<ResourcesType, Integer> entry : tradeOffer.getOfferedResources().entrySet()){
                ResourcesType type = entry.getKey();
                int originalAmount = entry.getValue();

                tradeOffer.getSenderVillage().getResourcesManagement().addResource(originalAmount, type);
            }
        }finally {
            tradeOffer.getSenderVillage().getLock().writeLock().unlock();
        }
        return false;
    }
}
