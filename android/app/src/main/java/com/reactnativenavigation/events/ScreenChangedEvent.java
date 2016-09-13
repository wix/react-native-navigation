package com.reactnativenavigation.events;

import com.reactnativenavigation.params.BaseScreenParams;
import com.reactnativenavigation.params.FabParams;
import com.reactnativenavigation.params.PageParams;

public class ScreenChangedEvent implements Event {
    public static final String TYPE = "ScreenChangedEvent";
    public FabParams fabParams;

    public ScreenChangedEvent(BaseScreenParams screenParams) {
        this.fabParams = screenParams.fabParams;
    }

    public ScreenChangedEvent(PageParams topTabParams) {
        this.fabParams = topTabParams.fabParams;
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
