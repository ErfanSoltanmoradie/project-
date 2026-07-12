package service.army;

import model.army.*;
import model.building.ArmyProducer;
import model.building.Barrack;
import model.building.Building;
import model.building.BuildingType;
import model.player.Player;
import model.village.Village;
import service.resource.ResourcesManagement;

import java.time.Instant;

public class ArmyManagement {

    private final Village village;
    private final Armies armies;
    private final ResourcesManagement resourcesManagement;

    public ArmyManagement(Player player) {

        this.village = player.getVillage();
        this.armies = village.getArmies();
        this.resourcesManagement = village.getResourcesManagement();

    }

    //for checking requirements
    private TrainArmyResult canTrainArmy(ArmyType type, int count) {

        if (count <= 0)
            return TrainArmyResult.INVALID_COUNT;

        //is there any ArmyProducer or not
        ArmyProducer producer = getArmyProducer();
        if(producer == null)
            return TrainArmyResult.NO_ARMY_PRODUCER;

        //is there any Barrack or not
        Barrack barrack = getBarrack();
        if(barrack == null)
            return TrainArmyResult.NO_BARRACK;

        //has the soldier been unlocked or not
        if (!type.isUnlocked(producer.getLevel())) {
            return TrainArmyResult.SOLDIER_LOCKED;
        }
        //check queue capacity
        if (armies.getArmyQueue().size() + count > producer.getMaxTrainingCount()) {
            return TrainArmyResult.QUEUE_FULL;
        }
        //check Barrack capacity
        if (armies.getTotalArmyCount()
                + armies.getArmyQueue().size()
                + count > barrack.getCapacity()){
            return TrainArmyResult.BARRACK_FULL;
        }
        //check resource
        if (!resourcesManagement.checkResourcesArmyCost(type.getTrainCost(), count)) {
            return TrainArmyResult.NOT_ENOUGH_RESOURCES;
        }

        return TrainArmyResult.SUCCESS;
    }

    //add to queue
    public TrainArmyResult trainArmy(ArmyType type, int count) {

        village.getLock().writeLock().lock();

        try {

            TrainArmyResult result = canTrainArmy(type, count);

            if (result != TrainArmyResult.SUCCESS) {
                return result;
            }

            resourcesManagement.withdrawResourcesArmyCost(type.getTrainCost(), count);

            boolean isMachineIdle = ! armies.getArmyQueue().isTraining();

            Instant statTime;

            if (armies.getArmyQueue().isEmpty()) {
                statTime = Instant.now();

            }else {
                statTime = armies.getArmyQueue().getLast().finishTime();
            }

            for (int i = 0; i < count; i++) {

                Instant finishTime = statTime.plus(type.getTrainCost().neededTime());

                QueuedArmy queuedArmy = new QueuedArmy(type, finishTime);

                armies.getArmyQueue().enqueue(queuedArmy);

                statTime = finishTime;
            }
            if (isMachineIdle){

                QueuedArmy firstArmyInQueue = armies.getArmyQueue().peek();

                TrainArmyTask newTask = new TrainArmyTask(
                        Instant.now(),
                        firstArmyInQueue.armyType().getTrainCost().neededTime(),
                        firstArmyInQueue.armyType()
                );

                armies.getArmyQueue().setTraining(true);

                village.getTimedOperation().put(newTask.getId(), newTask);
            }
            return TrainArmyResult.SUCCESS;

        } finally {
            village.getLock().writeLock().unlock();
        }
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