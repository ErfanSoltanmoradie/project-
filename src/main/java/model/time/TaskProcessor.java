package model.time;

import model.building.Building;
import model.village.Village;
import service.buildings.BuildingFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskProcessor {

    private Village village;

    public TaskProcessor(Village village) {
        this.village = village;
    }

    public void process(){
        // snapshot is a trick to copy the operations task because operations task will be
                //changed during foreach loop!!!
        List<TimedOperation> snapshot = new ArrayList<>(village.getTimedOperation().values());
        List<UUID> finishedTasks = new ArrayList<>();

        for ( TimedOperation timedOperation : snapshot){
            if(timedOperation.isFinished()){
                timedOperation.execute(this.village);
                finishedTasks.add(timedOperation.getId());
            }
        }

        for (UUID id : finishedTasks){
            village.getTimedOperation().remove(id); // We remove the finished task
        }
    }
}
