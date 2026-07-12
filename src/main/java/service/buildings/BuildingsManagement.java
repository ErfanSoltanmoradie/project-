package service.buildings;

import model.building.*;
import model.time.BuildTask;
import model.time.UpgradeTask;
import model.village.Village;
import model.world.Coordinate;
import service.resource.ResourcesManagement;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

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

    public void buildPlant(PlantType plantType, Coordinate coor){
        village.getLock().writeLock().lock();
        try {
            Laboratory laboratory=null;
            for(Building building : village.getBuildings().values()){
                if(building instanceof Laboratory lab){
                    laboratory = lab;
                    break;
                }
            }
            if(laboratory==null){return;}
            if(laboratory.getLevel() < plantType.getRequiredLaboratoryLevel()){return;}

            Cost cost = Cost.buildCost(plantType);

            if(resources.checkResourcesCost(cost)){
                resources.withdrawResourcesCost(cost);
                BuildTask buildTask=new BuildTask(Instant.now(),
                        Duration.ofSeconds(5), plantType,coor, laboratory.getLevel());

                village.getTimedOperation().put(buildTask.getId(), buildTask);
            }
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

        }finally {
            village.getLock().writeLock().unlock();
        }
    }

    public void removePlant(UUID plantId){
        village.getPlants().remove(plantId);
    }

    /*public int getTotalNeutralizationPower(){
        return PlantType.getTotalNeutralizationPower(village.getPlant());
    }*/
}
