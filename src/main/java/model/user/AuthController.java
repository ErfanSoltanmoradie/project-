package model.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.building.Building;
import model.building.BuildingType;
import model.building.MinerBuilding;
import model.player.Player;
import model.repository.PlayerRepository;
import model.resources.ResourcesType;
import service.map.VillageController;

import java.io.IOException;

public class AuthController {

    private AuthService authService;
    private PlayerRepository playerRepository;
    private Start start;

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML private StackPane authPanel;
    @FXML private Button loginButton;
    @FXML private Button signupButton;
    @FXML private TextField usernameTextField;
    @FXML private TextField passwordTextField;
    @FXML private TextField usernameTextField1;
    @FXML private TextField passwordTextField1;
    @FXML private AnchorPane signupPanel;
    @FXML private AnchorPane loginPanel;

    @FXML
    public String getUsername1() { return this.usernameTextField1.getText(); }
    public String getPassword1() { return this.passwordTextField1.getText(); }
    @FXML
    public String getUsername() { return this.usernameTextField.getText(); }
    public String getPassword() { return this.passwordTextField.getText(); }

    @FXML private void onFirstSignupClicked() { this.showSignupPanel(); }
    @FXML private void onFirstLoginClicked() { this.showLoginPanel(); }

    @FXML
    private void onExitClicked() {
        if (this.start != null) {
            this.start.saveAllData();
        }
        System.exit(0);
    }

    @FXML
    private void handleMousePressed(MouseEvent mouseEvent) {
        this.xOffset = mouseEvent.getSceneX();
        this.yOffset = mouseEvent.getSceneY();
    }

    @FXML
    private void handleMouseDragged(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }

    @FXML
    public void onLoginClicked() {
        AuthResult authResult = this.authService.login(this.getUsername1(), this.getPassword1());
        System.out.println(authResult.getAuthStatus().toString());

        if (authResult.getAuthStatus() == AuthStatus.SUCCESS && !authResult.getPlayer().isOnlineStatus()) {
            this.usernameTextField1.clear();
            this.passwordTextField1.clear();
            authResult.getPlayer().setOnlineStatus(true);
            this.addProducedResources(authResult.getPlayer());
            showVillage(authResult.getPlayer());
            hideLoginPanel();
        }
    }

    @FXML
    public void onSignupClicked() {
        AuthResult authResult = this.authService.register(this.getUsername(), this.getPassword());
        System.out.println(authResult.getAuthStatus().toString());

        if (authResult.getAuthStatus() == AuthStatus.SUCCESS
                && authResult.getPlayer().getUsername().equalsIgnoreCase(this.getUsername())) {
            this.usernameTextField.clear();
            this.passwordTextField.clear();
            authResult.getPlayer().setOnlineStatus(true);
            showVillage(authResult.getPlayer());
            hideSignupPanel();
        }
    }

