package com.reactnativenavigation.viewcontrollers.sheet;

import android.app.Activity;
import android.content.res.Configuration;
import android.view.View;
import android.view.ViewGroup;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.util.ReactFindViewUtil;
import com.facebook.react.views.scroll.ReactScrollView;
import com.reactnativenavigation.options.layout.LayoutOptions;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ScrollEventListener;
import com.reactnativenavigation.options.Options;
import com.reactnativenavigation.viewcontrollers.viewcontroller.Presenter;
import com.reactnativenavigation.utils.SystemUiUtils;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ReactViewCreator;
import com.reactnativenavigation.viewcontrollers.child.ChildController;
import com.reactnativenavigation.viewcontrollers.child.ChildControllersRegistry;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import static com.reactnativenavigation.utils.ObjectUtils.perform;
import com.reactnativenavigation.views.sheet.SheetLayout;

public class SheetViewController extends ChildController<SheetLayout> {
    private final ReactInstanceManager reactInstanceManager;
    private final String componentName;
    private final SheetPresenter presenter;
    private final ReactViewCreator viewCreator;

    private enum VisibilityState {
        Appear, Disappear
    }

    private VisibilityState lastVisibilityState = VisibilityState.Disappear;

    private final View.OnLayoutChangeListener contentViewLayoutChangeListener;
    private ReactScrollView scrollView;
    private ViewGroup headerView;
    private ViewGroup footerView;
    private ViewGroup contentView;

    private int contentHeight = 0;

    public final SheetLayout sheetView;

