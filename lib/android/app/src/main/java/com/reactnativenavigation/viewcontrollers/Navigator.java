package com.reactnativenavigation.viewcontrollers;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.reactnativenavigation.anim.NavigationAnimator;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.presentation.OptionsPresenter;
import com.reactnativenavigation.presentation.OverlayManager;
import com.reactnativenavigation.react.EventEmitter;
import com.reactnativenavigation.utils.CommandListener;
import com.reactnativenavigation.utils.CompatUtils;
import com.reactnativenavigation.viewcontrollers.modal.ModalStack;
import com.reactnativenavigation.viewcontrollers.stack.StackController;
import com.reactnativenavigation.views.element.ElementTransitionManager;

import java.util.Collection;
import java.util.Collections;

public class Navigator extends ParentController {

    private final ModalStack modalStack;
    private final OverlayManager overlayManager;
    private ViewController root;
    private final FrameLayout rootLayout;
    private final FrameLayout modalsLayout;
    private final FrameLayout overlaysLayout;
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

    public FrameLayout getRootLayout() {
        return rootLayout;
    }

    public void setEventEmitter(EventEmitter eventEmitter) {
        modalStack.setEventEmitter(eventEmitter);
    }

    public void setContentLayout(ViewGroup contentLayout) {
        this.contentLayout = contentLayout;
        contentLayout.addView(rootLayout);
        contentLayout.addView(modalsLayout);
        contentLayout.addView(overlaysLayout);
    }

    public Navigator(final Activity activity, ChildControllersRegistry childRegistry, ModalStack modalStack, OverlayManager overlayManager) {
        super(activity, childRegistry,"navigator" + CompatUtils.generateViewId(), new OptionsPresenter(activity, new Options()), new Options());
        this.modalStack = modalStack;
        this.overlayManager = overlayManager;
        rootLayout = new FrameLayout(getActivity());
        modalsLayout = new FrameLayout(getActivity());
        overlaysLayout = new FrameLayout(getActivity());
        modalStack.setModalsContainer(modalsLayout);
    }

    @NonNull
    @Override
    protected ViewGroup createView() {
        return rootLayout;
    }

    @NonNull
    @Override
    public Collection<ViewController> getChildControllers() {
        return root == null ? Collections.emptyList() : Collections.singletonList(root);
    }

    @Override
    public boolean handleBack(CommandListener listener) {
        if (modalStack.isEmpty() && root == null) return false;
        if (modalStack.isEmpty()) return root.handleBack(listener);
        return modalStack.handleBack(listener, root);
    }

    @Override
    protected ViewController getCurrentChild() {
        return root;
    }

    @Override
    public void destroy() {
        destroyViews();
        super.destroy();
    }

    public void destroyViews() {
        modalStack.destroy();
        overlayManager.destroy();
        destroyRoot();
    }

    private void destroyRoot() {
        if (root != null) root.destroy();
        root = null;
    }

    @Override
    public void sendOnNavigationButtonPressed(String buttonId) {

    }

    public void setRoot(final ViewController viewController, CommandListener commandListener) {
        destroyRoot();
        if (isRootNotCreated()) {
            removePreviousContentView();
            getView();
        }
        root = viewController;
        rootLayout.addView(viewController.getView());
        if (viewController.options.animations.startApp.hasAnimation()) {
            new NavigationAnimator(viewController.getActivity(), new ElementTransitionManager())
                    .animateStartApp(viewController.getView(), viewController.options.animations.startApp, new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            commandListener.onSuccess(viewController.getId());
                        }
                    });
        } else {
            commandListener.onSuccess(viewController.getId());
        }
    }

    private void removePreviousContentView() {
        contentLayout.removeViewAt(0);
    }

    public void mergeOptions(final String componentId, Options options) {
        ViewController target = findControllerById(componentId);
        if (target != null) {
            target.mergeOptions(options);
        }
    }

    public void push(final String fromId, final ViewController viewController, CommandListener listener) {
        ViewController from = findControllerById(fromId);
        if (from != null) {
            from.performOnParentStack(
                    stack -> ((StackController) stack).push(viewController, listener),
                    () -> rejectPush(fromId, viewController, listener)
            );
        } else {
            rejectPush(fromId, viewController, listener);
        }
    }

    public void setStackRoot(String fromId, ViewController viewController, CommandListener listener) {
        ViewController from = findControllerById(fromId);
        if (from != null) {
            from.performOnParentStack(stack -> ((StackController) stack).setRoot(viewController, listener));
        }
    }

    public void pop(final String fromId, CommandListener listener) {
        ViewController from = findControllerById(fromId);
        if (from != null) {
            from.performOnParentStack(stack -> ((StackController) stack).pop(listener));
        }
    }

    public void popSpecific(final String id, CommandListener listener) {
        ViewController from = findControllerById(id);
        if (from != null) {
            from.performOnParentStack(stack -> ((StackController) stack).popSpecific(from, listener), () -> listener.onError("Nothing to pop"));
        } else {
            listener.onError("Nothing to pop");
        }
    }

    public void popToRoot(final String id, CommandListener listener) {
        ViewController from = findControllerById(id);
        if (from != null) {
            from.performOnParentStack(stack -> ((StackController) stack).popToRoot(listener));
        }
    }

    public void popTo(final String componentId, CommandListener listener) {
        ViewController target = findControllerById(componentId);
        if (target != null) {
            target.performOnParentStack(stack -> ((StackController) stack).popTo(target, listener), () -> listener.onError("Nothing to pop"));
        } else {
            listener.onError("Nothing to pop");
        }
    }

    public void showModal(final ViewController viewController, CommandListener listener) {
        modalStack.showModal(viewController, root, listener);
    }

    public void dismissModal(final String componentId, CommandListener listener) {
        if (isRootNotCreated() && modalStack.size() == 1) {
            listener.onError("Can not dismiss modal if root is not set and only one modal is displayed.");
            return;
        }
        modalStack.dismissModal(componentId, root, listener);
    }

    public void dismissAllModals(CommandListener listener) {
        modalStack.dismissAllModals(listener, root);
    }

    public void showOverlay(ViewController overlay, CommandListener listener) {
        overlayManager.show(overlaysLayout, overlay, listener);
    }

    public void dismissOverlay(final String componentId, CommandListener listener) {
        overlayManager.dismiss(componentId, listener);
    }

    @Nullable
    @Override
    public ViewController findControllerById(String id) {
        ViewController controllerById = super.findControllerById(id);
        return controllerById != null ? controllerById : modalStack.findControllerById(id);
    }

    private void rejectPush(String fromId, ViewController viewController, CommandListener listener) {
        listener.onError("Could not push component: " +
                         viewController.getId() +
                         ". Stack with id " +
                         fromId +
                         " was not found.");
    }

    private boolean isRootNotCreated() {
        return view == null;
    }
}
