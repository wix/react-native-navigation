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
        result.topMargin = NumberParser.parse(json, "topMargin");
        result.orientation = OrientationOptions.parse(json);

        return result;
    }

    public Colour backgroundColor = new NullColor();
    public Number topMargin = new NullNumber();
    public OrientationOptions orientation = new OrientationOptions();

    public void mergeWith(LayoutOptions other) {
        if (other.backgroundColor.hasValue()) backgroundColor = other.backgroundColor;
        if (other.topMargin.hasValue()) topMargin = other.topMargin;
        if (other.orientation.hasValue()) orientation = other.orientation;

    }

    public void mergeWithDefault(LayoutOptions defaultOptions) {
        if (!backgroundColor.hasValue()) backgroundColor = defaultOptions.backgroundColor;
        if (!topMargin.hasValue()) topMargin = defaultOptions.topMargin;
        if (!orientation.hasValue()) orientation = defaultOptions.orientation;
    }
}
