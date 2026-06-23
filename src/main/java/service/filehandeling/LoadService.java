package service.filehandeling;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class LoadService {

    public static GameState load(File fileName) {

        if (!fileName.exists()) {
            return new GameState();
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
