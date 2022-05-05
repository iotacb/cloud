package de.kostari.cloud.utilities.files.csv;

import java.util.ArrayList;
import java.util.List;

import de.kostari.cloud.utilities.files.FileReader;

public class CSVFile {

    private String path;
    private String delimiter = ",";

    private List<String[]> data = new ArrayList<>();

    public CSVFile(String path) {
        this.path = path;
        readCSV(path, delimiter);
    }

    public CSVFile(String path, String delimiter) {
        this.path = path;
        this.delimiter = delimiter;
        readCSV(path, delimiter);
    }

    private void readCSV(String path, String delimiter) {
        List<String> lines = FileReader.readFile(path);
        List<String[]> data = new ArrayList<>();
        lines.forEach(line -> {
            data.add(line.split(delimiter));
        });
        this.data = data;
    }

    public String[] getRow(int row) {
        return data.get(row);
    }

    public String[] getColumn(int column) {
        List<String> columnData = new ArrayList<>();
        data.forEach(row -> {
            columnData.add(row[column]);
        });
        return columnData.toArray(new String[columnData.size()]);
    }

    public String getPath() {
        return path;
    }

    public List<String[]> getData() {
        return data;
    }

    public String getDelimiter() {
        return delimiter;
    }

}
