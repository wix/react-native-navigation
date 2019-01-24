package com.reactnativenavigation.viewcontrollers.bottomtabs;

import android.support.annotation.*;
import android.view.*;
import android.widget.*;

import com.reactnativenavigation.parse.*;
import com.reactnativenavigation.presentation.*;
import com.reactnativenavigation.viewcontrollers.*;

import java.util.*;

import static android.view.ViewGroup.LayoutParams.*;

public abstract class AttachMode {
    protected final ViewGroup parent;
    protected final BottomTabsPresenter presenter;
    protected final List<ViewController> tabs;
    final ViewController initialTab;
    private final Options resolved;


    public static AttachMode get(ViewGroup parent, List<ViewController> tabs, BottomTabsPresenter presenter, Options resolved) {
        switch (resolved.bottomTabsOptions.tabsAttachMode) {
            case AFTER_INITIAL_TAB:
                return new AfterInitialTab(parent, tabs, presenter, resolved);
            case ON_SWITCH_TO_TAB:
                return new OnSwitchToTab(parent, tabs, presenter, resolved);
            case UNDEFINED:
            case TOGETHER:
            default:
                return new Together(parent, tabs, presenter, resolved);
        }
    }

    AttachMode(ViewGroup parent, List<ViewController> tabs, BottomTabsPresenter presenter, Options resolved) {
        this.parent = parent;
        this.tabs = tabs;
        this.presenter = presenter;
        this.resolved = resolved;
        initialTab = tabs.get(resolved.bottomTabsOptions.currentTabIndex.get(0));
    }

    public abstract void attach();

    public void destroy() {

    }

    public void onTabSelected(ViewController tab) {

    }

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    public void attach(ViewController tab) {
        ViewGroup view = tab.getView();
        view.setLayoutParams(new RelativeLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        presenter.applyLayoutParamsOptions(resolved, tabs.indexOf(tab));
        view.setVisibility(tab == initialTab ? View.VISIBLE : View.INVISIBLE);
        parent.addView(view);
    }
}
