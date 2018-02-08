package com.reactnativenavigation.parse;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class FabMenuOptions implements DEFAULT_VALUES {

    public static FabMenuOptions parse(JSONObject json) {
        FabMenuOptions options = new FabMenuOptions();
        if (json == null) return options;

        options.id = TextParser.parse(json, "id");
        options.backgroundColor = ColorParser.parse(json, "backgroundColor");
        options.clickColor = ColorParser.parse(json, "clickColor");
        options.rippleColor = ColorParser.parse(json, "rippleColor");
        options.hidden = Options.BooleanOptions.parse(json.optString("hidden"));
        if (json.has("icon")) {
            options.icon = TextParser.parse(json.optJSONObject("icon"), "uri");
        }
        if (json.has("fabs")) {
            JSONArray fabsArray = json.optJSONArray("fabs");
            for (int i = 0; i < fabsArray.length(); i++) {
                options.fabsArray.add(FabOptions.parse(fabsArray.optJSONObject(i)));
            }
        }

        return options;
    }

    public Text id = new NullText();
    public Color backgroundColor = new NullColor();
    public Color clickColor = new NullColor();
    public Color rippleColor = new NullColor();
    public Text icon = new NullText();
    public Options.BooleanOptions hidden = Options.BooleanOptions.NoValue;
    public ArrayList<FabOptions> fabsArray = new ArrayList<>();

    void mergeWith(final FabMenuOptions other) {
        if (other.id.hasValue()) {
            id = other.id;
        }
        if (other.backgroundColor.hasValue()) {
            backgroundColor = other.backgroundColor;
        }
        if (other.clickColor.hasValue()) {
            clickColor = other.clickColor;
        }
        if (other.rippleColor.hasValue()) {
            rippleColor = other.rippleColor;
        }
        if (other.hidden != Options.BooleanOptions.NoValue) {
            hidden = other.hidden;
        }
        if (other.icon.hasValue()) {
            icon = other.icon;
        }
        if (other.fabsArray.size() > 0) {
            fabsArray = other.fabsArray;
        }
    }

    void mergeWithDefault(FabMenuOptions defaultOptions) {
        if (!id.hasValue()) {
            id = defaultOptions.id;
        }
        if (!backgroundColor.hasValue()) {
            backgroundColor = defaultOptions.backgroundColor;
        }
        if (!clickColor.hasValue()) {
            clickColor = defaultOptions.clickColor;
        }
        if (!rippleColor.hasValue()) {
            rippleColor = defaultOptions.rippleColor;
        }
        if (hidden == Options.BooleanOptions.NoValue) {
            hidden = defaultOptions.hidden;
        }
        if (!icon.hasValue()) {
            icon = defaultOptions.icon;
        }
        if (fabsArray.size() == 0) {
            fabsArray = defaultOptions.fabsArray;
        }
    }
}
