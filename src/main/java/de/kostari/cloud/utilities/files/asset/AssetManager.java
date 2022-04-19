package de.kostari.cloud.utilities.files.asset;

import java.util.ArrayList;
import java.util.List;

import de.kostari.cloud.utilities.files.asset.assets.Asset;
import de.kostari.cloud.utilities.files.asset.assets.Audio;

public class AssetManager {

    private List<Asset> assets;

    public AssetManager() {
        this.assets = new ArrayList<>();
    }

    public void addAsset(Asset asset) {
        this.assets.add(asset);
    }

    public Asset getAsset(int id) {
        for (Asset asset : this.assets) {
            if (asset.getId() == id) {
                return asset;
            }
        }
        return null;
    }

    public Asset getAsset(String name) {
        for (Asset asset : this.assets) {
            if (asset.getName() == name) {
                return asset;
            }
        }
        return null;
    }

    public Audio getAudio(int id) {
        return (Audio) getAsset(id);
    }

    public Audio getAudio(String name) {
        return (Audio) getAsset(name);
    }

    public List<Asset> getAssets() {
        return assets;
    }

}
