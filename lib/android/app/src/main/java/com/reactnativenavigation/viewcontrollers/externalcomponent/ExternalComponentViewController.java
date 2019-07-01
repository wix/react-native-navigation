package com.reactnativenavigation.viewcontrollers.externalcomponent;

import android.app.Activity;
import androidx.fragment.app.FragmentActivity;
import androidx.core.view.ViewCompat;
import android.view.View;

import com.facebook.react.ReactInstanceManager;
import com.reactnativenavigation.parse.ExternalComponent;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.presentation.ExternalComponentPresenter;
import com.reactnativenavigation.react.EventEmitter;
import com.reactnativenavigation.utils.CoordinatorLayoutUtils;
import com.reactnativenavigation.utils.StatusBarUtils;
import com.reactnativenavigation.viewcontrollers.NoOpYellowBoxDelegate;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.views.BehaviourDelegate;
import com.reactnativenavigation.views.ExternalComponentLayout;

import static com.reactnativenavigation.utils.ObjectUtils.perform;

public class ExternalComponentViewController extends ViewController<ExternalComponentLayout> {
    private final ExternalComponent externalComponent;
    private final ExternalComponentCreator componentCreator;
    private ReactInstanceManager reactInstanceManager;
    private final EventEmitter emitter;
    private final ExternalComponentPresenter presenter;

    public ExternalComponentViewController(Activity activity, String id, ExternalComponent externalComponent, ExternalComponentCreator componentCreator, ReactInstanceManager reactInstanceManager, EventEmitter emitter, ExternalComponentPresenter presenter, Options initialOptions) {
        super(activity, id, new NoOpYellowBoxDelegate(), initialOptions);
        this.externalComponent = externalComponent;
        this.componentCreator = componentCreator;
        this.reactInstanceManager = reactInstanceManager;
        this.emitter = emitter;
        this.presenter = presenter;
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
    public void mergeOptions(Options options) {
        if (options == Options.EMPTY) return;
        performOnParentController(parentController -> parentController.mergeChildOptions(options, this));
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

    public FragmentActivity getActivity() {
        return (FragmentActivity) super.getActivity();
    }

    private void enableDrawingBehindStatusBar(View view) {
        view.setFitsSystemWindows(true);
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, insets) -> insets);
    }
}
