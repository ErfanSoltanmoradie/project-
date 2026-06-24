package model.gameengine;

import model.time.TaskProcessor;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameEngine {

    //private final TaskProcessor taskProcessor;
    private ScheduledExecutorService scheduler;

    private final List<TaskProcessor> processors;

    public GameEngine(List<TaskProcessor> processors) {
        this.processors = processors;
    }

    public void start() {
         scheduler =
                Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(() -> {

            for (TaskProcessor processor : processors) {
                processor.process();
            }

        }, 0, 1, TimeUnit.SECONDS);
    }

    public void stop() {
        if (scheduler != null) {
            scheduler.shutdown();
        }
    }
}