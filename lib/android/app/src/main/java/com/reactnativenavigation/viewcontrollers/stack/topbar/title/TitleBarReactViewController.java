package com.reactnativenavigation.viewcontrollers.stack.topbar.title;

import android.app.Activity;

import com.reactnativenavigation.options.ComponentOptions;
import com.reactnativenavigation.options.Options;
import com.reactnativenavigation.react.events.ComponentType;
import com.reactnativenavigation.utils.CompatUtils;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController;
import com.reactnativenavigation.viewcontrollers.viewcontroller.YellowBoxDelegate;
import com.reactnativenavigation.viewcontrollers.viewcontroller.overlay.ViewControllerOverlay;
import com.reactnativenavigation.views.stack.topbar.titlebar.TitleBarReactView;
import com.reactnativenavigation.views.stack.topbar.titlebar.TitleBarReactViewCreator;

public class TitleBarReactViewController extends ViewController<TitleBarReactView> {

    private final TitleBarReactViewCreator reactViewCreator;
    private final ComponentOptions component;

    public ComponentOptions getComponent() {
        return component;
    }

    public TitleBarReactViewController(Activity activity, TitleBarReactViewCreator reactViewCreator, ComponentOptions component) {
        super(activity, CompatUtils.generateViewId() + "", new YellowBoxDelegate(activity), new Options(), new ViewControllerOverlay(activity));
        this.reactViewCreator = reactViewCreator;
        this.component = component;
    }

    @Override
    public void onViewWillAppear() {
        if (!isDestroyed()) {
            getView().sendComponentWillStart(ComponentType.Title);
        }
        super.onViewWillAppear();
        if (!isDestroyed()) {
            runOnPreDraw(view -> view.setLayoutParams(view.getLayoutParams()));
            getView().sendComponentStart(ComponentType.Title);
        }
    }

    @Override
    public void onViewDisappear() {
        getView().sendComponentStop(ComponentType.Title);
        super.onViewDisappear();
    }

    @Override
    public TitleBarReactView createView() {
        return reactViewCreator.create(getActivity(), component.componentId.get(), component.name.get());
    }

    @Override
    public void sendOnNavigationButtonPressed(String buttonId) {

    }

    @Override
    public String getCurrentComponentName() {
        return null;
    }
}
