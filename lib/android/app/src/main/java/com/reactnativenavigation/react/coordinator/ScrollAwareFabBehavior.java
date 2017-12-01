package com.reactnativenavigation.react.coordinator;


import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;

public class ScrollAwareFabBehavior extends FloatingActionButton.Behavior {

	@Override
	public boolean onStartNestedScroll(final CoordinatorLayout coordinatorLayout, final FloatingActionButton child, final View directTargetChild, final View target, final int nestedScrollAxes) {
		return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL
				|| super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
	}

	@Override
	public void onNestedScroll(final CoordinatorLayout coordinatorLayout, final FloatingActionButton child, final View target, final int dxConsumed, final int dyConsumed, final int dxUnconsumed, final int dyUnconsumed) {
		super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
		if (dyConsumed > 0 && child.getVisibility() == View.VISIBLE) {
			child.hide();
		} else if (dyConsumed < 0 && child.getVisibility() != View.VISIBLE) {
			child.show();
		}
	}
}
