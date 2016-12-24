package com.reactnativenavigation.params.parsers;

import android.os.Bundle;

import com.reactnativenavigation.params.InterpolationParams;
import com.reactnativenavigation.params.LinearInterpolationParams;
import com.reactnativenavigation.params.PathInterpolationParams;

public class InterpolationParser extends Parser {
    private Bundle params;

    public InterpolationParser(Bundle params) {
        this.params = params;
    }

    public InterpolationParams parse() {
        InterpolationParams.Type type = InterpolationParams.Type.fromString(params.getString("type"));
        if (InterpolationParams.Type.Path.equals(type)) {
            return parsePathInterpolation();
        }
        return new LinearInterpolationParams();
    }

    private InterpolationParams parsePathInterpolation() {
        PathInterpolationParams result = new PathInterpolationParams();
        result.controlX1 = Float.valueOf(params.getString("controlX1"));
        result.controlY1 = Float.valueOf(params.getString("controlY1"));
        result.controlX2 = Float.valueOf(params.getString("controlX2"));
        result.controlY2 = Float.valueOf(params.getString("controlY2"));
        return result;
    }
}
