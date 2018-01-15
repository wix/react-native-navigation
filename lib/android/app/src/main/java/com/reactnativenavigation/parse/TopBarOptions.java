package com.reactnativenavigation.parse;


import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;

import com.reactnativenavigation.utils.TypefaceLoader;

import org.json.JSONObject;

import java.util.ArrayList;

public class TopBarOptions implements DEFAULT_VALUES {

    public static TopBarOptions parse(TypefaceLoader typefaceManager, JSONObject json) {
        TopBarOptions options = new TopBarOptions();
        if (json == null) return options;

        options.title = json.optString("title", NO_VALUE);
        options.backgroundColor = json.optInt("backgroundColor", NO_COLOR_VALUE);
        options.textColor = json.optInt("textColor", NO_COLOR_VALUE);
        options.textFontSize = (float) json.optDouble("textFontSize", NO_FLOAT_VALUE);
        options.textFontFamily = typefaceManager.getTypeFace(json.optString("textFontFamily", NO_VALUE));
        options.hidden = Options.BooleanOptions.parse(json.optString("hidden"));
        options.animateHide = Options.BooleanOptions.parse(json.optString("animateHide"));
        options.hideOnScroll = Options.BooleanOptions.parse(json.optString("hideOnScroll"));
        options.drawBehind = Options.BooleanOptions.parse(json.optString("drawBehind"));
        options.rightButtons = Button.parseJsonArray(json.optJSONArray("rightButtons"));
        options.leftButtons = Button.parseJsonArray(json.optJSONArray("leftButtons"));

        return options;
    }

    public String title = NO_VALUE;
    @ColorInt public int backgroundColor = NO_COLOR_VALUE;
    @ColorInt public int textColor = NO_COLOR_VALUE;
    public float textFontSize = NO_FLOAT_VALUE;
    @Nullable public Typeface textFontFamily;
    public Options.BooleanOptions hidden = Options.BooleanOptions.NoValue;
    public Options.BooleanOptions animateHide = Options.BooleanOptions.NoValue;
    public Options.BooleanOptions hideOnScroll = Options.BooleanOptions.NoValue;
    public Options.BooleanOptions drawBehind = Options.BooleanOptions.NoValue;
    public ArrayList<Button> leftButtons;
    public ArrayList<Button> rightButtons;

    void mergeWith(final TopBarOptions other) {
        if (!NO_VALUE.equals(other.title)) title = other.title;
        if (other.backgroundColor != NO_COLOR_VALUE)
            backgroundColor = other.backgroundColor;
        if (other.textColor != NO_COLOR_VALUE)
            textColor = other.textColor;
        if (other.textFontSize != NO_FLOAT_VALUE)
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
        if (NO_VALUE.equals(title))
            title = defaultOptions.title;
        if (backgroundColor == NO_COLOR_VALUE)
            backgroundColor = defaultOptions.backgroundColor;
        if (textColor == NO_COLOR_VALUE)
            textColor = defaultOptions.textColor;
        if (textFontSize == NO_FLOAT_VALUE)
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
