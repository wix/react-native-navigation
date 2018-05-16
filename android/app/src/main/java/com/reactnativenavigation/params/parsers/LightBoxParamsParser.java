package com.reactnativenavigation.params.parsers;

import android.os.Bundle;
import android.view.WindowManager;

import com.reactnativenavigation.params.LightBoxParams;
import com.reactnativenavigation.params.NavigationParams;

public class LightBoxParamsParser extends Parser {
    private Bundle params;

    public LightBoxParamsParser(Bundle params) {
        this.params = params;
    }

    public LightBoxParams parse() {
        LightBoxParams result = new LightBoxParams();
        if (params.isEmpty()) {
            return result;
        }
        result.screenId = params.getString("screenId");
        result.navigationParams = new NavigationParams(params.getBundle("navigationParams"));
        result.backgroundColor = getColor(params, "backgroundColor");
        result.tapBackgroundToDismiss = params.getBoolean("tapBackgroundToDismiss");
        result.overrideBackPress = params.getBoolean("overrideBackPress");
        result.adjustSoftInput = Adjustment.fromString(params.getString("adjustSoftInput")).value;
        return result;
    }

    public enum Adjustment {
        NOTHING("nothing", WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING),
        PAN("pan", WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN),
        RESIZE("resize", WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE),
        UNSPECIFIED("unspecified", WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);

        private String name;
        private int value;

        Adjustment(String name, int value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public String toString() {
            return name;
        }

        public static Adjustment fromString(String name) {
            for (Adjustment adjustment : values()) {
                if (adjustment.name.equals(name)) {
                    return adjustment;
                }
            }
            return UNSPECIFIED;
        }

    }
}
