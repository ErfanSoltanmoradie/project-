package model.time;


import model.event.EventType;

import java.time.Duration;
import java.time.Instant;
import java.util.Random;

public class RandomEventTask extends TimedOperation{

    private static final Random random = new Random();

    public RandomEventTask(Instant startTime, Duration neededTime, TimedOperationType timedOperationType) {
        super(startTime, neededTime, timedOperationType);
    }

    @Override
    public TaskResult execute() {

        TaskResult taskResult = new TaskResult();

        int randomNumber = random.nextInt(100);

        if(randomNumber <= 33)
            taskResult.getEventType().add(EventType.DISEASE);

        else if (randomNumber > 33 && randomNumber <= 66)
            taskResult.getEventType().add(EventType.STORM);
        else
            taskResult.getEventType().add(EventType.DISCOVERY);


        return taskResult;
    }
}