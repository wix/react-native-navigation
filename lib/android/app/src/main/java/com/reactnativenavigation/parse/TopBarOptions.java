package com.reactnativenavigation.parse;


import android.util.Log;

import com.reactnativenavigation.BuildConfig;
import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.parse.params.Colour;
import com.reactnativenavigation.parse.params.Fraction;
import com.reactnativenavigation.parse.params.NullBool;
import com.reactnativenavigation.parse.params.NullColor;
import com.reactnativenavigation.parse.params.NullFraction;
import com.reactnativenavigation.parse.params.NullNumber;
import com.reactnativenavigation.parse.params.NullText;
import com.reactnativenavigation.parse.params.Number;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.parse.parsers.BoolParser;
import com.reactnativenavigation.parse.parsers.ColorParser;
import com.reactnativenavigation.parse.parsers.FractionParser;
import com.reactnativenavigation.parse.parsers.NumberParser;
import com.reactnativenavigation.parse.parsers.TextParser;
import com.reactnativenavigation.utils.TypefaceLoader;

import org.json.JSONObject;

public class TopBarOptions {

    public static TopBarOptions parse(TypefaceLoader typefaceLoader, JSONObject json) {
        TopBarOptions options = new TopBarOptions();
        if (json == null) return options;

        options.title = TitleOptions.parse(typefaceLoader, json.optJSONObject("title"));
        options.subtitle = SubtitleOptions.parse(typefaceLoader, json.optJSONObject("subtitle"));
        options.background = TopBarBackgroundOptions.parse(json.optJSONObject("background"));
        options.visible = BoolParser.parse(json, "visible");
        options.animate = BoolParser.parse(json,"animate");
        options.hideOnScroll = BoolParser.parse(json,"hideOnScroll");
        options.drawBehind = BoolParser.parse(json,"drawBehind");
        options.testId = TextParser.parse(json, "testID");
        options.height = NumberParser.parse(json, "height");
        options.borderColor = ColorParser.parse(json, "borderColor");
        options.borderHeight = FractionParser.parse(json, "borderHeight");
        options.elevation = FractionParser.parse(json, "elevation");
        options.topMargin = NumberParser.parse(json, "topMargin");
        options.buttons = TopBarButtons.parse(typefaceLoader, json);

        options.rightButtonColor = ColorParser.parse(json, "rightButtonColor");
        options.leftButtonColor = ColorParser.parse(json, "leftButtonColor");
        options.leftButtonDisabledColor = ColorParser.parse(json, "leftButtonDisabledColor");
        options.rightButtonDisabledColor = ColorParser.parse(json, "rightButtonDisabledColor");

        options.validate();
        return options;
    }

    public TitleOptions title = new TitleOptions();
    public SubtitleOptions subtitle = new SubtitleOptions();
    public TopBarButtons buttons = new TopBarButtons();
    public Text testId = new NullText();
    public TopBarBackgroundOptions background = new TopBarBackgroundOptions();
    public Bool visible = new NullBool();
    public Bool animate = new NullBool();
    public Bool hideOnScroll = new NullBool();
    public Bool drawBehind = new NullBool();
    public Number height = new NullNumber();
    public Fraction elevation = new NullFraction();
    public Number topMargin = new NullNumber();
    public Fraction borderHeight = new NullFraction();
    public Colour borderColor = new NullColor();

    // Deprecated
    public Colour rightButtonColor = new NullColor();
    public Colour leftButtonColor = new NullColor();
    public Colour rightButtonDisabledColor = new NullColor();
    public Colour leftButtonDisabledColor = new NullColor();

    public TopBarOptions copy() {
        TopBarOptions result = new TopBarOptions();
        result.mergeWith(this);
        return result;
    }

    void mergeWith(final TopBarOptions other) {
        title.mergeWith(other.title);
        subtitle.mergeWith(other.subtitle);
        background.mergeWith(other.background);
        buttons.mergeWith(other.buttons);
        if (other.testId.hasValue()) testId = other.testId;
        if (other.visible.hasValue()) visible = other.visible;
        if (other.animate.hasValue()) animate = other.animate;
        if (other.hideOnScroll.hasValue()) hideOnScroll = other.hideOnScroll;
        if (other.drawBehind.hasValue()) drawBehind = other.drawBehind;
        if (other.height.hasValue()) height = other.height;
        if (other.borderHeight.hasValue()) borderHeight = other.borderHeight;
        if (other.borderColor.hasValue()) borderColor = other.borderColor;
        if (other.elevation.hasValue()) elevation = other.elevation;
        if (other.topMargin.hasValue()) topMargin = other.topMargin;

        if (other.rightButtonColor.hasValue()) rightButtonColor = other.rightButtonColor;
        if (other.leftButtonColor.hasValue()) leftButtonColor = other.leftButtonColor;
        if (other.rightButtonDisabledColor.hasValue()) rightButtonDisabledColor = other.rightButtonDisabledColor;
        if (other.leftButtonDisabledColor.hasValue()) leftButtonDisabledColor = other.leftButtonDisabledColor;

        validate();
    }

    public TopBarOptions mergeWithDefault(TopBarOptions defaultOptions) {
        title.mergeWithDefault(defaultOptions.title);
        subtitle.mergeWithDefault(defaultOptions.subtitle);
        background.mergeWithDefault(defaultOptions.background);
        buttons.mergeWithDefault(defaultOptions.buttons);
        if (!visible.hasValue()) visible = defaultOptions.visible;
        if (!animate.hasValue()) animate = defaultOptions.animate;
        if (!hideOnScroll.hasValue()) hideOnScroll = defaultOptions.hideOnScroll;
        if (!drawBehind.hasValue()) drawBehind = defaultOptions.drawBehind;
        if (!testId.hasValue()) testId = defaultOptions.testId;
        if (!height.hasValue()) height = defaultOptions.height;
        if (!borderHeight.hasValue()) borderHeight = defaultOptions.borderHeight;
        if (!borderColor.hasValue()) borderColor = defaultOptions.borderColor;
        if (!elevation.hasValue()) elevation = defaultOptions.elevation;
        if (!topMargin.hasValue()) topMargin = defaultOptions.topMargin;

        if (!rightButtonColor.hasValue()) rightButtonColor = defaultOptions.rightButtonColor;
        if (!leftButtonColor.hasValue()) leftButtonColor = defaultOptions.leftButtonColor;
        if (!rightButtonDisabledColor.hasValue()) rightButtonDisabledColor = defaultOptions.rightButtonDisabledColor;
        if (!leftButtonDisabledColor.hasValue()) leftButtonDisabledColor = defaultOptions.leftButtonDisabledColor;

        validate();
        return this;
    }

    public void validate() {
        if (title.component.hasValue() && (title.text.hasValue() || subtitle.text.hasValue())) {
            if (BuildConfig.DEBUG) Log.w("RNN", "A screen can't use both text and component - clearing text.");
            title.text = new NullText();
            subtitle.text = new NullText();
        }
    }

    public boolean isHiddenOrDrawBehind() {
        return drawBehind.isTrue() || visible.isFalse();
    }
}
