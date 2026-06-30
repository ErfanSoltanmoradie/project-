package model.time;

import model.village.Village;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class MovementTask extends TimedOperation implements Serializable {
    private Village villageA;
    private Village villageB;   // target village



    public MovementTask(Instant startTime, Duration neededTime, OperationStatus status,
                        Village villageA, Village villageB, TimedOperationType timedOperationType) {
        super(startTime, neededTime, timedOperationType);
        this.villageA = villageA;
        this.villageB = villageB;
    }

    @Override
    public TaskResult execute() {
        return null;
    }
}
