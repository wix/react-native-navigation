package com.reactnativenavigation.parse;

import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.parse.params.Colour;
import com.reactnativenavigation.parse.params.NullBool;
import com.reactnativenavigation.parse.params.NullColor;
import com.reactnativenavigation.parse.parsers.BoolParser;
import com.reactnativenavigation.parse.parsers.ColorParser;

import org.json.JSONObject;

public class NavigationBarOptions {
    public static NavigationBarOptions parse(JSONObject json) {
        NavigationBarOptions result = new NavigationBarOptions();
        if (json == null) return result;

        result.backgroundColor = ColorParser.parse(json, "backgroundColor");
        result.isVisible = BoolParser.parse(json, "visible");

        return result;
    }

    public Colour backgroundColor = new NullColor();
    public Bool isVisible = new NullBool();

    public void mergeWith(NavigationBarOptions other) {
        if (other.backgroundColor.hasValue()) backgroundColor = other.backgroundColor;
        if (other.isVisible.hasValue()) isVisible = other.isVisible;
    }

    public void mergeWithDefault(NavigationBarOptions defaultOptions) {
        if (!backgroundColor.hasValue()) backgroundColor = defaultOptions.backgroundColor;
        if (!isVisible.hasValue()) isVisible = defaultOptions.isVisible;
    }
}