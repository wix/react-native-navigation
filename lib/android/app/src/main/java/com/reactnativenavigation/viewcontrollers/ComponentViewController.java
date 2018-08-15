package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.presentation.OptionsPresenter;
import com.reactnativenavigation.views.ComponentLayout;
import com.reactnativenavigation.views.ReactComponent;

public class ComponentViewController extends ChildController<ComponentLayout> {

    private final String componentName;

    private final ReactViewCreator viewCreator;

    public ComponentViewController(final Activity activity,
                                   final ChildControllersRegistry childRegistry,
                                   final String id,
                                   final String componentName,
                                   final ReactViewCreator viewCreator,
                                   final Options initialOptions,
                                   final OptionsPresenter presenter) {
        super(activity, childRegistry, id, presenter, initialOptions);
        this.componentName = componentName;
        this.viewCreator = viewCreator;
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
        getView().sendOnNavigationButtonPressed(buttonId);
    }

    @Override
    public void applyOptions(Options options) {
        super.applyOptions(options);
        view.applyOptions(options);
    }

    @Override
    protected boolean isViewShown() {
        return super.isViewShown() && view.isReady();
    }

    @NonNull
    @Override
    protected ComponentLayout createView() {
        view = (ComponentLayout) viewCreator.create(getActivity(), getId(), componentName);
        return (ComponentLayout) view.asView();
    }

    @Override
    public void mergeOptions(Options options) {
        performOnParentController(parentController -> parentController.mergeChildOptions(options, getView()));
        super.mergeOptions(options);
    }

    @Override
    public void destroy() {
        if (!isDestroyed()) performOnParentController(parent -> parent.onChildDestroyed(getView()));
        super.destroy();
    }

    ReactComponent getComponent() {
        return view;
    }
}
