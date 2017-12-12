package com.reactnativenavigation.presentation;

import android.view.View;

import com.reactnativenavigation.anim.StackAnimator;
import com.reactnativenavigation.parse.NavigationOptions;
import com.reactnativenavigation.utils.TypefaceLoader;
import com.reactnativenavigation.viewcontrollers.ContainerViewController;
import com.reactnativenavigation.views.ContainerView;

public class OptionsPresenter {

	private ContainerViewController controller;
	private final StackAnimator animator;

	public OptionsPresenter(ContainerViewController controller) {
		this.controller = controller;
		animator = new StackAnimator(controller.getActivity());
	}

	public void applyOptions(NavigationOptions options) {
		if (controller != null && controller.getTopBar() != null) {
			controller.getTopBar().setTitle(options.topBarOptions.title);
			controller.getTopBar().setBackgroundColor(options.topBarOptions.backgroundColor);
			controller.getTopBar().setTitleTextColor(options.topBarOptions.textColor);
			controller.getTopBar().setTitleFontSize(options.topBarOptions.textFontSize);
			TypefaceLoader typefaceLoader = new TypefaceLoader();
			controller.getTopBar().setTitleTypeface(typefaceLoader.getTypeFace(controller.getActivity(), options.topBarOptions.textFontFamily));
			applyTopBarHiddenOptions(options);
		}
	}

	private void applyTopBarHiddenOptions(NavigationOptions options) {
		if (options.topBarOptions.hidden == NavigationOptions.BooleanOptions.True) {
			hideTopBar(options.topBarOptions.animateHide);
		}
		if (options.topBarOptions.hidden == NavigationOptions.BooleanOptions.False) {
			showTopBar(options.topBarOptions.animateHide);
		}
	}

	private void showTopBar(NavigationOptions.BooleanOptions animated) {
		if (controller.getTopBar().getVisibility() == View.VISIBLE) {
			return;
		}
		if (animated == NavigationOptions.BooleanOptions.True) {
			ContainerView topBarContainerView = (ContainerView) controller.getContainerView();
			animator.animateShowTopBar(controller.getTopBar(), topBarContainerView.getReactView().asView());
		} else {
			controller.getTopBar().setVisibility(View.VISIBLE);
		}
	}

	private void hideTopBar(NavigationOptions.BooleanOptions animated) {
		if (controller.getTopBar().getVisibility() == View.GONE) {
			return;
		}
		if (animated == NavigationOptions.BooleanOptions.True) {
			ContainerView topBarContainerView = (ContainerView) controller.getContainerView();
			animator.animateHideTopBar(controller.getTopBar(), topBarContainerView.getReactView().asView());
		} else {
			controller.getTopBar().setVisibility(View.GONE);
		}
	}
}
