package com.reactnativenavigation.mocks;

import android.view.View;

import com.reactnativenavigation.anim.NavigationAnimator;

import org.robolectric.RuntimeEnvironment;

public class TestNavigationAnimator extends NavigationAnimator {

	public TestNavigationAnimator() {
		super(RuntimeEnvironment.application);
	}

	@Override
	public void animatePush(final View enteringView, NavigationAnimationListener animationListener) {
		if (animationListener != null) animationListener.onAnimationEnd();
	}

	@Override
	public void animatePop(final View enteringView, NavigationAnimationListener animationListener) {
		if (animationListener != null) animationListener.onAnimationEnd();
	}
}
