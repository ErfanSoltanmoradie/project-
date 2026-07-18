package service.map;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class WinnerController {

    @FXML
    private AnchorPane winnerPanel;

    @FXML
    private Label winnerLabel;

    @FXML
    public void initialize() {
        hide();
    }

    public void show(String winnerName) {
        if (winnerName == null) {
            winnerLabel.setText("All lands have been destroyed. Humanity has gone extinct.");
        } else {
            winnerLabel.setText("Winner: " + winnerName);
        }
        winnerPanel.setVisible(true);
        winnerPanel.setManaged(true);
    }

    public void hide() {
        winnerPanel.setVisible(false);
        winnerPanel.setManaged(false);
    }
}