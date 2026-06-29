package model.time;

import model.building.Building;
import model.building.BuildingStatus;
import model.building.Laboratory;
import model.village.Village;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class UpgradeTask extends TimedOperation {

    private UUID buildingId;

    public UpgradeTask(Instant startTime, Instant finishTime, UUID buildingId) {
        super(startTime, finishTime, TimedOperationType.UPGRADE_TASK);
        this.buildingId = buildingId;
    }

    @Override
    public void execute(Village village, List<TimedOperation> toAdd) {

        Building building = village.getBuildings().get(this.getBuildingId());

        if (building != null && building.getBuildingStatus() == BuildingStatus.UPGRADING) {
            if (building instanceof Laboratory lab) {
                lab.upgradeWithVillage(village);
            } else {
                building.upgrade();
            }
            building.setBuildingStatus(BuildingStatus.ACTIVE);
        }
    }

    public UUID getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(UUID buildingId) {
        this.buildingId = buildingId;
    }
}