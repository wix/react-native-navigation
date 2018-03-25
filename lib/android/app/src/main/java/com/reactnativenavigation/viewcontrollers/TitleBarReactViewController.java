package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;

import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.utils.CompatUtils;
import com.reactnativenavigation.views.titlebar.TitleBarReactView;
import com.reactnativenavigation.views.titlebar.TitleBarReactViewCreator;

public class TitleBarReactViewController extends ViewController<TitleBarReactView> {

    private final TitleBarReactViewCreator reactViewCreator;
    private String componentName;

    public TitleBarReactViewController(TitleBarReactViewController reactViewController) {
        this(reactViewController.getActivity(), reactViewController.reactViewCreator);
    }

    public TitleBarReactViewController(Activity activity, TitleBarReactViewCreator reactViewCreator) {
        super(activity, CompatUtils.generateViewId() + "", new Options());
        this.reactViewCreator = reactViewCreator;
    }

    @Override
    protected TitleBarReactView createView() {
        return reactViewCreator.create(getActivity(), getId(), componentName);
    }

    @Override
    public void sendOnNavigationButtonPressed(String buttonId) {

    }

    public void setComponent(String componentName) {
        this.componentName = componentName;
    }
}