    public void showVillage(Player player) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/village.fxml"));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException("error in loading village.fxml: ", e);
        }

        VillageController villageController = loader.getController();

        villageController.setGameState(start.getGameState());
        villageController.setPlayerRepository(this.playerRepository);
        villageController.setPlayer(player);

        Stage stage = new Stage();
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.setResizable(true);

        stage.setOnCloseRequest(windowEvent -> {
            player.setOnlineStatus(false);
            player.setLastSeen(System.currentTimeMillis());
            if (villageController.getGameLoop() != null) {
                villageController.stopGameLoop(villageController.getGameLoop());
            }
        });

        stage.show();
    }

    private void addProducedResources(Player player) {
        player.getVillage().getLock().writeLock().lock();
        try {
            long passedTime = System.currentTimeMillis() - player.getLastSeen();
            passedTime /= 1000;

            player.getVillage().getResourcesManagement().addResource(this.callProducedIron(player, passedTime), ResourcesType.IRON);
            player.getVillage().getResourcesManagement().addResource(this.callProducedCleanSoil(player, passedTime), ResourcesType.CLEAN_SOIL);
            player.getVillage().getResourcesManagement().addResource(this.callProducedDirtySoil(player, passedTime), ResourcesType.DIRTY_SOIL);
            player.getVillage().getResourcesManagement().addResource(this.callProducedCleanWater(player, passedTime), ResourcesType.CLEAN_WATER);
            player.getVillage().getResourcesManagement().addResource(this.callProducedStone(player, passedTime), ResourcesType.STONE);
            player.getVillage().getResourcesManagement().addResource(this.callProducedGunPowder(player, passedTime), ResourcesType.GUN_POWDER);
            player.getVillage().getResourcesManagement().addResource(this.callProducedWood(player, passedTime), ResourcesType.WOOD);
            player.getVillage().getResourcesManagement().addResource(this.callProducedDirtyWater(player, passedTime), ResourcesType.DIRTY_WATER);
        } finally {
            player.getVillage().getLock().writeLock().unlock();
        }
    }

    private int callProducedWood(Player player, long passedTime) {
        int amount = 0;
        for (Building building : player.getVillage().getBuildings().values()) {
            if (building instanceof MinerBuilding && building.getType() == BuildingType.WOOD_MINE) {
                amount += (int) (passedTime * ((MinerBuilding) building).getProduction());
            }
        }
        return amount;
    }

    private int callProducedIron(Player player, long passedTime) {
        int amount = 0;
        for (Building building : player.getVillage().getBuildings().values()) {
            if (building.getType() == BuildingType.IRON_MINE && building instanceof MinerBuilding) {
                amount += (int) (passedTime * ((MinerBuilding) building).getProduction());
            }
        }
        return amount;
    }

    private int callProducedDirtyWater(Player player, long passedTime) {
        int amount = 0;
        for (Building building : player.getVillage().getBuildings().values()) {
            if (building.getType() == BuildingType.DIRTY_WATER_MINE && building instanceof MinerBuilding) {
                amount += (int) (passedTime * ((MinerBuilding) building).getProduction());
            }
        }
        return amount;
    }

    private int callProducedCleanWater(Player player, long passedTime) {
        int amount = 0;
        for (Building building : player.getVillage().getBuildings().values()) {
            if (building.getType() == BuildingType.WATER_PURIFIER && building instanceof MinerBuilding) {
                amount += (int) (passedTime * ((MinerBuilding) building).getProduction());
            }
        }
        return amount;
    }

    private int callProducedCleanSoil(Player player, long passedTime) {
        int amount = 0;
        for (Building building : player.getVillage().getBuildings().values()) {
            if (building.getType() == BuildingType.SOIL_PURIFIER && building instanceof MinerBuilding) {
                amount += (int) (passedTime * ((MinerBuilding) building).getProduction());
            }
        }
        return amount;
    }

    private int callProducedDirtySoil(Player player, long passedTime) {
        int amount = 0;
        for (Building building : player.getVillage().getBuildings().values()) {
            if (building.getType() == BuildingType.DIRTY_SOIL_MINE && building instanceof MinerBuilding) {
                amount += (int) (passedTime * ((MinerBuilding) building).getProduction());
            }
        }
        return amount;
    }

    private int callProducedGunPowder(Player player, long passedTime) {
        int amount = 0;
        for (Building building : player.getVillage().getBuildings().values()) {
            if (building.getType() == BuildingType.GUNPOWDER_MINE && building instanceof MinerBuilding) {
                amount += (int) (passedTime * ((MinerBuilding) building).getProduction());
            }
        }
        return amount;
    }

    private int callProducedStone(Player player, long passedTime) {
        int amount = 0;
        for (Building building : player.getVillage().getBuildings().values()) {
            if (building.getType() == BuildingType.STONE_MINE && building instanceof MinerBuilding) {
                amount += (int) (passedTime * ((MinerBuilding) building).getProduction());
            }
        }
        return amount;
    }

    private void hideLoginPanel() { this.loginPanel.setVisible(false); this.loginPanel.setManaged(false); }
    private void hideSignupPanel() { this.signupPanel.setVisible(false); this.signupPanel.setManaged(false); }
    private void showSignupPanel() { this.signupPanel.setVisible(true); this.signupPanel.setManaged(true); }
    private void showLoginPanel() { this.loginPanel.setVisible(true); this.loginPanel.setManaged(true); }

    public PlayerRepository getPlayerRepository() { return playerRepository; }
    public void setPlayerRepository(PlayerRepository playerRepository) { this.playerRepository = playerRepository; }
    public AuthService getAuthService() { return authService; }
    public void setAuthService(AuthService authService) { this.authService = authService; }
    public Start getStart() { return start; }
    public void setStart(Start start) { this.start = start; }
}