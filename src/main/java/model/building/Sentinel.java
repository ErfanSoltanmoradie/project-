package model.building;

import model.world.Coordinate;

import java.io.Serializable;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class Sentinel extends DefensiveBuilding implements Serializable {
    public Sentinel(BuildingType type, Coordinate position) {
        super(type, position);
    }

    private static final Map<Integer, UpgradeBuildingInfo> upgradeSentinelCost; //Integer ----> level
    static {
        upgradeSentinelCost = new HashMap<>();

        upgradeSentinelCost.put(1, new UpgradeBuildingInfo(
                new Cost(2, 1, 140, 100, 60, 20, 30, 30, 0, Duration.ofSeconds(1))));

        upgradeSentinelCost.put(2, new UpgradeBuildingInfo(
                new Cost(3, 2, 210, 150, 90, 30, 45, 45, 0, Duration.ofSeconds(1))));

        upgradeSentinelCost.put(3, new UpgradeBuildingInfo(
                new Cost(4, 3, 280, 200, 120, 40, 60, 60, 0, Duration.ofSeconds(1))));

        upgradeSentinelCost.put(4, new UpgradeBuildingInfo(
                new Cost(5, 4, 350, 250, 150, 50, 75, 75, 0, Duration.ofSeconds(1))));
    }
    public static UpgradeBuildingInfo getSentinelUpgradeInfo(int currentLevel){
        return Sentinel.upgradeSentinelCost.get(currentLevel);
    }

    @Override
    public int getDefenceStrength(){
        return switch (this.getLevel()) {
            case 1 -> 50;
            case 2 -> 60;
            case 3 -> 70;
            case 4 -> 80;
            case 5 -> 90;
            default -> 0;
        };
    }
}
