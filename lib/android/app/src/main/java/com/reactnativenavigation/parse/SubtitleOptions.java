package com.reactnativenavigation.parse;

import android.graphics.Typeface;
import android.support.annotation.Nullable;

import com.reactnativenavigation.parse.params.Colour;
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

public class SubtitleOptions {
    public static SubtitleOptions parse(TypefaceLoader typefaceManager, JSONObject json) {
        final SubtitleOptions options = new SubtitleOptions();
        if (json == null) {
            return options;
        }

        options.text = TextParser.parse(json, "text");
        options.color = ColorParser.parse(json, "color");
        options.fontSize = FractionParser.parse(json, "fontSize");
        options.fontFamily = typefaceManager.getTypeFace(json.optString("fontFamily", ""));
        options.alignment = Alignment.fromString(TextParser.parse(json, "alignment").get(""));

        return options;
    }

    public Text text = new NullText();
    public Colour color = new NullColor();
    public Fraction fontSize = new NullFraction();
    @Nullable public Typeface fontFamily;
    public Alignment alignment = Alignment.Default;

    void mergeWith(final SubtitleOptions other) {
        if (other.text.hasValue()) text = other.text;
        if (other.color.hasValue()) color = other.color;
        if (other.fontSize.hasValue()) fontSize = other.fontSize;
        if (other.fontFamily != null) fontFamily = other.fontFamily;
        if (other.alignment != Alignment.Default) alignment = other.alignment;
    }

    void mergeWithDefault(SubtitleOptions defaultOptions) {
        if (!text.hasValue()) text = defaultOptions.text;
        if (!color.hasValue()) color = defaultOptions.color;
        if (!fontSize.hasValue()) fontSize = defaultOptions.fontSize;
        if (fontFamily == null) fontFamily = defaultOptions.fontFamily;
        if (alignment == Alignment.Default) alignment = defaultOptions.alignment;
    }
}
