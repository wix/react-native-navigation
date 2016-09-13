package com.reactnativenavigation.events;

public class ModalDismissedEvent implements Event {
    public static final String TYPE = "ModalDismissedEvent";

    @Override
    public String getType() {
        return TYPE;
    }
}
