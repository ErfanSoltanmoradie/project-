package model.army;

import model.time.TaskResult;
import model.time.TimedOperation;
import model.time.TimedOperationType;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;

public class TrainArmyTask extends TimedOperation implements Serializable {

    private final ArmyType armyType;

    public TrainArmyTask(Instant startTime, Duration neededTime, ArmyType armyType) {
        super(startTime, neededTime, TimedOperationType.TRAIN_ARMY_TASK);
        this.armyType = armyType;

    }

    public ArmyType getArmyType() {
        return armyType;
    }

    @Override
    public TaskResult execute() {

        TaskResult taskResult = new TaskResult();

        taskResult.getTrainedArmiesToAdd().put(armyType, 1);

        return taskResult;
    }
}