package com.reactnativenavigation.parse;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AnimationOptions {

    public static AnimationOptions parse(JSONObject json) {
        AnimationOptions options = new AnimationOptions();
        if (json == null) return options;

        options.hasValue = true;

        options.y = ValueAnimationOptions.parse(json.optJSONObject("y"));
        options.x = ValueAnimationOptions.parse(json.optJSONObject("x"));
        options.rotationX = ValueAnimationOptions.parse(json.optJSONObject("rotationX"));
        options.rotationY = ValueAnimationOptions.parse(json.optJSONObject("rotationY"));
        options.rotation = ValueAnimationOptions.parse(json.optJSONObject("rotation"));
        options.alpha = ValueAnimationOptions.parse(json.optJSONObject("alpha"));
        options.scaleX = ValueAnimationOptions.parse(json.optJSONObject("scaleX"));
        options.scaleY = ValueAnimationOptions.parse(json.optJSONObject("scaleY"));

        return options;
    }

    private boolean hasValue = false;

    public ValueAnimationOptions y = new ValueAnimationOptions();
    public ValueAnimationOptions x = new ValueAnimationOptions();
    public ValueAnimationOptions rotationX = new ValueAnimationOptions();
    public ValueAnimationOptions rotationY = new ValueAnimationOptions();
    public ValueAnimationOptions rotation = new ValueAnimationOptions();
    public ValueAnimationOptions alpha = new ValueAnimationOptions();
    public ValueAnimationOptions scaleY = new ValueAnimationOptions();
    public ValueAnimationOptions scaleX = new ValueAnimationOptions();

    void mergeWith(AnimationOptions other) {
        if (other.hasValue()) {
            hasValue = true;
        }
        y.mergeWith(other.y);
        x.mergeWith(other.x);
        alpha.mergeWith(other.alpha);
        scaleY.mergeWith(other.scaleY);
        scaleX.mergeWith(other.scaleX);
        rotationX.mergeWith(other.rotationX);
        rotationY.mergeWith(other.rotationY);
        rotation.mergeWith(other.rotation);
    }

    void mergeWithDefault(AnimationOptions defaultOptions) {
        if (defaultOptions.hasValue()) {
            hasValue = true;
        }
        y.mergeWithDefault(defaultOptions.y);
        x.mergeWithDefault(defaultOptions.x);
        alpha.mergeWithDefault(defaultOptions.alpha);
        scaleX.mergeWithDefault(defaultOptions.scaleX);
        scaleY.mergeWithDefault(defaultOptions.scaleY);
        rotationX.mergeWithDefault(defaultOptions.rotationX);
        rotationY.mergeWithDefault(defaultOptions.rotationY);
        rotation.mergeWithDefault(defaultOptions.rotation);
    }

    public boolean hasValue() {
        return hasValue;
    }

    public AnimatorSet getAnimation(View view) {
        AnimatorSet animationSet = new AnimatorSet();
        List<Animator> animators = new ArrayList<>();
        if (this.alpha.hasValue()) {
            ObjectAnimator alpha = ObjectAnimator.ofFloat(view, View.ALPHA, this.alpha.from.get(), this.alpha.to.get());
            this.alpha.setUpAnimator(alpha);
            animators.add(alpha);
        }
        if (this.y.hasValue()) {
            ObjectAnimator y = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, this.y.from.get(), this.y.to.get());
            this.alpha.setUpAnimator(y);
            animators.add(y);
        }
        if (this.x.hasValue()) {
            ObjectAnimator x = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, this.x.from.get(), this.x.to.get());
            this.x.setUpAnimator(x);
            animators.add(x);
        }
        if (this.scaleY.hasValue()) {
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, View.SCALE_Y, this.scaleY.from.get(), this.scaleY.to.get());
            this.scaleY.setUpAnimator(scaleY);
            animators.add(scaleY);
        }
        if (this.scaleX.hasValue()) {
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, View.SCALE_X, this.scaleX.from.get(), this.scaleX.to.get());
            this.scaleX.setUpAnimator(scaleX);
            animators.add(scaleX);
        }
        if (this.rotationX.hasValue()) {
            ObjectAnimator rotationX = ObjectAnimator.ofFloat(view, View.ROTATION_X, this.rotationX.from.get(), this.rotationX.to.get());
            this.rotationX.setUpAnimator(rotationX);
            animators.add(rotationX);
        }
        if (this.rotationY.hasValue()) {
            ObjectAnimator rotationY = ObjectAnimator.ofFloat(view, View.ROTATION_Y, this.rotationY.from.get(), this.rotationY.to.get());
            this.rotationY.setUpAnimator(rotationY);
            animators.add(rotationY);
        }
        if (this.rotation.hasValue()) {
            ObjectAnimator rotation = ObjectAnimator.ofFloat(view, View.ROTATION, this.rotation.from.get(), this.rotation.to.get());
            this.rotationY.setUpAnimator(rotation);
            animators.add(rotation);
        }

        animationSet.playTogether(animators);
        return animationSet;
    }
}
