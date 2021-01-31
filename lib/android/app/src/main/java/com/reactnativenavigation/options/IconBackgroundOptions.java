package com.reactnativenavigation.options;

import android.content.Context;

import androidx.annotation.Nullable;

import com.reactnativenavigation.options.params.Colour;
import com.reactnativenavigation.options.params.NullColor;
import com.reactnativenavigation.options.params.NullDensityPixel;
import com.reactnativenavigation.options.params.DensityPixel;
import com.reactnativenavigation.options.parsers.ColorParser;
import com.reactnativenavigation.options.parsers.DensityPixelParser;

import org.json.JSONObject;

public class IconBackgroundOptions {
    public Colour color = new NullColor();
    public Colour disabledColor = new NullColor();
    public DensityPixel width = new NullDensityPixel();
    public DensityPixel height = new NullDensityPixel();
    public DensityPixel cornerRadius = new NullDensityPixel();

    public static IconBackgroundOptions parse(Context context, @Nullable JSONObject json) {
        IconBackgroundOptions button = new IconBackgroundOptions();
        if (json == null) return button;
        button.color = ColorParser.parse(context, json, "color");
        button.disabledColor = ColorParser.parse(context, json, "disabledColor");
        button.width = DensityPixelParser.parse(json, "width");
        button.height = DensityPixelParser.parse(json, "height");
        button.cornerRadius = DensityPixelParser.parse(json, "cornerRadius");
        return button;
    }

    public boolean equals(IconBackgroundOptions other) {
        return color.equals(other.color) &&
                disabledColor.equals(other.disabledColor) &&
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
        return color.hasValue();
    }

    public void mergeWith(IconBackgroundOptions other) {
        if (other.color.hasValue()) color = other.color;
        if (other.disabledColor.hasValue()) disabledColor = other.disabledColor;
        if (other.width.hasValue()) width = other.width;
        if (other.height.hasValue()) height = other.height;
        if (other.cornerRadius.hasValue()) cornerRadius = other.cornerRadius;
    }

    public void mergeWithDefault(IconBackgroundOptions defaultOptions) {
        if (!color.hasValue()) color = defaultOptions.color;
        if (!disabledColor.hasValue()) disabledColor = defaultOptions.disabledColor;
        if (!width.hasValue()) width = defaultOptions.width;
        if (!height.hasValue()) height = defaultOptions.height;
        if (!cornerRadius.hasValue()) cornerRadius = defaultOptions.cornerRadius;
    }
}
