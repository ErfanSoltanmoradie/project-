package model.time;

import model.building.*;
import model.village.Village;
import model.world.Coordinate;
import service.buildings.BuildingFactory;
import service.buildings.purification.PlantFactory;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class BuildTask extends TimedOperation implements Serializable {

    private BuildingType buildingType;
    private PlantType plantType;
    private Coordinate coordinate;
    private int labLevel;

    public BuildTask(Instant startTime, Duration neededTime, BuildingType buildingType, Coordinate coordinate) {
        super(startTime, neededTime, TimedOperationType.BUILD_TASK);
        this.buildingType = buildingType;
        this.coordinate = coordinate;
    }

    public BuildTask(Instant startTime, Duration neededTime, PlantType plantType, Coordinate coordinate, int labLevel) {
        super(startTime, neededTime, TimedOperationType.BUILD_TASK);
        this.plantType = plantType;
        this.coordinate = coordinate;
        this.labLevel = labLevel;
    }

    @Override
    public TaskResult execute() {

        Building building = null;
        Plant plant = null;

        if(this.buildingType != null){
             building = BuildingFactory.createBuilding(this);
        }

        if(this.plantType != null){
            plant = PlantFactory.createPlant(this, this.labLevel);
        }

        TaskResult taskResult = new TaskResult();

        if (building != null){

            building.setBuildingStatus(BuildingStatus.ACTIVE);
            taskResult.getBuildingsToAdd().put(building.getId(), building);
            taskResult.getProductionBuildingsToReschedule().add(building.getId());

        }

        if(plant != null){
            taskResult.getPlantsToAdd().put(plant.getId(), plant);
        }

        return taskResult;
    }

    public PlantType getPlantType() {
        return plantType;
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
