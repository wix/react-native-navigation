package com.reactnativenavigation.viewcontrollers.viewcontroller;

import com.facebook.react.ReactInstanceManager;
import com.reactnativenavigation.hierarchy.root.RootAnimator;
import com.reactnativenavigation.options.AnimationOptions;
import com.reactnativenavigation.options.Options;
import com.reactnativenavigation.react.CommandListener;
import com.reactnativenavigation.views.BehaviourDelegate;

import androidx.annotation.VisibleForTesting;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;

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
        AnimationOptions enter = options.animations.setRoot.getEnter();
        appearingRoot.setWaitForRender(enter.waitForRender);
        if (enter.waitForRender.isTrue()) {
            appearingRoot.getView().setAlpha(0);
            appearingRoot.addOnAppearedListener(() -> {
                if (appearingRoot.isDestroyed()) {
                    listener.onError("Could not set root - Waited for the view to become visible but it was destroyed");
                } else {
                    appearingRoot.getView().setAlpha(1);
                    animateSetRootAndReportSuccess(appearingRoot, disappearingRoot, listener, options);
                }
            });
        } else {
            animateSetRootAndReportSuccess(appearingRoot, disappearingRoot, listener, options);
        }
    }

    private void animateSetRootAndReportSuccess(ViewController root,
                                                ViewController disappearingRoot,
                                                CommandListener listener,
                                                Options options)
    {
        AnimationOptions exit = options.animations.setRoot.getExit();
        AnimationOptions enter = options.animations.setRoot.getEnter();
        if ((enter.enabled.isTrueOrUndefined() && enter.hasAnimation())
                || (disappearingRoot != null && exit.enabled.isTrueOrUndefined()&& exit.hasAnimation())) {
            animator.setRoot(root,
                    disappearingRoot,
                    options.animations.setRoot,
                    () -> {
                        listener.onSuccess(root.getId());
                        return Unit.INSTANCE;
                    });
        } else {
            listener.onSuccess(root.getId());
        }
    }
}
