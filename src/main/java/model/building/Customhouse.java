package model.building;

import model.world.Coordinate;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class Customhouse extends Building {
    private double commission=0.05;
    private static final Map<Integer, UpgradeBuildingInfo> upgradeCustomhouseBuildingsCost;

    public Customhouse(BuildingType type, Coordinate position) {
        super(type, position, 9, 9);

    }
    static {
        upgradeCustomhouseBuildingsCost=new HashMap<>();
        upgradeCustomhouseBuildingsCost.put(1, new UpgradeBuildingInfo(new Cost(3, 2, 500, 400, 300, 0, 100, 100, 0, Duration.ofSeconds(4))));
        upgradeCustomhouseBuildingsCost.put(2, new UpgradeBuildingInfo(new Cost(4, 3, 1000, 800, 600, 0, 200, 200, 0, Duration.ofSeconds(5))));
        upgradeCustomhouseBuildingsCost.put(3, new UpgradeBuildingInfo(new Cost(5, 4, 1600, 1200, 900, 0, 300, 300, 0, Duration.ofSeconds(6)))); }

    public static UpgradeBuildingInfo upgradeBuildingInfo(int level) {
        return upgradeCustomhouseBuildingsCost.get(level);
    }

    @Override
    public void upgrade() {
        this.setLevel(this.getLevel() + 1);
        this.setBuildingStatus(BuildingStatus.ACTIVE);
        switch (this.getLevel()){
            case 1 -> this.setCommission(0.05);
            case 2 -> this.setCommission(0.03);
            case 3 -> this.setCommission(0.02);
        }

    }
    public double getCommission() {
        return commission;
    }
    public void setCommission(double commission) {
        this.commission = commission;
    }

}