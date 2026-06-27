package service.alliance;

import model.building.Building;
import model.building.BuildingType;
import model.building.Cost;
import model.building.ResearchCenter;
import model.player.Player;

public class AllianceService {

    private AllianceRequestRepository allianceRequestRepository;
    private AllianceRepository allianceRepository;

    public AllianceService(AllianceRequestRepository allianceRequestRepository, AllianceRepository allianceRepository) {
        this.allianceRequestRepository = allianceRequestRepository;
        this.allianceRepository = allianceRepository;
    }

    public void sendRequest(Player sender, Player receiver){

        if(this.hasAlliance(sender, receiver) || sender == receiver || isRequestAlreadyExist(sender, receiver))
            return;

        AllianceRequest allianceRequest = new AllianceRequest(sender, receiver);
        allianceRequestRepository.saveAllianceRequest(allianceRequest);
    }

    public boolean isRequestAlreadyExist(Player sender, Player receiver){
        for (AllianceRequest allianceRequest : allianceRequestRepository.getAllAllianceRequests().values()){
            /*if(sender == allianceRequest.getSender() && receiver == allianceRequest.getReceiver())
                return true;*/
            if ((sender == allianceRequest.getSender() && receiver == allianceRequest.getReceiver()) ||
                    (sender == allianceRequest.getReceiver() && receiver == allianceRequest.getSender()))
                    return true;
        }
        return false;
    }

    public void acceptRequest(AllianceRequest allianceRequest){

        if(this.checkResearchCenterLevel(allianceRequest.getSender(), allianceRequest.getReceiver())
           && this.checkMajorBuildingLevel(allianceRequest.getSender(), allianceRequest.getReceiver())
            && this.checkAllianceCost(allianceRequest.getSender(), allianceRequest.getReceiver())){

            allianceRequest.getReceiver().getVillage().getResourcesManagement().withdrawResourcesCost(Cost.allianceCost());
            allianceRequest.getSender().getVillage().getResourcesManagement().withdrawResourcesCost(Cost.allianceCost());

            this.increaseScienceLevel(allianceRequest.getSender(), allianceRequest.getReceiver());

            Alliance alliance = new Alliance(allianceRequest.getSender(), allianceRequest.getReceiver());

            allianceRepository.saveAlliance(alliance);

            allianceRequest.setAllianceStatus(AllianceStatus.ACCEPTED);
            allianceRequestRepository.removeAllianceRequest(allianceRequest);

            allianceRequest.getReceiver().setAlliance(alliance);
            allianceRequest.getSender().setAlliance(alliance);
        }else {
            // Throw an exception that explains that alliance can not be formed
        }


    }

    public void rejectRequest(AllianceRequest allianceRequest){
        allianceRequest.setAllianceStatus(AllianceStatus.REJECTED);
        allianceRequestRepository.removeAllianceRequest(allianceRequest);
    }

    public boolean checkAllianceCost(Player senderPlayer, Player receiverPlayer){
        if(receiverPlayer.getVillage().getResourcesManagement().checkResourcesCost(Cost.allianceCost())
                && senderPlayer.getVillage().getResourcesManagement().checkResourcesCost(Cost.allianceCost())){

            return true;
        }
        return false;
    }

    public boolean checkResearchCenterLevel(Player senderPlayer, Player receiverPlayer){

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

        if(receiverPlayerResearchCenter.getLevel() >= 2 && senderPlayerResearchCenter.getLevel() >= 2)
            return true;

        return false;
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

        if(senderPlayerMajorBuilding.getLevel() >= 2 && receiverPlayerMajorBuilding.getLevel() >= 2)
            return true;

        return false;
    }

    public boolean hasAlliance(Player senderPlayer, Player receiverPlayer){
        if(receiverPlayer.getAlliance() == null && senderPlayer.getAlliance() == null)
            return false;
        return true;
    }

    public void increaseScienceLevel(Player sender, Player receiver){

        if(sender.getAllianceCounts() == 0 ){
            for (Building building : sender.getVillage().getBuildings().values()) {

                if (building instanceof ResearchCenter researchCenter) {

                    researchCenter.setScienceLevel(researchCenter.getScienceLevel() + 1);

                    break;
                }
            }
        }

        if(receiver.getAllianceCounts() == 0){
            for (Building building : receiver.getVillage().getBuildings().values()) {

                if (building instanceof ResearchCenter researchCenter) {
                    
                    researchCenter.setScienceLevel(researchCenter.getScienceLevel() + 1);

                    break;
                }
            }
        }
    }
}
