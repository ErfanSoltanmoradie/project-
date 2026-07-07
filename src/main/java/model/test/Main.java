package model.test;

import model.building.BuildingType;
import model.building.Laboratory;
import model.building.Plant;
import model.building.PlantType;
import model.player.Player;
import model.time.TaskProcessor;
import model.village.Village;
import service.buildings.BuildingsManagement;
import service.buildings.purification.PlantFactory;

public class Main {


    public static void main(String[] args) throws InterruptedException {
        Village village = new Village(null, 1000);
        Player player = new Player("EH", village);

        Laboratory laboratory = new Laboratory(BuildingType.LABORATORY, null);
        village.getBuildings().put(laboratory.getId(), laboratory);

        BuildingsManagement buildingsManagement = new BuildingsManagement(village);

        buildingsManagement.buildPlant(PlantType.NRC, null);

        buildingsManagement.upgrade(laboratory);



        /*System.out.println("CLOUD RADIATION: " + village.getCloud().getRadiation());
        Plant plant = new Plant(PlantType.NRC, null, laboratory.getLevel(), 1, 1);
        Plant plant1 = new Plant(PlantType.NRC, null, laboratory.getLevel(), 1, 1);
        Plant plant2 = new Plant(PlantType.NRC, null, laboratory.getLevel(), 1, 1);
        Plant plant4 = new Plant(PlantType.NRC, null, laboratory.getLevel(), 1, 1);
        Plant plant3 = new Plant(PlantType.NRC, null, laboratory.getLevel(), 1, 1);
        Plant plant5 = new Plant(PlantType.NRC, null, laboratory.getLevel(), 1, 1);
        Plant plant6 = new Plant(PlantType.NRC, null, laboratory.getLevel(), 1, 1);


        village.getPlants().put(plant1.getId(), plant1);
        village.getPlants().put(plant2.getId(), plant2);
        village.getPlants().put(plant3.getId(), plant3);
        village.getPlants().put(plant4.getId(), plant4);
        village.getPlants().put(plant5.getId(), plant5);
        village.getPlants().put(plant6.getId(), plant6);
        village.getPlants().put(plant.getId(), plant);*/

        //System.out.println("NRC NeutralizationPower: " + plant.getNeutralizationPower());
        /*int totall = PlantType.getTotalNeutralizationPower(village.getPlants());
        System.out.println("totall NeutralizationPower " + totall);*/

        TaskProcessor taskProcessor = new TaskProcessor(village);
        while (true){
            taskProcessor.process();
            Thread.sleep(2500);
            System.out.println("Tasks: " + village.getTimedOperation().size());
            System.out.println("Cloud Radiation: " + village.getCloud().getRadiation());
        }

    }
}
