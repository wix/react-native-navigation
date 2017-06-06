package com.reactnativenavigation.events;

public class JsDevReloadEvent implements Event {

    public static final String TYPE = "JsDevReloadEvent";

    @Override
    public String getType() {
        return TYPE;
    }
}
