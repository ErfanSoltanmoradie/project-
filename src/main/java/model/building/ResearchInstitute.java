package model.building;

import model.world.Coordinate;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class ResearchInstitute extends Building{
    private int scienceLevel;

    private static final Map<Integer, UpgradeBuildingInfo> upgradeResearchBuildingsCost;

    public ResearchInstitute(BuildingType type, Coordinate position) {
        super(type, position);
        this.scienceLevel = 1;
    }

    static {
        upgradeResearchBuildingsCost = new HashMap<>();

        upgradeResearchBuildingsCost.put(1, new UpgradeBuildingInfo(1 ,1,
                new Cost(0, 0, 0, 0, 0, 0, 0, Duration.ofMinutes(10))));

        upgradeResearchBuildingsCost.put(2, new UpgradeBuildingInfo(1 ,1,
                new Cost(0, 0, 0, 0, 0, 0, 0, Duration.ofMinutes(10))));

        upgradeResearchBuildingsCost.put(3, new UpgradeBuildingInfo(1 ,1,
                new Cost(0, 0, 0, 0, 0, 0, 0, Duration.ofMinutes(10))));

        upgradeResearchBuildingsCost.put(4, new UpgradeBuildingInfo(1 ,1,
                new Cost(0, 0, 0, 0, 0, 0, 0, Duration.ofMinutes(10))));
    }

    public static UpgradeBuildingInfo upgradeBuildingInfo(int level){
        return ResearchInstitute.upgradeResearchBuildingsCost.get(level);
    }

    @Override
    public void upgrade() {
        this.setBuildingStatus(BuildingStatus.ACTIVE);

    }
}
