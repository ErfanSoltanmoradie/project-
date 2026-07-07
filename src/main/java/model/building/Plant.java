package model.building;

import model.world.Coordinate;

import java.util.Map;
import java.util.UUID;

public class Plant {
    private final UUID id;
    private final PlantType type;
    private  Coordinate position;
    private int neutralizationPower;
    private int width;
    private int height;

    public Plant(PlantType type, Coordinate position, int laboratoryLevel, int width, int height) {
        this.id = UUID.randomUUID();
        this.type = type;
        this.position = position;
        this.neutralizationPower = type.getNeutralizationPower(laboratoryLevel);
        this.height = height;
        this.width = width;
    }
    public void upgradeNeutralizationPower() {
        this.neutralizationPower = (int) (this.neutralizationPower * 1.1);
    }

    public UUID getId() {
        return id;
    }

    public PlantType getType() {
        return type;
    }

    public Coordinate getPosition() {
        return position;
    }

    public int getNeutralizationPower() {
        return neutralizationPower;
    }

    public int getWidth() {
        return width;
    }

    public void setPosition(Coordinate position) {
        this.position = position;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}