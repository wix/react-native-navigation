package com.reactnativenavigation.presentation;

import android.view.View;

import com.reactnativenavigation.anim.StackAnimator;
import com.reactnativenavigation.parse.Button;
import com.reactnativenavigation.parse.NavigationOptions;
import com.reactnativenavigation.parse.TopBarOptions;
import com.reactnativenavigation.parse.TopTabOptions;
import com.reactnativenavigation.utils.TypefaceLoader;
import com.reactnativenavigation.views.TopBar;

import java.util.ArrayList;

public class OptionsPresenter {

	private final StackAnimator animator;
    private View contentView;
    private TopBar topBar;

    public OptionsPresenter(TopBar topBar, View contentView) {
        this.topBar = topBar;
        this.contentView = contentView;
        animator = new StackAnimator(topBar.getContext());
    }

    public void applyOptions(NavigationOptions options) {
        applyTopBarOptions(options.topBarOptions);
        applyButtons(options.leftButtons, options.rightButtons);
        applyTopTabOptions(options.topTabOptions);
    }

    private void applyTopBarOptions(TopBarOptions options) {
        topBar.setTitle(options.title);
        topBar.setBackgroundColor(options.backgroundColor);
        topBar.setTitleTextColor(options.textColor);
        topBar.setTitleFontSize(options.textFontSize);

        TypefaceLoader typefaceLoader = new TypefaceLoader(topBar.getContext());
        topBar.setTitleTypeface(typefaceLoader.getTypeFace(options.textFontFamily));
        if (options.hidden == NavigationOptions.BooleanOptions.True) {
            hideTopBar(options.animateHide);
        }
        if (options.hidden == NavigationOptions.BooleanOptions.False) {
            showTopBar(options.animateHide);
        }
    }

	private void showTopBar(NavigationOptions.BooleanOptions animated) {
		if (topBar.getVisibility() == View.VISIBLE) {
			return;
		}
		if (animated == NavigationOptions.BooleanOptions.True) {
			animator.animateShowTopBar(topBar, contentView);
		} else {
			topBar.setVisibility(View.VISIBLE);
		}
	}

	private void hideTopBar(NavigationOptions.BooleanOptions animated) {
		if (topBar.getVisibility() == View.GONE) {
			return;
		}
		if (animated == NavigationOptions.BooleanOptions.True) {
			animator.animateHideTopBar(topBar, contentView);
		} else {
			topBar.setVisibility(View.GONE);
		}
	}

    private void applyButtons(ArrayList<Button> leftButtons, ArrayList<Button> rightButtons) {
        topBar.setButtons(leftButtons, rightButtons);
    }

    private void applyTopTabOptions(TopTabOptions topTabOptions) {
        if (topTabOptions.fontFamily != null) {
            topBar.setTopTabFontFamily(topTabOptions.tabIndex, topTabOptions.fontFamily);
        }
    }
}
