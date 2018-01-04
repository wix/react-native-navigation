package com.reactnativenavigation.anim;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.reactnativenavigation.views.TopBar;

@SuppressWarnings("ResourceType")
public class StackAnimator {

	public interface StackAnimationListener {
		void onAnimationEnd();
	}

	private static final int DURATION = 300;
	private static final int DURATION_TOPBAR = 300;
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
		if (windowManager != null) {
			windowManager.getDefaultDisplay().getMetrics(metrics);
		}
		return metrics.heightPixels;
	}

    public void animateShowTopBar(final TopBar topBar, final View container) {
        animateShowTopBar(topBar, container, -1 * topBar.getMeasuredHeight());
    }

    public void animateShowTopBar(final TopBar topBar, final View container, float startTranslation) {
        ObjectAnimator topbarAnim = ObjectAnimator.ofFloat(topBar, View.TRANSLATION_Y, startTranslation, 0);
        topbarAnim.setInterpolator(DECELERATE_INTERPOLATOR);
        topbarAnim.setDuration(DURATION_TOPBAR);

		topbarAnim.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {
				topBar.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				//TODO: needs refactoring
				if (container != null) {
					ViewGroup.LayoutParams layoutParams = container.getLayoutParams();
					layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
					container.setLayoutParams(layoutParams);
				}
			}

			@Override
			public void onAnimationCancel(Animator animation) {

			}

			@Override
			public void onAnimationRepeat(Animator animation) {

            }
        });
        topbarAnim.start();
    }

    public void animateHideTopBar(final TopBar topBar, final View container) {
        animateHideTopBar(topBar, container, 0);
    }

    public void animateHideTopBar(final TopBar topBar, final View container, float startTranslation) {
        ObjectAnimator topbarAnim = ObjectAnimator.ofFloat(topBar, View.TRANSLATION_Y, startTranslation, -1 * topBar.getMeasuredHeight());
        topbarAnim.setInterpolator(ACCELERATE_INTERPOLATOR);
        topbarAnim.setDuration(DURATION_TOPBAR);

		topbarAnim.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				//TODO: needs refactoring
				if (container != null) {
					ViewGroup.LayoutParams layoutParams = container.getLayoutParams();
					layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
					container.setLayoutParams(layoutParams);
				}

				topBar.setVisibility(View.GONE);
			}

			@Override
			public void onAnimationCancel(Animator animation) {

			}

			@Override
			public void onAnimationRepeat(Animator animation) {

            }
        });
        topbarAnim.start();
    }
}
