package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;

import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.presentation.Presenter;
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
                                   final Presenter presenter) {
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
    public boolean isViewShown() {
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
        if (options == Options.EMPTY) return;
        performOnParentController(parentController -> parentController.mergeChildOptions(options, this, getView()));
        super.mergeOptions(options);
    }

    ReactComponent getComponent() {
        return view;
    }

    @Override
    public void destroy() {
        final boolean blurOnUnmount = options != null && options.modal.blurOnUnmount.isTrue();
        if (blurOnUnmount) {
            blurActivityFocus();
        }
        super.destroy();
    }

    private void blurActivityFocus() {
        final Activity activity = getActivity();
        final View focusView = activity != null ? activity.getCurrentFocus() : null;
        if (focusView != null) {
            focusView.clearFocus();
        }
    }
}
