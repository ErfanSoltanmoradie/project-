package model.building;

import model.world.Coordinate;

import java.io.Serializable;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class MajorBuilding extends Building  implements Serializable {

    public MajorBuilding(BuildingType type, Coordinate position) {
        super(type, position, 3, 3);
    }

    private static final Map<Integer, UpgradeBuildingInfo> upgradeMajorBuildingBuildingsCost;

    static {
        upgradeMajorBuildingBuildingsCost = new HashMap<>();

        upgradeMajorBuildingBuildingsCost.put(1, new UpgradeBuildingInfo(1 ,1,
                new Cost(200, 200,   200, 200, 200, 200, 0, Duration.ofSeconds(5))));

        upgradeMajorBuildingBuildingsCost.put(2, new UpgradeBuildingInfo(1 ,1,
                new Cost(300, 300,   300, 300, 300, 300, 0, Duration.ofSeconds(5))));

        upgradeMajorBuildingBuildingsCost.put(3, new UpgradeBuildingInfo(1 ,1,
                new Cost(200, 200,   200, 200, 200, 200, 0, Duration.ofSeconds(5))));

        upgradeMajorBuildingBuildingsCost.put(4, new UpgradeBuildingInfo(1 ,1,
                new Cost(200, 200,   200, 200, 200, 200, 0, Duration.ofSeconds(5))));
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
