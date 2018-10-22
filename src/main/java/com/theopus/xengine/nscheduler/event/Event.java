package com.theopus.xengine.nscheduler.event;

public class Event<D> {
    private static int count;

    private int id;
    private D d;


    public Event(D d) {
        this.id = count++;
        this.d = d;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", d=" + d +
                '}';
    }
}
