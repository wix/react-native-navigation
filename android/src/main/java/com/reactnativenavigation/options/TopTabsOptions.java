package com.reactnativenavigation.options;

import android.content.Context;

import androidx.annotation.NonNull;
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

public class TopTabsOptions {

    @NonNull
    public ThemeColour selectedTabColor = new NullThemeColour();
    @NonNull
    public ThemeColour unselectedTabColor = new NullThemeColour();
    @NonNull
    public Number fontSize = new NullNumber();
    @NonNull
    public Bool visible = new NullBool();
    @NonNull
    public Number height = new NullNumber();

    public static TopTabsOptions parse(Context context, @Nullable JSONObject json) {
        TopTabsOptions result = new TopTabsOptions();
        if (json == null) return result;
        result.selectedTabColor = ThemeColour.parse(context, json.optJSONObject("selectedTabColor"));
        result.unselectedTabColor = ThemeColour.parse(context, json.optJSONObject("unselectedTabColor"));
        result.fontSize = NumberParser.parse(json, "fontSize");
        result.visible = BoolParser.parse(json, "visible");
        result.height = NumberParser.parse(json, "height");
        return result;
    }

    void mergeWith(TopTabsOptions other) {
        if (other.selectedTabColor.hasValue()) selectedTabColor = other.selectedTabColor;
        if (other.unselectedTabColor.hasValue()) unselectedTabColor = other.unselectedTabColor;
        if (other.fontSize.hasValue()) fontSize = other.fontSize;
        if (other.visible.hasValue()) visible = other.visible;
        if (other.height.hasValue()) height = other.height;
    }

    void mergeWithDefault(TopTabsOptions defaultOptions) {
        if (!selectedTabColor.hasValue()) selectedTabColor = defaultOptions.selectedTabColor;
        if (!unselectedTabColor.hasValue()) unselectedTabColor = defaultOptions.unselectedTabColor;
        if (!fontSize.hasValue()) fontSize = defaultOptions.fontSize;
        if (!visible.hasValue()) visible = defaultOptions.visible;
        if (!height.hasValue()) height = defaultOptions.height;
    }


}
