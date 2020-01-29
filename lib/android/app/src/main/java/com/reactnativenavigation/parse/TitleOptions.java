package com.reactnativenavigation.parse;

import android.graphics.Typeface;
import androidx.annotation.Nullable;

import com.reactnativenavigation.parse.params.Colour;
import com.reactnativenavigation.parse.params.Fraction;
import com.reactnativenavigation.parse.params.NullColor;
import com.reactnativenavigation.parse.params.NullFraction;
import com.reactnativenavigation.parse.params.NullNumber;
import com.reactnativenavigation.parse.params.NullText;
import com.reactnativenavigation.parse.params.Number;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.parse.parsers.ColorParser;
import com.reactnativenavigation.parse.parsers.FractionParser;
import com.reactnativenavigation.parse.parsers.NumberParser;
import com.reactnativenavigation.parse.parsers.TextParser;
import com.reactnativenavigation.utils.TypefaceLoader;

import org.json.JSONObject;

public class TitleOptions {

    public static TitleOptions parse(TypefaceLoader typefaceManager, JSONObject json) {
        final TitleOptions options = new TitleOptions();
        if (json == null) return options;

        options.component = Component.parse(json.optJSONObject("component"));
        options.text = TextParser.parse(json, "text");
        options.color = ColorParser.parse(json, "color");
        options.fontSize = FractionParser.parse(json, "fontSize");
        options.fontFamily = typefaceManager.getTypeFace(json.optString("fontFamily", ""));
        options.alignment = Alignment.fromString(TextParser.parse(json, "alignment").get(""));
        options.height = NumberParser.parse(json, "height");
        options.topMargin = NumberParser.parse(json, "topMargin");

        return options;
    }

    public Text text = new NullText();
    public Colour color = new NullColor();
    public Fraction fontSize = new NullFraction();
    public Alignment alignment = Alignment.Default;
    @Nullable public Typeface fontFamily;
    public Component component = new Component();
    public Number height = new NullNumber();
    public Number topMargin = new NullNumber();

    void mergeWith(final TitleOptions other) {
        if (other.text.hasValue()) text = other.text;
        if (other.color.hasValue()) color = other.color;
        if (other.fontSize.hasValue()) fontSize = other.fontSize;
        if (other.fontFamily != null) fontFamily = other.fontFamily;
        if (other.alignment != Alignment.Default) alignment = other.alignment;
        if (other.component.hasValue()) component = other.component;
        if (other.height.hasValue()) height = other.height;
        if (other.topMargin.hasValue()) topMargin = other.topMargin;
    }

    void mergeWithDefault(TitleOptions defaultOptions) {
        if (!text.hasValue()) text = defaultOptions.text;
        if (!color.hasValue()) color = defaultOptions.color;
        if (!fontSize.hasValue()) fontSize = defaultOptions.fontSize;
        if (fontFamily == null) fontFamily = defaultOptions.fontFamily;
        if (alignment == Alignment.Default) alignment = defaultOptions.alignment;
        component.mergeWithDefault(defaultOptions.component);
        if (!height.hasValue()) height = defaultOptions.height;
        if (!topMargin.hasValue()) topMargin = defaultOptions.topMargin;
    }
}
