package model.army;

import java.io.Serializable;

public class Armies implements Serializable {

    private final ArmyStorage armyStorage;
    private final ArmyQueue armyQueue;

    public Armies() {
        this.armyStorage = new ArmyStorage();
        this.armyQueue = new ArmyQueue();
    }

    public ArmyStorage getArmyStorage() {
        return armyStorage;
    }

    public ArmyQueue getArmyQueue() {
        return armyQueue;
    }

    public int getTotalArmyCount() {
        return armyStorage.getTotalArmyCount();
    }
}
