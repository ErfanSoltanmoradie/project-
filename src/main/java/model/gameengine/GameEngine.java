package model.gameengine;

import model.building.*;
import model.player.Player;
import model.resources.Resources;
import model.resources.ResourcesType;
import model.time.*;
import model.trade.TradeOffer;
import model.village.Village;
import model.world.Coordinate;
import service.alliance.AllianceRequest;
import service.alliance.AllianceService;
import service.buildings.BuildingsManagement;
import service.trade.TradeService;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameEngine {

    public static void main(String[] args) throws Exception {

        /*Coordinate coordinate = new Coordinate(10, 10);
        Village village = new Village(coordinate, 5000);
        Player player = new Player("Erfan", "1234", village);
        TaskProcessor taskProcessor = new TaskProcessor(village);

        StorageBuilding soilStorage =  new StorageBuilding(BuildingType.SOIL_STORAGE, coordinate, 2000);
        StorageBuilding waterStorage = new StorageBuilding(BuildingType.WATER_STORAGE, coordinate, 2000);
        StorageBuilding woodStorage = new StorageBuilding(BuildingType.WOOD_STORAGE,coordinate, 2000);
        StorageBuilding  ironStorage =  new StorageBuilding(BuildingType.IRON_STORAGE, coordinate, 2000);
        StorageBuilding  stoneStorage = new StorageBuilding(BuildingType.STONE_STORAGE, coordinate, 2000);
        StorageBuilding gunpowderStorage = new StorageBuilding(BuildingType.GUNPOWDER_STORAGE, coordinate, 2000);

        soilStorage.setBuildingStatus(BuildingStatus.ACTIVE);
        waterStorage.setBuildingStatus(BuildingStatus.ACTIVE);
        woodStorage.setBuildingStatus(BuildingStatus.ACTIVE);
        ironStorage.setBuildingStatus(BuildingStatus.ACTIVE);
        stoneStorage.setBuildingStatus(BuildingStatus.ACTIVE);
        gunpowderStorage.setBuildingStatus(BuildingStatus.ACTIVE);

        village.getBuildings().put(soilStorage.getId(), soilStorage);
        village.getBuildings().put(waterStorage.getId(), waterStorage);
        village.getBuildings().put(woodStorage.getId(), woodStorage);
        village.getBuildings().put(ironStorage.getId(), ironStorage);
        village.getBuildings().put(stoneStorage.getId(), stoneStorage);
        village.getBuildings().put(gunpowderStorage.getId(), gunpowderStorage);

        BuildingsManagement buildingsManagement = new BuildingsManagement(player);

        System.out.println("Resources before buying buildings:");
        System.out.println("Wood: " + village.getResources().getAmount(ResourcesType.WOOD));
        System.out.println("IRON: " + village.getResources().getAmount(ResourcesType.IRON));
        System.out.println("Clean water: " + village.getResources().getAmount(ResourcesType.CLEAN_WATER));
        System.out.println("CLEAN SOIL: " + village.getResources().getAmount(ResourcesType.CLEAN_SOIL));
        System.out.println("GUNPOWDER: " + village.getResources().getAmount(ResourcesType.GUN_POWDER));
        System.out.println("DIRTY SOIL: " + village.getResources().getAmount(ResourcesType.DIRTY_SOIL));
        System.out.println("DIRTY WATER: " + village.getResources().getAmount(ResourcesType.DIRTY_WATER));
        System.out.println("STONE: " + village.getResources().getAmount(ResourcesType.STONE));


        buildingsManagement.build(BuildingType.STONE_MINE, coordinate);
        buildingsManagement.build(BuildingType.IRON_MINE, coordinate);
        buildingsManagement.build(BuildingType.WATER_PURIFIER, coordinate);
        buildingsManagement.build(BuildingType.SOIL_PURIFIER, coordinate);
        buildingsManagement.build(BuildingType.GUNPOWDER_MINE, coordinate);
        buildingsManagement.build(BuildingType.WOOD_MINE, coordinate);


        System.out.println("Resources after buying buildings:");
        System.out.println("Wood: " + village.getResources().getAmount(ResourcesType.WOOD));
        System.out.println("IRON: " + village.getResources().getAmount(ResourcesType.IRON));
        System.out.println("Clean water: " + village.getResources().getAmount(ResourcesType.CLEAN_WATER));
        System.out.println("CLEAN SOIL: " + village.getResources().getAmount(ResourcesType.CLEAN_SOIL));
        System.out.println("GUNPOWDER: " + village.getResources().getAmount(ResourcesType.GUN_POWDER));
        System.out.println("DIRTY SOIL: " + village.getResources().getAmount(ResourcesType.DIRTY_SOIL));
        System.out.println("DIRTY WATER: " + village.getResources().getAmount(ResourcesType.DIRTY_WATER));
        System.out.println("STONE: " + village.getResources().getAmount(ResourcesType.STONE));


        for (TimedOperation task : village.getTimedOperation().values()) {
            task.setFinishTime(Instant.now().minusSeconds(1));
        }

        taskProcessor.process();

        System.out.println("\nBuildings:");

        for (Building building : village.getBuildings().values()) {
            System.out.println(
                    building.getType()
                            + " | level=" + building.getLevel()
                            + " | status=" + building.getBuildingStatus()
            );
        }

        System.out.println(
                "Timed Operations Count: "
                        + village.getTimedOperation().size()
        );



        System.out.println("\n*********** PRODUCTION TEST ************");

        for (int i = 0; i < 20; i++) {

            for (TimedOperation task : village.getTimedOperation().values()) {
                task.setFinishTime(Instant.now().minusSeconds(1));
            }

            taskProcessor.process();
        }

        System.out.println("\n************ AFTER PRODUCTION ***********");

        System.out.println("WOOD: " + village.getResources().getAmount(ResourcesType.WOOD));
        System.out.println("IRON: " + village.getResources().getAmount(ResourcesType.IRON));
        System.out.println("STONE: " + village.getResources().getAmount(ResourcesType.STONE));
        System.out.println("DIRTY WATER: " + village.getResources().getAmount(ResourcesType.DIRTY_WATER));
        System.out.println("DIRTY SOIL: " + village.getResources().getAmount(ResourcesType.DIRTY_SOIL));
        System.out.println("CLEAN WATER: " + village.getResources().getAmount(ResourcesType.CLEAN_WATER));
        System.out.println("CLEAN SOIL: " + village.getResources().getAmount(ResourcesType.CLEAN_SOIL));
        System.out.println("GUNPOWDER: " + village.getResources().getAmount(ResourcesType.GUN_POWDER));


        System.out.println("\n************* TASK CHECK***************");
        System.out.println("Remaining Tasks: " + village.getTimedOperation().size());*/

        Player player1 = new Player("Player 1 (Sender)", new Village(new Coordinate(0, 0), 1000));
        Player player2 = new Player("Player 2 (Receiver)", new Village(new Coordinate(10, 10), 1000));

        player1.getVillage().getCloud().setNeutralized(200);
        player2.getVillage().getCloud().setNeutralized(200);

        MajorBuilding mb1 = new MajorBuilding(BuildingType.MAJOR_BUILDING, new Coordinate(1, 1));
        mb1.setBuildingStatus(BuildingStatus.ACTIVE);
        mb1.setLevel(2);
        player1.getVillage().getBuildings().put(mb1.getId(), mb1);

        MajorBuilding mb2 = new MajorBuilding(BuildingType.MAJOR_BUILDING, new Coordinate(1, 1));
        mb2.setBuildingStatus(BuildingStatus.ACTIVE);
        mb2.setLevel(2);
        player2.getVillage().getBuildings().put(mb2.getId(), mb2);

        ResearchCenter rc1 = new ResearchCenter(BuildingType.RESEARCH_CENTER, new Coordinate(2, 2));
        rc1.setLevel(2);
        rc1.setScienceLevel(1);
        player1.getVillage().getBuildings().put(rc1.getId(), rc1);

        ResearchCenter rc2 = new ResearchCenter(BuildingType.RESEARCH_CENTER, new Coordinate(2, 2));
        rc2.setLevel(2);
        rc2.setScienceLevel(1);
        player2.getVillage().getBuildings().put(rc2.getId(), rc2);

        StorageBuilding ironStorage1 = new StorageBuilding(BuildingType.IRON_STORAGE, new Coordinate(3,3), 5000);
        player1.getVillage().getBuildings().put(ironStorage1.getId(), ironStorage1);
        StorageBuilding woodStorage2 = new StorageBuilding(BuildingType.WOOD_STORAGE, new Coordinate(3,3), 5000);
        player2.getVillage().getBuildings().put(woodStorage2.getId(), woodStorage2);
        StorageBuilding ironStorage2 = new StorageBuilding(BuildingType.IRON_STORAGE, new Coordinate(3,3), 5000);
        player2.getVillage().getBuildings().put(ironStorage2.getId(), ironStorage2);

        Plant plant1 = new Plant(PlantType.NRC, new Coordinate(4, 4),new Laboratory(BuildingType.LABORATORY,new Coordinate(3,6)).getLevel());
        player1.getVillage().getPlant().put(plant1.getId(), plant1);

        Plant plant2 = new Plant(PlantType.NRC, new Coordinate(4, 4),new Laboratory(BuildingType.LABORATORY,new Coordinate(3,6)).getLevel());
        player2.getVillage().getPlant().put(plant2.getId(), plant2);

        AllianceService allianceService = new AllianceService();

        allianceService.sendRequest(player1, player2);

        List<AllianceRequest> p1Requests = player1.getPendingRequests();
        if (p1Requests != null && !p1Requests.isEmpty()) {
            AllianceRequest firstRequest = p1Requests.get(0);
            allianceService.acceptRequest(firstRequest);

            System.out.println("the power of plants before alliance " + plant1.getNeutralizationPower());
            System.out.println("the power of plants before alliance " + plant2.getNeutralizationPower());

            AllianceTask allianceTask = new AllianceTask(java.time.Instant.now(), java.time.Instant.now(), firstRequest);
            List<TimedOperation> operationsList = new ArrayList<>();
            allianceTask.execute(player1.getVillage(), operationsList);


            System.out.println("the power of plants after alliance " + plant1.getNeutralizationPower());
            System.out.println("the power of plants after alliance " + plant2.getNeutralizationPower());


            System.out.println("successful " + firstRequest.getAllianceStatus());
            System.out.println("request status: " + firstRequest.getAllianceStatus());
            System.out.println("the first player is a part of alliance " + (player1.getAlliance() != null));
            System.out.println("scienceLevel of first Player: " + rc1.getScienceLevel());
            System.out.println("scienceLevel of second Player: " + rc2.getScienceLevel());
            System.out.println("the number of alliance for first player: " + player1.getAllianceCounts());

        } else {
            System.out.println("error");
        }

        Customhouse ch1 = new Customhouse(BuildingType.CUSTOMHOUSE, new Coordinate(5, 5));
        ch1.setCommission(0.1);
        player1.getVillage().getBuildings().put(ch1.getId(), ch1);

        Customhouse ch2 = new Customhouse(BuildingType.CUSTOMHOUSE, new Coordinate(5, 5));
        ch2.setCommission(0.1);
        player2.getVillage().getBuildings().put(ch2.getId(), ch2);

        Map<ResourcesType, Integer> offeredResources = new HashMap<>();
        offeredResources.put(ResourcesType.WOOD, 300);
        Map<ResourcesType, Integer> requestedResources = new HashMap<>();
        requestedResources.put(ResourcesType.IRON, 200);

        TradeService tradeService = new TradeService(player1.getPlayerId(), player1.getVillage().getResourcesManagement());
        tradeService.sendRequest(player1, player2, offeredResources, requestedResources);

         if (!player2.getVillage().getTradeOffers().isEmpty()) {
             TradeOffer offer = player2.getVillage().getTradeOffers().values().iterator().next();
             tradeService.acceptOffer(offer);
             System.out.println(player1.getVillage().getResources().getAmount(ResourcesType.WOOD));
             System.out.println(player2.getVillage().getResources().getAmount(ResourcesType.IRON));
             System.out.println(player1.getVillage().getPlant().values());
         } else {
             System.out.println("unsuccessful");
         }
    }

}