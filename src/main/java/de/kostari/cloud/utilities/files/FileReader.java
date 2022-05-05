package de.kostari.cloud.utilities.files;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class FileReader {

    public static List<String> readFile(String path) {
        try {
            return Files.readAllLines(new File(path).toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
