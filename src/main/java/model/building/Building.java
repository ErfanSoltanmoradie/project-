package model.building;

import model.world.Coordinate;

import java.io.Serializable;
import java.util.UUID;

public abstract class Building implements Serializable {

    private final UUID id;
    private final BuildingType type;
    private final Coordinate position;
    private int level;
    private BuildingStatus buildingStatus;

    public Building(BuildingType type, Coordinate position) {
        this.id = UUID.randomUUID();
        this.level = 1;
        this.type = type;
        this.position = position;
        this.buildingStatus = BuildingStatus.BUILDING;
    }

    public abstract void upgrade();

    public BuildingStatus getBuildingStatus() {
        return buildingStatus;
    }

    public void setBuildingStatus(BuildingStatus buildingStatus) {
        this.buildingStatus = buildingStatus;
    }

    public UUID getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }

    public BuildingType getType() {
        return type;
    }

    public Coordinate getPosition() {
        return position;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
