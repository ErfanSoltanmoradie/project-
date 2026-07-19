package service.map;

import model.building.ArmyProducer;
import model.building.Barrack;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import model.building.Building;
import model.building.BuildingType;
import model.building.Plant;
import model.building.PlantType;
import model.finalPart.GlobalTower;
import model.village.Village;
import model.world.Coordinate;
import service.buildings.BuildingLimitExceededException;
import service.buildings.BuildingsManagement;

import java.util.Optional;

public class GameMapController {

    private final Village village;
    private final GameCanvasView gameCanvasView;
    private final GameMap gameMap;

    private boolean buildModeActive = false;
    private boolean plantModeActive = false;
    private boolean globalTowerModeActive = false;
    private BuildingType selectedBuildingType = null;
    private PlantType selectedPlantType = null;
    private final BuildingsManagement buildingsManagement;

    private Building selectedBuilding = null;
    private Plant selectedPlant;
    private VillageController villageController;

    //private ArmyProducerController armyProducerController;

    public GameMapController(Village village, GameCanvasView gameCanvasView) {
        this.village = village;
        this.gameMap = this.village.getGameMap();
        this.gameCanvasView = gameCanvasView;
        this.buildingsManagement = new BuildingsManagement(this.village);
    }

    public void setArmyProducerController(ArmyProducerController armyProducerController) {
        this.villageController = villageController;
    }

    public void setVillageController(VillageController villageController) {
        this.villageController = villageController;
    }

    public BuildingsManagement getBuildingsManagement() {
        return buildingsManagement;
    }

    public void enterBuildMode(BuildingType buildingType) {
        System.out.println("Select a place for your building " + buildingType);
        this.buildModeActive = true;
        this.selectedBuildingType = buildingType;
        if (villageController != null) villageController.hideInfoPanel();
    }

    public void enterPlantBuildMode(model.building.PlantType plantType) {
        System.out.println("Select a place for your plant: " + plantType);
        this.plantModeActive = true;
        this.selectedPlantType = plantType;
        this.buildModeActive = false;
        if (villageController != null) villageController.hideInfoPanel();
    }

    public void enterGlobalTowerBuildMode() {
        System.out.println("Select a place for your Global Tower");
        this.globalTowerModeActive = true;
        this.buildModeActive = false;
        this.plantModeActive = false;
        if (villageController != null) villageController.hideInfoPanel();
    }

    public void handleMapClick(double pixelX, double pixelY) {
        if(villageController != null) villageController.hideAddBuildingPanel();

        Coordinate coordinate = gameCanvasView.getCoordinateFromPixels(pixelX, pixelY);
        int row = coordinate.getX();
        int col = coordinate.getY();

        if (globalTowerModeActive) {
            if (!this.gameMap.isAreaFree(row, col, GlobalTower.WIDTH, GlobalTower.HEIGHT)) {
                this.globalTowerModeActive = false;
                return;
            }

            try {
                this.buildingsManagement.buildGlobalTower(coordinate);
            } catch (IllegalStateException e) {
                System.err.println(e.getMessage());
            }
            this.globalTowerModeActive = false;
            return;
        }

        if (plantModeActive && selectedPlantType != null) {
            if (!this.gameMap.isAreaFree(row, col, selectedPlantType.getWidth(), selectedPlantType.getHeight())) {
                this.selectedPlantType = null;
                this.plantModeActive = false;
                return;
            }

            try {
                this.buildingsManagement.buildPlant(this.selectedPlantType, coordinate);
            } catch (BuildingLimitExceededException e) {
                System.err.println(e.getMessage());
            }
            this.selectedPlantType = null;
            this.plantModeActive = false;
            return;
        }

        if (buildModeActive && selectedBuildingType != null) {

            if(!this.gameMap.isAreaFree(row, col, selectedBuildingType.getWidth(), selectedBuildingType.getHeight())){
                this.selectedBuildingType = null;
                this.buildModeActive = false;
                return;
            }

            try {
                this.buildingsManagement.build(this.selectedBuildingType, coordinate);
            }catch(BuildingLimitExceededException e ){
                System.err.println(e.getMessage());
            }

            this.selectedBuildingType = null;
            this.buildModeActive = false;

            return;
        }

        Building clickedBuilding = null;
        for (Building building : village.getBuildings().values()) {
            Coordinate position = building.getPosition();

            if (row >= position.getX() && row < position.getX() + building.getHeight() &&
                    col >= position.getY() && col < position.getY() + building.getWidth()) {

                clickedBuilding = building;
                break;
            }
        }
        Plant clickedPlant = null;
        if (clickedBuilding == null) {
            this.selectedBuilding = null;
            for (Plant plant : village.getPlants().values()) {
                Coordinate position = plant.getPosition();
                if (position != null && row >= position.getX() && row < position.getX() + plant.getHeight() &&
                        col >= position.getY() && col < position.getY() + plant.getWidth()) {
                    clickedPlant = plant;
                    break;
                }
            }
        }

        if (clickedBuilding != null) {
            this.selectedBuilding = clickedBuilding;
            this.selectedPlant = null;
            System.out.println("Selected: " + selectedBuilding.getType() + " level: " + selectedBuilding.getLevel()
                    + " Status: " + selectedBuilding.getBuildingStatus());

            System.out.println(clickedBuilding.getClass().getName());

            if(selectedBuilding instanceof ArmyProducer && villageController != null) {
                villageController.showDecisionPanel(selectedBuilding);
                return;
            }

            if (selectedBuilding instanceof Barrack && villageController != null) {
                villageController.showDecisionBarrackPanel(selectedBuilding);
                return;
            }

            System.out.println("Selected: " + selectedBuilding.getType());

            if (villageController != null) {
                villageController.showBuildingInfo(selectedBuilding);
            }
        } else if (clickedPlant != null) {
            this.selectedPlant = clickedPlant;
            this.selectedBuilding = null;
            System.out.println("Selected Plant: " + selectedPlant.getType());
            if (villageController != null) {
                villageController.showPlantInfo(clickedPlant);
            }
        } else {
            this.selectedBuilding = null;
            this.selectedPlant = null;
            if (villageController != null) {

                villageController.hideInfoPanel();
                villageController.hideShopPanel();
                villageController.hideAddBuildingPanel();
                villageController.hideTradePanel();
                villageController.hideMakeATradePanel();
                villageController.hideReceivedTradeRequestsPanel();
                villageController.hideSentTradeRequestsPanel();
                villageController.hideAlliancePanel();
                villageController.hideAllianceRequestsPanel();
                villageController.hideAttackPanel();
                villageController.hideAttackHistoryPanel();
                villageController.hideDecidePanel();
                villageController.hideDecisionPanel();
                villageController.hideDecisionBarrackPanel();
                villageController.hideGlobalTowerPanel();
                villageController.hideAddPlantPanel();
            }
        }
    }

    public void handleUpgradeClicked() {
        if (selectedBuilding != null) {
            this.buildingsManagement.upgrade(selectedBuilding);
            if (villageController != null) {
                villageController.showBuildingInfo(selectedBuilding);
            }
        }
    }
    public Plant getSelectedPlant() {
        return selectedPlant;
    }
}
