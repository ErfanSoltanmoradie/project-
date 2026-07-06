package service.filehandeling;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class LoadService {


    public static GameState load(File fileName, GameState gameState) {

        if (!fileName.exists() || fileName.length() == 0) {
            return gameState;
        }

        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
            return (GameState) ois.readObject();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
