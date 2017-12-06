package com.reactnativenavigation.events;

import com.reactnativenavigation.params.StyleParams;

/**
 * Created by DanielL on 05.12.2017.
 */

public class BottomTabsStyleChangedEvent implements Event {
    public static final String TYPE = "BottomTabsStyleChangedEvent";

    public StyleParams styleParams;

    public BottomTabsStyleChangedEvent(StyleParams styleParams) {
        this.styleParams = styleParams;
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
