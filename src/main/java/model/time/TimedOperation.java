package model.time;

import model.building.Building;
import model.building.BuildingType;
import model.village.Village;

import java.io.Serializable;
import java.security.PublicKey;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class TimedOperation implements Serializable {

    private final UUID id;  // Task ID
    private Instant startTime;
    private Duration neededTime;
    private Instant finishTime;   // start time + needed time
    private final TimedOperationType timedOperationType;
    //private OperationStatus status;


    public TimedOperation(Instant startTime, Duration neededTime, TimedOperationType timedOperationType) {
        this.id = UUID.randomUUID();
        this.startTime = startTime;
        this.neededTime = neededTime;
        this.finishTime = startTime.plus(neededTime);
        this.timedOperationType = timedOperationType;
        //this.status = status;
    }

    public abstract TaskResult execute();

    public TimedOperationType getTimedOperationType() {
        return timedOperationType;
    }

    public boolean isFinished(){
        return Instant.now().isAfter(finishTime);
    }

    public UUID getId() {
        return this.id;
    }

    public Instant getStartTime() {
        return this.startTime;
    }

    public Instant getFinishTime() {
        return this.finishTime;
    }

    public void setStartTime(Instant startTime) {this.startTime = startTime;}

    public void setFinishTime(Instant finishTime) {this.finishTime = finishTime;}

}
