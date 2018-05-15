package com.reactnativenavigation.parse;

import com.reactnativenavigation.parse.params.Color;
import com.reactnativenavigation.parse.parsers.ColorParser;

import org.json.JSONObject;

public class StatusBarOptions {
    public static StatusBarOptions parse(JSONObject json) {
        StatusBarOptions result = new StatusBarOptions();
        if (json == null) return result;

        result.backgroundColor = ColorParser.parse(json, "statusBarBackgroundColor");

        return result;
    }

    public Color backgroundColor = new Color(android.graphics.Color.BLACK);

    public void mergeWith(StatusBarOptions other) {
        if (other.backgroundColor.hasValue()) backgroundColor = other.backgroundColor;
    }

    public void mergeWithDefault(StatusBarOptions defaultOptions) {
        if (!backgroundColor.hasValue()) backgroundColor = defaultOptions.backgroundColor;
    }
}
