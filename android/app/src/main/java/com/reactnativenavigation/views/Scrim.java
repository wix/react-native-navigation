package com.reactnativenavigation.views;

import android.content.Context;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import com.reactnativenavigation.params.StyleParams;

import static com.reactnativenavigation.views.Scrim.State.Invisible;
import static com.reactnativenavigation.views.Scrim.State.Visible;

public class Scrim extends View {
    enum State {Visible, Invisible}

    private State state = Invisible;
    private final float collapseThreshold;
    private final static int ANIMATION_DURATION = 600;
    private final Interpolator interpolator;

    public Scrim(Context context, StyleParams.Color color, float collapseThreshold) {
        super(context);
        this.collapseThreshold = collapseThreshold;
        setBackgroundColor(color.getColor());
        setAlpha(0);
        interpolator = new DecelerateInterpolator();
    }

    public void collapse(float collapse) {
        if (shouldShowScrim(collapse)) {
            showScrim();
        } else if (shouldHideScrim(collapse)) {
            hideScrim();
        }

    }

    private boolean shouldShowScrim(float collapse) {
        return Math.abs(collapse) >= collapseThreshold && state == Invisible;
    }

    private boolean shouldHideScrim(float collapse) {
        return Math.abs(collapse) < collapseThreshold && state == Visible;
    }

    private void showScrim() {
        state = Visible;
        animate()
                .alpha(1)
                .setDuration(ANIMATION_DURATION)
                .setInterpolator(interpolator);
    }

    private void hideScrim() {
        state = Invisible;
        animate()
                .alpha(0)
                .setDuration(ANIMATION_DURATION)
                .setInterpolator(interpolator);
    }
}
