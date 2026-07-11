package model.time;



import model.building.*;

import model.army.ArmyQueue;
import model.army.ArmyType;
import model.army.QueuedArmy;
import model.army.TrainArmyTask;


import model.army.*;
import model.battle.*;

import model.building.Building;
import model.building.BuildingStatus;

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

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class TaskProcessor {

    private final Village village;
    private final AtomicBoolean processing = new AtomicBoolean(false);
    private long lastCloudNeutralization;
    private boolean firstCloudNeutralization = false;
    private boolean isBattleFinished;

    private void cloudNeutralization(){

        int totalAmount = PlantType.getTotalNeutralizationPower(this.village.getPlants());

        if(totalAmount <= this.village.getCloud().getRadiation())
            this.village.getCloud().setRadiation(this.village.getCloud().getRadiation() - totalAmount);
        else {
            this.village.getCloud().setRadiation(0);
        }

        this.firstCloudNeutralization = true;
        this.lastCloudNeutralization = System.currentTimeMillis();
    }

    public TaskProcessor(Village village) {
        this.village = village;
    }

    public void process(){

        if (!processing.compareAndSet(false, true)) {
            throw new IllegalStateException("TaskProcessor is running concurrently!");
        }

        if( (int) ((System.currentTimeMillis() - this.lastCloudNeutralization) / 1000) % 5 == 0  || !this.firstCloudNeutralization)
            this.cloudNeutralization();


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

            for (TaskResult task : taskResults) {


                village.getTimedOperation().putAll(task.getTasksToAdd());

                //village.getBuildings().putAll(task.getBuildingsToAdd());
                for (Building building : task.getBuildingsToAdd().values()) {
                    if (village.getGameMap().placeBuilding(building, building.getPosition().getX(), building.getPosition().getY())) {
                        village.getBuildings().put(building.getId(), building);
                    }
                }

                for (Plant plant : task.getPlantsToAdd().values()) {
                    if (village.getGameMap().placePlant(plant, plant.getPosition().getX(), plant.getPosition().getY())) {
                        village.getPlants().put(plant.getId(), plant);
                    }
                }

                for (UUID uuid : task.getTasksToRemove()) {
                    village.getTimedOperation().remove(uuid);
                }

                for (UUID uuid : task.getBuildingsToUpgrade()) {
                    Building building;
                    building = village.getBuildings().get(uuid);

                    if (building != null && building.getBuildingStatus() == BuildingStatus.UPGRADING) {
                        if (building instanceof Laboratory lab) {
                            lab.upgradeWithVillage(village);
                        } else {
                            task.getProductionBuildingsToReschedule().add(building.getId());
                            building.upgrade();
                        }
                    }
                }

                for (ResourcesType resourcesType : task.getResourcesToAdd().keySet()) {
                    resourcesManagement.addResource(task.getResourcesToAdd().get(resourcesType), resourcesType);
                }

                for (ResourcesType resourcesType : task.getResourcesToWithdraw().keySet()) {
                    resourcesManagement.withdrawResource(task.getResourcesToWithdraw().get(resourcesType), resourcesType);
                }

                for (UUID uuid : task.getProductionBuildingsToReschedule()) {
                    building1 = village.getBuildings().get(uuid);

                    if (building1 == null)
                        continue;

                    System.out.println(building1.getBuildingStatus());
                    if (building1.getBuildingStatus() == BuildingStatus.ACTIVE) {
                        ProductionTask nextTask = ProductionTaskFactory.buildProductionTask(building1);
                        if (nextTask != null) {
                            village.getTimedOperation().put(nextTask.getId(), nextTask);
                        }
                    }
                }

                for (EventType eventType : task.getEventType()) {
                    Event event = new Event(this.village);
                    if (eventType == EventType.STORM)
                        event.storm();
                    else if (eventType == EventType.DISEASE)
                        event.disease();
                    else
                        event.discovery();
                }


                for (AllianceRequest allianceRequest : task.getAllianceRequestsToApply()) {
                    if (!this.checkResearchCenterLevel(allianceRequest.getSender(), allianceRequest.getReceiver())) {
                        allianceRequest.setAllianceStatus(AllianceStatus.REJECTED);
                    } else {
                        allianceRequest.getReceiver().setAlliance(new Alliance(allianceRequest.getSender(), allianceRequest.getReceiver()));
                        allianceRequest.getSender().setAlliance(new Alliance(allianceRequest.getSender(), allianceRequest.getReceiver()));
                        this.applyAllianceBonus(allianceRequest);
                        System.out.println("SUCCESS");
                    }

                }

                for (TradeOffer tradeOffer : task.getTradeOffers()) {

                    if (this.checkTradeCondition(tradeOffer)) {
                        for (Map.Entry<ResourcesType, Integer> entry : tradeOffer.getRequestedResources().entrySet()) {
                            ResourcesType resource = entry.getKey();
                            int amount = entry.getValue();
                            tradeOffer.getSenderVillage().getResourcesManagement().addResource(amount, resource);
                        }

                        for (Map.Entry<ResourcesType, Integer> entry : tradeOffer.getOfferedResources().entrySet()) {
                            ResourcesType resource = entry.getKey();
                            int amount = entry.getValue();
                            tradeOffer.getReceiverVillage().getResourcesManagement().addResource(amount, resource);
                        }
                    }
                }


                //TrainArmy
                for (Map.Entry<ArmyType, Integer> entry : task.getTrainedArmiesToAdd().entrySet()) {
                    ArmyType trainedType = entry.getKey();
                    int count = entry.getValue();

                    village.getArmies().getArmyStorage().increaseArmy(trainedType, count);

                    village.getArmies().getArmyQueue().dequeue();

                    ArmyQueue queue = village.getArmies().getArmyQueue();

                    if (queue.isEmpty()) {
                        queue.setTraining(false);
                    } else {
                        QueuedArmy nextArmy = queue.peek();

                        Instant startTime = nextArmy.finishTime().minus(nextArmy.armyType().getTrainCost().neededTime());

                        TrainArmyTask nextTask = new TrainArmyTask(
                                startTime,
                                nextArmy.armyType().getTrainCost().neededTime(),
                                nextArmy.armyType()
                        );

                        queue.setTraining(true);

                        village.getTimedOperation().put(nextTask.getId(), nextTask);
                    }
                }

                //battle
                for (Map.Entry<UUID, BattleStatus> entry1 : task.getBattleStatusChange().entrySet()) {

                    System.out.println("BattleStatusChange = " + task.getBattleStatusChange());
                    System.out.println("ActiveBattles = " + village.getActiveBattles().keySet());

                    Battle battle = village.getActiveBattles().get(entry1.getKey());

                    if(battle == null)
                        continue;

                    BattleWinner winner = task.getBattleWinners().get(battle.getBattleId());

                    if(winner != null){
                        battle.setWinner(winner);
                        battle.setFinishedTime(Instant.now());
                    }

                    battle.setStatus(entry1.getValue());

                    //attacker arrival in the defender village
                    if(entry1.getValue() == BattleStatus.FIGHTING && battle.getAttackerVillage()==village){

                        Village defenderVillage = battle.getDefenderVillage();

                        defenderVillage.getLock().writeLock().lock();

                        try {

                            //creat BattleArmy defender
                            BattleArmy defenderArmy = new BattleArmy();

                            ArmyStorage armyStorage = battle.getDefenderVillage().getArmies().getArmyStorage();

                            for (ArmyType type : ArmyType.values()) {
                                int count = armyStorage.getArmyCount(type);

                                if (count == 0) continue;

                                defenderArmy.increaseArmy(type, count);

                                armyStorage.decreaseArmy(type, count);
                            }

                            battle.setDefenderArmy(defenderArmy);

                        }finally {
                            defenderVillage.getLock().writeLock().unlock();
                        }

                        //start battleTask (startBattle)
                        BattleTask battleTask = new BattleTask(
                                Instant.now(),
                                Duration.ofSeconds(20),
                                battle
                        );
                        battle.getAttackerVillage().getTimedOperation().put(battleTask.getId(), battleTask);

                        //the battle finished
                    }else if (entry1.getValue() == BattleStatus.RETURNING && battle.getAttackerVillage() == village){

                        ReturnFromBattleTask returnArmyTask = new ReturnFromBattleTask(
                                Instant.now(),
                                Duration.ofSeconds(battle.getTravelTime()),
                                battle,
                                task.getAttackerReturningArmies(),
                                task.getDefenderReturningArmies(),
                                task.getAttackerArmyLosses(),
                                task.getDefenderArmyLosses(),
                                task.getAttackerLoot(),
                                task.getDefenderResourceLoss(),
                                task.getVillageHealthChange()
                        );

                        village.getTimedOperation().put(returnArmyTask.getId(), returnArmyTask);

                        Village defenderVillage = battle.getDefenderVillage();

                        Integer damage = task.getVillageHealthChange()
                                .get(defenderVillage.getVillageId());

                        if (damage != null) {
                            defenderVillage.decreaseHealth(damage);
                        }

                        ResourcesManagement defenderResourceManagement =
                                new ResourcesManagement(defenderVillage);

                        for (Map.Entry<ResourcesType, Integer> resource :
                                task.getDefenderResourceLoss().entrySet()) {

                            defenderResourceManagement.withdrawResource(
                                    resource.getValue(),
                                    resource.getKey()
                            );
                        }

                        //return from battle
                    }else if(entry1.getValue() == BattleStatus.FINISHED && battle.getAttackerVillage() == village){

                        for (Map.Entry<ArmyType, Integer> army : task.getAttackerReturningArmies().entrySet()) {

                            village.getArmies().getArmyStorage().increaseArmy(army.getKey(), army.getValue());
                        }

                        for (Map.Entry<ResourcesType, Integer> loot :
                                task.getAttackerLoot().entrySet()) {

                            resourcesManagement.addResource(loot.getValue(), loot.getKey());
                        }

                        Village defenderVillage = battle.getDefenderVillage();

                        defenderVillage.getLock().writeLock().lock();

                        try {

                            ArmyStorage armyStorage = battle.getDefenderVillage().getArmies().getArmyStorage();


                            for (Map.Entry<ArmyType, Integer> army : task.getDefenderReturningArmies().entrySet()) {
                                armyStorage.increaseArmy(army.getKey(), army.getValue());
                            }
                        }finally {
                            defenderVillage.getLock().writeLock().unlock();
                        }
                        System.out.println("===========================");
                        System.out.println(task.getAttackerArmyLosses());
                        System.out.println(task.getDefenderArmyLosses());
                        System.out.println("===========================");


                        BattleHistory history = new BattleHistory(
                                battle,
                                task.getAttackerArmyLosses(),
                                task.getDefenderArmyLosses(),
                                task.getAttackerLoot(),
                                battle.getDefenderUsername(),
                                battle.getAttackerUsername());

                        /*battle.getAttackerVillage().getBattleHistory().put(history.getBattleId(), history);
                        battle.getDefenderVillage().getBattleHistory().put(history.getBattleId(), history);*/
                        battle.getAttackerVillage().getBattleHistory().add(history);
                        battle.getDefenderVillage().getBattleHistory().add(history);

                        battle.getAttackerVillage().getActiveBattles().remove(battle.getBattleId());
                        battle.getDefenderVillage().getActiveBattles().remove(battle.getBattleId());

                        this.isBattleFinished =true;
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
        this.isBattleFinished = false;
    }



    public Village getVillage() {
        return village;
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

        for (Plant plant : allianceRequest.getSender().getVillage().getPlants().values()) {
            plant.upgradeNeutralizationPower();
        }
        for (Plant plant : allianceRequest.getReceiver().getVillage().getPlants().values()) {
            plant.upgradeNeutralizationPower();
        }

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

    public boolean isBattleFinished() {
        return isBattleFinished;
    }

    public void setBattleFinished(boolean battleFinished) {
        isBattleFinished = battleFinished;
    }
}


