package com.reactnativenavigation.views;

import android.support.annotation.RestrictTo;
import android.view.View;

import com.reactnativenavigation.parse.NavigationOptions;
import com.reactnativenavigation.viewcontrollers.ContainerViewController;

public interface Container {
    void applyOptions(NavigationOptions options);

    void sendOnNavigationButtonPressed(String id);

    TopBar getTopBar();

    View getContentView();

    void drawBehindTopBar();

    void drawBelowTopBar();
}
