package model.user;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.building.Building;
import model.building.BuildingType;
import model.building.MinerBuilding;
import model.player.Player;
import model.resources.ResourcesType;
import service.map.VillageController;

import java.io.IOException;


public class AuthController {

    private AuthService authService;

    @FXML
    private StackPane authPanel;

    @FXML
    private Button loginButton;

    @FXML
    private Button signupButton;

    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    public String getUsername(){
       return this.usernameTextField.getText();
    }

    public String getPassword(){
       return this.passwordTextField.getText();
    }

    @FXML
    public void onLoginClicked(){
        AuthResult authResult = this.authService.login(this.getUsername(), this.getPassword());
        System.out.println(authResult.getAuthStatus().toString());

        if(authResult.getAuthStatus() == AuthStatus.SUCCESS && authResult.getPlayer().isOnlineStatus() == false){
            this.usernameTextField.clear();
            this.passwordTextField.clear();
            authResult.getPlayer().setOnlineStatus(true);
            this.addProducedResources(authResult.getPlayer());
            showVillage(authResult.getPlayer());
        }
    }

    @FXML
    public void onSignupClicked(){
        AuthResult authResult = this.authService.register(this.getUsername(), this.getPassword());
        System.out.println(authResult.getAuthStatus().toString());

        if(authResult.getPlayer().getUsername().equalsIgnoreCase(this.getUsername())){
            this.usernameTextField.clear();
            this.passwordTextField.clear();
            showVillage(authResult.getPlayer());
        }
    }

    public void showVillage(Player player)  {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/village.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        VillageController villageController = loader.getController();
        villageController.setPlayer(player);

        Stage stage = new Stage();

        Scene scene = new Scene(root, 1024, 768);

        stage.setScene(scene);
        stage.setResizable(true);

        stage.setOnCloseRequest(WindowEvent -> {
            player.setOnlineStatus(false);
            player.setLastSeen(System.currentTimeMillis());
            villageController.stopGameLoop(villageController.getGameLoop());
        });

        stage.show();
    }

    private void addProducedResources(Player player){
            player.getVillage().getLock().writeLock().lock();
            try {
                long passedTime = System.currentTimeMillis() - player.getLastSeen();
                passedTime /= 1000;

                int producedIron = this.callProducedIron(player, passedTime);
                int producedWood = this.callProducedWood(player, passedTime);
                int producedDirtyWater = this.callProducedDirtyWater(player, passedTime);
                int producedCleanWater = this.callProducedCleanWater(player, passedTime);
                int producedStone = this.callProducedStone(player, passedTime);
                int producedGunPowder = this.callProducedGunPowder(player, passedTime);
                int producedCleanSoil = this.callProducedCleanSoil(player, passedTime);
                int producedDirtySoil = this.callProducedDirtySoil(player, passedTime);

                player.getVillage().getResourcesManagement().addResource(producedIron, ResourcesType.IRON);
                player.getVillage().getResourcesManagement().addResource(producedCleanSoil, ResourcesType.CLEAN_SOIL);
                player.getVillage().getResourcesManagement().addResource(producedDirtySoil, ResourcesType.DIRTY_SOIL);
                player.getVillage().getResourcesManagement().addResource(producedCleanWater, ResourcesType.CLEAN_WATER);
                player.getVillage().getResourcesManagement().addResource(producedStone, ResourcesType.STONE);
                player.getVillage().getResourcesManagement().addResource(producedGunPowder, ResourcesType.GUN_POWDER);
                player.getVillage().getResourcesManagement().addResource(producedWood, ResourcesType.WOOD);
                player.getVillage().getResourcesManagement().addResource(producedDirtyWater, ResourcesType.DIRTY_WATER);

            }finally {
                player.getVillage().getLock().writeLock().unlock();
            }
    }

    private int callProducedWood(Player player, long passedTime){
        int amount = 0;
        for (Building building : player.getVillage().getBuildings().values()){
            if(building instanceof MinerBuilding){
                if(building.getType() == BuildingType.WOOD_MINE){
                    amount += (int) (passedTime * ((MinerBuilding) building).getProduction());
                }
            }
        }
        return amount;
    }

    private int callProducedIron(Player player, long passedTime){
        int amount = 0;
        for (Building building : player.getVillage().getBuildings().values()){
            if(building.getType() == BuildingType.IRON_MINE){
                amount += (int) (passedTime * ((MinerBuilding) building).getProduction());
            }
        }
        return amount;
    }

    private int callProducedDirtyWater(Player player, long passedTime){
        int amount = 0;
        for (Building building : player.getVillage().getBuildings().values()){

            if(building.getType() == BuildingType.DIRTY_WATER_MINE){
                amount += (int) (passedTime * ((MinerBuilding) building).getProduction());
            }
        }
        return amount;
    }

    private int callProducedCleanWater(Player player, long passedTime){
        int amount = 0;
        for (Building building : player.getVillage().getBuildings().values()){

            if(building.getType() == BuildingType.WATER_PURIFIER){
                amount += (int) (passedTime * ((MinerBuilding) building).getProduction());
            }
        }
        return amount;
    }

    private int callProducedCleanSoil(Player player, long passedTime){
        int amount = 0;
        for (Building building : player.getVillage().getBuildings().values()){

            if(building.getType() == BuildingType.SOIL_PURIFIER){
                amount += (int) (passedTime * ((MinerBuilding) building).getProduction());
            }
        }
        return amount;
    }

    private int callProducedDirtySoil(Player player, long passedTime){
        int amount = 0;
        for (Building building : player.getVillage().getBuildings().values()){

            if(building.getType() == BuildingType.DIRTY_SOIL_MINE){
                amount += (int) (passedTime * ((MinerBuilding) building).getProduction());
            }
        }
        return amount;
    }

    private int callProducedGunPowder(Player player, long passedTime){
        int amount = 0;
        for (Building building : player.getVillage().getBuildings().values()){

            if(building.getType() == BuildingType.GUNPOWDER_MINE){
                amount += (int) (passedTime * ((MinerBuilding) building).getProduction());
            }
        }
        return amount;
    }

    private int callProducedStone(Player player, long passedTime){
        int amount = 0;
        for (Building building : player.getVillage().getBuildings().values()){

            if(building.getType() == BuildingType.STONE_MINE){
                amount += (int) (passedTime * ((MinerBuilding) building).getProduction());
            }
        }
        return amount;
    }


    public AuthService getAuthService() {
        return authService;
    }

    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }
}
