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
                                   final Options initialNavigationOptions) {
        super(activity, id);
        this.componentName = componentName;
        this.viewCreator = viewCreator;
        options = initialNavigationOptions;
    }

    @Override
    public void onViewAppeared() {
        super.onViewAppeared();
        view.applyOptions(options);
        view.sendComponentStart();
    }

    @Override
    public void onViewDisappear() {
        view.sendComponentStop();
        super.onViewDisappear();
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
        this.options.mergeWith(options);
        view.applyOptions(this.options);
        applyOnParentController(parentController -> parentController.applyOptions(this.options, view));
    }

    Options getOptions() {
        return options;
    }

    ReactComponent getComponent() {
        return view;
    }
}
