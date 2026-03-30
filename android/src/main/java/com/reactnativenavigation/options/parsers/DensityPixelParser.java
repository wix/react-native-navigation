package com.reactnativenavigation.options.parsers;

import com.reactnativenavigation.options.params.NullDensityPixel;
import com.reactnativenavigation.options.params.DensityPixel;

import org.json.JSONObject;

public class DensityPixelParser {
    public static DensityPixel parse(JSONObject json, String number) {
        return json.has(number) ? new DensityPixel(json.optInt(number)) : new NullDensityPixel();
    }
}
