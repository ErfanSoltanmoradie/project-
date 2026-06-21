package service.buildings;


import model.building.Building;
import model.building.BuildingStatus;
import model.building.Cost;
import model.building.BuildingType;
import model.player.Player;
import model.resources.Resources;
import model.time.BuildTask;
import model.time.UpgradeTask;
import model.village.Village;
import model.world.Coordinate;
import service.resource.ResourcesManagement;

import java.time.Instant;

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
}
