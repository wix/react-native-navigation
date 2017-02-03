package com.reactnativenavigation.views.collapsingToolbar;

import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import static com.reactnativenavigation.views.collapsingToolbar.CollapsingTopBarReactHeaderAnimator.State.Invisible;
import static com.reactnativenavigation.views.collapsingToolbar.CollapsingTopBarReactHeaderAnimator.State.Visible;

public class CollapsingTopBarReactHeaderAnimator {
    enum State {Visible, Invisible}

    private State state = Invisible;
    private CollapsingTopBarReactHeader header;
    private final float hideThreshold;
    private float showThreshold;
    private final static int ANIMATION_DURATION = 360;
    private final Interpolator interpolator = new DecelerateInterpolator();

    public CollapsingTopBarReactHeaderAnimator(CollapsingTopBarReactHeader header, float hideThreshold, float showThreshold) {
        this.header = header;
        this.hideThreshold = hideThreshold;
        this.showThreshold = showThreshold;
    }

    public void collapse(float collapse) {
        if (shouldShow(collapse)) {
            show();
        } else if (shouldHide(collapse)) {
            hide();
        }
    }

    private boolean shouldShow(float collapse) {
        return Math.abs(collapse) < showThreshold && state == Invisible;
    }

    private boolean shouldHide(float collapse) {
        return Math.abs(collapse) >= hideThreshold && state == Visible;
    }

    private void show() {
        state = State.Visible;
        header.animate()
                .alpha(1)
                .setDuration(ANIMATION_DURATION)
                .setInterpolator(interpolator);
    }

    private void hide() {
        state = State.Invisible;
        header.animate()
                .alpha(0)
                .setDuration(ANIMATION_DURATION)
                .setInterpolator(interpolator);
    }
}
