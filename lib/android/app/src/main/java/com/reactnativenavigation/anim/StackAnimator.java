package com.reactnativenavigation.anim;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

@SuppressWarnings("ResourceType")
public class StackAnimator {

	public interface StackAnimationListener {
		void onAnimationEnd();
	}

	private static final int DURATION = 300;
	private static final int START_DELAY = 100;
	private static final DecelerateInterpolator DECELERATE_INTERPOLATOR = new DecelerateInterpolator();
	private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
	private float translationY;

	public StackAnimator(Context context) {
		translationY = getWindowHeight(context);
	}

	public void animatePush(final View view, @Nullable final StackAnimationListener animationListener) {
		view.setVisibility(View.INVISIBLE);
		ObjectAnimator alpha = ObjectAnimator.ofFloat(view, View.ALPHA, 0, 1);
		alpha.setInterpolator(DECELERATE_INTERPOLATOR);

		AnimatorSet set = new AnimatorSet();
		set.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {
				view.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				if (animationListener != null) {
					animationListener.onAnimationEnd();
				}
			}

			@Override
			public void onAnimationCancel(Animator animation) {

			}

			@Override
			public void onAnimationRepeat(Animator animation) {

			}
		});
		ObjectAnimator translationY = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, this.translationY, 0);
		translationY.setInterpolator(DECELERATE_INTERPOLATOR);
		translationY.setDuration(DURATION);
		alpha.setDuration(DURATION);
		set.playTogether(translationY, alpha);
		set.start();
	}

	public void animatePop(View view, @Nullable final StackAnimationListener animationListener) {
		ObjectAnimator alpha = ObjectAnimator.ofFloat(view, View.ALPHA, 1, 0);
		alpha.setInterpolator(ACCELERATE_INTERPOLATOR);

		AnimatorSet set = new AnimatorSet();
		set.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {

			}

			@Override
			public void onAnimationEnd(Animator animation) {
				if (animationListener != null) {
					animationListener.onAnimationEnd();
				}
			}

			@Override
			public void onAnimationCancel(Animator animation) {

			}

			@Override
			public void onAnimationRepeat(Animator animation) {

			}
		});
		ObjectAnimator translationY = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, 0, this.translationY);
		translationY.setInterpolator(ACCELERATE_INTERPOLATOR);
		translationY.setDuration(DURATION);
		alpha.setDuration(DURATION);
		set.playTogether(translationY, alpha);
		set.start();
	}

	private float getWindowHeight(Context context) {
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(metrics);
		return metrics.heightPixels;
	}


}
