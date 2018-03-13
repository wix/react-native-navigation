package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.ViewTreeObserver;

import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.presentation.FabOptionsPresenter;
import com.reactnativenavigation.utils.CompatUtils;
import com.reactnativenavigation.utils.StringUtils;
import com.reactnativenavigation.utils.Task;
import com.reactnativenavigation.views.Component;

public abstract class ViewController<T extends ViewGroup> implements ViewTreeObserver.OnGlobalLayoutListener {

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

    Options initialOptions;
    public Options options;

    private final Activity activity;
    private final String id;
    protected T view;
    @Nullable private ParentController<T> parentController;
    private boolean isShown;
    private boolean isDestroyed;
    private ViewVisibilityListener viewVisibilityListener = new ViewVisibilityListenerAdapter();
    FabOptionsPresenter fabOptionsPresenter;

    public ViewController(Activity activity, String id, Options initialOptions) {
        this.activity = activity;
        this.id = id;
        fabOptionsPresenter = new FabOptionsPresenter();
        this.initialOptions = initialOptions;
        options = initialOptions.copy();
    }

    protected abstract T createView();

    public void setViewVisibilityListener(ViewVisibilityListener viewVisibilityListener) {
        this.viewVisibilityListener = viewVisibilityListener;
    }

    @SuppressWarnings("WeakerAccess")
    @VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
    public void ensureViewIsCreated() {
        getView();
    }

    public boolean handleBack() {
        return false;
    }

    public void applyOptions(Options options) {

    }

    public Activity getActivity() {
        return activity;
    }

    protected void applyOnParentController(Task<ParentController> task) {
        if (parentController != null) task.run(parentController);
    }

    @Nullable
    ParentController getParentController() {
        return parentController;
    }

    public void setParentController(@NonNull final ParentController parentController) {
        this.parentController = parentController;
    }

    boolean performOnParentStack(Task<StackController> task) {
        if (parentController instanceof StackController) {
            task.run((StackController) parentController);
            return true;
        }
        if (this instanceof StackController) {
            task.run((StackController) this);
            return true;
        }
        return false;
    }

    void performOnParentStack(Task accept, Runnable reject) {
        if (!performOnParentStack(accept)) {
            reject.run();
        }
    }

    @NonNull
    public T getView() {
        if (view == null) {
            if (isDestroyed) {
                throw new RuntimeException("Tried to create view after it has already been destroyed");
            }
            view = createView();
            view.setId(CompatUtils.generateViewId());
            view.getViewTreeObserver().addOnGlobalLayoutListener(this);
        }
        return view;
    }

    public String getId() {
        return id;
    }

    boolean isSameId(final String id) {
        return StringUtils.isEqual(this.id, id);
    }

    @Nullable
    public ViewController findControllerById(String id) {
        return isSameId(id) ? this : null;
    }

    public boolean containsComponent(Component component) {
        return getView().equals(component);
    }

    public void onViewRegainedFocus() {
        applyOptions(options);
    }

    public void onViewLostFocus() {

    }

    public void onViewWillAppear() {

    }

    public void onViewAppeared() {
        isShown = true;
        applyOptions(options);
        applyOnParentController(parentController -> {
            parentController.clearOptions();
            if (getView() instanceof Component) parentController.applyOptions(options, (Component) getView());
        });
    }

    public void onViewWillDisappear() {

    }

    public void onViewDisappear() {
        isShown = false;
    }

    @CallSuper
    public void destroy() {
        if (isShown) {
            isShown = false;
            onViewDisappear();
            if (view instanceof Destroyable) {
                ((Destroyable) view).destroy();
            }
        }
        if (view != null) {
            view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            if (view.getParent() instanceof ViewGroup) {
                ((ViewManager) view.getParent()).removeView(view);
            }
            view = null;
            isDestroyed = true;
        }
    }

    @Override
    public void onGlobalLayout() {
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

    public abstract void sendOnNavigationButtonPressed(String buttonId);

    protected boolean isViewShown() {
        return !isDestroyed && getView().isShown();
    }
}
