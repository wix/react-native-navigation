package com.reactnativenavigation.options;

import com.reactnativenavigation.options.params.Bool;
import com.reactnativenavigation.options.params.NullBool;
import com.reactnativenavigation.options.parsers.BoolParser;

import org.json.JSONObject;

import java.util.Objects;

public class HardwareBackButtonOptions {
    public Bool dismissModalOnPress = new NullBool();

    public boolean equals(HardwareBackButtonOptions other) {
        return Objects.equals(dismissModalOnPress, other.dismissModalOnPress);
    }

    public static HardwareBackButtonOptions parseJson(JSONObject json) {
        HardwareBackButtonOptions button = new HardwareBackButtonOptions();
        if (json == null || json.toString().equals("{}")) return button;
        button.dismissModalOnPress = BoolParser.parse(json, "dismissModalOnPress");
        return button;
    }

    public HardwareBackButtonOptions copy() {
        HardwareBackButtonOptions button = new HardwareBackButtonOptions();
        button.mergeWith(this);
        return button;
    }

    public void mergeWith(HardwareBackButtonOptions other) {
        if (other.dismissModalOnPress.hasValue()) dismissModalOnPress = other.dismissModalOnPress;
    }

    public void mergeWithDefault(HardwareBackButtonOptions defaultOptions) {
        if (!dismissModalOnPress.hasValue()) dismissModalOnPress = defaultOptions.dismissModalOnPress;
    }
}
