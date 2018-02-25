package com.reactnativenavigation.mocks;

import android.view.*;

import com.reactnativenavigation.anim.*;

import org.robolectric.*;

public class TestNavigationAnimator extends NavigationAnimator {

    public TestNavigationAnimator() {
        super(RuntimeEnvironment.application);
    }

    @Override
    public void animatePush(final View enteringView, AnimationListener animationListener) {
        if (animationListener != null) animationListener.onAnimationEnd();
    }

    @Override
    public void animatePop(final View enteringView, AnimationListener animationListener) {
        if (animationListener != null) animationListener.onAnimationEnd();
    }
}
