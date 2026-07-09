package model.battle;

import model.time.TaskResult;
import model.time.TimedOperation;
import model.time.TimedOperationType;
import service.battle.BattleCalculator;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;

public class BattleTask extends TimedOperation implements Serializable {

    private final Battle  battle;

    public BattleTask(Instant startTime, Duration neededTime, Battle battle) {
        super(startTime,neededTime, TimedOperationType.BATTLE_TASK);
        this.battle = battle;
    }

    public Battle getBattle() {
        return battle;
    }

    @Override
    public TaskResult execute() {

        TaskResult taskResult = new TaskResult();

        BattleCalculator battleCalculator = new BattleCalculator();

        battleCalculator.calculateBattle(battle, taskResult);

        battle.setFinishedTime(Instant.now());

        taskResult.getBattleStatusChange().put(battle.getBattleId(),BattleStatus.RETURNING);

        return taskResult;
    }
}
