package com.reactnativenavigation.presentation;

import com.reactnativenavigation.parse.Button;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.TopBarOptions;
import com.reactnativenavigation.parse.TopTabOptions;
import com.reactnativenavigation.parse.TopTabsOptions;
import com.reactnativenavigation.views.ReactComponent;
import com.reactnativenavigation.views.TopBar;

import java.util.ArrayList;

import static com.reactnativenavigation.parse.Options.BooleanOptions.False;
import static com.reactnativenavigation.parse.Options.BooleanOptions.True;

public class OptionsPresenter {
    private TopBar topBar;
    private ReactComponent component;

    public OptionsPresenter(TopBar topBar, ReactComponent component) {
        this.topBar = topBar;
        this.component = component;
    }

    public void applyOptions(Options options) {
        applyButtons(options.topBarOptions.leftButtons, options.topBarOptions.rightButtons);
        applyTopBarOptions(options.topBarOptions);
        applyTopTabsOptions(options.topTabsOptions);
        applyTopTabOptions(options.topTabOptions);
    }

    private void applyTopBarOptions(TopBarOptions options) {
        if (options.title.hasValue()) topBar.setTitle(options.title.get());
        topBar.setBackgroundColor(options.backgroundColor);
        topBar.setTitleTextColor(options.textColor);
        topBar.setTitleFontSize(options.textFontSize);

        topBar.setTitleTypeface(options.textFontFamily);
        if (options.hidden == True) {
            topBar.hide(options.animateHide);
        }
        if (options.hidden == False) {
            topBar.show(options.animateHide);
        }
        if (options.drawBehind == True) {
            component.drawBehindTopBar();
        } else if (options.drawBehind == False) {
            component.drawBelowTopBar(topBar);
        }

        if (options.hideOnScroll == True) {
            topBar.enableCollapse(component.getScrollEventListener());
        } else if (options.hideOnScroll == False) {
            topBar.disableCollapse();
        }
    }

    private void applyButtons(ArrayList<Button> leftButtons, ArrayList<Button> rightButtons) {
        topBar.setButtons(leftButtons, rightButtons);
    }

    private void applyTopTabsOptions(TopTabsOptions options) {
        topBar.applyTopTabsColors(options.selectedTabColor, options.unselectedTabColor);
        topBar.applyTopTabsFontSize(options.fontSize);
    }

    private void applyTopTabOptions(TopTabOptions topTabOptions) {
        if (topTabOptions.fontFamily != null) {
            topBar.setTopTabFontFamily(topTabOptions.tabIndex, topTabOptions.fontFamily);
        }
    }
}
