package service.map;



import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.building.*;
import model.player.Player;
import model.resources.Resources;
import model.resources.ResourcesType;
import model.time.TaskProcessor;
import model.village.Village;
import service.resource.ResourcesManagement;

import java.io.IOException;


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

    @FXML private Button woodMineBuildButton;

    @FXML private Button ironMineBuildButton;

    @FXML private Button waterMineBuildButton;

    @FXML private Button soilMineBuildButton;

    @FXML private Button gunPowderMineBuildButton;

    @FXML private Button StoneMineBuildButton;

    @FXML private Button tradeButton;

    @FXML private Button majorBuildingButton;

    @FXML private Button researchCenter;

    @FXML private Button armyProducerBuildButton;

    @FXML private Button barrackBuildButton;


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
            this.controller.handleUpgradeClicked();
            this.hideInfoPanel();
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
            controller.enterBuildMode(BuildingType.WOOD_MINE);
        }
    }

    @FXML
    private void onIronMineBuildClicked(ActionEvent actionEvent){
        if (controller != null) {
            this.hideAddBuildingPanel();
            controller.enterBuildMode(BuildingType.IRON_MINE);
        }
    }

    @FXML
    private void onStoneMineBuildClicked(ActionEvent actionEvent){
        if (controller != null) {
            this.hideAddBuildingPanel();
            controller.enterBuildMode(BuildingType.STONE_MINE);
        }
    }

    @FXML
    private void onWaterMineBuildClicked(ActionEvent actionEvent){
        if (controller != null) {
            this.hideAddBuildingPanel();
            controller.enterBuildMode(BuildingType.DIRTY_WATER_MINE);
        }
    }

    @FXML
    private void onSoilMineBuildClicked(ActionEvent actionEvent){
        if (controller != null) {
            this.hideAddBuildingPanel();
            controller.enterBuildMode(BuildingType.DIRTY_SOIL_MINE);
        }
    }

    @FXML
    private void onGunPowderMineBuildClicked(ActionEvent actionEvent){
        if (controller != null) {
            this.hideAddBuildingPanel();
            controller.enterBuildMode(BuildingType.GUNPOWDER_MINE);
        }
    }

    @FXML
    private void onMajorBuildingBuildClicked(ActionEvent actionEvent){
        if(controller != null){
            this.hideAddBuildingPanel();
            controller.enterBuildMode(BuildingType.MAJOR_BUILDING);
        }
    }

    @FXML
    private void onResearchCenterBuildClicked(){
        if(controller != null){
            this.hideAddBuildingPanel();
            controller.enterBuildMode(BuildingType.RESEARCH_CENTER);
        }
    }

    @FXML
    private void onTradeButtonClicked(){

    }

    @FXML
    private void onArmyProducerBuildClicked(ActionEvent actionEvent){
        if(controller != null){
            this.hideAddBuildingPanel();
            controller.enterBuildMode(BuildingType.ARMY_PRODUCER);
        }

    }

    @FXML
    private void oneBarrackBuildClicked(ActionEvent actionEvent){
        if(controller != null){
            this.hideAddBuildingPanel();
            controller.enterBuildMode(BuildingType.BARRACKS);
        }
    }

    private void setTradeButtonEnable(){
        if(this.checkTradeResearchBuildingCondition() && this.checkTradeMajorBuildingCondition())
            this.tradeButton.setDisable(false);
    }

    private boolean checkTradeMajorBuildingCondition(){
        this.player.getLock().readLock().lock();
        try {
            this.player.getVillage().getLock().readLock().lock();
            try {
                for (Building building : this.player.getVillage().getBuildings().values()){
                    if(building instanceof MajorBuilding ){
                        if(building.getLevel() >= 3)
                            return true;
                    }
                }
            }finally {
                this.player.getVillage().getLock().readLock().unlock();
            }
        }finally {
            this.player.getLock().readLock().unlock();
        }
        return false;
    }

    private boolean checkTradeResearchBuildingCondition(){
        this.player.getLock().readLock().lock();
        try {
            this.player.getVillage().getLock().readLock().lock();
            try {
                for (Building building : this.player.getVillage().getBuildings().values()){
                    if(building instanceof ResearchCenter ){
                        if(building.getLevel() >= 2)
                            return true;
                    }
                }
            }finally {
                this.player.getVillage().getLock().readLock().unlock();
            }
        }finally {
            this.player.getLock().readLock().unlock();
        }
        return false;
    }

    public void showBuildingInfo(Building building){
        if(building.getBuildingStatus() == BuildingStatus.UPGRADING || building.getBuildingStatus() == BuildingStatus.BUILDING){
            this.upgradeButton.setDisable(true);
        }else {
            this.upgradeButton.setDisable(false);
        }
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

                setTradeButtonEnable();

                taskProcessor.process();

                if(player != null)
                    updateResourcesUI();

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

    public void openArmyProducer(ArmyProducer armyProducer) {
        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/project/armyProducer.fxml")
            );

            Parent root = loader.load();

            ArmyProducerController controller = loader.getController();
            controller.setPlayer(player,armyProducer);

            Stage stage = new Stage();
            stage.setTitle("Army Producer");
            stage.setScene(new Scene(root));
            stage.show();
            stage.setOnHidden(e -> controller.stopRefresh());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void openBarrack(Barrack barrack) {
        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/project/barrack.fxml")
            );

            Parent root = loader.load();

            BarrackController controller = loader.getController();
            controller.setPlayer(player, barrack);

            Stage stage = new Stage();
            stage.setTitle("Barrack");
            stage.setScene(new Scene(root));
            stage.show();
            stage.setOnHidden(e -> controller.stopRefresh());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

