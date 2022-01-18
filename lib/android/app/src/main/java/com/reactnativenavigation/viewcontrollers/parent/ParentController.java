package com.reactnativenavigation.viewcontrollers.parent;

import static com.reactnativenavigation.utils.CollectionUtils.forEach;
import static com.reactnativenavigation.utils.ObjectUtils.perform;

import android.app.Activity;
import android.content.res.Configuration;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.reactnativenavigation.options.Options;
import com.reactnativenavigation.options.OverlayAttachOptions;
import com.reactnativenavigation.options.params.Bool;
import com.reactnativenavigation.utils.CollectionUtils;
import com.reactnativenavigation.viewcontrollers.bottomtabs.BottomTabsController;
import com.reactnativenavigation.viewcontrollers.child.ChildController;
import com.reactnativenavigation.viewcontrollers.child.ChildControllersRegistry;
import com.reactnativenavigation.viewcontrollers.stack.StackController;
import com.reactnativenavigation.viewcontrollers.viewcontroller.Presenter;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController;
import com.reactnativenavigation.views.component.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

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

    public List<ViewController<?>> getChildren(){
        return Collections.emptyList();
    }

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

    @Override
    protected View findTooltipAnchorView(OverlayAttachOptions options) {
        final String id = options.anchorId.get();
        View found = null;
        final View topBarView = findTopBarViewById(id);
        if (topBarView != null) {
            found = topBarView;
        } else {
            final View bottomTabViewById = findBottomTabViewById(id);
            if (bottomTabViewById != null) {
                found = bottomTabViewById;
            }
        }
        return found;
    }

    @Nullable
    protected View findTopBarViewById(String id) {
        final View[] found = {null};
        lookup((controller) -> {
            if (controller instanceof StackController) {
                final StackController stackController = (StackController) controller;
                final View topBarViewById = stackController.presenter.findTopBarViewById(id);
                found[0] = topBarViewById;
                return topBarViewById != null;
            }
            return false;
        });
        return found[0];
    }

    @Nullable
    protected View findBottomTabViewById(@NonNull String id) {
        final View[] found = {null};
        lookup((controller) -> {
            if (controller instanceof BottomTabsController) {
                BottomTabsController bottomTabsController = (BottomTabsController) controller;
                found[0] = bottomTabsController.getTabViewByTag(id);
                return found[0] != null;
            }
            return false;
        });
        return found[0];
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

    protected interface LookupPredicate<T> {
        boolean test(T t);
    }
    public ViewController<?> lookup(LookupPredicate<ViewController<?>> predicate){
        if(predicate.test(this)){
            return this;
        }else{
            final List<ViewController<?>> children = getChildren();
            for(ViewController<?> child : children){
               if(child instanceof ParentController){
                   ViewController<?> result= ((ParentController<?>) child).lookup(predicate);
                   if(result!=null){
                       return result;
                   }
               }else{
                   if(predicate.test(child)){
                       return child;
                   }
               }
            }
            return null;
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Collection<? extends ViewController<?>> childControllers = getChildControllers();
        for(ViewController<?> controller: childControllers){
            controller.onConfigurationChanged(newConfig);
        }
    }
}
