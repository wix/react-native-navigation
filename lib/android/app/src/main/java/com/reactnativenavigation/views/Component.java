package com.reactnativenavigation.views;

import com.reactnativenavigation.parse.Options;

public interface Component {
    void applyOptions(Options options);

    TopBar getTopBar();

    void drawBehindTopBar();

    void drawBelowTopBar();
}
