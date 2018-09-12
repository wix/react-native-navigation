package com.reactnativenavigation.parse.params;

import android.support.annotation.NonNull;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation.TitleState;

import javax.annotation.Nullable;

public enum TitleDisplayMode {
    ALWAYS_SHOW(TitleState.ALWAYS_SHOW), SHOW_WHEN_ACTIVE(TitleState.SHOW_WHEN_ACTIVE), ALWAYS_HIDE(TitleState.ALWAYS_HIDE), UNDEFINED(null);

    public static TitleDisplayMode fromString(String mode) {
        switch (mode) {
            case Constants.ALWAYS_SHOW:
                return ALWAYS_SHOW;
            case Constants.SHOW_WHEN_ACTIVE:
                return SHOW_WHEN_ACTIVE;
            case Constants.ALWAYS_HIDE:
                return ALWAYS_HIDE;
            default:
                return UNDEFINED;
        }
    }

    @Nullable private TitleState state;

    TitleDisplayMode(@Nullable TitleState state) {
        this.state = state;
    }

    public boolean hasValue() {
        return state != null;
    }

    public TitleState get(@NonNull TitleState defaultValue) {
        return state == null ? defaultValue : state;
    }

    @NonNull
    public TitleState toState() {
        if (state == null) throw new RuntimeException("TitleDisplayMode is undefined");
        return state;
    }

    private static class Constants {
        static final String ALWAYS_SHOW = "alwaysShow";
        static final String SHOW_WHEN_ACTIVE = "showWhenActive";
        static final String ALWAYS_HIDE = "alwaysHide";
    }
}
