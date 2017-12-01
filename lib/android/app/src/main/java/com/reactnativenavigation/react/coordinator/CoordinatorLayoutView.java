package com.reactnativenavigation.react.coordinator;


import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class CoordinatorLayoutView extends CoordinatorLayout {

	public CoordinatorLayoutView(Context context) {
		super(context);
		LayoutParams params =
				new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		params.setBehavior(new AppBarLayout.ScrollingViewBehavior());
		this.setLayoutParams(params);
		this.setFitsSystemWindows(false);
	}

	/**
	 * Measuring fix. Based on this issue - https://github.com/cesardeazevedo/react-native-bottom-sheet-behavior/issues/21
	 */
	private final Runnable measureAndLayout = new Runnable() {
		@Override
		public void run() {
			measure(
					MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.EXACTLY),
					MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.EXACTLY));
			layout(getLeft(), getTop(), getRight(), getBottom());
		}
	};

	@Override
	public void requestLayout() {
		super.requestLayout();
		post(measureAndLayout);
	}
}
