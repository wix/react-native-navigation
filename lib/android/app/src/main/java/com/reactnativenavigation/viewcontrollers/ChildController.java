package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.view.ViewGroup;

import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.presentation.OptionsPresenter;

public abstract class ChildController<T extends ViewGroup> extends ViewController<T>  {
    private final OptionsPresenter presenter;
    private final ChildControllersRegistry childRegistry;

    public ChildControllersRegistry getChildRegistry() {
        return childRegistry;
    }

    public ChildController(Activity activity, ChildControllersRegistry childRegistry, String id, Options initialOptions) {
        super(activity, id, initialOptions);
        presenter = new OptionsPresenter(activity);
        this.childRegistry = childRegistry;
    }

    @Override
    public void onViewAppeared() {
        super.onViewAppeared();
        childRegistry.onViewAppeared(this);
    }

    @Override
    public void onViewDisappear() {
        super.onViewDisappear();
        childRegistry.onViewDisappear(this);
    }

    public void onViewBroughtToFront() {
        presenter.onViewBroughtToFront(options);
    }

    @Override
    public void applyOptions(Options options) {
        super.applyOptions(options);
        presenter.present(getView(), options);
    }
}
