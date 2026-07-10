package service.battle;
import model.army.ArmyStorage;
import model.army.ArmyType;
import model.battle.*;
import model.building.*;
import model.resources.ResourcesType;
import model.storage.Storage;
import model.time.TaskProcessor;
import model.time.TimedOperation;
import model.village.Village;
import model.world.Coordinate;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class BattleManagementTest {

    private Village createVillage(int x, int y) {

        Village village = new Village(new Coordinate(x, y), 1000);

        StorageBuilding woodStorage = new StorageBuilding(BuildingType.WOOD_STORAGE,new Coordinate(1,1),10000);
        StorageBuilding ironStorage = new StorageBuilding(BuildingType.IRON_STORAGE,new Coordinate(1,1),10000);
        StorageBuilding stoneStorage = new StorageBuilding(BuildingType.STONE_STORAGE,new Coordinate(1,1),10000);
        StorageBuilding gunpowder = new StorageBuilding(BuildingType.GUNPOWDER_STORAGE,new Coordinate(1,1),10000);
        StorageBuilding soilStorage = new StorageBuilding(BuildingType.SOIL_STORAGE,new Coordinate(1,1),10000);
        StorageBuilding waterStorage = new StorageBuilding(BuildingType.WATER_STORAGE,new Coordinate(1,1),10000);

        woodStorage.setBuildingStatus(BuildingStatus.ACTIVE);
        ironStorage.setBuildingStatus(BuildingStatus.ACTIVE);
        stoneStorage.setBuildingStatus(BuildingStatus.ACTIVE);
        gunpowder.setBuildingStatus(BuildingStatus.ACTIVE);
        soilStorage.setBuildingStatus(BuildingStatus.ACTIVE);
        waterStorage.setBuildingStatus(BuildingStatus.ACTIVE);

        village.getBuildings().put(woodStorage.getId(), woodStorage);
        village.getBuildings().put(ironStorage.getId(), ironStorage);
        village.getBuildings().put(stoneStorage.getId(), stoneStorage);
        village.getBuildings().put(gunpowder.getId(), gunpowder);
        village.getBuildings().put(soilStorage.getId(), soilStorage);
        village.getBuildings().put(waterStorage.getId(), waterStorage);


        ArmyProducer armyProducer =
                new ArmyProducer(BuildingType.ARMY_PRODUCER,
                        new Coordinate(x + 1, y));

        Barrack barrack =
                new Barrack(BuildingType.BARRACKS,
                        new Coordinate(x + 2, y));

        village.getBuildings().put(armyProducer.getId(), armyProducer);
        village.getBuildings().put(barrack.getId(), barrack);

        armyProducer.setBuildingStatus(BuildingStatus.ACTIVE);
        barrack.setBuildingStatus(BuildingStatus.ACTIVE);

        return village;
    }

    private void printVillage(String title, Village village) {

        System.out.println();
        System.out.println("========== " + title + " ==========");

        System.out.println("HP : " + village.getHealth());

        System.out.println();

        System.out.println("Resources");
        System.out.println("Wood : " + village.getResources().getAmount(ResourcesType.WOOD));
        System.out.println("Stone : " + village.getResources().getAmount(ResourcesType.STONE));
        System.out.println("Iron : " + village.getResources().getAmount(ResourcesType.IRON));
        System.out.println("GunPowder : " + village.getResources().getAmount(ResourcesType.GUN_POWDER));
        System.out.println("Coin : " + village.getResources().getAmount(ResourcesType.COIN));

        System.out.println();

        System.out.println("Armies");
        System.out.println("Ragnar : "
                + village.getArmies().getArmyStorage().getArmyCount(ArmyType.RAGNAR));
        System.out.println("Rosoo : "
                + village.getArmies().getArmyStorage().getArmyCount(ArmyType.ROSOO));
        System.out.println("Lagerta : "
                + village.getArmies().getArmyStorage().getArmyCount(ArmyType.LAGERTA));

        System.out.println();

        System.out.println("Active Battles : "
                + village.getActiveBattles().size());

        System.out.println("Battle History : "
                + village.getBattleHistory().size());

        System.out.println("==============================");
    }

    private void printBattleHistory(BattleHistory history) {

        System.out.println();

        System.out.println("========== BATTLE RESULT ==========");

        System.out.println("Winner : " + history.getWinner());

        System.out.println();

        System.out.println("Attacker Losses");
        System.out.println(history.getAttackerLosses());

        System.out.println();

        System.out.println("Defender Losses");
        System.out.println(history.getDefenderLosses());

        System.out.println();

        System.out.println("Loot");
        System.out.println(history.getAttackerLoot());

        System.out.println("===================================");
    }

    private void finishAllTasks(Village village) {

        for (TimedOperation task : village.getTimedOperation().values()) {
            task.setFinishTime(Instant.now().minusSeconds(1));
        }

        new TaskProcessor(village).process();
    }

    @Test
    void startBattleTest() {

        Village attacker = createVillage(10,10);
        Village defender = createVillage(40,40);

        attacker.getArmies().getArmyStorage().increaseArmy(ArmyType.RAGNAR,20);
        attacker.getArmies().getArmyStorage().increaseArmy(ArmyType.ROSOO,10);

        BattleArmy army = new BattleArmy();
        army.increaseArmy(ArmyType.RAGNAR,10);
        army.increaseArmy(ArmyType.ROSOO,5);

        BattleManagement battleManagement =
                new BattleManagement(attacker,defender);

        boolean result = battleManagement.startBattle(army);

        assertTrue(result);

        assertEquals(
                10,
                attacker.getArmies().getArmyStorage().getArmyCount(ArmyType.RAGNAR)
        );

        assertEquals(
                5,
                attacker.getArmies().getArmyStorage().getArmyCount(ArmyType.ROSOO)
        );

        assertEquals(
                1,
                attacker.getActiveBattles().size()
        );

        assertEquals(
                1,
                attacker.getTimedOperation().size()
        );
    }

    @Test
    void travelToBattleTest() {

        Village attacker = createVillage(10, 10);
        Village defender = createVillage(30, 20);

        // سربازهای مهاجم
        attacker.getArmies().getArmyStorage().increaseArmy(ArmyType.RAGNAR, 20);

        // سربازهای مدافع
        defender.getArmies().getArmyStorage().increaseArmy(ArmyType.RAGNAR, 15);
        defender.getArmies().getArmyStorage().increaseArmy(ArmyType.ROSOO, 5);

        BattleArmy attackerArmy = new BattleArmy();
        attackerArmy.increaseArmy(ArmyType.RAGNAR, 10);

        BattleManagement battleManagement =
                new BattleManagement(attacker, defender);

        assertTrue(battleManagement.startBattle(attackerArmy));

        // برای تست، زمان رسیدن را تمام شده فرض می‌کنیم
        for (TimedOperation task : attacker.getTimedOperation().values()) {
            task.setFinishTime(Instant.now().minusSeconds(1));
        }

        TaskProcessor processor = new TaskProcessor(attacker);
        processor.process();

        Battle battle = attacker.getActiveBattles().values().iterator().next();

        System.out.println("Battle Status : " + battle.getStatus());

        System.out.println("Defender Ragnar : "
                + battle.getDefenderArmy().getArmyCount(ArmyType.RAGNAR));

        System.out.println("Defender Rosoo : "
                + battle.getDefenderArmy().getArmyCount(ArmyType.ROSOO));

        System.out.println("Defender Storage Ragnar : "
                + defender.getArmies().getArmyStorage().getArmyCount(ArmyType.RAGNAR));

        System.out.println("Timed Operations : "
                + attacker.getTimedOperation().size());

        assertEquals(BattleStatus.FIGHTING, battle.getStatus());

        assertNotNull(battle.getDefenderArmy());

        assertEquals(15,
                battle.getDefenderArmy().getArmyCount(ArmyType.RAGNAR));

        assertEquals(5,
                battle.getDefenderArmy().getArmyCount(ArmyType.ROSOO));

        assertEquals(0,
                defender.getArmies().getArmyStorage().getArmyCount(ArmyType.RAGNAR));

        assertEquals(0,
                defender.getArmies().getArmyStorage().getArmyCount(ArmyType.ROSOO));

        System.out.println("========== After Travel ==========");

        System.out.println("Battle Status = " + battle.getStatus());

        System.out.println("Attacker Storage");
        System.out.println("Ragnar : "
                + attacker.getArmies().getArmyStorage().getArmyCount(ArmyType.RAGNAR));

        System.out.println();

        System.out.println("Defender Storage");
        System.out.println("Ragnar : "
                + defender.getArmies().getArmyStorage().getArmyCount(ArmyType.RAGNAR));
        System.out.println("Rosoo : "
                + defender.getArmies().getArmyStorage().getArmyCount(ArmyType.ROSOO));

        System.out.println();

        System.out.println("Battle Defender Army");
        System.out.println("Ragnar : "
                + battle.getDefenderArmy().getArmyCount(ArmyType.RAGNAR));
        System.out.println("Rosoo : "
                + battle.getDefenderArmy().getArmyCount(ArmyType.ROSOO));

        System.out.println("==================================");
    }

    @Test
    void fullBattleTest() {

        Village attacker = createVillage(10, 10);
        Village defender = createVillage(40, 40);

        attacker.getArmies().getArmyStorage().increaseArmy(ArmyType.RAGNAR, 20);
        attacker.getArmies().getArmyStorage().increaseArmy(ArmyType.ROSOO, 10);

        defender.getArmies().getArmyStorage().increaseArmy(ArmyType.RAGNAR, 15);
        defender.getArmies().getArmyStorage().increaseArmy(ArmyType.LAGERTA, 5);

        BattleArmy battleArmy = new BattleArmy();

        battleArmy.increaseArmy(ArmyType.RAGNAR, 10);
        battleArmy.increaseArmy(ArmyType.ROSOO, 5);

        BattleManagement management =
                new BattleManagement(attacker, defender);

        assertTrue(management.startBattle(battleArmy));

        System.out.println("========== START ==========");
        System.out.println(attacker.getActiveBattles().size());

        for (TimedOperation task : attacker.getTimedOperation().values()) {
            task.setFinishTime(Instant.now().minusSeconds(1));
        }

        new TaskProcessor(attacker).process();

        Battle battle =
                attacker.getActiveBattles()
                        .values()
                        .iterator()
                        .next();

        System.out.println("========== ARRIVED ==========");
        System.out.println("Status : " + battle.getStatus());

        assertEquals(BattleStatus.FIGHTING, battle.getStatus());

        for (TimedOperation task : attacker.getTimedOperation().values()) {
            task.setFinishTime(Instant.now().minusSeconds(1));
        }

        new TaskProcessor(attacker).process();

        System.out.println("========== BATTLE ==========");
        System.out.println("Status : " + battle.getStatus());

        assertEquals(BattleStatus.RETURNING, battle.getStatus());

        for (TimedOperation task : attacker.getTimedOperation().values()) {
            task.setFinishTime(Instant.now().minusSeconds(1));
        }

        new TaskProcessor(attacker).process();

        System.out.println("========== FINISHED ==========");

        System.out.println("Battle Status : " + battle.getStatus());

        System.out.println("Active Battles Attacker : "
                + attacker.getActiveBattles().size());

        System.out.println("Active Battles Defender : "
                + defender.getActiveBattles().size());

        System.out.println();

        System.out.println("Attacker Storage");
        System.out.println("Ragnar : "
                + attacker.getArmies().getArmyStorage().getArmyCount(ArmyType.RAGNAR));
        System.out.println("Rosoo : "
                + attacker.getArmies().getArmyStorage().getArmyCount(ArmyType.ROSOO));

        System.out.println();

        System.out.println("Defender Storage");
        System.out.println("Ragnar : "
                + defender.getArmies().getArmyStorage().getArmyCount(ArmyType.RAGNAR));
        System.out.println("Lagerta : "
                + defender.getArmies().getArmyStorage().getArmyCount(ArmyType.LAGERTA));

        System.out.println();

        System.out.println("Attacker Wood : "
                + attacker.getResources().getAmount(ResourcesType.WOOD));

        System.out.println("Defender Wood : "
                + defender.getResources().getAmount(ResourcesType.WOOD));

        System.out.println();

        System.out.println("Attacker History : "
                + attacker.getBattleHistory().size());

        System.out.println("Defender History : "
                + defender.getBattleHistory().size());

        System.out.println("================================");

        assertEquals(BattleStatus.FINISHED, battle.getStatus());

        assertTrue(attacker.getActiveBattles().isEmpty());

        assertTrue(defender.getActiveBattles().isEmpty());
    }

    @Test
    void startBattleWithZeroArmyTest() {

        Village attacker = createVillage(10, 10);
        Village defender = createVillage(40, 40);

        BattleArmy army = new BattleArmy();

        BattleManagement management =
                new BattleManagement(attacker, defender);

        boolean result = management.startBattle(army);

        System.out.println("Battle Started : " + result);

        assertFalse(result);
    }

    @Test
    void attackYourselfTest() {

        Village village = createVillage(10, 10);

        village.getArmies().getArmyStorage().increaseArmy(ArmyType.RAGNAR, 10);

        BattleArmy army = new BattleArmy();
        army.increaseArmy(ArmyType.RAGNAR, 5);

        BattleManagement management =
                new BattleManagement(village, village);

        boolean result = management.startBattle(army);

        System.out.println("Battle Started : " + result);

        assertFalse(result);
    }

    @Test
    void requestMoreArmyThanExistsTest() {

        Village attacker = createVillage(10, 10);
        Village defender = createVillage(40, 40);

        attacker.getArmies().getArmyStorage().increaseArmy(ArmyType.RAGNAR, 5);

        BattleArmy army = new BattleArmy();
        army.increaseArmy(ArmyType.RAGNAR, 20);

        BattleManagement management =
                new BattleManagement(attacker, defender);

        boolean result = management.startBattle(army);

        System.out.println("Battle Started : " + result);

        assertFalse(result);
    }
    @Test
    void defenderWithoutArmyTest() {

        Village attacker = createVillage(10, 10);
        Village defender = createVillage(40, 40);

        attacker.getArmies().getArmyStorage().increaseArmy(ArmyType.RAGNAR, 20);

        BattleArmy army = new BattleArmy();
        army.increaseArmy(ArmyType.RAGNAR, 10);

        BattleManagement management =
                new BattleManagement(attacker, defender);

        assertTrue(management.startBattle(army));

        System.out.println("Battle Started Successfully");
    }

    @Test
    void attackerLoseTest() {

        Village attacker = createVillage(10, 10);
        Village defender = createVillage(40, 40);

        // نیروهای مهاجم
        attacker.getArmies().getArmyStorage().increaseArmy(ArmyType.RAGNAR, 5);

        // نیروهای مدافع
        defender.getArmies().getArmyStorage().increaseArmy(ArmyType.RAGNAR, 40);
        defender.getArmies().getArmyStorage().increaseArmy(ArmyType.LAGERTA, 20);

        int attackerWoodBefore =
                attacker.getResources().getAmount(ResourcesType.WOOD);

        int defenderWoodBefore =
                defender.getResources().getAmount(ResourcesType.WOOD);

        int defenderHealthBefore = defender.getHealth();

        BattleArmy battleArmy = new BattleArmy();
        battleArmy.increaseArmy(ArmyType.RAGNAR, 5);

        BattleManagement management =
                new BattleManagement(attacker, defender);

        assertTrue(management.startBattle(battleArmy));

        // ---------- رسیدن ----------
        for (TimedOperation task : attacker.getTimedOperation().values()) {
            task.setFinishTime(Instant.now().minusSeconds(1));
        }
        new TaskProcessor(attacker).process();

        Battle battle =
                attacker.getActiveBattles().values().iterator().next();

        assertEquals(BattleStatus.FIGHTING, battle.getStatus());

        // ---------- جنگ ----------
        for (TimedOperation task : attacker.getTimedOperation().values()) {
            task.setFinishTime(Instant.now().minusSeconds(1));
        }
        new TaskProcessor(attacker).process();

        assertEquals(BattleStatus.RETURNING, battle.getStatus());

        // ---------- برگشت ----------
        for (TimedOperation task : attacker.getTimedOperation().values()) {
            task.setFinishTime(Instant.now().minusSeconds(1));
        }
        new TaskProcessor(attacker).process();

        BattleHistory history =
                attacker.getBattleHistory().getTail();

        System.out.println("========== ATTACKER LOSE ==========");
        System.out.println("Winner : " + history.getWinner());

        System.out.println();

        System.out.println("Attacker Ragnar : "
                + attacker.getArmies().getArmyStorage().getArmyCount(ArmyType.RAGNAR));

        System.out.println("Defender Ragnar : "
                + defender.getArmies().getArmyStorage().getArmyCount(ArmyType.RAGNAR));

        System.out.println("Defender Lagerta : "
                + defender.getArmies().getArmyStorage().getArmyCount(ArmyType.LAGERTA));

        System.out.println();

        System.out.println("Attacker Wood : "
                + attacker.getResources().getAmount(ResourcesType.WOOD));

        System.out.println("Defender Wood : "
                + defender.getResources().getAmount(ResourcesType.WOOD));

        System.out.println();

        System.out.println("Defender Health : "
                + defender.getHealth());

        System.out.println();

        System.out.println("Attacker History : "
                + attacker.getBattleHistory().size());

        System.out.println("Defender History : "
                + defender.getBattleHistory().size());

        System.out.println("================================");

        // ---------- Assert ----------

        assertEquals(BattleWinner.DEFENDER, history.getWinner());

        // مهاجم نباید غنیمت گرفته باشد
        assertEquals(attackerWoodBefore,
                attacker.getResources().getAmount(ResourcesType.WOOD));

        // منابع مدافع نباید کم شده باشد
        assertEquals(defenderWoodBefore,
                defender.getResources().getAmount(ResourcesType.WOOD));

        // HP مدافع نباید کم شده باشد
        assertEquals(defenderHealthBefore,
                defender.getHealth());

        // تعدادی از نیروهای مهاجم باید برگشته باشند
        assertTrue(attacker.getArmies()
                .getArmyStorage()
                .getArmyCount(ArmyType.RAGNAR) > 0);

        // تاریخچه ثبت شده باشد
        assertEquals(1, attacker.getBattleHistory().size());
        assertEquals(1, defender.getBattleHistory().size());

        // جنگ از ActiveBattle حذف شده باشد
        assertTrue(attacker.getActiveBattles().isEmpty());
        assertTrue(defender.getActiveBattles().isEmpty());
    }

    @Test
    void attackerWinScenario() {

        Village attacker = createVillage(10, 10);
        Village defender = createVillage(40, 40);

        //------------------------------
        // وضعیت اولیه
        //------------------------------

        attacker.getArmies().getArmyStorage().increaseArmy(ArmyType.RAGNAR, 20);
        attacker.getArmies().getArmyStorage().increaseArmy(ArmyType.ROSOO, 30);

        defender.getArmies().getArmyStorage().increaseArmy(ArmyType.RAGNAR, 5);
        defender.getArmies().getArmyStorage().increaseArmy(ArmyType.LAGERTA, 5);

        int attackerWoodBefore =
                attacker.getResources().getAmount(ResourcesType.WOOD);

        int defenderWoodBefore =
                defender.getResources().getAmount(ResourcesType.WOOD);

        int defenderHpBefore =
                defender.getHealth();

        printVillage("ATTACKER BEFORE", attacker);
        printVillage("DEFENDER BEFORE", defender);

        //------------------------------
        // ارتش ارسالی
        //------------------------------

        BattleArmy battleArmy = new BattleArmy();

        battleArmy.increaseArmy(ArmyType.RAGNAR, 20);
        battleArmy.increaseArmy(ArmyType.ROSOO, 20);

        BattleManagement management =
                new BattleManagement(attacker, defender);

        assertTrue(management.startBattle(battleArmy));

        //------------------------------
        // Travel
        //------------------------------

        finishAllTasks(attacker);

        Battle battle =
                attacker.getActiveBattles()
                        .values()
                        .iterator()
                        .next();

        assertEquals(BattleStatus.FIGHTING,
                battle.getStatus());

        //------------------------------
        // Battle
        //------------------------------

        finishAllTasks(attacker);

        assertEquals(BattleStatus.RETURNING,
                battle.getStatus());

        //------------------------------
        // Return
        //------------------------------

        finishAllTasks(attacker);

        assertEquals(BattleStatus.FINISHED,
                battle.getStatus());

        //------------------------------
        // History
        //------------------------------

        BattleHistory history =
                attacker.getBattleHistory().getTail();

        printBattleHistory(history);

        printVillage("ATTACKER AFTER", attacker);
        printVillage("DEFENDER AFTER", defender);

        //------------------------------
        // ASSERT
        //------------------------------

        assertEquals(
                BattleWinner.ATTACKER,
                history.getWinner());

        assertEquals(
                BattleStatus.FINISHED,
                battle.getStatus());

        assertTrue(
                attacker.getActiveBattles().isEmpty());

        assertTrue(
                defender.getActiveBattles().isEmpty());

        assertEquals(
                1,
                attacker.getBattleHistory().size());

        assertEquals(
                1,
                defender.getBattleHistory().size());

        assertTrue(
                attacker.getResources()
                        .getAmount(ResourcesType.WOOD)
                        >= attackerWoodBefore);

        assertTrue(
                defender.getResources()
                        .getAmount(ResourcesType.WOOD)
                        <= defenderWoodBefore);

        assertTrue(
                defender.getHealth()
                        < defenderHpBefore);

        assertTrue(
                attacker.getArmies()
                        .getArmyStorage()
                        .getArmyCount(ArmyType.RAGNAR)
                        > 0);

        assertTrue(
                defender.getArmies()
                        .getArmyStorage()
                        .getArmyCount(ArmyType.RAGNAR)
                        >= 0);
    }

    @Test
    void defenderWinScenario() {

        Village attacker = createVillage(10, 10);
        Village defender = createVillage(40, 40);

        // مهاجم ضعیف
        attacker.getArmies().getArmyStorage().increaseArmy(ArmyType.RAGNAR, 5);

        // مدافع قوی
        defender.getArmies().getArmyStorage().increaseArmy(ArmyType.RAGNAR, 20);
        defender.getArmies().getArmyStorage().increaseArmy(ArmyType.LAGERTA, 20);

        int attackerHpBefore = attacker.getHealth();
        int defenderHpBefore = defender.getHealth();

        int attackerWoodBefore =
                attacker.getResources().getAmount(ResourcesType.WOOD);

        int defenderWoodBefore =
                defender.getResources().getAmount(ResourcesType.WOOD);

        printVillage("ATTACKER BEFORE", attacker);
        printVillage("DEFENDER BEFORE", defender);

        BattleArmy army = new BattleArmy();
        army.increaseArmy(ArmyType.RAGNAR, 5);

        BattleManagement management =
                new BattleManagement(attacker, defender);

        assertTrue(management.startBattle(army));

        // رسیدن به دهکده
        attacker.getTimedOperation()
                .values()
                .forEach(t -> t.setFinishTime(Instant.now().minusSeconds(1)));

        new TaskProcessor(attacker).process();

        Battle battle =
                attacker.getActiveBattles()
                        .values()
                        .iterator()
                        .next();

        assertEquals(BattleStatus.FIGHTING, battle.getStatus());

        // پایان نبرد
        attacker.getTimedOperation()
                .values()
                .forEach(t -> t.setFinishTime(Instant.now().minusSeconds(1)));

        new TaskProcessor(attacker).process();

        assertEquals(BattleStatus.RETURNING, battle.getStatus());

        // پایان برگشت
        attacker.getTimedOperation()
                .values()
                .forEach(t -> t.setFinishTime(Instant.now().minusSeconds(1)));

        new TaskProcessor(attacker).process();

        BattleHistory history =
                attacker.getBattleHistory().getTail();

        System.out.println("========== BATTLE RESULT ==========");
        System.out.println("Winner : " + history.getWinner());

        System.out.println();
        System.out.println("Attacker Losses");
        System.out.println(history.getAttackerLosses());

        System.out.println();
        System.out.println("Defender Losses");
        System.out.println(history.getDefenderLosses());

        System.out.println();
        System.out.println("Loot");
        System.out.println(history.getAttackerLoot());

        System.out.println("===================================");

        printVillage("ATTACKER AFTER", attacker);
        printVillage("DEFENDER AFTER", defender);

        // ---------- Assert ----------

        assertEquals(BattleWinner.DEFENDER, history.getWinner());

        assertEquals(BattleStatus.FINISHED, battle.getStatus());

        assertTrue(attacker.getActiveBattles().isEmpty());
        assertTrue(defender.getActiveBattles().isEmpty());

        assertEquals(attackerWoodBefore,
                attacker.getResources().getAmount(ResourcesType.WOOD));

        assertEquals(defenderWoodBefore,
                defender.getResources().getAmount(ResourcesType.WOOD));

        assertEquals(defenderHpBefore,
                defender.getHealth());

        assertEquals(attackerHpBefore,
                attacker.getHealth());

        assertTrue(history.getAttackerLoot().isEmpty());

        assertFalse(history.getAttackerLosses().isEmpty());

        assertFalse(history.getDefenderLosses().isEmpty());

        assertEquals(1, attacker.getBattleHistory().size());
        assertEquals(1, defender.getBattleHistory().size());
    }

    @Test
    void attackerCannotStartTwoBattles() {

        Village attacker = createVillage(10, 10);

        Village defender1 = createVillage(20, 20);

        Village defender2 = createVillage(30, 30);

        BattleArmy army = new BattleArmy();
        army.increaseArmy(ArmyType.RAGNAR, 10);

        attacker.getArmies().getArmyStorage().increaseArmy(ArmyType.RAGNAR, 20);

        defender1.getArmies().getArmyStorage().increaseArmy(ArmyType.RAGNAR, 10);

        defender2.getArmies().getArmyStorage().increaseArmy(ArmyType.RAGNAR, 10);

        BattleManagement battle1 =
                new BattleManagement(attacker, defender1);

        BattleManagement battle2 =
                new BattleManagement(attacker, defender2);

        assertTrue(battle1.startBattle(army));

        System.out.println("First Battle : Started");

        assertFalse(battle2.startBattle(army));

        System.out.println("Second Battle : Rejected");

        System.out.println("Active Battles : "
                + attacker.getActiveBattles().size());

        assertEquals(1, attacker.getActiveBattles().size());
    }

    @Test
    void defenderCannotBeAttackedTwice() {

        Village attacker1 = createVillage(10, 10);

        Village attacker2 = createVillage(15, 15);

        Village defender = createVillage(40, 40);

        attacker1.getArmies().getArmyStorage().increaseArmy(ArmyType.RAGNAR, 20);
        attacker2.getArmies().getArmyStorage().increaseArmy(ArmyType.RAGNAR, 20);

        defender.getArmies().getArmyStorage().increaseArmy(ArmyType.RAGNAR, 10);

        BattleArmy army = new BattleArmy();
        army.increaseArmy(ArmyType.RAGNAR, 10);

        BattleManagement battle1 =
                new BattleManagement(attacker1, defender);

        BattleManagement battle2 =
                new BattleManagement(attacker2, defender);

        assertTrue(battle1.startBattle(army));

        System.out.println("First Attack : Started");

        assertFalse(battle2.startBattle(army));

        System.out.println("Second Attack : Rejected");

        System.out.println("Defender Active Battles : "
                + defender.getActiveBattles().size());

        assertEquals(1, defender.getActiveBattles().size());
    }
}
