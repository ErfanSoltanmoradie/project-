package model.building;

import java.time.Duration;

public enum PlantType {
    NRC(1, 15, new Cost(0, 0, 0, 0, 50, 50, 0, Duration.ofHours(2))),
    SNRC(2, 35, new Cost(0, 0, 0, 0, 125, 125, 0, Duration.ofHours(4))),
    PSNRC(3, 60, new Cost(0, 0, 0, 0, 250, 250, 0, Duration.ofHours(6)));
    private final int requiredLaboratoryLevel;
    private int neutralizationPower;
    private final Cost basePlantCost;


    PlantType(int requiredLaboratoryLevel, int neutralizationPower, Cost basePlantCost) {
        this.requiredLaboratoryLevel = requiredLaboratoryLevel;
        this.neutralizationPower =neutralizationPower;
        this.basePlantCost = basePlantCost;
    }
    public int getActualNeutralizationPower(int laboratoryLevel) {
        return (int) (neutralizationPower * Math.pow(1.1, laboratoryLevel - 1));
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
