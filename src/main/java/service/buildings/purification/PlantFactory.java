package service.buildings.purification;

import model.building.*;
import model.time.BuildTask;

public class PlantFactory {

    public static Plant createPlant(BuildTask buildTask, int labLevel){

        return switch (buildTask.getPlantType()) {
            case NRC -> new Plant(PlantType.NRC, buildTask.getCoordinate(), labLevel, 1 , 1);


            default -> null;
        };
    }
}
