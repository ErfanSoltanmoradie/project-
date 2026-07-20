package model.building;

import model.village.Village;
import model.world.Coordinate;

import java.io.Serializable;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class Laboratory extends Building implements Serializable {

    private static final Map<Integer, UpgradeBuildingInfo> upgradeLaboratoryBuildingsCost;

    public Laboratory(BuildingType type, Coordinate position) {
        super(type, position, 7, 7);

    }

    static {
        upgradeLaboratoryBuildingsCost = new HashMap<>();

        upgradeLaboratoryBuildingsCost.put(1, new UpgradeBuildingInfo(
                new Cost(2, 1, 250, 200, 150, 0, 50, 50, 0, Duration.ofSeconds(1))));

        upgradeLaboratoryBuildingsCost.put(2, new UpgradeBuildingInfo(
                new Cost(3, 2, 500, 400, 300, 0, 100, 100, 0, Duration.ofSeconds(2))));

        upgradeLaboratoryBuildingsCost.put(3, new UpgradeBuildingInfo(
                new Cost(4, 3, 750, 600, 450, 0, 150, 150, 0, Duration.ofSeconds(2))));

        upgradeLaboratoryBuildingsCost.put(4, new UpgradeBuildingInfo(
                new Cost(5, 4, 1000, 800, 600, 0, 200, 200, 0, Duration.ofSeconds(2))));
    }

    public static UpgradeBuildingInfo upgradeBuildingInfo(int level){
        return Laboratory.upgradeLaboratoryBuildingsCost.get(level);
    }

    @Override
    public void upgrade() {
        this.setLevel(this.getLevel() + 1);
        this.setBuildingStatus(BuildingStatus.ACTIVE);

    }

    public void upgradeWithVillage(Village village) {
        int oldLevel = this.getLevel();
        upgrade();
        for (Plant plant : village.getPlants().values()) {
            if (plant.getType().getRequiredLaboratoryLevel() <= oldLevel) {
                plant.upgradeNeutralizationPower();
            }
        }
    }
}