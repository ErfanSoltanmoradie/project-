package service.map;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.army.LinkedList;
import model.battle.BattleArmy;
import model.battle.BattleHistory;
import model.battle.BattleStatus;
import model.battle.BattleWinner;
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

import java.time.Duration;
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

    @FXML private Button leaveTradePanel;

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

        row.setStyle("-fx-padding: 12; " +
                "-fx-background-color: #2b2b2b; " +
                "-fx-background-radius: 8; " +
                "-fx-border-color: #444444; " +
                "-fx-border-width: 1; " +
                "-fx-alignment: CENTER_LEFT;");

        VBox senderContainer = new VBox();
        senderContainer.setSpacing(4);


        String senderName = offer.getSenderVillage().getUserName();
        Label senderLabel = new Label("SENDER PLAYER [ " + senderName + " ]");
        senderLabel.setStyle("-fx-text-fill: #ff9800; -fx-font-size: 13px; -fx-font-weight: bold;");

        Label timeLabel = new Label("TRANSFER TIME: " + offer.getTradeTime() + " seconds");
        timeLabel.setStyle("-fx-text-fill: #888888; -fx-font-size: 11px;");

        senderContainer.getChildren().addAll(senderLabel, timeLabel);


        VBox resourcesContainer = new VBox();
        resourcesContainer.setSpacing(4);


        String offeredStr = String.valueOf(offer.getOfferedResources().get(ResourcesType.WOOD));
        String requestedStr = String.valueOf(offer.getRequestedResources().get(ResourcesType.IRON));

        Label receiveLabel = new Label("RECEIVE: " + offeredStr);
        receiveLabel.setStyle("-fx-text-fill: #4CAF50; -fx-font-size: 11px;");

        Label payLabel = new Label("SEND: " + requestedStr);
        payLabel.setStyle("-fx-text-fill: #f44336; -fx-font-size: 11px;");

        resourcesContainer.getChildren().addAll(receiveLabel, payLabel);


        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);


        HBox actionButtons = new HBox(8);
        actionButtons.setStyle("-fx-alignment: CENTER_RIGHT;");


        Button acceptBtn = new Button("ACCEPT");
        acceptBtn.setStyle("-fx-background-color: #4CAF50; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 5; " +
                "-fx-cursor: hand;");
        acceptBtn.setOnAction(e -> {

            System.out.println("ACCEPTED");
            tradeService.acceptOffer(offer);
        });


        Button rejectBtn = new Button("REJECT");
        rejectBtn.setStyle("-fx-background-color: #f44336; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 5; " +
                "-fx-cursor: hand;");
        rejectBtn.setOnAction(e -> {

            tradeService.rejectOffer(offer);
            System.out.println("REJECTED");
        });

        actionButtons.getChildren().addAll(acceptBtn, rejectBtn);


        row.getChildren().addAll(senderContainer, resourcesContainer, spacer, actionButtons);


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

       offeredResources.put(ResourcesType.WOOD, Integer.parseInt(this.sendWoodTextField.getText()));
       requestedResources.put((ResourcesType.IRON), Integer.parseInt(this.receiveIronTextField.getText()));


        TradeService tradeService = new TradeService();
        tradeService.sendRequest(this.player, this.getReceiverTradeRequest(), offeredResources, requestedResources);


        this.hideMakeATradePanel();
    }

    @FXML
    private void onReceivedTradeRequests(){
        this.hideTradePanel();
        this.showReceivedTradeRequestsPanel();
        this.showTradeRequestsOnPanel(this.player);
    }


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
    private void onLeaveTradeButtonClicked(){
        this.hideTradePanel();
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

