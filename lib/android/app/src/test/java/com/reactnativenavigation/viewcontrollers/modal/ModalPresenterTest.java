package com.reactnativenavigation.viewcontrollers.modal;

import android.app.Activity;
import android.widget.FrameLayout;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.anim.ModalAnimator;
import com.reactnativenavigation.mocks.SimpleViewController;
import com.reactnativenavigation.parse.AnimationOptions;
import com.reactnativenavigation.parse.ModalPresentationStyle;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.utils.CommandListener;
import com.reactnativenavigation.utils.CommandListenerAdapter;
import com.reactnativenavigation.viewcontrollers.ChildController;
import com.reactnativenavigation.viewcontrollers.ChildControllersRegistry;
import com.reactnativenavigation.viewcontrollers.ViewController;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class ModalPresenterTest extends BaseTest {
    private static final String MODAL_ID_1 = "modalId1";
    private static final String MODAL_ID_2 = "modalId2";

    private ChildController modal1;
    private ChildController modal2;
    private ModalPresenter uut;
    private FrameLayout contentLayout;
    private ModalAnimator animator;
    private ViewController root;

    @Override
    public void beforeEach() {
        Activity activity = newActivity();
        ChildControllersRegistry childRegistry = new ChildControllersRegistry();

        root = spy(new SimpleViewController(activity, childRegistry, "root", new Options()));
        contentLayout = new FrameLayout(activity);
        contentLayout.addView(root.getView());
        activity.setContentView(contentLayout);

        animator = spy(new ModalAnimator(activity));
        uut = new ModalPresenter(animator);
        uut.setContentLayout(contentLayout);
        modal1 = spy(new SimpleViewController(activity, childRegistry, MODAL_ID_1, new Options()));
        modal2 = spy(new SimpleViewController(activity, childRegistry, MODAL_ID_2, new Options()));
    }

    @Test
    public void showModal() {
        Options defaultOptions = new Options();
        uut.setDefaultOptions(defaultOptions);
        disableShowModalAnimation(modal1);
        uut.showModal(modal1, root, new CommandListenerAdapter());
        verify(modal1).setWaitForRender(any());
        verify(modal1).resolveCurrentOptions(defaultOptions);
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
        uut.showModal(modal1, root, listener);
        verify(animator, times(0)).show(
                eq(modal1.getView()),
                eq(modal1.options.animations.showModal),
                any()
        );
        verify(listener, times(1)).onSuccess(MODAL_ID_1);
    }

    @Test
    public void showModal_resolvesDefaultOptions() throws JSONException {
        Options defaultOptions = new Options();
        JSONObject disabledShowModalAnimation = new JSONObject().put("enable", false);
        defaultOptions.animations.showModal = AnimationOptions.parse(disabledShowModalAnimation);

        uut.setDefaultOptions(defaultOptions);
        uut.showModal(modal1, root, new CommandListenerAdapter());
        verifyZeroInteractions(animator);
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
    public void showModal_waitForRender() {
        modal1.options.animations.showModal.waitForRender = new Bool(true);
        uut.showModal(modal1, root, new CommandListenerAdapter());
        verify(modal1).setOnAppearedListener(any());
        verifyZeroInteractions(animator);
    }

    @Test
    public void dismissModal_animatesByDefault() {
        disableShowModalAnimation(modal1);

        uut.showModal(modal1, root, new CommandListenerAdapter());
        uut.dismissTopModal(modal1, root, new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                verify(modal1, times(1)).onViewDisappear();
                verify(modal1, times(1)).destroy();
            }
        });

        verify(animator, times(1)).dismiss(eq(modal1.getView()), eq(modal1.options.animations.dismissModal), any());
    }

    @Test
    public void dismissModal_previousViewIsAddedAtIndex0() {
        modal2.ensureViewIsCreated();
        FrameLayout spy = spy(new FrameLayout(newActivity()));
        uut.setContentLayout(spy);
        uut.dismissTopModal(modal1, modal2, new CommandListenerAdapter());
        verify(spy, times(1)).addView(modal2.getView(), 0);
    }

    @Test
    public void dismissModal_noAnimation() {
        disableShowModalAnimation(modal1);
        disableDismissModalAnimation(modal1);

        uut.showModal(modal1, root, new CommandListenerAdapter());
        uut.dismissTopModal(modal1, root, new CommandListenerAdapter());
        verify(modal1, times(1)).onViewDisappear();
        verify(modal1, times(1)).destroy();
        verify(animator, times(0)).dismiss(any(), eq(modal1.options.animations.dismissModal), any());
    }

    @Test
    public void dismissModal_previousModalIsAddedBackToHierarchy() {
        disableShowModalAnimation(modal1, modal2);

        uut.showModal(modal1, root, new CommandListenerAdapter());
        uut.showModal(modal2, modal1, new CommandListenerAdapter());
        assertThat(modal1.getView().getParent()).isNull();
        uut.dismissTopModal(modal2, modal1, new CommandListenerAdapter());
        verify(modal1, times(2)).onViewAppeared();
    }

    @Test
    public void dismissModal_previousControllerIsNotAddedIfDismissedModalIsNotTop() {
        disableShowModalAnimation(modal1, modal2);
        disableDismissModalAnimation(modal1, modal2);

        uut.showModal(modal1, root, new CommandListenerAdapter());
        uut.showModal(modal2, modal1, new CommandListenerAdapter());
        assertThat(modal1.getView().getParent()).isNull();
        assertThat(root.getView().getParent()).isNull();

        uut.dismissModal(modal1, new CommandListenerAdapter());
        assertThat(root.getView().getParent()).isNull();

        uut.dismissTopModal(modal2, root, new CommandListenerAdapter());
        assertThat(root.getView().getParent()).isNotNull();
    }

    @Test
    public void dismissModal_previousViewIsNotDetachedIfOverCurrentContext() {
        modal1.options.modal.presentationStyle = ModalPresentationStyle.OverCurrentContext;
        disableShowModalAnimation(modal1, modal2);

        uut.showModal(modal1, root, new CommandListenerAdapter());
        assertThat(root.getView().getParent()).isNotNull();
        verify(root, times(0)).onViewDisappear();
    }
}
