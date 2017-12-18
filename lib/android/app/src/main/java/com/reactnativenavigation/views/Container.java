package com.reactnativenavigation.views;

import android.support.annotation.RestrictTo;

import com.reactnativenavigation.parse.NavigationOptions;

public interface Container {
    void applyOptions(NavigationOptions options);

    @RestrictTo(RestrictTo.Scope.TESTS)
    TopBar getTopBar();
}
