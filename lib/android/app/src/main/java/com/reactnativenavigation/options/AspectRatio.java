package com.reactnativenavigation.options;

import androidx.annotation.CheckResult;

import com.reactnativenavigation.options.params.Number;
import com.reactnativenavigation.options.parsers.NumberParser;

import org.json.JSONObject;

public class AspectRatio {
    public Number numerator = new Number(0);
    public Number denominator = new Number(0);


    public static AspectRatio parse(JSONObject json) {
        AspectRatio aspectRatio = new AspectRatio();
        aspectRatio.numerator = NumberParser.parse(json, "numerator");
        aspectRatio.denominator = NumberParser.parse(json, "denominator");
        return aspectRatio;
    }

    @CheckResult
    public AspectRatio copy() {
        AspectRatio aspectRatio = new AspectRatio();
        aspectRatio.mergeWith(this);
        return aspectRatio;
    }

    @CheckResult
    public AspectRatio mergeWith(AspectRatio other) {
        this.denominator = other.denominator;
        this.numerator = other.numerator;
        return this;
    }
}