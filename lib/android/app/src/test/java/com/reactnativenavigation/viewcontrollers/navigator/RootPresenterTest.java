package com.reactnativenavigation.viewcontrollers.navigator;

import android.app.Activity;
import android.view.View;

import com.facebook.react.ReactInstanceManager;
import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.anim.StackAnimator;
import com.reactnativenavigation.mocks.SimpleViewController;
import com.reactnativenavigation.parse.AnimationOptions;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.presentation.LayoutDirectionApplier;
import com.reactnativenavigation.presentation.RootPresenter;
import com.reactnativenavigation.utils.CommandListenerAdapter;
import com.reactnativenavigation.viewcontrollers.ChildControllersRegistry;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.views.BehaviourDelegate;
import com.reactnativenavigation.views.element.ElementTransitionManager;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class RootPresenterTest extends BaseTest {
    private RootPresenter uut;
    private CoordinatorLayout rootContainer;
    private ViewController root;
    private StackAnimator animator;
    private LayoutDirectionApplier layoutDirectionApplier;
    private Options defaultOptions;
    private ReactInstanceManager reactInstanceManager;

    @Override
    public void beforeEach() {
        reactInstanceManager = Mockito.mock(ReactInstanceManager.class);
        Activity activity = newActivity();
        rootContainer = new CoordinatorLayout(activity);
        root = new SimpleViewController(activity, new ChildControllersRegistry(), "child1", new Options());
        animator = spy(createAnimator(activity));
        layoutDirectionApplier = Mockito.mock(LayoutDirectionApplier.class);
        uut = new RootPresenter(animator, layoutDirectionApplier);
        uut.setRootContainer(rootContainer);
        defaultOptions = new Options();
    }

    @Test
    public void setRoot_viewIsAddedToContainer() {
        uut.setRoot(root, defaultOptions, new CommandListenerAdapter(), reactInstanceManager);
        assertThat(root.getView().getParent()).isEqualTo(rootContainer);
        assertThat(((CoordinatorLayout.LayoutParams) root.getView().getLayoutParams()).getBehavior()).isInstanceOf(BehaviourDelegate.class);
    }

    @Test
    public void setRoot_reportsOnSuccess() {
        CommandListenerAdapter listener = spy(new CommandListenerAdapter());
        uut.setRoot(root, defaultOptions, listener, reactInstanceManager);
        verify(listener).onSuccess(root.getId());
    }

    @Test
    public void setRoot_doesNotAnimateByDefault() {
        CommandListenerAdapter listener = spy(new CommandListenerAdapter());
        uut.setRoot(root, defaultOptions, listener, reactInstanceManager);
        verifyZeroInteractions(animator);
        verify(listener).onSuccess(root.getId());
    }

    @Test
    public void setRoot_animates() {
        Options animatedSetRoot = new Options();
        animatedSetRoot.animations.setRoot = new AnimationOptions() {
            @Override
            public boolean hasAnimation() {
                return true;
            }
        };

        ViewController spy = spy(root);
        when(spy.resolveCurrentOptions(defaultOptions)).thenReturn(animatedSetRoot);
        CommandListenerAdapter listener = spy(new CommandListenerAdapter());

        uut.setRoot(spy, defaultOptions, listener, reactInstanceManager);
        verify(animator).setRoot(eq(spy.getView()), eq(animatedSetRoot.animations.setRoot), any());
        verify(listener).onSuccess(spy.getId());
    }

    @Test
    public void setRoot_waitForRenderIsSet() {
        root.options.animations.setRoot.waitForRender = new Bool(true);
        ViewController spy = spy(root);

        uut.setRoot(spy, defaultOptions, new CommandListenerAdapter(), reactInstanceManager);

        ArgumentCaptor<Bool> captor = ArgumentCaptor.forClass(Bool.class);
        verify(spy).setWaitForRender(captor.capture());
        assertThat(captor.getValue().get()).isTrue();
    }

    @Test
    public void setRoot_waitForRender() {
        root.options.animations.setRoot.waitForRender = new Bool(true);

        ViewController spy = spy(root);
        CommandListenerAdapter listener = spy(new CommandListenerAdapter());
        uut.setRoot(spy, defaultOptions, listener, reactInstanceManager);
        verify(spy).addOnAppearedListener(any());
        assertThat(spy.getView().getAlpha()).isZero();
        verifyZeroInteractions(listener);

        spy.onViewWillAppear();
        assertThat(spy.getView().getAlpha()).isOne();
        verify(listener).onSuccess(spy.getId());
    }

    @Test
    public void setRoot_appliesLayoutDirection() {
        CommandListenerAdapter listener = spy(new CommandListenerAdapter());
        uut.setRoot(root, defaultOptions, listener, reactInstanceManager);
        verify(layoutDirectionApplier).apply(root, defaultOptions, reactInstanceManager);
    }

    @NonNull
    private StackAnimator createAnimator(Activity activity) {
        return new StackAnimator(activity, mock(ElementTransitionManager.class)) {
            @Override
            public void setRoot(View root, AnimationOptions setRoot, Runnable onAnimationEnd) {
                onAnimationEnd.run();
            }
        };
    }
}
