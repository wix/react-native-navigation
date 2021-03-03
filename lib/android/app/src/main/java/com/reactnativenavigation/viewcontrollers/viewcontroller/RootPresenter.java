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

    public void setRoot(ViewController appearingRoot, ViewController<?> disappearingRoot, Options defaultOptions, CommandListener listener, ReactInstanceManager reactInstanceManager) {
        layoutDirectionApplier.apply(appearingRoot, defaultOptions, reactInstanceManager);
        rootLayout.addView(appearingRoot.getView(), matchParentWithBehaviour(new BehaviourDelegate(appearingRoot)));
        Options options = appearingRoot.resolveCurrentOptions(defaultOptions);
        appearingRoot.setWaitForRender(options.animations.setRoot.waitForRender);
        if (options.animations.setRoot.waitForRender.isTrue()) {
            appearingRoot.getView().setAlpha(0);
            appearingRoot.addOnAppearedListener(() -> {
                if (appearingRoot.isDestroyed()) {
                    listener.onError("Could not set root - Waited for the view to become visible but it was destroyed");
                } else {
                    appearingRoot.getView().setAlpha(1);
                    animateSetRootAndReportSuccess(appearingRoot, listener, options);
                }
            });
        } else {
            animateSetRootAndReportSuccess(appearingRoot, listener, options);
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
