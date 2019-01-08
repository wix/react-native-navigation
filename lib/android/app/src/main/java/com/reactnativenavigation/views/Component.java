package com.reactnativenavigation.views;

import com.reactnativenavigation.views.topbar.TopBar;

public interface Component extends Renderable {
    void drawBehindTopBar();

    void drawBelowTopBar(TopBar topBar);
}
