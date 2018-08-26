package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.view.View;
import android.view.ViewManager;

import com.reactnativenavigation.parse.Component;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.utils.CompatUtils;
import com.reactnativenavigation.views.titlebar.TitleBarReactView;
import com.reactnativenavigation.views.titlebar.TitleBarReactViewCreator;

public class TitleBarReactViewController extends ViewController<TitleBarReactView> {

    private final TitleBarReactViewCreator reactViewCreator;
    private Component component;

    public TitleBarReactViewController(Activity activity, TitleBarReactViewCreator reactViewCreator) {
        super(activity, CompatUtils.generateViewId() + "", new Options());
        this.reactViewCreator = reactViewCreator;
    }

    @Override
    public void onViewAppeared() {
        super.onViewAppeared();
        runOnPreDraw(view -> view.setLayoutParams(view.getLayoutParams()));
        view.sendComponentStart();
    }

    @Override
    public void onViewDisappear() {
        view.sendComponentStop();
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
    protected void onYellowBoxAdded(View yellowBox) {
        ((ViewManager) yellowBox.getParent()).removeView(yellowBox);
    }

    public void setComponent(Component component) {
        this.component = component;
    }
}
