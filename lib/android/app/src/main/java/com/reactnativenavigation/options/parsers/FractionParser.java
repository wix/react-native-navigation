package com.reactnativenavigation.options.parsers;

import com.reactnativenavigation.options.params.Fraction;
import com.reactnativenavigation.options.params.NullFraction;

import org.json.JSONObject;

public class FractionParser {
    public static Fraction parse(JSONObject json, String fraction) {
        return json.has(fraction) ? new Fraction(json.optDouble(fraction)) : new NullFraction();
    }
}
