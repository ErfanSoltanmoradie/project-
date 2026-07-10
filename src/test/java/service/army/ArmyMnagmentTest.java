package service.army;

import model.army.ArmyType;
import model.building.ArmyProducer;
import model.building.Barrack;
import model.building.BuildingType;
import model.player.Player;
import model.player.PlayerFactory;
import model.resources.ResourcesType;
import model.time.TaskProcessor;
import model.village.Village;
import model.world.Coordinate;
import model.world.WorldMap;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ArmyMnagmentTest {

    @Test
    void trainOneArmy() {

        WorldMap map = new WorldMap();
        Player player = new PlayerFactory(map).createPlayer("Fatemeh");

        Village village = player.getVillage();

        // ساختمان‌های لازم
        Barrack barrack = new Barrack(BuildingType.BARRACKS, new Coordinate(0,0));
        ArmyProducer producer = new ArmyProducer(BuildingType.ARMY_PRODUCER, new Coordinate(1,0));

        village.getBuildings().put(barrack.getId(), barrack);
        village.getBuildings().put(producer.getId(), producer);

        ArmyManagement armyManagement = new ArmyManagement(player);

        boolean result = armyManagement.trainArmy(ArmyType.RAGNAR,1);

        assertTrue(result);
        assertEquals(1,
                village.getArmies().getArmyQueue().size());

        assertEquals(0,
                village.getArmies().getArmyStorage().getRagnarCount());

        assertEquals(1,
                village.getTimedOperation().size());
    }
    @Test
    void finishTrainingOneArmy() throws InterruptedException {

        WorldMap map = new WorldMap();
        Player player = new PlayerFactory(map).createPlayer("Fatemeh");

        Village village = player.getVillage();

        Barrack barrack = new Barrack(BuildingType.BARRACKS, new Coordinate(0,0));
        ArmyProducer producer = new ArmyProducer(BuildingType.ARMY_PRODUCER, new Coordinate(1,0));

        village.getBuildings().put(barrack.getId(), barrack);
        village.getBuildings().put(producer.getId(), producer);

        ArmyManagement armyManagement = new ArmyManagement(player);

        TaskProcessor processor = new TaskProcessor(village);

        armyManagement.trainArmy(ArmyType.RAGNAR,1);

        System.out.println("--------- Before Processing ---------");
        System.out.println("Queue Size = " + village.getArmies().getArmyQueue().size());
        System.out.println("Storage Ragnar = " + village.getArmies().getArmyStorage().getRagnarCount());
        System.out.println("Timed Tasks = " + village.getTimedOperation().size());

        Thread.sleep(6000);

        processor.process();

        System.out.println("--------- After Processing ---------");
        System.out.println("Queue Size = " + village.getArmies().getArmyQueue().size());
        System.out.println("Storage Ragnar = " + village.getArmies().getArmyStorage().getRagnarCount());
        System.out.println("Timed Tasks = " + village.getTimedOperation().size());

        assertEquals(0, village.getArmies().getArmyQueue().size());
        assertEquals(1, village.getArmies().getArmyStorage().getRagnarCount());
        assertEquals(0, village.getTimedOperation().size());
    }
    @Test
    void trainThreeSoldiers() {

        WorldMap map = new WorldMap();
        Player player = new PlayerFactory(map).createPlayer("fatemeh");

        Village village = player.getVillage();

        Barrack barrack =
                new Barrack(BuildingType.BARRACKS, new Coordinate(0,0));

        ArmyProducer producer =
                new ArmyProducer(BuildingType.ARMY_PRODUCER, new Coordinate(1,0));

        village.getBuildings().put(barrack.getId(), barrack);
        village.getBuildings().put(producer.getId(), producer);

        ArmyManagement armyManagement = new ArmyManagement(player);

        boolean result =
                armyManagement.trainArmy(ArmyType.RAGNAR,3);

        assertTrue(result);

        System.out.println("Queue Size = "
                + village.getArmies().getArmyQueue().size());

        System.out.println("Timed Tasks = "
                + village.getTimedOperation().size());

        assertEquals(3,
                village.getArmies().getArmyQueue().size());

        assertEquals(1,
                village.getTimedOperation().size());

        System.out.println("TEST PASSED");
    }
    @Test
    void processFirstTrainingTask() throws Exception {

        WorldMap map = new WorldMap();
        Player player = new PlayerFactory(map).createPlayer("fatemeh");

        Village village = player.getVillage();

        Barrack barrack =
                new Barrack(BuildingType.BARRACKS, new Coordinate(0,0));

        ArmyProducer producer =
                new ArmyProducer(BuildingType.ARMY_PRODUCER, new Coordinate(1,0));

        village.getBuildings().put(barrack.getId(), barrack);
        village.getBuildings().put(producer.getId(), producer);

        ArmyManagement armyManagement = new ArmyManagement(player);

        armyManagement.trainArmy(ArmyType.RAGNAR, 3);

        // منتظر می‌مانیم تا اولین سرباز آماده شود.
        Thread.sleep(5500);

        TaskProcessor processor = new TaskProcessor(village);
        processor.process();

        System.out.println("Queue Size = "
                + village.getArmies().getArmyQueue().size());

        System.out.println("Ragnar Count = "
                + village.getArmies().getArmyStorage().getRagnarCount());

        System.out.println("Timed Tasks = "
                + village.getTimedOperation().size());

        assertEquals(2,
                village.getArmies().getArmyQueue().size());

        assertEquals(1,
                village.getArmies().getArmyStorage().getRagnarCount());

        assertEquals(1,
                village.getTimedOperation().size());

        System.out.println("TEST PASSED");
    }
    @Test
    void finishWholeQueue() throws Exception {

        WorldMap map = new WorldMap();
        Player player = new PlayerFactory(map).createPlayer("fatemeh");

        Village village = player.getVillage();

        Barrack barrack =
                new Barrack(BuildingType.BARRACKS, new Coordinate(0,0));

        ArmyProducer producer =
                new ArmyProducer(BuildingType.ARMY_PRODUCER, new Coordinate(1,0));

        village.getBuildings().put(barrack.getId(), barrack);
        village.getBuildings().put(producer.getId(), producer);

        ArmyManagement armyManagement = new ArmyManagement(player);

        armyManagement.trainArmy(ArmyType.RAGNAR,3);

        TaskProcessor processor = new TaskProcessor(village);

        for(int i=0;i<3;i++){
            Thread.sleep(5500);
            processor.process();
        }

        System.out.println("Queue = " + village.getArmies().getArmyQueue().size());
        System.out.println("Storage = " + village.getArmies().getArmyStorage().getRagnarCount());
        System.out.println("Tasks = " + village.getTimedOperation().size());
        System.out.println("Training = " + village.getArmies().getArmyQueue().isTraining());

        assertEquals(0, village.getArmies().getArmyQueue().size());
        assertEquals(3, village.getArmies().getArmyStorage().getRagnarCount());
        assertEquals(0, village.getTimedOperation().size());
        assertFalse(village.getArmies().getArmyQueue().isTraining());

        System.out.println("TEST PASSED");
    }
    @Test
    void trainArmy_zeroCount() {

        WorldMap map = new WorldMap();
        Player player = new PlayerFactory(map).createPlayer("fatemeh");

        Village village = player.getVillage();

        Barrack barrack =
                new Barrack(BuildingType.BARRACKS, new Coordinate(0,0));

        ArmyProducer producer =
                new ArmyProducer(BuildingType.ARMY_PRODUCER, new Coordinate(1,0));

        village.getBuildings().put(barrack.getId(), barrack);
        village.getBuildings().put(producer.getId(), producer);

        ArmyManagement armyManagement = new ArmyManagement(player);


        boolean result = armyManagement.trainArmy(ArmyType.RAGNAR, 0);

        System.out.println("Result = " + result);
        System.out.println("Queue size = " + village.getArmies().getArmyQueue().size());

        assertFalse(result);
        assertEquals(0, village.getArmies().getArmyQueue().size());

        System.out.println("Test Passed ✅");
    }
    @Test
    void trainArmy_negativeCount() {

        WorldMap map = new WorldMap();
        Player player = new PlayerFactory(map).createPlayer("fatemeh");

        Village village = player.getVillage();

        Barrack barrack =
                new Barrack(BuildingType.BARRACKS, new Coordinate(0,0));

        ArmyProducer producer =
                new ArmyProducer(BuildingType.ARMY_PRODUCER, new Coordinate(1,0));

        village.getBuildings().put(barrack.getId(), barrack);
        village.getBuildings().put(producer.getId(), producer);

        ArmyManagement armyManagement = new ArmyManagement(player);

        boolean result = armyManagement.trainArmy(ArmyType.RAGNAR, -5);

        System.out.println("Result = " + result);
        System.out.println("Queue size = " + village.getArmies().getArmyQueue().size());

        assertFalse(result);
        assertEquals(0, village.getArmies().getArmyQueue().size());

        System.out.println("Test Passed ✅");
    }
    @Test
    void trainArmy_withoutArmyProducer() {


        WorldMap map = new WorldMap();
        Player player = new PlayerFactory(map).createPlayer("fatemeh");

        Village village = player.getVillage();

        ArmyManagement armyManagement = new ArmyManagement(player);

        village.getBuildings().clear();

        // فقط Barrack
        Barrack barrack = new Barrack(
                BuildingType.BARRACKS,
                new Coordinate(0, 0)
        );
        village.getBuildings().put(barrack.getId(), barrack);

        boolean result = armyManagement.trainArmy(ArmyType.RAGNAR, 1);

        System.out.println("Result = " + result);

        assertFalse(result);

        System.out.println("Test Passed ✅");
    }
    @Test
    void trainArmy_notEnoughResources() {

        WorldMap map = new WorldMap();
        Player player = new PlayerFactory(map).createPlayer("fatemeh");

        Village village = player.getVillage();

        Barrack barrack =
                new Barrack(BuildingType.BARRACKS, new Coordinate(0,0));

        ArmyProducer producer =
                new ArmyProducer(BuildingType.ARMY_PRODUCER, new Coordinate(1,0));

        village.getBuildings().put(barrack.getId(), barrack);
        village.getBuildings().put(producer.getId(), producer);

        ArmyManagement armyManagement = new ArmyManagement(player);

        village.getResources().withdraw(ResourcesType.WOOD, 2000);
        village.getResources().withdraw(ResourcesType.STONE, 1000);
        village.getResources().withdraw(ResourcesType.IRON, 1000);
        village.getResources().withdraw(ResourcesType.GUN_POWDER, 1000);

        boolean result = armyManagement.trainArmy(ArmyType.RAGNAR, 1);

        System.out.println("Result = " + result);
        System.out.println("Queue = " + village.getArmies().getArmyQueue().size());

        assertFalse(result);
        assertEquals(0, village.getArmies().getArmyQueue().size());

        System.out.println("Test Passed ✅");
    }
    @Test
    void trainArmy_fullQueue() {


        WorldMap map = new WorldMap();
        Player player = new PlayerFactory(map).createPlayer("fatemeh");

        Village village = player.getVillage();

        Barrack barrack =
                new Barrack(BuildingType.BARRACKS, new Coordinate(0,0));

        ArmyProducer producer =
                new ArmyProducer(BuildingType.ARMY_PRODUCER, new Coordinate(1,0));

        village.getBuildings().put(barrack.getId(), barrack);
        village.getBuildings().put(producer.getId(), producer);

        ArmyManagement armyManagement = new ArmyManagement(player);

        boolean first = armyManagement.trainArmy(ArmyType.RAGNAR, 10);

        boolean second = armyManagement.trainArmy(ArmyType.RAGNAR, 1);

        System.out.println("First = " + first);
        System.out.println("Second = " + second);
        System.out.println("Queue = " + village.getArmies().getArmyQueue().size());

        assertTrue(first);
        assertFalse(second);
        assertEquals(10, village.getArmies().getArmyQueue().size());

        System.out.println("Test Passed ✅");
    }
    @Test
    void trainArmy_barrackFull() {

        WorldMap map = new WorldMap();
        Player player = new PlayerFactory(map).createPlayer("fatemeh");

        Village village = player.getVillage();

        Barrack barrack =
                new Barrack(BuildingType.BARRACKS, new Coordinate(0,0));

        ArmyProducer producer =
                new ArmyProducer(BuildingType.ARMY_PRODUCER, new Coordinate(1,0));

        village.getBuildings().put(barrack.getId(), barrack);
        village.getBuildings().put(producer.getId(), producer);

        ArmyManagement armyManagement = new ArmyManagement(player);

        village.getArmies().getArmyStorage().increaseArmy(ArmyType.RAGNAR, 30);

        boolean result = armyManagement.trainArmy(ArmyType.RAGNAR, 1);

        System.out.println("Result = " + result);

        assertFalse(result);

        System.out.println("Test Passed ✅");
    }

}
