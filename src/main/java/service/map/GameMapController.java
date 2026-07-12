package service.map;

import javafx.scene.control.Alert;
import model.building.Building;
import model.building.BuildingType;
import model.building.Plant;
import model.building.PlantType;
import model.village.Village;
import model.world.Coordinate;
import service.buildings.BuildingsManagement;

public class GameMapController {

    private final Village village;
    private final GameCanvasView gameCanvasView;
    private final GameMap gameMap;

    private boolean buildModeActive = false;
    private BuildingType selectedBuildingType = null;
    private final BuildingsManagement buildingsManagement;

    private Building selectedBuilding = null;
    private VillageController villageController;

    private boolean plantModeActive = false;

    private PlantType selectedPlantType = null;
    private Plant selectedPlant;

    public GameMapController(Village village, GameCanvasView gameCanvasView) {
        this.village = village;
        this.gameMap = this.village.getGameMap();
        this.gameCanvasView = gameCanvasView;
        this.buildingsManagement = new BuildingsManagement(this.village);
    }

    public void setVillageController(VillageController villageController) {
        this.villageController = villageController;
    }

    public void enterBuildMode(BuildingType buildingType) {
        System.out.println("Select a place for your building" + buildingType);
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

    public void handleMapClick(double pixelX, double pixelY) {
        villageController.hideAddBuildingPanel();

        Coordinate coordinate = gameCanvasView.getCoordinateFromPixels(pixelX, pixelY);
        int row = coordinate.getX();
        int col = coordinate.getY();

        if (plantModeActive && selectedPlantType != null) {
            if (!this.gameMap.isAreaFree(row, col, 1, 1)) {
                this.selectedPlantType = null;
                this.plantModeActive = false;
                return;
            }

            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Final approval");
            confirmAlert.setHeaderText("Are you sure about this?");
            confirmAlert.setContentText("building " + selectedPlantType + " will begin");

            java.util.Optional<javafx.scene.control.ButtonType> result = confirmAlert.showAndWait();

            if (result.isPresent() && result.get() == javafx.scene.control.ButtonType.OK) {
                this.buildingsManagement.buildPlant(this.selectedPlantType, coordinate);
            }

            this.selectedPlantType = null;
            this.plantModeActive = false;
            return;
        }

        if (buildModeActive && selectedBuildingType != null) {
            if (!this.gameMap.isAreaFree(row, col, selectedBuildingType.getWidth(), selectedBuildingType.getHeight())) {
                this.selectedBuildingType = null;
                this.buildModeActive = false;
                return;
            }

            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Final approval");
            confirmAlert.setHeaderText("Are you sure about this?");
            confirmAlert.setContentText("building " + selectedBuildingType + " will begin");

            java.util.Optional<javafx.scene.control.ButtonType> result = confirmAlert.showAndWait();

            if (result.isPresent() && result.get() == javafx.scene.control.ButtonType.OK) {
                this.buildingsManagement.build(selectedBuildingType, coordinate);
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
            System.out.println("Selected Building: " + selectedBuilding.getType());
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
