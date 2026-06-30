package model.time;

import model.building.Building;
import model.building.MinerBuilding;
import model.building.WaterSoilPurifier;
import model.resources.ResourcesType;

import java.time.Duration;
import java.time.Instant;

public class ProductionTaskFactory {

    public static ProductionTask buildProductionTask(Building building){
        switch (building){

            case WaterSoilPurifier waterSoilPurifier -> {

                switch (waterSoilPurifier.getType()) {
                    case SOIL_PURIFIER:
                        return new ProductionTask(Instant.now(), Duration.ofMinutes(1), TimedOperationType.PRODUCTION_TASK,
                                building.getId(), ResourcesType.CLEAN_SOIL, waterSoilPurifier.getProduction(),ResourcesType.DIRTY_SOIL ,waterSoilPurifier.getConsumeAmount());

                    case WATER_PURIFIER:
                        return new ProductionTask(Instant.now(), Duration.ofMinutes(1), TimedOperationType.PRODUCTION_TASK,
                                building.getId(), ResourcesType.CLEAN_WATER, waterSoilPurifier.getProduction(),ResourcesType.DIRTY_WATER , waterSoilPurifier.getConsumeAmount());
                }
            }
            case MinerBuilding minerBuilding -> {

                switch (minerBuilding.getType()) {
                    case STONE_MINE:
                        return new ProductionTask(Instant.now(), Duration.ofSeconds(1), TimedOperationType.PRODUCTION_TASK,
                                building.getId(), ResourcesType.STONE, minerBuilding.getProduction(), ResourcesType.STONE,0);

                    case WOOD_MINE:
                        return new ProductionTask(Instant.now(), Duration.ofSeconds(1), TimedOperationType.PRODUCTION_TASK,
                                building.getId(), ResourcesType.WOOD, minerBuilding.getProduction(), ResourcesType.WOOD, 0 );

                    case DIRTY_WATER_MINE:
                        return new ProductionTask(Instant.now(), Duration.ofSeconds(1), TimedOperationType.PRODUCTION_TASK,
                                building.getId(), ResourcesType.DIRTY_WATER, minerBuilding.getProduction(), ResourcesType.DIRTY_WATER,0 );

                    case DIRTY_SOIL_MINE:
                        return new ProductionTask(Instant.now(), Duration.ofSeconds(1), TimedOperationType.PRODUCTION_TASK,
                                building.getId(), ResourcesType.DIRTY_SOIL, minerBuilding.getProduction(), ResourcesType.DIRTY_SOIL,0);

                    case IRON_MINE:
                        return new ProductionTask(Instant.now(), Duration.ofSeconds(1), TimedOperationType.PRODUCTION_TASK,
                                building.getId(), ResourcesType.IRON, minerBuilding.getProduction(), ResourcesType.IRON, 0);

                    case GUNPOWDER_MINE:
                        return new ProductionTask(Instant.now(), Duration.ofSeconds(1), TimedOperationType.PRODUCTION_TASK,
                                building.getId(), ResourcesType.GUN_POWDER, minerBuilding.getProduction(), ResourcesType.GUN_POWDER, 0);
                }
            }
            default -> {
                return null;
            }
        }
        return null;
    }

}
