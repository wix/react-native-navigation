package com.reactnativenavigation.viewcontrollers.navigator;

import android.app.Activity;
import android.content.res.Configuration;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.WindowInsetsCompat;

import com.facebook.react.ReactInstanceManager;
import com.reactnativenavigation.options.Options;
import com.reactnativenavigation.react.CommandListener;
import com.reactnativenavigation.react.CommandListenerAdapter;
import com.reactnativenavigation.react.events.EventEmitter;
import com.reactnativenavigation.utils.CompatUtils;
import com.reactnativenavigation.utils.Functions.Func1;
import com.reactnativenavigation.viewcontrollers.child.ChildControllersRegistry;
import com.reactnativenavigation.viewcontrollers.modal.ModalStack;
import com.reactnativenavigation.viewcontrollers.overlay.OverlayManager;
import com.reactnativenavigation.viewcontrollers.parent.ParentController;
import com.reactnativenavigation.viewcontrollers.stack.StackController;
import com.reactnativenavigation.viewcontrollers.viewcontroller.Presenter;
import com.reactnativenavigation.viewcontrollers.viewcontroller.RootPresenter;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController;
import com.reactnativenavigation.viewcontrollers.viewcontroller.overlay.RootOverlay;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Navigator extends ParentController<ViewGroup> {

    private final ModalStack modalStack;
    private final OverlayManager overlayManager;
    private final RootPresenter rootPresenter;
    private ViewController<?> root;
    private ViewController<?> previousRoot;
    private final CoordinatorLayout rootLayout;
    private final CoordinatorLayout modalsLayout;
    private final CoordinatorLayout overlaysLayout;
    private ViewGroup contentLayout;
    private Options defaultOptions = new Options();

    @Override
    public void setDefaultOptions(Options defaultOptions) {
        super.setDefaultOptions(defaultOptions);
        this.defaultOptions = defaultOptions;
        modalStack.setDefaultOptions(defaultOptions);
    }

    public Options getDefaultOptions() {
        return defaultOptions;
    }

    CoordinatorLayout getRootLayout() {
        return rootLayout;
    }

    public void setEventEmitter(EventEmitter eventEmitter) {
        modalStack.setEventEmitter(eventEmitter);
    }

    public void setContentLayout(ViewGroup contentLayout) {
        this.contentLayout = contentLayout;
        contentLayout.addView(rootLayout);
        modalsLayout.setVisibility(View.GONE);
        contentLayout.addView(modalsLayout);
        overlaysLayout.setVisibility(View.GONE);
        contentLayout.addView(overlaysLayout);
    }

    public Navigator(final Activity activity, ChildControllersRegistry childRegistry, ModalStack modalStack, OverlayManager overlayManager, RootPresenter rootPresenter) {
        super(activity, childRegistry, "navigator" + CompatUtils.generateViewId(), new Presenter(activity, new Options()), new Options());
        this.modalStack = modalStack;
        this.overlayManager = overlayManager;
        this.rootPresenter = rootPresenter;
        rootLayout = new CoordinatorLayout(getActivity());
        modalsLayout = new CoordinatorLayout(getActivity());
        overlaysLayout = new CoordinatorLayout(getActivity());
    }



    public void bindViews() {
        modalStack.setModalsLayout(modalsLayout);
        modalStack.setRootLayout(rootLayout);
        rootPresenter.setRootContainer(rootLayout);
    }

    @NonNull
    @Override
    public ViewGroup createView() {
        return rootLayout;
    }

    @NonNull
    @Override
    public Collection<ViewController<?>> getChildControllers() {
        return root == null ? Collections.emptyList() : Collections.singletonList(root);
    }

    @Override
    public boolean handleBack(CommandListener listener) {
        if (modalStack.isEmpty() && root == null) return false;
        if (modalStack.isEmpty()) return root.handleBack(listener);
        return modalStack.handleBack(listener, root);
    }

    @Override
    public ViewController<?> getCurrentChild() {
        return root;
    }

    @Override
    public void destroy() {
        destroyViews();
        super.destroy();
    }

    public void destroyViews() {
        modalStack.destroy();
        overlayManager.destroy(overlaysLayout);
        destroyRoot();
    }

    private void destroyRoot() {
        if (root != null) root.destroy();
        root = null;
    }

    private void destroyPreviousRoot() {
        if (previousRoot != null) previousRoot.destroy();
        previousRoot = null;
    }

    @Override
    public void sendOnNavigationButtonPressed(String buttonId) {

    }

    public void setRoot(final ViewController<?> appearing, CommandListener commandListener, ReactInstanceManager reactInstanceManager) {
        previousRoot = root;
        modalStack.destroy();
        final boolean removeSplashView = isRootNotCreated();
        if (isRootNotCreated()) getView();
        final ViewController<?> disappearing = previousRoot;
        root = appearing;
        root.setOverlay(new RootOverlay(getActivity(), contentLayout));
        root.setParentController(this);
        rootPresenter.setRoot(appearing, disappearing, defaultOptions, new CommandListenerAdapter(commandListener) {
            @Override
            public void onSuccess(String childId) {
                root.onViewDidAppear();
                if (removeSplashView) contentLayout.removeViewAt(0);
                destroyPreviousRoot();
                super.onSuccess(childId);
            }
        }, reactInstanceManager);
    }

    public void mergeOptions(final String componentId, Options options) {
        ViewController<?> target = findController(componentId);
        if (target != null) {
            target.mergeOptions(options);
        }
    }

    public void push(final String id, final ViewController<?> viewController, CommandListener listener) {
        applyOnStack(id, listener, stack -> stack.push(viewController, listener));
    }

    public void setStackRoot(String id, List<ViewController<?>> children, CommandListener listener) {
        applyOnStack(id, listener, stack -> stack.setRoot(children, listener));
    }

    public void pop(String id, Options mergeOptions, CommandListener listener) {
        applyOnStack(id, listener, stack -> stack.pop(mergeOptions, listener));
    }

    public void popToRoot(final String id, Options mergeOptions, CommandListener listener) {
        applyOnStack(id, listener, stack -> stack.popToRoot(mergeOptions, listener));
    }

    public void popTo(final String id, Options mergeOptions, CommandListener listener) {
        ViewController<?> target = findController(id);
        if (target != null) {
            target.performOnParentStack(stack -> stack.popTo(target, mergeOptions, listener));
        } else {
            listener.onError("Failed to execute stack command. Stack by " + id + " not found.");
        }
    }

    public void showModal(final ViewController<?> viewController, CommandListener listener) {
        modalStack.showModal(viewController, root, listener);
    }

    public void dismissModal(final String componentId, CommandListener listener) {
        if (isRootNotCreated() && modalStack.size() == 1) {
            listener.onError("Can not dismiss modal if root is not set and only one modal is displayed.");
            return;
        }
        modalStack.dismissModal(componentId, root, listener);
    }

    public void dismissAllModals(Options mergeOptions, CommandListener listener) {
        modalStack.dismissAllModals(root, mergeOptions, listener);
    }

    public void showOverlay(ViewController<?> overlay, CommandListener listener) {
        overlayManager.show(overlaysLayout, overlay, listener);
    }

    public void dismissOverlay(final String componentId, CommandListener listener) {
        overlayManager.dismiss(overlaysLayout, componentId, listener);
    }

    public void dismissAllOverlays(CommandListener listener) {
        overlayManager.dismissAll(overlaysLayout, listener);
    }

    @Nullable
    @Override
    public ViewController<?> findController(String id) {
        ViewController<?> controllerById = super.findController(id);
        if (controllerById == null) {
            controllerById = modalStack.findControllerById(id);
        }
        if (controllerById == null) {
            controllerById = overlayManager.findControllerById(id);
        }
        return controllerById;
    }

    private void applyOnStack(String fromId, CommandListener listener, Func1<StackController> task) {
        ViewController<?> from = findController(fromId);
        if (from != null) {
            if (from instanceof StackController) {
                task.run((StackController) from);
            } else {
                from.performOnParentStack(task);
            }
        } else {
            listener.onError("Failed to execute stack command. Stack " + fromId + " not found.");
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        modalStack.onConfigurationChanged(newConfig);
        overlayManager.onConfigurationChanged(newConfig);
        super.onConfigurationChanged(newConfig);
    }

    private boolean isRootNotCreated() {
        return view == null;
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    CoordinatorLayout getModalsLayout() {
        return modalsLayout;
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    CoordinatorLayout getOverlaysLayout() {
        return overlaysLayout;
    }

    public void onHostPause() {
        overlayManager.onHostPause();
        if (!modalStack.isEmpty()) {
            modalStack.onHostPause();
            if(modalStack.peekDisplayedOverCurrentContext()){
                onViewDisappear();
            }
        } else {
            onViewDisappear();
        }
    }

    public void onHostResume() {
        overlayManager.onHostResume();
        if (!modalStack.isEmpty()) {
            modalStack.onHostResume();
            if(modalStack.peekDisplayedOverCurrentContext()){
                onViewDidAppear();
            }
        } else {
            onViewDidAppear();
        }
    }
}
