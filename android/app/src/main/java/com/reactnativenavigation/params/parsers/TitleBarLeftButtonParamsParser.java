package com.reactnativenavigation.params.parsers;

import android.os.Bundle;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.reactnativenavigation.params.TitleBarLeftButtonParams;

public class TitleBarLeftButtonParamsParser extends TitleBarButtonParamsParser {

    public TitleBarLeftButtonParams parseSingleButton(Bundle params) {
        TitleBarLeftButtonParams leftButtonParams = new TitleBarLeftButtonParams(super.parseSingleButton(params));
        leftButtonParams.iconState = getIconStateFromId(leftButtonParams.eventId);
        return leftButtonParams;
    }

    private MaterialMenuDrawable.IconState getIconStateFromId(String id) {
        switch (id) {
            case "back":
                return MaterialMenuDrawable.IconState.ARROW;
            case "cancel":
                return MaterialMenuDrawable.IconState.X;
            case "accept":
                return MaterialMenuDrawable.IconState.CHECK;
            case "sideMenu":
                return MaterialMenuDrawable.IconState.BURGER;
            default:
                throw new RuntimeException("Unsupported button id: " + id + ". Supported ids are: back, cancel, accept and sideMenu");
        }
    }
}
