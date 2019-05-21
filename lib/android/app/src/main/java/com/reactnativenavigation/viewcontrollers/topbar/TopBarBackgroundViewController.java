package com.reactnativenavigation.viewcontrollers.topbar;

import android.app.Activity;

import com.reactnativenavigation.parse.Component;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.utils.CompatUtils;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.viewcontrollers.YellowBoxDelegate;
import com.reactnativenavigation.views.topbar.TopBarBackgroundView;
import com.reactnativenavigation.views.topbar.TopBarBackgroundViewCreator;

public class TopBarBackgroundViewController extends ViewController<TopBarBackgroundView> {

    private TopBarBackgroundViewCreator viewCreator;
    private Component component;

    public TopBarBackgroundViewController(Activity activity, TopBarBackgroundViewCreator viewCreator) {
        super(activity, CompatUtils.generateViewId() + "", new YellowBoxDelegate(), new Options());
        this.viewCreator = viewCreator;
    }

    @Override
    protected TopBarBackgroundView createView() {
        return viewCreator.create(getActivity(), component.componentId.get(), component.name.get());
    }

    @Override
    public void onViewAppeared() {
        super.onViewAppeared();
        getView().sendComponentStart();
    }

    @Override
    public void onViewDisappear() {
        getView().sendComponentStop();
        super.onViewDisappear();
    }

    @Override
    public void sendOnNavigationButtonPressed(String buttonId) {

    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public Component getComponent() {
        return component;
    }
}
