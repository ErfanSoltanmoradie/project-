package service.map;

import model.building.Building;
import model.building.BuildingType;
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
        System.out.println("Select a place for your building " + buildingType);
        this.buildModeActive = true;
        this.selectedBuildingType = buildingType;
        if (villageController != null) villageController.hideInfoPanel();
    }

    public void handleMapClick(double pixelX, double pixelY) {
        villageController.hideAddBuildingPanel();

        Coordinate coordinate = gameCanvasView.getCoordinateFromPixels(pixelX, pixelY);
        int row = coordinate.getX();
        int col = coordinate.getY();

        if (buildModeActive && selectedBuildingType != null) {

            if(!this.gameMap.isAreaFree(row, col, selectedBuildingType.getWidth(), selectedBuildingType.getHeight())){
                this.selectedBuildingType = null;
                this.buildModeActive = false;
                return;
            }

            this.buildingsManagement.build(this.selectedBuildingType, coordinate);
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

        if (clickedBuilding != null) {
            this.selectedBuilding = clickedBuilding;
            System.out.println("Selected: " + selectedBuilding.getType() + " level: " + selectedBuilding.getLevel()
             + " Status: " + selectedBuilding.getBuildingStatus());

            if (villageController != null) {
                villageController.showBuildingInfo(selectedBuilding);
            }
        } else {
            this.selectedBuilding = null;
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
}
