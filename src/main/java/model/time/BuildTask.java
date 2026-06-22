package model.time;

import model.building.Building;
import model.building.BuildingStatus;
import model.building.BuildingType;
import model.building.MinerBuilding;
import model.village.Village;
import model.world.Coordinate;
import service.buildings.BuildingFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class BuildTask extends TimedOperation {

    private BuildingType buildingType;
    private Coordinate coordinate;

    public BuildTask(Instant startTime, Instant finishTime, BuildingType buildingType, Coordinate coordinate) {
        super(startTime, finishTime, TimedOperationType.BUILD_TASK);
        this.buildingType = buildingType;
        this.coordinate = coordinate;
    }

    @Override
    public void execute(Village village, List<TimedOperation> toAdd) {

        Building building = BuildingFactory.createBuilding(this);

        if (building != null){

            building.setBuildingStatus(BuildingStatus.ACTIVE);
            village.getBuildings().put(building.getId(), building);

            if(building.getType() == BuildingType.WATER_PURIFIER || building.getType() == BuildingType.SOIL_PURIFIER){

                PurificationWaterAndSoilTask purificationWaterAndSoilTask = new PurificationWaterAndSoilTask(Instant.now(), Instant.now().plus(Duration.ofSeconds(1)),
                        TimedOperationType.PURIFICATION_WATER_AND_SOIL_TASK, Duration.ofSeconds(1), building.getId());

                toAdd.add(purificationWaterAndSoilTask);
            } else {
                ProductionTask productionTask = new ProductionTask(Instant.now(), Instant.now().plus(Duration.ofSeconds(1)),
                        TimedOperationType.PRODUCTION_TASK, Duration.ofSeconds(1), building.getId());

                toAdd.add(productionTask);
            }
        }
    }

    public BuildingType getBuildingType() {
        return buildingType;
    }

    public void setBuildingType(BuildingType buildingType) {
        this.buildingType = buildingType;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }
}
