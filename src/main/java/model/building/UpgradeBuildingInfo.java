package model.building;

public class UpgradeBuildingInfo {

    private final Cost cost;
    private final int requiredScienceLevel;
    private final int requiredMajorBuildingLevel;

    public UpgradeBuildingInfo(int requiredScienceLevel, int requiredMajorBuildingLevel, Cost cost) {
        this.requiredScienceLevel = requiredScienceLevel;
        this.requiredMajorBuildingLevel = requiredMajorBuildingLevel;
        this.cost = cost;
    }

    public Cost getCost() {
        return cost;
    }

    public int getRequiredScienceLevel() {
        return requiredScienceLevel;
    }

    public int getRequiredMajorBuildingLevel() {
        return requiredMajorBuildingLevel;
    }
}
