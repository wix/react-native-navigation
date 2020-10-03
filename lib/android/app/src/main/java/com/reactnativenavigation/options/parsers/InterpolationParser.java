package com.reactnativenavigation.options.parsers;


import android.animation.TimeInterpolator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import org.json.JSONObject;

public class InterpolationParser {
    public static TimeInterpolator parse(JSONObject json) {
        switch (json.optString("interpolation", "linear")) {
            case "decelerate":
                return new DecelerateInterpolator();
            case "accelerateDecelerate":
                return new AccelerateDecelerateInterpolator();
            case "accelerate":
                return new AccelerateInterpolator();
            case "overshoot":
                double tension = json.optDouble("tension", 1.0);
                return new OvershootInterpolator((float)tension);
            case "linear":
            default:
                return new LinearInterpolator();
        }
    }
}
