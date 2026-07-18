package service.map;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.battle.BattleArmy;
import model.battle.BattleHistory;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import model.building.*;
import model.finalPart.GlobalTower;
import model.player.Player;
import model.repository.PlayerRepository;
import model.resources.Resources;
import model.resources.ResourcesType;
import model.time.TaskProcessor;
import model.village.Village;
import model.world.Coordinate;
import service.alliance.AllianceRequest;
import service.alliance.AllianceService;
import service.battle.BattleManagement;
import service.battle.BattleTravelTime;
import service.buildings.BuildingsManagement;
import service.filehandeling.GameState;
import service.resource.ResourcesManagement;
import service.trade.TradeOffer;
import service.trade.TradeService;
import service.trade.TradeStatus;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

public class VillageController {

    private PlayerRepository playerRepository;
    private int lastSeenAnnouncementCount = 0;
    private GameState gameState;

    public PlayerRepository getPlayerRepository() {
        return playerRepository;
    }

    public void setPlayerRepository(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @FXML private StackPane rootStackPane;

    @FXML private AnchorPane infoPanel;

    @FXML private AnchorPane shopPanel;

    @FXML private Button upgradeButton;

    @FXML private Button buyBuilding;

    @FXML private Label infoPanelTitleLabel;
    @FXML private Label buildingLevelLabel;
    @FXML private Label plantsCountLabel;
    @FXML private Label neutralizationPowerLabel;
    @FXML private Label towerAnnouncementLabel;

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

    @FXML private ImageView ironMineImage;

    @FXML private ImageView stoneMineImage;

    @FXML private ImageView LaboratoryMineImage;

    @FXML private ImageView nrcImage;

    @FXML private ImageView snrcImage;

    @FXML private ImageView psnrcImage;

    @FXML private Label woodMineLabel;

    @FXML private Label ironMineLabel;

    @FXML private Label stoneMineLabel;

    @FXML private Label cleanWaterMineLabel;

    @FXML private Label cleanSoilMineLabel;

    @FXML private Label gunPowderMineLabel;

    @FXML private Label laboratoryLabel;

    @FXML private Label customhouseLabel;

    @FXML private Label nrcLabel;

    @FXML private Label snrcLabel;

    @FXML private Label psnrcLabel;

    @FXML private Label dirtyWaterMineLabel;

    @FXML private Label dirtySoilMineLabel;

    @FXML private Label waterPurifierLabel;

    @FXML private Label soilPurifierLabel;

    @FXML private Label woodStorageLabel;

    @FXML private Label ironStorageLabel;

    @FXML private Label stoneStorageLabel;

    @FXML private Label gunPowderStorageLabel;

    @FXML private Label waterStorageLabel;

    @FXML private Label soilStorageLabel;

    @FXML private Label ballistaDefensiveLabel;

    @FXML private Label catapultDefensiveLabel;

    @FXML private Label sentinelDefensiveLabel;

    @FXML private Label majorBuildingLabel;

    @FXML private Label researchCenterLabel;

    @FXML private Button woodMineBuildButton;

    @FXML private Button ironMineBuildButton;

    @FXML private Button waterMineBuildButton;

    @FXML private Button soilMineBuildButton;

    @FXML private Button gunPowderMineBuildButton;

    @FXML private Button StoneMineBuildButton;

    @FXML private Button waterStorageBuildButton;

    @FXML private Button soilStorageBuildButton;

    @FXML private Button waterPurifierBuildButton;

    @FXML private Button soilPurifierBuildButton;

    @FXML private Button woodStorageBuildButton;

    @FXML private Button stoneMineBuildButton;

    @FXML private Button ironStorageBuildButton;

    @FXML private Button stoneStorageBuildButton;

    @FXML private Button gunPowderStorageBuildButton;

    @FXML private Button ballistaDefensiveBuildButton;

    @FXML private Button catapultDefensiveBuildButton;

    @FXML private Button sentinelDefensiveBuildButton;

    @FXML private Button customhouseBuildButton;

    @FXML private Button laboratoryBuildButton;

    @FXML private Button researchCenterBuildButton;

    @FXML private Button nrcBuildButton;

    @FXML private Button snrcBuildButton;

    @FXML private Button psnrcBuildButton;

    @FXML private Button tradeButton;

    @FXML private Button majorBuildingButton;

    @FXML private Button researchCenter;


    @FXML private AnchorPane tradePanel;


    @FXML private VBox tradePlayersContainer;

    @FXML private Button makeADealButton;

    @FXML AnchorPane makeATradePanel;

    @FXML TextField receiveIronTextField;

    @FXML TextField sendWoodTextField;

    @FXML Button MakeADealButton;

    @FXML Button receivedtraderequests;

    @FXML AnchorPane receivedTradeRequestsPanel;

    @FXML VBox tradeRequestsContainer;

    @FXML AnchorPane sentTradeRequestsPanel;

    @FXML VBox tradeSentRequestsContainer;

    @FXML Button allianceButton;

    @FXML AnchorPane alliancePanel;

    @FXML VBox allowedToAlliancePlayers;

    @FXML AnchorPane allianceRequestsPanel;

    @FXML Button managePendingAllianceRequests;

    @FXML VBox allianceRequestContainer;

    @FXML Button battleButton;

    @FXML VBox playersContainerForAttack;

    @FXML AnchorPane battlePanel;

    @FXML Button battleHistoryButton;

    @FXML VBox battleHistoryContainer;

    @FXML AnchorPane battleHistoryPannel;

    @FXML ProgressBar woodProgressBar;

    @FXML ProgressBar ironProgressBar;

    @FXML ProgressBar stoneProgressBar;

    @FXML ProgressBar cleanWaterProgressBar;

    @FXML ProgressBar cleanSoilProgressBar;

    @FXML ProgressBar gunPowderProgressBar;

    @FXML AnchorPane decidePanel;

    @FXML Button makeTradeButton;

    @FXML TextField sendIronTextField;

    @FXML TextField sendGunPowderTextField;

    @FXML TextField receiveGunPowderTextField;

    @FXML TextField sendSoilTextField;

    @FXML TextField receiveSoilTextField;

    @FXML TextField sendStoneTextField;

    @FXML TextField receiveStoneTextField;

    @FXML TextField sendWaterTextField;

    @FXML TextField receiveWaterTextField;

    @FXML private Button armyProducerBuildButton;

    @FXML private Button barrackBuildButton;

    @FXML HBox decisionPanel;

    @FXML HBox barracksDecisionPanel;

    @FXML AnchorPane addPlantPanel;

    @FXML Label healthAmountLabel;

    @FXML Label usernameLabel2;
    @FXML ProgressBar healthProgressBar;

    @FXML Button towerButton;
    @FXML AnchorPane globalTowerPanel;

    @FXML Label towerStatusLabel;
    @FXML Label towerHpLabel;
    @FXML ProgressBar towerHpBar;
    @FXML Label towerProtectionLabel;
    @FXML Label towerRequirementWarningLabel;
    @FXML Button buildTowerButton;
    @FXML Button leaveTowerPanelButton;
    @FXML Button confirmTowerBuildButton;
    @FXML private WinnerController winnerViewController;
    @FXML private EliminatedController eliminatedViewController;

    @FXML Label radiationLabel;

    @FXML ProgressBar radiationProgressBar;

    @FXML HBox decisionCustomhousePanel;


    @FXML Label receivedCoinFromSellingStone;
    @FXML Label receivedCoinFromSellingGunPowder;
    @FXML Label receivedCoinFromSellingIron;
    @FXML Label receivedCoinFromSellingWood;
    @FXML Label receivedCoinFromSellingWater;
    @FXML Label receivedCoinFromSellingSoil;

    @FXML TextField sellWoodTextField;
    @FXML TextField sellIronTextField;
    @FXML TextField sellStoneTextField;
    @FXML TextField sellGunPowderTextField;
    @FXML TextField sellWaterTextField;
    @FXML TextField sellSoilTextField;

    @FXML AnchorPane sellResourcesPanel;

    @FXML ProgressBar coinProgressBar;

    @FXML Label coinLabel;

    @FXML TextField receiveWoodTextField;


    private Player player;
    private TaskProcessor taskProcessor;
    private GameCanvasView gameCanvasView;
    private GameMapController controller;
    private AnimationTimer gameLoop;

    private double lastMouseX;
    private double lastMouseY;

    private Player receiverTradeRequest;
    private Map<UUID, Player> traders = new HashMap<>();
    private List<Player> allowedToAlliance = new ArrayList<>();
    private List<Player> enemies = new ArrayList<>();
    private boolean winnerWindowShown = false;
    private boolean eliminatedWindowShown = false;

    private ArmyProducer SelectedArmyProducer;

    private Customhouse selectedCustomhouse;

    public void setPlayer(Player player) {
        if (player == null) {
            return;
        }
        if (player.getVillage() == null) {
            return;
        }
        this.player = player;
        this.updateResourcesUI();
        this.taskProcessor = new TaskProcessor(player.getVillage());

        this.usernameLabel2.setText(null);
        this.usernameLabel2.setText(this.player.getUsername());

        initMap();
        startGameLoop();
    }

    private void healthProgressBar(){
        this.healthAmountLabel.setText(null);
        this.healthAmountLabel.setText(String.valueOf(this.checkHealth()));

        this.healthProgressBar.setMaxWidth(Double.MAX_VALUE);
        this.healthProgressBar.setProgress( ((double) this.checkHealth() / 2000));
        this.healthProgressBar.getStyleClass().add("neon-progress-bar");
        this.healthProgressBar.setStyle("-fx-accent: #ff0202;");
    }

    private void radiationProgressBar(){
        this.radiationLabel.setText(null);

        this.radiationProgressBar.setMaxWidth(Double.MAX_VALUE);
        this.radiationProgressBar.setProgress(checkRadiation());

        this.radiationProgressBar.getStyleClass().add("neon-progress-bar");
        this.radiationProgressBar.setStyle("-fx-accent: #38ff00;");
    }



    private double checkRadiation(){
        this.player.getVillage().getLock().readLock().lock();
        try {
            this.radiationLabel.setText(String.valueOf(player.getVillage().getCloud().getRadiation()));

            return  ((double) player.getVillage().getCloud().getRadiation() / 2000);
        }finally {
            player.getVillage().getLock().readLock().unlock();
        }
    }

    private int checkHealth(){
        this.player.getVillage().getLock().readLock().lock();
        try {
            return this.player.getVillage().getHealth();
        }finally {
            player.getVillage().getLock().readLock().unlock();
        }
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


    public void validPlayersToTrade(){
        this.traders.clear();
        try {
            for (Player player1 : this.playerRepository.getAllPlayers().values()){

                if(player1.getPlayerId().equals(this.player.getPlayerId()))
                    continue;

                if(BuildingsManagement.checkResearchCenterBuildingForTrade(player1) &&
                        BuildingsManagement.checkCustomHouseBuildingForTrade(player1)&&
                        BuildingsManagement.checkCustomHouseBuildingForTrade(player1)){

                    this.traders.put(player1.getPlayerId(), player1);
                }
            }
        }catch (NullPointerException e){
            System.out.println("" + e);
        }
    }

    private void showTradersOnPanel(Map<UUID, Player> traders){
        tradePlayersContainer.getChildren().clear();
        for (Player player : traders.values()){
            HBox playerRow = createPlayerRowElement(player);
            tradePlayersContainer.getChildren().add(playerRow);
        }
    }

    private HBox createPlayerRowElement(Player targetPlayer) {
        HBox row = new HBox();
        row.setSpacing(15);

        row.setStyle("-fx-padding: 10; " +
                "-fx-background-color: #2b2b2b; " +
                "-fx-background-radius: 8; " +
                "-fx-border-color: #444444; " +
                "-fx-border-width: 1; " +
                "-fx-alignment: CENTER_LEFT;");


        VBox textContainer = new VBox();
        textContainer.setSpacing(4);

        String pName = targetPlayer.getUsername();
        Label nameLabel = new Label("Player: " + pName);
        nameLabel.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 14px; -fx-font-weight: bold;");

        Coordinate coord = targetPlayer.getVillage().getCoordinate();
        Label coordLabel = new Label("Positiont: [" + coord.getX() + " , " + coord.getY() + "]");
        coordLabel.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 11px;");

        textContainer.getChildren().addAll(nameLabel, coordLabel);


        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);


        Button selectBtn = new Button("CHOOSE");
        selectBtn.setStyle("-fx-background-color: #ff9800; " +
                "-fx-text-fill: #121212; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 5; " +
                "-fx-cursor: hand;");

        selectBtn.setOnAction(e -> {
            this.tradePanel.setVisible(false);
            this.openTradeOfferForm();
            this.setReceiverTradeRequest(targetPlayer);
        });

        row.getChildren().addAll(textContainer, spacer, selectBtn);


        row.setOnMouseEntered(event -> row.setStyle(row.getStyle() + "-fx-background-color: #383838; -fx-border-color: #ff9800;"));
        row.setOnMouseExited(event -> row.setStyle(row.getStyle() + "-fx-background-color: #2b2b2b; -fx-border-color: #444444;"));

        return row;
    }

    private void showSentTradeRequestsOnPanel(){
        tradeSentRequestsContainer.getChildren().clear();

        this.player.getVillage().getLock().readLock().lock();
        try {
            for(TradeOffer tradeOffer : player.getVillage().getSentTradeRequests()){
                if(tradeOffer.getTradeStatus() == TradeStatus.PENDING){
                    HBox playerRow = this.createSentTradeRequestsRowElement(tradeOffer) ;

                    this.tradeSentRequestsContainer.getChildren().add(playerRow);
                }
            }
        }finally {
            this.player.getVillage().getLock().readLock().unlock();
        }
    }

    private HBox createSentTradeRequestsRowElement(TradeOffer tradeOffer){
        HBox row = new HBox();
        row.setSpacing(15);

        row.setStyle("-fx-padding: 10; " +
                "-fx-background-color: #2b2b2b; " +
                "-fx-background-radius: 8; " +
                "-fx-border-color: #444444; " +
                "-fx-border-width: 1; " +
                "-fx-alignment: CENTER_LEFT;");


        VBox playerVBox = new VBox();
        playerVBox.setSpacing(5);

        String receiverPlayerName = tradeOffer.getReceiverVillage().getUserName();
        Label receiverLabel = new Label("receiver player [ " + receiverPlayerName + " ]");
        receiverLabel.setStyle("-fx-text-fill: #FF9800FF; -fx-font-size: 14px; -fx-font-weight: bold;");

        playerVBox.getChildren().addAll(receiverLabel);

        VBox resourcesContainer = new VBox();
        resourcesContainer.setSpacing(4);


        String offeredStr = String.valueOf(tradeOffer.getOfferedResources().get(ResourcesType.WOOD));
        String requestedStr = String.valueOf(tradeOffer.getRequestedResources().get(ResourcesType.IRON));

        Label receiveLabel = new Label("RECEIVE: " + offeredStr);
        receiveLabel.setStyle("-fx-text-fill: #4CAF50; -fx-font-size: 11px;");

        Label payLabel = new Label("SEND: " + requestedStr);
        payLabel.setStyle("-fx-text-fill: #f44336; -fx-font-size: 11px;");

        resourcesContainer.getChildren().addAll(receiveLabel, payLabel);

        row.getChildren().addAll(playerVBox, resourcesContainer);

        row.setOnMouseEntered(event -> row.setStyle(row.getStyle() + "-fx-background-color: #383838; -fx-border-color: #ff9800;"));
        row.setOnMouseExited(event -> row.setStyle(row.getStyle() + "-fx-background-color: #2b2b2b; -fx-border-color: #444444;"));

        return row;
    }



    private void showTradeRequestsOnPanel(Player player){
        tradeRequestsContainer.getChildren().clear();
        player.getVillage().getLock().readLock().lock();
        try {
            for (TradeOffer receivedTradeRequests : player.getVillage().getReceivedTradeRequests()){
                if(receivedTradeRequests.getTradeStatus() == (TradeStatus.PENDING)){
                    HBox playerRow = createTradeRequestsRowElement(receivedTradeRequests);
                    tradeRequestsContainer.getChildren().add(playerRow);
                }
            }
        }finally {
            player.getVillage().getLock().readLock().unlock();
        }
    }

    private HBox createTradeRequestsRowElement(TradeOffer offer) {
        TradeService tradeService = new TradeService();

        HBox row = new HBox();
        row.setSpacing(15);
        row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        row.setStyle("-fx-padding: 12; " +
                "-fx-background-color: #2b2b2b; " +
                "-fx-background-radius: 8; " +
                "-fx-border-color: #444444; " +
                "-fx-border-width: 1;");

        VBox senderContainer = new VBox();
        senderContainer.setSpacing(4);
        senderContainer.setMinWidth(120);
        senderContainer.setPrefWidth(120);

        String senderName = offer.getSenderVillage().getUserName();
        Label senderLabel = new Label(senderName + " ➔ YOU");
        senderLabel.setStyle("-fx-text-fill: #ff9800; -fx-font-size: 13px; -fx-font-weight: bold;");

        Label timeLabel = new Label("Time: " + offer.getTradeTime() + "s");
        timeLabel.setStyle("-fx-text-fill: #f5e9e9; -fx-font-size: 11px;");

        senderContainer.getChildren().addAll(senderLabel, timeLabel);

        VBox offeredResourcesContainer = new VBox();
        offeredResourcesContainer.setSpacing(4);
        offeredResourcesContainer.setMinWidth(140);

        for (Map.Entry<ResourcesType, Integer> entry : offer.getOfferedResources().entrySet()) {
            ResourcesType resourcesType = entry.getKey();
            int amount = entry.getValue();
            if (amount > 0) {
                Label label = new Label(resourcesType.toString() + ": " + amount);
                label.setStyle("-fx-text-fill: #00ff0b; -fx-font-size: 11px; -fx-font-weight: bold;");
                offeredResourcesContainer.getChildren().add(label);
            }
        }

        VBox requestedResourcesContainer = new VBox();
        requestedResourcesContainer.setSpacing(4);
        requestedResourcesContainer.setMinWidth(100);

        for (Map.Entry<ResourcesType, Integer> entry : offer.getRequestedResources().entrySet()) {
            ResourcesType resourcesType = entry.getKey();
            int amount = entry.getValue();
            if (amount > 0) {
                Label label = new Label(resourcesType.toString() + ": " + amount);
                label.setStyle("-fx-text-fill: #ff1000; -fx-font-size: 11px; -fx-font-weight: bold;");
                requestedResourcesContainer.getChildren().add(label);
            }
        }

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox actionButtons = new HBox(8);
        actionButtons.setStyle("-fx-alignment: CENTER_RIGHT;");

        Button acceptBtn = new Button("ACCEPT");
        acceptBtn.setMinWidth(75);
        acceptBtn.setPrefWidth(75);
        acceptBtn.setStyle("-fx-background-color: #00ff0a; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 5; " +
                "-fx-cursor: hand;");
        acceptBtn.setOnAction(e -> {
            System.out.println("ACCEPTED");
            tradeService.acceptOffer(offer);
        });

        Button rejectBtn = new Button("REJECT");
        rejectBtn.setMinWidth(75);
        rejectBtn.setPrefWidth(75);
        rejectBtn.setStyle("-fx-background-color: #ff1200; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 5; " +
                "-fx-cursor: hand;");
        rejectBtn.setOnAction(e -> {
            tradeService.rejectOffer(offer);
            System.out.println("REJECTED");
        });

        actionButtons.getChildren().addAll(acceptBtn, rejectBtn);

        row.getChildren().addAll(senderContainer, offeredResourcesContainer, requestedResourcesContainer, spacer, actionButtons);

        row.setOnMouseEntered(event -> row.setStyle(row.getStyle() + "-fx-background-color: #383838; -fx-border-color: #ff9800;"));
        row.setOnMouseExited(event -> row.setStyle(row.getStyle() + "-fx-background-color: #2b2b2b; -fx-border-color: #444444;"));

        return row;
    }

    private void findAllowedPlayerForAlliance(){

        this.allowedToAlliance.clear();

        if(this.player.getAlliance() == null){
            for (Player player1 : this.playerRepository.getAllPlayers().values()){
                boolean flag = false;
                player1.getLock().readLock().lock();
                try {
                    player1.getVillage().getLock().readLock().lock();
                    try {

                        if(player1.getPlayerId().equals(this.player.getPlayerId())){
                            continue;
                        }


                        if(player1.getAlliance() != null)
                            continue;

                        for (AllianceRequest allianceRequest : player1.getPendingRequests()){
                            if(allianceRequest == null)
                                continue;
                            if(allianceRequest.getSender().equals(this.player) ||allianceRequest.getReceiver().equals(this.player)){
                                flag = true;
                            }

                        }
                        if (flag)
                            continue;

                        if(AllianceService.checkSenderAllianceRequestMajorBuildingLevel(player1)
                                && AllianceService.checkCloudForAllianceSender(player1)
                                && AllianceService.checkScienceLevelForAlliance(player1))

                            this.allowedToAlliance.add(player1);

                    }finally {
                        player1.getVillage().getLock().readLock().unlock();
                    }
                }finally {
                    player1.getLock().readLock().unlock();
                }
            }
        }
    }

    private void showAllowedToAllianceOnPanel(List<Player> allowedToAlliance){

        this.allowedToAlliancePlayers.getChildren().clear();
        for (Player player1 : allowedToAlliance){

            HBox playerRow = createAllianceRowElement(player1);

            this.allowedToAlliancePlayers.getChildren().add(playerRow);
        }
    }

    private HBox createAllianceRowElement(Player targetPlayer) {
        HBox row = new HBox();
        row.setSpacing(15);

        row.setStyle("-fx-padding: 10; " +
                "-fx-background-color: #2b2b2b; " +
                "-fx-background-radius: 8; " +
                "-fx-border-color: #444444; " +
                "-fx-border-width: 1; " +
                "-fx-alignment: CENTER_LEFT;");


        VBox textContainer = new VBox();
        textContainer.setSpacing(4);

        String pName = targetPlayer.getUsername();
        Label nameLabel = new Label("Player: " + pName);
        nameLabel.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 14px; -fx-font-weight: bold;");

        Coordinate coord = targetPlayer.getVillage().getCoordinate();
        Label coordLabel = new Label("Positiont: [" + coord.getX() + " , " + coord.getY() + "]");
        coordLabel.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 11px;");

        textContainer.getChildren().addAll(nameLabel, coordLabel);


        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);


        Button selectBtn = new Button("CHOOSE");
        selectBtn.setStyle("-fx-background-color: #ff9800; " +
                "-fx-text-fill: #121212; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 5; " +
                "-fx-cursor: hand;");

        selectBtn.setOnAction(e -> {
            this.hideAlliancePanel();
            AllianceService allianceService = new AllianceService();
            allianceService.sendRequest(this.player, targetPlayer);
        });

        row.getChildren().addAll(textContainer, spacer, selectBtn);


        row.setOnMouseEntered(event -> row.setStyle(row.getStyle() + "-fx-background-color: #383838; -fx-border-color: #ff9800;"));
        row.setOnMouseExited(event -> row.setStyle(row.getStyle() + "-fx-background-color: #2b2b2b; -fx-border-color: #444444;"));

        return row;
    }

