package model.gameengine;


import model.building.*;
import model.resources.ResourcesType;
import model.time.TaskProcessor;
import model.village.Village;
import model.world.Coordinate;
import service.buildings.BuildingsManagement;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class UltimateChaosTest {

    public static void main(String[] args) throws Exception {

        Village village = new Village(null, 2000);
        TaskProcessor processor = new TaskProcessor(village);
        BuildingsManagement bm = new BuildingsManagement(village);

        AtomicBoolean running = new AtomicBoolean(true);


        Thread processorThread = new Thread(() -> {
            while (running.get()) {
                processor.process();
                sleep(15);
            }
        });

        Thread builder1 = new Thread(() -> stressBuild(bm, running, "B1"));
        Thread builder2 = new Thread(() -> stressBuild(bm, running, "B2"));
        Thread builder3 = new Thread(() -> stressBuild(bm, running, "B3"));

        Thread upgrader1 = new Thread(() -> stressUpgrade(village, bm, running));
        Thread upgrader2 = new Thread(() -> stressUpgrade(village, bm, running));

        Thread chaosThread = new Thread(() -> {
            Random r = new Random();
            while (running.get()) {
                int x = r.nextInt(3);

                switch (x) {
                    case 0 -> village.getResources().withdraw(ResourcesType.WOOD, 50);
                    case 1 -> village.getResources().withdraw(ResourcesType.IRON, 50);
                    case 2 -> village.getResources().addResource(ResourcesType.WOOD, 100, 999999);
                }

                sleep(10);
            }
        });

        Thread watchdog = new Thread(() -> {
            long lastTaskSize = -1;
            int stuckCounter = 0;

            while (running.get()) {

                int taskSize = village.getTimedOperation().size();

                System.out.println("[WATCHDOG] Tasks=" + taskSize +
                        " Buildings=" + village.getBuildings().size());

                if (taskSize == lastTaskSize) {
                    stuckCounter++;
                } else {
                    stuckCounter = 0;
                }

                lastTaskSize = taskSize;

                if (stuckCounter > 50) {
                    System.out.println("💀 DEADLOCK / LIVELock suspected!");
                    running.set(false);
                }

                sleep(100);
            }
        });

        processorThread.start();
        builder1.start();
        builder2.start();
        builder3.start();
        upgrader1.start();
        upgrader2.start();
        chaosThread.start();
        watchdog.start();

        Thread.sleep(30000);
        running.set(false);

        processorThread.join();
        builder1.join();
        builder2.join();
        builder3.join();
        upgrader1.join();
        upgrader2.join();
        chaosThread.join();
        watchdog.join();
        
        System.out.println("\n================ FINAL REPORT ================");

        System.out.println("Buildings: " + village.getBuildings().size());
        System.out.println("Tasks: " + village.getTimedOperation().size());

        long upgrading = village.getBuildings().values().stream()
                .filter(b -> b.getBuildingStatus() == BuildingStatus.UPGRADING)
                .count();

        System.out.println("Still upgrading: " + upgrading);

        System.out.println("=============================================");
    }

    private static void stressBuild(BuildingsManagement bm, AtomicBoolean running, String name) {
        int i = 0;
        Random r = new Random();

        while (running.get()) {
            bm.build(
                    BuildingType.WOOD_MINE,
                    new Coordinate(i % 100, (i / 100) % 100)
            );

            i++;
            sleep(5 + r.nextInt(5));
        }
    }

    private static void stressUpgrade(Village village, BuildingsManagement bm, AtomicBoolean running) {
        Random r = new Random();

        while (running.get()) {

            Building target = null;

            village.getLock().readLock().lock();
            try {
                for (Building b : village.getBuildings().values()) {
                    if (b.getBuildingStatus() == BuildingStatus.ACTIVE) {
                        target = b;
                        break;
                    }
                }
            } finally {
                village.getLock().readLock().unlock();
            }

            if (target != null) {
                bm.upgrade(target);
            }

            sleep(5 + r.nextInt(5));
        }
    }

    private static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {}
    }
}