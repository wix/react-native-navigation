package com.reactnativenavigation.mocks;

import android.support.annotation.Nullable;
import android.view.View;

import com.reactnativenavigation.anim.StackAnimator;

import org.robolectric.RuntimeEnvironment;

public class TestStackAnimator extends StackAnimator {

	public TestStackAnimator() {
		super(RuntimeEnvironment.application);
	}

	@Override
	public void animatePush(final View enteringView, final View exitingView, final Runnable onComplete) {
		if (onComplete != null) onComplete.run();
	}

	@Override
	public void animatePop(final View enteringView, final View exitingView, @Nullable final Runnable onComplete) {
		if (onComplete != null) onComplete.run();
	}
}
