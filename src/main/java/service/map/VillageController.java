package service.map;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.battle.BattleArmy;
import model.battle.BattleHistory;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import model.building.*;
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
import service.resource.ResourcesManagement;
import service.trade.TradeOffer;
import service.trade.TradeService;
import service.trade.TradeStatus;

import java.awt.event.MouseEvent;
import java.util.*;

public class VillageController {

    private PlayerRepository playerRepository;

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


    @FXML private AnchorPane tradePanel;


    @FXML private VBox tradePlayersContainer;

    @FXML private Button makeADealButton;

    @FXML AnchorPane makeATradePanel;

    @FXML TextField receiveIronTextField;

    @FXML TextField sendWoodTextField;

    @FXML TextField receiveWoodTextField;

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

    @FXML AnchorPane decisionPanel;

    @FXML AnchorPane barracksDecisionPanel;


    //@FXML private ImageView borderImageView;


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

    private ArmyProducer SelectedArmyProducer;

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

    private HBox createSentTradeRequestsRowElement(TradeOffer offer){
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

        String receiverName = offer.getReceiverVillage().getUserName();
        Label receiverLabel = new Label("RECEIVER: " + receiverName);
        receiverLabel.setStyle("-fx-text-fill: #ff9800; -fx-font-size: 13px; -fx-font-weight: bold;");
        receiverLabel.setAlignment(Pos.CENTER);

        senderContainer.getChildren().addAll(receiverLabel);

        VBox offeredResourcesContainer = new VBox();
        offeredResourcesContainer.setSpacing(4);
        offeredResourcesContainer.setMinWidth(140);

        for (Map.Entry<ResourcesType, Integer> entry : offer.getOfferedResources().entrySet()) {
            ResourcesType resourcesType = entry.getKey();
            int amount = entry.getValue();
            if (amount > 0) {
                Label label = new Label(resourcesType.toString() + ": " + amount);
                label.setStyle("-fx-text-fill: #ff0000; -fx-font-size: 11px; -fx-font-weight: bold;");
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
                label.setStyle("-fx-text-fill: #08ff00; -fx-font-size: 11px; -fx-font-weight: bold;");
                requestedResourcesContainer.getChildren().add(label);
            }
        }

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);

        row.getChildren().addAll(senderContainer, spacer, offeredResourcesContainer, spacer2, requestedResourcesContainer);

        row.setOnMouseEntered(event -> row.setStyle(row.getStyle() + "-fx-background-color: #2d2d2d; -fx-border-color: #ff9800;"));
        row.setOnMouseExited(event -> row.setStyle(row.getStyle() + "-fx-background-color: #2b2b2b; -fx-border-color: #333333;"));


        return row;
        /*HBox row = new HBox();
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

        return row;*/
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

    @FXML
    private void onReceivedTradeRequests(){
        this.hideTradePanel();
        this.showReceivedTradeRequestsPanel();
        this.showTradeRequestsOnPanel(this.player);
    }


    @FXML
    private void onUpgradeClicked() {
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
                BuildingsManagement.checkCustomHouseBuildingForTrade(this.player))

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
        System.out.println(building.getBuildingStatus().toString());
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

        this.woodLabel.setText("" + currentWood + " / " + maxWoodCapacity);
        this.ironLabel.setText( "" + currentIron + " / " + maxIronCapacity);
        this.stoneLabel.setText( "" + currentStone + " / " + maxStoneCapacity);
        this.cleanWaterLabel.setText("" + currentWater + " / " + maxCleanWaterCapacity);
        this.cleanSoilLabel.setText("" + currentSoil + " / " + maxCleanSoilCapacity);
        this.gunPowderLabel.setText("" + currentGunPowder + " / " + maxGunPowderCapacity);

        if (maxWoodCapacity > 0) woodProgressBar.setProgress((double) currentWood / maxWoodCapacity);
        if (maxIronCapacity > 0) ironProgressBar.setProgress((double) currentIron / maxIronCapacity);
        if (maxStoneCapacity > 0) stoneProgressBar.setProgress((double) currentStone / maxStoneCapacity);
        if (maxCleanWaterCapacity > 0) cleanWaterProgressBar.setProgress((double) currentWater / maxCleanWaterCapacity);
        if (maxCleanSoilCapacity > 0) cleanSoilProgressBar.setProgress((double) currentSoil / maxCleanSoilCapacity);
        if (maxGunPowderCapacity > 0) gunPowderProgressBar.setProgress((double) currentGunPowder / maxGunPowderCapacity);


        this.applyWoodStyle();

        this.applyIronStyle();

        this.applyStoneStyle();

        this.applyCleanSoilStyle();

        this.applyCleanWaterStyle();

