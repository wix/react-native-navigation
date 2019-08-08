package com.reactnativenavigation.presentation;

import android.content.Context;
import android.widget.FrameLayout;

import com.facebook.react.ReactInstanceManager;
import com.reactnativenavigation.anim.NavigationAnimator;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.utils.CommandListener;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.views.element.ElementTransitionManager;

public class RootPresenter {
    private NavigationAnimator animator;
    private LayoutDirectionApplier layoutDirectionApplier;
    private FrameLayout rootLayout;

    public void setRootContainer(FrameLayout rootLayout) {
        this.rootLayout = rootLayout;
    }

    public RootPresenter(Context context) {
        this(new NavigationAnimator(context, new ElementTransitionManager()), new LayoutDirectionApplier());
    }

    public RootPresenter(NavigationAnimator animator, LayoutDirectionApplier layoutDirectionApplier) {
        this.animator = animator;
        this.layoutDirectionApplier = layoutDirectionApplier;
    }

    public void setRoot(ViewController root, Options defaultOptions, CommandListener listener, ReactInstanceManager reactInstanceManager) {
        layoutDirectionApplier.apply(root, defaultOptions, reactInstanceManager);
        rootLayout.addView(root.getView());
        Options options = root.resolveCurrentOptions(defaultOptions);
        root.setWaitForRender(options.animations.setRoot.waitForRender);
        if (options.animations.setRoot.waitForRender.isTrue()) {
            root.getView().setAlpha(0);
            root.addOnAppearedListener(() -> {
                root.getView().setAlpha(1);
                animateSetRootAndReportSuccess(root, listener, options);
            });
        } else {
            animateSetRootAndReportSuccess(root, listener, options);
        }
    }

    private void animateSetRootAndReportSuccess(ViewController root, CommandListener listener, Options options) {
        if (options.animations.setRoot.hasAnimation()) {
            animator.setRoot(root.getView(), options.animations.setRoot, () -> listener.onSuccess(root.getId()));
        } else {
            listener.onSuccess(root.getId());
        }
    }
}
