package service.map;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.army.ArmyCost;
import model.army.ArmyType;
import model.army.QueuedArmy;
import model.building.ArmyProducer;
import model.player.Player;
import service.army.ArmyManagement;
import service.army.TrainArmyResult;

import java.time.Duration;
import java.time.Instant;

public class ArmyProducerController {

    private Player player;
    private ArmyProducer armyProducer;
    private ArmyManagement armyManagement;
    private Timeline timeline;

    @FXML
    private Spinner<Integer> ragnarSpinner;

    @FXML
    private Spinner<Integer> rosooSpinner;

    @FXML
    private Spinner<Integer> lagertaSpinner;

    @FXML
    private Button trainButton;

    @FXML
    private Label ragnarCostLabel;

    @FXML
    private Label rosooCostLabel;

    @FXML
    private Label lagertaCostLabel;

    @FXML
    private Button upgradeButton;

    @FXML
    private Button closeButton;

    @FXML private AnchorPane armyProducerPanel;

    @FXML
    private VBox queueContainer;

    @FXML
    private AnchorPane queuePanel;



    @FXML
    public void initialize() {

        ragnarSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0,100,0));

        rosooSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0,100,0));

        lagertaSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0,100,0));

        timeline = new Timeline(
                new KeyFrame(
                        javafx.util.Duration.seconds(1),
                        e -> refresh()
                )
        );

        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    @FXML
    private void trainArmy() {

        int ragnar = ragnarSpinner.getValue();
        int rosoo = rosooSpinner.getValue();
        int lagerta = lagertaSpinner.getValue();

        if (ragnar > 0) {
            handleTrainResult(
                    armyManagement.trainArmy(ArmyType.RAGNAR, ragnar)
            );
        }

        if (rosoo > 0) {
            handleTrainResult(
                    armyManagement.trainArmy(ArmyType.ROSOO, rosoo)
            );
        }

        if (lagerta > 0) {
            handleTrainResult(
                    armyManagement.trainArmy(ArmyType.LAGERTA, lagerta)
            );
        }

        ragnarSpinner.getValueFactory().setValue(0);
        rosooSpinner.getValueFactory().setValue(0);
        lagertaSpinner.getValueFactory().setValue(0);

        refresh();
    }

    @FXML
    private void onCloseButtonClicked(){
        Stage stage =(Stage) this.closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onQueueClicked(){
        this.showQueuePanel();
    }

    private void showAlert(String title, String message) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public void setPlayer(Player player, ArmyProducer armyProducer/*, GameMapController gameMapController*/) {
        this.player = player;
        this.armyProducer = armyProducer;
        /*gameMapController.setArmyProducerController(this);*/
        this.armyManagement = new ArmyManagement(player);
        refresh();
        timeline.play();

    }

    public void stopRefresh(){
        if (timeline != null) {
            timeline.stop();
        }
    }

    public void refresh() {
        refreshQueueUI();


        Instant now = Instant.now();

        for (QueuedArmy army : armyManagement.getArmyQueue()) {

            long totalSeconds = Duration.between(now, army.finishTime()).toSeconds();

            if (totalSeconds < 0) {
                totalSeconds = 0;
            }

            long hours = totalSeconds / 3600;
            long minutes = (totalSeconds % 3600) / 60;
            long seconds = totalSeconds % 60;

            StringBuilder remainingTime = new StringBuilder();

            if (hours > 0) {
                remainingTime.append(hours).append(" h ");
            }

            if (minutes > 0 || hours > 0) {
                remainingTime.append(minutes).append(" min ");
            }

            remainingTime.append(seconds).append(" sec");

            String name = switch (army.armyType()) {
                case RAGNAR -> "Ragnar";
                case ROSOO -> "Rosoo";
                case LAGERTA -> "Lagerta";
            };

        }

        rosooSpinner.setDisable(!ArmyType.ROSOO.isUnlocked(armyProducer.getLevel()));
        lagertaSpinner.setDisable(!ArmyType.LAGERTA.isUnlocked(armyProducer.getLevel()));


    }

    private String getCostText(ArmyCost cost) {

        long seconds = cost.neededTime().toSeconds();

        return String.format(
                "%dWood | %dStone | %dIron | %dGunPowder | %ds",
                cost.wood(),
                cost.stone(),
                cost.iron(),
                cost.gunPowder(),
                seconds
        );
    }



    public void showArmyProducerPanel(){
        this.armyProducerPanel.setVisible(true);
        this.armyProducerPanel.setManaged(true);
    }

    public void hideArmyProducerPanel(){
        this.armyProducerPanel.setVisible(false);
        this.armyProducerPanel.setManaged(false);
    }

    private void handleTrainResult(TrainArmyResult result) {

        switch (result) {

            case SUCCESS -> {}

            case INVALID_COUNT ->
                    showAlert("Training", "Please select at least one soldier.");

            case NO_ARMY_PRODUCER ->
                    showAlert("Training", "Army Producer not found.");

            case NO_BARRACK ->
                    showAlert("Training", "Barrack not found.");

            case SOLDIER_LOCKED ->
                    showAlert("Training", "This soldier is locked.");

            case QUEUE_FULL ->
                    showAlert("Training", "Training queue is full.");

            case BARRACK_FULL ->
                    showAlert("Training", "Barrack capacity exceeded.");

            case NOT_ENOUGH_RESOURCES ->
                    showAlert("Training", "Not enough resources.");
        }
    }


    private void showQueuePanel(){
        this.queuePanel.setVisible(true);
        this.queuePanel.setManaged(true);
    }

    public void hideQueuePanel(){
        this.queuePanel.setVisible(false);
        this.queuePanel.setManaged(false);
    }

    public void refreshQueueUI() {
        queueContainer.getChildren().clear();

        Instant now = Instant.now();

        for (QueuedArmy army : armyManagement.getArmyQueue()) {

            long remainingSeconds = Duration.between(now, army.finishTime()).toSeconds();
            remainingSeconds = Math.max(0, remainingSeconds);

            long totalDuration = army.armyType().getTrainCost().neededTime().toSeconds();

            double progress = 1.0 - ((double) remainingSeconds / totalDuration);
            progress = Math.max(0.0, Math.min(1.0, progress));

            HBox soldierRow = new HBox(15);
            soldierRow.setAlignment(Pos.CENTER_LEFT);
            soldierRow.setStyle("-fx-background-color: #000428; " +
                    "-fx-padding: 10; " +
                    "-fx-background-radius: 10; " +
                    "-fx-border-color: #1c1c1c; " +
                    "-fx-border-width: 1;");

            String imagePath = switch (army.armyType()) {
                case RAGNAR -> "/images/soldiers/ragnar.png";
                case ROSOO -> "/images/soldiers/rosoo.png";
                case LAGERTA -> "/images/soldiers/lagerta.png";
            };

            ImageView imageView = new ImageView();
            try {
                imageView.setImage(new Image(getClass().getResourceAsStream(imagePath)));
            } catch (Exception e) {
                imageView.setImage(null);
            }
            imageView.setFitWidth(50);
            imageView.setFitHeight(50);
            imageView.setPreserveRatio(true);

            VBox infoVBox = new VBox(5);
            HBox.setHgrow(infoVBox, Priority.ALWAYS);

            Label nameLabel = new Label(army.armyType().toString());
            nameLabel.setStyle("-fx-text-fill: #ffb700; -fx-font-weight: bold; -fx-font-size: 14px;");

            StackPane progressStack = new StackPane();

            ProgressBar progressBar = new ProgressBar();
            progressBar.setMaxWidth(Double.MAX_VALUE);
            progressBar.setProgress(progress);
            progressBar.getStyleClass().add("neon-progress-bar");
            progressBar.setStyle("-fx-accent: #36ff00;"); // Fallback neon green color

            Label timeLabel = new Label(remainingSeconds + "s");
            timeLabel.setStyle("-fx-text-fill: #000000; -fx-font-weight: bold; -fx-font-size: 11px;");

            progressStack.getChildren().addAll(progressBar, timeLabel);
            infoVBox.getChildren().addAll(nameLabel, progressStack);

            soldierRow.getChildren().addAll(imageView, infoVBox);

            queueContainer.getChildren().add(soldierRow);
        }
    }
}