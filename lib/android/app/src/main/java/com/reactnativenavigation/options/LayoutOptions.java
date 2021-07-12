package com.reactnativenavigation.options;

import android.content.Context;

import com.reactnativenavigation.options.params.NullNumber;
import com.reactnativenavigation.options.params.Number;
import com.reactnativenavigation.options.params.RNNColour;
import com.reactnativenavigation.options.params.RNNColourKt;
import com.reactnativenavigation.options.params.RNNNullColor;
import com.reactnativenavigation.options.parsers.NumberParser;

import org.json.JSONObject;

public class LayoutOptions {
    public static LayoutOptions parse(Context context, JSONObject json) {
        LayoutOptions result = new LayoutOptions();
        if (json == null) return result;

        result.backgroundColor = RNNColourKt.parse(context, json.optJSONObject("backgroundColor"));
        result.componentBackgroundColor = RNNColourKt.parse(context, json.optJSONObject("componentBackgroundColor"));
        result.topMargin = NumberParser.parse(json, "topMargin");
        result.orientation = OrientationOptions.parse(json);
        result.direction = LayoutDirection.fromString(json.optString("direction", ""));

        return result;
    }

    public RNNColour backgroundColor = new RNNNullColor();
    public RNNColour componentBackgroundColor = new RNNNullColor();
    public Number topMargin = new NullNumber();
    public OrientationOptions orientation = new OrientationOptions();
    public LayoutDirection direction = LayoutDirection.DEFAULT;

    public void mergeWith(LayoutOptions other) {
        backgroundColor.mergeWith(other.backgroundColor);
        componentBackgroundColor.mergeWith(other.componentBackgroundColor);
        if (other.topMargin.hasValue()) topMargin = other.topMargin;
        if (other.orientation.hasValue()) orientation = other.orientation;
        if (other.direction.hasValue()) direction = other.direction;
    }

    public void mergeWithDefault(LayoutOptions defaultOptions) {
        backgroundColor.mergeWithDefault(defaultOptions.backgroundColor);
        componentBackgroundColor.mergeWithDefault(defaultOptions.componentBackgroundColor);
        if (!topMargin.hasValue()) topMargin = defaultOptions.topMargin;
        if (!orientation.hasValue()) orientation = defaultOptions.orientation;
        if (!direction.hasValue()) direction = defaultOptions.direction;
    }
}
