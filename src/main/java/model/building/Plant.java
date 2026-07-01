package model.building;

import model.world.Coordinate;

import java.util.Map;
import java.util.UUID;

public class Plant {
    private final UUID id;
    private final PlantType type;
    private final Coordinate position;
    private int neutralizationPower;

<<<<<<< HEAD
=======
    private final int builtAtLevel;

>>>>>>> 624e47b62d8161b8725088c5913d6fb183f1aa32
    public Plant(PlantType type, Coordinate position, int laboratoryLevel) {
        this.id = UUID.randomUUID();
        this.type = type;
        this.position = position;
<<<<<<< HEAD
        this.neutralizationPower = type.getNeutralizationPower(laboratoryLevel);
    }
=======
        this.neutralizationPower = type.getNeutralizationPower();
        this.builtAtLevel = laboratoryLevel;
    }

>>>>>>> 624e47b62d8161b8725088c5913d6fb183f1aa32
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

<<<<<<< HEAD

}
=======
    public int getBuiltAtLevel() {
        return builtAtLevel;
    }

}

>>>>>>> 624e47b62d8161b8725088c5913d6fb183f1aa32
