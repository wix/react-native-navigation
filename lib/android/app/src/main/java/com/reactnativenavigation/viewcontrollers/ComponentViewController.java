package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.presentation.NavigationOptionsListener;
import com.reactnativenavigation.views.ComponentLayout;
import com.reactnativenavigation.views.ReactComponent;

public class ComponentViewController extends ViewController<ComponentLayout> implements NavigationOptionsListener {

    private final String componentName;

    private final ReactViewCreator viewCreator;

    public ComponentViewController(final Activity activity,
                                   final String id,
                                   final String componentName,
                                   final ReactViewCreator viewCreator,
                                   final Options initialOptions) {
        super(activity, id, initialOptions);
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
        view.applyOptions(options);
        applyOnParentController(parentController -> parentController.mergeChildOptions(options, view));
        this.options = this.options.mergeWith(options);
    }

    ReactComponent getComponent() {
        return view;
    }
}
