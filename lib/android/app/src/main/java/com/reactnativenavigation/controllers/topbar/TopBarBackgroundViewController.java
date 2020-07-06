package com.reactnativenavigation.controllers.topbar;

import android.app.Activity;

import com.reactnativenavigation.parse.ComponentOptions;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.react.events.ComponentType;
import com.reactnativenavigation.utils.CompatUtils;
import com.reactnativenavigation.controllers.viewcontroller.ViewController;
import com.reactnativenavigation.controllers.viewcontroller.YellowBoxDelegate;
import com.reactnativenavigation.controllers.viewcontroller.ViewControllerOverlay;
import com.reactnativenavigation.views.topbar.TopBarBackgroundView;
import com.reactnativenavigation.views.topbar.TopBarBackgroundViewCreator;

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
