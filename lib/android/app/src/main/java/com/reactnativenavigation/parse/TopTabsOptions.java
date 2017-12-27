package com.reactnativenavigation.parse;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.reactnativenavigation.views.style.Color;
import com.reactnativenavigation.views.style.NullColor;

import org.json.JSONObject;

public class TopTabsOptions implements DEFAULT_VALUES {

    @NonNull public Color selectedTabColor = new NullColor();
    @NonNull public Color unselectedTabColor = new NullColor();

    public static TopTabsOptions parse(@Nullable JSONObject json) {
        TopTabsOptions result = new TopTabsOptions();
        if (json == null) return result;
        result.selectedTabColor = ColorParser.parse(json, "selectedTabColor");
        result.unselectedTabColor = ColorParser.parse(json, "unselectedTabColor");
        return result;
    }

    void mergeWith(TopTabsOptions other) {
        if (other.selectedTabColor.hasColor()) {
            selectedTabColor = other.selectedTabColor;
        }
        if (other.unselectedTabColor.hasColor()) {
            unselectedTabColor = other.unselectedTabColor;
        }
    }

    void mergeWithDefault(TopTabsOptions defaultOptions) {
        if (!selectedTabColor.hasColor()) {
            selectedTabColor = defaultOptions.selectedTabColor;
        }
        if (!unselectedTabColor.hasColor()) {
            unselectedTabColor = defaultOptions.unselectedTabColor;
        }
    }
}
