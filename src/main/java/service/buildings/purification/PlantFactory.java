package service.buildings.purification;

import model.building.*;
import model.time.BuildTask;

public class PlantFactory {

    public static Plant createPlant(BuildTask buildTask, int labLevel){

        return switch (buildTask.getPlantType()) {
            case NRC -> new Plant(PlantType.NRC, buildTask.getCoordinate(), labLevel, 2 , 2);
            case SNRC -> new Plant(PlantType.SNRC, buildTask.getCoordinate(), labLevel, 2 , 2);
            case PSNRC -> new Plant(PlantType.PSNRC, buildTask.getCoordinate(), labLevel, 2, 2);

            default -> null;
        };
    }
}
