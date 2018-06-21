package com.reactnativenavigation.parse;

import com.reactnativenavigation.parse.params.Color;
import com.reactnativenavigation.parse.params.NullColor;
import com.reactnativenavigation.parse.params.NullText;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.parse.parsers.ColorParser;
import com.reactnativenavigation.parse.parsers.TextParser;

import org.json.JSONObject;

public class BottomTabOptions {

    public static BottomTabOptions parse(JSONObject json) {
        BottomTabOptions options = new BottomTabOptions();
        if (json == null) return options;

        options.text = TextParser.parse(json, "text");
        options.textColor = ColorParser.parse(json, "textColor");
        options.selectedTextColor = ColorParser.parse(json, "selectedTextColor");
        if (json.has("icon")) options.icon = TextParser.parse(json.optJSONObject("icon"), "uri");
        options.iconColor = ColorParser.parse(json, "iconColor");
        options.selectedIconColor = ColorParser.parse(json, "selectedIconColor");
        options.badge = TextParser.parse(json, "badge");
        options.testId = TextParser.parse(json, "testID");
        return options;
    }

    public Text text = new NullText();
    public Color textColor = new NullColor();
    public Color selectedTextColor = new NullColor();
    public Text icon = new NullText();
    public Color iconColor = new NullColor();
    public Color selectedIconColor = new NullColor();
    public Text testId = new NullText();
    public Text badge = new NullText();

    void mergeWith(final BottomTabOptions other) {
        if (other.text.hasValue()) text = other.text;
        if (other.textColor.hasValue()) textColor = other.textColor;
        if (other.selectedTextColor.hasValue()) selectedTextColor = other.selectedTextColor;
        if (other.icon.hasValue()) icon = other.icon;
        if (other.iconColor.hasValue()) iconColor = other.iconColor;
        if (other.selectedIconColor.hasValue()) selectedIconColor = other.selectedIconColor;
        if (other.badge.hasValue()) badge = other.badge;
        if (other.testId.hasValue()) testId = other.testId;
    }

    void mergeWithDefault(final BottomTabOptions defaultOptions) {
        if (!text.hasValue()) text = defaultOptions.text;
        if (!textColor.hasValue()) textColor = defaultOptions.textColor;
        if (!selectedTextColor.hasValue()) selectedTextColor = defaultOptions.selectedTextColor;
        if (!icon.hasValue()) icon = defaultOptions.icon;
        if (!iconColor.hasValue()) iconColor = defaultOptions.iconColor;
        if (!selectedIconColor.hasValue()) selectedIconColor = defaultOptions.selectedIconColor;
        if (!badge.hasValue()) badge = defaultOptions.badge;
    }
}
