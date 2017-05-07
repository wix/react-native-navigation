package com.reactnativenavigation.params;

import android.support.annotation.Nullable;

import com.balysv.materialmenu.MaterialMenuDrawable;

public class TitleBarLeftButtonParams extends TitleBarButtonParams {
    @Nullable public MaterialMenuDrawable.IconState iconState;

    public TitleBarLeftButtonParams(TitleBarButtonParams params) {
        icon = params.icon;
        color = params.color;
        eventId = params.eventId;
        enabled = params.enabled;
    }

    public boolean isBackButton() {
        return eventId.equals("back");
    }

    public boolean hasDefaultIcon() {
        return iconState != null;
    }

    public boolean hasCustomIcon() {
        return icon != null;
    }
}
