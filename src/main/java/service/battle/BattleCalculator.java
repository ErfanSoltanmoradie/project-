package service.battle;

import model.army.ArmyType;
import model.battle.Battle;
import model.battle.BattleArmy;
import model.battle.BattleWinner;
import model.building.*;
import model.resources.Resources;
import model.resources.ResourcesType;
import model.time.TaskResult;
import model.village.Village;

public class BattleCalculator {

    public void calculateBattle(Battle battle,TaskResult taskResult) {

        int attackPower = calculateAttackPower(battle);
        int defensePower = calculateDefensePower(battle);

        BattleWinner winner;

        if(attackPower > defensePower)
            winner = BattleWinner.ATTACKER;
        else
            winner = BattleWinner.DEFENDER;

        battle.setWinner(winner);

        taskResult.getBattleWinners().put(battle.getBattleId(),winner);

        calculateAttackerLosses(battle, winner, taskResult);
        calculateDefenderLosses(battle, winner, taskResult);
        calculateAttackerReturningArmies(battle, taskResult);
        calculateDefenderReturningArmies(battle, taskResult);
        calculateLoot(battle, winner, taskResult);
        calculateVillageDamage(battle, winner, taskResult);

    }

    private int calculateAttackPower(Battle battle) {

        ArmyProducer producer = getArmyProducer(battle.getAttackerVillage());

        if(producer == null) return 0;

        BattleArmy army = battle.getAttackerArmy();

        int attackPower = 0;

        attackPower += army.getArmyCount(ArmyType.RAGNAR) * ArmyType.RAGNAR.getFinalAttack(producer);

        attackPower += army.getArmyCount(ArmyType.ROSOO) * ArmyType.ROSOO.getFinalAttack(producer);

        attackPower += army.getArmyCount(ArmyType.LAGERTA) * ArmyType.LAGERTA.getFinalAttack(producer);

        return attackPower;
    }

    private int calculateDefensePower(Battle battle) {

        ArmyProducer producer = getArmyProducer(battle.getDefenderVillage());

        if(producer == null) return 0;

        BattleArmy army = battle.getDefenderArmy();

        int defensePowerOfArmies = 0;

        defensePowerOfArmies += army.getArmyCount(ArmyType.RAGNAR) * ArmyType.RAGNAR.getFinalDefense(producer);

        defensePowerOfArmies += army.getArmyCount(ArmyType.ROSOO) * ArmyType.ROSOO.getFinalDefense(producer);

        defensePowerOfArmies += army.getArmyCount(ArmyType.LAGERTA) * ArmyType.LAGERTA.getFinalDefense(producer);

        int defensePowerOfBuilding = calculateDefenseBuildingPower(battle.getDefenderVillage());

        return defensePowerOfArmies + defensePowerOfBuilding;

    }

    private int calculateDefenseBuildingPower(Village village) {

        int defensePowerOfBuilding = 0;

        for (Building building : village.getBuildings().values()) {

            switch (building.getType()) {
                case BALLISTA_DEFENSIVE -> defensePowerOfBuilding += ((Ballista) building).getDefenceStrength();

                case CATAPULT_DEFENSIVE -> defensePowerOfBuilding += ((Catapult) building).getDefenceStrength();

                case SENTINEL_DEFENSIVE -> defensePowerOfBuilding += ((Sentinel) building).getDefenceStrength();

                default -> defensePowerOfBuilding += 0;
            }
        }
        return defensePowerOfBuilding;
    }

    private void calculateAttackerLosses(Battle battle, BattleWinner winner, TaskResult taskResult) {

        BattleArmy army = battle.getAttackerArmy();

        int lossPercent;

        if(winner == BattleWinner.ATTACKER)
            lossPercent = 20;
        else
            lossPercent = 60;

        for (ArmyType type : ArmyType.values()) {

            int count = army.getArmyCount(type);

            if (count == 0) continue;

            int loss =Math.max(1,Math.round(count* lossPercent/100.0f));

            army.decreaseArmy(type, loss);

            taskResult.getAttackerArmyLosses().put(type, loss);
        }
    }

    private void calculateAttackerReturningArmies(Battle battle, TaskResult taskResult) {

        BattleArmy army = battle.getAttackerArmy();

        for (ArmyType type : ArmyType.values()) {

            int remaining = army.getArmyCount(type);

            if (remaining == 0)
                continue;

            taskResult.getAttackerReturningArmies().put(type, remaining);
        }
    }

    private void calculateDefenderLosses(Battle battle, BattleWinner winner, TaskResult taskResult) {

        BattleArmy army = battle.getDefenderArmy();

        int lossPercent;

        if(winner == BattleWinner.DEFENDER)
            lossPercent = 10;
        else
            lossPercent = 40;

        for (ArmyType type : ArmyType.values()) {

            int count = army.getArmyCount(type);

            if (count == 0) continue;

            int loss =Math.max(1,Math.round(count* lossPercent/100.0f));

            army.decreaseArmy(type, loss);

            taskResult.getDefenderArmyLosses().put(type, loss);
        }
    }

    private void calculateDefenderReturningArmies(Battle battle, TaskResult taskResult) {

        BattleArmy army = battle.getDefenderArmy();

        for (ArmyType type : ArmyType.values()) {

            int remaining = army.getArmyCount(type);

            if (remaining == 0)
                continue;

            taskResult.getDefenderReturningArmies().put(type, remaining);
        }
    }

    private void calculateLoot(Battle battle, BattleWinner winner, TaskResult taskResult) {

        if(winner == BattleWinner.DEFENDER)
            return;

        Resources resources = battle.getDefenderVillage().getResources();

        ResourcesType[] lootableResources = new  ResourcesType[6];
        lootableResources[0] = ResourcesType.WOOD;
        lootableResources[1] = ResourcesType.STONE;
        lootableResources[2] = ResourcesType.IRON;
        lootableResources[3] = ResourcesType.GUN_POWDER;
        lootableResources[4] = ResourcesType.CLEAN_SOIL;
        lootableResources[5] = ResourcesType.CLEAN_WATER;

        for (ResourcesType type : lootableResources) {

            int amount = resources.getAmount(type);

            int loot =Math.min(1200,Math.round(amount * 0.4f));

            taskResult.getAttackerLoot().put(type, loot);
            taskResult.getDefenderResourceLoss().put(type, loot);
        }

        int coin = resources.getAmount(ResourcesType.COIN);

        int coinLoot = Math.min(150, Math.round(coin * 0.2f));

        taskResult.getAttackerLoot().put(ResourcesType.COIN, coinLoot);
        taskResult.getDefenderResourceLoss().put(ResourcesType.COIN, coinLoot);
    }

    private void calculateVillageDamage(Battle battle, BattleWinner winner, TaskResult taskResult) {

        if(winner == BattleWinner.DEFENDER) return;

        taskResult.getVillageHealthChange().put(battle.getDefenderVillage().getVillageId(),250);

    }

    private ArmyProducer getArmyProducer(Village village) {

        for (Building building : village.getBuildings().values()) {

            if (building.getType() == BuildingType.ARMY_PRODUCER) {
                return (ArmyProducer) building;
            }
        }
        return null;
    }
}