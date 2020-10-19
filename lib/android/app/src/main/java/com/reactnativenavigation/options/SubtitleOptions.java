package com.reactnativenavigation.options;

import android.content.Context;

import com.reactnativenavigation.options.params.Colour;
import com.reactnativenavigation.options.params.Fraction;
import com.reactnativenavigation.options.params.NullColor;
import com.reactnativenavigation.options.params.NullFraction;
import com.reactnativenavigation.options.params.NullText;
import com.reactnativenavigation.options.params.Text;
import com.reactnativenavigation.options.parsers.ColorParser;
import com.reactnativenavigation.options.parsers.FontParser;
import com.reactnativenavigation.options.parsers.FractionParser;
import com.reactnativenavigation.options.parsers.TextParser;
import com.reactnativenavigation.options.parsers.TypefaceLoader;

import org.json.JSONObject;

public class SubtitleOptions {
    public static SubtitleOptions parse(Context context, TypefaceLoader typefaceManager, JSONObject json) {
        final SubtitleOptions options = new SubtitleOptions();
        if (json == null) {
            return options;
        }

        options.text = TextParser.parse(json, "text");
        options.color = ColorParser.parse(context, json, "color");
        options.fontSize = FractionParser.parse(json, "fontSize");
        options.font = FontParser.parse(json);
        options.alignment = Alignment.fromString(TextParser.parse(json, "alignment").get(""));

        return options;
    }

    public Text text = new NullText();
    public Colour color = new NullColor();
    public Fraction fontSize = new NullFraction();
    public FontOptions font = new FontOptions();
    public Alignment alignment = Alignment.Default;

    void mergeWith(final SubtitleOptions other) {
        if (other.text.hasValue()) text = other.text;
        if (other.color.hasValue()) color = other.color;
        if (other.fontSize.hasValue()) fontSize = other.fontSize;
        font.mergeWith(other.font);
        if (other.alignment != Alignment.Default) alignment = other.alignment;
    }

    void mergeWithDefault(SubtitleOptions defaultOptions) {
        if (!text.hasValue()) text = defaultOptions.text;
        if (!color.hasValue()) color = defaultOptions.color;
        if (!fontSize.hasValue()) fontSize = defaultOptions.fontSize;
        font.mergeWithDefault(defaultOptions.font);
        if (alignment == Alignment.Default) alignment = defaultOptions.alignment;
    }
}
