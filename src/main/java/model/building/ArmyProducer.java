package model.building;

import model.world.Coordinate;

import java.io.Serializable;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class ArmyProducer extends Building implements Serializable {

    public ArmyProducer(BuildingType type, Coordinate position) {
        super(type, position, 9, 9);
    }

    private static final Map<Integer, UpgradeBuildingInfo> upgradeArmyProducerCost; //Integer ----> level
    static {
        upgradeArmyProducerCost = new HashMap<>();

        upgradeArmyProducerCost.put(1, new UpgradeBuildingInfo(
                new Cost(2, 1, 200, 200, 150, 100, 50, 50, 0, Duration.ofSeconds(1))));

        upgradeArmyProducerCost.put(2, new UpgradeBuildingInfo(
                new Cost(3, 2, 400, 400, 300, 200, 100, 100, 0, Duration.ofSeconds(1))));

        upgradeArmyProducerCost.put(3, new UpgradeBuildingInfo(
                new Cost(4, 3, 600, 600, 450, 300, 150, 150, 0, Duration.ofSeconds(1))));

        upgradeArmyProducerCost.put(4, new UpgradeBuildingInfo(
                new Cost(5, 4, 800, 800, 600, 400, 200, 200, 0, Duration.ofSeconds(1)))); }
    public static UpgradeBuildingInfo getArmyProducerUpgradeInfo(int currentLevel){
        return ArmyProducer.upgradeArmyProducerCost.get(currentLevel);
    }

    @Override
    public void upgrade() {
        if(getLevel()>=5)
            return;
        setLevel(getLevel()+1);
        setBuildingStatus(BuildingStatus.ACTIVE);
    }
    public int getMaxTrainingCount(){
        return switch (getLevel()){
            case 1 -> 10;
            case 2 -> 15;
            case 3 -> 20;
            case 4 -> 25;
            case 5 -> 30;
            default -> 0;
        };
    }
    public double getPowerMultiplier(){
        return switch (getLevel()){
            case 4 -> 1.2;
            case 5 -> 1.4;
            default -> 1.0;
        };
    }
}