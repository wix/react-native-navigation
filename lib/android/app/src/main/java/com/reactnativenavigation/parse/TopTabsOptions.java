package com.reactnativenavigation.parse;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.parse.params.Color;
import com.reactnativenavigation.parse.params.NullBool;
import com.reactnativenavigation.parse.params.NullColor;
import com.reactnativenavigation.parse.params.NullNumber;
import com.reactnativenavigation.parse.params.Number;
import com.reactnativenavigation.parse.parsers.BoolParser;
import com.reactnativenavigation.parse.parsers.ColorParser;
import com.reactnativenavigation.parse.parsers.NumberParser;

import org.json.JSONObject;

public class TopTabsOptions implements DEFAULT_VALUES {

    @NonNull public Color selectedTabColor = new NullColor();
    @NonNull public Color unselectedTabColor = new NullColor();
    @NonNull public Number fontSize = new NullNumber();
    @NonNull public Bool visible = new NullBool();

    public static TopTabsOptions parse(@Nullable JSONObject json) {
        TopTabsOptions result = new TopTabsOptions();
        if (json == null) return result;
        result.selectedTabColor = ColorParser.parse(json, "selectedColor");
        result.unselectedTabColor = ColorParser.parse(json, "unselectedTabColor");
        result.fontSize = NumberParser.parse(json, "fontSize");
        result.visible = BoolParser.parse(json, "visible");
        return result;
    }

    void mergeWith(TopTabsOptions other) {
        if (other.selectedTabColor.hasValue()) selectedTabColor = other.selectedTabColor;
        if (other.unselectedTabColor.hasValue()) unselectedTabColor = other.unselectedTabColor;
        if (other.fontSize.hasValue()) fontSize = other.fontSize;
        if (other.visible.hasValue()) visible = other.visible;
    }

    void mergeWithDefault(TopTabsOptions defaultOptions) {
        if (!selectedTabColor.hasValue()) selectedTabColor = defaultOptions.selectedTabColor;
        if (!unselectedTabColor.hasValue()) unselectedTabColor = defaultOptions.unselectedTabColor;
        if (!fontSize.hasValue()) fontSize = defaultOptions.fontSize;
        if (!visible.hasValue()) visible = defaultOptions.visible;
    }
}
