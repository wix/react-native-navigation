package com.reactnativenavigation.parse;

import org.json.JSONObject;

public class TopTabOptions implements DEFAULT_VALUES {
    public String title = NO_VALUE;

    public static TopTabOptions parse(JSONObject json) {
        TopTabOptions result = new TopTabOptions();
        if (json == null) return result;

        result.title = json.optString("title", NO_VALUE);
        return result;
    }

    void mergeWith(TopTabOptions topTabsOptions) {

    }

    void mergeWithDefault(TopTabOptions topTabsOptions) {

    }
}