    private void showAllianceRequests(){
        this.allianceRequestContainer.getChildren().clear();

        for (AllianceRequest allianceRequest : this.player.getPendingRequests()){
            if(!allianceRequest.getSender().equals(this.player) /*&& allianceRequest.getSender().getAlliance() != null*/){
                HBox allianceRow = createAllianceRequestsElement(allianceRequest);
                this.allianceRequestContainer.getChildren().add(allianceRow);
            }
        }
    }

    private HBox createAllianceRequestsElement(AllianceRequest allianceRequest){
        HBox row = new HBox();
        row.setSpacing(10);

        row.setStyle("-fx-padding: 10; " +
                "-fx-background-color: #2b2b2b; " +
                "-fx-background-radius: 8; " +
                "-fx-border-color: #444444; " +
                "-fx-border-width: 1; " +
                "-fx-alignment: CENTER_LEFT;");

        VBox textContainer = new VBox();
        textContainer.setSpacing(4);

        String senderName = allianceRequest.getSender().getUsername();
        Label usernameLabel = new Label("Player: " + senderName);
        usernameLabel.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 14px; -fx-font-weight: bold;");

        textContainer.getChildren().add(usernameLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);


        Button selectBtn = new Button("ACCEPT");
        selectBtn.setStyle("-fx-background-color: #ff9800; " +
                "-fx-text-fill: #121212; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 5; " +
                "-fx-cursor: hand;");

        selectBtn.setOnAction(e -> {
            this.hideAllianceRequestsPanel();
            AllianceService allianceService = new AllianceService();
            allianceService.acceptRequest(allianceRequest);
        });

        row.getChildren().addAll(textContainer, spacer, selectBtn);

        row.setOnMouseEntered(event -> row.setStyle(row.getStyle() + "-fx-background-color: #383838; -fx-border-color: #ff9800;"));
        row.setOnMouseExited(event -> row.setStyle(row.getStyle() + "-fx-background-color: #2b2b2b; -fx-border-color: #444444;"));
        return row;
    }

