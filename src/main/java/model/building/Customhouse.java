package model.building;

import model.world.Coordinate;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class Customhouse extends Building {
    private double commission=0.05;
    private static final Map<Integer, UpgradeBuildingInfo> upgradeCustomhouseBuildingsCost;

    public Customhouse(BuildingType type, Coordinate position) {
        super(type, position);

    }
    static {
        upgradeCustomhouseBuildingsCost=new HashMap<>();
        upgradeCustomhouseBuildingsCost.put(1,new UpgradeBuildingInfo(2,3,new Cost(500,400,300,0,100,100,0,Duration.ofHours(4))));
        upgradeCustomhouseBuildingsCost.put(2,new UpgradeBuildingInfo(3,4,new Cost(600,450,300,0,180,180,0,Duration.ofHours(4))));
        upgradeCustomhouseBuildingsCost.put(3,new UpgradeBuildingInfo(4,5,new Cost(1000,800,600,0,200,200,0,Duration.ofHours(6))));
    }

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
