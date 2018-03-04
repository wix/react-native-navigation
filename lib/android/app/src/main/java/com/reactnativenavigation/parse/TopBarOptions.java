package com.reactnativenavigation.parse;


import android.graphics.Typeface;
import android.support.annotation.Nullable;

import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.parse.params.Button;
import com.reactnativenavigation.parse.params.Color;
import com.reactnativenavigation.parse.params.Fraction;
import com.reactnativenavigation.parse.params.NullBool;
import com.reactnativenavigation.parse.params.NullColor;
import com.reactnativenavigation.parse.params.NullFraction;
import com.reactnativenavigation.parse.params.NullText;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.parse.parsers.BoolParser;
import com.reactnativenavigation.parse.parsers.ColorParser;
import com.reactnativenavigation.parse.parsers.FractionParser;
import com.reactnativenavigation.parse.parsers.TextParser;
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
        options.visible = BoolParser.parse(json, "visible");
        options.animate = BoolParser.parse(json,"animate");
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
    public Bool visible = new NullBool();
    public Bool animate = new NullBool();
    public Bool hideOnScroll = new NullBool();
    public Bool drawBehind = new NullBool();
    public ArrayList<Button> leftButtons;
    public ArrayList<Button> rightButtons;

    void mergeWith(final TopBarOptions other) {
        if (other.testId.hasValue()) {
            testId = other.testId;
        }
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
        if (other.visible.hasValue()) {
            visible = other.visible;
        }
        if (other.animate.hasValue()) {
            animate = other.animate;
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
        if (!visible.hasValue())
            visible = defaultOptions.visible;
        if (!animate.hasValue())
            animate = defaultOptions.animate;
        if (!hideOnScroll.hasValue())
            hideOnScroll = defaultOptions.hideOnScroll;
        if (!drawBehind.hasValue())
            drawBehind = defaultOptions.drawBehind;
        if (leftButtons == null)
            leftButtons = defaultOptions.leftButtons;
        if (rightButtons == null)
            rightButtons = defaultOptions.rightButtons;
        if (!testId.hasValue()) {
            testId = defaultOptions.testId;
        }
    }
}
