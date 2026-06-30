package model.time;

import model.building.Building;
import model.building.BuildingStatus;
import model.building.BuildingType;
import model.building.MinerBuilding;
import model.village.Village;
import model.world.Coordinate;
import service.buildings.BuildingFactory;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class BuildTask extends TimedOperation implements Serializable {

    private BuildingType buildingType;
    private Coordinate coordinate;

    public BuildTask(Instant startTime, Duration neededTime, BuildingType buildingType, Coordinate coordinate) {
        super(startTime, neededTime, TimedOperationType.BUILD_TASK);
        this.buildingType = buildingType;
        this.coordinate = coordinate;
    }

    @Override
    public TaskResult execute() {

        Building building = BuildingFactory.createBuilding(this);
        TaskResult taskResult = new TaskResult();

        if (building != null){

            building.setBuildingStatus(BuildingStatus.ACTIVE);
            taskResult.getBuildingsToAdd().put(building.getId(), building);
            taskResult.getProductionBuildingsToReschedule().add(building.getId());
            /*if(building.getType() == BuildingType.WATER_PURIFIER || building.getType() == BuildingType.SOIL_PURIFIER){

                PurificationWaterAndSoilTask purificationWaterAndSoilTask = new PurificationWaterAndSoilTask(Instant.now(), Instant.now().plus(Duration.ofSeconds(1)),
                        TimedOperationType.PURIFICATION_WATER_AND_SOIL_TASK, Duration.ofSeconds(1), building.getId());

                taskResult.getTasksToAdd().put(purificationWaterAndSoilTask.getId(), purificationWaterAndSoilTask);
            } else {
                ProductionTask productionTask = new ProductionTask(Instant.now(), Instant.now().plus(Duration.ofSeconds(1)),
                        TimedOperationType.PRODUCTION_TASK, Duration.ofSeconds(1), building);

                taskResult.getTasksToAdd().put(productionTask.getId(), productionTask);
            }/*/
        }
        return taskResult;
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
