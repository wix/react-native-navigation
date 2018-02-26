package com.reactnativenavigation.parse;

import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.parse.params.NullBool;
import com.reactnativenavigation.parse.parsers.BoolParser;

import org.json.JSONObject;

public class SideMenuOptions {
    public Bool visible = new NullBool();

    public static SideMenuOptions parse(JSONObject json) {
        SideMenuOptions options = new SideMenuOptions();
        if (json == null) return options;

        options.visible = BoolParser.parse(json, "visible");
        return options;
    }

    public void mergeWith(SideMenuOptions other) {
        if (other.visible.hasValue()) {
            visible = other.visible;
        }
    }
}
