package com.reactnativenavigation.parse;


import android.graphics.Typeface;
import android.support.annotation.Nullable;

import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.parse.params.NullBool;
import com.reactnativenavigation.parse.parsers.BoolParser;
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
        options.hidden = BoolParser.parse(json, "hidden");
        options.animateHide = BoolParser.parse(json,"animateHide");
        options.hideOnScroll = BoolParser.parse(json,"hideOnScroll");
        options.drawBehind = BoolParser.parse(json,"drawBehind");
        options.rightButtons = Button.parseJsonArray(json.optJSONArray("rightButtons"));
        options.leftButtons = Button.parseJsonArray(json.optJSONArray("leftButtons"));
        options.testId = TextParser.parse(json, "testID");

        return options;
    }

    public Text title = new NullText();
    public Text testId = new NullText();
    public Color backgroundColor = new NullColor();
    public Color textColor = new NullColor();
    public Fraction textFontSize = new NullFraction();
    @Nullable public Typeface textFontFamily;
    public Bool hidden = new NullBool();
    public Bool animateHide = new NullBool();
    public Bool hideOnScroll = new NullBool();
    public Bool drawBehind = new NullBool();
    public ArrayList<Button> leftButtons;
    public ArrayList<Button> rightButtons;

    void mergeWith(final TopBarOptions other) {
        if (other.title.hasValue())
            title = other.title;
        if (other.backgroundColor.hasValue())
            backgroundColor = other.backgroundColor;
        if (other.textColor.hasValue())
            textColor = other.textColor;
        if (other.textFontSize.hasValue())
            textFontSize = other.textFontSize;
        if (other.textFontFamily != null)
            textFontFamily = other.textFontFamily;
        if (other.hidden.hasValue()) {
            hidden = other.hidden;
        }
        if (other.animateHide.hasValue()) {
            animateHide = other.animateHide;
        }
        if (other.hideOnScroll.hasValue()) {
            hideOnScroll = other.hideOnScroll;
        }
        if (other.drawBehind.hasValue()) {
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
        if (!hidden.hasValue())
            hidden = defaultOptions.hidden;
        if (!animateHide.hasValue())
            animateHide = defaultOptions.animateHide;
        if (!hideOnScroll.hasValue())
            hideOnScroll = defaultOptions.hideOnScroll;
        if (!drawBehind.hasValue())
            drawBehind = defaultOptions.drawBehind;
        if (leftButtons == null)
            leftButtons = defaultOptions.leftButtons;
        if (rightButtons == null)
            rightButtons = defaultOptions.rightButtons;
    }
}
