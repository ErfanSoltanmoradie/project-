package model.time;

import model.event.Event;
import model.village.Village;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Random;

public class RandomEventTask extends TimedOperation{

    private static final Random random = new Random();
    private final Duration neededTime;

    public RandomEventTask(Instant startTime, Duration neededTime, TimedOperationType timedOperationType) {
        super(startTime, startTime.plus(neededTime), timedOperationType);
        this.neededTime = neededTime;
    }

    @Override
    public void execute(Village village, List<TimedOperation> toAdd) {
        Event event = new Event(village);

        int randomNumber = random.nextInt(100);

        if(randomNumber <= 33)
            event.disease();
        else if (randomNumber > 33 && randomNumber <= 66)
            event.discovery();
        else
            event.storm();


        toAdd.add(new RandomEventTask(Instant.now(), neededTime, TimedOperationType.RANDOM_EVENT_TASK));
    }
}