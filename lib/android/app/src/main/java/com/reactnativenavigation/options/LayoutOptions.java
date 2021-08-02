package com.reactnativenavigation.options;

import android.content.Context;

import com.reactnativenavigation.options.params.NullNumber;
import com.reactnativenavigation.options.params.Number;
import com.reactnativenavigation.options.params.ThemeColour;
import com.reactnativenavigation.options.params.NullThemeColour;
import com.reactnativenavigation.options.parsers.NumberParser;

import org.json.JSONObject;

public class LayoutOptions {
    public static LayoutOptions parse(Context context, JSONObject json) {
        LayoutOptions result = new LayoutOptions();
        if (json == null) return result;

        result.backgroundColor = ThemeColour.parse(context, json.optJSONObject("backgroundColor"));
        result.componentBackgroundColor = ThemeColour.parse(context, json.optJSONObject("componentBackgroundColor"));
        result.topMargin = NumberParser.parse(json, "topMargin");
        result.orientation = OrientationOptions.parse(json);
        result.direction = LayoutDirection.fromString(json.optString("direction", ""));

        return result;
    }

    public ThemeColour backgroundColor = new NullThemeColour();
    public ThemeColour componentBackgroundColor = new NullThemeColour();
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
