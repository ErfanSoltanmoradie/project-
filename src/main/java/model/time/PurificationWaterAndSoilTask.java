package model.time;

import model.building.Building;
import model.building.WaterSoilPurifier;
import model.resources.ResourcesType;
import model.village.Village;
import service.resource.ResourcesManagement;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public class PurificationWaterAndSoilTask extends TimedOperation{

    private final UUID buildingId;
    private final Duration neededTime;
    private ResourcesManagement resources;

    public PurificationWaterAndSoilTask(Instant startTime, Instant finishTime,
                                            TimedOperationType timedOperationType, Duration neededTime, UUID buildingId) {
        super(startTime, finishTime, timedOperationType);
        this.buildingId = buildingId;
        this.neededTime = neededTime;
    }

    @Override
    public void execute(Village village) {
        this.resources = village.getResourcesManagement();
        Building building = village.getBuildings().get(this.buildingId);

        if(building instanceof WaterSoilPurifier){
            WaterSoilPurifier waterSoilPurifier = (WaterSoilPurifier) building;

            switch (waterSoilPurifier.getType()){

                case WATER_PURIFIER:
                    if(waterSoilPurifier.getConsumeAmount() <= village.getResources().getAmount(ResourcesType.DIRTY_WATER)){
                        this.resources.withdrawResource(waterSoilPurifier.getConsumeAmount(), ResourcesType.DIRTY_WATER);
                        this.resources.addResource(waterSoilPurifier.getProduction(), ResourcesType.CLEAN_WATER);
                    }
                    break;

                case SOIL_PURIFIER:
                    if(waterSoilPurifier.getConsumeAmount() <= village.getResources().getAmount(ResourcesType.DIRTY_SOIL)){
                        this.resources.withdrawResource(waterSoilPurifier.getConsumeAmount(), ResourcesType.DIRTY_SOIL);
                        this.resources.addResource(waterSoilPurifier.getProduction(), ResourcesType.CLEAN_SOIL);
                    }
                    break;
            }

            PurificationWaterAndSoilTask purificationWaterAndSoilTask = new PurificationWaterAndSoilTask(Instant.now(),
                    Instant.now().plus(neededTime), TimedOperationType.PURIFICATION_WATER_AND_SOIL_TASK, this.neededTime,this.buildingId);
            village.getTimedOperation().put(purificationWaterAndSoilTask.getId(), purificationWaterAndSoilTask);
        }
    }
}
