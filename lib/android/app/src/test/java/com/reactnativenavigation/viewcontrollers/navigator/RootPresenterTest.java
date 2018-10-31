package com.reactnativenavigation.viewcontrollers.navigator;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.FrameLayout;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.anim.NavigationAnimator;
import com.reactnativenavigation.mocks.SimpleViewController;
import com.reactnativenavigation.parse.AnimationOptions;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.utils.CommandListenerAdapter;
import com.reactnativenavigation.viewcontrollers.ChildControllersRegistry;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.views.element.ElementTransitionManager;

import org.junit.Test;

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
    private FrameLayout rootContainer;
    private ViewController root;
    private NavigationAnimator animator;
    private Options defaultOptions;


    @Override
    public void beforeEach() {
        Activity activity = newActivity();
        rootContainer = new FrameLayout(activity);
        root = new SimpleViewController(activity, new ChildControllersRegistry(), "child1", new Options());
        animator = spy(createAnimator(activity));
        uut = new RootPresenter(animator);
        uut.setRootContainer(rootContainer);
        defaultOptions = new Options();
    }

    @Test
    public void setRoot_viewIsAddedToContainer() {
        uut.setRoot(root, defaultOptions, new CommandListenerAdapter());
        assertThat(root.getView().getParent()).isEqualTo(rootContainer);
    }

    @Test
    public void setRoot_reportsOnSuccess() {
        CommandListenerAdapter listener = spy(new CommandListenerAdapter());
        uut.setRoot(root, defaultOptions, listener);
        verify(listener).onSuccess(root.getId());
    }

    @Test
    public void setRoot_doesNotAnimateByDefault() {
        CommandListenerAdapter listener = spy(new CommandListenerAdapter());
        uut.setRoot(root, defaultOptions, listener);
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

        uut.setRoot(spy, defaultOptions, listener);
        verify(animator).setRoot(eq(spy.getView()), eq(animatedSetRoot.animations.setRoot), any());
        verify(listener).onSuccess(spy.getId());
    }

    @NonNull
    private NavigationAnimator createAnimator(Activity activity) {
        return new NavigationAnimator(activity, mock(ElementTransitionManager.class)) {
            @Override
            public void setRoot(View root, AnimationOptions setRoot, Runnable onAnimationEnd) {
                onAnimationEnd.run();
            }
        };
    }
}
