package com.reactnativenavigation.params;

import android.support.annotation.Nullable;

import com.balysv.materialmenu.MaterialMenuDrawable;

public class TitleBarLeftButtonParams extends TitleBarButtonParams {
    @Nullable public MaterialMenuDrawable.IconState iconState;
    public boolean overrideBackPressInJs;

    public TitleBarLeftButtonParams(TitleBarButtonParams params) {
        icon = params.icon;
        color = params.color;
        eventId = params.eventId;
        enabled = params.enabled;
    }

    public boolean isBackButton() {
        return eventId.equals("back");
    }

    public boolean hasIcon() {
        return iconState != null;
    }

    public void setOverrideBackPressInJs(boolean overrideBackPressInJs) {
        this.overrideBackPressInJs = overrideBackPressInJs;
    }
}
