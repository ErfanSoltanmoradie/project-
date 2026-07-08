package service.buildings;

import model.building.*;
import model.time.BuildTask;

public class BuildingFactory{

    public static Building createBuilding(BuildTask buildTask){
        return switch (buildTask.getBuildingType()) {

            case IRON_MINE -> new MinerBuilding(BuildingType.IRON_MINE, buildTask.getCoordinate(), 100);
            case SOIL_PURIFIER -> new WaterSoilPurifier(BuildingType.SOIL_PURIFIER, buildTask.getCoordinate(), 100);
            case DIRTY_SOIL_MINE ->new MinerBuilding(BuildingType.DIRTY_SOIL_MINE, buildTask.getCoordinate(), 100);
            case WATER_PURIFIER -> new WaterSoilPurifier(BuildingType.WATER_PURIFIER, buildTask.getCoordinate(), 100);
            case DIRTY_WATER_MINE -> new MinerBuilding(BuildingType.DIRTY_WATER_MINE, buildTask.getCoordinate(), 100);
            case WOOD_MINE -> new MinerBuilding(BuildingType.WOOD_MINE, buildTask.getCoordinate(), 100);
            case STONE_MINE -> new MinerBuilding(BuildingType.STONE_MINE, buildTask.getCoordinate(), 100);
            case GUNPOWDER_MINE -> new MinerBuilding(BuildingType.GUNPOWDER_MINE, buildTask.getCoordinate(), 100);

            case SOIL_STORAGE -> new StorageBuilding(BuildingType.SOIL_STORAGE, buildTask.getCoordinate(), 2000);
            case WATER_STORAGE -> new StorageBuilding(BuildingType.WATER_STORAGE, buildTask.getCoordinate(), 2000);
            case WOOD_STORAGE -> new StorageBuilding(BuildingType.WOOD_STORAGE, buildTask.getCoordinate(), 2000);
            case IRON_STORAGE -> new StorageBuilding(BuildingType.IRON_STORAGE, buildTask.getCoordinate(), 2000);
            case STONE_STORAGE -> new StorageBuilding(BuildingType.STONE_STORAGE, buildTask.getCoordinate(), 2000);
            case GUNPOWDER_STORAGE -> new StorageBuilding(BuildingType.GUNPOWDER_STORAGE, buildTask.getCoordinate(), 2000);

            case BALLISTA_DEFENSIVE -> new Ballista(BuildingType.BALLISTA_DEFENSIVE, buildTask.getCoordinate());
            case CATAPULT_DEFENSIVE -> new Catapult(BuildingType.CATAPULT_DEFENSIVE, buildTask.getCoordinate());
            case SENTINEL_DEFENSIVE -> new Sentinel(BuildingType.SENTINEL_DEFENSIVE, buildTask.getCoordinate());

            case BARRACKS      ->  new Barrack(BuildingType.BARRACKS, buildTask.getCoordinate());
            case ARMY_PRODUCER ->  new ArmyProducer(BuildingType.ARMY_PRODUCER, buildTask.getCoordinate());


            // more building need to be added
            default -> null;
        };
    }
}
