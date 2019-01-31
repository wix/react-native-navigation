package com.reactnativenavigation.viewcontrollers.bottomtabs;

import android.support.annotation.VisibleForTesting;
import android.view.ViewGroup;

import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.presentation.BottomTabsPresenter;
import com.reactnativenavigation.viewcontrollers.ViewController;

import java.util.List;

public class BottomTabsAttacher {
    private final List<ViewController> tabs;
    private final BottomTabsPresenter presenter;
    @VisibleForTesting
    AttachMode attachStrategy;

    public BottomTabsAttacher(List<ViewController> tabs, BottomTabsPresenter presenter) {
        this.tabs = tabs;
        this.presenter = presenter;
    }

    void init(ViewGroup parent, Options resolved) {
        attachStrategy = AttachMode.get(parent, tabs, presenter, resolved);
    }

    void attach() {
        attachStrategy.attach();
    }

    public void destroy() {
        attachStrategy.destroy();
    }

    void onTabSelected(ViewController tab) {
        attachStrategy.onTabSelected(tab);
    }
}
