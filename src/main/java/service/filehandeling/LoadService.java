package service.filehandeling;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class LoadService {
    public static GameState load(File fileName) {

        try (ObjectInputStream ois =
                     new ObjectInputStream(
                             new FileInputStream(fileName))) {

            GameState gameState =
                    (GameState) ois.readObject();

            //System.out.println(gameState.toString());

            return gameState;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
