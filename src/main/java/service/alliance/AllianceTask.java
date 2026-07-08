package service.alliance;

import model.time.TaskResult;
import model.time.TimedOperation;
import model.time.TimedOperationType;
import java.time.Duration;
import java.time.Instant;


public class AllianceTask extends TimedOperation {

    private final AllianceRequest allianceRequest;

    public AllianceTask(Instant start, Duration neededTime, AllianceRequest allianceRequest) {
        super(start, neededTime, TimedOperationType.ALLIANCE_TASK);
        this.allianceRequest=allianceRequest;
    }
    @Override
    public TaskResult execute() {
        TaskResult taskResult = new TaskResult();

        taskResult.getAllianceRequestsToApply().add(this.allianceRequest);

        return taskResult;
    }
}