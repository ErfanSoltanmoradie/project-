package service.filehandeling;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class SaveService {

    public static void save(GameState gameState, File fileName){

        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
            oos.writeObject(gameState);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
