package com.reactnativenavigation.options;

import android.text.TextUtils;
import android.view.View;

import java.util.Locale;

public enum LayoutDirection {
    RTL(View.LAYOUT_DIRECTION_RTL),
    LTR(View.LAYOUT_DIRECTION_LTR),
    LOCALE(View.LAYOUT_DIRECTION_LOCALE),
    DEFAULT(View.LAYOUT_DIRECTION_LTR);

    private final int direction;

    LayoutDirection(int direction) {
        this.direction = direction;
    }

    public static LayoutDirection fromString(String direction) {
        switch (direction) {
            case "rtl":
                return RTL;
            case "ltr":
                return LTR;
            case "locale":
                return LOCALE;
            default:
                return DEFAULT;
        }
    }

    public boolean hasValue() {
        return this != DEFAULT;
    }

    public int get() {
        return direction;
    }

    public int inverse() {
        return (isRtl() ? LTR : RTL).direction;
    }

    public boolean isRtl() {
        switch (direction) {
            case View.LAYOUT_DIRECTION_LTR:
                return false;
            case View.LAYOUT_DIRECTION_RTL:
                return true;
            case View.LAYOUT_DIRECTION_LOCALE:
                return TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == View.LAYOUT_DIRECTION_RTL;
            default:
                return false;
        }
    }
}
