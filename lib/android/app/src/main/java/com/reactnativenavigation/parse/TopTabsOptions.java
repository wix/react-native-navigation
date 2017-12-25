package com.reactnativenavigation.parse;

import android.support.annotation.Nullable;

import org.json.JSONObject;

public class TopTabsOptions implements DEFAULT_VALUES {

    public static TopTabsOptions parse(@Nullable JSONObject json) {
        TopTabsOptions result = new TopTabsOptions();
        if (json == null) return result;
        return result;
    }

    void mergeWith(TopTabsOptions topTabsOptions) {

    }

    void mergeWithDefault(TopTabsOptions topTabsOptions) {

    }
}
