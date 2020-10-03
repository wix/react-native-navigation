package com.reactnativenavigation.options.parsers;


import android.animation.TimeInterpolator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import com.reactnativenavigation.options.interpolators.SpringInterpolator;

import org.json.JSONObject;

public class InterpolationParser {
    public static TimeInterpolator parse(JSONObject json) {
        JSONObject interpolation = json.optJSONObject("interpolation");
        String type = interpolation == null ? "linear" : interpolation.optString("type", "linear");
        switch (type) {
            case "decelerate":
                return new DecelerateInterpolator();
            case "accelerateDecelerate":
                return new AccelerateDecelerateInterpolator();
            case "accelerate":
                return new AccelerateInterpolator();
            case "overshoot":
                double tension = interpolation.optDouble("tension", 1.0);
                return new OvershootInterpolator((float)tension);
            case "spring":
                float mass = (float)interpolation.optDouble("mass", 3.0);
                float damping = (float)interpolation.optDouble("damping", 500.0);
                float stiffness = (float)interpolation.optDouble("stiffness", 1000.0);
                boolean allowsOverdamping = interpolation.optBoolean("allowsOverdamping", false);
                float initialVelocity = (float)interpolation.optDouble("initialVelocity", 0);
                return new SpringInterpolator(mass, damping, stiffness, allowsOverdamping, initialVelocity);
            case "linear":
            default:
                return new LinearInterpolator();
        }
    }
}
