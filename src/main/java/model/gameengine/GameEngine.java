package model.gameengine;

import model.building.Building;
import model.building.BuildingType;
import model.player.Player;
import model.resources.Resources;
import model.resources.ResourcesType;
import model.time.TaskProcessor;
import model.time.TimedOperation;
import model.time.TimedOperationType;
import model.village.Village;
import model.world.Coordinate;
import service.buildings.BuildingsManagement;

import java.time.Duration;
import java.time.Instant;

public class GameEngine {

    public static void main(String[] args) throws Exception {

        Village village = new Village(new Coordinate(0, 0), 100);
        Player player= new Player("erfan", "123", village);
        BuildingsManagement buildingsManagement =
                new BuildingsManagement(player);

        TaskProcessor taskProcessor = new TaskProcessor(village);

        System.out.println("========== PURIFICATION TEST START ==========");


        BuildingsManagement bm = new BuildingsManagement(player);

        bm.build(BuildingType.WATER_STORAGE , new Coordinate(80, 10));
        bm.build(BuildingType.SOIL_STORAGE, new Coordinate(20, 30));
        bm.build(BuildingType.WATER_PURIFIER, new Coordinate(1,1));
        bm.build(BuildingType.SOIL_PURIFIER, new Coordinate(2,1));



        for (int i = 0; i < 10; i++) {
            Thread.sleep(1000);
            taskProcessor.process();
        }


        village.getResources().addResource(ResourcesType.DIRTY_WATER, 500, 5000);
        village.getResources().addResource(ResourcesType.DIRTY_SOIL, 500, 5000);

        System.out.println("Before production:");
        System.out.println("DIRTY WATER = " + village.getResources().getAmount(ResourcesType.DIRTY_WATER));
        System.out.println("CLEAN WATER = " + village.getResources().getAmount(ResourcesType.CLEAN_WATER));
        System.out.println("DIRTY SOIL  = " + village.getResources().getAmount(ResourcesType.DIRTY_SOIL));
        System.out.println("CLEAN SOIL  = " + village.getResources().getAmount(ResourcesType.CLEAN_SOIL));


        for (int i = 0; i < 50; i++) {

            Thread.sleep(1000);
            for (TimedOperation task : village.getTimedOperation().values()) {
                task.setFinishTime(Instant.now().minusSeconds(1));
            }

            taskProcessor.process();
        }


        System.out.println("\nAfter production:");
        System.out.println("DIRTY WATER = " + village.getResources().getAmount(ResourcesType.DIRTY_WATER));
        System.out.println("CLEAN WATER = " + village.getResources().getAmount(ResourcesType.CLEAN_WATER));
        System.out.println("DIRTY SOIL  = " + village.getResources().getAmount(ResourcesType.DIRTY_SOIL));
        System.out.println("CLEAN SOIL  = " + village.getResources().getAmount(ResourcesType.CLEAN_SOIL));

        System.out.println("========== PURIFICATION TEST END ==========");


    }

    private static void printVillage(Village village) {

        Resources r = village.getResources();

        System.out.println("WOOD        = "
                + r.getAmount(ResourcesType.WOOD));

        System.out.println("IRON        = "
                + r.getAmount(ResourcesType.IRON));

        System.out.println("STONE       = "
                + r.getAmount(ResourcesType.STONE));

        System.out.println("GUNPOWDER   = "
                + r.getAmount(ResourcesType.GUN_POWDER));

        System.out.println("DIRTY WATER = "
                + r.getAmount(ResourcesType.DIRTY_WATER));

        System.out.println("DIRTY SOIL  = "
                + r.getAmount(ResourcesType.DIRTY_SOIL));

        System.out.println("CLEAN WATER = "
                + r.getAmount(ResourcesType.CLEAN_WATER));

        System.out.println("CLEAN SOIL  = "
                + r.getAmount(ResourcesType.CLEAN_SOIL));

        System.out.println("COIN        = "
                + r.getAmount(ResourcesType.COIN));
    }
}