    private ReactFindViewUtil.OnViewFoundListener createOnViewFoundListener(int headerTag, int contentTag, int footerTag) {
        return new ReactFindViewUtil.OnViewFoundListener() {
            @Override
            public String getNativeId() {
                return "SheetContent-"+contentTag;
            }

            @Override
            public void onViewFound(final View view) {
                if (view instanceof ReactScrollView) {
                    scrollView = (ReactScrollView) view;
                    contentView = (ViewGroup) scrollView.getChildAt(0);
                    if (contentView != null) {
                        getReactContext().runOnUiQueueThread((Runnable) () -> {
                            setupFooterAndHeader(headerTag, footerTag);
                            updateContentHeight();
                        });

                        startListenLayoutChange();
                    } else {
                        // Wait access to first child
                        scrollView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                            @Override
                            public void onLayoutChange (View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                                scrollView.removeOnLayoutChangeListener(this);
                                contentView = (ViewGroup) scrollView.getChildAt(0);
                                getReactContext().runOnUiQueueThread((Runnable) () -> {
                                    setupFooterAndHeader(headerTag, footerTag);
                                    updateContentHeight();
                                });

                                startListenLayoutChange();
                            }
                        });
                    }
                } else if (view instanceof ViewGroup) {
                    contentView = (ViewGroup) view;
                    getReactContext().runOnUiQueueThread((Runnable) () -> {
                        setupFooterAndHeader(headerTag, footerTag);
                        updateContentHeight();
                    });

                    startListenLayoutChange();
                }
            }
        };
    }

    public SheetViewController(final Activity activity,
                                   final ChildControllersRegistry childRegistry,
                                   final String id,
                                   final String componentName,
                                   final ReactViewCreator viewCreator,
                                   final Options initialOptions,
                                   final Presenter presenter,
                                   final SheetPresenter componentPresenter,
                                   final ReactInstanceManager reactInstanceManager) {
        super(activity, childRegistry, id, presenter, initialOptions);
        this.componentName = componentName;
        this.viewCreator = viewCreator;
        this.presenter = componentPresenter;
        this.reactInstanceManager = reactInstanceManager;

        sheetView = (SheetLayout) viewCreator.create(getActivity(), getId(), componentName);

        LayoutOptions layoutOptions = resolveCurrentOptions(componentPresenter.defaultOptions).layout;

        if (layoutOptions.sheetBorderTopRadius.hasValue()) {
            sheetView.setBorderTopRadius(layoutOptions.sheetBorderTopRadius.get());
        }

        if (layoutOptions.sheetBackdropOpacity.hasValue()) {
            sheetView.setBackdropOpacity(layoutOptions.sheetBackdropOpacity.get());
        }

        this.contentViewLayoutChangeListener = new View.OnLayoutChangeListener() {
            public void onLayoutChange(View view, int newLeft, int newRight, int newTop, int newBottom, int oldLeft, int oldRight, int oldTop, int oldBottom) {
                updateContentHeight();
            }
        };

        if (layoutOptions.sheetFullScreen.isTrue()) {
            sheetView.present(0);
        }
    }

    public void setupContentViews(int headerTag, int contentTag, int footerTag) {
        LayoutOptions layoutOptions = resolveCurrentOptions(this.presenter.defaultOptions).layout;
        if (layoutOptions.sheetFullScreen.isTrue()) {
            return;
        }

        ReactFindViewUtil.addViewListener(createOnViewFoundListener(headerTag, contentTag, footerTag));
    }

    private void setupFooterAndHeader(int headerTag, int footerTag) {
        if(sheetView != null) {
            headerView = (ViewGroup) ReactFindViewUtil.findView(sheetView, "SheetHeader-"+headerTag);
            footerView = (ViewGroup) ReactFindViewUtil.findView(sheetView, "SheetFooter-"+footerTag);
        }
    }

    private void startListenLayoutChange() {
        if (contentView != null) {
            contentView.addOnLayoutChangeListener(this.contentViewLayoutChangeListener);
        }
    }

    private void stopListenLayoutChange() {
        if (contentView != null) {
            contentView.removeOnLayoutChangeListener(this.contentViewLayoutChangeListener);
        }
    }

    private void updateContentHeight() {
        int newContentHeight = calcContentHeight();
        if (sheetView != null && contentHeight != newContentHeight) {
            contentHeight = newContentHeight;
            sheetView.present(newContentHeight);
        }
    }

    private int calcContentHeight() {
        return (contentView != null ? contentView.getHeight() : 0) +
            (headerView != null ? headerView.getHeight() : 0) +
            (footerView != null ? footerView.getHeight() : 0);
    }

    @Override
    public void start() {
        if (!isDestroyed())
            getView().start();
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

    private ReactContext getReactContext() {
        return reactInstanceManager.getCurrentReactContext();
    }

    @Override
    public ScrollEventListener getScrollEventListener() {
        return perform(view, null, SheetLayout::getScrollEventListener);
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
        if (view != null)
            view.requestApplyInsets();
        if (view != null && lastVisibilityState == VisibilityState.Disappear)
            view.sendComponentStart();
        lastVisibilityState = VisibilityState.Appear;
    }

    @Override
    public void onViewDisappear() {
        if (lastVisibilityState == VisibilityState.Disappear)
            return;
        lastVisibilityState = VisibilityState.Disappear;
        if (view != null)
            view.sendComponentStop();
        super.onViewDisappear();
    }

    @Override
    public void sendOnNavigationButtonPressed(String buttonId) {
        getView().sendOnNavigationButtonPressed(buttonId);
    }

    @Override
    public void applyOptions(Options options) {
        if (isRoot())
            applyTopInset();
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
    public SheetLayout createView() {
        return (SheetLayout) sheetView.asView();
    }

    @Override
    public void mergeOptions(Options options) {
        if (options == Options.EMPTY)
            return;
        if (isViewShown())
            presenter.mergeOptions(getView(), options);
        super.mergeOptions(options);
    }

    @Override
    public void applyTopInset() {
        if (view != null)
            presenter.applyTopInsets(view, getTopInset());
    }

    @Override
    public int getTopInset() {
        int statusBarInset = resolveCurrentOptions(presenter.defaultOptions).statusBar.isHiddenOrDrawBehind() ? 0
                : SystemUiUtils.getStatusBarHeight(getActivity());
        final Integer perform = perform(getParentController(), 0, p -> p.getTopInset(this));
        return statusBarInset + perform;
    }

    @Override
    public void applyBottomInset() {
        if (view != null)
            presenter.applyBottomInset(view, getBottomInset());
    }

    @Override
    protected WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat insets) {
        final Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
        int systemWindowInsetTop = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top +
                insets.getInsets(WindowInsetsCompat.Type.navigationBars()).top -
                systemBarsInsets.top;
        int systemWindowInsetBottom = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom +
                insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom -
                systemBarsInsets.bottom;

        WindowInsetsCompat finalInsets = new WindowInsetsCompat.Builder()
                .setInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.ime(),
                        Insets.of(systemBarsInsets.left,
                                systemWindowInsetTop,
                                systemBarsInsets.right,
                                Math.max(systemWindowInsetBottom - getBottomInset(), 0)))
                .build();
        ViewCompat.onApplyWindowInsets(view, finalInsets);
        return insets;
    }

    @Override
    public void destroy() {
        final boolean blurOnUnmount = options != null && options.modal.blurOnUnmount.isTrue();
        if (blurOnUnmount) {
            blurActivityFocus();
        }
        stopListenLayoutChange();
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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        presenter.onConfigurationChanged(view, options);
    }
}
