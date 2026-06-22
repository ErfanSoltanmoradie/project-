package model.gameengine;

import model.building.Building;
import model.building.BuildingStatus;
import model.building.BuildingType;
import model.building.StorageBuilding;
import model.player.Player;
import model.resources.Resources;
import model.resources.ResourcesType;
import model.time.TaskProcessor;
import model.time.TimedOperation;
import model.time.TimedOperationType;
import model.time.UpgradeTask;
import model.village.Village;
import model.world.Coordinate;
import service.buildings.BuildingsManagement;

import java.time.Duration;
import java.time.Instant;

public class GameEngine {

    public static void main(String[] args) throws Exception {

        Coordinate coordinate = new Coordinate(10, 10);
        Village village = new Village(coordinate, 5000);
        Player player = new Player("Erfan", "1234", village);
        TaskProcessor taskProcessor = new TaskProcessor(village);

        StorageBuilding soilStorage =  new StorageBuilding(BuildingType.SOIL_STORAGE, coordinate, 2000);
        StorageBuilding waterStorage = new StorageBuilding(BuildingType.WATER_STORAGE, coordinate, 2000);
        StorageBuilding woodStorage = new StorageBuilding(BuildingType.WOOD_STORAGE,coordinate, 2000);
        StorageBuilding  ironStorage =  new StorageBuilding(BuildingType.IRON_STORAGE, coordinate, 2000);
        StorageBuilding  stoneStorage = new StorageBuilding(BuildingType.STONE_STORAGE, coordinate, 2000);
        StorageBuilding gunpowderStorage = new StorageBuilding(BuildingType.GUNPOWDER_STORAGE, coordinate, 2000);

        soilStorage.setBuildingStatus(BuildingStatus.ACTIVE);
        waterStorage.setBuildingStatus(BuildingStatus.ACTIVE);
        woodStorage.setBuildingStatus(BuildingStatus.ACTIVE);
        ironStorage.setBuildingStatus(BuildingStatus.ACTIVE);
        stoneStorage.setBuildingStatus(BuildingStatus.ACTIVE);
        gunpowderStorage.setBuildingStatus(BuildingStatus.ACTIVE);

        village.getBuildings().put(soilStorage.getId(), soilStorage);
        village.getBuildings().put(waterStorage.getId(), waterStorage);
        village.getBuildings().put(woodStorage.getId(), woodStorage);
        village.getBuildings().put(ironStorage.getId(), ironStorage);
        village.getBuildings().put(stoneStorage.getId(), stoneStorage);
        village.getBuildings().put(gunpowderStorage.getId(), gunpowderStorage);

        BuildingsManagement buildingsManagement = new BuildingsManagement(player);

        System.out.println("Resources before buying buildings:");
        System.out.println("Wood: " + village.getResources().getAmount(ResourcesType.WOOD));
        System.out.println("IRON: " + village.getResources().getAmount(ResourcesType.IRON));
        System.out.println("Clean water: " + village.getResources().getAmount(ResourcesType.CLEAN_WATER));
        System.out.println("CLEAN SOIL: " + village.getResources().getAmount(ResourcesType.CLEAN_SOIL));
        System.out.println("GUNPOWDER: " + village.getResources().getAmount(ResourcesType.GUN_POWDER));
        System.out.println("DIRTY SOIL: " + village.getResources().getAmount(ResourcesType.DIRTY_SOIL));
        System.out.println("DIRTY WATER: " + village.getResources().getAmount(ResourcesType.DIRTY_WATER));
        System.out.println("STONE: " + village.getResources().getAmount(ResourcesType.STONE));


        buildingsManagement.build(BuildingType.STONE_MINE, coordinate);
        buildingsManagement.build(BuildingType.IRON_MINE, coordinate);
        buildingsManagement.build(BuildingType.WATER_PURIFIER, coordinate);
        buildingsManagement.build(BuildingType.SOIL_PURIFIER, coordinate);
        buildingsManagement.build(BuildingType.GUNPOWDER_MINE, coordinate);
        buildingsManagement.build(BuildingType.WOOD_MINE, coordinate);


        System.out.println("Resources after buying buildings:");
        System.out.println("Wood: " + village.getResources().getAmount(ResourcesType.WOOD));
        System.out.println("IRON: " + village.getResources().getAmount(ResourcesType.IRON));
        System.out.println("Clean water: " + village.getResources().getAmount(ResourcesType.CLEAN_WATER));
        System.out.println("CLEAN SOIL: " + village.getResources().getAmount(ResourcesType.CLEAN_SOIL));
        System.out.println("GUNPOWDER: " + village.getResources().getAmount(ResourcesType.GUN_POWDER));
        System.out.println("DIRTY SOIL: " + village.getResources().getAmount(ResourcesType.DIRTY_SOIL));
        System.out.println("DIRTY WATER: " + village.getResources().getAmount(ResourcesType.DIRTY_WATER));
        System.out.println("STONE: " + village.getResources().getAmount(ResourcesType.STONE));


        for (TimedOperation task : village.getTimedOperation().values()) {
            task.setFinishTime(Instant.now().minusSeconds(1));
        }

        taskProcessor.process();

        System.out.println("\nBuildings:");

        for (Building building : village.getBuildings().values()) {
            System.out.println(
                    building.getType()
                            + " | level=" + building.getLevel()
                            + " | status=" + building.getBuildingStatus()
            );
        }

        System.out.println(
                "Timed Operations Count: "
                        + village.getTimedOperation().size()
        );



        System.out.println("\n*********** PRODUCTION TEST ************");

        for (int i = 0; i < 20; i++) {

            for (TimedOperation task : village.getTimedOperation().values()) {
                task.setFinishTime(Instant.now().minusSeconds(1));
            }

            taskProcessor.process();
        }

        System.out.println("\n************ AFTER PRODUCTION ***********");

        System.out.println("WOOD: " + village.getResources().getAmount(ResourcesType.WOOD));
        System.out.println("IRON: " + village.getResources().getAmount(ResourcesType.IRON));
        System.out.println("STONE: " + village.getResources().getAmount(ResourcesType.STONE));
        System.out.println("DIRTY WATER: " + village.getResources().getAmount(ResourcesType.DIRTY_WATER));
        System.out.println("DIRTY SOIL: " + village.getResources().getAmount(ResourcesType.DIRTY_SOIL));
        System.out.println("CLEAN WATER: " + village.getResources().getAmount(ResourcesType.CLEAN_WATER));
        System.out.println("CLEAN SOIL: " + village.getResources().getAmount(ResourcesType.CLEAN_SOIL));
        System.out.println("GUNPOWDER: " + village.getResources().getAmount(ResourcesType.GUN_POWDER));

        
        System.out.println("\n************* TASK CHECK***************");
        System.out.println("Remaining Tasks: " + village.getTimedOperation().size());

    }
}