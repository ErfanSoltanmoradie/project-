package model.time;

import model.building.Building;
import model.building.BuildingType;
import model.building.Plant;
import model.building.ResearchCenter;
import model.player.Player;
import model.village.Village;
import service.alliance.Alliance;
import service.alliance.AllianceRequest;
import service.alliance.AllianceStatus;

import java.time.Instant;
import java.util.List;

public class AllianceTask extends TimedOperation{
    private final AllianceRequest allianceRequest;
    private Alliance alliance;
    public AllianceTask(Instant start, Instant end,AllianceRequest allianceRequest) {
        super(start,end,TimedOperationType.ALLIANCE_TASK);
        this.allianceRequest=allianceRequest;
        this.alliance = new Alliance(allianceRequest.getSender(), allianceRequest.getReceiver());

    }
    @Override
    public void execute(Village village, List<TimedOperation> toAdd) {
        if (!this.checkResearchCenterLevel(allianceRequest.getSender(), allianceRequest.getReceiver())) {
            allianceRequest.setAllianceStatus(AllianceStatus.REJECTED);
            return;
        }
        allianceRequest.getSender().setAlliance(alliance);
        allianceRequest.getReceiver().setAlliance(alliance);

        if(allianceRequest.getSender().getAllianceCounts() == 0 ){
            for (Building building : allianceRequest.getSender().getVillage().getBuildings().values()) {
                if (building instanceof ResearchCenter researchCenter) {
                    researchCenter.setScienceLevel(researchCenter.getScienceLevel() + 1);
                    break;
                }
            }
        }

        if(allianceRequest.getReceiver().getAllianceCounts() == 0){
            for (Building building : allianceRequest.getReceiver().getVillage().getBuildings().values()) {
                if (building instanceof ResearchCenter researchCenter) {
                    researchCenter.setScienceLevel(researchCenter.getScienceLevel() + 1);
                    break;
                }
            }
        }

        allianceRequest.getSender().setAllianceCounts(allianceRequest.getSender().getAllianceCounts() + 1);
        allianceRequest.getReceiver().setAllianceCounts(allianceRequest.getReceiver().getAllianceCounts() + 1);
        allianceRequest.setAllianceStatus(AllianceStatus.ACCEPTED);
        for (Plant plant : allianceRequest.getSender().getVillage().getPlant().values()) {
            plant.upgradeNeutralizationPower();
        }
        for (Plant plant : allianceRequest.getReceiver().getVillage().getPlant().values()) {
            plant.upgradeNeutralizationPower();
        }


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

        if (receiverPlayerResearchCenter == null || senderPlayerResearchCenter == null)
            return false;

        if(receiverPlayerResearchCenter.getLevel() >= 2 && senderPlayerResearchCenter.getLevel() >= 2)
            return true;

        return false;
    }
}
