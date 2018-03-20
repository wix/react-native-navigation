package com.reactnativenavigation.viewcontrollers.externalcomponent;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;

import com.reactnativenavigation.parse.ExternalComponent;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.views.ExternalComponentLayout;

public class ExternalComponentViewController extends ViewController<ExternalComponentLayout> {
    private final ExternalComponent externalComponent;
    private final ExternalComponentCreator componentCreator;

    public ExternalComponentViewController(Activity activity, String id, ExternalComponent externalComponent, ExternalComponentCreator componentCreator, Options initialOptions) {
        super(activity, id, initialOptions);
        this.externalComponent = externalComponent;
        this.componentCreator = componentCreator;
    }

    @Override
    public void applyOptions(Options options) {
        getView().applyOptions(options);
    }

    @Override
    protected ExternalComponentLayout createView() {
        ExternalComponentLayout content = new ExternalComponentLayout(getActivity());
        content.addView(componentCreator.create(getActivity(), externalComponent.passProps).asView());
        return content;
    }

    @Override
    public void sendOnNavigationButtonPressed(String buttonId) {

    }

    public FragmentActivity getActivity() {
        return (FragmentActivity) super.getActivity();
    }
}
