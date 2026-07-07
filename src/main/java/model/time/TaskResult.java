package model.time;


import model.building.Building;
import model.building.Plant;
import model.event.EventType;
import model.resources.ResourcesType;

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

    public Map<UUID, Plant> getPlantsToAdd() {
        return plantsToAdd;
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
