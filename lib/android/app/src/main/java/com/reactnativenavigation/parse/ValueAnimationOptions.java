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
    private FloatParam to = new NullFloatParam();
    private Number duration = new NullNumber();
    private Number startDelay = new NullNumber();
    private Interpolation interpolation = Interpolation.NO_VALUE;

    Animator getAnimation(View view) {
        if (!this.from.hasValue() || !this.to.hasValue())
            throw new IllegalArgumentException("Params 'from' and 'to' are mandatory");
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, animProp, this.from.get(), this.to.get());
        animator.setInterpolator(this.interpolation.getInterpolator());
        if (this.duration.hasValue())
            animator.setDuration(this.duration.get());
        if (this.startDelay.hasValue())
            animator.setStartDelay(this.startDelay.get());
        return animator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ValueAnimationOptions options = (ValueAnimationOptions) o;

        return animProp.equals(options.animProp);
    }

    @Override
    public int hashCode() {
        return animProp.hashCode();
    }
}
