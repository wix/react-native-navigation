package com.reactnativenavigation.parse;

import com.reactnativenavigation.parse.params.Colour;
import com.reactnativenavigation.parse.params.NullColor;
import com.reactnativenavigation.parse.parsers.ColorParser;

import org.json.JSONObject;

public class NavigationBarOptions {
    public static NavigationBarOptions parse(JSONObject json) {
        NavigationBarOptions result = new NavigationBarOptions();
        if (json == null) return result;

        result.backgroundColor = ColorParser.parse(json, "backgroundColor");

        return result;
    }

    public Colour backgroundColor = new NullColor();

    public void mergeWith(NavigationBarOptions other) {
        if (other.backgroundColor.hasValue()) backgroundColor = other.backgroundColor;
    }

    public void mergeWithDefault(NavigationBarOptions defaultOptions) {
        if (!backgroundColor.hasValue()) backgroundColor = defaultOptions.backgroundColor;
    }
}