package com.reactnativenavigation.presentation;

import android.view.View;

import com.reactnativenavigation.anim.StackAnimator;
import com.reactnativenavigation.parse.NavigationOptions;
import com.reactnativenavigation.parse.TopBarOptions;
import com.reactnativenavigation.parse.TopTabsOptions;
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
            applyTopBarOptions(options.topBarOptions);
            applyTopTabsOptions(options.topTabsOptions);
		}
	}

    private void applyTopBarOptions(TopBarOptions options) {
        controller.setTitle(options.title);
        controller.setBackgroundColor(options.backgroundColor);
        controller.setTitleTextColor(options.textColor);
        controller.setTitleFontSize(options.textFontSize);
        TypefaceLoader typefaceLoader = new TypefaceLoader();
        controller.getTopBar().setTitleTypeface(typefaceLoader.getTypeFace(controller.getActivity(), options.textFontFamily));
        if (options.hidden == NavigationOptions.BooleanOptions.True) {
            hideTopBar(options.animateHide);
        }
        if (options.hidden == NavigationOptions.BooleanOptions.False) {
            showTopBar(options.animateHide);
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

    private void applyTopTabsOptions(TopTabsOptions topTabsOptions) {
        // TODO: -guyca
    }
}
