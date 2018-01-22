package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.ViewTreeObserver;

import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.utils.CompatUtils;
import com.reactnativenavigation.utils.DeviceScreen;
import com.reactnativenavigation.utils.StringUtils;
import com.reactnativenavigation.utils.Task;
import com.reactnativenavigation.views.ReactComponent;

public abstract class ViewController<T extends ViewGroup> implements ViewTreeObserver.OnGlobalLayoutListener {

    public Options options;

    private final Activity activity;
    private final String id;
    protected T view;
    @Nullable private ParentController<T> parentController;
    private boolean isShown = false;
    private boolean isDestroyed;

    public ViewController(Activity activity, String id) {
        this.activity = activity;
        this.id = id;
    }

    protected abstract T createView();

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

    public void onViewAppeared() {
        isShown = true;
        applyOnParentController(parentController -> parentController.applyOptions(options, (ReactComponent) getView()));
    }

    public void onViewDisappear() {
        isShown = false;
    }

    public void destroy() {
        if (isShown) {
            isShown = false;
            onViewDisappear();
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
            isShown = true;
            onViewAppeared();
        } else if (isShown && !isViewShown()) {
            isShown = false;
            onViewDisappear();
        }
//        UiUtils.runOnPreDrawOnce(view, () -> {
//            Log.i("ViewController", "onGlobalLayout: " + title() + " isShown: " + isShown + " isViewShown(): " + isViewShown());
//            if (!isShown && isViewShown()) {
//                isShown = true;
//                onViewAppeared();
//            } else if (isShown && !isViewShown()) {
//                isShown = false;
//                onViewDisappear();
//            }
//        });
    }

    protected boolean isViewShown() {
        return !isDestroyed && getView().isShown() && isViewInScreenBounds();
    }

    private boolean isViewInScreenBounds() {
        return view.getX() >= 0 && view.getX() < DeviceScreen.width(view.getResources());
    }

    private String title() {
        if (options == null) return "";
        return options.topBarOptions.title.get("");
    }
}
