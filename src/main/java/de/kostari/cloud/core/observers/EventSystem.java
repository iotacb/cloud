package de.kostari.cloud.core.observers;

import java.util.ArrayList;
import java.util.List;

import de.kostari.cloud.core.objects.GameObject;
import de.kostari.cloud.core.observers.events.Event;

public class EventSystem {

    private static List<Observer> observers = new ArrayList<>();

    public static void addObserver(Observer observer) {
        observers.add(observer);
    }

    public static void notify(GameObject gameObject, Event event) {
        observers.forEach(observer -> {
            observer.onNotify(gameObject, event);
        });
    }

}
