package com.reactnativenavigation.options;

import android.content.Context;

import com.reactnativenavigation.options.params.Bool;
import com.reactnativenavigation.options.params.NullBool;
import com.reactnativenavigation.options.params.NullNumber;
import com.reactnativenavigation.options.params.NullText;
import com.reactnativenavigation.options.params.Number;
import com.reactnativenavigation.options.params.ThemeColour;
import com.reactnativenavigation.options.params.NullThemeColour;
import com.reactnativenavigation.options.params.Text;
import com.reactnativenavigation.options.parsers.BoolParser;
import com.reactnativenavigation.options.parsers.FontParser;
import com.reactnativenavigation.options.parsers.IconParser;
import com.reactnativenavigation.options.parsers.NumberParser;
import com.reactnativenavigation.options.parsers.TextParser;
import com.reactnativenavigation.options.parsers.TypefaceLoader;

import org.json.JSONObject;

public class BottomTabOptions {

    public static BottomTabOptions parse(Context context, TypefaceLoader typefaceManager, JSONObject json) {
        BottomTabOptions options = new BottomTabOptions();
        if (json == null) return options;

        options.text = TextParser.parse(json, "text");
        options.textColor = ThemeColour.parse(context, json.optJSONObject("textColor"));
        options.selectedTextColor = ThemeColour.parse(context, json.optJSONObject("selectedTextColor"));
        options.icon = IconParser.parse(json, "icon");
        options.iconWidth = NumberParser.parse(json, "iconWidth");
        options.iconHeight = NumberParser.parse(json, "iconHeight");
        options.selectedIcon = IconParser.parse(json, "selectedIcon");
        options.iconColor = ThemeColour.parse(context, json.optJSONObject("iconColor"));
        options.selectedIconColor = ThemeColour.parse(context, json.optJSONObject("selectedIconColor"));
        options.badge = TextParser.parse(json, "badge");
        options.badgeColor = ThemeColour.parse(context, json.optJSONObject("badgeColor"));
        options.animateBadge = BoolParser.parse(json, "animateBadge");
        options.testId = TextParser.parse(json, "testID");
        options.font = FontParser.parse(json);
        options.fontSize = NumberParser.parse(json, "fontSize");
        options.selectedFontSize = NumberParser.parse(json, "selectedFontSize");
        options.dotIndicator = DotIndicatorOptions.parse(context, json.optJSONObject("dotIndicator"));
        options.selectTabOnPress = BoolParser.parse(json, "selectTabOnPress");
        options.popToRoot = BoolParser.parse(json, "popToRoot");

        return options;
    }

    public Text text = new NullText();
    public ThemeColour textColor = new NullThemeColour();
    public ThemeColour selectedTextColor = new NullThemeColour();
    public Text icon = new NullText();
    public Number iconWidth = new NullNumber();
    public Number iconHeight = new NullNumber();
    public Text selectedIcon = new NullText();
    public ThemeColour iconColor = new NullThemeColour();
    public ThemeColour selectedIconColor = new NullThemeColour();
    public Text testId = new NullText();
    public Text badge = new NullText();
    public ThemeColour badgeColor = new NullThemeColour();
    public Bool animateBadge = new NullBool();
    public DotIndicatorOptions dotIndicator = new DotIndicatorOptions();
    public Number fontSize = new NullNumber();
    public Number selectedFontSize = new NullNumber();
    public Bool selectTabOnPress = new NullBool();
    public Bool popToRoot = new NullBool();
    public FontOptions font = new FontOptions();


    void mergeWith(final BottomTabOptions other) {
        if (other.textColor.hasValue()) textColor = other.textColor;
        if (other.selectedTextColor.hasValue()) selectedTextColor = other.selectedTextColor;
        if (other.iconColor.hasValue()) iconColor = other.iconColor;
        if (other.selectedIconColor.hasValue()) selectedIconColor = other.selectedIconColor;
        if (other.badgeColor.hasValue()) badgeColor = other.badgeColor;

        if (other.text.hasValue()) text = other.text;
        if (other.icon.hasValue()) icon = other.icon;
        if (other.iconWidth.hasValue()) iconWidth = other.iconWidth;
        if (other.iconHeight.hasValue()) iconHeight = other.iconHeight;
        if (other.selectedIcon.hasValue()) selectedIcon = other.selectedIcon;
        if (other.badge.hasValue()) badge = other.badge;
        if (other.animateBadge.hasValue()) animateBadge = other.animateBadge;
        if (other.testId.hasValue()) testId = other.testId;
        if (other.fontSize.hasValue()) fontSize = other.fontSize;
        if (other.selectedFontSize.hasValue()) selectedFontSize = other.selectedFontSize;
        font.mergeWith(other.font);
        if (other.dotIndicator.hasValue()) dotIndicator = other.dotIndicator;
        if (other.selectTabOnPress.hasValue()) selectTabOnPress = other.selectTabOnPress;
        if (other.popToRoot.hasValue()) popToRoot = other.popToRoot;
    }

    void mergeWithDefault(final BottomTabOptions defaultOptions) {
        if (!textColor.hasValue()) textColor = defaultOptions.textColor;
        if (!selectedTextColor.hasValue()) selectedTextColor = defaultOptions.selectedTextColor;
        if (!iconColor.hasValue()) iconColor = defaultOptions.iconColor;
        if (!selectedIconColor.hasValue()) selectedIconColor = defaultOptions.selectedIconColor;
        if (!badgeColor.hasValue()) badgeColor = defaultOptions.badgeColor;

        if (!text.hasValue()) text = defaultOptions.text;
        if (!icon.hasValue()) icon = defaultOptions.icon;
        if (!iconWidth.hasValue()) iconWidth = defaultOptions.iconWidth;
        if (!iconHeight.hasValue()) iconHeight = defaultOptions.iconHeight;
        if (!selectedIcon.hasValue()) selectedIcon = defaultOptions.selectedIcon;
        if (!badge.hasValue()) badge = defaultOptions.badge;
        if (!animateBadge.hasValue()) animateBadge = defaultOptions.animateBadge;
        if (!fontSize.hasValue()) fontSize = defaultOptions.fontSize;
        if (!selectedFontSize.hasValue()) selectedFontSize = defaultOptions.selectedFontSize;
        font.mergeWithDefault(defaultOptions.font);
        if (!testId.hasValue()) testId = defaultOptions.testId;
        if (!dotIndicator.hasValue()) dotIndicator = defaultOptions.dotIndicator;
        if (!selectTabOnPress.hasValue()) selectTabOnPress = defaultOptions.selectTabOnPress;
        if (!popToRoot.hasValue()) popToRoot = defaultOptions.popToRoot;

    }

}
