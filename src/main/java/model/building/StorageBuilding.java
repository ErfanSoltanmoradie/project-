package model.building;

import model.storage.Storage;
import model.village.Village;
import model.world.Coordinate;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class StorageBuilding extends Building implements Storage {
    private int capacity;

    private static final Map<Integer, UpgradeBuildingInfo> upgradeStoragesCost;

    static {
        upgradeStoragesCost = new HashMap<>();

        upgradeStoragesCost.put(1, new UpgradeBuildingInfo(1 ,1,
                new Cost(0, 0, 0, 0, 0, 0, 0, Duration.ofMinutes(10))));

        upgradeStoragesCost.put(2, new UpgradeBuildingInfo(1 ,1,
                new Cost(0, 0, 0, 0, 0, 0, 0, Duration.ofMinutes(10))));

        upgradeStoragesCost.put(3, new UpgradeBuildingInfo(1 ,1,
                new Cost(0, 0, 0, 0, 0, 0, 0, Duration.ofMinutes(10))));

        upgradeStoragesCost.put(4, new UpgradeBuildingInfo(1 ,1,
                new Cost(0, 0, 0, 0, 0, 0, 0, Duration.ofMinutes(10))));
    }

    public static UpgradeBuildingInfo getUpgradeStoragesCost(int level){
        return StorageBuilding.upgradeStoragesCost.get(level);
    }

    public StorageBuilding(BuildingType type, Coordinate position, int capacity) {
        super(type, position);
        setCapacity(capacity);
    }

    @Override
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public void upgrade() {
        int level = this.getLevel();
        this.setCapacity(this.capacity + 1000);
        this.setLevel(level + 1);
        this.setBuildingStatus(BuildingStatus.ACTIVE);
    }

    public int getCapacity() {
        return capacity;
    }
}
