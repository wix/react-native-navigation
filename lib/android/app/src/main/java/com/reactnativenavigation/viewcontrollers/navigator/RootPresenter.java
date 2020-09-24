package com.reactnativenavigation.viewcontrollers.navigator;

import android.content.Context;

import com.facebook.react.ReactInstanceManager;
import com.reactnativenavigation.options.Options;
import com.reactnativenavigation.react.CommandListener;
import com.reactnativenavigation.viewcontrollers.viewcontroller.LayoutDirectionApplier;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController;
import com.reactnativenavigation.views.BehaviourDelegate;

import androidx.annotation.VisibleForTesting;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import static com.reactnativenavigation.utils.CoordinatorLayoutUtils.matchParentWithBehaviour;

public class RootPresenter {
    private RootAnimator animator;
    private CoordinatorLayout rootLayout;
    private LayoutDirectionApplier layoutDirectionApplier;

    public void setRootContainer(CoordinatorLayout rootLayout) {
        this.rootLayout = rootLayout;
    }

    public RootPresenter(Context context) {
        this(new RootAnimator(context), new LayoutDirectionApplier());
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
        if (options.animations.setRoot.enabled.isTrueOrUndefined()) {
            animator.setRoot(root, options, () -> listener.onSuccess(root.getId()));
        } else if (options.animations.setRoot.waitForRender.isTrue()) {
            root.getView().setAlpha(0);
            root.addOnAppearedListener(() -> {
                if (root.isDestroyed()) {
                    listener.onError("Could not set root - Waited for the view to become visible but it was destroyed");
                } else {
                    root.getView().setAlpha(1);
                    listener.onSuccess(root.getId());
                }
            });
        } else {
            listener.onSuccess(root.getId());
        }
    }
}
