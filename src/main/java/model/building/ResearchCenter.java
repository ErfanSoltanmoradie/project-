package model.building;

import model.world.Coordinate;

import java.io.Serializable;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class ResearchCenter extends Building implements Serializable {
    private int scienceLevel;

    private static final Map<Integer, UpgradeBuildingInfo> upgradeResearchBuildingsCost;

    public ResearchCenter(BuildingType type, Coordinate position) {
        super(type, position, 7, 7);
        this.scienceLevel = 1;
    }

    static {
        upgradeResearchBuildingsCost = new HashMap<>();

        upgradeResearchBuildingsCost.put(1, new UpgradeBuildingInfo(1 ,1,
                new Cost(0, 0, 0, 0, 0, 0, 0, Duration.ofSeconds(10))));

        upgradeResearchBuildingsCost.put(2, new UpgradeBuildingInfo(1 ,1,
                new Cost(0, 0, 0, 0, 0, 0, 0, Duration.ofSeconds(10))));

        upgradeResearchBuildingsCost.put(3, new UpgradeBuildingInfo(1 ,1,
                new Cost(0, 0, 0, 0, 0, 0, 0, Duration.ofSeconds(10))));

        upgradeResearchBuildingsCost.put(4, new UpgradeBuildingInfo(1 ,1,
                new Cost(0, 0, 0, 0, 0, 0, 0, Duration.ofSeconds(10))));
    }

    public static UpgradeBuildingInfo upgradeBuildingInfo(int level){
        return ResearchCenter.upgradeResearchBuildingsCost.get(level);
    }

    @Override
    public void upgrade() {
        int level = this.getLevel();

        this.scienceLevel ++;
        this.setLevel(level + 1);
        this.setBuildingStatus(BuildingStatus.ACTIVE);

    }

    public int getScienceLevel() {
        return scienceLevel;
    }

    public void setScienceLevel(int scienceLevel) {
        this.scienceLevel = scienceLevel;
    }
}
