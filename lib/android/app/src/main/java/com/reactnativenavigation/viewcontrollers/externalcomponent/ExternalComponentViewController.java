package com.reactnativenavigation.viewcontrollers.externalcomponent;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;

import com.facebook.react.ReactInstanceManager;
import com.reactnativenavigation.parse.ExternalComponent;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.react.EventEmitter;
import com.reactnativenavigation.viewcontrollers.NoOpYellowBoxDelegate;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.views.ExternalComponentLayout;

public class ExternalComponentViewController extends ViewController<ExternalComponentLayout> {
    private final ExternalComponent externalComponent;
    private final ExternalComponentCreator componentCreator;
    private ReactInstanceManager reactInstanceManager;
    private final EventEmitter emitter;

    public ExternalComponentViewController(Activity activity, String id, ExternalComponent externalComponent, ExternalComponentCreator componentCreator, ReactInstanceManager reactInstanceManager, EventEmitter emitter, Options initialOptions) {
        super(activity, id, new NoOpYellowBoxDelegate(), initialOptions);
        this.externalComponent = externalComponent;
        this.componentCreator = componentCreator;
        this.reactInstanceManager = reactInstanceManager;
        this.emitter = emitter;
    }

    @Override
    protected ExternalComponentLayout createView() {
        ExternalComponentLayout content = new ExternalComponentLayout(getActivity());
        content.addView(componentCreator
                .create(getActivity(), reactInstanceManager, externalComponent.passProps)
                .asView());
        return content;
    }

    @Override
    public void sendOnNavigationButtonPressed(String buttonId) {
        emitter.emitOnNavigationButtonPressed(getId(), buttonId);
    }

    @Override
    public void mergeOptions(Options options) {
        if (options == Options.EMPTY) return;
        performOnParentController(parentController -> parentController.mergeChildOptions(options, this, getView()));
        super.mergeOptions(options);
    }

    @Override
    public void onViewAppeared() {
        super.onViewAppeared();
        emitter.emitComponentDidAppear(getId(), externalComponent.name.get());
    }

    @Override
    public void onViewDisappear() {
        super.onViewDisappear();
        emitter.emitComponentDidDisappear(getId(), externalComponent.name.get());
    }

    public FragmentActivity getActivity() {
        return (FragmentActivity) super.getActivity();
    }
}
