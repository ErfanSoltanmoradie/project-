package service.map;


import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import model.building.*;
import model.player.Player;
import model.resources.Resources;
import model.resources.ResourcesType;
import model.time.TaskProcessor;
import model.village.Village;
import service.resource.ResourcesManagement;


//import backend.Player;
public class VillageController {

    @FXML private StackPane rootStackPane;

    @FXML private AnchorPane infoPanel;

    @FXML private AnchorPane shopPanel;

    @FXML private Button upgradeButton;

    @FXML private Button buyBuilding;

    @FXML private Label woodLabel;

    @FXML private Label ironLabel;

    @FXML private Label stoneLabel;

    @FXML private Label gunPowderLabel;

    @FXML private Label cleanWaterLabel;

    @FXML private Label cleanSoilLabel;

    @FXML private Button shopButton;

    @FXML private AnchorPane addBuildingPanel;

    @FXML private ScrollPane addBuildingScrollPane;

    @FXML private HBox addBuildingHBox;

    @FXML private VBox addBuildingVBox;

    @FXML private ImageView woodMineImage;

    @FXML private Label woodMineLabel;

    @FXML private Label laboratoryLabel;

    @FXML private Label customhouseLabel;

    @FXML private Label nrcLabel;

    @FXML private Label snrcLabel;

    @FXML private Label psnrcLabel;

    @FXML private Button woodMineBuildButton;

    @FXML private Button ironMineBuildButton;

    @FXML private Button waterMineBuildButton;

    @FXML private Button soilMineBuildButton;

    @FXML private Button gunPowderMineBuildButton;

    @FXML private Button StoneMineBuildButton;

    @FXML private Button laboratoryBuildButton;

    @FXML private Button customhouseBuildButton;

    @FXML private Button nrcBuildButton;

    @FXML private Button snrcBuildButton;

    @FXML private Button psnrcBuildButton;

    //@FXML private ImageView borderImageView;

    private Player player;
    private TaskProcessor taskProcessor;
    private GameCanvasView gameCanvasView;
    private GameMapController controller;
    private AnimationTimer gameLoop;

    private double lastMouseX;
    private double lastMouseY;


    public void setPlayer(Player player) {
        this.player = player;
        this.updateResourcesUI();
        this.taskProcessor = new TaskProcessor(player.getVillage());

        initMap();
        startGameLoop();
    }

    private void initMap() {
        Village village = player.getVillage();

        this.gameCanvasView = new GameCanvasView(village);

        //this.borderImageView.fitWidthProperty().bind(rootStackPane.widthProperty());
       // this.borderImageView.fitHeightProperty().bind(rootStackPane.heightProperty());
        this.gameCanvasView.widthProperty().bind(rootStackPane.widthProperty());
        this.gameCanvasView.heightProperty().bind(rootStackPane.heightProperty());

        this.controller = new GameMapController(village, gameCanvasView);
        this.controller.setVillageController(this);
//////////////////////////////////////////////////////////////
        this.gameCanvasView.setOnMousePressed(event -> {
            this.lastMouseX = event.getX();
            this.lastMouseY = event.getY();
        });

        this.gameCanvasView.setOnMouseDragged(event -> {
            double deltaX = event.getX() - this.lastMouseX;
            double deltaY = event.getY() - this.lastMouseY;

            this.gameCanvasView.moveCamera(deltaX, deltaY);

            this.lastMouseX = event.getX();
            this.lastMouseY = event.getY();
        });
////////////////////////////////////////////////////////////////////////////
        this.gameCanvasView.setOnMouseClicked(event -> {
            if (controller != null) {
                this.controller.handleMapClick(event.getX(), event.getY());
            }
        });

        if (!rootStackPane.getChildren().contains(gameCanvasView)) {
            rootStackPane.getChildren().add(0, gameCanvasView);
        }

        if (rootStackPane.getChildren().size() > 1) {
            javafx.scene.Node uiLayer = rootStackPane.getChildren().get(1);

            //uiLayer.setPickOnBounds(false);
        }


        this.gameCanvasView.setOnScroll(event -> {
            double zoomFactor = (event.getDeltaY() > 0) ? 1.1 : 0.9;

            gameCanvasView.zoom(zoomFactor, event.getX(), event.getY());
        });
    }

