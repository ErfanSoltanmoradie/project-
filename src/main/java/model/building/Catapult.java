package model.building;

import model.world.Coordinate;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class Catapult extends  DefensiveBuilding{
    public Catapult(BuildingType type, Coordinate position) {
        super(type, position);
    }

    private static final Map<Integer, UpgradeBuildingInfo> upgradeCatapultCost; //Integer ----> level
    static {
        upgradeCatapultCost = new HashMap<>();

        upgradeCatapultCost.put(1, new UpgradeBuildingInfo(1 ,1,
                new Cost(10, 10, 10, 10, 10, 10, 10, Duration.ofSeconds(1))));

        upgradeCatapultCost.put(2, new UpgradeBuildingInfo(1 ,1,
                new Cost(100, 50, 40, 10, 5, 5, 5, Duration.ofSeconds(1))));

        upgradeCatapultCost.put(3, new UpgradeBuildingInfo(1 ,1,
                new Cost(100, 50, 40, 10, 5, 5, 5, Duration.ofSeconds(1))));

        upgradeCatapultCost.put(4, new UpgradeBuildingInfo(1 ,1,
                new Cost(100, 50, 40, 10, 5, 5, 5, Duration.ofSeconds(1))));
    }
    public static UpgradeBuildingInfo getCatapultUpgradeInfo(int currentLevel){
        return Catapult.upgradeCatapultCost.get(currentLevel);
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
