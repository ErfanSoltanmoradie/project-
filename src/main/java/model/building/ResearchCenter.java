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

        upgradeResearchBuildingsCost.put(1, new UpgradeBuildingInfo(
                new Cost(2, 0, 500, 350, 250, 0, 150, 150, 0, Duration.ofSeconds(10))));

        upgradeResearchBuildingsCost.put(2, new UpgradeBuildingInfo(
                new Cost(3, 0, 600, 500, 450, 0, 200, 200, 0, Duration.ofSeconds(10))));

        upgradeResearchBuildingsCost.put(3, new UpgradeBuildingInfo(
                new Cost(4, 0, 800, 700, 600, 0, 300, 300, 0, Duration.ofSeconds(10))));

        upgradeResearchBuildingsCost.put(4, new UpgradeBuildingInfo(
                new Cost(5, 0, 1000, 900, 800, 0, 400, 400, 0, Duration.ofSeconds(10))));
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
