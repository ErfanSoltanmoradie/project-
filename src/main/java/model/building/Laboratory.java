package model.building;

import model.world.Coordinate;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class Laboratory extends Building{

    private static final Map<Integer, UpgradeBuildingInfo> upgradeLaboratoryBuildingsCost;

    public Laboratory(BuildingType type, Coordinate position) {
        super(type, position);

    }

    static {
        upgradeLaboratoryBuildingsCost = new HashMap<>();

        upgradeLaboratoryBuildingsCost.put(1, new UpgradeBuildingInfo(1 ,1,
                new Cost(0, 0, 0, 0, 0, 0, 0, Duration.ofMinutes(10))));

        upgradeLaboratoryBuildingsCost.put(2, new UpgradeBuildingInfo(1 ,1,
                new Cost(0, 0, 0, 0, 0, 0, 0, Duration.ofMinutes(10))));

        upgradeLaboratoryBuildingsCost.put(3, new UpgradeBuildingInfo(1 ,1,
                new Cost(0, 0, 0, 0, 0, 0, 0, Duration.ofMinutes(10))));

        upgradeLaboratoryBuildingsCost.put(4, new UpgradeBuildingInfo(1 ,1,
                new Cost(0, 0, 0, 0, 0, 0, 0, Duration.ofMinutes(10))));
    }

    public static UpgradeBuildingInfo upgradeBuildingInfo(int level){
        return Laboratory.upgradeLaboratoryBuildingsCost.get(level);
    }

    @Override
    public void upgrade() {
        this.setBuildingStatus(BuildingStatus.ACTIVE);

    }
}
