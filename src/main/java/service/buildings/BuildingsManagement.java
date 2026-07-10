package service.buildings;

import model.building.*;
import model.player.Player;
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


    public static boolean checkMajorBuildingForTrade(Player player){
        player.getLock().readLock().lock();
        try {
            player.getVillage().getLock().readLock().lock();
            try {
                for (Building building : player.getVillage().getBuildings().values()){
                    if(building instanceof MajorBuilding){
                        if(building.getLevel() >= 3)
                            return true;
                    }
                }
            }finally {
                player.getVillage().getLock().readLock().unlock();
            }
        }finally {
            player.getLock().readLock().unlock();
        }
        return false;
    }

    public static boolean checkResearchCenterBuildingForTrade(Player player){
        player.getLock().readLock().lock();
        try {
            player.getVillage().getLock().readLock().lock();
            try {
                for (Building building : player.getVillage().getBuildings().values()){
                    if(building instanceof ResearchCenter){
                        if(building.getLevel() >= 2)
                            return true;
                    }
                }
            }finally {
                player.getVillage().getLock().readLock().unlock();
            }
        }finally {
            player.getLock().readLock().unlock();
        }
        return false;
    }

    public static boolean checkCustomHouseBuildingForTrade(Player player){
        player.getLock().readLock().lock();
        try {
            player.getVillage().getLock().readLock().lock();
            try {
                for (Building building : player.getVillage().getBuildings().values()){
                    if(building instanceof Customhouse){
                        return true;
                    }
                }
            }finally {
                player.getVillage().getLock().readLock().unlock();
            }
        }finally {
            player.getLock().readLock().unlock();
        }
        return false;
    }

    /*public int getTotalNeutralizationPower(){
        return PlantType.getTotalNeutralizationPower(village.getPlant());
    }*/
}
