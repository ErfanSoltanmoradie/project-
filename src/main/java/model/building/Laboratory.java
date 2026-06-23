package model.building;

import model.world.Coordinate;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class Laboratory extends Building{

    private static final Map<Integer, UpgradeBuildingInfo> upgradeLaboratoryBuildingsCost;

    public Laboratory(BuildingType type, Coordinate position) {
        super(type, position);

    }

    static {
        upgradeLaboratoryBuildingsCost = new HashMap<>();

        upgradeLaboratoryBuildingsCost.put(1, new UpgradeBuildingInfo(1 ,1,
                new Cost(0, 0, 0, 0, 0, 0, 0, Duration.ofMinutes(10))));

        upgradeLaboratoryBuildingsCost.put(2, new UpgradeBuildingInfo(1 ,1,
                new Cost(0, 0, 0, 0, 0, 0, 0, Duration.ofMinutes(10))));

        upgradeLaboratoryBuildingsCost.put(3, new UpgradeBuildingInfo(1 ,1,
                new Cost(0, 0, 0, 0, 0, 0, 0, Duration.ofMinutes(10))));

        upgradeLaboratoryBuildingsCost.put(4, new UpgradeBuildingInfo(1 ,1,

                new Cost(0, 0, 0, 0, 0, 0, 0, Duration.ofMinutes(10))));
    }

    public static UpgradeBuildingInfo upgradeBuildingInfo(int level){
        return Laboratory.upgradeLaboratoryBuildingsCost.get(level);
    }

    @Override
    public void upgrade() {
        this.setLevel(this.getLevel() + 1);
        this.setBuildingStatus(BuildingStatus.ACTIVE);
    }
    private final Map<PlantType, Integer> plants = new HashMap<>();

    public int getTotalNeutralizationPower(){
        int total=0;
        for (Map.Entry<PlantType, Integer> entry : plants.entrySet()) {
            total += entry.getKey().getActualNeutralizationPower(this.getLevel()) * entry.getValue();
        }
        return total;
    }
    public void buildPlant(PlantType plantType){
        if(this.getLevel()<plantType.getRequiredLaboratoryLevel()) {
            System.out.println("YOU DONT HAVE THE NEEDED LEVEL FOR LABORATORY");
        }else{
            plants.put(plantType,plants.getOrDefault(plantType,0)+1);
        }

    }
    public void removePlant(PlantType plantType){
        int count = plants.getOrDefault(plantType, 0);
        if(count>1)
            plants.put(plantType, count - 1);
        else
            plants.remove(plantType);
    }


}
