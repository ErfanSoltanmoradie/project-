package model.building;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;

public enum PlantType {
    NRC(1, 15, new Cost(0, 0, 0, 0, 50, 50, 0, Duration.ofHours(2))),
    SNRC(2, 35, new Cost(0, 0, 0, 0, 125, 125, 0, Duration.ofHours(4))),
    PSNRC(3, 60, new Cost(0, 0, 0, 0, 250, 250, 0, Duration.ofHours(6)));
    private final int requiredLaboratoryLevel;
    private final int neutralizationPower;
    private final Cost basePlantCost;


    PlantType(int requiredLaboratoryLevel, int neutralizationPower, Cost basePlantCost) {
        this.requiredLaboratoryLevel = requiredLaboratoryLevel;
        this.neutralizationPower =neutralizationPower;
        this.basePlantCost = basePlantCost;
    }

    public int getNeutralizationPower(int laboratoryLevel) {
<<<<<<< HEAD
        int levelDifference = laboratoryLevel - requiredLaboratoryLevel;
        return (int) (neutralizationPower * Math.pow(1.1, levelDifference));
    }

    public static int getTotalNeutralizationPower(Map<UUID, Plant> plants) {
        int total = 0;
        for (Plant plant : plants.values()) {
            total += plant.getNeutralizationPower();
        }
        return total;
=======
        return (int) (neutralizationPower * Math.pow(1.1, laboratoryLevel - 1));
>>>>>>> 624e47b62d8161b8725088c5913d6fb183f1aa32
    }

    public static int getTotalNeutralizationPower(Map<UUID, Plant> plants) {
        int total = 0;
        for (Plant plant : plants.values()) {
            total += plant.getNeutralizationPower();
        }
        return total;
    }

    public int getRequiredLaboratoryLevel() {
        return requiredLaboratoryLevel;
    }

    public int getNeutralizationPower() {
        return neutralizationPower;
    }

    public Cost getBasePlantCost() {
        return basePlantCost;
    }
}
