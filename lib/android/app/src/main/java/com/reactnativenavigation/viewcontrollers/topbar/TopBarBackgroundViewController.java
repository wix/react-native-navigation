package com.reactnativenavigation.viewcontrollers.topbar;

import android.app.Activity;

import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.utils.CompatUtils;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.views.topbar.TopBarBackgroundView;
import com.reactnativenavigation.views.topbar.TopBarBackgroundViewCreator;

public class TopBarBackgroundViewController extends ViewController<TopBarBackgroundView> {

    private TopBarBackgroundViewCreator viewCreator;
    private String component;

    public TopBarBackgroundViewController(Activity activity, TopBarBackgroundViewCreator viewCreator) {
        super(activity, CompatUtils.generateViewId() + "", new Options());
        this.viewCreator = viewCreator;
    }

    public TopBarBackgroundViewController(TopBarBackgroundViewController topBarBackgroundViewController) {
        super(topBarBackgroundViewController.getActivity(), CompatUtils.generateViewId() + "", new Options());
        this.viewCreator = topBarBackgroundViewController.viewCreator;
    }

    @Override
    protected TopBarBackgroundView createView() {
        return viewCreator.create(getActivity(), getId(), component);
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
    public void sendOnNavigationButtonPressed(String buttonId) {

    }

    public void setComponent(String component) {
        this.component = component;
    }
}
