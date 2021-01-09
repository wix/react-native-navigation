package com.reactnativenavigation.options;

import androidx.annotation.CheckResult;

import com.reactnativenavigation.options.params.Number;
import com.reactnativenavigation.options.parsers.NumberParser;

import org.json.JSONObject;

public class Dimension {
    public Number height = new Number(0);
    public Number width = new Number(0);


    public static Dimension parse(JSONObject json) {
        Dimension dimension = new Dimension();
        if (json != null) {
            dimension.height = NumberParser.parse(json, "height");
            dimension.width = NumberParser.parse(json, "width");
        } else {
            dimension.height = NumberParser.nullNumber();
            dimension.width = NumberParser.nullNumber();
        }
        return dimension;
    }

    @CheckResult
    public Dimension copy() {
        Dimension dimension = new Dimension();
        dimension.mergeWith(this);
        return dimension;
    }

    @CheckResult
    public Dimension mergeWith(Dimension other) {
        this.height = other.height;
        this.width = other.width;
        return this;
    }
}
