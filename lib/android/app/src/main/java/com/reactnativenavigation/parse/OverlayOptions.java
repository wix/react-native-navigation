package com.reactnativenavigation.parse;

import org.json.JSONObject;

public class OverlayOptions {
    public Options.BooleanOptions interceptTouchOutside = Options.BooleanOptions.False;

    public static OverlayOptions parse(JSONObject json) {
        OverlayOptions options = new OverlayOptions();
        if (json == null) return options;

        options.interceptTouchOutside = Options.BooleanOptions.parse(json.optString("interceptTouchOutside", ""));
        return options;
    }
}
