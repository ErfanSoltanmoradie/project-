package model.building;

import model.world.Coordinate;

import java.io.Serializable;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class Catapult extends  DefensiveBuilding implements Serializable {
    public Catapult(BuildingType type, Coordinate position) {
        super(type, position);
    }

    private static final Map<Integer, UpgradeBuildingInfo> upgradeCatapultCost; //Integer ----> level
    static {
        upgradeCatapultCost = new HashMap<>();

        upgradeCatapultCost.put(1, new UpgradeBuildingInfo(
                new Cost(2, 1, 200, 160, 100, 40, 40, 40, 0, Duration.ofSeconds(1))));

        upgradeCatapultCost.put(2, new UpgradeBuildingInfo(
                new Cost(3, 2, 450, 360, 240, 90, 90, 90, 0, Duration.ofSeconds(1))));

        upgradeCatapultCost.put(3, new UpgradeBuildingInfo(
                new Cost(4, 3, 600, 480, 320, 120, 120, 120, 0, Duration.ofSeconds(1))));

        upgradeCatapultCost.put(4, new UpgradeBuildingInfo(
                new Cost(5, 4, 750, 620, 400, 150, 150, 150, 0, Duration.ofSeconds(1))));
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
