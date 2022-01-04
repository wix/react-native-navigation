
package com.reactnativenavigation.options;

import com.reactnativenavigation.options.params.Bool;
import com.reactnativenavigation.options.params.NullBool;
import com.reactnativenavigation.options.parsers.BoolParser;

import org.json.JSONObject;

public class OverlayOptions {
    public Bool interceptTouchOutside = new NullBool();
    public OverlayAttachOptions overlayAttachOptions = new OverlayAttachOptions();
    public static OverlayOptions parse(JSONObject json) {
        OverlayOptions options = new OverlayOptions();
        if (json == null) return options;
        options.overlayAttachOptions = OverlayAttachOptions.parse(json.optJSONObject("attach"));
        options.interceptTouchOutside = BoolParser.parse(json,"interceptTouchOutside");
        return options;
    }
}
