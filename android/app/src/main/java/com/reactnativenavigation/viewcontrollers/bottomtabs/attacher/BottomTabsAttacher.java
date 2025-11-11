package com.reactnativenavigation.viewcontrollers.bottomtabs.attacher;

import android.view.ViewGroup;

import com.reactnativenavigation.options.Options;
import com.reactnativenavigation.viewcontrollers.bottomtabs.BottomTabsPresenter;
import com.reactnativenavigation.viewcontrollers.bottomtabs.attacher.modes.AttachMode;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController;

import java.util.List;

import androidx.annotation.VisibleForTesting;

public class BottomTabsAttacher {
    private final List<ViewController<?>> tabs;
    private final BottomTabsPresenter presenter;
    private final Options defaultOptions;
    @VisibleForTesting
    public AttachMode attachStrategy;

    public BottomTabsAttacher(List<ViewController<?>> tabs, BottomTabsPresenter presenter, Options defaultOptions) {
        this.tabs = tabs;
        this.presenter = presenter;
        this.defaultOptions = defaultOptions;
    }

    public void init(ViewGroup parent, Options resolved) {
        attachStrategy = AttachMode.get(parent, tabs, presenter, resolved.withDefaultOptions(defaultOptions));
    }

    public void attach() {
        attachStrategy.attach();
    }

    public void destroy() {
        attachStrategy.destroy();
    }

    public void onTabSelected(ViewController<?> tab) {
        attachStrategy.onTabSelected(tab);
    }
}
