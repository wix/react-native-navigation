package com.reactnativenavigation.parse;

import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.parse.params.NullBool;
import com.reactnativenavigation.parse.params.NullNumber;
import com.reactnativenavigation.parse.params.Number;
import com.reactnativenavigation.parse.parsers.BoolParser;
import com.reactnativenavigation.parse.parsers.NumberParser;

import org.json.JSONObject;

public class SideMenuOptions {
    public Bool visible = new NullBool();
    public Bool animate = new NullBool();
    public Bool enabled = new NullBool();
    public Number height = new NullNumber();
    public Number width = new NullNumber();

    public static SideMenuOptions parse(JSONObject json) {
        SideMenuOptions options = new SideMenuOptions();
        if (json == null) return options;

        options.visible = BoolParser.parse(json, "visible");
        options.animate = BoolParser.parse(json, "animate");
        options.enabled = BoolParser.parse(json, "enabled");
        options.height = NumberParser.parse(json, "height");
        options.width = NumberParser.parse(json, "width");

        return options;
    }

    public void mergeWith(SideMenuOptions other) {
        if (other.visible.hasValue()) visible = other.visible;
        if (other.animate.hasValue()) animate = other.animate;
        if (other.enabled.hasValue()) enabled = other.enabled;
        if (other.height.hasValue()) height = other.height;
        if (other.width.hasValue()) width = other.width;
    }

    public void mergeWithDefault(SideMenuOptions defaultOptions) {
        if (!visible.hasValue()) visible = defaultOptions.visible;
        if (!animate.hasValue()) animate = defaultOptions.animate;
        if (!enabled.hasValue()) enabled = defaultOptions.enabled;
        if (!height.hasValue()) height = defaultOptions.height;
        if (!width.hasValue()) width = defaultOptions.width;
    }
}
