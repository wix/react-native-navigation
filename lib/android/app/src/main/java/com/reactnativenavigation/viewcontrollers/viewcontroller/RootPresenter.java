package com.reactnativenavigation.viewcontrollers.viewcontroller;

import com.facebook.react.ReactInstanceManager;
import com.reactnativenavigation.hierarchy.root.RootAnimator;
import com.reactnativenavigation.options.Options;
import com.reactnativenavigation.react.CommandListener;
import com.reactnativenavigation.views.BehaviourDelegate;

import androidx.annotation.VisibleForTesting;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import static com.reactnativenavigation.utils.CoordinatorLayoutUtils.matchParentWithBehaviour;

public class RootPresenter {
    private final RootAnimator animator;
    private CoordinatorLayout rootLayout;
    private final LayoutDirectionApplier layoutDirectionApplier;

    public void setRootContainer(CoordinatorLayout rootLayout) {
        this.rootLayout = rootLayout;
    }

    public RootPresenter() {
        this(new RootAnimator(), new LayoutDirectionApplier());
    }

    @VisibleForTesting
    public RootPresenter(RootAnimator animator, LayoutDirectionApplier layoutDirectionApplier) {
        this.animator = animator;
        this.layoutDirectionApplier = layoutDirectionApplier;
    }

    public void setRoot(ViewController root, Options defaultOptions, CommandListener listener, ReactInstanceManager reactInstanceManager) {
        layoutDirectionApplier.apply(root, defaultOptions, reactInstanceManager);
        rootLayout.addView(root.getView(), matchParentWithBehaviour(new BehaviourDelegate(root)));
        Options options = root.resolveCurrentOptions(defaultOptions);
        root.setWaitForRender(options.animations.setRoot.waitForRender);
        if (options.animations.setRoot.waitForRender.isTrue()) {
            root.getView().setAlpha(0);
            root.addOnAppearedListener(() -> {
                if (root.isDestroyed()) {
                    listener.onError("Could not set root - Waited for the view to become visible but it was destroyed");
                } else {
                    root.getView().setAlpha(1);
                    animateSetRootAndReportSuccess(root, listener, options);
                }
            });
        } else {
            animateSetRootAndReportSuccess(root, listener, options);
        }
    }

    private void animateSetRootAndReportSuccess(ViewController root, CommandListener listener, Options options) {
        if (options.animations.setRoot.hasAnimation()) {
            animator.setRoot(root, options.animations.setRoot, () -> listener.onSuccess(root.getId()));
        } else {
            listener.onSuccess(root.getId());
        }
    }
}
