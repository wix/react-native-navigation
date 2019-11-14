package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;

import com.reactnativenavigation.parse.Component;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.utils.CompatUtils;
import com.reactnativenavigation.views.titlebar.TitleBarReactView;
import com.reactnativenavigation.views.titlebar.TitleBarReactViewCreator;

public class TitleBarReactViewController extends ViewController<TitleBarReactView> {

    private final TitleBarReactViewCreator reactViewCreator;
    private Component component;

    public TitleBarReactViewController(Activity activity, TitleBarReactViewCreator reactViewCreator) {
        super(activity, CompatUtils.generateViewId() + "", new YellowBoxDelegate(), new Options());
        this.reactViewCreator = reactViewCreator;
    }

    @Override
    public void onViewAppeared() {
        super.onViewAppeared();
        if (!isDestroyed()) {
            runOnPreDraw(view -> view.setLayoutParams(view.getLayoutParams()));
            getView().sendComponentStart();
        }
    }

    @Override
    public void onViewDisappear() {
        getView().sendComponentStop();
        super.onViewDisappear();
    }

    @Override
    protected TitleBarReactView createView() {
        return reactViewCreator.create(getActivity(), component.componentId.get(), component.name.get());
    }

    @Override
    public void sendOnNavigationButtonPressed(String buttonId) {

    }

    public void setComponent(Component component) {
        this.component = component;
    }
}
