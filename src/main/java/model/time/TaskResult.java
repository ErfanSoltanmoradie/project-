package model.time;

import model.army.ArmyType;
import model.building.Building;
import model.building.Plant;
import model.event.EventType;
import model.resources.ResourcesType;
import service.alliance.AllianceRequest;
import service.trade.TradeOffer;
import service.trade.TradeTask;

import java.time.Instant;
import java.util.*;

public class TaskResult {

    private final Map<UUID, Building> buildingsToAdd = new HashMap<>();
    private final Map<UUID, Plant> plantsToAdd = new HashMap<>();
    private final List<UUID> buildingsToUpgrade = new ArrayList<>();
    //private final List<UUID> buildingsToRemove = new ArrayList<>();
    private final Map<UUID, TimedOperation> tasksToAdd = new HashMap<>();
    private final Map<ResourcesType, Integer> resourcesToAdd = new HashMap<>();
    private final Map<ResourcesType, Integer> resourcesToWithdraw = new HashMap<>();
    private final List<UUID> tasksToRemove = new ArrayList<>();
    private final List<UUID> productionBuildingsToReschedule = new ArrayList<>();
    private final List<EventType> eventType = new ArrayList<>();

<<<<<<< HEAD
    public Map<UUID, Plant> getPlantsToAdd() {
        return plantsToAdd;
    }

    public List<EventType> getEventType() {
=======
    private final List<TradeOffer> tradeOffers = new ArrayList<>();

    public List<TradeOffer> getTradeOffers() {
        return tradeOffers;
    }

    private final List<AllianceRequest> allianceRequestsToApply = new ArrayList<>();

    public List<AllianceRequest> getAllianceRequestsToApply() {
        return allianceRequestsToApply;
    }

    public Map<UUID, Plant> getPlantsToAdd() {
        return plantsToAdd;
    }
    private final Map<ArmyType, Integer> trainedArmiesToAdd = new HashMap<>();

    public Map<ArmyType, Integer> getTrainedArmiesToAdd () {
        return trainedArmiesToAdd;

    }

    public List<EventType> getEventType () {
>>>>>>> 8f79b4d52b278d0d7991a8f6e08fac50fa5ee007
        return eventType;
    }

    public List<UUID> getProductionBuildingsToReschedule () {
        return productionBuildingsToReschedule;
    }

    public Map<ResourcesType, Integer> getResourcesToWithdraw () {
        return resourcesToWithdraw;
    }

    public Map<ResourcesType, Integer> getResourcesToAdd () {
        return resourcesToAdd;
    }

    public List<UUID> getBuildingsToUpgrade () {
        return buildingsToUpgrade;
    }

    public Map<UUID, Building> getBuildingsToAdd () {
        return buildingsToAdd;
    }

    public Map<UUID, TimedOperation> getTasksToAdd () {
        return tasksToAdd;
    }

    public List<UUID> getTasksToRemove () {
        return tasksToRemove;
    }
}
