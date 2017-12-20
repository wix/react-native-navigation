package com.reactnativenavigation.views;

import android.support.annotation.RestrictTo;

import com.reactnativenavigation.parse.NavigationOptions;

public interface Container {
    void applyOptions(NavigationOptions options);

    void sendOnNavigationButtonPressed(String id);

    @RestrictTo(RestrictTo.Scope.TESTS)
    TopBar getTopBar();
}
