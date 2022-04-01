package de.kostari.cloud.core.observers;

import de.kostari.cloud.core.objects.GameObject;
import de.kostari.cloud.core.observers.events.Event;

public interface Observer {

    void onNotify(GameObject gameObject, Event event);

}
