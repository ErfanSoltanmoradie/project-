package model.building;

import java.io.Serializable;
import java.time.Duration;

public class Cost  implements Serializable {
    private final int wood;
    private final int stone;
    private final int iron;
    private final int gunPowder;
    private final int cleanWater;
    private final int cleanSoil;
    private final int coin;
    private final Duration neededTime;

    public Cost(int wood, int stone, int iron, int gunPowder,
                int cleanWater, int cleanSoil, int coin, Duration neededTime) {

        this.wood = wood;
        this.iron = iron;
        this.gunPowder = gunPowder;
        this.cleanWater = cleanWater;
        this.cleanSoil = cleanSoil;
        this.coin = coin;
        this.stone = stone;
        this.neededTime = neededTime;
    }


    public static Cost buildCost(BuildingType buildingType) {
        return buildingType.getBaseBuildCost();
    }


    public static Cost upgradeCost(Building building){

        return switch (building.getType()) {
            case WOOD_MINE, STONE_MINE, IRON_MINE , SOIL_PURIFIER, DIRTY_WATER_MINE, GUNPOWDER_MINE ->
                    MinerBuilding.getMineUpgradeInfo(building.getLevel()).getCost();

            case WATER_STORAGE, SOIL_STORAGE, STONE_STORAGE, WOOD_STORAGE, IRON_STORAGE, GUNPOWDER_STORAGE ->
                    StorageBuilding.getUpgradeStoragesCost(building.getLevel()).getCost();

            case BALLISTA_DEFENSIVE -> Ballista.getBallistaUpgradeInfo(building.getLevel()).getCost();

            case CATAPULT_DEFENSIVE -> Catapult.getCatapultUpgradeInfo(building.getLevel()).getCost();

            case SENTINEL_DEFENSIVE -> Sentinel.getSentinelUpgradeInfo(building.getLevel()).getCost();

            case BARRACKS           -> Barrack.getBarrackUpgradeInfo(building.getLevel()).getCost();

            case ARMY_PRODUCER      -> ArmyProducer.getArmyProducerUpgradeInfo(building.getLevel()).getCost();


            default -> new Cost(0, 0, 0, 0, 0, 0, 0, Duration.ofMinutes(0));
        };
    }


    public int getWood() {return wood;}

    public int getStone() {return stone;}

    public int getIron() {return iron;}

    public int getGunPowder() {return gunPowder;}

    public int getCleanWater() {return cleanWater;}

    public int getCleanSoil() {return cleanSoil;}

    public int getCoin() {return coin;}

    public Duration getNeededTime() {
        return neededTime;
    }
}
