package service.map;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.army.ArmyStorage;
import model.army.ArmyType;
import model.building.ArmyProducer;
import model.building.Barrack;
import model.building.BuildingStatus;
import model.player.Player;
import service.army.ArmyManagement;
import service.buildings.BuildingsManagement;

public class BarrackController {


    @FXML
    private Label capacityLabel;

    @FXML
    private Label queueLabel;


    @FXML
    private Label ragnarCountLabel;

    @FXML
    private Label rosooCountLabel;

    @FXML
    private Label lagertaCountLabel;

    @FXML
    private Label attackLabel;

    @FXML
    private Label defenseLabel;

    @FXML
    private Button closeButton;

    private Player player;
    private Barrack barrack;
    private ArmyManagement armyManagement;
    private Timeline timeline;

    @FXML
    private void onCloseClicked(){
        Stage stage =(Stage) this.closeButton.getScene().getWindow();
        stage.close();
    }

    public void setPlayer(Player player, Barrack barrack) {
        this.player = player;
        this.barrack = barrack;
        this.armyManagement = new ArmyManagement(player);
        refresh();
        timeline.play();

    }

    public void stopRefresh(){
        if (timeline != null) {
            timeline.stop();
        }
    }

    public void initialize(){

        timeline = new Timeline(
                new KeyFrame(
                        javafx.util.Duration.seconds(1),
                        e -> refresh()
                )
        );

        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    public void refresh() {

        ArmyStorage storage = armyManagement.getArmyStorage();



        int totalArmy = storage.getRagnarCount()
                + storage.getRussoCount()
                + storage.getLagertaCount();

        capacityLabel.setText("Capacity : " + totalArmy + " / " + barrack.getCapacity());

        queueLabel.setText("Queue : " +armyManagement.getArmyQueue().size());


        ragnarCountLabel.setText("Ragnar : " + storage.getRagnarCount());

        rosooCountLabel.setText("Rosoo : " + storage.getRussoCount());

        lagertaCountLabel.setText("Lagerta : " + storage.getLagertaCount());

        int attack =
                storage.getRagnarCount() * ArmyType.RAGNAR.getAttack()
                        + storage.getRussoCount() * ArmyType.ROSOO.getAttack()
                        + storage.getLagertaCount() * ArmyType.LAGERTA.getAttack();

        int defense =
                storage.getRagnarCount() * ArmyType.RAGNAR.getDefense()
                        + storage.getRussoCount() * ArmyType.ROSOO.getDefense()
                        + storage.getLagertaCount() * ArmyType.LAGERTA.getDefense();

        attackLabel.setText("Attack : " + attack);

        defenseLabel.setText("Defense : " + defense);


    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Barrack");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
