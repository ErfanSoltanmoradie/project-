package service.map;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.army.ArmyCost;
import model.army.ArmyType;
import model.army.QueuedArmy;
import model.building.ArmyProducer;
import model.building.Barrack;
import model.building.BuildingStatus;
import model.player.Player;
import service.army.ArmyManagement;
import service.army.TrainArmyResult;
import service.buildings.BuildingsManagement;

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
    private Label levelLabel;

    @FXML
    private Label capacityLabel;

    @FXML
    private Label queueLabel;

    @FXML
    private Label ragnarCostLabel;

    @FXML
    private Label rosooCostLabel;

    @FXML
    private Label lagertaCostLabel;

    @FXML
    private ListView<String> queueListView;

    @FXML
    private Button upgradeButton;

    public void setPlayer(Player player,ArmyProducer armyProducer) {
        this.player = player;
        this.armyProducer = armyProducer;
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

        levelLabel.setText("Level : " + armyProducer.getLevel());

        capacityLabel.setText("Capacity : " + armyProducer.getMaxTrainingCount());

        queueLabel.setText("Queue : " + armyManagement.getArmyQueue().size());

        queueListView.getItems().clear();

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

            queueListView.getItems().add(
                    "⚔ " + name + "    ⏳ " + remainingTime
            );
        }

        rosooSpinner.setDisable(!ArmyType.ROSOO.isUnlocked(armyProducer.getLevel()));
        lagertaSpinner.setDisable(!ArmyType.LAGERTA.isUnlocked(armyProducer.getLevel()));

        //اگر ساختمان در حال ارتقا هست یا به آخرین لول رسیده ارتقا دادن غیر فعال شود
        upgradeButton.setDisable(armyProducer.getLevel() >= 5 || armyProducer.getBuildingStatus() == BuildingStatus.UPGRADING);

        ragnarCostLabel.setText(getCostText(ArmyType.RAGNAR.getTrainCost()));
        rosooCostLabel.setText(getCostText(ArmyType.ROSOO.getTrainCost()));
        lagertaCostLabel.setText(getCostText(ArmyType.LAGERTA.getTrainCost()));
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
    private void upgrade() {

        BuildingsManagement buildingsManagement = new BuildingsManagement(player.getVillage());

        buildingsManagement.upgrade(armyProducer);

        refresh();
    }
    //DialogUtils
    private void showAlert(String title, String message) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
}