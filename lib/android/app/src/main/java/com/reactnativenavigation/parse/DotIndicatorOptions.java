package com.reactnativenavigation.parse;

import android.support.annotation.Nullable;

import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.parse.params.Colour;
import com.reactnativenavigation.parse.params.NullBool;
import com.reactnativenavigation.parse.params.NullColor;
import com.reactnativenavigation.parse.params.NullNumber;
import com.reactnativenavigation.parse.params.Number;
import com.reactnativenavigation.parse.parsers.BoolParser;
import com.reactnativenavigation.parse.parsers.ColorParser;
import com.reactnativenavigation.parse.parsers.NumberParser;

import org.json.JSONObject;

public class DotIndicatorOptions {
    public static DotIndicatorOptions parse(@Nullable JSONObject json) {
        DotIndicatorOptions options = new DotIndicatorOptions();
        if (json == null) return options;

        options.color = ColorParser.parse(json, "color");
        options.size = NumberParser.parse(json, "size");
        options.visible = BoolParser.parse(json, "visible");

        return options;
    }

    public Colour color = new NullColor();
    public Number size = new NullNumber();
    public Bool visible = new NullBool();

    public boolean hasValue() {
        return visible.hasValue();
    }
}
