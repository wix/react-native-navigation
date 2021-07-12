package com.reactnativenavigation.options;

import android.content.Context;

import androidx.annotation.Nullable;

import com.reactnativenavigation.options.params.Bool;
import com.reactnativenavigation.options.params.NullBool;
import com.reactnativenavigation.options.params.RNNColour;
import com.reactnativenavigation.options.params.RNNColourKt;
import com.reactnativenavigation.options.params.RNNNullColor;
import com.reactnativenavigation.options.parsers.BoolParser;

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

    public static StatusBarOptions parse(Context context, JSONObject json) {
        StatusBarOptions result = new StatusBarOptions();
        if (json == null) return result;

        result.backgroundColor = RNNColourKt.parse(context, json.optJSONObject("backgroundColor"));
        result.textColorScheme = TextColorScheme.fromString(json.optString("style"));
        result.visible = BoolParser.parse(json, "visible");
        result.drawBehind = BoolParser.parse(json, "drawBehind");
        result.translucent = BoolParser.parse(json, "translucent");

        return result;
    }

    public RNNColour backgroundColor = new RNNNullColor();
    public TextColorScheme textColorScheme = TextColorScheme.None;
    public Bool visible = new NullBool();
    public Bool drawBehind = new NullBool();
    public Bool translucent = new NullBool();

    public void mergeWith(StatusBarOptions other) {
        backgroundColor.mergeWith(other.backgroundColor);
        if (other.textColorScheme.hasValue()) textColorScheme = other.textColorScheme;
        if (other.visible.hasValue()) visible = other.visible;
        if (other.drawBehind.hasValue()) drawBehind = other.drawBehind;
        if (other.translucent.hasValue()) translucent = other.translucent;
    }

    public void mergeWithDefault(StatusBarOptions defaultOptions) {
        backgroundColor.mergeWithDefault(defaultOptions.backgroundColor);
        if (!textColorScheme.hasValue()) textColorScheme = defaultOptions.textColorScheme;
        if (!visible.hasValue()) visible = defaultOptions.visible;
        if (!drawBehind.hasValue()) drawBehind = defaultOptions.drawBehind;
        if (!translucent.hasValue()) translucent = defaultOptions.translucent;
    }

    public boolean isHiddenOrDrawBehind() {
        return drawBehind.isTrue() || visible.isFalse();
    }

    public boolean hasTransparency() {
        return translucent.isTrue() || visible.isFalse() || backgroundColor.hasTransparency();
    }
}
