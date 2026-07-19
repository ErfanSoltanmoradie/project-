package service.map;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class EliminatedController {

    @FXML private AnchorPane eliminatedPanel;
    @FXML private Label eliminatedReasonLabel;

    @FXML
    public void initialize() { hide(); }

    public void show(String reason) {
        eliminatedReasonLabel.setText(reason != null ? reason : "You have been eliminated from the game.");
        eliminatedPanel.setVisible(true);
        eliminatedPanel.setManaged(true);
    }

    public void hide() {
        eliminatedPanel.setVisible(false);
        eliminatedPanel.setManaged(false);
    }
}