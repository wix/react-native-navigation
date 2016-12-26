package com.reactnativenavigation.events;

import com.reactnativenavigation.params.FabParams;

public class FabSetEvent implements Event {
    public static final String TYPE = "FabSetEvent";

    public FabParams fabParams;

    public FabSetEvent(FabParams fabParams) {
        this.fabParams = fabParams;
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
