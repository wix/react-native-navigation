package com.reactnativenavigation.options;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.reactnativenavigation.options.params.Bool;
import com.reactnativenavigation.options.params.NullBool;
import com.reactnativenavigation.options.params.NullNumber;
import com.reactnativenavigation.options.params.Number;
import com.reactnativenavigation.options.params.RNNColour;
import com.reactnativenavigation.options.params.RNNColourKt;
import com.reactnativenavigation.options.params.RNNNullColor;
import com.reactnativenavigation.options.parsers.BoolParser;
import com.reactnativenavigation.options.parsers.NumberParser;

import org.json.JSONObject;

public class TopTabsOptions {

    @NonNull
    public RNNColour selectedTabColor = new RNNNullColor();
    @NonNull
    public RNNColour unselectedTabColor = new RNNNullColor();
    @NonNull
    public Number fontSize = new NullNumber();
    @NonNull
    public Bool visible = new NullBool();
    @NonNull
    public Number height = new NullNumber();

    public static TopTabsOptions parse(Context context, @Nullable JSONObject json) {
        TopTabsOptions result = new TopTabsOptions();
        if (json == null) return result;
        result.selectedTabColor = RNNColourKt.parse(context, json.optJSONObject("selectedTabColor"));
        result.unselectedTabColor = RNNColourKt.parse(context, json.optJSONObject("unselectedTabColor"));
        result.fontSize = NumberParser.parse(json, "fontSize");
        result.visible = BoolParser.parse(json, "visible");
        result.height = NumberParser.parse(json, "height");
        return result;
    }

    void mergeWith(TopTabsOptions other) {
        selectedTabColor.mergeWith(other.selectedTabColor);
        unselectedTabColor.mergeWith(other.unselectedTabColor);
        if (other.fontSize.hasValue()) fontSize = other.fontSize;
        if (other.visible.hasValue()) visible = other.visible;
        if (other.height.hasValue()) height = other.height;
    }

    void mergeWithDefault(TopTabsOptions defaultOptions) {
        selectedTabColor.mergeWithDefault(defaultOptions.selectedTabColor);
        unselectedTabColor.mergeWithDefault(defaultOptions.unselectedTabColor);
        if (!fontSize.hasValue()) fontSize = defaultOptions.fontSize;
        if (!visible.hasValue()) visible = defaultOptions.visible;
        if (!height.hasValue()) height = defaultOptions.height;
    }


}
