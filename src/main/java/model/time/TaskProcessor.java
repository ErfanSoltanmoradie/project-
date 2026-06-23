package model.time;

import model.building.Building;
import model.village.Village;
import service.buildings.BuildingFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskProcessor {

    private Village village;

    public TaskProcessor(Village village) {
        this.village = village;
    }

    public void process(){

        List<TimedOperation> snapshot = new ArrayList<>(village.getTimedOperation().values());
        List<TimedOperation> toAdd = new ArrayList<>();
        List<UUID> toRemove = new ArrayList<>();

        for ( TimedOperation timedOperation : snapshot){
            if(timedOperation.isFinished()){
                timedOperation.execute(this.village, toAdd);
                toRemove.add(timedOperation.getId());
            }
        }

        for (TimedOperation timedOperation : toAdd){
            this.village.getTimedOperation().put(timedOperation.getId(), timedOperation);
        }

        for (UUID uuid : toRemove){
            this.village.getTimedOperation().remove(uuid);
        }
    }
}
