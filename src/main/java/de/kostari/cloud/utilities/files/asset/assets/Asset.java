package de.kostari.cloud.utilities.files.asset.assets;

public class Asset {

    private String name;
    private String path;

    private int id;

    public Asset(String path, String name, int id) {
        this.path = path;
        this.name = name;
        this.id = id;
    }

    public Asset(String path, int id) {
        this.path = path;
        this.id = id;
        this.name = "";
    }

    public Asset(String path, String name) {
        this.path = path;
        this.id = -1;
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
