package de.kostari.cloud.core.observers.events;

public class Event {

    public EventType type;

    public Event() {
        this.type = EventType.USER_EVENT;
    }

    public Event(EventType type) {
        this.type = type;
    }

}
