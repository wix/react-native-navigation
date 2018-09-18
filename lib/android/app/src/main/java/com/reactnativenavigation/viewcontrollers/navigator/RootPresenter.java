package com.reactnativenavigation.viewcontrollers.navigator;

import android.content.Context;
import android.widget.FrameLayout;

import com.reactnativenavigation.anim.NavigationAnimator;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.utils.CommandListener;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.views.element.ElementTransitionManager;

public class RootPresenter {
    private NavigationAnimator animator;
    private FrameLayout rootLayout;

    public void setRootContainer(FrameLayout rootLayout) {
        this.rootLayout = rootLayout;
    }

    public RootPresenter(Context context) {
        animator = new NavigationAnimator(context, new ElementTransitionManager());
    }

    RootPresenter(NavigationAnimator animator) {
        this.animator = animator;
    }

    public void setRoot(ViewController root, Options defaultOptions, CommandListener listener) {
        rootLayout.addView(root.getView());
        Options options = root.resolveCurrentOptions(defaultOptions);
        if (options.animations.setRoot.hasAnimation()) {
            animator.setRoot(root.getView(), options.animations.setRoot, () -> listener.onSuccess(root.getId()));
        } else {
            listener.onSuccess(root.getId());
        }
    }
}
