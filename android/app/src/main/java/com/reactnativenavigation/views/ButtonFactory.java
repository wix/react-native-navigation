package com.reactnativenavigation.views;

import android.view.Menu;
import android.view.View;

import com.reactnativenavigation.params.TitleBarButtonParams;

class ButtonFactory {

    public static TitleBarButton create(int index, Menu menu, View parent, TitleBarButtonParams params, MenuButtonOnClickListener menuButtonOnClickListener, String navigatorEventId) {
        switch (params.eventId) {
            case TitleBarSearchButton.BUTTON_ID:
                return new TitleBarSearchButton(index, menu, parent, params, menuButtonOnClickListener, navigatorEventId);
            default:
                return new TitleBarButton(index, menu, parent, params, menuButtonOnClickListener, navigatorEventId);
        }
    }
}
