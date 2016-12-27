package com.reactnativenavigation.params.parsers;

import android.os.Bundle;

import com.reactnativenavigation.params.InterpolationParams;
import com.reactnativenavigation.params.LinearInterpolationParams;
import com.reactnativenavigation.params.PathInterpolationParams;
import com.reactnativenavigation.views.sharedElementTransition.ControlPoint;

public class InterpolationParser extends Parser {
    private Bundle params;

    private static final float[] defaultShowControlPoints = new float[]{0.5f, 1, 0, 0.5f};
    private static final float[] defaultHideControlPoints = new float[]{0.5f, 0, 1, 0.5f};

    public InterpolationParser(Bundle params) {
        this.params = params;
    }

    public InterpolationParams parseShowInterpolation() {
        return parse(params.getBundle("show"), defaultShowControlPoints);
    }

    public InterpolationParams parseHideInterpolation() {
        return parse(params.getBundle("hide"), defaultHideControlPoints);
    }

    private InterpolationParams parse(Bundle params, float[] defaultControlPoints) {
        InterpolationParams.Type type = InterpolationParams.Type.fromString(params.getString("type"));
        InterpolationParams result = InterpolationParams.Type.Path.equals(type) ?
                parsePathInterpolation(params, defaultControlPoints) :
                new LinearInterpolationParams();
        result.easing = InterpolationParams.Easing.fromString(params.getString("easing"));
        return result;
    }

    private InterpolationParams parsePathInterpolation(Bundle params, float[] defaultValues) {
        PathInterpolationParams result = new PathInterpolationParams();
        result.p1 = new ControlPoint(
                Float.valueOf(params.getString("controlX1")), defaultValues[0],
                Float.valueOf(params.getString("controlY1")), defaultValues[1]);
        result.p2 = new ControlPoint(
                Float.valueOf(params.getString("controlX2")), defaultValues[2],
                Float.valueOf(params.getString("controlY2")), defaultValues[3]
        );
        return result;
    }
}
