package com.reactnativenavigation.presentation;

import com.reactnativenavigation.parse.NavigationOptions;
import com.reactnativenavigation.utils.TypefaceLoader;
import com.reactnativenavigation.viewcontrollers.StackController;

/**
 * Created by romanko on 9/14/17.
 */

public class OptionsPresenter {

	private StackController controller;

	public OptionsPresenter(StackController controller) {
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
		}
	}
}