    /*@FXML
    private void onAddBuildingClicked(ActionEvent event) {
        if (controller != null) {
            controller.enterBuildMode(BuildingType.WOOD_MINE);
        }
    }*/

    @FXML
    private void onUpgradeClicked(ActionEvent event) {
        if (this.controller != null) {
            if (this.controller.getSelectedPlant() != null) {
                this.player.getVillage().getPlants().remove(this.controller.getSelectedPlant().getId());
                this.player.getVillage().getGameMap().getTile(
                        this.controller.getSelectedPlant().getPosition().getX(),
                        this.controller.getSelectedPlant().getPosition().getY()
                ).setPlant(null);
                this.hideInfoPanel();
            } else {
                this.controller.handleUpgradeClicked(); // روال عادی آپگرید ساختمان
                this.hideInfoPanel();
            }
        }
    }

    @FXML
    private void onShopClicked(ActionEvent event){
        this.buyBuilding.setVisible(true);
        this.shopPanel.setVisible(true);
        this.shopPanel.setManaged(true);
    }

    @FXML
    private void onBuyBuildingClicked(ActionEvent actionEvent){
        this.hideShopPanel();
        this.addBuildingPanel.setVisible(true);
        this.addBuildingPanel.setManaged(true);
        //this.addBuildingScrollPane.setVisible(true);

    }

    @FXML
    private void onWoodMineBuildClicked(ActionEvent actionEvent){
        if (controller != null) {
            this.hideAddBuildingPanel();
            if(!checkResourcesAndAlert(BuildingType.WOOD_MINE)) return ;
            else
                controller.enterBuildMode(BuildingType.WOOD_MINE);
        }
    }

    @FXML
    private void onIronMineBuildClicked(ActionEvent actionEvent){
        if (controller != null) {
            this.hideAddBuildingPanel();
            if(!checkResourcesAndAlert(BuildingType.IRON_MINE)) return ;
            else
                controller.enterBuildMode(BuildingType.IRON_MINE);
        }
    }

    @FXML
    private void onStoneMineBuildClicked(ActionEvent actionEvent){
        if (controller != null) {
            this.hideAddBuildingPanel();
            if(!checkResourcesAndAlert(BuildingType.STONE_MINE)) return ;
            else
                controller.enterBuildMode(BuildingType.STONE_MINE);
        }
    }

    @FXML
    private void onWaterMineBuildClicked(ActionEvent actionEvent){
        if (controller != null) {
            this.hideAddBuildingPanel();
            if(!checkResourcesAndAlert(BuildingType.WATER_STORAGE)) return ;
            else
                controller.enterBuildMode(BuildingType.DIRTY_WATER_MINE);
        }
    }

    @FXML
    private void onSoilMineBuildClicked(ActionEvent actionEvent){
        if (controller != null) {
            this.hideAddBuildingPanel();
            if(!checkResourcesAndAlert(BuildingType.SOIL_STORAGE)) return ;
            else
                controller.enterBuildMode(BuildingType.DIRTY_SOIL_MINE);
        }
    }

    @FXML
    private void onGunPowderMineBuildClicked(ActionEvent actionEvent){
        if (controller != null) {
            this.hideAddBuildingPanel();
            if(!checkResourcesAndAlert(BuildingType.GUNPOWDER_MINE)) return ;
            else
                controller.enterBuildMode(BuildingType.GUNPOWDER_MINE);
        }
    }
    @FXML
    private void onLaboratoryBuildClicked(ActionEvent actionEvent){
        if (controller != null) {
            this.hideAddBuildingPanel();
            if(!checkResourcesAndAlert(BuildingType.LABORATORY)) return ;
            else
                controller.enterBuildMode(BuildingType.LABORATORY);
        }
    }

