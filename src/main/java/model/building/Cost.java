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

    public static Cost buildCost(PlantType plantType) {return plantType.getBasePlantCost();}

    private static Cost safeCost(UpgradeBuildingInfo info) {
        if (info == null) {
            return new Cost(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE,
                    Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Duration.ofDays(9999));
        }
        return info.getCost();
    }

    public static Cost upgradeCost(Building building){

        return switch (building.getType()) {
            case WOOD_MINE, STONE_MINE, IRON_MINE , SOIL_PURIFIER, DIRTY_WATER_MINE, GUNPOWDER_MINE ->
                    safeCost(MinerBuilding.getMineUpgradeInfo(building.getLevel()));

            case WATER_STORAGE, SOIL_STORAGE, STONE_STORAGE, WOOD_STORAGE, IRON_STORAGE, GUNPOWDER_STORAGE ->
                    safeCost(StorageBuilding.getUpgradeStoragesCost(building.getLevel()));

            case BALLISTA_DEFENSIVE -> safeCost(Ballista.getBallistaUpgradeInfo(building.getLevel()));

            case CATAPULT_DEFENSIVE -> safeCost(Catapult.getCatapultUpgradeInfo(building.getLevel()));

            case SENTINEL_DEFENSIVE -> safeCost(Sentinel.getSentinelUpgradeInfo(building.getLevel()));


            case LABORATORY -> safeCost(Laboratory.upgradeBuildingInfo(building.getLevel()));

            case CUSTOMHOUSE -> safeCost(Customhouse.upgradeBuildingInfo(building.getLevel()));


            case BARRACKS  -> safeCost(Barrack.getBarrackUpgradeInfo(building.getLevel()));

            case ARMY_PRODUCER -> safeCost(ArmyProducer.getArmyProducerUpgradeInfo(building.getLevel()));


            case MAJOR_BUILDING -> safeCost(MajorBuilding.upgradeBuildingInfo(building.getLevel()));

            case RESEARCH_CENTER -> safeCost(ResearchCenter.upgradeBuildingInfo(building.getLevel()));


            default -> new Cost(0, 0, 0, 0, 0, 0, 0, Duration.ofMinutes(0));
        };
    }

    public static Cost allianceCost(){
        return new Cost(500, 500, 300, 0, 0, 0, 100,Duration.ofMinutes(0));
    }

    public int getWood() {return wood;}

    public boolean isMaxLevelReached() {
        return this.wood == Integer.MAX_VALUE;
    }

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