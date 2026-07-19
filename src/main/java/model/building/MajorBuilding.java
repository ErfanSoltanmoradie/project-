package model.building;

import model.world.Coordinate;

import java.io.Serializable;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class MajorBuilding extends Building  implements Serializable {

    public MajorBuilding(BuildingType type, Coordinate position) {
        super(type, position, 10, 10);
    }

    private static final Map<Integer, UpgradeBuildingInfo> upgradeMajorBuildingBuildingsCost;

    static {
        upgradeMajorBuildingBuildingsCost = new HashMap<>();

        upgradeMajorBuildingBuildingsCost.put(1, new UpgradeBuildingInfo(
                new Cost(1 ,1,200, 200,   200, 200, 200, 200, 0, Duration.ofSeconds(5))));

        upgradeMajorBuildingBuildingsCost.put(2, new UpgradeBuildingInfo(
                new Cost(1 ,1,300, 300,   300, 300, 300, 300, 0, Duration.ofSeconds(5))));

        upgradeMajorBuildingBuildingsCost.put(3, new UpgradeBuildingInfo(
                new Cost(1 ,1,200, 200,   200, 200, 200, 200, 0, Duration.ofSeconds(5))));

        upgradeMajorBuildingBuildingsCost.put(4, new UpgradeBuildingInfo(
                new Cost(1 ,1,200, 200,   200, 200, 200, 200, 0, Duration.ofSeconds(5))));
    }

    public static UpgradeBuildingInfo upgradeBuildingInfo(int level){
        return MajorBuilding.upgradeMajorBuildingBuildingsCost.get(level);
    }

    @Override
    public void upgrade() {
        this.setLevel(this.getLevel() + 1);
        this.setBuildingStatus(BuildingStatus.ACTIVE);
    }
}
