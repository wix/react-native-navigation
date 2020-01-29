package com.reactnativenavigation.viewcontrollers.bottomtabs;

import android.view.*;

import com.reactnativenavigation.parse.*;
import com.reactnativenavigation.presentation.*;
import com.reactnativenavigation.viewcontrollers.*;

import java.util.*;

public class OnSwitchToTab extends AttachMode {
    private final ViewController initialTab;

    public OnSwitchToTab(ViewGroup parent, List<ViewController> tabs, BottomTabsPresenter presenter, Options resolved) {
        super(parent, tabs, presenter, resolved);
        this.initialTab = tabs.get(resolved.bottomTabsOptions.currentTabIndex.get(0));
    }

    @Override
    public void attach() {
        attach(initialTab);
    }

    @Override
    public void onTabSelected(ViewController tab) {
        if (tab != initialTab && isNotAttached(tab)) {
            attach(tab);
        }
    }

    private boolean isNotAttached(ViewController tab) {
        return tab.getView().getParent() == null;
    }
}
