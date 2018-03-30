package com.reactnativenavigation.views;

import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.views.topbar.TopBar;

public interface Component {
    void applyOptions(Options options);

    void drawBehindTopBar();

    void drawBelowTopBar(TopBar topBar);
}
