package model.time;


import model.building.Building;
import model.event.EventType;
import model.resources.ResourcesType;
import service.alliance.AllianceRequest;
import service.trade.TradeOffer;
import service.trade.TradeTask;

import java.util.*;

public class TaskResult {

    private final Map<UUID, Building> buildingsToAdd = new HashMap<>();
    private final List<UUID> buildingsToUpgrade = new ArrayList<>();
    //private final List<UUID> buildingsToRemove = new ArrayList<>();
    private final Map<UUID, TimedOperation> tasksToAdd = new HashMap<>();
    private final Map<ResourcesType, Integer> resourcesToAdd = new HashMap<>();
    private final Map<ResourcesType, Integer> resourcesToWithdraw = new HashMap<>();
    private final List<UUID> tasksToRemove = new ArrayList<>();
    private final List<UUID> productionBuildingsToReschedule = new ArrayList<>();
    private final List<EventType> eventType = new ArrayList<>();
    private final List<TradeOffer> tradeOffers = new ArrayList<>();

    public List<TradeOffer> getTradeOffers() {
        return tradeOffers;
    }

    private final List<AllianceRequest> allianceRequestsToApply = new ArrayList<>();

    public List<AllianceRequest> getAllianceRequestsToApply() {
        return allianceRequestsToApply;
    }

    public List<EventType> getEventType() {
        return eventType;
    }

    public List<UUID> getProductionBuildingsToReschedule() {
        return productionBuildingsToReschedule;
    }

    public Map<ResourcesType, Integer> getResourcesToWithdraw() {
        return resourcesToWithdraw;
    }

    public Map<ResourcesType, Integer> getResourcesToAdd() {
        return resourcesToAdd;
    }

    public List<UUID> getBuildingsToUpgrade() {
        return buildingsToUpgrade;
    }

    public Map<UUID, Building> getBuildingsToAdd() {
        return buildingsToAdd;
    }

    public Map<UUID, TimedOperation> getTasksToAdd() {
        return tasksToAdd;
    }

    public List<UUID> getTasksToRemove() {
        return tasksToRemove;
    }
}
