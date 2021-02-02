package com.reactnativenavigation.viewcontrollers.bottomtabs.attacher.modes;

import android.view.*;

import com.reactnativenavigation.options.*;
import com.reactnativenavigation.viewcontrollers.bottomtabs.BottomTabsPresenter;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController;

import java.util.*;

public class OnSwitchToTab extends AttachMode {

    public OnSwitchToTab(ViewGroup parent, List<ViewController<?>> tabs, BottomTabsPresenter presenter, Options resolved) {
        super(parent, tabs, presenter, resolved);
    }

    @Override
    public void attach() {
        attach(initialTab);
    }

    @Override
    public void onTabSelected(ViewController<?> tab) {
        if (tab != initialTab && isNotAttached(tab)) {
            attach(tab);
        }
    }

    private boolean isNotAttached(ViewController<?> tab) {
        return tab.getView().getParent() == null;
    }
}
