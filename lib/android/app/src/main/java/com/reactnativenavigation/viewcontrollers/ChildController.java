package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.support.annotation.CallSuper;
import android.view.ViewGroup;

import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.presentation.OptionsPresenter;

public abstract class ChildController<T extends ViewGroup> extends ViewController<T>  {
    final OptionsPresenter presenter;
    private final ChildControllersRegistry childRegistry;

    public ChildControllersRegistry getChildRegistry() {
        return childRegistry;
    }

    public ChildController(Activity activity, ChildControllersRegistry childRegistry, String id, OptionsPresenter presenter, Options initialOptions) {
        super(activity, id, initialOptions);
        this.presenter = presenter;
        this.childRegistry = childRegistry;
    }

    @Override
    @CallSuper
    public void setDefaultOptions(Options defaultOptions) {
        presenter.setDefaultOptions(defaultOptions);
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
        presenter.onViewBroughtToFront(getView(), options);
    }

    @Override
    public void applyOptions(Options options) {
        super.applyOptions(options);
        presenter.present(getView(), options);
        if (isRoot()) {
            presenter.applyRootOptions(getView(), options);
        }
    }

    protected boolean isRoot() {
        return getParentController() == null &&
                !(this instanceof Navigator) &&
                getView().getParent() != null;
    }
}
