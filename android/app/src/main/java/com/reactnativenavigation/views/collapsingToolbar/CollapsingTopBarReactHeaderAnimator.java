package com.reactnativenavigation.views.collapsingToolbar;

import android.util.Log;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import static com.reactnativenavigation.views.collapsingToolbar.CollapsingTopBarReactHeaderAnimator.State.Invisible;
import static com.reactnativenavigation.views.collapsingToolbar.CollapsingTopBarReactHeaderAnimator.State.Visible;

class CollapsingTopBarReactHeaderAnimator {
    interface OnVisibleListener {
        void onVisible();
    }
    interface OnHiddenListener {
        void onHidden();
    }
    enum State {Visible, Invisible}

    private State state = Invisible;
    private CollapsingTopBarReactHeader header;
    private final float hideThreshold;
    private float showThreshold;
    private final static int ANIMATION_DURATION = 360;
    private final Interpolator interpolator = new DecelerateInterpolator();
    private OnVisibleListener onVisibleListener;
    private OnHiddenListener onHiddenListener;

    void setOnVisibleListener(OnVisibleListener onVisibleListener) {
        this.onVisibleListener = onVisibleListener;
    }

    void setOnHiddenListener(OnHiddenListener onHiddenListener) {
        this.onHiddenListener = onHiddenListener;
    }

    CollapsingTopBarReactHeaderAnimator(CollapsingTopBarReactHeader header, float hideThreshold, float showThreshold) {
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
        Log.i("shouldShow", "collapse: " + collapse + "[" + showThreshold + "]");
        return Math.abs(collapse) < showThreshold && state == Invisible;
    }

    private boolean shouldHide(float collapse) {
        Log.i("shouldHide", "collapse: " + collapse + "[" + hideThreshold + "]");
        return Math.abs(collapse) >= hideThreshold && state == Visible;
    }

    private void show() {
        if (state == Invisible && onVisibleListener != null) {
            onVisibleListener.onVisible();
        }
        state = State.Visible;
        header.animate()
                .alpha(1)
                .setDuration(ANIMATION_DURATION)
                .setInterpolator(interpolator);
    }

    private void hide() {
        if (state == Visible && onHiddenListener != null) {
            onHiddenListener.onHidden();
        }
        state = State.Invisible;
        header.animate()
                .alpha(0)
                .setDuration(ANIMATION_DURATION)
                .setInterpolator(interpolator);
    }
}
