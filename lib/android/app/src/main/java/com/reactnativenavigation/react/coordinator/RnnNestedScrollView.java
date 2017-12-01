package com.reactnativenavigation.react.coordinator;


import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.ViewGroup;

import com.facebook.react.uimanager.MeasureSpecAssertions;

public class RnnNestedScrollView extends NestedScrollView {

	public RnnNestedScrollView(Context context) {
		super(context);

		CoordinatorLayout.LayoutParams params =
				new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		params.setBehavior(new AppBarLayout.ScrollingViewBehavior());
		setLayoutParams(params);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		MeasureSpecAssertions.assertExplicitMeasureSpec(widthMeasureSpec, heightMeasureSpec);

		setMeasuredDimension(
				MeasureSpec.getSize(widthMeasureSpec),
				MeasureSpec.getSize(heightMeasureSpec));
	}
}
