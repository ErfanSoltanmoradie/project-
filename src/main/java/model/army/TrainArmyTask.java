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

        taskResult.getTasksToRemove().add(getId());

        taskResult.getTrainedArmiesToAdd().put(armyType, 1);

        return taskResult;
    }
}
    /*
    public TaskResult execute(Village village, List<TimedOperation> toAdd) {

        ArmyStorage storage = village.getArmies().getArmyStorage();

        ArmyQueue queue =village.getArmies();

        storage.increaseArmy(getArmyType(),1);

        queue.dequeue();

        if (!queue.isEmpty()) {

            ArmyType nextArmy = queue.peek();

            Instant start = getFinishTime();

            Instant finish = start.plus(nextArmy.getTrainCost().neededTime());

            queue.setLastFinishTime(finish);

            TrainArmyTask nextTask = new TrainArmyTask(start, finish, nextArmy);

            toAdd.add(nextTask);
        }
        */