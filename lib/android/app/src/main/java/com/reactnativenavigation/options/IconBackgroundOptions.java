package com.reactnativenavigation.options;

import android.content.Context;

import androidx.annotation.Nullable;

import com.reactnativenavigation.options.params.Colour;
import com.reactnativenavigation.options.params.NullColor;
import com.reactnativenavigation.options.params.NullPixelDensity;
import com.reactnativenavigation.options.params.PixelDensity;
import com.reactnativenavigation.options.parsers.ColorParser;
import com.reactnativenavigation.options.parsers.PixelDensityParser;

import org.json.JSONObject;

public class IconBackgroundOptions {
    public Colour color = new NullColor();
    public PixelDensity width = new NullPixelDensity();
    public PixelDensity height = new NullPixelDensity();
    public PixelDensity cornerRadius = new NullPixelDensity();

    public static IconBackgroundOptions parse(Context context, @Nullable JSONObject json) {
        IconBackgroundOptions button = new IconBackgroundOptions();
        if (json == null) return button;
        button.color = ColorParser.parse(context, json, "color");
        button.width = PixelDensityParser.parse(json, "width");
        button.height = PixelDensityParser.parse(json, "height");
        button.cornerRadius = PixelDensityParser.parse(json, "cornerRadius");
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
        return color.hasValue() &&
                (width.hasValue() ||
                height.hasValue() ||
                cornerRadius.hasValue());
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
