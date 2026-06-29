package model.building;

import model.world.Coordinate;

import java.util.Map;
import java.util.UUID;

public class Plant {
    private final UUID id;
    private final PlantType type;
    private final Coordinate position;
    private int neutralizationPower;

    public Plant(PlantType type, Coordinate position, int laboratoryLevel) {
        this.id = UUID.randomUUID();
        this.type = type;
        this.position = position;
        this.neutralizationPower = type.getNeutralizationPower(laboratoryLevel);
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


}
