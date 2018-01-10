package com.reactnativenavigation.presentation;

import com.reactnativenavigation.parse.Button;
import com.reactnativenavigation.parse.NavigationOptions;
import com.reactnativenavigation.parse.TopBarOptions;
import com.reactnativenavigation.parse.TopTabOptions;
import com.reactnativenavigation.parse.TopTabsOptions;
import com.reactnativenavigation.views.Container;
import com.reactnativenavigation.views.TopBar;

import java.util.ArrayList;

import static com.reactnativenavigation.parse.NavigationOptions.BooleanOptions.False;
import static com.reactnativenavigation.parse.NavigationOptions.BooleanOptions.True;

public class OptionsPresenter {

    private Container reactContainer;
    private TopBar topBar;

    public OptionsPresenter(Container reactContainer) {
        this.reactContainer = reactContainer;
        this.topBar = reactContainer.getTopBar();
    }

    public void applyOptions(NavigationOptions options) {
        applyTopBarOptions(options.topBarOptions);
        applyButtons(options.topBarOptions.leftButtons, options.topBarOptions.rightButtons);
        applyTopTabsOptions(options.topTabsOptions);
        applyTopTabOptions(options.topTabOptions);
    }

    private void applyTopBarOptions(TopBarOptions options) {
        topBar.setTitle(options.title);
        topBar.setBackgroundColor(options.backgroundColor);
        topBar.setTitleTextColor(options.textColor);
        topBar.setTitleFontSize(options.textFontSize);

        topBar.setTitleTypeface(options.textFontFamily);
        if (options.hidden == NavigationOptions.BooleanOptions.True) {
            topBar.hide(options.animateHide);
        }
        if (options.hidden == NavigationOptions.BooleanOptions.False) {
            topBar.show(options.animateHide);
        }
        if (options.drawBehind == True) {
            reactContainer.drawBehindTopBar();
        } else if (options.drawBehind == False) {
            reactContainer.drawBelowTopBar();
        }

        if (options.hideOnScroll == True) {
            topBar.enableCollapse();
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