        this.applyGunPowderStyle();

    }

    private void applyWoodStyle(){
        woodProgressBar.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-border-radius: 15; " +
                        "-fx-border-color: #d2a048; " +
                        "-fx-border-width: 1.5;"
        );

        var barLayer = woodProgressBar.lookup(".bar");
        if (barLayer != null) {
            barLayer.setStyle(

                    "-fx-background-color: linear-gradient(to right, #8e6220, #dab11c); " +
                            "-fx-background-radius: 15; " +

                            "-fx-effect: dropshadow(gaussian, #ffc83b, 25, 0.6, 0, 0);"
            );
        }

        var trackLayer = woodProgressBar.lookup(".track");
        if (trackLayer != null) {
            trackLayer.setStyle(
                    "-fx-background-color: rgba(0, 0, 0, 0.65); " +
                            "-fx-background-radius: 15;"
            );
        }
    }

    private void applyIronStyle(){
        ironProgressBar.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-border-radius: 15; " +
                        "-fx-border-color: #a29e99; " +
                        "-fx-border-width: 1.5;"
        );

        var barLayer = ironProgressBar.lookup(".bar");
        if (barLayer != null) {
            barLayer.setStyle(

                    "-fx-background-color: linear-gradient(to right, #887f6c, #bdb8a6); " +
                            "-fx-background-radius: 15; " +

                            "-fx-effect: dropshadow(gaussian, #9a927c, 25, 0.6, 0, 0);"
            );
        }

        var trackLayer = ironProgressBar.lookup(".track");
        if (trackLayer != null) {
            trackLayer.setStyle(
                    "-fx-background-color: rgba(0, 0, 0, 0.65); " +
                            "-fx-background-radius: 15;"
            );
        }
    }

    private void applyStoneStyle(){
        stoneProgressBar.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-border-radius: 15; " +
                        "-fx-border-color: #ffe0ad; " +
                        "-fx-border-width: 1.5;"
        );

        var barLayer = stoneProgressBar.lookup(".bar");
        if (barLayer != null) {
            barLayer.setStyle(

                    "-fx-background-color: linear-gradient(to right, #f1cf9d, #ffeeba); " +
                            "-fx-background-radius: 15; " +

                            "-fx-effect: dropshadow(gaussian, #fdeab5, 25, 0.6, 0, 0);"
            );
        }

        var trackLayer = stoneProgressBar.lookup(".track");
        if (trackLayer != null) {
            trackLayer.setStyle(
                    "-fx-background-color: rgba(0, 0, 0, 0.65); " +
                            "-fx-background-radius: 15;"
            );
        }
    }

    private void applyCleanSoilStyle(){
        cleanSoilProgressBar.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-border-radius: 15; " +
                        "-fx-border-color: #4b3917; " +
                        "-fx-border-width: 1.5;"
        );

        var barLayer = cleanSoilProgressBar.lookup(".bar");
        if (barLayer != null) {
            barLayer.setStyle(

                    "-fx-background-color: linear-gradient(to right, #8e6220, #4f4111); " +
                            "-fx-background-radius: 15; " +

                            "-fx-effect: dropshadow(gaussian, #564312, 25, 0.6, 0, 0);"
            );
        }

        var trackLayer = cleanSoilProgressBar.lookup(".track");
        if (trackLayer != null) {
            trackLayer.setStyle(
                    "-fx-background-color: rgba(0, 0, 0, 0.65); " +
                            "-fx-background-radius: 15;"
            );
        }
    }

    private void applyCleanWaterStyle(){
        cleanWaterProgressBar.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-border-radius: 15; " +
                        "-fx-border-color: #0df7ff; " +
                        "-fx-border-width: 1.5;"
        );

        var barLayer = cleanWaterProgressBar.lookup(".bar");
        if (barLayer != null) {
            barLayer.setStyle(

                    "-fx-background-color: linear-gradient(to right, #208e81, #1cdad4); " +
                            "-fx-background-radius: 15; " +

                            "-fx-effect: dropshadow(gaussian, #3bffdb, 25, 0.6, 0, 0);"
            );
        }

        var trackLayer = cleanWaterProgressBar.lookup(".track");
        if (trackLayer != null) {
            trackLayer.setStyle(
                    "-fx-background-color: rgba(0, 0, 0, 0.65); " +
                            "-fx-background-radius: 15;"
            );
        }
    }

    private void applyGunPowderStyle(){
        gunPowderProgressBar.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-border-radius: 15; " +
                        "-fx-border-color: #56565d; " +
                        "-fx-border-width: 1.5;"
        );

        var barLayer = gunPowderProgressBar.lookup(".bar");
        if (barLayer != null) {
            barLayer.setStyle(

                    "-fx-background-color: linear-gradient(to right, #413939, #363434); " +
                            "-fx-background-radius: 15; " +

                            "-fx-effect: dropshadow(gaussian, #3f3838, 25, 0.6, 0, 0);"
            );
        }

        var trackLayer = gunPowderProgressBar.lookup(".track");
        if (trackLayer != null) {
            trackLayer.setStyle(
                    "-fx-background-color: rgba(0, 0, 0, 0.65); " +
                            "-fx-background-radius: 15;"
            );
        }
    }


    private void startGameLoop() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {

                setTradeButtonEnable();
                setAllianceButtonEnable();

                taskProcessor.process();

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

