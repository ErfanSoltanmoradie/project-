package model.building;

import model.production.Production;
import model.world.Coordinate;

import java.io.Serializable;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class MinerBuilding extends Building implements Production , Serializable {

    private int production;

    private static final Map<Integer, UpgradeBuildingInfo> upgradeMinersCost; //Integer ----> level

    static {
        upgradeMinersCost = new HashMap<>();

        upgradeMinersCost.put(1, new UpgradeBuildingInfo(
                new Cost(1 ,1,10, 10, 10, 10, 10, 10, 10, Duration.ofSeconds(5))));

        upgradeMinersCost.put(2, new UpgradeBuildingInfo(
                new Cost(1 ,1,20, 20, 20, 20, 20, 20, 20, Duration.ofSeconds(10))));

        upgradeMinersCost.put(3, new UpgradeBuildingInfo(
                new Cost(1 ,1,100, 50, 40, 10, 5, 5, 5, Duration.ofSeconds(10))));

        upgradeMinersCost.put(4, new UpgradeBuildingInfo(
                new Cost(1 ,1,100, 50, 40, 10, 5, 5, 5, Duration.ofSeconds(10))));
    }

    public MinerBuilding( BuildingType type, Coordinate position, int production) {
        super(type, position, 7, 7);
        this.production = production;
    }

    @Override
    public void setProduction(int production) {
        this.production = production;
    }

    public static UpgradeBuildingInfo getMineUpgradeInfo(int currentLevel){
        return MinerBuilding.upgradeMinersCost.get(currentLevel);
    }

    @Override
    public void upgrade() {
        int level = this.getLevel();
        this.production += 10;
        this.setLevel(level + 1);
        this.setBuildingStatus(BuildingStatus.ACTIVE);
    }

    public int getProduction() {
        return production;
    }
}
