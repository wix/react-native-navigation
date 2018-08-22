package com.reactnativenavigation.viewcontrollers.topbar;

import android.app.Activity;
import android.view.View;
import android.view.ViewManager;

import com.reactnativenavigation.parse.Component;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.utils.CompatUtils;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.views.topbar.TopBarBackgroundView;
import com.reactnativenavigation.views.topbar.TopBarBackgroundViewCreator;

public class TopBarBackgroundViewController extends ViewController<TopBarBackgroundView> {

    private TopBarBackgroundViewCreator viewCreator;
    private Component component;

    public TopBarBackgroundViewController(Activity activity, TopBarBackgroundViewCreator viewCreator) {
        super(activity, CompatUtils.generateViewId() + "", new Options());
        this.viewCreator = viewCreator;
    }

    public TopBarBackgroundViewController(TopBarBackgroundViewController controller) {
        super(controller.getActivity(), controller.getId(), controller.options);
        this.viewCreator = controller.viewCreator;
    }

    @Override
    protected TopBarBackgroundView createView() {
        return viewCreator.create(getActivity(), component.componentId.get(), component.name.get());
    }

    @Override
    public void onViewAppeared() {
        super.onViewAppeared();
        view.sendComponentStart();
    }

    @Override
    public void onViewDisappear() {
        view.sendComponentStop();
        super.onViewDisappear();
    }

    @Override
    protected void onYellowBoxAdded(View yellowBox) {
        ((ViewManager) yellowBox.getParent()).removeView(yellowBox);
    }

    @Override
    public void sendOnNavigationButtonPressed(String buttonId) {

    }

    public void setComponent(Component component) {
        this.component = component;
    }
}
