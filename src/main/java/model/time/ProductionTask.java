package model.time;

import model.building.*;
import model.resources.ResourcesType;
import model.village.Village;
import service.resource.ResourcesManagement;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class ProductionTask extends TimedOperation implements Serializable {
    private final Duration neededTime;
    private final UUID buildingId;
    private ResourcesManagement resources;

    public ProductionTask(Instant startTime, Instant finishTime,
                          TimedOperationType timedOperationType, Duration neededTime, UUID buildingId) {
        super(startTime, finishTime, timedOperationType);
        this.neededTime = neededTime;
        this.buildingId = buildingId;
    }

    @Override
    public void execute(Village village, List<TimedOperation> toAdd) {
        this.resources = village.getResourcesManagement();

        Building building = village.getBuildings().get(this.buildingId);

        if (building == null) return;

        if(building.getBuildingStatus() != BuildingStatus.ACTIVE){
            return;
        }

        switch (building) {

            case WaterSoilPurifier waterSoilPurifier -> {
                int producedAmount = waterSoilPurifier.getProduction();
                int consumeAmount = waterSoilPurifier.getConsumeAmount();
                switch (waterSoilPurifier.getType()) {
                    case SOIL_PURIFIER:
                        resources.addResource(producedAmount, ResourcesType.CLEAN_SOIL);
                        resources.withdrawResource(consumeAmount, ResourcesType.DIRTY_SOIL);
                        break;
                    case WATER_PURIFIER:
                        resources.addResource(producedAmount, ResourcesType.CLEAN_WATER);
                        resources.withdrawResource(consumeAmount, ResourcesType.DIRTY_WATER);
                        break;
                }
            }
            case MinerBuilding minerBuilding -> {
                int producedAmount = minerBuilding.getProduction();

                switch (minerBuilding.getType()) {
                    case STONE_MINE:
                        resources.addResource(producedAmount, ResourcesType.STONE);
                        break;
                    case WOOD_MINE:
                        resources.addResource(producedAmount, ResourcesType.WOOD);
                        break;
                    case DIRTY_WATER_MINE:
                        resources.addResource(producedAmount, ResourcesType.DIRTY_WATER);
                        break;
                    case DIRTY_SOIL_MINE:
                        resources.addResource(producedAmount, ResourcesType.DIRTY_SOIL);
                        break;
                    case IRON_MINE:
                        resources.addResource(producedAmount, ResourcesType.IRON);
                        break;
                    case GUNPOWDER_MINE:
                        resources.addResource(producedAmount, ResourcesType.GUN_POWDER);
                        break;
                }
            }
            default -> {
            }
        }

        ProductionTask productionTask = new ProductionTask(Instant.now(),
                Instant.now().plus(neededTime), TimedOperationType.PRODUCTION_TASK, this.neededTime,  this.buildingId);
        toAdd.add(productionTask);
    }
}
