package com.reactnativenavigation.options;

import android.graphics.Typeface;
import androidx.annotation.Nullable;

import com.reactnativenavigation.options.params.Colour;
import com.reactnativenavigation.options.params.Fraction;
import com.reactnativenavigation.options.params.NullColor;
import com.reactnativenavigation.options.params.NullFraction;
import com.reactnativenavigation.options.params.NullNumber;
import com.reactnativenavigation.options.params.NullText;
import com.reactnativenavigation.options.params.Number;
import com.reactnativenavigation.options.params.Text;
import com.reactnativenavigation.options.parsers.ColorParser;
import com.reactnativenavigation.options.parsers.FractionParser;
import com.reactnativenavigation.options.parsers.NumberParser;
import com.reactnativenavigation.options.parsers.TextParser;
import com.reactnativenavigation.options.parsers.TypefaceLoader;

import org.json.JSONObject;

public class TitleOptions {

    public static TitleOptions parse(TypefaceLoader typefaceManager, JSONObject json) {
        final TitleOptions options = new TitleOptions();
        if (json == null) return options;

        options.component = ComponentOptions.parse(json.optJSONObject("component"));
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
    public ComponentOptions component = new ComponentOptions();
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
