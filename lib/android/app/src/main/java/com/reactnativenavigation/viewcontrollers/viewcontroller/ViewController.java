package com.reactnativenavigation.viewcontrollers.viewcontroller;

import static com.reactnativenavigation.utils.CollectionUtils.forEach;
import static com.reactnativenavigation.utils.ObjectUtils.perform;

import android.app.Activity;
import android.content.res.Configuration;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.ViewTreeObserver;

import androidx.annotation.CallSuper;
import androidx.annotation.CheckResult;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.reactnativenavigation.options.Options;
import com.reactnativenavigation.options.params.Bool;
import com.reactnativenavigation.options.params.NullBool;
import com.reactnativenavigation.react.CommandListener;
import com.reactnativenavigation.utils.Functions.Func1;
import com.reactnativenavigation.utils.StringUtils;
import com.reactnativenavigation.utils.UiThread;
import com.reactnativenavigation.utils.UiUtils;
import com.reactnativenavigation.viewcontrollers.parent.ParentController;
import com.reactnativenavigation.viewcontrollers.stack.StackController;
import com.reactnativenavigation.viewcontrollers.viewcontroller.overlay.ViewControllerOverlay;
import com.reactnativenavigation.views.BehaviourAdapter;
import com.reactnativenavigation.views.component.Component;
import com.reactnativenavigation.views.component.Renderable;

import java.util.ArrayList;
import java.util.List;

public abstract class ViewController<T extends ViewGroup> implements ViewTreeObserver.OnGlobalLayoutListener,
        ViewGroup.OnHierarchyChangeListener,
        BehaviourAdapter {

    private final List<Runnable> onAppearedListeners = new ArrayList<>();
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
    private final YellowBoxDelegate yellowBoxDelegate;
    @Nullable
    protected T view;
    @Nullable
    private ParentController<? extends ViewGroup> parentController;
    private boolean isShown;
    private boolean isDestroyed;
    private ViewVisibilityListener viewVisibilityListener = new ViewVisibilityListenerAdapter();
    private ViewControllerOverlay overlay;

    @Nullable
    public abstract String getCurrentComponentName();

    public void setOverlay(ViewControllerOverlay overlay) {
        this.overlay = overlay;
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public ViewController(Activity activity, String id, YellowBoxDelegate yellowBoxDelegate, Options initialOptions, ViewControllerOverlay overlay) {
        this.activity = activity;
        this.id = id;
        this.yellowBoxDelegate = yellowBoxDelegate;
        this.initialOptions = initialOptions;
        this.overlay = overlay;
        options = initialOptions.copy();
    }

    public void setWaitForRender(Bool waitForRender) {
        this.waitForRender = waitForRender;
    }

    public ScrollEventListener getScrollEventListener() {
        return null;
    }

    public void addOnAppearedListener(Runnable onAppearedListener) {
        if (isShown) {
            onAppearedListener.run();
        } else {
            onAppearedListeners.add(onAppearedListener);
        }
    }

    public void removeOnAppearedListener(Runnable onAppearedListener) {
        onAppearedListeners.remove(onAppearedListener);
    }

    public abstract T createView();

    public void setViewVisibilityListener(ViewVisibilityListener viewVisibilityListener) {
        this.viewVisibilityListener = viewVisibilityListener;
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
    public void ensureViewIsCreated() {
        getView();
    }

    protected boolean isViewCreated() {
        return view != null;
    }

    public boolean handleBack(CommandListener listener) {
        return false;
    }

    public void addOverlay(View v, ViewGroup.LayoutParams layoutParams) {
        perform(view, view -> overlay.add(view, v, layoutParams));
    }

    public void removeOverlay(View view) {
        overlay.remove(view);
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

    public ViewController<?> getTopMostParent(){
        if(parentController!=null){
            return parentController.getTopMostParent();
        }else{
            return this;
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

    public void performOnView(Func1<View> task) {
        if (view != null) task.run(view);
    }

    public void performOnParentController(Func1<ParentController> task) {
        if (parentController != null) task.run(parentController);
    }

    @Nullable
    public ParentController getParentController() {
        return parentController;
    }

    public ParentController requireParentController() {
        return parentController;
    }

    public void setParentController(ParentController parentController) {
        this.parentController = parentController;
    }

    public void performOnParentStack(Func1<StackController> task) {
        if (parentController instanceof StackController) {
            task.run((StackController) parentController);
        } else if (this instanceof StackController) {
            task.run((StackController) this);
        } else performOnParentController(parent -> parent.performOnParentStack(task));
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

    @Nullable
    public ViewController findController(View child) {
        return view == child ? this : null;
    }

    public boolean containsComponent(Component component) {
        return getView().equals(component);
    }

    @CallSuper
    public void onViewWillAppear() {
        isShown = true;
        applyOptions(options);
        performOnParentController(parentController -> {
            parentController.clearOptions();
            if (getView() instanceof Component) parentController.applyChildOptions(options, this);
        });
        if (!onAppearedListeners.isEmpty() && !appearEventPosted) {
            appearEventPosted = true;
            UiThread.post(() -> {
                forEach(onAppearedListeners, Runnable::run);
                onAppearedListeners.clear();
            });
        }
    }

    public void onViewDidAppear() {

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
            setParentController(null);
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
                onViewWillAppear();
            }
        } else if (isShown && !isViewShown()) {
            if (!viewVisibilityListener.onViewDisappear(view)) {
                isShown = false;
                onViewDisappear();
            }
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
    }

    public void onAttachToParent() {

    }

    @Override
    public void onChildViewAdded(View parent, View child) {
        yellowBoxDelegate.onChildViewAdded(parent, child);
    }

    @Override
    public void onChildViewRemoved(View view, View view1) {

    }

    protected void runOnPreDraw(Func1<T> task) {
        if (!isDestroyed) UiUtils.runOnPreDrawOnce(getView(), task);
    }

    public abstract void sendOnNavigationButtonPressed(String buttonId);

    public boolean isViewShown() {
        return !isDestroyed &&
                view != null &&
                view.isShown() &&
                isRendered();
    }

    public boolean isRendered() {
        return view != null && (
                waitForRender.isFalseOrUndefined() ||
                        !(view instanceof Renderable) ||
                        ((Renderable) view).isRendered()
        );
    }

    public void start() {

    }

    public void applyOnController(ViewController controller, Func1<ViewController> task) {
        if (controller != null) task.run(controller);
    }

    @Override
    @CallSuper
    public boolean onMeasureChild(CoordinatorLayout parent, ViewGroup child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        perform(findController(child), ViewController::applyTopInset);
        return false;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, ViewGroup child, View dependency) {
        return false;
    }

    public void applyTopInset() {

    }

    public int getTopInset() {
        return 0;
    }

    public void applyBottomInset() {

    }

    public int getBottomInset() {
        return perform(parentController, 0, p -> p.getBottomInset(this));
    }
}
