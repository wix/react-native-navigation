package com.reactnativenavigation.options;

import androidx.annotation.CheckResult;

import org.json.JSONObject;

public class CustomPIPDimension {
    public Dimension compact = new Dimension();
    public Dimension expanded = new Dimension();

    public static CustomPIPDimension parse(JSONObject json) {
        CustomPIPDimension dimension = new CustomPIPDimension();
        if (json != null) {
            dimension.compact = Dimension.parse(json.optJSONObject("compact"));
            dimension.expanded = Dimension.parse(json.optJSONObject("expanded"));
        } else {
            dimension.compact = new Dimension();
            dimension.expanded = new Dimension();
        }
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
