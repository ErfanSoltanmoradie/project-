package model.time;

import model.building.*;
import model.resources.ResourcesType;
import model.village.Village;
import service.resource.ResourcesManagement;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class ProductionTask extends TimedOperation implements Serializable {

    private final UUID buildingId;
    private final ResourcesType producedType;
    private final int producedAmount;
    private final ResourcesType consumedType;
    private final int consumedAmount;

    public ProductionTask(Instant startTime, Duration neededTime,
                          TimedOperationType timedOperationType, UUID buildingId,
                          ResourcesType producedType, int producedAmount, ResourcesType consumedType, int consumedAmount) {

        super(startTime, neededTime, timedOperationType);
        this.buildingId = buildingId;
        this.producedType = producedType;
        this.producedAmount = producedAmount;
        this.consumedAmount = consumedAmount;
        this.consumedType = consumedType;
    }

    @Override
    public TaskResult execute() {

        TaskResult taskResult = new TaskResult();

        taskResult.getResourcesToAdd().put(this.producedType, producedAmount);
        taskResult.getResourcesToWithdraw().put(this.consumedType, consumedAmount);

        taskResult.getProductionBuildingsToReschedule().add(this.buildingId);

        return taskResult;
    }
}