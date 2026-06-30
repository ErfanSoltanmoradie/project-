package service.buildings;

import model.building.Building;
import model.building.BuildingStatus;
import model.building.Cost;
import model.building.BuildingType;
import model.time.BuildTask;
import model.time.UpgradeTask;
import model.village.Village;
import model.world.Coordinate;
import service.resource.ResourcesManagement;
import java.time.Instant;

public class BuildingsManagement{

   private final Village village;
   private final ResourcesManagement resources;

    public BuildingsManagement(Village village) {
        this.village = village;
        this.resources = village.getResourcesManagement();
    }

    public void build(BuildingType buildingType, Coordinate coordinate){

        village.getLock().writeLock().lock();
        try {
            Cost cost = Cost.buildCost(buildingType);

            /*if(resources.checkResourcesCost(cost)){
                resources.withdrawResourcesCost(cost);
                //Build and add a task
                BuildTask buildTask = new BuildTask(Instant.now(),
                        cost.getNeededTime(), buildingType, coordinate);
                village.getTimedOperation().put(buildTask.getId(), buildTask);
            } else{
                return;
            }*/
            if(!resources.checkResourcesCost(cost)) return;
            resources.withdrawResourcesCost(cost);
            //Build and add a task
            BuildTask buildTask = new BuildTask(Instant.now(),
                    cost.getNeededTime(), buildingType, coordinate);
            village.getTimedOperation().put(buildTask.getId(), buildTask);

        }finally {
            village.getLock().writeLock().unlock();
        }
    }

    public void upgrade(Building building){

        village.getLock().writeLock().lock();
        try {
            Cost cost = Cost.upgradeCost(building);

            if (!resources.checkResourcesCost(cost)) return;

            resources.withdrawResourcesCost(cost);
            building.setBuildingStatus(BuildingStatus.UPGRADING);

            UpgradeTask upgradeTask = new UpgradeTask(Instant.now(),
                    cost.getNeededTime(), building.getId());

            village.getTimedOperation().put(upgradeTask.getId(), upgradeTask);
            /*if (resources.checkResourcesCost(cost)) {

                resources.withdrawResourcesCost(cost);
                building.setBuildingStatus(BuildingStatus.UPGRADING);

                UpgradeTask upgradeTask = new UpgradeTask(Instant.now(),
                        cost.getNeededTime(), building.getId());

                village.getTimedOperation().put(upgradeTask.getId(), upgradeTask);
            } else {
                return;
            }*/
        }finally {
            village.getLock().writeLock().unlock();
        }
    }
}
