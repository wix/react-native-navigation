package com.reactnativenavigation.params;

import android.os.Bundle;

public class NavigationParams {
    public static final NavigationParams EMPTY = new NavigationParams(Bundle.EMPTY);

    private static final String SCREEN_INSTANCE_ID = "screenInstanceID";
    private static final String NAVIGATOR_ID = "navigatorID";
    private static final String NAVIGATOR_EVENT_ID = "navigatorEventID";

    public String screenInstanceId;
    public String navigatorId;
    public String navigatorEventId;

    public NavigationParams(Bundle bundle) {
        screenInstanceId = bundle.getString(SCREEN_INSTANCE_ID);
        navigatorId = bundle.getString(NAVIGATOR_ID);
        navigatorEventId = bundle.getString(NAVIGATOR_EVENT_ID);
    }

    public Bundle toBundle() {
        Bundle b = new Bundle();
        b.putString(SCREEN_INSTANCE_ID, screenInstanceId);
        b.putString(NAVIGATOR_ID, navigatorId);
        b.putString(NAVIGATOR_EVENT_ID, navigatorEventId);
        return b;
    }
}