    private void setEnemies(){
        this.enemies.clear();

        this.enemies.addAll(this.playerRepository.getAllPlayers().values());
    }

    private void showEnemies(){

        this.playersContainerForAttack.getChildren().clear();

        for (Player targetPlayer : this.enemies){
            AllianceService.lockPlayers(this.player, targetPlayer);
            try {
                AllianceService.lockVillages(this.player, targetPlayer);
                try {
                    if(!targetPlayer.getPlayerId().equals(this.player.getPlayerId())){
                        HBox playerRow = this.createEnemiesElement(targetPlayer);

                        this.playersContainerForAttack.getChildren().add(playerRow);
                    }
                }finally {
                    AllianceService.unlockVillages(this.player, targetPlayer);
                }
            }finally {
                AllianceService.unlockPlayers(this.player, targetPlayer);
            }
        }
    }

    private HBox createEnemiesElement(Player targetPlayer){
        HBox row = new HBox();
        row.setSpacing(10);

        row.setStyle("-fx-padding: 10; " +
                "-fx-background-color: #2b2b2b; " +
                "-fx-background-radius: 8; " +
                "-fx-border-color: #444444; " +
                "-fx-border-width: 1; " +
                "-fx-alignment: CENTER_LEFT;");

        VBox textContainer = new VBox();
        textContainer.setSpacing(4);

        String defenderName = targetPlayer.getUsername();
        Label usernameLabel = new Label("Defender Player: " + defenderName);
        usernameLabel.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 14px; -fx-font-weight: bold;");

        long travelTime = BattleTravelTime.calculateTravelTime(this.player.getVillage(), targetPlayer.getVillage());
        Label timeLabel = new Label("TRANSFER TIME: " + travelTime + " seconds");
        timeLabel.setStyle("-fx-text-fill: #888888; -fx-font-size: 11px;");

        textContainer.getChildren().addAll(usernameLabel, timeLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button selectBtn = new Button("BATTLE");
        selectBtn.setStyle("-fx-background-color: #ff9800; " +
                "-fx-text-fill: #121212; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 5; " +
                "-fx-cursor: hand;");

        selectBtn.setOnAction(e -> {
            // do the attack
            this.hideAttackPanel();
            this.doAttack(targetPlayer);
        });

        row.getChildren().addAll(textContainer, spacer, selectBtn);

        row.setOnMouseEntered(event -> row.setStyle(row.getStyle() + "-fx-background-color: #383838; -fx-border-color: #ff9800;"));
        row.setOnMouseExited(event -> row.setStyle(row.getStyle() + "-fx-background-color: #2b2b2b; -fx-border-color: #444444;"));

        return row;
    }

    private void doAttack(Player targetPlayer){

        BattleManagement battleManagement = new BattleManagement(this.player.getVillage(), targetPlayer.getVillage());
        BattleArmy battleArmy = new BattleArmy(10, 10, 10);
        //you need to create an army first it's just a test
        battleManagement.startBattle(battleArmy);
        this.battleButton.setDisable(true);
    }

    private void showAttackHistory(){

        this.battleHistoryContainer.getChildren().clear();

        player.getLock().readLock().lock();
        try {
            player.getVillage().getLock().readLock().lock();
            try {
                for (BattleHistory battleHistory : player.getVillage().getBattleHistory()){
                    HBox row = createAttackHistoryElements(battleHistory);
                    this.battleHistoryContainer.getChildren().add(row);
                }

            }finally {
                player.getVillage().getLock().readLock().unlock();
            }
        }finally {
            player.getLock().readLock().unlock();
        }
    }

    private HBox createAttackHistoryElements(BattleHistory battleHistory){
        HBox row = new HBox();
        row.setSpacing(15);

        row.setStyle("-fx-padding: 10; " +
                "-fx-background-color: #2b2b2b; " +
                "-fx-background-radius: 8; " +
                "-fx-border-color: #444444; " +
                "-fx-border-width: 1; " +
                "-fx-alignment: CENTER_LEFT;");

        VBox textContainer = new VBox();
        textContainer.setSpacing(4);

        String defenderName = battleHistory.getDefenderUsername();
        Label defenderUsernameLabel = new Label("Defender: " + defenderName);
        defenderUsernameLabel.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 14px; -fx-font-weight: bold;");

        String attackerName = battleHistory.getAttackerUsername();
        Label attackerUsername = new Label("Attacker: " + attackerName);
        attackerUsername.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 14px; -fx-font-weight: bold;");

        textContainer.getChildren().addAll(defenderUsernameLabel, attackerUsername);

        Region spacer1 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);

        VBox statusVBox = new VBox();
        statusVBox.setSpacing(15);

        Label status = new Label("WINNER: " + battleHistory.getWinner().toString());
        status.setStyle("-fx-text-fill: #ff0000; -fx-font-size: 14px; -fx-font-weight: bold;");

        statusVBox.getChildren().add(status);

        VBox lootVBox = new VBox();
        lootVBox.setSpacing(3);

        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);


        for(Map.Entry<ResourcesType, Integer> entry : battleHistory.getAttackerLoot().entrySet()){
            ResourcesType resourcesType = entry.getKey();
            int amount = entry.getValue();
            if(amount > 0){
                Label loot = new Label("" + resourcesType.toString() + ": " + amount);
                loot.setStyle("-fx-text-fill: #00ffff; -fx-font-size: 14px; -fx-font-weight: bold;");
                lootVBox.getChildren().add(loot);
            }
        }


        row.getChildren().addAll(textContainer,spacer1 ,statusVBox, spacer2,lootVBox);

        row.setOnMouseEntered(event -> row.setStyle(row.getStyle() + "-fx-background-color: #383838; -fx-border-color: #ff9800;"));
        row.setOnMouseExited(event -> row.setStyle(row.getStyle() + "-fx-background-color: #2b2b2b; -fx-border-color: #444444;"));


