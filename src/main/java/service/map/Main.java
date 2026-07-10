package service.map;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.player.Player;
import model.player.PlayerFactory;
import model.repository.PlayerRepository;
import model.repository.UserRepository;
import model.resources.ResourcesType;
import model.user.AuthController;
import model.user.AuthService;
import model.user.Start;
import model.user.User;
import model.village.Village;
import model.world.Coordinate;
import model.world.WorldMap;
import service.filehandeling.GameInitializer;
import service.resource.ResourcesManagement;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class Main extends Application {

    private Start start = new Start();

    public  Start getStart() {
        return start;
    }

    public void setStart(Start start) {
        this.start = start;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        FXMLLoader authLoader = new FXMLLoader(getClass().getResource("/com/example/project/auth.fxml"));
        Parent authRoot = null;
        try {
            authRoot = authLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        AuthController authController = authLoader.getController();
        authController.setPlayerRepository(start.getPlayerRepository());
        authController.setAuthService(start.getAuthService());


        Scene scene = new Scene(authRoot);
        primaryStage.setScene(scene);

        primaryStage.setResizable(false);
        primaryStage.show();

        primaryStage.setOnCloseRequest(WindowEvent ->{
            start.saveAllData();
        });
    }



    public static void main(String[] args) {
        launch(args);
    }
}