package com.reactnativenavigation.parse;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.util.Property;
import android.view.View;

import com.reactnativenavigation.parse.params.FloatParam;
import com.reactnativenavigation.parse.params.Interpolation;
import com.reactnativenavigation.parse.params.NullFloatParam;
import com.reactnativenavigation.parse.params.NullNumber;
import com.reactnativenavigation.parse.params.Number;
import com.reactnativenavigation.parse.parsers.FloatParser;
import com.reactnativenavigation.parse.parsers.InterpolationParser;
import com.reactnativenavigation.parse.parsers.NumberParser;

import org.json.JSONObject;

public class ValueAnimationOptions {

    public static ValueAnimationOptions parse(JSONObject json, Property<View, Float> property) {
        ValueAnimationOptions options = new ValueAnimationOptions();

        options.animProp = property;
        options.from = FloatParser.parse(json, "from");
        options.to = FloatParser.parse(json, "to");
        options.duration = NumberParser.parse(json, "duration");
        options.startDelay = NumberParser.parse(json, "startDelay");
        options.interpolation = InterpolationParser.parse(json, "interpolation");

        return options;
    }

    private Property<View, Float> animProp;

    private FloatParam from = new NullFloatParam();
    private FloatParam fromDelta = new FloatParam(0f);
    private FloatParam to = new NullFloatParam();
    private FloatParam toDelta = new FloatParam(0f);
    private Number duration = new NullNumber();
    private Number startDelay = new NullNumber();
    private Interpolation interpolation = Interpolation.NO_VALUE;

    void setFromDelta(float fromDelta) {
        this.fromDelta = new FloatParam(fromDelta);
    }

    void setToDelta(float toDelta) {
        this.toDelta = new FloatParam(toDelta);
    }

    Animator getAnimation(View view) {
        if (!from.hasValue() || !to.hasValue()) throw new IllegalArgumentException("Params 'from' and 'to' are mandatory");
        ObjectAnimator animator = ObjectAnimator.ofFloat(view,
                animProp,
                from.get() + fromDelta.get(),
                to.get() + toDelta.get()
        );
        animator.setInterpolator(interpolation.getInterpolator());
        if (duration.hasValue()) animator.setDuration(duration.get());
        if (startDelay.hasValue()) animator.setStartDelay(startDelay.get());
        return animator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return animProp.equals(((ValueAnimationOptions) o).animProp);
    }

    public boolean equals(Property<View, Float> animationProperty) {
        return animProp.getName().equals(animationProperty.getName());
    }

    @Override
    public int hashCode() {
        return animProp.hashCode();
    }
}
