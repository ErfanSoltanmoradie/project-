package model.building;

import model.production.Production;
import model.world.Coordinate;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class WaterSoilPurifier extends Building implements Production, Serializable {

    private int production;
    private int consumeAmount;

    private static final Map<Integer, UpgradeBuildingInfo> upgradeWellsCost; //Integer ----> level

    static {
        upgradeWellsCost = new HashMap<>();

        upgradeWellsCost.put(1, new UpgradeBuildingInfo(1 ,1,
                new Cost(100, 50, 40, 10, 5, 5, 5, Duration.ofSeconds(1))));

        upgradeWellsCost.put(2, new UpgradeBuildingInfo(1 ,1,
                new Cost(100, 50, 40, 10, 5, 5, 5, Duration.ofSeconds(1))));

        upgradeWellsCost.put(3, new UpgradeBuildingInfo(1 ,1,
                new Cost(100, 50, 40, 10, 5, 5, 5, Duration.ofSeconds(1))));

        upgradeWellsCost.put(4, new UpgradeBuildingInfo(1 ,1,
                new Cost(100, 50, 40, 10, 5, 5, 5, Duration.ofSeconds(1))));
    }

    public WaterSoilPurifier(BuildingType type, Coordinate position, int production) {
        super(type, position, 8, 8);
        this.production = production;
        this.consumeAmount = this.production * 2;
    }

    public static UpgradeBuildingInfo getWellUpgradeInfo(int currentLevel){
        return WaterSoilPurifier.upgradeWellsCost.get(currentLevel);
    }

    @Override
    public void upgrade() {
        int level = this.getLevel();
        this.consumeAmount *= 2;
        this.production *= 2;
        this.setLevel(level + 1);
        this.setBuildingStatus(BuildingStatus.ACTIVE);
    }

    @Override
    public void setProduction(int production) {
        this.production = production;
    }

    public int getProduction() {
        return production;
    }

    public int getConsumeAmount() {
        return consumeAmount;
    }

    public void setConsumeAmount(int consumeAmount) {
        this.consumeAmount = consumeAmount;
    }
}
