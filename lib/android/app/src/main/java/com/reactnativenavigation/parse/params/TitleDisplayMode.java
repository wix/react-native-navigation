package com.reactnativenavigation.parse.params;

import androidx.annotation.NonNull;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation.TitleState;

import javax.annotation.Nullable;

public enum TitleDisplayMode {
    ALWAYS_SHOW(Constants.ALWAYS_SHOW),
    SHOW_WHEN_ACTIVE(Constants.SHOW_WHEN_ACTIVE),
    ALWAYS_HIDE(Constants.ALWAYS_HIDE),
    SHOW_WHEN_ACTIVE_FORCE(Constants.SHOW_WHEN_ACTIVE_FORCE),
    UNDEFINED(null);

    public static String fromString(String mode) {
        return mode;
    }

    @Nullable private String state;

    TitleDisplayMode(@Nullable String state) {
        this.state = state;
    }

    public boolean hasValue() {
        return state != null;
    }

    public String get(@NonNull String defaultValue) {
        return state == null ? defaultValue : state;
    }

    @NonNull
    public String toState() {
        if (state == null) throw new RuntimeException("TitleDisplayMode is undefined");
        return state;
    }

    private static class Constants {
        static final String ALWAYS_SHOW = "alwaysShow";
        static final String SHOW_WHEN_ACTIVE = "showWhenActive";
        static final String SHOW_WHEN_ACTIVE_FORCE = "showWhenActiveForce";
        static final String ALWAYS_HIDE = "alwaysHide";
    }
}
