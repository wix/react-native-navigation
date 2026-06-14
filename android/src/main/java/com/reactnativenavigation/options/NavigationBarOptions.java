package com.reactnativenavigation.options;

import android.content.Context;

import com.reactnativenavigation.options.params.Bool;
import com.reactnativenavigation.options.params.NullBool;
import com.reactnativenavigation.options.params.ThemeColour;
import com.reactnativenavigation.options.params.NullThemeColour;
import com.reactnativenavigation.options.parsers.BoolParser;

import org.json.JSONObject;

public class NavigationBarOptions {
    public static NavigationBarOptions parse(Context context, JSONObject json) {
        NavigationBarOptions result = new NavigationBarOptions();
        if (json == null) return result;

        result.backgroundColor = ThemeColour.parse(context, json.optJSONObject("backgroundColor"));
        result.isVisible = BoolParser.parse(json, "visible");
        result.drawBehind = BoolParser.parse(json, "drawBehind");

        return result;
    }

    public ThemeColour backgroundColor = new NullThemeColour();
    public Bool isVisible = new NullBool();
    public Bool drawBehind = new NullBool();

    public void mergeWith(NavigationBarOptions other) {
        if (other.isVisible.hasValue()) isVisible = other.isVisible;
        if (other.backgroundColor.hasValue()) backgroundColor = other.backgroundColor;
        if (other.drawBehind.hasValue()) drawBehind = other.drawBehind;
    }

    public void mergeWithDefault(NavigationBarOptions defaultOptions) {
        if (!isVisible.hasValue()) isVisible = defaultOptions.isVisible;
        if (!backgroundColor.hasValue()) backgroundColor = defaultOptions.backgroundColor;
        if (!drawBehind.hasValue()) drawBehind = defaultOptions.drawBehind;
    }

    public boolean shouldDrawBehind() {
        if (drawBehind.isFalse()) return false;
        if (drawBehind.isTrue()) return true;
        return backgroundColor.hasTransparency();
    }

    public boolean isDrawBehindAndVisible() {
        return shouldDrawBehind() && isVisible.isTrueOrUndefined();
    }

    public boolean hasAnyValue() {
        return isVisible.hasValue() || drawBehind.hasValue() || backgroundColor.hasValue();
    }
}
