package com.reactnativenavigation.events;

import com.reactnativenavigation.params.BaseScreenParams;
import com.reactnativenavigation.params.FabParams;

public class ScreenChangedEvent implements Event {
    public static final String TYPE = "ScreenChangedEvent";
    public FabParams fabParams;

    public ScreenChangedEvent(BaseScreenParams screenParams) {
        this.fabParams = screenParams.getFab();
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
