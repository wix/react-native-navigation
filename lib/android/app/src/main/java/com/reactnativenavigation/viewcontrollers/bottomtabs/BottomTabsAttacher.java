package com.reactnativenavigation.viewcontrollers.bottomtabs;

import android.view.ViewGroup;

import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.presentation.BottomTabsPresenter;
import com.reactnativenavigation.viewcontrollers.ViewController;

import java.util.List;

import androidx.annotation.VisibleForTesting;

public class BottomTabsAttacher {
    private final List<ViewController> tabs;
    private final BottomTabsPresenter presenter;
    private Options defaultOptions;
    @VisibleForTesting
    AttachMode attachStrategy;

    public BottomTabsAttacher(List<ViewController> tabs, BottomTabsPresenter presenter, Options defaultOptions) {
        this.tabs = tabs;
        this.presenter = presenter;
        this.defaultOptions = defaultOptions;
    }

    void init(ViewGroup parent, Options resolved) {
        attachStrategy = AttachMode.get(parent, tabs, presenter, resolved.withDefaultOptions(defaultOptions));
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
