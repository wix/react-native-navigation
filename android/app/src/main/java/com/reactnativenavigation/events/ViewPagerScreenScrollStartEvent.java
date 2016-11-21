package com.reactnativenavigation.events;

public class ViewPagerScreenScrollStartEvent implements Event {
    public static final String TYPE = "ViewPagerScreenScrollStartEvent";
    @Override
    public String getType() {
        return TYPE;
    }
}
