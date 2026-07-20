package model.building;

import model.world.Coordinate;
import java.io.Serializable;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class Barrack extends Building implements Serializable {

    private int capacity;

    public Barrack(BuildingType type, Coordinate position) {
        super(type, position, 9, 9);
        this.capacity = 30;
    }
    private static final Map<Integer, UpgradeBuildingInfo> upgradeBarrackCost; //Integer ----> level
    static {
        upgradeBarrackCost = new HashMap<>();

        upgradeBarrackCost.put(1, new UpgradeBuildingInfo(
                new Cost(2, 1, 300, 300, 200, 100, 0, 0, 0, Duration.ofSeconds(1))));

        upgradeBarrackCost.put(2, new UpgradeBuildingInfo(
                new Cost(3, 2, 600, 600, 400, 200, 0, 0, 0, Duration.ofSeconds(1))));

        upgradeBarrackCost.put(3, new UpgradeBuildingInfo(
                new Cost(4, 3, 900, 900, 600, 300, 0, 0, 0, Duration.ofSeconds(1))));

        upgradeBarrackCost.put(4, new UpgradeBuildingInfo(
                new Cost(5, 4, 1200, 1200, 800, 400, 0, 0, 0, Duration.ofSeconds(1)))); }

    public static UpgradeBuildingInfo getBarrackUpgradeInfo(int currentLevel){
        return Barrack.upgradeBarrackCost.get(currentLevel);
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    public void upgrade() {

        if(getLevel()>=5)
            return;

        setLevel(getLevel()+1);
        switch (getLevel()){
            case 1 -> capacity=30;
            case 2 -> capacity=60;
            case 3 -> capacity=125;
            case 4 -> capacity=250;
            case 5 -> capacity=500;
        }
        setBuildingStatus(BuildingStatus.ACTIVE);
    }

}