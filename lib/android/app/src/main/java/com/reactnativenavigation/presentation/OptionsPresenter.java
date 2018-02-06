package com.reactnativenavigation.presentation;

import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.reactnativenavigation.parse.Button;
import com.reactnativenavigation.parse.FabOptions;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.TopBarOptions;
import com.reactnativenavigation.parse.TopTabOptions;
import com.reactnativenavigation.parse.TopTabsOptions;
import com.reactnativenavigation.views.Fab;
import com.reactnativenavigation.views.ReactComponent;
import com.reactnativenavigation.views.TopBar;

import java.util.ArrayList;

import static com.reactnativenavigation.parse.Options.BooleanOptions.False;
import static com.reactnativenavigation.parse.Options.BooleanOptions.True;

public class OptionsPresenter {
    private TopBar topBar;
    private ReactComponent component;
    private Fab fab;

    public OptionsPresenter(TopBar topBar, ReactComponent component, Fab fab) {
        this.topBar = topBar;
        this.component = component;
        this.fab = fab;
    }

    public void applyOptions(Options options) {
        applyButtons(options.topBarOptions.leftButtons, options.topBarOptions.rightButtons);
        applyTopBarOptions(options.topBarOptions);
        applyTopTabsOptions(options.topTabsOptions);
        applyTopTabOptions(options.topTabOptions);
        applyFabOptions(options.fabOptions);
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

    private void applyFabOptions(FabOptions options) {
        if (options.id.hasValue()) {
            fab.bringToFront();
            if (options.hidden == True) {
                fab.hide(true);
            } else {
                fab.show(true);
            }

            fab.setOnClickListener(v -> {
                component.sendOnNavigationButtonPressed(options.id.get());
            });

            if (options.backgroundColor.hasValue()) {
                fab.setColorNormal(options.backgroundColor.get());
            }
            if (options.clickColor.hasValue()) {
                fab.setColorPressed(options.clickColor.get());
            }
            if (options.rippleColor.hasValue()) {
                fab.setColorRipple(options.rippleColor.get());
            }
            if (options.icon.hasValue()) {
                fab.applyIcon(options.icon.get());
            }

        }
    }
}
