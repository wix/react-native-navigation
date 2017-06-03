package com.reactnativenavigation.anim;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

@SuppressWarnings("ResourceType")
public class StackAnimator {

	private static int androidOpenEnterAnimResId;
	private static int androidOpenExitAnimResId;
	private static int androidCloseEnterAnimResId;
	private static int androidCloseExitAnimResId;
	private final Context context;

	public StackAnimator(Context context) {
		this.context = context;
		loadResIfNeeded(context);
	}

	private void loadResIfNeeded(Context context) {
		if (androidOpenEnterAnimResId > 0) return;

		int[] attrs = {android.R.attr.activityOpenEnterAnimation, android.R.attr.activityOpenExitAnimation, android.R.attr.activityCloseEnterAnimation, android.R.attr.activityCloseExitAnimation};
		TypedArray typedArray = context.obtainStyledAttributes(android.R.style.Animation_Activity, attrs);
		androidOpenEnterAnimResId = typedArray.getResourceId(0, -1);
		androidOpenExitAnimResId = typedArray.getResourceId(1, -1);
		androidCloseEnterAnimResId = typedArray.getResourceId(2, -1);
		androidCloseExitAnimResId = typedArray.getResourceId(3, -1);
		typedArray.recycle();
	}

	public void animatePush(final View enteringView, final View exitingView, @Nullable final Runnable onComplete) {
		Animation enterAnim = AnimationUtils.loadAnimation(context, androidOpenEnterAnimResId);
		Animation exitAnim = AnimationUtils.loadAnimation(context, androidOpenExitAnimResId);

		new ViewAnimationSetBuilder()
				.withEndListener(onComplete)
				.add(enteringView, enterAnim)
				.add(exitingView, exitAnim)
				.start();
	}

	public void animatePop(final View enteringView, final View exitingView, @Nullable final Runnable onComplete) {
		Animation enterAnim = AnimationUtils.loadAnimation(context, androidCloseEnterAnimResId);
		Animation exitAnim = AnimationUtils.loadAnimation(context, androidCloseExitAnimResId);

		new ViewAnimationSetBuilder()
				.withEndListener(onComplete)
				.add(enteringView, enterAnim)
				.add(exitingView, exitAnim)
				.start();
	}
}
