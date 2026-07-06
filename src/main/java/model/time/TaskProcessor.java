package model.time;

import model.building.Building;
import model.building.BuildingStatus;
import model.event.Event;
import model.event.EventType;
import model.resources.ResourcesType;
import model.village.Village;
import service.resource.ResourcesManagement;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class TaskProcessor {

    private Village village;
    private final AtomicBoolean processing = new AtomicBoolean(false);

    public TaskProcessor(Village village) {
        this.village = village;
    }

    public void process(){

        if (!processing.compareAndSet(false, true)) {
            throw new IllegalStateException("TaskProcessor is running concurrently!");
        }

        List<TimedOperation> snapshot;
        List<UUID> removeTasks = new ArrayList<>();
        List<TaskResult> taskResults = new ArrayList<>();

        village.getLock().readLock().lock();
        try {
            snapshot = new ArrayList<>(village.getTimedOperation().values());
        } finally {
            village.getLock().readLock().unlock();
        }

        for ( TimedOperation timedOperation : snapshot){
            if(timedOperation.isFinished()){
                TaskResult result = timedOperation.execute();
                taskResults.add(result);
                removeTasks.add(timedOperation.getId());
            }
        }

        village.getLock().writeLock().lock();
        try {

            ResourcesManagement resourcesManagement = village.getResourcesManagement();
            Building building1 = null;

            for (TaskResult task : taskResults){


                village.getTimedOperation().putAll(task.getTasksToAdd());

                //village.getBuildings().putAll(task.getBuildingsToAdd());
                for (Building building : task.getBuildingsToAdd().values()){
                    if(village.getGameMap().placeBuilding(building, building.getPosition().getX(), building.getPosition().getY())){
                        village.getBuildings().put(building.getId(), building);
                    }
                }

                for (UUID uuid : task.getTasksToRemove()){
                    village.getTimedOperation().remove(uuid);
                }

                for (UUID uuid : task.getBuildingsToUpgrade()){
                    Building building;
                    building = village.getBuildings().get(uuid);

                    if (building != null && building.getBuildingStatus() == BuildingStatus.UPGRADING) {
                        task.getProductionBuildingsToReschedule().add(building.getId());
                        building.upgrade();
                    }
                }

                for (ResourcesType resourcesType : task.getResourcesToAdd().keySet()){
                    resourcesManagement.addResource(task.getResourcesToAdd().get(resourcesType), resourcesType);
                }

                for (ResourcesType resourcesType : task.getResourcesToWithdraw().keySet()){
                    resourcesManagement.withdrawResource(task.getResourcesToWithdraw().get(resourcesType), resourcesType);
                }

                for (UUID uuid : task.getProductionBuildingsToReschedule()){
                    building1 = village.getBuildings().get(uuid);

                    if(building1 == null)
                        continue;

                    System.out.println(building1.getBuildingStatus());
                    if(building1.getBuildingStatus() == BuildingStatus.ACTIVE){
                        ProductionTask nextTask = ProductionTaskFactory.buildProductionTask(building1);
                        if(nextTask != null){
                            village.getTimedOperation().put(nextTask.getId(), nextTask);
                        }
                    }
                }

                for (EventType eventType : task.getEventType()){
                    Event event = new Event(this.village);
                    if(eventType == EventType.STORM)
                        event.storm();
                    else if(eventType == EventType.DISEASE)
                        event.disease();
                    else
                        event.discovery();
                }
            }

            for (UUID uuid : removeTasks){
                village.getTimedOperation().remove(uuid);
            }

        }finally {
            village.getLock().writeLock().unlock();
            processing.set(false);
        }
    }
}
