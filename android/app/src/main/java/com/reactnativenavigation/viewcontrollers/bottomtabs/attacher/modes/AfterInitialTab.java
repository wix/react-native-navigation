package com.reactnativenavigation.viewcontrollers.bottomtabs.attacher.modes;

import android.view.*;

import com.reactnativenavigation.options.*;
import com.reactnativenavigation.viewcontrollers.bottomtabs.BottomTabsPresenter;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController;

import java.util.*;

import static com.reactnativenavigation.utils.CollectionUtils.filter;
import static com.reactnativenavigation.utils.CollectionUtils.forEach;

public class AfterInitialTab extends AttachMode {
    private final Runnable attachOtherTabs;

    public AfterInitialTab(ViewGroup parent, List<ViewController<?>> tabs, BottomTabsPresenter presenter, Options resolved) {
        super(parent, tabs, presenter, resolved);
        attachOtherTabs = () -> forEach(otherTabs(), this::attach);
    }

    @Override
    public void attach() {
        initialTab.addOnAppearedListener(attachOtherTabs);
        attach(initialTab);
    }

    @Override
    public void destroy() {
        initialTab.removeOnAppearedListener(attachOtherTabs);
    }

    private List<ViewController<?>> otherTabs() {
        return filter(tabs, t -> t != initialTab);
    }
}
