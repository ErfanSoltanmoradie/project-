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
        return resourcesManagement.checkResourcesArmyCost(type.getTrainCost(), count);
    }

    //add to queue
    public boolean trainArmy(ArmyType type, int count) {

        village.getLock().writeLock().lock();

        try {

            if (!canTrainArmy(type, count))
                return false;

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
            return true;

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