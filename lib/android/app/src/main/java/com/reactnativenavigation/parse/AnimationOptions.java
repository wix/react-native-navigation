package com.reactnativenavigation.parse;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.util.Property;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.View;

import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.parse.params.Fraction;
import com.reactnativenavigation.parse.params.NullBool;
import com.reactnativenavigation.parse.params.NullFraction;
import com.reactnativenavigation.parse.params.NullText;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.parse.parsers.BoolParser;
import com.reactnativenavigation.parse.parsers.FractionParser;
import com.reactnativenavigation.parse.parsers.TextParser;
import com.reactnativenavigation.R;

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
                case "enabled":
                    options.enabled = BoolParser.parse(json, key);
                    break;
                case "waitForRender":
                    options.waitForRender = BoolParser.parse(json, key);
                    break;
                case "enableDeck":
                    options.enableDeck = BoolParser.parse(json, key);
                    break;
                case "deckPresentDuration":
                    options.deckPresentDuration = FractionParser.parse(json, key);
                    break;
                case "deckDismissDuration":
                    options.deckDismissDuration = FractionParser.parse(json, key);
                    break;
                case "enableDeckSwipeToDismiss":
                    // Swipe-to-dismiss is currently only supported on IOS
                    break;
                default:
                    options.valueOptions.add(ValueAnimationOptions.parse(json.optJSONObject(key), getAnimProp(key)));
            }
        }

        return options;
    }

    public Text id = new NullText();
    public Bool enabled = new NullBool();
    public Bool waitForRender = new NullBool();
    public Bool enableDeck = new NullBool();
    public Fraction deckPresentDuration = new NullFraction();
    public Fraction deckDismissDuration = new NullFraction();
    private HashSet<ValueAnimationOptions> valueOptions = new HashSet<>();

    void mergeWith(AnimationOptions other) {
        if (other.id.hasValue()) id = other.id;
        if (other.enabled.hasValue()) enabled = other.enabled;
        if (other.waitForRender.hasValue()) waitForRender = other.waitForRender;
        if (other.enableDeck.hasValue()) enableDeck = other.enableDeck;
        if (other.deckPresentDuration.hasValue()) deckPresentDuration = other.deckPresentDuration;
        if (other.deckDismissDuration.hasValue()) deckDismissDuration = other.deckDismissDuration;
        if (!other.valueOptions.isEmpty()) valueOptions = other.valueOptions;
    }

    void mergeWithDefault(AnimationOptions defaultOptions) {
        if (!id.hasValue()) id = defaultOptions.id;
        if (!enabled.hasValue()) enabled = defaultOptions.enabled;
        if (!waitForRender.hasValue()) waitForRender = defaultOptions.waitForRender;
        if (!enableDeck.hasValue()) enableDeck = defaultOptions.enableDeck;
        if (!deckPresentDuration.hasValue()) deckPresentDuration = defaultOptions.deckPresentDuration;
        if (!deckDismissDuration.hasValue()) deckDismissDuration = defaultOptions.deckDismissDuration;
        if (valueOptions.isEmpty()) valueOptions = defaultOptions.valueOptions;
    }

    public boolean hasValue() {
        return id.hasValue() || enabled.hasValue() || waitForRender.hasValue() || enableDeck.hasValue() || deckPresentDuration.hasValue() || deckDismissDuration.hasValue();
    }

    public AnimatorSet getAnimation(View view) {
        return getAnimation(view, null);
    }

    public AnimatorSet getAnimation(View view, AnimatorSet defaultAnimation) {
        return getAnimation(view, null, defaultAnimation, false);
    }

    public AnimatorSet getAnimation(View view, View otherView, AnimatorSet defaultAnimation, boolean isReverse) {
        if (!hasAnimation()) return defaultAnimation;
        AnimatorSet animationSet = new AnimatorSet();
        List<Animator> animators = new ArrayList<>();
        for (ValueAnimationOptions options : valueOptions) {
            animators.add(options.getAnimation(view));
        }

        if (enableDeck.isTrue()) {
            int duration = (int) ((deckPresentDuration.hasValue() ? deckPresentDuration.get() : 0.3f) * 1000);
            float screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

            float topMargin = screenHeight * 0.05f;
            float fromY = isReverse ? topMargin : screenHeight;
            float toY = isReverse ? screenHeight : topMargin;

            ObjectAnimator modalPosition = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, fromY, toY);
            modalPosition.setStartDelay(0);
            modalPosition.setDuration(duration);
            modalPosition.setInterpolator(new AccelerateDecelerateInterpolator()); // Mimic DeckTransition interpolator - curveEaseOut

            animators.add(modalPosition);

            if (otherView != null) {
                float toScale = isReverse ? 1.0f : 0.95f;

                ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(otherView, View.SCALE_X, toScale);
                scaleXAnimator.setStartDelay(0);
                scaleXAnimator.setDuration(duration);
                scaleXAnimator.setInterpolator(new AccelerateDecelerateInterpolator()); // Mimic DeckTransition interpolator - curveEaseOut
                animators.add(scaleXAnimator);

                ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(otherView, View.SCALE_Y, toScale);
                scaleYAnimator.setStartDelay(0);
                scaleYAnimator.setDuration(duration);
                scaleYAnimator.setInterpolator(new AccelerateDecelerateInterpolator()); // Mimic DeckTransition interpolator - curveEaseOut
                animators.add(scaleYAnimator);

                Object viewTag = view.getTag(R.id.deckTransitionDepth);
                Object otherTag = otherView.getTag(R.id.deckTransitionDepth);

                int stackDepth = otherTag != null ? (int) otherTag : 0;

                // Set the shown/hidden views stack depth
                if (isReverse) {
                    view.setTag(R.id.deckTransitionDepth, 0);
                } else {
                    view.setTag(R.id.deckTransitionDepth, stackDepth + 1);
                }

                // Reset position of other view when new Depth is zero
                boolean resetOtherPosition = isReverse && stackDepth <= 0;

                if (isReverse) {
                    ObjectAnimator otherPos = ObjectAnimator.ofFloat(otherView, View.TRANSLATION_Y, !resetOtherPosition ? topMargin : 0.0f);
                    otherPos.setStartDelay(0);
                    otherPos.setDuration(duration);
                    otherPos.setInterpolator(new AccelerateDecelerateInterpolator()); // Mimic DeckTransition interpolator - curveEaseOut
                    animators.add(otherPos);

                    ObjectAnimator otherAlpha = ObjectAnimator.ofFloat(otherView, View.ALPHA, 1.0f);
                    otherAlpha.setStartDelay(0);
                    otherAlpha.setDuration(duration);
                    otherAlpha.setInterpolator(new AccelerateDecelerateInterpolator()); // Mimic DeckTransition interpolator - curveEaseOut
                    animators.add(otherAlpha);
                } else {
                    ObjectAnimator otherPos = ObjectAnimator.ofFloat(otherView, View.TRANSLATION_Y, 0.0f);
                    otherPos.setStartDelay(0);
                    otherPos.setDuration(duration);
                    otherPos.setInterpolator(new AccelerateDecelerateInterpolator()); // Mimic DeckTransition interpolator - curveEaseOut
                    animators.add(otherPos);

                    ObjectAnimator otherAlpha = ObjectAnimator.ofFloat(otherView, View.ALPHA, 0.5f);
                    otherAlpha.setStartDelay(0);
                    otherAlpha.setDuration(duration);
                    otherAlpha.setInterpolator(new AccelerateDecelerateInterpolator()); // Mimic DeckTransition interpolator - curveEaseOut
                    animators.add(otherAlpha);
                }
            }
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
        return !valueOptions.isEmpty() || enableDeck.isTrue();
    }
}
