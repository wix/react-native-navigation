package com.reactnativenavigation.viewcontrollers.modal;

import android.app.Activity;
import android.widget.FrameLayout;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.anim.ModalAnimator2;
import com.reactnativenavigation.mocks.SimpleViewController;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.utils.CommandListenerAdapter;
import com.reactnativenavigation.viewcontrollers.Navigator.CommandListener;
import com.reactnativenavigation.viewcontrollers.ViewController;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ModalPresenterTest extends BaseTest {
    private static final String MODAL_ID_1 = "modalId1";
    private static final String MODAL_ID_2 = "modalId2";

    private ViewController modal1;
    private ViewController modal2;
    private ModalPresenter uut;
    private FrameLayout contentLayout;
    private ModalAnimator2 animator;

    @Override
    public void beforeEach() {
        Activity activity = newActivity();
        animator = spy(new ModalAnimator2(activity));
        uut = new ModalPresenter(animator);
        contentLayout = new FrameLayout(activity);
        activity.setContentView(contentLayout);
        uut.setContentLayout(contentLayout);
        modal1 = spy(new SimpleViewController(activity, MODAL_ID_1, new Options()));
        modal2 = spy(new SimpleViewController(activity, MODAL_ID_2, new Options()));
    }

    @Test
    public void showModal_noAnimation() {
        disableShowModalAnimation(modal1);
        CommandListener listener = spy(new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                assertThat(modal1.getView().getParent()).isEqualTo(contentLayout);
                verify(modal1, times(1)).onViewAppeared();
            }
        });
        uut.showModal(modal1, null, listener);
        verify(animator, times(0)).show(
                eq(modal1.getView()),
                eq(modal1.options.animations.showModal),
                any()
        );
        verify(listener, times(1)).onSuccess(MODAL_ID_1);
    }

    @Test
    public void showModal_previousModalIsRemovedFromHierarchy() {
        uut.showModal(modal1, null, new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                uut.showModal(modal2, modal1, new CommandListenerAdapter() {
                    @Override
                    public void onSuccess(String childId) {
                        assertThat(modal1.getView().getParent()).isNull();
                        verify(modal1, times(1)).onViewDisappear();
                    }
                });
                assertThat(modal1.getView().getParent()).isEqualTo(modal2.getView().getParent());
            }
        });
    }

    @Test
    public void showModal_animatesByDefault() {
        uut.showModal(modal1, null, new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                verify(animator, times(1)).show(
                        eq(modal1.getView()),
                        eq(modal1.options.animations.showModal),
                        any()
                );
                assertThat(animator.isRunning()).isFalse();
            }
        });
    }

    @Test
    public void dismissModal_animatesByDefault() {
        disableShowModalAnimation(modal1);

        uut.showModal(modal1, null, new CommandListenerAdapter());
        uut.dismissModal(modal1, null, new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                verify(modal1, times(1)).onViewDisappear();
                verify(modal1, times(1)).destroy();
            }
        });

        verify(animator, times(1)).dismiss(eq(modal1.getView()), any());
    }

    @Test
    public void dismissModal_previousModalIsAddedAtIndex0() {
        FrameLayout spy = spy(new FrameLayout(newActivity()));
        uut.setContentLayout(spy);
        uut.dismissModal(modal1, modal2, new CommandListenerAdapter());
        verify(spy, times(1)).addView(modal2.getView(), 0);
    }

    @Test
    public void dismissModal_noAnimation() {
        disableShowModalAnimation(modal1);
        disableDismissModalAnimation(modal1);

        uut.showModal(modal1, null, new CommandListenerAdapter());
        uut.dismissModal(modal1, null, new CommandListenerAdapter());
        verify(modal1, times(1)).onViewDisappear();
        verify(modal1, times(1)).destroy();
        verify(animator, times(0)).dismiss(any(), any());
    }

    @Test
    public void dismissModal_previousModalIsAddedBackToHierarchy() {
        disableShowModalAnimation(modal1, modal2);

        uut.showModal(modal1, null, new CommandListenerAdapter());
        uut.showModal(modal2, modal1, new CommandListenerAdapter());
        assertThat(modal1.getView().getParent()).isNull();
        uut.dismissModal(modal2, modal1, new CommandListenerAdapter());
        verify(modal1, times(2)).onViewAppeared();
    }
}
