package com.reactnativenavigation.parse;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.util.Property;
import android.view.View;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class AnimationOptions {

    public static AnimationOptions parse(JSONObject json) {
        AnimationOptions options = new AnimationOptions();
        if (json == null) return options;

        options.hasValue = true;
        for (Iterator<String> it = json.keys(); it.hasNext(); ) {
            String key = it.next();
            options.valueOptions.add(ValueAnimationOptions.parse(json.optJSONObject(key), getAnimProp(key)));
        }

        return options;
    }

    private boolean hasValue = false;

    private HashSet<ValueAnimationOptions> valueOptions = new HashSet<>();

    void mergeWith(AnimationOptions other) {
        if (other.hasValue()) {
            hasValue = true;
            valueOptions = other.valueOptions;
        }
    }

    void mergeWithDefault(AnimationOptions defaultOptions) {
        if (defaultOptions.hasValue()) {
            hasValue = true;
            valueOptions = defaultOptions.valueOptions;
        }
    }

    public boolean hasValue() {
        return hasValue;
    }

    public AnimatorSet getAnimation(View view) {
        AnimatorSet animationSet = new AnimatorSet();
        List<Animator> animators = new ArrayList<>();
        for (ValueAnimationOptions options: valueOptions) {
            animators.add(options.getAnimation(view));
        }
        animationSet.playTogether(animators);
        return animationSet;
    }

    private static Property<View, Float> getAnimProp(String key) {
        switch (key) {
            case "y":
                return View.TRANSLATION_Y;
            case "x":
                return View.TRANSLATION_X;
            case "alpha":
                return View.ALPHA;
            case "scaleY":
                return View.SCALE_Y;
            case "scaleX":
                return View.SCALE_X;
            case "rotationX":
                return View.ROTATION_X;
            case "rotationY":
                return View.ROTATION_Y;
            case "rotation":
                return View.ROTATION;
        }
        throw new IllegalArgumentException("This animation is not supported: " + key);
    }
}
