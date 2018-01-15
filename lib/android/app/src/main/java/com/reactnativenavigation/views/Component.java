package com.reactnativenavigation.views;

import com.reactnativenavigation.parse.Options;

public interface Component {
    void applyOptions(Options options);

    void drawBehindTopBar();

    void drawBelowTopBar(TopBar topBar);
}
