package com.reactnativenavigation.viewcontrollers.navigator;

import android.app.Activity;

import com.facebook.react.ReactInstanceManager;
import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.TestActivity;
import com.reactnativenavigation.hierarchy.root.RootAnimator;
import com.reactnativenavigation.mocks.SimpleViewController;
import com.reactnativenavigation.options.AnimationOptions;
import com.reactnativenavigation.options.ElementTransitions;
import com.reactnativenavigation.options.SharedElements;
import com.reactnativenavigation.options.TransitionAnimationOptions;
import com.reactnativenavigation.options.Options;
import com.reactnativenavigation.options.params.Bool;
import com.reactnativenavigation.react.CommandListenerAdapter;
import com.reactnativenavigation.viewcontrollers.child.ChildControllersRegistry;
import com.reactnativenavigation.viewcontrollers.viewcontroller.LayoutDirectionApplier;
import com.reactnativenavigation.viewcontrollers.viewcontroller.RootPresenter;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController;
import com.reactnativenavigation.views.BehaviourDelegate;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.robolectric.android.controller.ActivityController;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class RootPresenterTest extends BaseTest {
    private RootPresenter uut;
    private CoordinatorLayout rootContainer;
    private ViewController root;
    private RootAnimator animator;
    private LayoutDirectionApplier layoutDirectionApplier;
    private Options defaultOptions;
    private ReactInstanceManager reactInstanceManager;
    private Activity activity;
    private ActivityController<TestActivity> activityController;

    @Override
    public void beforeEach() {
        activityController = newActivityController(TestActivity.class);
        activity = activityController.create().get();
        reactInstanceManager = Mockito.mock(ReactInstanceManager.class);
        rootContainer = new CoordinatorLayout(activity);
        root = new SimpleViewController(activity, new ChildControllersRegistry(), "child1", new Options());
        animator = spy(createAnimator());
        layoutDirectionApplier = Mockito.mock(LayoutDirectionApplier.class);
        uut = new RootPresenter(animator, layoutDirectionApplier);
        uut.setRootContainer(rootContainer);
        defaultOptions = new Options();
    }

    @Test
    public void setRoot_viewIsAddedToContainer() {
        uut.setRoot(root, null, defaultOptions, new CommandListenerAdapter(), reactInstanceManager);
        assertThat(root.getView().getParent()).isEqualTo(rootContainer);
        assertThat(((CoordinatorLayout.LayoutParams) root.getView().getLayoutParams()).getBehavior()).isInstanceOf(BehaviourDelegate.class);
    }

    @Test
    public void setRoot_reportsOnSuccess() {
        CommandListenerAdapter listener = spy(new CommandListenerAdapter());
        uut.setRoot(root, null, defaultOptions, listener, reactInstanceManager);
        verify(listener).onSuccess(root.getId());
    }

    @Test
    public void setRoot_doesNotAnimateByDefault() {
        CommandListenerAdapter listener = spy(new CommandListenerAdapter());
        uut.setRoot(root, null, defaultOptions, listener, reactInstanceManager);
        verifyZeroInteractions(animator);
        verify(listener).onSuccess(root.getId());
    }

    @Test
    public void setRoot_playEnterAnimOnlyWhenNoDisappearingView() {
        Options animatedSetRoot = new Options();
        AnimationOptions enter = spy(new AnimationOptions());
        AnimationOptions exit = spy(new AnimationOptions());
        animatedSetRoot.animations.setRoot = createEnterExitTransitionAnim(enter, exit);
        ViewController spy = spy(root);
        when(spy.resolveCurrentOptions(defaultOptions)).thenReturn(animatedSetRoot);
        CommandListenerAdapter listener = spy(new CommandListenerAdapter());

        uut.setRoot(spy, null, defaultOptions, listener, reactInstanceManager);
        verify(animator).setRoot(eq(spy), eq(animatedSetRoot.animations.setRoot), any());
        verify(listener).onSuccess(spy.getId());
    }

    @Test
    public void setRoot_animates() {
        Options animatedSetRoot = new Options();
        animatedSetRoot.animations.setRoot = createEnterTransitionAnim(new AnimationOptions() {
            @Override
            public boolean hasAnimation() {
                return true;
            }
        });

        ViewController spy = spy(root);
        when(spy.resolveCurrentOptions(defaultOptions)).thenReturn(animatedSetRoot);
        CommandListenerAdapter listener = spy(new CommandListenerAdapter());

        uut.setRoot(spy, null, defaultOptions, listener, reactInstanceManager);
        verify(animator).setRoot(eq(spy), eq(animatedSetRoot.animations.setRoot), any());
        verify(listener).onSuccess(spy.getId());
    }

    @Test
    public void setRoot_waitForRenderIsSet() {
        root.options.animations.setRoot.getEnter().waitForRender = new Bool(true);
        ViewController spy = spy(root);

        uut.setRoot(spy, null, defaultOptions, new CommandListenerAdapter(), reactInstanceManager);

        ArgumentCaptor<Bool> captor = ArgumentCaptor.forClass(Bool.class);
        verify(spy).setWaitForRender(captor.capture());
        assertThat(captor.getValue().get()).isTrue();
    }

    @Test
    public void setRoot_waitForRender() {
        root.options.animations.setRoot.getEnter().waitForRender = new Bool(true);

        ViewController spy = spy(root);
        CommandListenerAdapter listener = spy(new CommandListenerAdapter());
        uut.setRoot(spy, null, defaultOptions, listener, reactInstanceManager);
        verify(spy).addOnAppearedListener(any());
        assertThat(spy.getView().getAlpha()).isZero();
        verifyZeroInteractions(listener);

        spy.onViewWillAppear();
        idleMainLooper();
        assertThat(spy.getView().getAlpha()).isOne();
        verify(listener).onSuccess(spy.getId());
    }

    @Test
    public void setRoot_appliesLayoutDirection() {
        CommandListenerAdapter listener = spy(new CommandListenerAdapter());
        uut.setRoot(root, null, defaultOptions, listener, reactInstanceManager);
        verify(layoutDirectionApplier).apply(root, defaultOptions, reactInstanceManager);
    }

    @NonNull
    private RootAnimator createAnimator() {
        return new RootAnimator() {
            @Override
            public void setRoot(@NotNull ViewController<?> root, @NotNull TransitionAnimationOptions setRoot, @NotNull Runnable onAnimationEnd) {
                onAnimationEnd.run();
            }
        };
    }


    private TransitionAnimationOptions createEnterExitTransitionAnim(AnimationOptions enter, AnimationOptions exit) {
        return new TransitionAnimationOptions(enter, exit, new SharedElements(), new ElementTransitions());
    }

    private TransitionAnimationOptions createEnterTransitionAnim(AnimationOptions enter) {
        return createEnterExitTransitionAnim(enter, new AnimationOptions());
    }

    private TransitionAnimationOptions createExitTransitionAnim(AnimationOptions exit) {
        return createEnterExitTransitionAnim(new AnimationOptions(), exit);
    }
}
