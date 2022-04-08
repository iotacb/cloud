package de.kostari.cloud.utilities.render;

import java.util.ArrayList;
import java.util.List;

public class Renderer {

    private static final int MAX_BATCH_SIZE = 1000;
    public static List<RenderBatch> batches = new ArrayList<>();

    public static void add(RenderObject object) {
        boolean added = false;
        for (RenderBatch batch : batches) {
            if (batch.hasRoom()) {
                batch.add(object);
                added = true;
                break;
            }
        }

        if (!added) {
            RenderBatch batch = new RenderBatch(MAX_BATCH_SIZE);
            batch.add(object);
            batches.add(batch);
        }
    }

    public static void render() {
        for (RenderBatch batch : batches) {
            batch.render();
        }
    }

    public static void clearBatches() {
        for (RenderBatch batch : batches) {
            batch.clearBatch();
        }
    }

}
