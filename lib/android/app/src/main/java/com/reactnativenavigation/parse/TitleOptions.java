package com.reactnativenavigation.parse;

import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.Log;

import com.reactnativenavigation.BuildConfig;
import com.reactnativenavigation.parse.params.Color;
import com.reactnativenavigation.parse.params.Fraction;
import com.reactnativenavigation.parse.params.NullColor;
import com.reactnativenavigation.parse.params.NullFraction;
import com.reactnativenavigation.parse.params.NullText;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.parse.parsers.ColorParser;
import com.reactnativenavigation.parse.parsers.FractionParser;
import com.reactnativenavigation.parse.parsers.TextParser;
import com.reactnativenavigation.utils.TypefaceLoader;

import org.json.JSONObject;

public class TitleOptions {
    public enum Alignment {
        Center, Fill, Default;

        public static Alignment fromString(String alignment) {
            switch (alignment) {
                case "center":
                    return Center;
                case "fill":
                    return Fill;
                default:
                    return Default;
            }
        }
    }

    public static TitleOptions parse(TypefaceLoader typefaceManager, JSONObject json) {
        final TitleOptions options = new TitleOptions();
        if (json == null) {
            return options;
        }

        options.text = TextParser.parse(json, "text");
        options.color = ColorParser.parse(json, "color");
        options.fontSize = FractionParser.parse(json, "fontSize");
        options.fontFamily = typefaceManager.getTypeFace(json.optString("fontFamily", ""));
        options.component = TextParser.parse(json, "component");
        options.alignment = Alignment.fromString(TextParser.parse(json, "alignment").get(""));

        validate(options);

        return options;
    }

    public Text text = new NullText();
    public Color color = new NullColor();
    public Fraction fontSize = new NullFraction();
    @Nullable public Typeface fontFamily;
    public Text component = new NullText();
    public Alignment alignment = Alignment.Default;

    void mergeWith(final TitleOptions other) {
        if (other.text.hasValue()) text = other.text;
        if (other.color.hasValue()) color = other.color;
        if (other.fontSize.hasValue()) fontSize = other.fontSize;
        if (other.fontFamily != null) fontFamily = other.fontFamily;
        if (other.component.hasValue()) component = other.component;
        if (other.alignment != Alignment.Default) alignment = other.alignment;
        validate(this);
    }

    void mergeWithDefault(TitleOptions defaultOptions) {
        if (!text.hasValue()) text = defaultOptions.text;
        if (!color.hasValue()) color = defaultOptions.color;
        if (!fontSize.hasValue()) fontSize = defaultOptions.fontSize;
        if (fontFamily == null) fontFamily = defaultOptions.fontFamily;
        if (!component.hasValue()) component = defaultOptions.component;
        if (alignment == Alignment.Default) alignment = defaultOptions.alignment;
        validate(this);
    }

    private static void validate(TitleOptions options) {
        if (options.component.hasValue() && options.text.hasValue()) {
            if (BuildConfig.DEBUG) Log.w("RNN", "A screen can't use both text and component - clearing text.");
            options.text = new NullText();
        }
    }
}
