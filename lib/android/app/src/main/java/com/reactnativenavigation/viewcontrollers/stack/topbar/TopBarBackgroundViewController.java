package com.reactnativenavigation.viewcontrollers.stack.topbar;

import android.app.Activity;

import com.reactnativenavigation.options.ComponentOptions;
import com.reactnativenavigation.options.Options;
import com.reactnativenavigation.react.events.ComponentType;
import com.reactnativenavigation.utils.CompatUtils;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController;
import com.reactnativenavigation.viewcontrollers.viewcontroller.YellowBoxDelegate;
import com.reactnativenavigation.viewcontrollers.viewcontroller.overlay.ViewControllerOverlay;
import com.reactnativenavigation.views.stack.topbar.TopBarBackgroundView;
import com.reactnativenavigation.views.stack.topbar.TopBarBackgroundViewCreator;

public class TopBarBackgroundViewController extends ViewController<TopBarBackgroundView> {

    private TopBarBackgroundViewCreator viewCreator;
    private ComponentOptions component;

    public TopBarBackgroundViewController(Activity activity, TopBarBackgroundViewCreator viewCreator) {
        super(activity, CompatUtils.generateViewId() + "", new YellowBoxDelegate(activity), new Options(), new ViewControllerOverlay(activity));
        this.viewCreator = viewCreator;
    }

    @Override
    public TopBarBackgroundView createView() {
        return viewCreator.create(getActivity(), component.componentId.get(), component.name.get());
    }

    @Override
    public void onViewWillAppear() {
        getView().sendComponentWillStart(ComponentType.Background);
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

    public void setComponent(ComponentOptions component) {
        this.component = component;
    }

    public ComponentOptions getComponent() {
        return component;
    }
}
