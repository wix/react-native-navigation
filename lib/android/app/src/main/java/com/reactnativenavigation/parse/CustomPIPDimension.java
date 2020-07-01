package com.reactnativenavigation.parse;

import androidx.annotation.CheckResult;

import org.json.JSONObject;

public class CustomPIPDimension {
    public Dimension compact = new Dimension();
    public Dimension expanded = new Dimension();

    public static CustomPIPDimension parse(JSONObject json) {
        CustomPIPDimension dimension = new CustomPIPDimension();
        dimension.compact = Dimension.parse(json.optJSONObject("compact"));
        dimension.expanded = Dimension.parse(json.optJSONObject("expanded"));
        return dimension;
    }

    @CheckResult
    public CustomPIPDimension copy() {
        CustomPIPDimension dimension = new CustomPIPDimension();
        dimension.mergeWith(this);
        return dimension;
    }

    @CheckResult
    public CustomPIPDimension mergeWith(CustomPIPDimension other) {
        this.compact.mergeWith(other.compact);
        this.expanded.mergeWith(other.expanded);
        return this;
    }
}
