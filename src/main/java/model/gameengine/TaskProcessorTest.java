package model.gameengine;

import model.building.*;
import model.resources.Resources;
import model.resources.ResourcesType;
import model.time.*;
import model.village.Village;
import model.world.Coordinate;
import service.buildings.BuildingsManagement;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class TaskProcessorTest {

    public static void main(String[] args) throws InterruptedException {

        Village village = new Village(new Coordinate(0, 0), 1000);

        village.runTimeServices();

        TaskProcessor processor = new TaskProcessor(village);

        BuildingsManagement bm = new BuildingsManagement(village);


        Building building6 = new StorageBuilding(BuildingType.SOIL_STORAGE, null, 2000);
        Building building1 = new StorageBuilding(BuildingType.WATER_STORAGE, null, 2000);
        Building building2 = new StorageBuilding(BuildingType.WOOD_STORAGE, null, 2000);
        Building building3 = new StorageBuilding(BuildingType.IRON_STORAGE, null, 2000);
        Building building4 = new StorageBuilding(BuildingType.STONE_STORAGE, null, 2000);
        Building building5 = new StorageBuilding(BuildingType.GUNPOWDER_STORAGE, null, 2000);

        village.getBuildings().put(building1.getId(), building1);
        village.getBuildings().put(building2.getId(), building2);
        village.getBuildings().put(building6.getId(), building6);
        village.getBuildings().put(building3.getId(), building3);
        village.getBuildings().put(building4.getId(), building4);
        village.getBuildings().put(building5.getId(), building5);

        System.out.println("buildings " + village.getBuildings().size());

        bm.build(BuildingType.WOOD_MINE, new Coordinate(1, 1));

        Thread.sleep(2100);
        processor.process();

        System.out.println("buildings " + village.getBuildings().size());

        bm.build(BuildingType.STONE_MINE, new Coordinate(2, 2));

        Thread.sleep(2100);
        processor.process();

        System.out.println("Buildings after built " + village.getBuildings().size());

        for (int i = 0; i < 3; i++) {
            Thread.sleep(1100);
            processor.process();
        }
        System.out.println("Buildings after mine: " + village.getBuildings().size());
        System.out.println("**********");
        System.out.println("WOOD: " +
                village.getResources().getAmount(ResourcesType.WOOD));

        village.getResourcesManagement().addResource(200, ResourcesType.WOOD);
        System.out.println("**********");
        System.out.println("WOOD: " +
                village.getResources().getAmount(ResourcesType.WOOD));


        System.out.println("STONE: " +
                village.getResources().getAmount(ResourcesType.STONE));

        Building building = village.getBuildings().values().iterator().next();

        bm.upgrade(building);

        Thread.sleep(2100);
        processor.process();

        System.out.println("Building level after upgrade: " + building.getLevel());

        /*RandomEventTask eventTask = new RandomEventTask(
                Instant.now(),
                Duration.ofSeconds(1),
                TimedOperationType.RANDOM_EVENT_TASK
        );

        village.getTimedOperation().put(eventTask.getId(), eventTask);

        Thread.sleep(1500);
        processor.process();

        System.out.println("Health: " + village.getHealth());
        System.out.println("Cloud radiation: " + village.getCloud().getRadiation());*/

        System.out.println("==== FINAL STATE ====");
        System.out.println("Buildings: " + village.getBuildings().size());
        System.out.println("TimedOps: " + village.getTimedOperation().size());
        System.out.println("WOOD: " + village.getResources().getAmount(ResourcesType.WOOD));
        System.out.println("IRON: " + village.getResources().getAmount(ResourcesType.IRON));
        System.out.println("STONE: " + village.getResources().getAmount(ResourcesType.STONE));
        System.out.println("COIN: " + village.getResources().getAmount(ResourcesType.COIN));
        System.out.println("Health: " + village.getHealth());
        for (TimedOperation op : village.getTimedOperation().values()) {
            System.out.println(op.getTimedOperationType());
        }
        System.out.println("==== DONE ====");
    }
}