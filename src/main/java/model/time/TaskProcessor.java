package model.time;

import model.army.ArmyQueue;
import model.army.ArmyType;
import model.army.TrainArmyTask;
import model.building.Building;
import model.building.BuildingStatus;
import model.event.Event;
import model.event.EventType;
import model.resources.ResourcesType;
import model.village.Village;
import service.resource.ResourcesManagement;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

                village.getBuildings().putAll(task.getBuildingsToAdd());

                village.getTimedOperation().putAll(task.getTasksToAdd());

                for (UUID uuid : task.getTasksToRemove()){
                    village.getTimedOperation().remove(uuid);
                }

                for (UUID uuid : task.getBuildingsToUpgrade()){
                    Building building;
                    building = village.getBuildings().get(uuid);

                    if (building != null && building.getBuildingStatus() == BuildingStatus.UPGRADING) {
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

                for (Map.Entry<ArmyType, Integer> entry : task.getTrainedArmiesToAdd().entrySet()) {
                    ArmyType trainedType = entry.getKey();
                    int count = entry.getValue();

                    // 1. اضافه کردن نیروی جدید به اردوگاه (Storage)
                    village.getArmies().getArmyStorage().increaseArmy(trainedType, count);

                    // 2. خارج کردن این نیرو از صف آموزش
                    village.getArmies().getArmyQueue().dequeue();

                    // 3. اگر هنوز نیرویی در صف باقی مانده است، آموزش نیروی بعدی را کلید بزن
                    if (!village.getArmies().getArmyQueue().isEmpty()) {
                        ArmyType nextArmy = village.getArmies().getArmyQueue().peek();

                        TrainArmyTask nextTask = new TrainArmyTask(
                                Instant.now(),
                                nextArmy.getTrainCost().neededTime(),
                                nextArmy
                        );
                        village.getTimedOperation().put(nextTask.getId(), nextTask);
                    }
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