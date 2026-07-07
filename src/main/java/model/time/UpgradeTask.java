package model.time;

import model.building.Building;
import model.building.BuildingStatus;
import model.village.Village;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class UpgradeTask extends TimedOperation implements Serializable {

    private UUID buildingId;

    public UpgradeTask(Instant startTime, Duration neededTime, UUID buildingId) {
        super(startTime, neededTime, TimedOperationType.UPGRADE_TASK);
        this.buildingId = buildingId;
    }

    @Override
    public TaskResult execute() {

        TaskResult taskResult =new TaskResult();

        taskResult.getBuildingsToUpgrade().add(this.buildingId);

        return taskResult;
    }

    public UUID getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(UUID buildingId) {
        this.buildingId = buildingId;
    }
}