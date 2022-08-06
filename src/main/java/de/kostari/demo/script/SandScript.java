package de.kostari.demo.script;

import java.util.ArrayList;
import java.util.List;

import de.kostari.cloud.utilities.files.FileReader;

public class SandScript {

    public static void loadScript(String path) {
        List<String> lines = FileReader.readFile(path);
        readBlock(lines);
    }

    private static void readBlock(List<String> lines) {
        List<String[]> blocks = new ArrayList<>();
        List<String> block = new ArrayList<>();
        boolean isBlock = false;
        for (String line : lines) {
            if (line.startsWith("{")) {
                isBlock = true;
                block.add(line);
            } else if (line.startsWith("}")) {
                block.add(line);
                isBlock = false;
                blocks.add(block.toArray(new String[block.size()]));
                block.clear();
            } else {
                if (isBlock) {
                    block.add(line);
                }
            }
        }

        for (String[] blockLines : blocks) {
            for (String line : blockLines) {
                System.out.println(line);
            }
        }
    }

}
