package com.reactnativenavigation.viewcontrollers.parent;

import android.app.Activity;
import android.content.res.Configuration;
import android.view.View;
import android.view.ViewGroup;

import com.reactnativenavigation.options.Options;
import com.reactnativenavigation.options.params.Bool;
import com.reactnativenavigation.utils.CollectionUtils;
import com.reactnativenavigation.viewcontrollers.bottomtabs.BottomTabsController;
import com.reactnativenavigation.viewcontrollers.child.ChildController;
import com.reactnativenavigation.viewcontrollers.child.ChildControllersRegistry;
import com.reactnativenavigation.viewcontrollers.viewcontroller.Presenter;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController;
import com.reactnativenavigation.views.component.Component;

import java.util.Collection;

import androidx.annotation.CallSuper;
import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import static com.reactnativenavigation.utils.CollectionUtils.*;
import static com.reactnativenavigation.utils.ObjectUtils.perform;

public abstract class ParentController<T extends ViewGroup> extends ChildController<T> {

    public ParentController(Activity activity, ChildControllersRegistry childRegistry, String id, Presenter presenter, Options initialOptions) {
        super(activity, childRegistry, id, presenter, initialOptions);
    }

    @Override
    public void setWaitForRender(Bool waitForRender) {
        super.setWaitForRender(waitForRender);
        applyOnController(getCurrentChild(), currentChild -> currentChild.setWaitForRender(waitForRender));
    }

    @Override
    public void setDefaultOptions(Options defaultOptions) {
        super.setDefaultOptions(defaultOptions);
        forEach(getChildControllers(), child -> child.setDefaultOptions(defaultOptions));
    }

    @Override
    public void onViewDidAppear() {
        super.onViewDidAppear();
        ViewController<?> currentChild = getCurrentChild();
        if (currentChild != null) currentChild.onViewDidAppear();
    }

    @Override
    public void onViewDisappear() {
        super.onViewDisappear();
        ViewController<?> currentChild = getCurrentChild();
        if (currentChild != null) currentChild.onViewDisappear();
    }

    @Override
    @CheckResult
    public Options resolveCurrentOptions() {
        if (CollectionUtils.isNullOrEmpty(getChildControllers())) return initialOptions;
        return getCurrentChild()
                .resolveCurrentOptions()
                .copy()
                .withDefaultOptions(initialOptions);
    }

    public Options resolveChildOptions(ViewController<?> child) {
        if (child == this) return resolveCurrentOptions();
        return child
                .resolveCurrentOptions()
                .copy()
                .withDefaultOptions(initialOptions);
    }

    @Override
    @CheckResult
    public Options resolveCurrentOptions(Options defaultOptions) {
        return resolveCurrentOptions().withDefaultOptions(defaultOptions);
    }

    public boolean isCurrentChild(ViewController<?> child) {
        return getCurrentChild() == child;
    }

    public abstract ViewController<?> getCurrentChild();

    @NonNull
    @Override
    public abstract T createView();

    @NonNull
    public abstract Collection<? extends ViewController<?>> getChildControllers();

    @Nullable
    protected BottomTabsController getBottomTabsController() {
        if (this instanceof BottomTabsController) {
            return (BottomTabsController) this;
        }
        return perform(getParentController(), null, ParentController::getBottomTabsController);
    }

    @Nullable
    @Override
    public ViewController<?> findController(final String id) {
        ViewController<?> fromSuper = super.findController(id);
        if (fromSuper != null) return fromSuper;

        for (ViewController<?> child : getChildControllers()) {
            ViewController<?> fromChild = child.findController(id);
            if (fromChild != null) return fromChild;
        }

        return null;
    }

    @Nullable
    @Override
    public ViewController<?> findController(View child) {
        ViewController<?> fromSuper = super.findController(child);
        if (fromSuper != null) return fromSuper;

        for (ViewController<?> childController : getChildControllers()) {
            ViewController<?> fromChild = childController.findController(child);
            if (fromChild != null) return fromChild;
        }

        return null;
    }

    @Override
    public boolean containsComponent(Component component) {
        if (super.containsComponent(component)) {
            return true;
        }
        for (ViewController<?> child : getChildControllers()) {
            if (child.containsComponent(component)) return true;
        }
        return false;
    }

    @CallSuper
    public void applyChildOptions(Options options, ViewController<?> child) {
        this.options = initialOptions.mergeWith(options);
    }

    @CallSuper
    public void mergeChildOptions(Options options, ViewController<?> child) {
    }

    @Override
    public void destroy() {
        super.destroy();
        forEach(getChildControllers(), ViewController::destroy);
    }

    @SuppressWarnings("WeakerAccess")
    @CallSuper
    public void clearOptions() {
        performOnParentController(ParentController::clearOptions);
        options = initialOptions.copy().clearOneTimeOptions();
    }

    public void setupTopTabsWithViewPager(ViewPager viewPager) {

    }

    public void clearTopTabs() {

    }

    @Override
    public boolean isRendered() {
        return getCurrentChild() != null && getCurrentChild().isRendered();
    }

    public void onChildDestroyed(ViewController<?> child) {

    }

    @Override
    public void applyTopInset() {
        forEach(getChildControllers(), ViewController::applyTopInset);
    }

    public int getTopInset(ViewController<?> child) {
        return perform(getParentController(), 0, p -> p.getTopInset(child));
    }

    @Override
    public void applyBottomInset() {
        forEach(getChildControllers(), ViewController::applyBottomInset);
    }

    public int getBottomInset(ViewController<?> child) {
        return perform(getParentController(), 0, p -> p.getBottomInset(child));
    }

    @Override
    public String getCurrentComponentName() {
        return getCurrentChild().getCurrentComponentName();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Collection<? extends ViewController<?>> childControllers = getChildControllers();
        for(ViewController<?> controller: childControllers){
            controller.onConfigurationChanged(newConfig);
        }
    }
}
