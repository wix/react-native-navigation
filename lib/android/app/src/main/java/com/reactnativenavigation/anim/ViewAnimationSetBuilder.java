package com.reactnativenavigation.anim;

import android.view.View;
import android.view.animation.Animation;

import java.util.ArrayList;
import java.util.List;

public class ViewAnimationSetBuilder implements Animation.AnimationListener {

    private Runnable onComplete;
    private List<View> views = new ArrayList<>();
    private List<Animation> pendingAnimations = new ArrayList<>();
    private boolean started = false;

    public ViewAnimationSetBuilder withEndListener(Runnable onComplete) {
        this.onComplete = onComplete;
        return this;
    }

    public ViewAnimationSetBuilder add(View view, Animation animation) {
        views.add(view);
        pendingAnimations.add(animation);
        animation.setAnimationListener(this);
        view.setAnimation(animation);
        return this;
    }

    public void start() {
        for (Animation animation : pendingAnimations) {
            animation.start();
        }
        started = true;
        if (pendingAnimations.isEmpty()) finish();
    }

    @Override
    public void onAnimationStart(final Animation animation) {
        // nothing
    }

    @Override
    public void onAnimationEnd(final Animation animation) {
        pendingAnimations.remove(animation);
        if (started && pendingAnimations.isEmpty()) finish();
    }

    @Override
    public void onAnimationRepeat(final Animation animation) {
        // nothing
    }

    private void finish() {
        for (View view : views) {
            view.clearAnimation();
        }
        views.clear();
        if (onComplete != null) onComplete.run();
    }
}
