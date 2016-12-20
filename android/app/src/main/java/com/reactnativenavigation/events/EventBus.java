package com.reactnativenavigation.events;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public enum EventBus {
    instance;

    private final List<WeakReference<Subscriber>> subscribers = new ArrayList<>();

    public void register(Subscriber subscriber) {
        if (isSubscribed(subscriber)) return;
        subscribers.add(new WeakReference<>(subscriber));
    }

    public void unregister(Subscriber subscriber) {
        ListIterator<WeakReference<Subscriber>> iterator = subscribers.listIterator();
        while (iterator.hasNext()) {
            WeakReference<Subscriber> ref = iterator.next();
            Subscriber registered = ref.get();
            if (registered != null && registered == subscriber) {
                iterator.remove();
            }
        }
    }

    public void post(Event event) {
        ListIterator<WeakReference<Subscriber>> iterator = subscribers.listIterator();
        while (iterator.hasNext()) {
            WeakReference<Subscriber> ref = iterator.next();
            Subscriber registered = ref.get();
            if (registered != null) {
                registered.onEvent(event);
            }
        }
    }

    public boolean isSubscribed(Subscriber subscriber) {
        for (WeakReference<Subscriber> ref : subscribers) {
            Subscriber registered = ref.get();
            if (registered != null && registered.equals(subscriber)) {
                return true;
            }
        }
        return false;
    }
}
