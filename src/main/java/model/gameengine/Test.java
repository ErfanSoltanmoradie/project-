package model.gameengine;

import model.building.Building;
import model.building.BuildingType;
import model.player.Player;
import model.resources.ResourcesType;
import model.time.TaskProcessor;
import model.village.Village;
import service.buildings.BuildingsManagement;
import service.filehandeling.GameState;
import service.filehandeling.LoadService;
import service.filehandeling.SaveService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) throws Exception {

        File file = new File("players.dat");

        GameState gameState =
                LoadService.load(file);

        if (gameState == null) {
            System.out.println("players.dat not found");
            return;
        }

        List<TaskProcessor> processors = new ArrayList<>();

        for (Player player : gameState.getPlayers().values()) {
            player.getVillage().runTimeServices();
            processors.add(new TaskProcessor(player.getVillage()));
        }

        GameEngine engine = new GameEngine(processors);
        engine.start();

        if (gameState.getPlayers().isEmpty()) {
            System.out.println("No players found");
            return;
        }

        Player player =
                gameState.getPlayers().values().iterator().next();

        Village village = player.getVillage();

        BuildingsManagement buildingsManagement =
                new BuildingsManagement(village);

        System.out.println("Buildings before build: "
                + village.getBuildings().size());

        /*buildingsManagement.build(
                BuildingType.WOOD_MINE,
                village.getCoordinate()
        );

        System.out.println("Task count after build request: "
                + village.getTimedOperation().size());*/

        Thread.sleep(5000);

        /*System.out.println("Buildings after build: "
                + village.getBuildings().size());

        for (Building building : village.getBuildings().values()) {
            System.out.println(
                    building.getType()
                            + " level="
                            + building.getLevel()
            );
        }*/


        Building woodMine = null;

        for (Building building : village.getBuildings().values()) {
            if (building.getType() == BuildingType.WOOD_MINE) {
                woodMine = building;
                break;
            }
        }

        System.out.println(
                "Before upgrade level = "
                        + woodMine.getLevel()
        );

        System.out.println("Tasks before upgrade = "
                + village.getTimedOperation().size());

        buildingsManagement.upgrade(woodMine);

        /*int beforeWood =
                village.getResources()
                        .getAmount(ResourcesType.WOOD);

        System.out.println(
                "Wood before = "
                        + beforeWood
        );

        Thread.sleep(5000);

        int afterWood =
                village.getResources()
                        .getAmount(ResourcesType.WOOD);

        System.out.println(
                "Wood after = "
                        + afterWood
        );*/
        System.out.println("Tasks after upgrade = "
                + village.getTimedOperation().size());

        Thread.sleep(5000);

        System.out.println(
                "After upgrade level = "
                        + woodMine.getLevel()
        );
        System.out.println("Tasks after execution = "
                + village.getTimedOperation().size());

        System.out.println("Building level = "
                + woodMine.getLevel());

        System.out.println("Building status = "
                + woodMine.getBuildingStatus());

       // SaveService.save(gameState, file);
        engine.stop();
    }
}
