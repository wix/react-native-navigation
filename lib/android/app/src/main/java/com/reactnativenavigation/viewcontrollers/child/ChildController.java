package com.reactnativenavigation.viewcontrollers.child;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;

import com.reactnativenavigation.options.Options;
import com.reactnativenavigation.utils.LogKt;
import com.reactnativenavigation.viewcontrollers.parent.ParentController;
import com.reactnativenavigation.viewcontrollers.viewcontroller.Presenter;
import com.reactnativenavigation.viewcontrollers.viewcontroller.NoOpYellowBoxDelegate;
import com.reactnativenavigation.viewcontrollers.navigator.Navigator;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController;
import com.reactnativenavigation.viewcontrollers.viewcontroller.overlay.ViewControllerOverlay;
import com.reactnativenavigation.views.component.Component;

import androidx.annotation.CallSuper;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

public abstract class ChildController<T extends ViewGroup> extends ViewController<T> {
    private final Presenter presenter;
    private final ChildControllersRegistry childRegistry;

    public ChildControllersRegistry getChildRegistry() {
        return childRegistry;
    }

    public ChildController(Activity activity, ChildControllersRegistry childRegistry, String id, Presenter presenter, Options initialOptions) {
        super(activity, id, new NoOpYellowBoxDelegate(activity), initialOptions, new ViewControllerOverlay(activity));
        this.presenter = presenter;
        this.childRegistry = childRegistry;
    }

    @Override
    public T getView() {
        if (view == null) {
            super.getView();
            view.setFitsSystemWindows(true);
            ViewCompat.setOnApplyWindowInsetsListener(view, this::onApplyWindowInsets);
        }
        return view;
    }

    @Override
    @CallSuper
    public void setDefaultOptions(Options defaultOptions) {
        presenter.setDefaultOptions(defaultOptions);
    }

    @Override
    public void onViewWillAppear() {
        super.onViewWillAppear();
        childRegistry.onViewAppeared(this);
    }

    @Override
    public void onViewDisappear() {
        super.onViewDisappear();
        childRegistry.onViewDisappear(this);
    }

    public void onViewBroughtToFront() {
        presenter.onViewBroughtToFront(this, resolveCurrentOptions());
    }

    @Override
    public void applyOptions(Options options) {
        super.applyOptions(options);
        presenter.applyOptions(this, resolveCurrentOptions());
    }

    @Override
    public void mergeOptions(Options options) {
        if (options == Options.EMPTY) return;
        if (isViewShown()) presenter.mergeOptions(this, options);
        super.mergeOptions(options);
        performOnParentController(parentController -> parentController.mergeChildOptions(options, this));
    }

    @Override
    public void destroy() {
        if (!isDestroyed() && getView() instanceof Component) {
            performOnParentController(parent -> parent.onChildDestroyed(this));
        }
        super.destroy();
        childRegistry.onChildDestroyed(this);
    }

    public boolean isRoot() {
        return getParentController() == null &&
                !(this instanceof Navigator) &&
                getView().getParent() != null;
    }

    protected WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat insets) {
       return insets;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        presenter.onConfigurationChanged(this, options);
    }
}
