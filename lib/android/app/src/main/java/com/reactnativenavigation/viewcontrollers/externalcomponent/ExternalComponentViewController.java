package com.reactnativenavigation.viewcontrollers.externalcomponent;

import android.app.Activity;
import android.view.View;

import com.facebook.react.ReactInstanceManager;
import com.reactnativenavigation.parse.ExternalComponent;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.presentation.ExternalComponentPresenter;
import com.reactnativenavigation.presentation.Presenter;
import com.reactnativenavigation.react.events.ComponentType;
import com.reactnativenavigation.react.events.EventEmitter;
import com.reactnativenavigation.utils.CoordinatorLayoutUtils;
import com.reactnativenavigation.utils.StatusBarUtils;
import com.reactnativenavigation.viewcontrollers.ChildController;
import com.reactnativenavigation.viewcontrollers.ChildControllersRegistry;
import com.reactnativenavigation.views.BehaviourDelegate;
import com.reactnativenavigation.views.ExternalComponentLayout;

import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentActivity;

import static com.reactnativenavigation.utils.ObjectUtils.perform;

public class ExternalComponentViewController extends ChildController<ExternalComponentLayout> {
    private final ExternalComponent externalComponent;
    private final ExternalComponentCreator componentCreator;
    private ReactInstanceManager reactInstanceManager;
    private final EventEmitter emitter;
    private final ExternalComponentPresenter presenter;

    public ExternalComponentViewController(Activity activity, ChildControllersRegistry childRegistry, String id, Presenter presenter, ExternalComponent externalComponent, ExternalComponentCreator componentCreator, ReactInstanceManager reactInstanceManager, EventEmitter emitter, ExternalComponentPresenter externalComponentPresenter, Options initialOptions) {
        super(activity, childRegistry, id, presenter, initialOptions);
        this.externalComponent = externalComponent;
        this.componentCreator = componentCreator;
        this.reactInstanceManager = reactInstanceManager;
        this.emitter = emitter;
        this.presenter = externalComponentPresenter;
    }

    @Override
    protected ExternalComponentLayout createView() {
        ExternalComponentLayout content = new ExternalComponentLayout(getActivity());
        enableDrawingBehindStatusBar(content);
        content.addView(componentCreator
                .create(getActivity(), reactInstanceManager, externalComponent.passProps)
                .asView(), CoordinatorLayoutUtils.matchParentWithBehaviour(new BehaviourDelegate(this)));
        return content;
    }

    @Override
    public void sendOnNavigationButtonPressed(String buttonId) {
        emitter.emitOnNavigationButtonPressed(getId(), buttonId);
    }

    @Override
    public void onViewWillAppear() {
        super.onViewWillAppear();
        emitter.emitComponentDidAppear(getId(), externalComponent.name.get(), ComponentType.Component);
    }

    @Override
    public void onViewDisappear() {
        super.onViewDisappear();
        emitter.emitComponentDidDisappear(getId(), externalComponent.name.get(), ComponentType.Component);
    }

    @Override
    public void applyTopInset() {
        if (view != null) presenter.applyTopInsets(view, getTopInset());
    }

    @Override
    public int getTopInset() {
        int statusBarInset = resolveCurrentOptions().statusBar.drawBehind.isTrue() ? 0 : StatusBarUtils.getStatusBarHeight(getActivity());
        return statusBarInset + perform(getParentController(), 0, p -> p.getTopInset(this));
    }

    @Override
    public void applyBottomInset() {
        if (view != null) presenter.applyBottomInset(view, getBottomInset());
    }

    @Override
    public String getCurrentComponentName() {
        return externalComponent.name.get();
    }

    public FragmentActivity getActivity() {
        return (FragmentActivity) super.getActivity();
    }

    private void enableDrawingBehindStatusBar(View view) {
        view.setFitsSystemWindows(true);
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, insets) -> insets);
    }
}