        return row;
    }


    private void openTradeOfferForm(){
        this.showMakeATradePanel();
    }

    private int callCoin(int amount, ResourcesType resourcesType){
        int coin=switch (resourcesType){
            case WOOD -> amount/20;
            case IRON -> amount/8;
            case STONE -> amount/15;
            case GUN_POWDER ->  amount/10;
            case CLEAN_SOIL -> amount/10;
            case CLEAN_WATER -> amount/10;
            default  -> 0;
        };
        return coin;
    }

    private void setEmptySellRequestsField(){
        if(this.receivedCoinFromSellingWood.getText().isEmpty()) this.receivedCoinFromSellingWood.setText("0");
        if(this.receivedCoinFromSellingIron.getText().isEmpty()) this.receivedCoinFromSellingIron.setText("0");

        if(this.receivedCoinFromSellingGunPowder.getText().isEmpty()) this.receivedCoinFromSellingGunPowder.setText("0");
        if(this.receivedCoinFromSellingWater.getText().isEmpty()) this.receivedCoinFromSellingWater.setText("0");

        if(this.receivedCoinFromSellingSoil.getText().isEmpty()) this.receivedCoinFromSellingSoil.setText("0");
        if(this.receivedCoinFromSellingStone.getText().isEmpty()) this.receivedCoinFromSellingStone.setText("0");
    }

    private void clearSellResourcesTextFields(){
        this.sellWoodTextField.clear();
        this.sellWoodTextField.clear();
        this.sellIronTextField.clear();
        this.sellSoilTextField.clear();
        this.sellWaterTextField.clear();
        this.sellStoneTextField.clear();
    }

    private void makeDeals(Map<ResourcesType , Integer> offeredResources, Map<ResourcesType , Integer> requestedResources){

        this.setEmptyTradeRequestField();

        offeredResources.put(ResourcesType.WOOD, Integer.parseInt(this.receiveWoodTextField.getText()));
        requestedResources.put((ResourcesType.WOOD), Integer.parseInt(this.sendWoodTextField.getText()));

        offeredResources.put(ResourcesType.IRON, Integer.parseInt(this.receiveIronTextField.getText()));
        requestedResources.put((ResourcesType.IRON), Integer.parseInt(this.sendIronTextField.getText()));

        offeredResources.put(ResourcesType.GUN_POWDER, Integer.parseInt(this.receiveGunPowderTextField.getText()));
        requestedResources.put((ResourcesType.GUN_POWDER), Integer.parseInt(this.sendGunPowderTextField.getText()));

        offeredResources.put(ResourcesType.CLEAN_SOIL, Integer.parseInt(this.receiveSoilTextField.getText()));
        requestedResources.put((ResourcesType.CLEAN_SOIL), Integer.parseInt(this.sendSoilTextField.getText()));

        offeredResources.put(ResourcesType.STONE, Integer.parseInt(this.receiveStoneTextField.getText()));
        requestedResources.put((ResourcesType.STONE), Integer.parseInt(this.sendStoneTextField.getText()));

        offeredResources.put(ResourcesType.CLEAN_WATER, Integer.parseInt(this.receiveWaterTextField.getText()));
        requestedResources.put((ResourcesType.CLEAN_WATER), Integer.parseInt(this.sendWaterTextField.getText()));

        this.clearTradeTextFields();
    }

    private void setEmptyTradeRequestField(){
        if(this.receiveWoodTextField.getText().isEmpty()) this.receiveWoodTextField.setText("0");
        if(this.sendWoodTextField.getText().isEmpty()) this.sendWoodTextField.setText("0");

        if(this.receiveIronTextField.getText().isEmpty()) this.receiveIronTextField.setText("0");
        if(this.sendIronTextField.getText().isEmpty()) this.sendIronTextField.setText("0");

        if(this.receiveStoneTextField.getText().isEmpty()) this.receiveStoneTextField.setText("0");
        if(this.sendStoneTextField.getText().isEmpty()) this.sendStoneTextField.setText("0");

        if(this.receiveGunPowderTextField.getText().isEmpty()) this.receiveGunPowderTextField.setText("0");
        if(this.sendGunPowderTextField.getText().isEmpty()) this.sendGunPowderTextField.setText("0");

        if(this.receiveSoilTextField.getText().isEmpty()) this.receiveSoilTextField.setText("0");
        if(this.sendSoilTextField.getText().isEmpty()) this.sendSoilTextField.setText("0");

        if(this.receiveWaterTextField.getText().isEmpty()) this.receiveWaterTextField.setText("0");
        if(this.sendWaterTextField.getText().isEmpty()) this.sendWaterTextField.setText("0");
    }

    private void clearTradeTextFields(){
        this.receiveWoodTextField.clear();
        this.sendWoodTextField.clear();
        this.receiveIronTextField.clear();
        this.receiveGunPowderTextField.clear();
        this.sendIronTextField.clear();
        this.sendGunPowderTextField.clear();
        this.receiveSoilTextField.clear();
        this.sendSoilTextField.clear();
        this.receiveStoneTextField.clear();
        this.sendStoneTextField.clear();
        this.receiveWaterTextField.clear();
        this.sendWaterTextField.clear();
    }

    private void setupResourceSalesListeners() {
        sellWoodTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (newValue.isEmpty()) {
                    receivedCoinFromSellingWood.setText("0");
                } else {
                    int amount = Integer.parseInt(newValue);
                    receivedCoinFromSellingWood.setText(String.valueOf(callCoin(amount, ResourcesType.WOOD)));
                }
            } catch (NumberFormatException e) {
                sellWoodTextField.setText(oldValue);
            }
        });

        sellIronTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (newValue.isEmpty()) {
                    receivedCoinFromSellingIron.setText("0");
                } else {
                    int amount = Integer.parseInt(newValue);
                    receivedCoinFromSellingIron.setText(String.valueOf(callCoin(amount, ResourcesType.IRON)));
                }
            } catch (NumberFormatException e) {
                sellWoodTextField.setText(oldValue);
            }
        });

        sellStoneTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (newValue.isEmpty()) {
                    receivedCoinFromSellingStone.setText("0");
                } else {
                    int amount = Integer.parseInt(newValue);
                    receivedCoinFromSellingStone.setText(String.valueOf(callCoin(amount, ResourcesType.STONE)));
                }
            } catch (NumberFormatException e) {
                sellWoodTextField.setText(oldValue);
            }
        });

        sellSoilTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (newValue.isEmpty()) {
                    receivedCoinFromSellingSoil.setText("0");
                } else {
                    int amount = Integer.parseInt(newValue);
                    receivedCoinFromSellingSoil.setText(String.valueOf(callCoin(amount, ResourcesType.CLEAN_SOIL)));
                }
            } catch (NumberFormatException e) {
                sellWoodTextField.setText(oldValue);
            }
        });

        sellWaterTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (newValue.isEmpty()) {
                    receivedCoinFromSellingWater.setText("0");
                } else {
                    int amount = Integer.parseInt(newValue);
                    receivedCoinFromSellingWater.setText(String.valueOf(callCoin(amount, ResourcesType.CLEAN_WATER)));
                }
            } catch (NumberFormatException e) {
                sellWoodTextField.setText(oldValue);
            }});

        sellGunPowderTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (newValue.isEmpty()) {
                    receivedCoinFromSellingGunPowder.setText("0");
                } else {
                    int amount = Integer.parseInt(newValue);
                    receivedCoinFromSellingGunPowder.setText(String.valueOf(callCoin(amount, ResourcesType.GUN_POWDER)));
                }
            } catch (NumberFormatException e) {
                sellWoodTextField.setText(oldValue);
            }});
    }

    @FXML
    private void onMakeASellDealClicked(){
        this.player.getVillage().getLock().writeLock().lock();
        try {
            ResourcesManagement resourcesManagement = this.player.getVillage().getResourcesManagement();
            resourcesManagement.sellResource(Integer.parseInt(sellWoodTextField.getText()), ResourcesType.WOOD);
            resourcesManagement.sellResource(Integer.parseInt(sellIronTextField.getText()), ResourcesType.IRON);
            resourcesManagement.sellResource(Integer.parseInt(sellGunPowderTextField.getText()), ResourcesType.GUN_POWDER);
            resourcesManagement.sellResource(Integer.parseInt(sellWaterTextField.getText()), ResourcesType.CLEAN_WATER);
            resourcesManagement.sellResource(Integer.parseInt(sellSoilTextField.getText()), ResourcesType.CLEAN_SOIL);

            this.hideSellResourcesPanel();
            this.hideDecisionCustomhousePanel();
        }finally {
            this.player.getVillage().getLock().writeLock().unlock();
        }

    }

    @FXML
    private void onUpgradeCustomhouseDecisionClicked(){
        this.showBuildingInfo(this.selectedCustomhouse);
        this.hideDecisionPanel();
    }

    @FXML
    private void onManageCostumeHouseDecisionClicked(){
        this.showSellResourcesPanel();

    }

    @FXML
    private void onBuildPlantClicked(){
        this.showAddPlantPanel();
        this.hideShopPanel();
    }


    @FXML
    private void onReceivedTradeRequests(){
        this.hideTradePanel();
        this.showReceivedTradeRequestsPanel();
        this.showTradeRequestsOnPanel(this.player);
    }


    @FXML
    private void onUpgradeClicked(ActionEvent event) {
        if (this.controller == null) {
            return;
        }

        if (this.controller.getSelectedPlant() != null) {
            var selectedPlant = this.controller.getSelectedPlant();

            this.player.getVillage().getPlants().remove(selectedPlant.getId());

            this.player.getVillage().getGameMap().getTile(
                    selectedPlant.getPosition().getX(),
                    selectedPlant.getPosition().getY()
            ).setPlant(null);

            this.hideInfoPanel();
            this.hideDecisionCustomhousePanel();
        } else {
            this.controller.handleUpgradeClicked();
            this.hideInfoPanel();
            this.hideDecisionCustomhousePanel();
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
            if(!checkResourcesAndAlert(BuildingType.WOOD_MINE)) return;
            if(showConstructionConfirmation("WoodMiner")) {
                this.hideAddBuildingPanel();
                controller.enterBuildMode(BuildingType.WOOD_MINE);
            }
        }
    }

    @FXML
    private void onBuildCustomHouseClicked(){
        if (controller != null) {
            if(!checkResourcesAndAlert(BuildingType.CUSTOMHOUSE)) return;
            if(showConstructionConfirmation("CUSTOMHOUSE")) {
                this.hideAddBuildingPanel();
                controller.enterBuildMode(BuildingType.CUSTOMHOUSE);
            }
        }
    }

    @FXML
    private void onBallistaBuildClicked(){

        if (controller != null) {
            if(!checkResourcesAndAlert(BuildingType.BALLISTA_DEFENSIVE)) return;
            if(showConstructionConfirmation("BALLISTA_DEFENSIVE")) {
                this.hideAddBuildingPanel();
                controller.enterBuildMode(BuildingType.BALLISTA_DEFENSIVE);
            }
        }
    }

    @FXML
    private void onSentinelBuildClicked(){

        if (controller != null) {
            if(!checkResourcesAndAlert(BuildingType.SENTINEL_DEFENSIVE)) return;
            if(showConstructionConfirmation("SENTINEL_DEFENSIVE")) {
                this.hideAddBuildingPanel();
                controller.enterBuildMode(BuildingType.SENTINEL_DEFENSIVE);
            }
        }

    }

    @FXML
    private void onCatapultBuildClicked(){
        if (controller != null) {
            if(!checkResourcesAndAlert(BuildingType.CATAPULT_DEFENSIVE)) return;
            if(showConstructionConfirmation("CATAPULT_DEFENSIVE")) {
                this.hideAddBuildingPanel();
                controller.enterBuildMode(BuildingType.CATAPULT_DEFENSIVE);
            }
        }
    }

    @FXML
    private void onIronMineBuildClicked(ActionEvent actionEvent){
        if (controller != null) {
            if(!checkResourcesAndAlert(BuildingType.IRON_MINE)) return;
            if(showConstructionConfirmation("IronMiner")) {
                this.hideAddBuildingPanel();
                controller.enterBuildMode(BuildingType.IRON_MINE);
            }
        }
    }

    @FXML
    private void onStoneMineBuildClicked(ActionEvent actionEvent){
        if (controller != null) {
            if(!checkResourcesAndAlert(BuildingType.STONE_MINE)) return;
            if(showConstructionConfirmation("StoneMiner")) {
                this.hideAddBuildingPanel();
                controller.enterBuildMode(BuildingType.STONE_MINE);
            }
        }
    }

    @FXML
    private void onWaterMineBuildClicked(ActionEvent actionEvent){
        if (controller != null) {
            if(!checkResourcesAndAlert(BuildingType.WATER_STORAGE)) return;
            if(showConstructionConfirmation("DirtyWaterMine")) {
                this.hideAddBuildingPanel();
                controller.enterBuildMode(BuildingType.DIRTY_WATER_MINE);
            }
        }
    }

    @FXML
    private void onSoilMineBuildClicked(ActionEvent actionEvent){
        if (controller != null) {
            if(!checkResourcesAndAlert(BuildingType.SOIL_STORAGE)) return;
            if(showConstructionConfirmation("DirtySoilMine")) {
                this.hideAddBuildingPanel();
                controller.enterBuildMode(BuildingType.DIRTY_SOIL_MINE);
            }
        }
    }

    @FXML
    private void onGunPowderMineBuildClicked(ActionEvent actionEvent){
        if (controller != null) {
            if(!checkResourcesAndAlert(BuildingType.GUNPOWDER_MINE)) return;
            if(showConstructionConfirmation("GunpowderMine")) {
                this.hideAddBuildingPanel();
                controller.enterBuildMode(BuildingType.GUNPOWDER_MINE);
            }
        }
    }


    @FXML
    private void onCustomhouseBuildClicked(ActionEvent actionEvent) {
        if (controller != null) {
            if (!checkResourcesAndAlert(BuildingType.CUSTOMHOUSE)) return;
            if(showConstructionConfirmation("Customhouse")) {
                this.hideAddBuildingPanel();
                controller.enterBuildMode(BuildingType.CUSTOMHOUSE);
            }
        }
    }

    @FXML
    private void onMajorBuildingBuildClicked(ActionEvent actionEvent){
        if(controller != null){
            if(!checkResourcesAndAlert(BuildingType.MAJOR_BUILDING)) return;
            if(showConstructionConfirmation("TownHall")) {
                this.hideAddBuildingPanel();
                controller.enterBuildMode(BuildingType.MAJOR_BUILDING);
            }
        }
    }

    @FXML
    private  void onLabBuildClicked(){
        if (controller != null) {
            if(!checkResourcesAndAlert(BuildingType.LABORATORY)) return;
            if(showConstructionConfirmation("Laboratory")) {
                this.hideAddBuildingPanel();
                controller.enterBuildMode(BuildingType.LABORATORY);
            }
        }
    }

    @FXML
    private void onWaterPurifierBuildClicked(){
        if(controller != null){
            this.hideAddBuildingPanel();
            controller.enterBuildMode(BuildingType.WATER_PURIFIER);
        }
    }

    @FXML
    private void onSoilPurifierBuildClicked(){
        if(controller != null){
            this.hideAddBuildingPanel();
            controller.enterBuildMode(BuildingType.SOIL_PURIFIER);
        }
    }

    @FXML
    private void onResearchCenterBuildClicked(){
        if(controller != null){
            if(!checkResourcesAndAlert(BuildingType.RESEARCH_CENTER)) return;
            if(showConstructionConfirmation("ResearchCenter")) {
                this.hideAddBuildingPanel();
                controller.enterBuildMode(BuildingType.RESEARCH_CENTER);
            }
        }
    }

    @FXML
    private void onNRCBuildClicked(ActionEvent actionEvent){
        if(controller != null) {
            if(!checkResourcesAndAlert(PlantType.NRC)) return;
            if(showConstructionConfirmation("NRC Plant")) {
                this.hideAddBuildingPanel();
                controller.enterPlantBuildMode(PlantType.NRC);
                this.hideAddPlantPanel();
            }
        }
    }

    @FXML
    private void onSNRCBuildClicked(ActionEvent actionEvent){
        if(controller != null) {
            if(!checkResourcesAndAlert(PlantType.SNRC)) return;
            if(showConstructionConfirmation("SNRC Plant")) {
                this.hideAddBuildingPanel();
                controller.enterPlantBuildMode(PlantType.SNRC);
                this.hideAddPlantPanel();
            }
        }
    }

    @FXML
    private void onPSNRCBuildClicked(ActionEvent actionEvent){
        if(controller != null) {
            if(!checkResourcesAndAlert(PlantType.PSNRC)) return;
            if(showConstructionConfirmation("PSNRC Plant")) {
                this.hideAddBuildingPanel();
                controller.enterPlantBuildMode(PlantType.PSNRC);
                this.hideAddPlantPanel();
            }
        }
    }

    private boolean showConstructionConfirmation(String typeName) {
        Alert confirmAlert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure you want to build " + typeName + "?",
                ButtonType.YES,
                ButtonType.NO
        );
        confirmAlert.setTitle("Final Approval");
        confirmAlert.setHeaderText("Confirm Construction");

        java.util.Optional<ButtonType> result = confirmAlert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.YES;
    }

    private void showReceivedTradeRequestsPanel(){
        this.receivedTradeRequestsPanel.setVisible(true);
        this.receivedTradeRequestsPanel.setManaged(true);
    }

    public void hideReceivedTradeRequestsPanel(){
        this.receivedTradeRequestsPanel.setVisible(false);
        this.receivedTradeRequestsPanel.setManaged(false);
    }

    public void showTradePanel(){
        this.tradePanel.setVisible(true);
        this.tradePanel.setManaged(true);
    }

    public void hideTradePanel(){
        this.tradePanel.setVisible(false);
        this.tradePanel.setManaged(false);
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


    @FXML
    private void onUpgradeDecisionClicked(){
        this.showBuildingInfo(this.SelectedArmyProducer);
        this.hideDecisionPanel();
    }

    @FXML
    private void onManageArmyDecisionClicked(){
        this.openArmyProducer(this.SelectedArmyProducer);
        this.hideDecisionPanel();
    }

    @FXML
    private void onPendingOrdersClicked(){
        this.showDecidePanel();
    }

    @FXML
    private void onBattleHistoryClicked(){
        showAttackHistoryPanel();
        this.showAttackHistory();
    }

    @FXML
    private void onBattleButtonClicked(){
        this.showAttackPanel();
        this.setEnemies();
        this.showEnemies();
    }

    @FXML
    private void onManagePendingAllianceRequestsClicked(){
        this.hideAlliancePanel();
        this.showAllianceRequestsPanel();
        this.showAllianceRequests();
    }

    @FXML
    private void onAllianceButtonClicked(){
        this.showAlliancePanel();
        this.findAllowedPlayerForAlliance();
        showAllowedToAllianceOnPanel(this.allowedToAlliance);
    }

    @FXML
    private void onTradeButtonClicked(){
        this.showTradePanel();
        this.validPlayersToTrade();
        this.showTradersOnPanel(this.traders);
    }

    @FXML
    private void onPendingSentRequestButtonClicked(){
        this.hideTradePanel();
        this.showSentTradeRequestsPanel();
        this.showSentTradeRequestsOnPanel();
    }

    @FXML
    private void onMakeADealClicked(){

        Map<ResourcesType , Integer> offeredResources = new HashMap<>();
        Map<ResourcesType , Integer> requestedResources = new HashMap<>();

        this.makeDeals(offeredResources, requestedResources);
        this.clearTradeTextFields();

        TradeService tradeService = new TradeService();
        tradeService.sendRequest(this.player, this.getReceiverTradeRequest(), offeredResources, requestedResources);

        this.hideMakeATradePanel();
    }

    @FXML
    private void onManageBarracksDecisionClicked(){
        this.openBarrack(this.selectedBarrack);
        this.hideDecisionBarrackPanel();
    }

    private Barrack selectedBarrack;

    @FXML
    private  void onUpgradeBarracksDecisionClicked(){
        this.showBuildingInfo(this.selectedBarrack);
        this.hideDecisionBarrackPanel();
    }

    @FXML
    private void onTowerButtonClicked(){
        this.refreshGlobalTowerPanel();
        this.showGlobalTowerPanel();
    }

    @FXML
    private void onLeaveTowerPanelClicked(){
        this.hideGlobalTowerPanel();
    }



    public void openArmyProducer(ArmyProducer armyProducer) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/project/armyProducer.fxml")
            );

            Parent root = loader.load();

            ArmyProducerController armyProducerController = loader.getController();
            armyProducerController.setPlayer(player,armyProducer/*, this.controller*/);
            armyProducerController.hideQueuePanel();
            armyProducerController.showArmyProducerPanel();

            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            Stage stage = new Stage();
            stage.setTitle("Army Producer");
            stage.setScene(scene);

            stage.initStyle(StageStyle.UNDECORATED);

            stage.setResizable(false);

            stage.show();
            stage.setOnHidden(e -> armyProducerController.stopRefresh());

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

            stage.initStyle(StageStyle.UNDECORATED);
            stage.setResizable(false);

            stage.show();
            stage.setOnHidden(e -> controller.stopRefresh());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setTradeButtonEnable(){
        if(BuildingsManagement.checkResearchCenterBuildingForTrade(this.player) &&
                BuildingsManagement.checkCustomHouseBuildingForTrade(this.player)&&
                BuildingsManagement.checkResearchCenterBuildingForTrade(this.player))

            this.tradeButton.setDisable(false);
    }

    private void setAllianceButtonEnable(){
        this.player.getLock().readLock().lock();
        try {
            this.player.getVillage().getLock().readLock().lock();
            try {
                if(AllianceService.checkSenderAllianceRequestMajorBuildingLevel(this.player)
                        && AllianceService.checkCloudForAllianceSender(this.player)
                        && AllianceService.checkScienceLevelForAlliance(this.player))

                    this.allianceButton.setDisable(false);
            }finally {
                this.player.getVillage().getLock().readLock().unlock();
            }
        }finally {
            this.player.getLock().readLock().unlock();
        }
    }

    private void showMakeATradePanel(){
        this.makeATradePanel.setVisible(true);
        this.makeATradePanel.setManaged(true);
    }

    public void hideMakeATradePanel(){
        this.makeATradePanel.setVisible(false);
        this.makeATradePanel.setManaged(false);
    }

    public void showBuildingInfo(Building building){
        if (building == null) return;

        infoPanelTitleLabel.setText(building.getType().toString());
        buildingLevelLabel.setVisible(true);
        buildingLevelLabel.setText("Level: " + building.getLevel());

        if (building.getType() == BuildingType.LABORATORY) {
            plantsCountLabel.setVisible(true);
            neutralizationPowerLabel.setVisible(true);

            int totalPlants = player.getVillage().getPlants().size();
            plantsCountLabel.setText("Total Plants: " + totalPlants);

            double totalPower = 0;
            for (model.building.Plant plant : player.getVillage().getPlants().values()) {
                totalPower += plant.getNeutralizationPower();
            }
            neutralizationPowerLabel.setText("Neutralization Power: " + totalPower);
        } else {
            plantsCountLabel.setVisible(false);
            neutralizationPowerLabel.setVisible(false);
        }

        boolean isMaxLevel = Cost.upgradeCost(building).isMaxLevelReached();

        if (isMaxLevel) {
            this.upgradeButton.setText("Maximum level");
            this.upgradeButton.setDisable(true);
        } else {
            this.upgradeButton.setText("Upgrade " + building.getType().toString() + " (Lvl " + building.getLevel() + ")");

            if (building.getBuildingStatus() == BuildingStatus.UPGRADING || building.getBuildingStatus() == BuildingStatus.BUILDING) {
                this.upgradeButton.setDisable(true);
            } else {
                this.upgradeButton.setDisable(false);
            }
        }

        this.infoPanel.setVisible(true);
        this.infoPanel.setManaged(true);

        System.out.println("UI Panel updated for: " + building.getType() + " Level: " + building.getLevel());
    }

    public void showPlantInfo(Plant plant){
        if(plant==null){return;}
        this.infoPanelTitleLabel.setText(plant.getType().toString());
        this.buildingLevelLabel.setVisible(false);
        this.plantsCountLabel.setVisible(false);
        this.neutralizationPowerLabel.setVisible(false);

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

    private void showSentTradeRequestsPanel(){
        this.sentTradeRequestsPanel.setVisible(true);
        this.sentTradeRequestsPanel.setManaged(true);
    }

    public void hideSentTradeRequestsPanel(){
        this.sentTradeRequestsPanel.setVisible(false);
        this.sentTradeRequestsPanel.setManaged(false);
    }

    public void hideAlliancePanel(){
        this.alliancePanel.setVisible(false);
        this.alliancePanel.setManaged(false);
    }

    private void showAlliancePanel(){
        this.alliancePanel.setVisible(true);
        this.alliancePanel.setManaged(true);
    }

    private void showAllianceRequestsPanel(){
        this.allianceRequestsPanel.setVisible(true);
        this.allianceRequestsPanel.setManaged(true);
    }

    public void hideAllianceRequestsPanel(){
        this.allianceRequestsPanel.setVisible(false);
        this.allianceRequestsPanel.setManaged(false);
    }

    public void hideAttackPanel(){
        this.battlePanel.setVisible(false);
        this.battlePanel.setManaged(false);
    }

    private void showAttackPanel(){
        this.battlePanel.setVisible(true);
        this.battlePanel.setManaged(true);
    }

    private void showAttackHistoryPanel(){
        this.battleHistoryPannel.setVisible(true);
        this.battleHistoryPannel.setManaged(true);
    }

    public void hideAttackHistoryPanel(){
        this.battleHistoryPannel.setVisible(false);
        this.battleHistoryPannel.setManaged(false);
    }

    private void showDecidePanel(){
        this.decidePanel.setVisible(true);
        this.decidePanel.setVisible(true);
    }

    public void hideDecidePanel(){
        this.decidePanel.setVisible(false);
        this.decidePanel.setVisible(false);
    }

    @FXML
    private void onBuildGlobalTowerClicked() {
        if (gameState == null || gameState.getPhaseTwoStartTime() == null) {
            refreshGlobalTowerPanel();
            return;
        }

        BuildingsManagement buildingsManagement = this.controller.getBuildingsManagement();

        if (!buildingsManagement.canBuildGlobalTower()) {
            this.refreshGlobalTowerPanel();
            return;
        }

        this.hideGlobalTowerPanel();
        this.controller.enterGlobalTowerBuildMode();
    }

    public void hideDecisionPanel(){
        this.decisionPanel.setVisible(false);
        this.decisionPanel.setManaged(false);
    }
    public void showDecisionPanel(Building building){
        this.SelectedArmyProducer = (ArmyProducer) building;
        this.decisionPanel.setManaged(true);
        this.decisionPanel.setVisible(true);
    }

    public void hideDecisionBarrackPanel(){
        this.barracksDecisionPanel.setVisible(false);
        this.barracksDecisionPanel.setManaged(false);
    }

    public void showDecisionBarrackPanel(Building building){
        this.selectedBarrack = (Barrack) building;
        this.barracksDecisionPanel.setManaged(true);
        this.barracksDecisionPanel.setVisible(true);
    }

    private void showAddPlantPanel(){
        this.addPlantPanel.setVisible(true);
        this.addPlantPanel.setManaged(true);
    }

    public void hideAddPlantPanel(){
        this.addPlantPanel.setVisible(false);
        this.addPlantPanel.setManaged(false);
    }



    private void showGlobalTowerPanel() {
        globalTowerPanel.setVisible(true);
        globalTowerPanel.setManaged(true);
    }

    public void hideGlobalTowerPanel() {
        globalTowerPanel.setVisible(false);
        globalTowerPanel.setManaged(false);
    }

    public void showDecisionCustomhousePanel(Customhouse customhouse){
        this.selectedCustomhouse = customhouse;
        this.decisionCustomhousePanel.setVisible(true);
        this.decisionCustomhousePanel.setManaged(true);
    }

    public void hideDecisionCustomhousePanel(){
        this.decisionCustomhousePanel.setVisible(false);
        this.decisionCustomhousePanel.setManaged(false);
    }



    public void showSellResourcesPanel(){
        this.sellResourcesPanel.setVisible(true);
        this.sellResourcesPanel.setManaged(true);

        setEmptySellRequestsField();
        this.setupResourceSalesListeners();
        this.clearSellResourcesTextFields();
    }

    public void hideSellResourcesPanel(){
        this.sellResourcesPanel.setVisible(false);
        this.sellResourcesPanel.setManaged(false);
    }

    private void refreshGlobalTowerPanel(){
        GlobalTower tower = this.player.getVillage().getGlobalTower();
        BuildingsManagement buildingsManagement = this.controller.getBuildingsManagement();

        if (tower == null || !tower.isActive()) {

            this.towerStatusLabel.setText(tower != null ? "Status: destroyed" : "Status: not built");
            this.towerHpLabel.setText("Health: -- / " + (tower != null ? tower.getMaxHp() : 1200));
            this.towerHpBar.setProgress(0);

            if (gameState.getPhaseTwoStartTime() == null) {
                this.buildTowerButton.setDisable(true);
                this.towerRequirementWarningLabel.setText("Global Tower is unlocked after Phase 2 begins.");
                return;
            }

            boolean canBuild = buildingsManagement.canBuildGlobalTower();
            this.buildTowerButton.setDisable(!canBuild);
            this.buildTowerButton.setVisible(true);
            this.towerRequirementWarningLabel.setText(canBuild
                    ? ""
                    : "Requirements not met yet (Major Building & Research Center must be level 5, and you need enough resources).");

        } else {

            if (tower.isUnderProtection()) {
                long secondsLeft = tower.getRemainingProtection().getSeconds();
                this.towerStatusLabel.setText("Status: active — PROTECTED (" + secondsLeft + "s left, elimination risk if destroyed now)");
                this.towerStatusLabel.setTextFill(javafx.scene.paint.Color.web("#4dd0e1"));
            } else {
                this.towerStatusLabel.setText("Status: active — unprotected (destruction just costs the tower, rebuildable)");
                this.towerStatusLabel.setTextFill(javafx.scene.paint.Color.WHITE);
            }

            this.towerHpLabel.setText("Health: " + tower.getHp() + " / " + tower.getMaxHp());
            this.towerHpBar.setProgress((double) tower.getHp() / tower.getMaxHp());

            java.time.LocalDateTime completeTime = tower.getConstructionCompleteTime();
            if (completeTime != null) {
                java.time.LocalDateTime protectionEnds = completeTime.plusHours(24);
                if (java.time.LocalDateTime.now().isBefore(protectionEnds)) {
                    long minutesLeft = java.time.Duration.between(java.time.LocalDateTime.now(), protectionEnds).toMinutes();
                    this.towerProtectionLabel.setText("in protecting: " + minutesLeft + "remained ");
                } else {
                    this.towerProtectionLabel.setText("");
                }
            }

            this.buildTowerButton.setDisable(true);
            this.buildTowerButton.setVisible(false);
            this.towerRequirementWarningLabel.setText("");
        }
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    private void updateResourcesUI(){
        Resources resources = this.player.getVillage().getResources();

        ResourcesManagement resourcesManagement = this.player.getVillage().getResourcesManagement();

        int maxWoodCapacity = 10000;//resourcesManagement.getMaxCapacity(ResourcesType.WOOD);
        int maxIronCapacity = 10000;//resourcesManagement.getMaxCapacity(ResourcesType.IRON);
        int maxStoneCapacity = 10000;//resourcesManagement.getMaxCapacity(ResourcesType.STONE);
        int maxCleanWaterCapacity = 10000;//resourcesManagement.getMaxCapacity(ResourcesType.CLEAN_WATER);
        int maxCleanSoilCapacity = 10000;//resourcesManagement.getMaxCapacity(ResourcesType.CLEAN_SOIL);
        int maxGunPowderCapacity = 10000;//resourcesManagement.getMaxCapacity(ResourcesType.GUN_POWDER);

        int currentWood = resources.getAmount(ResourcesType.WOOD);
        int currentIron = resources.getAmount(ResourcesType.IRON);
        int currentStone = resources.getAmount(ResourcesType.STONE);
        int currentWater = resources.getAmount(ResourcesType.CLEAN_WATER);
        int currentSoil = resources.getAmount(ResourcesType.CLEAN_SOIL);
        int currentGunPowder = resources.getAmount(ResourcesType.GUN_POWDER);
        int currentCoin = resources.getAmount(ResourcesType.COIN);

        this.woodLabel.setText("" + currentWood + " / " + maxWoodCapacity);
        this.ironLabel.setText( "" + currentIron + " / " + maxIronCapacity);
        this.stoneLabel.setText( "" + currentStone + " / " + maxStoneCapacity);
        this.cleanWaterLabel.setText("" + currentWater + " / " + maxCleanWaterCapacity);
        this.cleanSoilLabel.setText("" + currentSoil + " / " + maxCleanSoilCapacity);
        this.gunPowderLabel.setText("" + currentGunPowder + " / " + maxGunPowderCapacity);
        this.coinLabel.setText("" + currentCoin + " / " + 5000);

        if (maxWoodCapacity > 0) woodProgressBar.setProgress((double) currentWood / maxWoodCapacity);
        if (maxIronCapacity > 0) ironProgressBar.setProgress((double) currentIron / maxIronCapacity);
        if (maxStoneCapacity > 0) stoneProgressBar.setProgress((double) currentStone / maxStoneCapacity);
        if (maxCleanWaterCapacity > 0) cleanWaterProgressBar.setProgress((double) currentWater / maxCleanWaterCapacity);
        if (maxCleanSoilCapacity > 0) cleanSoilProgressBar.setProgress((double) currentSoil / maxCleanSoilCapacity);
        if (maxGunPowderCapacity > 0) gunPowderProgressBar.setProgress((double) currentGunPowder / maxGunPowderCapacity);

        coinProgressBar.setProgress((double) currentCoin / 5000);


        this.applyWoodStyle();

        this.applyIronStyle();

        this.applyStoneStyle();

        this.applyCleanSoilStyle();

        this.applyCleanWaterStyle();

        this.applyGunPowderStyle();

        this.applyCoinStyle();

    }

    private void applyCoinStyle(){
        coinProgressBar.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-border-radius: 15; " +
                        "-fx-border-color: #d2c748; " +
                        "-fx-border-width: 1.5;"
        );

        var barLayer = coinProgressBar.lookup(".bar");
        if (barLayer != null) {
            barLayer.setStyle(

                    "-fx-background-color: linear-gradient(to right, #e6ff04, #dab11c); " +
                            "-fx-background-radius: 15; " +

                            "-fx-effect: dropshadow(gaussian, #ffc408, 25, 0.6, 0, 0);");
        }

        var trackLayer = coinProgressBar.lookup(".track");
        if (trackLayer != null) {
            trackLayer.setStyle(
                    "-fx-background-color: rgba(0, 0, 0, 0.65); " +
                            "-fx-background-radius: 15;");
        }
    }

    private void applyWoodStyle(){
        woodProgressBar.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-border-radius: 15; " +
                        "-fx-border-color: #d2a048; " +
                        "-fx-border-width: 1.5;");

        var barLayer = woodProgressBar.lookup(".bar");
        if (barLayer != null) {
            barLayer.setStyle(

                    "-fx-background-color: linear-gradient(to right, #8e6220, #dab11c); " +
                            "-fx-background-radius: 15; " +

                            "-fx-effect: dropshadow(gaussian, #ffc83b, 25, 0.6, 0, 0);");
        }

        var trackLayer = woodProgressBar.lookup(".track");
        if (trackLayer != null) {
            trackLayer.setStyle(
                    "-fx-background-color: rgba(0, 0, 0, 0.65); " +
                            "-fx-background-radius: 15;");
        }
    }

    private void applyIronStyle(){
        ironProgressBar.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-border-radius: 15; " +
                        "-fx-border-color: #a29e99; " +
                        "-fx-border-width: 1.5;");

        var barLayer = ironProgressBar.lookup(".bar");
        if (barLayer != null) {
            barLayer.setStyle(

                    "-fx-background-color: linear-gradient(to right, #887f6c, #bdb8a6); " +
                            "-fx-background-radius: 15; " +

                            "-fx-effect: dropshadow(gaussian, #9a927c, 25, 0.6, 0, 0);");
        }

        var trackLayer = ironProgressBar.lookup(".track");
        if (trackLayer != null) {
            trackLayer.setStyle(
                    "-fx-background-color: rgba(0, 0, 0, 0.65); " +
                            "-fx-background-radius: 15;");
        }
    }

    private void applyStoneStyle(){
        stoneProgressBar.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-border-radius: 15; " +
                        "-fx-border-color: #ffe0ad; " +
                        "-fx-border-width: 1.5;");

        var barLayer = stoneProgressBar.lookup(".bar");
        if (barLayer != null) {
            barLayer.setStyle(

                    "-fx-background-color: linear-gradient(to right, #f1cf9d, #ffeeba); " +
                            "-fx-background-radius: 15; " +

                            "-fx-effect: dropshadow(gaussian, #fdeab5, 25, 0.6, 0, 0);");
        }

        var trackLayer = stoneProgressBar.lookup(".track");
        if (trackLayer != null) {
            trackLayer.setStyle(
                    "-fx-background-color: rgba(0, 0, 0, 0.65); " +
                            "-fx-background-radius: 15;");
        }
    }

    private void applyCleanSoilStyle(){
        cleanSoilProgressBar.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-border-radius: 15; " +
                        "-fx-border-color: #4b3917; " +
                        "-fx-border-width: 1.5;");

        var barLayer = cleanSoilProgressBar.lookup(".bar");
        if (barLayer != null) {
            barLayer.setStyle(

                    "-fx-background-color: linear-gradient(to right, #8e6220, #4f4111); " +
                            "-fx-background-radius: 15; " +

                            "-fx-effect: dropshadow(gaussian, #564312, 25, 0.6, 0, 0);");
        }

        var trackLayer = cleanSoilProgressBar.lookup(".track");
        if (trackLayer != null) {
            trackLayer.setStyle(
                    "-fx-background-color: rgba(0, 0, 0, 0.65); " +
                            "-fx-background-radius: 15;");
        }
    }

    private void applyCleanWaterStyle(){
        cleanWaterProgressBar.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-border-radius: 15; " +
                        "-fx-border-color: #0df7ff; " +
                        "-fx-border-width: 1.5;");

        var barLayer = cleanWaterProgressBar.lookup(".bar");
        if (barLayer != null) {
            barLayer.setStyle(

                    "-fx-background-color: linear-gradient(to right, #208e81, #1cdad4); " +
                            "-fx-background-radius: 15; " +

                            "-fx-effect: dropshadow(gaussian, #3bffdb, 25, 0.6, 0, 0);");
        }

        var trackLayer = cleanWaterProgressBar.lookup(".track");
        if (trackLayer != null) {
            trackLayer.setStyle(
                    "-fx-background-color: rgba(0, 0, 0, 0.65); " +
                            "-fx-background-radius: 15;");
        }
    }

    private void applyGunPowderStyle(){
        gunPowderProgressBar.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-border-radius: 15; " +
                        "-fx-border-color: #56565d; " +
                        "-fx-border-width: 1.5;");

        var barLayer = gunPowderProgressBar.lookup(".bar");
        if (barLayer != null) {
            barLayer.setStyle(

                    "-fx-background-color: linear-gradient(to right, #413939, #363434); " +
                            "-fx-background-radius: 15; " +

                            "-fx-effect: dropshadow(gaussian, #3f3838, 25, 0.6, 0, 0);");
        }

        var trackLayer = gunPowderProgressBar.lookup(".track");
        if (trackLayer != null) {
            trackLayer.setStyle(
                    "-fx-background-color: rgba(0, 0, 0, 0.65); " +
                            "-fx-background-radius: 15;");
        }
    }


    private void startGameLoop() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {


                setTradeButtonEnable();
                setAllianceButtonEnable();

                taskProcessor.process();

                if (!eliminatedWindowShown
                        && playerRepository != null
                        && player != null
                        && !playerRepository.isPlayerExists(player.getPlayerId())) {

                    eliminatedWindowShown = true;
                    stopGameLoop(gameLoop);
                    eliminatedViewController.show(player.getEliminationReason());
                    return;
                }

                if (!winnerWindowShown
                        && gameState != null
                        && gameState.isPhaseTwoEnforced()) {

                    winnerWindowShown = true;
                    stopGameLoop(gameLoop);
                    winnerViewController.show(gameState.getGameWinner());
                    return;
                }

                updateShopButtonsAvailability();
                checkBuildingButtonsLimit();

                if(player.getVillage().getActiveBattles().isEmpty()){
                    battleButton.setDisable(false);
                }else {
                    battleButton.setDisable(true);
                }

                if(taskProcessor.isBattleFinished()){
                    battleButton.setDisable(false);
                }

                if(player.getAlliance() != null)
                    allianceButton.setDisable(true);

                if(player != null)
                    updateResourcesUI();

                if (gameCanvasView != null) {
                    gameCanvasView.draw();
                }

                if (globalTowerPanel.isVisible()) {
                    refreshGlobalTowerPanel();
                }

                healthProgressBar();
                radiationProgressBar();

                int currentAnnouncementCount = model.finalPart.GlobalTowerAnnouncer.getAnnouncementCount();
                if (currentAnnouncementCount > lastSeenAnnouncementCount) {
                    List<String> announcements = model.finalPart.GlobalTowerAnnouncer.getAnnouncements();
                    String latest = announcements.get(announcements.size() - 1);
                    towerAnnouncementLabel.setText(latest);
                    towerAnnouncementLabel.setVisible(true);
                    towerAnnouncementLabel.setManaged(true);

                    javafx.animation.PauseTransition hideDelay = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(8));
                    hideDelay.setOnFinished(e -> {
                        towerAnnouncementLabel.setVisible(false);
                        towerAnnouncementLabel.setManaged(false);
                    });
                    hideDelay.play();

                    lastSeenAnnouncementCount = currentAnnouncementCount;
                }
            }
        };
        gameLoop.start();
    }

    public void stopGameLoop(AnimationTimer gameLoop){
        gameLoop.stop();
    }

    private void updateShopButtonsAvailability() {
        int labLevel = 0;
        for (model.building.Building b : player.getVillage().getBuildings().values()) {
            if (b.getType() == model.building.BuildingType.LABORATORY && b.getBuildingStatus() == model.building.BuildingStatus.ACTIVE) {
                labLevel = b.getLevel();
                break;
            }
        }
        if (labLevel < 1) {
            nrcBuildButton.setDisable(true);
            nrcLabel.setText("NRC (Lab Lvl 1 Required) -> Current: " + labLevel);
            nrcLabel.setTextFill(Color.RED);
        } else {
            nrcBuildButton.setDisable(false);
            nrcLabel.setText("NRC");
            nrcLabel.setTextFill(Color.GREEN);
        }


        if (labLevel < 2) {
            snrcBuildButton.setDisable(true);
            snrcLabel.setText("SNRC (Lab Lvl 2 Required) -> Current: "+ labLevel);
            snrcLabel.setTextFill(Color.RED);
        } else {
            snrcBuildButton.setDisable(false);
            snrcLabel.setText("SNRC");
            snrcLabel.setTextFill(Color.GREEN);
        }

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
    private void checkBuildingButtonsLimit(){
        Village village = player.getVillage();
        if (village == null || village.getBuildings() == null) return;

        int woodMines = (int) village.getBuildings().values().stream().filter(b -> b.getType() == BuildingType.WOOD_MINE).count();
        if (woodMineBuildButton != null) {
            boolean isLimitReached = woodMines >= 5;
            woodMineBuildButton.setDisable(isLimitReached);
            if (isLimitReached && woodMineLabel != null) {
                woodMineLabel.setText("The number of woodMine has reached the limit");
            }
        }

        int stoneMines = (int) village.getBuildings().values().stream().filter(b -> b.getType() == BuildingType.STONE_MINE).count();
        if (stoneMineBuildButton != null) {
            boolean isLimitReached = stoneMines >= 5;
            stoneMineBuildButton.setDisable(isLimitReached);
            if (isLimitReached && stoneMineLabel != null) {
                stoneMineLabel.setText("The number of stoneMine has reached the limit");
            }
        }

        int ironMines = (int) village.getBuildings().values().stream().filter(b -> b.getType() == BuildingType.IRON_MINE).count();
        if (ironMineBuildButton != null) {
            boolean isLimitReached = ironMines >= 5;
            ironMineBuildButton.setDisable(isLimitReached);
            if (isLimitReached && ironMineLabel != null) {
                ironMineLabel.setText("The number of ironMine has reached the limit");
            }
        }

        int gunPowderMines = (int) village.getBuildings().values().stream().filter(b -> b.getType() == BuildingType.GUNPOWDER_MINE).count();
        if (gunPowderMineBuildButton != null) {
            boolean isLimitReached = gunPowderMines >= 4;
            gunPowderMineBuildButton.setDisable(isLimitReached);
            if (isLimitReached && gunPowderMineLabel != null) {
                gunPowderMineLabel.setText("The number of gunPowderMine has reached the limit");
            }
        }

        int dirtyWaterMines = (int) village.getBuildings().values().stream().filter(b -> b.getType() == BuildingType.DIRTY_WATER_MINE).count();
        if (waterMineBuildButton != null) {
            boolean isLimitReached = dirtyWaterMines >= 4; // اصلاح سقف به ۴ طبق صورت مسئله شما
            waterMineBuildButton.setDisable(isLimitReached);
            if (isLimitReached && dirtyWaterMineLabel != null) {
                dirtyWaterMineLabel.setText("The number of dirtyWaterMine has reached the limit");
            }
        }

        int dirtySoilMines = (int) village.getBuildings().values().stream().filter(b -> b.getType() == BuildingType.DIRTY_SOIL_MINE).count();
        if (soilMineBuildButton != null) {
            boolean isLimitReached = dirtySoilMines >= 4;
            soilMineBuildButton.setDisable(isLimitReached);
            if (isLimitReached && dirtySoilMineLabel != null) {
                dirtySoilMineLabel.setText("The number of dirtySoilMine has reached the limit");
            }
        }

        int woodStorages = (int) village.getBuildings().values().stream().filter(b -> b.getType() == BuildingType.WOOD_STORAGE).count();
        if (woodStorageBuildButton != null) {
            boolean isLimitReached = woodStorages >= 4;
            woodStorageBuildButton.setDisable(isLimitReached);
            if (isLimitReached && woodStorageLabel != null) {
                woodStorageLabel.setText("The number of woodStorage has reached the limit");
            }
        }

        int stoneStorages = (int) village.getBuildings().values().stream().filter(b -> b.getType() == BuildingType.STONE_STORAGE).count();
        if (stoneStorageBuildButton != null) {
            boolean isLimitReached = stoneStorages >= 4;
            stoneStorageBuildButton.setDisable(isLimitReached);
            if (isLimitReached && stoneStorageLabel != null) {
                stoneStorageLabel.setText("The number of stoneStorages has reached the limit");
            }
        }

        int ironStorages = (int) village.getBuildings().values().stream().filter(b -> b.getType() == BuildingType.IRON_STORAGE).count();
        if (ironStorageBuildButton != null) {
            boolean isLimitReached = ironStorages >= 4;
            ironStorageBuildButton.setDisable(isLimitReached);
            if (isLimitReached && ironStorageLabel != null) {
                ironStorageLabel.setText("The number of ironStorages has reached the limit");
            }
        }

        int gunPowderStorages = (int) village.getBuildings().values().stream().filter(b -> b.getType() == BuildingType.GUNPOWDER_STORAGE).count();
        if (gunPowderStorageBuildButton != null) {
            boolean isLimitReached = gunPowderStorages >= 4;
            gunPowderStorageBuildButton.setDisable(isLimitReached);
            if (isLimitReached && gunPowderStorageLabel != null) { // اصلاح نام متغیر لیبل به gunPowderStorageLabel
                gunPowderStorageLabel.setText("The number of gunPowderStorages has reached the limit");
            }
        }

        int waterPurifiers = (int) village.getBuildings().values().stream().filter(b -> b.getType() == BuildingType.WATER_PURIFIER).count();
        if (waterPurifierBuildButton != null) {
            boolean isLimitReached = waterPurifiers >= 3;
            waterPurifierBuildButton.setDisable(isLimitReached);
            if (isLimitReached && waterPurifierLabel != null) {
                waterPurifierLabel.setText("The number of waterPurifier has reached the limit");
            }
        }

        int soilPurifiers = (int) village.getBuildings().values().stream().filter(b -> b.getType() == BuildingType.SOIL_PURIFIER).count();
        if (soilPurifierBuildButton != null) {
            boolean isLimitReached = soilPurifiers >= 3;
            soilPurifierBuildButton.setDisable(isLimitReached);
            if (isLimitReached && soilPurifierLabel != null) {
                soilPurifierLabel.setText("The number of soilPurifier has reached the limit");
            }
        }

        int waterStorages = (int) village.getBuildings().values().stream().filter(b -> b.getType() == BuildingType.WATER_STORAGE).count();
        if (waterStorageBuildButton != null) {
            boolean isLimitReached = waterStorages >= 3;
            waterStorageBuildButton.setDisable(isLimitReached);
            if (isLimitReached && waterStorageLabel != null) {
                waterStorageLabel.setText("The number of waterStorage has reached the limit");
            }
        }

        int soilStorages = (int) village.getBuildings().values().stream().filter(b -> b.getType() == BuildingType.SOIL_STORAGE).count();
        if (soilStorageBuildButton != null) {
            boolean isLimitReached = soilStorages >= 3;
            soilStorageBuildButton.setDisable(isLimitReached);
            if (isLimitReached && soilStorageLabel != null) {
                soilStorageLabel.setText("The number of soilStorage has reached the limit");
            }
        }

        int ballistaDefensive = (int) village.getBuildings().values().stream().filter(b -> b.getType() == BuildingType.BALLISTA_DEFENSIVE).count();
        if (ballistaDefensiveBuildButton != null) {
            boolean isLimitReached = ballistaDefensive >= 8;
            ballistaDefensiveBuildButton.setDisable(isLimitReached);
            if (isLimitReached && ballistaDefensiveLabel != null) {
                ballistaDefensiveLabel.setText("The number of ballistaDefensive has reached the limit");
            }
        }

        int catapultDefensive = (int) village.getBuildings().values().stream().filter(b -> b.getType() == BuildingType.CATAPULT_DEFENSIVE).count();
        if (catapultDefensiveBuildButton != null) {
            boolean isLimitReached = catapultDefensive >= 6;
            catapultDefensiveBuildButton.setDisable(isLimitReached);
            if (isLimitReached && catapultDefensiveLabel != null) {
                catapultDefensiveLabel.setText("The number of catapultDefensive has reached the limit");
            }
        }

        int sentinelDefensive = (int) village.getBuildings().values().stream().filter(b -> b.getType() == BuildingType.SENTINEL_DEFENSIVE).count();
        if (sentinelDefensiveBuildButton != null) {
            boolean isLimitReached = sentinelDefensive >= 6;
            sentinelDefensiveBuildButton.setDisable(isLimitReached);
            if (isLimitReached && sentinelDefensiveLabel != null) {
                sentinelDefensiveLabel.setText("The number of sentinelDefensive has reached the limit");
            }
        }

        int majorBuildings = (int) village.getBuildings().values().stream().filter(b -> b.getType() == BuildingType.MAJOR_BUILDING).count();
        if (majorBuildingButton != null) {
            boolean isLimitReached = majorBuildings >= 1;
            majorBuildingButton.setDisable(isLimitReached);
            if (isLimitReached && majorBuildingLabel != null) {
                majorBuildingLabel.setText("The number of majorBuildings has reached the limit");
            }
        }

        int researchCenters = (int) village.getBuildings().values().stream().filter(b -> b.getType() == BuildingType.RESEARCH_CENTER).count();
        if (researchCenterBuildButton != null) {
            boolean isLimitReached = researchCenters >= 1;
            researchCenterBuildButton.setDisable(isLimitReached);
            if (isLimitReached && researchCenterLabel != null) {
                researchCenterLabel.setText("The number of researchCenters has reached the limit");
            }
        }

        int customhouse = (int) village.getBuildings().values().stream().filter(b -> b.getType() == BuildingType.CUSTOMHOUSE).count();
        if (customhouseBuildButton != null) {
            boolean isLimitReached = customhouse >= 1;
            customhouseBuildButton.setDisable(isLimitReached);
            if (isLimitReached && customhouseLabel != null) {
                customhouseLabel.setText("The number of customhouse has reached the limit");
            }
        }
        int laboratory = (int) village.getBuildings().values().stream().filter(b -> b.getType() == BuildingType.LABORATORY).count();
        if (laboratoryBuildButton != null) {
            boolean isLimitReached = laboratory >= 1;
            laboratoryBuildButton.setDisable(isLimitReached);
            if(isLimitReached && laboratoryLabel != null) {
                laboratoryLabel.setText("The number of laboratory has reached the limit");
            }
        }
    }


    public AnimationTimer getGameLoop() {
        return gameLoop;
    }


    public Player getReceiverTradeRequest() {
        return receiverTradeRequest;
    }

    public void setReceiverTradeRequest(Player receiverTradeRequest) {
        this.receiverTradeRequest = receiverTradeRequest;
    }

    public Map<UUID, Player> getTraders() {
        return traders;
    }

    public void setTraders(Map<UUID, Player> traders) {
        this.traders = traders;
    }

    public List<Player> getAllowedToAlliance() {
        return allowedToAlliance;
    }

    public void setAllowedToAlliance(List<Player> allowedToAlliance) {
        this.allowedToAlliance = allowedToAlliance;
    }

    public List<Player> getEnemies() {
        return enemies;
    }

    public void setEnemies(List<Player> enemies) {
        this.enemies = enemies;
    }
}