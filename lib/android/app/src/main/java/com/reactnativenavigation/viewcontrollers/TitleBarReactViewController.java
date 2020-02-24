package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;

import com.reactnativenavigation.parse.Component;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.react.events.ComponentType;
import com.reactnativenavigation.utils.CompatUtils;
import com.reactnativenavigation.viewcontrollers.viewcontrolleroverlay.ViewControllerOverlay;
import com.reactnativenavigation.views.titlebar.TitleBarReactView;
import com.reactnativenavigation.views.titlebar.TitleBarReactViewCreator;

public class TitleBarReactViewController extends ViewController<TitleBarReactView> {

    private final TitleBarReactViewCreator reactViewCreator;
    private Component component;

    public TitleBarReactViewController(Activity activity, TitleBarReactViewCreator reactViewCreator) {
        super(activity, CompatUtils.generateViewId() + "", new YellowBoxDelegate(), new Options(), new ViewControllerOverlay(activity));
        this.reactViewCreator = reactViewCreator;
    }

    @Override
    public void onViewAppeared() {
        super.onViewAppeared();
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
    protected TitleBarReactView createView() {
        return reactViewCreator.create(getActivity(), component.componentId.get(), component.name.get());
    }

    @Override
    public void sendOnNavigationButtonPressed(String buttonId) {

    }

    @Override
    public String getCurrentComponentName() {
        return null;
    }

    public void setComponent(Component component) {
        this.component = component;
    }
}
