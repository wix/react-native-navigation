package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.support.annotation.CallSuper;
import android.view.ViewGroup;

import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.presentation.OptionsPresenter;
import com.reactnativenavigation.viewcontrollers.navigator.Navigator;
import com.reactnativenavigation.views.Component;

public abstract class ChildController<T extends ViewGroup> extends ViewController<T>  {
    final OptionsPresenter presenter;
    private final ChildControllersRegistry childRegistry;

    public ChildControllersRegistry getChildRegistry() {
        return childRegistry;
    }

    public ChildController(Activity activity, ChildControllersRegistry childRegistry, String id, OptionsPresenter presenter, Options initialOptions) {
        super(activity, id, new NoOpYellowBoxDelegate(), initialOptions);
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
        presenter.applyOptions(getView(), options);
        if (isRoot()) {
            presenter.applyRootOptions(getView(), options);
        }
    }

    @Override
    public void mergeOptions(Options options) {
        if (options == Options.EMPTY) return;
        presenter.mergeOptions(getView(), options);
        super.mergeOptions(options);
    }

    @Override
    public void destroy() {
        if (!isDestroyed() && getView() instanceof Component) {
            performOnParentController(parent -> parent.onChildDestroyed((Component) getView()));
        }
        super.destroy();
    }

    protected boolean isRoot() {
        return getParentController() == null &&
                !(this instanceof Navigator) &&
                getView().getParent() != null;
    }
}