    @FXML
    private void onCustomhouseBuildClicked(ActionEvent actionEvent){
        if (controller != null) {
            this.hideAddBuildingPanel();
            if(!checkResourcesAndAlert(BuildingType.CUSTOMHOUSE)) return ;
            else
                controller.enterBuildMode(BuildingType.CUSTOMHOUSE);
        }
    }

    @FXML
    private void onNRCBuildClicked(ActionEvent actionEvent){
        if(controller != null) {
            this.hideAddBuildingPanel();
            if(!checkResourcesAndAlert(PlantType.NRC)) return ;
            else
                controller.enterPlantBuildMode(PlantType.NRC);
        }
    }

    @FXML
    private void onSNRCBuildClicked(ActionEvent actionEvent){
        if(controller != null) {
            this.hideAddBuildingPanel();
            if(!checkResourcesAndAlert(PlantType.SNRC)) return ;
            else
                controller.enterPlantBuildMode(PlantType.SNRC);
        }
    }

    @FXML
    private void onPSNRCBuildClicked(ActionEvent actionEvent){
        if(controller != null) {
            this.hideAddBuildingPanel();
            if(!checkResourcesAndAlert(PlantType.PSNRC)) return ;
            else
                controller.enterPlantBuildMode(PlantType.PSNRC);
        }
    }

    public void showBuildingInfo(Building building){
        if(building == null) return;
        this.upgradeButton.setText("Upgrade " + building.getType().toString() + " (Lvl " + building.getLevel() + ")");
        if(building.getBuildingStatus() == BuildingStatus.UPGRADING || building.getBuildingStatus() == BuildingStatus.BUILDING){
            this.upgradeButton.setDisable(true);
        }else {
            this.upgradeButton.setDisable(false);
        }
        System.out.println("UI Panel updated for: " + building.getType() + " Level: " + building.getLevel());
        this.infoPanel.setVisible(true);
        this.infoPanel.setManaged(true);
    }

    public void showPlantInfo(Plant plant){
        if(plant==null){return;}
        this.upgradeButton.setText("Remove " + plant.getType().toString() + " (Power: " + plant.getNeutralizationPower() + ")");
        this.upgradeButton.setDisable(false);
        this.upgradeButton.setVisible(true);

        this.infoPanel.setVisible(true);
        this.infoPanel.setManaged(true);
    }

    public void hideInfoPanel(){
        this.infoPanel.setVisible(false);
        this.infoPanel.setManaged(false);
    }

    public void hideShopPanel(){
        this.shopPanel.setVisible(false);
        this.shopPanel.setManaged(false);
    }

    public void hideAddBuildingPanel(){
        this.addBuildingPanel.setVisible(false);
        this.addBuildingPanel.setManaged(false);
    }

    private void updateResourcesUI(){
        Resources resources = this.player.getVillage().getResources();
        ResourcesManagement resourcesManagement = this.player.getVillage().getResourcesManagement();

        int maxWoodCapacity = resourcesManagement.getMaxCapacity(ResourcesType.WOOD);
        int maxIronCapacity = resourcesManagement.getMaxCapacity(ResourcesType.IRON);
        int maxStoneCapacity = resourcesManagement.getMaxCapacity(ResourcesType.STONE);
        int maxCleanWaterCapacity = resourcesManagement.getMaxCapacity(ResourcesType.CLEAN_WATER);
        int maxCleanSoilCapacity = resourcesManagement.getMaxCapacity(ResourcesType.CLEAN_SOIL);
        int maxGunPowderCapacity = resourcesManagement.getMaxCapacity(ResourcesType.GUN_POWDER);


        this.woodLabel.setText("WOOD: " + "" + resources.getAmount(ResourcesType.WOOD) + " / " + maxWoodCapacity);
        this.ironLabel.setText("IRON: " + "" + resources.getAmount(ResourcesType.IRON) + " / " + maxIronCapacity);
        this.stoneLabel.setText("STONE: " + "" + resources.getAmount(ResourcesType.STONE) + " / " + maxStoneCapacity);
        this.cleanWaterLabel.setText("WATER: " + "" + resources.getAmount(ResourcesType.CLEAN_WATER) + " / " + maxCleanWaterCapacity);
        this.cleanSoilLabel.setText("SOIL: " + "" + resources.getAmount(ResourcesType.CLEAN_SOIL) + " / " + maxCleanSoilCapacity);
        this.gunPowderLabel.setText("GUN POWDER: " + "" + resources.getAmount(ResourcesType.GUN_POWDER) + " / " + maxGunPowderCapacity);
    }

