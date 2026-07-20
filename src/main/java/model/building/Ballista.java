package model.building;

import model.world.Coordinate;

import java.io.Serializable;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class Ballista extends  DefensiveBuilding implements Serializable {

    public Ballista(BuildingType type, Coordinate position) {
        super(type, position);
    }

    private static final Map<Integer, UpgradeBuildingInfo> upgradeBallistaCost; //Integer ----> level
    static {
        upgradeBallistaCost = new HashMap<>();

        upgradeBallistaCost.put(1, new UpgradeBuildingInfo(
                new Cost(2, 1, 350, 240, 160, 60, 60, 60, 0, Duration.ofSeconds(1))));

        upgradeBallistaCost.put(2, new UpgradeBuildingInfo(
                new Cost(3, 2, 450, 360, 240, 90, 90, 90, 0, Duration.ofSeconds(1))));

        upgradeBallistaCost.put(3, new UpgradeBuildingInfo(
                new Cost(4, 3, 600, 480, 320, 110, 110, 110, 0, Duration.ofSeconds(1))));

        upgradeBallistaCost.put(4, new UpgradeBuildingInfo(
                new Cost(5, 4, 800, 640, 420, 140, 140, 140, 0, Duration.ofSeconds(1)))); }
    public static UpgradeBuildingInfo getBallistaUpgradeInfo(int currentLevel){
        return Ballista.upgradeBallistaCost.get(currentLevel);
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
