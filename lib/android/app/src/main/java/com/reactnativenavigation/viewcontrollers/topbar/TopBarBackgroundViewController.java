package com.reactnativenavigation.viewcontrollers.topbar;

import android.app.Activity;

import com.reactnativenavigation.parse.Component;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.react.events.ComponentType;
import com.reactnativenavigation.utils.CompatUtils;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.viewcontrollers.YellowBoxDelegate;
import com.reactnativenavigation.viewcontrollers.viewcontrolleroverlay.ViewControllerOverlay;
import com.reactnativenavigation.views.topbar.TopBarBackgroundView;
import com.reactnativenavigation.views.topbar.TopBarBackgroundViewCreator;

public class TopBarBackgroundViewController extends ViewController<TopBarBackgroundView> {

    private TopBarBackgroundViewCreator viewCreator;
    private Component component;

    public TopBarBackgroundViewController(Activity activity, TopBarBackgroundViewCreator viewCreator) {
        super(activity, CompatUtils.generateViewId() + "", new YellowBoxDelegate(activity), new Options(), new ViewControllerOverlay(activity));
        this.viewCreator = viewCreator;
    }

    @Override
    protected TopBarBackgroundView createView() {
        return viewCreator.create(getActivity(), component.componentId.get(), component.name.get());
    }

    @Override
    public void onViewWillAppear() {
        super.onViewWillAppear();
        getView().sendComponentStart(ComponentType.Background);
    }

    @Override
    public void onViewDisappear() {
        getView().sendComponentStop(ComponentType.Background);
        super.onViewDisappear();
    }

    @Override
    public void sendOnNavigationButtonPressed(String buttonId) {

    }

    @Override
    public String getCurrentComponentName() {
        return component.name.get();
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public Component getComponent() {
        return component;
    }
}
