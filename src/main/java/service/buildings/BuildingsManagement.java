package service.buildings;


import model.building.*;
import model.player.Player;
import model.resources.Resources;
import model.time.BuildTask;
import model.time.UpgradeTask;
import model.village.Village;
import model.world.Coordinate;
import service.resource.ResourcesManagement;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public class BuildingsManagement {

   private final Village village;
   private final ResourcesManagement resources;

    public BuildingsManagement(Player player) {
        this.village = player.getVillage();
        this.resources = village.getResourcesManagement();
    }

    public void build(BuildingType buildingType, Coordinate coordinate){
        Cost cost = Cost.buildCost(buildingType);

        if(resources.checkResourcesCost(cost)){
            resources.withdrawResourcesCost(cost);
            //Build and add a task
            BuildTask buildTask = new BuildTask(Instant.now(),
                    Instant.now().plus(cost.getNeededTime()), buildingType, coordinate);
            village.getTimedOperation().put(buildTask.getId(), buildTask);
        } else{
            return;
        }
    }
    public void buildPlant(PlantType plantType, Coordinate coor){
        Laboratory laboratory=null;
        for(Building building1 : village.getBuildings().values()){
            if(building1 instanceof Laboratory lab){
                   laboratory = lab;
                   break;
            }
        }
        if(laboratory==null){return;}
        if(laboratory.getLevel()<plantType.getRequiredLaboratoryLevel()){return;}
        Cost cost = Cost.buildCost(plantType);

        if(resources.checkResourcesCost(cost)){
            resources.withdrawResourcesCost(cost);
            BuildTask buildTask=new BuildTask(Instant.now(),
                    Instant.now().plus(cost.getNeededTime()), plantType,coor);
            village.getTimedOperation().put(buildTask.getId(), buildTask);
        }else
            return;
    }

    public void upgrade(Building building){

        Cost cost = Cost.upgradeCost(building);

        if(resources.checkResourcesCost(cost)){

            resources.withdrawResourcesCost(cost);
            building.setBuildingStatus(BuildingStatus.UPGRADING);

            UpgradeTask upgradeTask = new UpgradeTask(Instant.now(),
                    Instant.now().plus(cost.getNeededTime()), building.getId());

            village.getTimedOperation().put(upgradeTask.getId(), upgradeTask);
        }else{
            return;
        }
    }


    public void removePlant(UUID plantId){
        village.getPlant().remove(plantId);
    }

    public int getTotalNeutralizationPower(){
        return PlantType.getTotalNeutralizationPower(village.getPlant());
    }
}
