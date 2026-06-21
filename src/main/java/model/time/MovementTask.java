package model.time;

import model.village.Village;

import java.time.Instant;
import java.time.LocalDateTime;

public class MovementTask extends TimedOperation{
    private Village villageA;
    private Village villageB;   // target village



    public MovementTask(Instant startTime, Instant finishTime, OperationStatus status,
                        Village villageA, Village village, TimedOperationType timedOperationType) {
        super(startTime, finishTime, timedOperationType);
        this.villageA = villageA;
        this.villageB = villageB;
    }

    @Override
    public void execute(Village village) {

    }
}
