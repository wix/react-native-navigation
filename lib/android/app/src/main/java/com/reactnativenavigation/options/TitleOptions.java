package com.reactnativenavigation.options;

import android.content.Context;

import com.reactnativenavigation.options.params.Fraction;
import com.reactnativenavigation.options.params.NullFraction;
import com.reactnativenavigation.options.params.NullNumber;
import com.reactnativenavigation.options.params.NullText;
import com.reactnativenavigation.options.params.Number;
import com.reactnativenavigation.options.params.ThemeColour;
import com.reactnativenavigation.options.params.NullThemeColour;
import com.reactnativenavigation.options.params.Text;
import com.reactnativenavigation.options.parsers.FontParser;
import com.reactnativenavigation.options.parsers.FractionParser;
import com.reactnativenavigation.options.parsers.NumberParser;
import com.reactnativenavigation.options.parsers.TextParser;
import com.reactnativenavigation.options.parsers.TypefaceLoader;

import org.json.JSONObject;

public class TitleOptions {

    public static TitleOptions parse(Context context, TypefaceLoader typefaceManager, JSONObject json) {
        final TitleOptions options = new TitleOptions();
        if (json == null) return options;

        options.component = ComponentOptions.parse(json.optJSONObject("component"));
        options.text = TextParser.parse(json, "text");
        options.color = ThemeColour.parse(context, json.optJSONObject("color"));
        options.fontSize = FractionParser.parse(json, "fontSize");
        options.font = FontParser.parse(json);
        options.alignment = Alignment.fromString(TextParser.parse(json, "alignment").get(""));
        options.height = NumberParser.parse(json, "height");
        options.topMargin = NumberParser.parse(json, "topMargin");

        return options;
    }

    public Text text = new NullText();
    public ThemeColour color = new NullThemeColour();
    public Fraction fontSize = new NullFraction();
    public Alignment alignment = Alignment.Default;
    public FontOptions font = new FontOptions();
    public ComponentOptions component = new ComponentOptions();
    public Number height = new NullNumber();
    public Number topMargin = new NullNumber();

    void mergeWith(final TitleOptions other) {
        if (other.text.hasValue()) {
            text = other.text;
            this.component.reset();
        } else {
            //there is a component but no text, clear text, breaks coexistence
            if (other.component.hasValue())
                this.text = other.text;
        }
        if (other.color.hasValue()) color = other.color;
        if (other.fontSize.hasValue()) fontSize = other.fontSize;
        font.mergeWith(other.font);
        if (other.alignment != Alignment.Default) alignment = other.alignment;
        if (other.component.hasValue()) component = other.component;
        if (other.height.hasValue()) height = other.height;
        if (other.topMargin.hasValue()) topMargin = other.topMargin;
    }

    void mergeWithDefault(TitleOptions defaultOptions) {
        if (!text.hasValue()) text = defaultOptions.text;
        if (!color.hasValue()) color = defaultOptions.color;
        if (!fontSize.hasValue()) fontSize = defaultOptions.fontSize;
        font.mergeWithDefault(defaultOptions.font);
        if (alignment == Alignment.Default) alignment = defaultOptions.alignment;
        component.mergeWithDefault(defaultOptions.component);
        if (!height.hasValue()) height = defaultOptions.height;
        if (!topMargin.hasValue()) topMargin = defaultOptions.topMargin;
    }
}
