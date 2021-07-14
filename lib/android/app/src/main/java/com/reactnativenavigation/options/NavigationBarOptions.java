package com.reactnativenavigation.options;

import android.content.Context;

import com.reactnativenavigation.options.params.Bool;
import com.reactnativenavigation.options.params.NullBool;
import com.reactnativenavigation.options.params.RNNColour;
import com.reactnativenavigation.options.params.RNNColourKt;
import com.reactnativenavigation.options.params.RNNNullColor;
import com.reactnativenavigation.options.parsers.BoolParser;

import org.json.JSONObject;

public class NavigationBarOptions {
    public static NavigationBarOptions parse(Context context, JSONObject json) {
        NavigationBarOptions result = new NavigationBarOptions();
        if (json == null) return result;

        result.backgroundColor = RNNColourKt.parse(context, json.optJSONObject("backgroundColor"));
        result.isVisible = BoolParser.parse(json, "visible");

        return result;
    }

    public RNNColour backgroundColor = new RNNNullColor();
    public Bool isVisible = new NullBool();

    public void mergeWith(NavigationBarOptions other) {
        if (other.isVisible.hasValue()) isVisible = other.isVisible;
        if (other.backgroundColor.hasValue()) backgroundColor = other.backgroundColor;
    }

    public void mergeWithDefault(NavigationBarOptions defaultOptions) {
        if (!isVisible.hasValue()) isVisible = defaultOptions.isVisible;
        if (!backgroundColor.hasValue()) backgroundColor = defaultOptions.backgroundColor;
    }
}