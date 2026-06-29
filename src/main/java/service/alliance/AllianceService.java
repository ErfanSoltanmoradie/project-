package service.alliance;

import model.building.*;
import model.player.Player;
import model.time.AllianceTask;

import java.time.Duration;
import java.time.Instant;

public class AllianceService {

    public void sendRequest(Player sender, Player receiver){

        if(this.hasAlliance(sender, receiver) || sender.getPlayerId().equals(receiver.getPlayerId()) || isRequestAlreadyExist(sender, receiver))
            return;
       if(sender.getVillage().getCloud().getNeutralized()<200 ||  receiver.getVillage().getCloud().getNeutralized()<200) return;

        if(!this.checkMajorBuildingLevel(sender, receiver)
                || !this.checkAllianceCost(sender, receiver)){
            return;
        }

        AllianceRequest allianceRequest = new AllianceRequest(sender, receiver);
        sender.addPendingRequest(allianceRequest);
        receiver.addPendingRequest(allianceRequest);
    }

    public void acceptRequest(AllianceRequest allianceRequest){
        if(allianceRequest==null) return;
        if(allianceRequest.getSender().getVillage().getCloud().getNeutralized()<200 ||  allianceRequest.getReceiver().getVillage().getCloud().getNeutralized()<200)
            return;
        if(this.checkMajorBuildingLevel(allianceRequest.getSender(), allianceRequest.getReceiver())
                && this.checkAllianceCost(allianceRequest.getSender(), allianceRequest.getReceiver())){

            allianceRequest.getReceiver().getVillage().getResourcesManagement().withdrawResourcesCost(Cost.allianceCost());
            allianceRequest.getSender().getVillage().getResourcesManagement().withdrawResourcesCost(Cost.allianceCost());

            Duration transferTime=Duration.ofHours(1);
            AllianceTask allianceTask=new AllianceTask(Instant.now(),Instant.now().plus(transferTime),allianceRequest);

            allianceRequest.getSender().removePendingRequest(allianceRequest);
            allianceRequest.getReceiver().removePendingRequest(allianceRequest);

            allianceRequest.getSender().getVillage().getTimedOperation().put(allianceTask.getId(),allianceTask);
        }else {
            rejectRequest(allianceRequest);
        }


    }

    public void rejectRequest(AllianceRequest allianceRequest){
        if (allianceRequest != null) {
            allianceRequest.setAllianceStatus(AllianceStatus.REJECTED);
            if(allianceRequest.getSender() != null) allianceRequest.getSender().removePendingRequest(allianceRequest);
            if(allianceRequest.getReceiver() != null) allianceRequest.getReceiver().removePendingRequest(allianceRequest);
        }

    }

    public boolean checkAllianceCost(Player senderPlayer, Player receiverPlayer){
        if(receiverPlayer.getVillage().getResourcesManagement().checkResourcesCost(Cost.allianceCost())
                && senderPlayer.getVillage().getResourcesManagement().checkResourcesCost(Cost.allianceCost())){

            return true;
        }
        return false;
    }

    public boolean isRequestAlreadyExist(Player sender, Player receiver) {
        for (AllianceRequest request : sender.getPendingRequests()) {
            if (request.getReceiver().getPlayerId().equals(receiver.getPlayerId())) {
                return true;
            }
        }
        for (AllianceRequest request : receiver.getPendingRequests()) {
            if (request.getReceiver().getPlayerId().equals(sender.getPlayerId())) {
                return true;
            }
        }
        return false;
    }
    public boolean hasAlliance(Player senderPlayer, Player receiverPlayer){
        if(receiverPlayer.getAlliance() == null && senderPlayer.getAlliance() == null)
            return false;
        return true;
    }

    public boolean checkMajorBuildingLevel(Player senderPlayer, Player receiverPlayer){
        Building receiverPlayerMajorBuilding= null;
        Building senderPlayerMajorBuilding = null;

        for(Building building : receiverPlayer.getVillage().getBuildings().values()){
            if(building.getType() == BuildingType.MAJOR_BUILDING){
                receiverPlayerMajorBuilding = building;
                break;
            }
        }

        for(Building building : senderPlayer.getVillage().getBuildings().values()){
            if(building.getType() == BuildingType.MAJOR_BUILDING){
                senderPlayerMajorBuilding = building;
                break;
            }
        }

        if (receiverPlayerMajorBuilding == null || senderPlayerMajorBuilding == null)
            return false;

        if(senderPlayerMajorBuilding.getLevel() >= 2 && receiverPlayerMajorBuilding.getLevel() >= 2)
            return true;

        return false;
    }


}
