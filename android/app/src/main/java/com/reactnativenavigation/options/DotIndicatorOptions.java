package com.reactnativenavigation.options;

import android.content.Context;

import androidx.annotation.Nullable;

import com.reactnativenavigation.options.params.Bool;
import com.reactnativenavigation.options.params.NullBool;
import com.reactnativenavigation.options.params.NullNumber;
import com.reactnativenavigation.options.params.Number;
import com.reactnativenavigation.options.params.ThemeColour;
import com.reactnativenavigation.options.params.NullThemeColour;
import com.reactnativenavigation.options.parsers.BoolParser;
import com.reactnativenavigation.options.parsers.NumberParser;

import org.json.JSONObject;

public class DotIndicatorOptions {
    public static DotIndicatorOptions parse(Context context, @Nullable JSONObject json) {
        DotIndicatorOptions options = new DotIndicatorOptions();
        if (json == null) return options;

        options.color = ThemeColour.parse(context, json.optJSONObject("color"));
        options.size = NumberParser.parse(json, "size");
        options.visible = BoolParser.parse(json, "visible");
        options.animate = BoolParser.parse(json, "animate");

        return options;
    }

    public ThemeColour color = new NullThemeColour();
    public Number size = new NullNumber();
    public Bool visible = new NullBool();
    public Bool animate = new NullBool();

    public boolean hasValue() {
        return visible.hasValue();
    }
}
