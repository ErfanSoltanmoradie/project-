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

        upgradeSentinelCost.put(1, new UpgradeBuildingInfo(1 ,1,
                new Cost(10, 10, 10, 10, 10, 10, 10, Duration.ofSeconds(1))));

        upgradeSentinelCost.put(2, new UpgradeBuildingInfo(1 ,1,
                new Cost(100, 50, 40, 10, 5, 5, 5, Duration.ofSeconds(1))));

        upgradeSentinelCost.put(3, new UpgradeBuildingInfo(1 ,1,
                new Cost(100, 50, 40, 10, 5, 5, 5, Duration.ofSeconds(1))));

        upgradeSentinelCost.put(4, new UpgradeBuildingInfo(1 ,1,
                new Cost(100, 50, 40, 10, 5, 5, 5, Duration.ofSeconds(1))));
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
