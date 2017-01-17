package com.reactnativenavigation.events;

public class OrientationChangedEvent implements Event {
    public static final String TYPE = "OrientationChanged";
    @Override
    public String getType() {
        return TYPE;
    }
}
