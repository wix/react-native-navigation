package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.support.annotation.CallSuper;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.ViewTreeObserver;

import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.parse.params.NullBool;
import com.reactnativenavigation.presentation.FabPresenter;
import com.reactnativenavigation.utils.CommandListener;
import com.reactnativenavigation.utils.Functions.Func1;
import com.reactnativenavigation.utils.StringUtils;
import com.reactnativenavigation.utils.UiThread;
import com.reactnativenavigation.utils.UiUtils;
import com.reactnativenavigation.viewcontrollers.stack.StackController;
import com.reactnativenavigation.views.Component;
import com.reactnativenavigation.views.Renderable;
import com.reactnativenavigation.views.element.Element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.reactnativenavigation.utils.CollectionUtils.forEach;

public abstract class ViewController<T extends ViewGroup> implements ViewTreeObserver.OnGlobalLayoutListener, ViewGroup.OnHierarchyChangeListener {

    private final List<Runnable> onAppearedListeners = new ArrayList();
    private boolean appearEventPosted;
    private boolean isFirstLayout = true;
    private Bool waitForRender = new NullBool();

    public interface ViewVisibilityListener {
        /**
         * @return true if the event is consumed, false otherwise
         */
        boolean onViewAppeared(View view);

        /**
         * @return true if the event is consumed, false otherwise
         */
        boolean onViewDisappear(View view);
    }

    public Options initialOptions;
    public Options options;

    private final Activity activity;
    private final String id;
    private YellowBoxDelegate yellowBoxDelegate;
    @Nullable protected T view;
    @Nullable private ParentController<T> parentController;
    private boolean isShown;
    private boolean isDestroyed;
    private ViewVisibilityListener viewVisibilityListener = new ViewVisibilityListenerAdapter();
    protected FabPresenter fabOptionsPresenter;

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public ViewController(Activity activity, String id, YellowBoxDelegate yellowBoxDelegate, Options initialOptions) {
        this.activity = activity;
        this.id = id;
        this.yellowBoxDelegate = yellowBoxDelegate;
        fabOptionsPresenter = new FabPresenter();
        this.initialOptions = initialOptions;
        options = initialOptions.copy();
    }

    public void setWaitForRender(Bool waitForRender) {
        this.waitForRender = waitForRender;
    }

    public void addOnAppearedListener(Runnable onAppearedListener) {
        onAppearedListeners.add(onAppearedListener);
    }

    public void removeOnAppearedListener(Runnable onAppearedListener) {
        onAppearedListeners.remove(onAppearedListener);
    }

    protected abstract T createView();

