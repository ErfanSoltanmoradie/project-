package service.filehandeling;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class SaveService {

    public static void save(Object object, File fileName) {

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {

            oos.writeObject(object);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