    private void startGameLoop() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {

                taskProcessor.process();

                if(player != null)
                    updateResourcesUI();
                updateShopButtonsAvailability();

                if (gameCanvasView != null) {
                    gameCanvasView.draw();
                }
            }
        };
        gameLoop.start();
    }

    public void stopGameLoop(AnimationTimer gameLoop){
        gameLoop.stop();
    }

    public AnimationTimer getGameLoop() {
        return gameLoop;
    }

    private void updateShopButtonsAvailability() {
        int labLevel = 0;
        for (model.building.Building b : player.getVillage().getBuildings().values()) {
            if (b.getType() == model.building.BuildingType.LABORATORY && b.getBuildingStatus() == model.building.BuildingStatus.ACTIVE) {
                labLevel = b.getLevel();
                break;
            }
        }
        // ۲. بررسی پیش‌نیاز گیاه NRC (نیاز به آزمایشگاه لول ۱)
        if (labLevel < 1) {
            nrcBuildButton.setDisable(true);
            nrcLabel.setText("NRC (Lab Lvl 1 Required) -> Current: " + labLevel); // در FXML شما اسمش snrcLabel است
            nrcLabel.setTextFill(Color.RED);
        } else {
            nrcBuildButton.setDisable(false);
            nrcLabel.setText("NRC");
            nrcLabel.setTextFill(Color.GREEN);
        }

        // ۳. بررسی پیش‌نیاز گیاه SNRC (نیاز به آزمایشگاه لول ۲)
        if (labLevel < 2) {
            snrcBuildButton.setDisable(true);
            snrcLabel.setText("SNRC (Lab Lvl 2 Required) -> Current: "+ labLevel);
            snrcLabel.setTextFill(Color.RED);
        } else {
            snrcBuildButton.setDisable(false);
            snrcLabel.setText("SNRC");
            snrcLabel.setTextFill(Color.GREEN);
        }

        // ۴. بررسی پیش‌نیاز گیاه PSNRC (نیاز به آزمایشگاه لول ۳)
        if (labLevel < 3) {
            psnrcBuildButton.setDisable(true);
            psnrcLabel.setText("PSNRC (Lab Lvl 3 Required) -> Current: "+ labLevel);
            psnrcLabel.setTextFill(Color.RED);
        } else {
            psnrcBuildButton.setDisable(false);
            psnrcLabel.setText("PSNRC");
            psnrcLabel.setTextFill(Color.GREEN);
        }
    }
    private boolean checkResourcesAndAlert(PlantType plant) {
        Cost cost = plant.getBasePlantCost();
        if (cost == null) return true;

        // بررسی اینکه آیا منابع بازیکن در انبار (resourcesManagement) کافی است یا خیر
        boolean hasResources = player.getVillage().getResourcesManagement().checkResourcesCost(cost);
        if (!hasResources) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("error in building");
            alert.setHeaderText("you don't have enough resources!");
            alert.setContentText("for building " + plant + " you need " + cost.getNeededTime().toSeconds() + " seconds and extra resources");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    private boolean checkResourcesAndAlert(BuildingType type) {
        model.building.Cost cost = model.building.Cost.buildCost(type);
        if (cost == null) return true;

        // بررسی اینکه آیا منابع بازیکن در انبار (resourcesManagement) کافی است یا خیر
        boolean hasResources = player.getVillage().getResourcesManagement().checkResourcesCost(cost);
        if (!hasResources) {
            javafx.scene.control.Alert alert = new Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("error in building");
            alert.setHeaderText("you don't have enough resources!");
            alert.setContentText("for building " + type + " you need " + cost.getNeededTime().toSeconds() + " seconds and extra resources");
            alert.showAndWait();
            return false;
        }
        return true;
    }
}

