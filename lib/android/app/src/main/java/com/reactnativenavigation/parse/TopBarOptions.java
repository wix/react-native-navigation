package com.reactnativenavigation.parse;


import android.graphics.Typeface;
import android.support.annotation.Nullable;

import com.reactnativenavigation.utils.TypefaceLoader;

import org.json.JSONObject;

import java.util.ArrayList;

public class TopBarOptions implements DEFAULT_VALUES {

    public static TopBarOptions parse(TypefaceLoader typefaceManager, JSONObject json) {
        TopBarOptions options = new TopBarOptions();
        if (json == null) return options;

        options.title = TextParser.parse(json, "title");
        options.backgroundColor = ColorParser.parse(json, "backgroundColor");
        options.textColor = ColorParser.parse(json, "textColor");
        options.textFontSize = FractionParser.parse(json, "textFontSize");
        options.textFontFamily = typefaceManager.getTypeFace(json.optString("textFontFamily", ""));
        options.hidden = Options.BooleanOptions.parse(json.optString("hidden"));
        options.animateHide = Options.BooleanOptions.parse(json.optString("animateHide"));
        options.hideOnScroll = Options.BooleanOptions.parse(json.optString("hideOnScroll"));
        options.drawBehind = Options.BooleanOptions.parse(json.optString("drawBehind"));
        options.rightButtons = Button.parseJsonArray(json.optJSONArray("rightButtons"));
        options.leftButtons = Button.parseJsonArray(json.optJSONArray("leftButtons"));

        return options;
    }

    public Text title = new NullText();
    public Color backgroundColor = new NullColor();
    public Color textColor = new NullColor();
    public Fraction textFontSize = new NullFraction();
    @Nullable public Typeface textFontFamily;
    public Options.BooleanOptions hidden = Options.BooleanOptions.NoValue;
    public Options.BooleanOptions animateHide = Options.BooleanOptions.NoValue;
    public Options.BooleanOptions hideOnScroll = Options.BooleanOptions.NoValue;
    public Options.BooleanOptions drawBehind = Options.BooleanOptions.NoValue;
    public ArrayList<Button> leftButtons;
    public ArrayList<Button> rightButtons;

    void mergeWith(final TopBarOptions other) {
        if (other.title != null) title = other.title;
        if (other.backgroundColor.hasValue())
            backgroundColor = other.backgroundColor;
        if (other.textColor.hasValue())
            textColor = other.textColor;
        if (other.textFontSize.hasValue())
            textFontSize = other.textFontSize;
        if (other.textFontFamily != null)
            textFontFamily = other.textFontFamily;
        if (other.hidden != Options.BooleanOptions.NoValue) {
            hidden = other.hidden;
        }
        if (other.animateHide != Options.BooleanOptions.NoValue) {
            animateHide = other.animateHide;
        }
        if (other.hideOnScroll != Options.BooleanOptions.NoValue) {
            hideOnScroll = other.hideOnScroll;
        }
        if (other.drawBehind != Options.BooleanOptions.NoValue) {
            drawBehind = other.drawBehind;
        }
        if (other.leftButtons != null)
            leftButtons = other.leftButtons;
        if (other.rightButtons != null)
            rightButtons = other.rightButtons;
    }

    void mergeWithDefault(TopBarOptions defaultOptions) {
        if (title == null)
            title = defaultOptions.title;
        if (!backgroundColor.hasValue())
            backgroundColor = defaultOptions.backgroundColor;
        if (!textColor.hasValue())
            textColor = defaultOptions.textColor;
        if (!textFontSize.hasValue())
            textFontSize = defaultOptions.textFontSize;
        if (textFontFamily == null)
            textFontFamily = defaultOptions.textFontFamily;
        if (hidden == Options.BooleanOptions.NoValue)
            hidden = defaultOptions.hidden;
        if (animateHide == Options.BooleanOptions.NoValue)
            animateHide = defaultOptions.animateHide;
        if (hideOnScroll == Options.BooleanOptions.NoValue)
            hideOnScroll = defaultOptions.hideOnScroll;
        if (drawBehind == Options.BooleanOptions.NoValue)
            drawBehind = defaultOptions.drawBehind;
        if (leftButtons == null)
            leftButtons = defaultOptions.leftButtons;
        if (rightButtons == null)
            rightButtons = defaultOptions.rightButtons;
    }
}
