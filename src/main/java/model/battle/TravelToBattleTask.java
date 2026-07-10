package model.battle;

import model.time.TaskResult;
import model.time.TimedOperation;
import model.time.TimedOperationType;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;

public class TravelToBattleTask extends TimedOperation implements Serializable {

    private final Battle  battle;

    public TravelToBattleTask(Instant startTime, Duration neededTime, Battle battle) {
        super(startTime,neededTime, TimedOperationType.TRAVEL_TO_BATTLE_TASK);
        this.battle = battle;
    }

    public Battle getBattle() {
        return battle;
    }

    @Override
    public TaskResult execute() {

        TaskResult taskResult = new TaskResult();

        taskResult.getBattleStatusChange().put(battle.getBattleId(),BattleStatus.FIGHTING);

        return taskResult;
    }
}
