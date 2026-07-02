package service.army;

import model.army.*;
import model.building.ArmyProducer;
import model.building.Barrack;
import model.building.Building;
import model.building.BuildingType;
import model.player.Player;
import model.village.Village;
import model.time.TimedOperation;
import java.time.Instant;

public class ArmyManagement {

    private final Village village;
    private final Armies armies;
    private final ArmyResourceManagement resourceManagement;

    public ArmyManagement(Player player) {

        this.village = player.getVillage();
        this.armies = village.getArmies();
        this.resourceManagement = new ArmyResourceManagement(village);
    }
    //add to queue
    public boolean trainArmy(ArmyType type, int count) {

        if (!canTrainArmy(type, count))
            return false;

        resourceManagement.withdrawResources(type.getTrainCost(), count);

        boolean isMachineIdle = !hasActiveTrainTask();

        Instant statTime;
        if (armies.getArmyQueue().isEmpty())
            statTime = Instant.now();
        else
            statTime = armies.getArmyQueue().getLastFinishTime();

        for (int i = 0; i < count; i++) {

            armies.getArmyQueue().enqueue(type);

            Instant finishTime = statTime.plus(type.getTrainCost().neededTime());

            armies.getArmyQueue().setLastFinishTime(finishTime);

            statTime = finishTime; // زمان شروع برای دور بعدیِ حلقه باید زمان پایانِ قبلی باشد
        }
        if (isMachineIdle && !armies.getArmyQueue().isEmpty()) {

            ArmyType firstArmyInQueue = armies.getArmyQueue().peek();

            TrainArmyTask newTask = new TrainArmyTask(
                    Instant.now(),
                    firstArmyInQueue.getTrainCost().neededTime(),
                    firstArmyInQueue
            );
            village.getTimedOperation().put(newTask.getId(), newTask);
        }

        return true;
    }
    //for checking requirements
    private boolean canTrainArmy(ArmyType type, int count) {

        if (count <= 0)
            return false;

        //is there any ArmyProducer or not
        ArmyProducer producer = getArmyProducer();

        //is there any Barrack or not
        Barrack barrack = getBarrack();

        if (producer == null || barrack == null)
            return false;

        //has the soldier been unlocked or not
        if (!type.isUnlocked(producer.getLevel()))
            return false;

        //check queue capacity
        if (armies.getArmyQueue().size() + count > producer.getMaxTrainingCount())
            return false;

        //check Barrack capacity
        if (armies.getTotalArmyCount()
                + armies.getArmyQueue().size()
                + count > barrack.getCapacity())
            return false;

        //check resource
        return resourceManagement.checkResources(type.getTrainCost(), count);
    }
    //For check that only one TrainTask is active at a time
    private boolean hasActiveTrainTask() {

        for(TimedOperation task : village.getTimedOperation().values()){
            if(task instanceof TrainArmyTask)
                return true;
        }
        return false;
    }

    //finding ArmyProducer
    private ArmyProducer getArmyProducer() {

        for (Building building : village.getBuildings().values()) {

            if (building.getType() == BuildingType.ARMY_PRODUCER)
                return (ArmyProducer) building;
        }

        return null;
    }
    //finding Barack
    private Barrack getBarrack() {

        for (Building building : village.getBuildings().values()) {

            if (building.getType() == BuildingType.BARRACKS)
                return (Barrack) building;
        }

        return null;
    }

    public ArmyQueue getArmyQueue() {
        return armies.getArmyQueue();
    }

    public ArmyStorage getArmyStorage() {
        return armies.getArmyStorage();
    }
}
/*

        for (int i = 0; i < count; i++) {

            armies.getArmyQueue().enqueue(type);

            Instant finishTime =statTime.plus(type.getTrainCost().neededTime());

            armies.getArmyQueue().setLastFinishTime(finishTime);

            //(i==0)-> we only want to create the Task in the first round
            if(i==0 && !hasActiveTrainTask()){

                TrainArmyTask task = new TrainArmyTask(statTime,type.getTrainCost().neededTime(), type);

                village.getTimedOperation().put(task.getId(), task);
            }
            statTime = finishTime;
        }
        */