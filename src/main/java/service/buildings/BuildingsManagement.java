package service.buildings;

import model.building.*;
import model.player.Player;
import model.time.BuildGlobalTowerTask;
import model.time.BuildTask;
import model.time.TimedOperation;
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

    /*private int getMaxAllowedCount(BuildingType type) {
        return switch (type){
            case WOOD_MINE, STONE_MINE, IRON_MINE -> 5;
            case GUNPOWDER_MINE, DIRTY_WATER_MINE, DIRTY_SOIL_MINE,
                 WOOD_STORAGE, STONE_STORAGE, IRON_STORAGE, GUNPOWDER_STORAGE -> 4;
            case WATER_PURIFIER, SOIL_PURIFIER,
                 WATER_STORAGE, SOIL_STORAGE -> 3;
            case BALLISTA_DEFENSIVE -> 8;
            case CATAPULT_DEFENSIVE , SENTINEL_DEFENSIVE -> 6;
            case MAJOR_BUILDING, RESEARCH_CENTER, CUSTOMHOUSE , LABORATORY, BARRACKS, ARMY_PRODUCER-> 1;
            default -> Integer.MAX_VALUE;
        };
    }*/

    private int getCurrentBuildingCount(BuildingType type) {
        int count = 0;
        for (Building building : village.getBuildings().values()) {
            if (building.getType() == type) {
                count++;
            }
        }

        for (TimedOperation operation : village.getTimedOperation().values()) {
            if (operation instanceof BuildTask buildTask && buildTask.getBuildingType() == type) {
                count++;
            }
        }
        return count;
    }

    public void build(BuildingType buildingType, Coordinate coordinate){
        village.getLock().writeLock().lock();
        try {
            int currentCount = getCurrentBuildingCount(buildingType);
            /*int maxAllowed = getMaxAllowedCount(buildingType);

            if(currentCount >= maxAllowed){
                throw new BuildingLimitExceededException(buildingType, maxAllowed);
            }*/

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

    public boolean canBuildGlobalTower(){
        if (village.getGlobalTower() != null && village.getGlobalTower().isActive()) {
            return false;
        }

        for (TimedOperation operation : village.getTimedOperation().values()) {
            if (operation instanceof BuildGlobalTowerTask) {
                return false;
            }
        }

        int majorBuildingLevel=0;
        int researchCenterBuildingLevel=0;

        for(Building b : village.getBuildings().values()){
            if(b instanceof MajorBuilding) majorBuildingLevel=b.getLevel();
            if(b instanceof ResearchCenter) researchCenterBuildingLevel=b.getLevel();
        }
        if(majorBuildingLevel<5 || researchCenterBuildingLevel<5) return false;

        Cost towerCost = getGlobalTowerCost();
        return resources.checkResourcesCost(towerCost);
    }

    private Cost getGlobalTowerCost(){
        return new Cost(25000, 18000, 12000, 8000, 5000, 5000, 0, Duration.ofSeconds(10));
    }

    public void buildGlobalTower(Coordinate coordinate){
        village.getLock().writeLock().lock();
        try {
            if(!canBuildGlobalTower()) throw new IllegalStateException("Cannot build global tower");
            Cost towerCost = getGlobalTowerCost();
            resources.withdrawResourcesCost(towerCost);
            BuildGlobalTowerTask towerTask = new BuildGlobalTowerTask(Instant.now(), Duration.ofSeconds(10), village, coordinate);
            village.getTimedOperation().put(towerTask.getId(), towerTask);
        } finally {
            village.getLock().writeLock().unlock();
        }
    }
}