    public void setViewVisibilityListener(ViewVisibilityListener viewVisibilityListener) {
        this.viewVisibilityListener = viewVisibilityListener;
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
    public void ensureViewIsCreated() {
        getView();
    }

    public boolean handleBack(CommandListener listener) {
        return false;
    }

    @CheckResult
    public Options resolveCurrentOptions() {
        return options;
    }

    @CheckResult
    public Options resolveCurrentOptions(Options defaultOptions) {
        return options.copy().withDefaultOptions(defaultOptions);
    }

    @CallSuper
    public void mergeOptions(Options options) {
        this.initialOptions = this.initialOptions.mergeWith(options);
        this.options = this.options.mergeWith(options);
        if (getParentController() != null) {
            this.options.clearOneTimeOptions();
            initialOptions.clearOneTimeOptions();
        }
    }

    @CallSuper
    public void applyOptions(Options options) {

    }

    public void setDefaultOptions(Options defaultOptions) {
        
    }

    public Activity getActivity() {
        return activity;
    }

    protected void performOnParentController(Func1<ParentController> task) {
        if (parentController != null) task.run(parentController);
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    public ParentController getParentController() {
        return parentController;
    }

    public void setParentController(@NonNull final ParentController parentController) {
        this.parentController = parentController;
    }

    public void performOnParentStack(Func1<StackController> task) {
        if (parentController instanceof StackController) {
            task.run((StackController) parentController);
        } else if (this instanceof StackController) {
            task.run((StackController) this);
        } else if (parentController != null){
            parentController.performOnParentStack(task);
        }
    }

    public T getView() {
        if (view == null) {
            if (isDestroyed) {
                throw new RuntimeException("Tried to create view after it has already been destroyed");
            }
            view = createView();
            view.setOnHierarchyChangeListener(this);
            view.getViewTreeObserver().addOnGlobalLayoutListener(this);
        }
        return view;
    }

    public void detachView() {
        if (view == null || view.getParent() == null) return;
        ((ViewManager) view.getParent()).removeView(view);
    }

    public void attachView(ViewGroup parent, int index) {
        if (view == null) return;
        if (view.getParent() == null) parent.addView(view, index);
    }

    public String getId() {
        return id;
    }

    boolean isSameId(final String id) {
        return StringUtils.isEqual(this.id, id);
    }

    @Nullable
    public ViewController findController(String id) {
        return isSameId(id) ? this : null;
    }

    public boolean containsComponent(Component component) {
        return getView().equals(component);
    }

    public void onViewWillAppear() {

    }

    @CallSuper
    public void onViewAppeared() {
        isShown = true;
        applyOptions(options);
        performOnParentController(parentController -> {
            parentController.clearOptions();
            if (getView() instanceof Component) parentController.applyChildOptions(options, (Component) getView());
        });
        if (!onAppearedListeners.isEmpty() && !appearEventPosted) {
            appearEventPosted = true;
            UiThread.post(() -> {
                forEach(onAppearedListeners, Runnable::run);
                onAppearedListeners.clear();
            });
        }
    }

    public void onViewWillDisappear() {

    }

    @CallSuper
    public void onViewDisappear() {
        isShown = false;
    }

    @CallSuper
    public void destroy() {
        if (isShown) {
            isShown = false;
            onViewDisappear();
        }
        yellowBoxDelegate.destroy();
        if (view instanceof Destroyable) {
            ((Destroyable) view).destroy();
        }
        if (view != null) {
            view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            view.setOnHierarchyChangeListener(null);
            if (view.getParent() instanceof ViewGroup) {
                ((ViewManager) view.getParent()).removeView(view);
            }
            view = null;
            isDestroyed = true;
        }
    }

    @Override
    public void onGlobalLayout() {
        if (isFirstLayout) {
            onAttachToParent();
            isFirstLayout = false;
        }
        if (!isShown && isViewShown()) {
            if (!viewVisibilityListener.onViewAppeared(view)) {
                isShown = true;
                onViewAppeared();
            }
        } else if (isShown && !isViewShown()) {
            if (!viewVisibilityListener.onViewDisappear(view)) {
                isShown = false;
                onViewDisappear();
            }
        }
    }

    protected void onAttachToParent() {

    }

    @Override
    public void onChildViewAdded(View parent, View child) {
        yellowBoxDelegate.onChildViewAdded(parent, child);
    }

    @Override
    public void onChildViewRemoved(View view, View view1) {

    }

    void runOnPreDraw(Func1<T> task) {
        UiUtils.runOnPreDrawOnce(getView(), () -> task.run(getView()));
    }

    public abstract void sendOnNavigationButtonPressed(String buttonId);

    public boolean isViewShown() {
        return !isDestroyed &&
               getView().isShown() &&
               view != null &&
               isRendered();
    }

    public boolean isRendered() {
        return view != null && (
                waitForRender.isFalseOrUndefined() ||
                !(view instanceof Renderable) ||
                ((Renderable) view).isRendered()
        );
    }

    void applyOnController(ViewController controller, Func1<ViewController> task) {
        if (controller != null) task.run(controller);
    }

    public List<Element> getElements() {
        return getView() instanceof IReactView && view != null? ((IReactView) view).getElements() : Collections.EMPTY_LIST;
    }
}
