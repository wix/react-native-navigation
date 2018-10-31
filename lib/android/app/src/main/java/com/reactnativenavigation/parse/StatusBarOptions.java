package com.reactnativenavigation.parse;

import android.support.annotation.Nullable;

import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.parse.params.Colour;
import com.reactnativenavigation.parse.params.NullBool;
import com.reactnativenavigation.parse.params.NullColor;
import com.reactnativenavigation.parse.parsers.BoolParser;
import com.reactnativenavigation.parse.parsers.ColorParser;

import org.json.JSONObject;

public class StatusBarOptions {
    public enum TextColorScheme {
        Light("light"), Dark("dark"), None("none");

        private String scheme;

        TextColorScheme(String scheme) {
            this.scheme = scheme;
        }

        public static TextColorScheme fromString(@Nullable String scheme) {
            if (scheme == null) return None;
            switch (scheme) {
                case "light":
                    return Light;
                case "dark":
                    return Dark;
                default:
                    return None;
            }
        }

        public boolean hasValue() {
            return !scheme.equals(None.scheme);
        }
    }

    public static StatusBarOptions parse(JSONObject json) {
        StatusBarOptions result = new StatusBarOptions();
        if (json == null) return result;

        result.backgroundColor = ColorParser.parse(json, "backgroundColor");
        result.textColorScheme = TextColorScheme.fromString(json.optString("style"));
        result.visible = BoolParser.parse(json, "visible");
        result.drawBehind = BoolParser.parse(json, "drawBehind");

        return result;
    }

    public Colour backgroundColor = new NullColor();
    public TextColorScheme textColorScheme = TextColorScheme.None;
    public Bool visible = new NullBool();
    public Bool drawBehind = new NullBool();

    public void mergeWith(StatusBarOptions other) {
        if (other.backgroundColor.hasValue()) backgroundColor = other.backgroundColor;
        if (other.textColorScheme.hasValue()) textColorScheme = other.textColorScheme;
        if (other.visible.hasValue()) visible = other.visible;
        if (other.drawBehind.hasValue()) drawBehind = other.drawBehind;
    }

    public void mergeWithDefault(StatusBarOptions defaultOptions) {
        if (!backgroundColor.hasValue()) backgroundColor = defaultOptions.backgroundColor;
        if (!textColorScheme.hasValue()) textColorScheme = defaultOptions.textColorScheme;
        if (!visible.hasValue()) visible = defaultOptions.visible;
        if (!drawBehind.hasValue()) drawBehind = defaultOptions.drawBehind;
    }
}
