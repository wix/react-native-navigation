package com.reactnativenavigation.presentation;

import android.util.Log;

import com.reactnativenavigation.parse.NavigationOptions;
import com.reactnativenavigation.utils.TypefaceLoader;
import com.reactnativenavigation.viewcontrollers.ContainerViewController;
import com.reactnativenavigation.viewcontrollers.StackController;

/**
 * Created by romanko on 9/14/17.
 */

public class OptionsPresenter {

	private ContainerViewController controller;

	public OptionsPresenter(ContainerViewController controller) {
		this.controller = controller;
	}

	public void applyOptions(NavigationOptions options) {
		if (controller != null) {
			controller.getTopBar().setTitle(options.title);
			controller.getTopBar().setBackgroundColor(options.topBarBackgroundColor);
			controller.getTopBar().setTitleTextColor(options.topBarTextColor);
			controller.getTopBar().setTitleFontSize(options.topBarTextFontSize);
			TypefaceLoader typefaceLoader = new TypefaceLoader();
			controller.getTopBar().setTitleTypeface(typefaceLoader.getTypeFace(controller.getActivity(), options.topBarTextFontFamily));
			controller.setTopBarHidden(options.topBarHidden, options.animateTopBarHide);
		}
	}
}
