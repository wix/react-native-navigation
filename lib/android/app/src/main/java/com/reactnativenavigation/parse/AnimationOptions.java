package com.reactnativenavigation.parse;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.util.Property;
import android.view.View;

import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.parse.params.NullBool;
import com.reactnativenavigation.parse.params.NullText;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.parse.parsers.BoolParser;
import com.reactnativenavigation.parse.parsers.TextParser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class AnimationOptions {

    public static AnimationOptions parse(JSONObject json) {
        AnimationOptions options = new AnimationOptions();
        if (json == null) return options;

        for (Iterator<String> it = json.keys(); it.hasNext(); ) {
            String key = it.next();
            switch (key) {
                case "id":
                    options.id = TextParser.parse(json, key);
                    break;
                case "enable":
                    options.enable = BoolParser.parse(json, key);
                    break;
                case "waitForRender":
                    options.waitForRender = BoolParser.parse(json, key);
                    break;
                default:
                    options.valueOptions.add(ValueAnimationOptions.parse(json.optJSONObject(key), getAnimProp(key)));
            }
        }

        return options;
    }

    public Text id = new NullText();
    public Bool enable = new NullBool();
    public Bool waitForRender = new NullBool();
    private HashSet<ValueAnimationOptions> valueOptions = new HashSet<>();

    void mergeWith(AnimationOptions other) {
        if (other.id.hasValue()) id = other.id;
        if (other.enable.hasValue()) enable = other.enable;
        if (other.waitForRender.hasValue()) waitForRender = other.waitForRender;
        if (!other.valueOptions.isEmpty()) valueOptions = other.valueOptions;
    }

    void mergeWithDefault(AnimationOptions defaultOptions) {
        if (!id.hasValue()) id = defaultOptions.id;
        if (!enable.hasValue()) enable = defaultOptions.enable;
        if (!waitForRender.hasValue()) waitForRender = defaultOptions.waitForRender;
        if (valueOptions.isEmpty()) valueOptions = defaultOptions.valueOptions;
    }

    public boolean hasValue() {
        return id.hasValue() || enable.hasValue() || waitForRender.hasValue();
    }

    public AnimatorSet getAnimation(View view) {
        return getAnimation(view, null);
    }

    public AnimatorSet getAnimation(View view, AnimatorSet defaultAnimation) {
        if (!hasAnimation()) return defaultAnimation;
        AnimatorSet animationSet = new AnimatorSet();
        List<Animator> animators = new ArrayList<>();
        for (ValueAnimationOptions options : valueOptions) {
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

    public boolean hasAnimation() {
        return !valueOptions.isEmpty();
    }
}
