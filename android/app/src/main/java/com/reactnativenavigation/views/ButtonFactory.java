package com.reactnativenavigation.views;

import android.view.Menu;
import android.view.View;

import com.reactnativenavigation.params.TitleBarButtonParams;

class ButtonFactory {

    public static TitleBarButton create(Menu menu, View parent, TitleBarButtonParams params, String navigatorEventId) {
        switch (params.eventId) {
            case TitleBarSearchButton.BUTTON_ID:
                return new TitleBarSearchButton(menu, parent, params, navigatorEventId);
            default:
                return new TitleBarButton(menu, parent, params, navigatorEventId);
        }
    }
}
