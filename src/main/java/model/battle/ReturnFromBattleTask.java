package model.battle;

import model.army.ArmyType;
import model.resources.ResourcesType;
import model.time.TaskResult;
import model.time.TimedOperation;
import model.time.TimedOperationType;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public class ReturnFromBattleTask extends TimedOperation implements Serializable {

    private final Battle  battle;
    private final Map<ArmyType, Integer> attackerReturningArmies;
    private final Map<ArmyType, Integer> defenderReturningArmies;
    private final Map<ArmyType, Integer> attackerArmyLosses;
    private final Map<ArmyType, Integer> defenderArmyLosses;
    private final Map<ResourcesType, Integer> attackerLoot;
    private final Map<ResourcesType, Integer> defenderResourceLoss;
    private final Map<UUID, Integer> villageHealthChange;

    public ReturnFromBattleTask(
            Instant startTime,
            Duration duration,
            Battle battle,
            Map<ArmyType, Integer> attackerReturningArmies,
            Map<ArmyType, Integer> defenderReturningArmies,
            Map<ArmyType, Integer> attackerArmyLosses,
            Map<ArmyType, Integer> defenderArmyLosses,
            Map<ResourcesType, Integer> attackerLoot,
            Map<ResourcesType, Integer> defenderResourceLoss,
            Map<UUID, Integer> villageHealthChange) {

        super(startTime, duration, TimedOperationType.RETURN_FROM_BATTLE_TASK);

        this.battle = battle;
        this.attackerReturningArmies = attackerReturningArmies;
        this.defenderReturningArmies = defenderReturningArmies;
        this.attackerArmyLosses = attackerArmyLosses;
        this.defenderArmyLosses = defenderArmyLosses;
        this.attackerLoot = attackerLoot;
        this.defenderResourceLoss = defenderResourceLoss;
        this.villageHealthChange = villageHealthChange;
    }

    public Battle getBattle() {
        return battle;
    }

    @Override
    public TaskResult execute() {

        TaskResult taskResult = new TaskResult();

        taskResult.getBattleStatusChange().put(battle.getBattleId(), BattleStatus.FINISHED);

        taskResult.getAttackerReturningArmies().putAll(attackerReturningArmies);

        taskResult.getDefenderReturningArmies().putAll(defenderReturningArmies);

        taskResult.getAttackerArmyLosses().putAll(attackerArmyLosses);

        taskResult.getDefenderArmyLosses().putAll(defenderArmyLosses);

        taskResult.getAttackerLoot().putAll(attackerLoot);

        taskResult.getDefenderResourceLoss().putAll(defenderResourceLoss);

        taskResult.getVillageHealthChange().putAll(villageHealthChange);

        return taskResult;
    }
}
