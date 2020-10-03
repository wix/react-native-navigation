package com.reactnativenavigation.options;

import android.content.Context;

import com.reactnativenavigation.options.params.Bool;
import com.reactnativenavigation.options.params.Colour;
import com.reactnativenavigation.options.params.NullBool;
import com.reactnativenavigation.options.params.NullColor;
import com.reactnativenavigation.options.parsers.BoolParser;
import com.reactnativenavigation.options.parsers.ColorParser;

import org.json.JSONObject;

public class NavigationBarOptions {
    public static NavigationBarOptions parse(Context context, JSONObject json) {
        NavigationBarOptions result = new NavigationBarOptions();
        if (json == null) return result;

        result.backgroundColor = ColorParser.parse(context, json, "backgroundColor");
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