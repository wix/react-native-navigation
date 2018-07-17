package com.reactnativenavigation.views;

import com.reactnativenavigation.views.topbar.TopBar;

public interface Component {
    void drawBehindTopBar();

    void drawBelowTopBar(TopBar topBar);

    boolean isRendered();
}
