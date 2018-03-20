package com.reactnativenavigation.presentation;

import android.app.Activity;

import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.OrientationOptions;
import com.reactnativenavigation.parse.TopBarOptions;
import com.reactnativenavigation.parse.TopTabOptions;
import com.reactnativenavigation.parse.TopTabsOptions;
import com.reactnativenavigation.parse.params.Button;
import com.reactnativenavigation.viewcontrollers.IReactView;
import com.reactnativenavigation.views.Component;
import com.reactnativenavigation.views.TopBar;

import java.util.ArrayList;

public class OptionsPresenter {
    private TopBar topBar;
    private Component component;

    public OptionsPresenter(TopBar topBar, Component component) {
        this.topBar = topBar;
        this.component = component;
    }

    public OptionsPresenter(TopBar topBar) {
        this.topBar = topBar;
    }

    public void applyOptions(Options options) {
        applyOrientation(options.orientationOptions);
        applyButtons(options.topBarOptions.leftButtons, options.topBarOptions.rightButtons);
        applyTopBarOptions(options.topBarOptions);
        applyTopTabsOptions(options.topTabsOptions);
        applyTopTabOptions(options.topTabOptions);
    }

    public void applyOrientation(OrientationOptions options) {
        ((Activity) topBar.getContext()).setRequestedOrientation(options.getValue());
    }

    private void applyTopBarOptions(TopBarOptions options) {
        if (options.title.hasValue()) topBar.setTitle(options.title.get());
        topBar.setBackgroundColor(options.backgroundColor);
        topBar.setTitleTextColor(options.textColor);
        topBar.setTitleFontSize(options.textFontSize);
        if (options.testId.hasValue()) topBar.setTestId(options.testId.get());

        topBar.setTitleTypeface(options.textFontFamily);
        if (options.visible.isFalse()) {
            topBar.hide(options.animate);
        }
        if (options.visible.isTrueOrUndefined()) {
            topBar.show(options.animate);
        }
        if (options.drawBehind.isTrue()) {
            component.drawBehindTopBar();
        } else if (options.drawBehind.isFalseOrUndefined()) {
            component.drawBelowTopBar(topBar);
        }

        if (options.hideOnScroll.isTrue()) {
            if (component instanceof IReactView) {
                topBar.enableCollapse(((IReactView) component).getScrollEventListener());
            }
        } else if (options.hideOnScroll.isTrue()) {
            topBar.disableCollapse();
        }
    }

    private void applyButtons(ArrayList<Button> leftButtons, ArrayList<Button> rightButtons) {
        topBar.setButtons(leftButtons, rightButtons);
    }

    private void applyTopTabsOptions(TopTabsOptions options) {
        topBar.applyTopTabsColors(options.selectedTabColor, options.unselectedTabColor);
        topBar.applyTopTabsFontSize(options.fontSize);
        topBar.setTopTabsVisible(options.visible.isTrueOrUndefined());
    }

    private void applyTopTabOptions(TopTabOptions topTabOptions) {
        if (topTabOptions.fontFamily != null) {
            topBar.setTopTabFontFamily(topTabOptions.tabIndex, topTabOptions.fontFamily);
        }
    }

    public void onChildWillDisappear(Options disappearing, Options appearing) {
        if (disappearing.topBarOptions.visible.isTrueOrUndefined() && appearing.topBarOptions.visible.isFalse()) {
            topBar.hide(disappearing.topBarOptions.animate);
        }
    }
}
