package com.reactnativenavigation.parse;

import com.reactnativenavigation.parse.params.Colour;
import com.reactnativenavigation.parse.params.NullColor;
import com.reactnativenavigation.parse.params.NullNumber;
import com.reactnativenavigation.parse.params.Number;
import com.reactnativenavigation.parse.parsers.ColorParser;
import com.reactnativenavigation.parse.parsers.NumberParser;

import org.json.JSONObject;

public class LayoutOptions {
    public static LayoutOptions parse(JSONObject json) {
        LayoutOptions result = new LayoutOptions();
        if (json == null) return result;

        result.backgroundColor = ColorParser.parse(json, "backgroundColor");
        result.componentBackgroundColor = ColorParser.parse(json, "componentBackgroundColor");
        result.topMargin = NumberParser.parse(json, "topMargin");
        result.orientation = OrientationOptions.parse(json);
        result.direction = LayoutDirection.fromString(json.optString("direction", ""));

        return result;
    }

    public Colour backgroundColor = new NullColor();
    public Colour componentBackgroundColor = new NullColor();
    public Number topMargin = new NullNumber();
    public OrientationOptions orientation = new OrientationOptions();
    public LayoutDirection direction = LayoutDirection.DEFAULT;

    public void mergeWith(LayoutOptions other) {
        if (other.backgroundColor.hasValue()) backgroundColor = other.backgroundColor;
        if (other.componentBackgroundColor.hasValue()) componentBackgroundColor = other.componentBackgroundColor;
        if (other.topMargin.hasValue()) topMargin = other.topMargin;
        if (other.orientation.hasValue()) orientation = other.orientation;
        if (other.direction.hasValue()) direction = other.direction;
    }

    public void mergeWithDefault(LayoutOptions defaultOptions) {
        if (!backgroundColor.hasValue()) backgroundColor = defaultOptions.backgroundColor;
        if (!componentBackgroundColor.hasValue()) componentBackgroundColor = defaultOptions.componentBackgroundColor;
        if (!topMargin.hasValue()) topMargin = defaultOptions.topMargin;
        if (!orientation.hasValue()) orientation = defaultOptions.orientation;
        if (!direction.hasValue()) direction = defaultOptions.direction;
    }
}
