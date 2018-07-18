package com.reactnativenavigation.parse.parsers;

import com.reactnativenavigation.parse.params.Fraction;
import com.reactnativenavigation.parse.params.NullFraction;

import org.json.JSONObject;

public class FractionParser {
    public static Fraction parse(JSONObject json, String fraction) {
        return json.has(fraction) ? new Fraction(json.optDouble(fraction)) : new NullFraction();
    }
}
