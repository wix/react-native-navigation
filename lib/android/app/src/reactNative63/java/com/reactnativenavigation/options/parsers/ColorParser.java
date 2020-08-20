package com.reactnativenavigation.options.parsers;

import android.content.Context;

import com.facebook.react.bridge.ColorPropConverter;
import com.reactnativenavigation.options.params.Colour;
import com.reactnativenavigation.options.params.DontApplyColour;
import com.reactnativenavigation.options.params.NullColor;
import com.reactnativenavigation.options.parsers.JSONParser;

import org.json.JSONObject;

public class ColorParser {
    public static Colour parse(Context context, JSONObject json, String colorName) {
        if (json.has(colorName)) {
            Object color = json.opt(colorName);
            if (color == null) {
                return new DontApplyColour();
            } else if (color instanceof Integer) {
                return new Colour(json.optInt(colorName));
            }
            Object convertedColor = JSONParser.convert(json.optJSONObject(colorName));
            Integer processedColor = ColorPropConverter.getColor(convertedColor, context);
            if (processedColor != null) {
                return new Colour(processedColor);
            }
        }
        return new NullColor();
    }
}
