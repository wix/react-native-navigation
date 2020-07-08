package com.reactnativenavigation.options;

import org.json.JSONObject;

public class SideMenuRootOptions {
    public SideMenuOptions left = new SideMenuOptions();
    public SideMenuOptions right = new SideMenuOptions();

    public static SideMenuRootOptions parse(JSONObject json) {
        SideMenuRootOptions options = new SideMenuRootOptions();
        if (json == null) return options;

        options.left = SideMenuOptions.parse(json.optJSONObject("left"));
        options.right = SideMenuOptions.parse(json.optJSONObject("right"));

        return options;
    }

    public void mergeWith(SideMenuRootOptions other) {
        left.mergeWith(other.left);
        right.mergeWith(other.right);
    }

    public void mergeWithDefault(SideMenuRootOptions defaultOptions) {
        left.mergeWithDefault(defaultOptions.left);
        right.mergeWithDefault(defaultOptions.right);
    }
}
