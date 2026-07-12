package service.buildings.purification;

import model.building.*;
import model.time.BuildTask;

public class PlantFactory {

    public static Plant createPlant(BuildTask buildTask, int labLevel){

        return switch (buildTask.getPlantType()) {
            case NRC -> new Plant(PlantType.NRC, buildTask.getCoordinate(), labLevel, 1 , 1);
<<<<<<< HEAD
            case SNRC -> new Plant(PlantType.SNRC, buildTask.getCoordinate(), labLevel, 1 , 1);
            case PSNRC -> new Plant(PlantType.PSNRC, buildTask.getCoordinate(), labLevel, 1 , 1);
=======

>>>>>>> 8f79b4d52b278d0d7991a8f6e08fac50fa5ee007

            default -> null;
        };
    }
}
