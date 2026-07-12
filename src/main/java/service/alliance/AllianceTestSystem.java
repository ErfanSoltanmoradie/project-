package service.alliance;

import model.building.Building;
import model.building.BuildingType;
import model.building.MajorBuilding;
import model.building.ResearchCenter;
import model.player.Player;
import model.resources.ResourcesType;
import model.time.TaskProcessor;
import model.village.Village;
import model.world.Coordinate;
import service.alliance.AllianceRequest;
import service.alliance.AllianceService;

import java.util.UUID;

public class AllianceTestSystem {

    /*public static void main(String[] args) throws InterruptedException {
        System.out.println("***START***");

        Village villageA = new Village(new Coordinate(0, 0), 100);
        Village villageB = new Village(new Coordinate(5, 5), 100);

        Player playerA = new Player("Player A ", villageA);
        Player playerB = new Player("Player B ", villageB);

        villageA.getCloud().setNeutralized(300);
        villageB.getCloud().setNeutralized(300);

        villageA.getResourcesManagement().addResource(1000, ResourcesType.WOOD);
        villageA.getResourcesManagement().addResource(1000, ResourcesType.IRON);
        villageB.getResourcesManagement().addResource(1000, ResourcesType.WOOD);
        villageB.getResourcesManagement().addResource(1000, ResourcesType.IRON);

        Building majorA = new MajorBuilding(BuildingType.MAJOR_BUILDING, new Coordinate(1,1));
        majorA.setLevel(2);
        villageA.getBuildings().put(majorA.getId(), majorA);

        Building majorB = new MajorBuilding(BuildingType.MAJOR_BUILDING, new Coordinate(1,1));
        majorB.setLevel(2);
        villageB.getBuildings().put(majorB.getId(), majorB);

        Building researchA = new ResearchCenter(BuildingType.RESEARCH_CENTER, new Coordinate(3,3));
        researchA.setLevel(2);
        villageA.getBuildings().put(researchA.getId(), researchA);

        Building researchB = new ResearchCenter(BuildingType.RESEARCH_CENTER, new Coordinate(3,3));
        researchB.setLevel(2);
        villageB.getBuildings().put(researchB.getId(), researchB);

        AllianceService allianceService = new AllianceService();
        TaskProcessor taskProcessorA = new TaskProcessor(villageA);
        TaskProcessor taskProcessorB = new TaskProcessor(villageB);

        System.out.println("SEND REQUEST");
        allianceService.sendRequest(playerA, playerB);

        System.out.println("PLAYER ONE REQUESTS " + playerA.getPendingRequests().size());
        System.out.println("PLAYER TWO REQUESTS " + playerB.getPendingRequests().size());

        if (!playerB.getPendingRequests().isEmpty()) {
            AllianceRequest request = playerB.getPendingRequests().get(0);

            System.out.println("PLAYER ACCEPT THE REQUEST");
            allianceService.acceptRequest(request);

            System.out.println("REQUEST DELETED FROM PENDINGS " + playerB.getPendingRequests().isEmpty());
            System.out.println("VILLAGE 1 TASKS " + villageA.getTimedOperation().size());

            System.out.println("WATING 5 SECONDS FOR APPLY ALLIANCE . . .");
            Thread.sleep(5500);

            taskProcessorA.process();
            taskProcessorB.process();

            System.out.println("FINAL RESULT");
            System.out.println("IS PLAYER ONE INT THE ALLIANCE?  " + (playerA.getAlliance()));
            System.out.println("IS PLAYER ONE INT THE ALLIANCE?   " + (playerB.getAlliance()));
            System.out.println("ALL ALLIANCES FOR PLAYER1 " + playerA.getAllianceCounts());
        } else {
            System.out.println("ERORRRRRRRRRRRRRRRRRRRRRRR");
        }
    }*/
}