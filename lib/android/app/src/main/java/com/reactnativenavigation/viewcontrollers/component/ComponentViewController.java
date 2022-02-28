package com.reactnativenavigation.viewcontrollers.component;

import android.app.Activity;
import android.content.res.Configuration;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.reactnativenavigation.options.OverlayAttachOptions;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ScrollEventListener;
import com.reactnativenavigation.options.Options;
import com.reactnativenavigation.viewcontrollers.viewcontroller.Presenter;
import com.reactnativenavigation.utils.SystemUiUtils;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ReactViewCreator;
import com.reactnativenavigation.viewcontrollers.child.ChildController;
import com.reactnativenavigation.viewcontrollers.child.ChildControllersRegistry;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController;
import com.reactnativenavigation.views.overlay.ViewTooltip;
import com.reactnativenavigation.views.component.ComponentLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewKt;
import androidx.core.view.WindowInsetsCompat;

import static com.reactnativenavigation.utils.ObjectUtils.perform;

import kotlin.Unit;

public class ComponentViewController extends ChildController<ComponentLayout> {
    private final String componentName;
    private final ComponentPresenter presenter;
    private final ReactViewCreator viewCreator;
    private boolean ignoreInsets = false;

    public void ignoreInsets(boolean ignore) {
        ignoreInsets = ignore;
    }

    private enum VisibilityState {Appear, Disappear}

    private VisibilityState lastVisibilityState = VisibilityState.Disappear;

    public ComponentViewController(final Activity activity,
                                   final ChildControllersRegistry childRegistry,
                                   final String id,
                                   final String componentName,
                                   final ReactViewCreator viewCreator,
                                   final Options initialOptions,
                                   final Presenter presenter,
                                   final ComponentPresenter componentPresenter) {
        super(activity, childRegistry, id, presenter, initialOptions);
        this.componentName = componentName;
        this.viewCreator = viewCreator;
        this.presenter = componentPresenter;
    }

    @Override
    public void start() {
        if (!isDestroyed()) getView().start();
    }

    @Override
    public String getCurrentComponentName() {
        return this.componentName;
    }

    @Override
    public void setDefaultOptions(Options defaultOptions) {
        super.setDefaultOptions(defaultOptions);
        presenter.setDefaultOptions(defaultOptions);
    }

    @Override
    public ScrollEventListener getScrollEventListener() {
        return perform(view, null, ComponentLayout::getScrollEventListener);
    }

    @Override
    public void onViewWillAppear() {
        super.onViewWillAppear();
        if (view != null)
            view.sendComponentWillStart();
    }

    @Override
    public void onViewDidAppear() {
        if (view != null)
            view.sendComponentWillStart();
        super.onViewDidAppear();
        if (view != null) {
            view.requestApplyInsets();
            if (lastVisibilityState == VisibilityState.Disappear)
                view.sendComponentStart();
        }
        lastVisibilityState = VisibilityState.Appear;
    }

    @Override
    public void onViewDisappear() {
        if (lastVisibilityState == VisibilityState.Disappear) return;
        lastVisibilityState = VisibilityState.Disappear;
        if (view != null) view.sendComponentStop();
        super.onViewDisappear();
    }

    @Override
    public void sendOnNavigationButtonPressed(String buttonId) {
        getView().sendOnNavigationButtonPressed(buttonId);
    }

    @Override
    public void applyOptions(Options options) {
        if (isRoot()) applyTopInset();
        super.applyOptions(options);
        getView().applyOptions(options);
        presenter.applyOptions(getView(), resolveCurrentOptions(presenter.defaultOptions));
    }

    @Override
    public boolean isViewShown() {
        return super.isViewShown() && view != null && view.isReady();
    }

    @NonNull
    @Override
    public ComponentLayout createView() {
        ComponentLayout view = (ComponentLayout) viewCreator.create(getActivity(), getId(), componentName);
        return (ComponentLayout) view.asView();
    }

    @Override
    public void mergeOptions(Options options) {
        if (options == Options.EMPTY) return;
        if (isViewShown()) presenter.mergeOptions(getView(), options);
        super.mergeOptions(options);
    }

    @Override
    public void applyTopInset() {
        if (view != null && !ignoreInsets) presenter.applyTopInsets(view, getTopInset());
    }

    @Override
    public int getTopInset() {
        int statusBarInset = resolveCurrentOptions(presenter.defaultOptions).statusBar.isHiddenOrDrawBehind() ? 0 : SystemUiUtils.getStatusBarHeight(getActivity());
        final Integer perform = perform(getParentController(), 0, p -> p.getTopInset(this));
        return statusBarInset + perform;
    }

    @Override
    public void applyBottomInset() {
        if (view != null && !ignoreInsets) presenter.applyBottomInset(view, getBottomInset());
    }

    @Override
    protected WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat insets) {
        ViewController<?> viewController = findController(view);
        if (viewController == null || viewController.getView() == null || ignoreInsets) return insets;
        final Options currentOptions = resolveCurrentOptions(presenter.defaultOptions);
        int mode = getActivity().getWindow().getAttributes().softInputMode;
        boolean adjustNothing = (mode & WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING) ==WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING ;
        final int keyboardBottomInset = currentOptions.layout.adjustResize.get(true) &&!adjustNothing ?
                insets.getInsets(WindowInsetsCompat.Type.ime()).bottom : 0;
        final Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
        final int visibleNavBar = currentOptions.navigationBar.isVisible.isTrueOrUndefined() ? 1 : 0;
        final int controllerBottomInset = currentOptions.bottomTabsOptions.isHiddenOrDrawBehind() ? 0 : getBottomInset();
        final WindowInsetsCompat finalInsets = new WindowInsetsCompat.Builder().setInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.ime(),
                Insets.of(systemBarsInsets.left,
                        0,
                        systemBarsInsets.right,
                        Math.max(0, Math.max(visibleNavBar * systemBarsInsets.bottom, keyboardBottomInset) - controllerBottomInset))
        ).build();
        ViewKt.doOnLayout(viewController.getView(), (v) -> {
            ViewCompat.onApplyWindowInsets(viewController.getView(), finalInsets);
            return Unit.INSTANCE;
        });
        return insets;
    }

    @Override
    public void destroy() {
        final boolean blurOnUnmount = options != null && options.modal.blurOnUnmount.isTrue();
        if (blurOnUnmount) {
            blurActivityFocus();
        }
        super.destroy();
    }

    private void blurActivityFocus() {
        final Activity activity = getActivity();
        final View focusView = activity != null ? activity.getCurrentFocus() : null;
        if (focusView != null) {
            focusView.clearFocus();
        }
    }

    @Override
    public ViewTooltip.TooltipView showAnchoredOverlay(@NonNull View anchorView, @NonNull OverlayAttachOptions overlayAttachOptions, @NonNull ViewController<?> overlayViewController) {
        if (view != null) {
            return view.getAttachedOverlayContainer().addAnchoredView(anchorView, overlayViewController.getView(),
                    overlayAttachOptions.gravity.get());
        }
        return null;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        presenter.onConfigurationChanged(view, options);
    }
}
