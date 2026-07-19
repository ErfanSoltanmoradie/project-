package service.buildings;

import model.building.BuildingType;

public class BuildingLimitExceededException extends RuntimeException {
    private final BuildingType buildingType;
    private final int maxAllowed;

    public BuildingLimitExceededException(BuildingType buildingType, int maxAllowed) {
        super("You have reached the maximum allowed limit of "+maxAllowed+buildingType+"buildings");
        this.buildingType = buildingType;
        this.maxAllowed = maxAllowed;
    }

    public BuildingType getBuildingType() {
        return buildingType;
    }

    public int getMaxAllowed() {
        return maxAllowed;
    }
}

