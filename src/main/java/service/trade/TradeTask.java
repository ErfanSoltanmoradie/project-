package service.trade;


import model.building.Building;
import model.building.Customhouse;
import model.resources.ResourcesType;
import model.time.TaskResult;
import model.time.TimedOperation;
import model.time.TimedOperationType;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

public class TradeTask extends TimedOperation {

    private final TradeOffer offer;

    public TradeTask(Instant start, Duration neededTime, TradeOffer offer) {
        super(start,neededTime, TimedOperationType.TRADE_TASK);
        this.offer=offer;
    }

    @Override
    public TaskResult execute() {

        TaskResult taskResult = new TaskResult();

        this.offer.setTradeStatus(TradeStatus.ACCEPTED);
        taskResult.getTradeOffers().add(this.offer);
        return taskResult;
    }
}