package com.reactnativenavigation.options;

import android.content.Context;
import android.view.MenuItem;

import androidx.annotation.Nullable;

import com.reactnativenavigation.options.params.Bool;
import com.reactnativenavigation.options.params.Colour;
import com.reactnativenavigation.options.params.Fraction;
import com.reactnativenavigation.options.params.NullBool;
import com.reactnativenavigation.options.params.NullColor;
import com.reactnativenavigation.options.params.NullFraction;
import com.reactnativenavigation.options.params.NullNumber;
import com.reactnativenavigation.options.params.NullText;
import com.reactnativenavigation.options.params.Number;
import com.reactnativenavigation.options.params.Text;
import com.reactnativenavigation.options.parsers.BoolParser;
import com.reactnativenavigation.options.parsers.ColorParser;
import com.reactnativenavigation.options.parsers.FontParser;
import com.reactnativenavigation.options.parsers.FractionParser;
import com.reactnativenavigation.options.parsers.NumberParser;
import com.reactnativenavigation.options.parsers.TextParser;
import com.reactnativenavigation.utils.CompatUtils;
import com.reactnativenavigation.utils.IdFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import static com.reactnativenavigation.utils.ObjectUtils.take;

public class IconBackgroundOptions {
    public Colour color = new NullColor();
    public Number width = new NullNumber();
    public Number height = new NullNumber();
    public Fraction cornerRadius = new NullFraction();

    public static IconBackgroundOptions parse(Context context, @Nullable JSONObject json) {
        IconBackgroundOptions button = new IconBackgroundOptions();
        if (json == null) return button;
        button.color = ColorParser.parse(context, json, "color");
        button.width = NumberParser.parse(json, "width");
        button.height = NumberParser.parse(json, "height");
        button.cornerRadius = FractionParser.parse(json, "cornerRadius");
        return button;
    }

    public boolean equals(IconBackgroundOptions other) {
        return color.equals(other.color) &&
               width.equals(other.width) &&
                height.equals(other.height) &&
                cornerRadius.equals(other.cornerRadius);
    }

    public IconBackgroundOptions copy() {
        IconBackgroundOptions options = new IconBackgroundOptions();
        options.mergeWith(this);
        return options;
    }

    public boolean hasValue() {
        return color.hasValue() ||
                width.hasValue() ||
                height.hasValue() ||
                cornerRadius.hasValue();
    }

    public void mergeWith(IconBackgroundOptions other) {
        if (other.color.hasValue()) color = other.color;
        if (other.width.hasValue()) width = other.width;
        if (other.height.hasValue()) height = other.height;
        if (other.cornerRadius.hasValue()) cornerRadius = other.cornerRadius;
    }

    public void mergeWithDefault(IconBackgroundOptions defaultOptions) {
        if (!color.hasValue()) color = defaultOptions.color;
        if (!width.hasValue()) width = defaultOptions.width;
        if (!height.hasValue()) height = defaultOptions.height;
        if (!cornerRadius.hasValue()) cornerRadius = defaultOptions.cornerRadius;
    }
}
