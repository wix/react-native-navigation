package com.reactnativenavigation.viewcontrollers.modal;

import android.app.Activity;
import android.os.Looper;
import android.widget.FrameLayout;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.SimpleViewController;
import com.reactnativenavigation.options.TransitionAnimationOptionsKt;
import com.reactnativenavigation.options.TransitionAnimationOptionsTestKt;
import com.reactnativenavigation.options.ModalPresentationStyle;
import com.reactnativenavigation.options.Options;
import com.reactnativenavigation.options.params.Bool;
import com.reactnativenavigation.react.CommandListener;
import com.reactnativenavigation.react.CommandListenerAdapter;
import com.reactnativenavigation.viewcontrollers.child.ChildController;
import com.reactnativenavigation.viewcontrollers.child.ChildControllersRegistry;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.robolectric.Shadows;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

public class ModalPresenterTest extends BaseTest {
    private static final String MODAL_ID_1 = "modalId1";
    private static final String MODAL_ID_2 = "modalId2";

    private ChildController<?> modal1;
    private ChildController<?> modal2;
    private ModalPresenter uut;
    private ModalAnimator animator;
    private ViewController<?> root;
    private CoordinatorLayout modalsLayout;


    @Override
    public void beforeEach() {
        super.beforeEach();
        Activity activity = newActivity();
        ChildControllersRegistry childRegistry = new ChildControllersRegistry();

        root = spy(new SimpleViewController(activity, childRegistry, "root", new Options()));
        FrameLayout contentLayout = new FrameLayout(activity);
        FrameLayout rootLayout = new FrameLayout(activity);
        rootLayout.addView(root.getView());
        modalsLayout = new CoordinatorLayout(activity);
        contentLayout.addView(rootLayout);
        contentLayout.addView(modalsLayout);
        activity.setContentView(contentLayout);

        animator = spy(new ModalAnimator(activity));
        uut = new ModalPresenter(animator);
        uut.setModalsLayout(modalsLayout);
        uut.setRootLayout(rootLayout);
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
        assertThat(modal1.getView().getFitsSystemWindows()).isTrue();
    }

    @Test
    public void showModal_noAnimation() {
        disableShowModalAnimation(modal1);
        CommandListener listener = spy(new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                Shadows.shadowOf(Looper.getMainLooper()).idle();
                assertThat(modal1.getView().getParent()).isEqualTo(modalsLayout);
                verify(modal1).onViewWillAppear();
            }
        });
        uut.showModal(modal1, root, listener);
        verify(animator, never()).show(
                eq(modal1),
                eq(root),
                eq(modal1.options.animations.showModal),
                any()
        );
        verify(listener).onSuccess(MODAL_ID_1);
    }

    @Test
    public void showModal_resolvesDefaultOptions() throws JSONException {
        Options defaultOptions = new Options();
        JSONObject disabledShowModalAnimation = TransitionAnimationOptionsTestKt.newModalAnimationJson(false);
        defaultOptions.animations.showModal = TransitionAnimationOptionsKt.parseTransitionAnimationOptions(disabledShowModalAnimation);
        uut.setDefaultOptions(defaultOptions);
        uut.showModal(modal1, root, new CommandListenerAdapter());
        verifyNoInteractions(animator);
    }

    @Test
    public void showModal_previousModalIsRemovedFromHierarchy() {
        uut.showModal(modal1, root, new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                uut.showModal(modal2, modal1, new CommandListenerAdapter() {
                    @Override
                    public void onSuccess(String childId) {
                        idleMainLooper();
                        assertThat(modal1.getView().getParent()).isNull();
                        verify(modal1).onViewDisappear();
                    }
                });
                assertThat(modal1.getView().getParent()).isEqualTo(modal2.getView().getParent());
            }
        });
    }

    @Test
    public void showModal_overCurrentContext_previousModalIsNotRemovedFromHierarchy() {
        Options options = new Options();
        options.modal.presentationStyle = ModalPresentationStyle.OverCurrentContext;
        uut.setDefaultOptions(options);
        disableShowModalAnimation(modal1);
        uut.showModal(modal1, root, new CommandListenerAdapter());
        verify(root, times(0)).detachView();
        verify(root, times(0)).onViewDisappear();
    }

    @Test
    public void showModal_animatesByDefault() {
        uut.showModal(modal1, root, new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                verify(animator).show(
                        eq(modal1),
                        eq(root),
                        any(),
                        any()
                );
                assertThat(animator.isRunning()).isFalse();
            }
        });
        assertThat(animator.isRunning()).isTrue();
    }

    @Test
    public void showModal_waitForRender() {
        modal1.options.animations.showModal.setWaitForRender(new Bool(true));
        uut.showModal(modal1, root, new CommandListenerAdapter());
        verify(modal1).addOnAppearedListener(any());
        verifyNoInteractions(animator);
    }

    @Test
    public void showModal_rejectIfContentIsNull() {
        uut.setModalsLayout(null);
        CommandListenerAdapter listener = Mockito.mock(CommandListenerAdapter.class);
        uut.showModal(modal1, modal2, listener);
        verify(listener).onError(any());
    }

    @Test
    public void showModal_onViewDidAppearIsInvokedBeforeViewDisappear() {
        disableShowModalAnimation(modal1);
        root.onViewWillAppear();

        uut.showModal(modal1, root, new CommandListenerAdapter());
        idleMainLooper();
        InOrder inOrder = inOrder(modal1, root);
        inOrder.verify(modal1).onViewDidAppear();
        inOrder.verify(root).onViewDisappear();
    }

    @Test
    public void dismissModal_animatesByDefault() {
        disableShowModalAnimation(modal1);

        uut.showModal(modal1, root, new CommandListenerAdapter());
        uut.dismissModal(modal1, root, root, new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                post(() -> {
                    verify(modal1).onViewDisappear();
                    verify(modal1).destroy();
                });
            }
        });

        verify(animator).dismiss(eq(root), eq(modal1), any(), any());
    }

    @Test
    public void dismissModal_previousViewIsAddedAtIndex0() {
        disableShowModalAnimation(modal1);
        FrameLayout spy = spy(new FrameLayout(newActivity()));
        uut.setRootLayout(spy);

        uut.showModal(modal1, root, new CommandListenerAdapter());
        idleMainLooper();
        uut.dismissModal(modal1, root, root, new CommandListenerAdapter());

        verify(spy).addView(root.getView(), 0);
    }

    @Test
    public void dismissModal_noAnimation() {
        disableModalAnimations(modal1);

        uut.showModal(modal1, root, new CommandListenerAdapter());
        Shadows.shadowOf(Looper.getMainLooper()).idle();
        uut.dismissModal(modal1, root, root, new CommandListenerAdapter());
        verify(modal1).onViewDisappear();
        verify(modal1).destroy();
        verify(animator, never()).dismiss(eq(root), any(), eq(modal1.options.animations.dismissModal), any());
    }

    @Test
    public void dismissModal_previousModalIsAddedBackToHierarchy() {
        disableShowModalAnimation(modal1, modal2);

        uut.showModal(modal1, root, new CommandListenerAdapter());
        idleMainLooper();
        assertThat(modal1.getView().getParent()).isNotNull();
        verify(modal1).onViewWillAppear();

        uut.showModal(modal2, modal1, new CommandListenerAdapter());
        idleMainLooper();
        assertThat(modal1.getView().getParent()).isNull();

        Shadows.shadowOf(Looper.getMainLooper()).idle();
        uut.dismissModal(modal2, modal1, root, new CommandListenerAdapter());
        assertThat(modal1.getView().getParent()).isNotNull();
        idleMainLooper();
        verify(modal1, times(2)).onViewWillAppear();
    }

    @Test
    public void dismissModal_previousControllerIsNotAddedIfDismissedModalIsNotTop() {
        disableShowModalAnimation(modal1, modal2);
        disableDismissModalAnimation(modal1, modal2);

        uut.showModal(modal1, root, new CommandListenerAdapter());
        uut.showModal(modal2, modal1, new CommandListenerAdapter());
        idleMainLooper();
        assertThat(modal1.getView().getParent()).isNull();
        assertThat(root.getView().getParent()).isNull();

        uut.dismissModal(modal1, null, root, new CommandListenerAdapter());
        assertThat(root.getView().getParent()).isNull();

        uut.dismissModal(modal2, root, root, new CommandListenerAdapter());
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

    @Test
    public void dismissModal_rejectIfContentIsNull() {
        uut.setModalsLayout(null);
        CommandListenerAdapter listener = Mockito.mock(CommandListenerAdapter.class);
        uut.dismissModal(modal1, root, root, listener);
        verify(listener).onError(any());
    }

    @Test
    public void dismissModal_successIsReportedBeforeViewIsDestroyed() {
        disableModalAnimations(modal1);
        CommandListenerAdapter listener = Mockito.mock(CommandListenerAdapter.class);
        ViewController<?> modal = spy(modal1);
        InOrder inOrder = inOrder(listener, modal);

        uut.showModal(modal, root, new CommandListenerAdapter());

        uut.dismissModal(modal, root, root, listener);
        inOrder.verify(listener).onSuccess(modal.getId());
        inOrder.verify(modal).destroy();
    }

    @Test
    public void dismissModal_modalsLayoutIfHiddenIsAllModalsAreDismissed() {
        disableShowModalAnimation(modal1, modal2);
        disableDismissModalAnimation(modal1, modal2);

        uut.showModal(modal1, root, new CommandListenerAdapter());
        assertVisible(modalsLayout);
        uut.showModal(modal2, modal1, new CommandListenerAdapter());
        assertVisible(modalsLayout);

        uut.dismissModal(modal2, modal1, root, new CommandListenerAdapter());
        assertVisible(modalsLayout);
        uut.dismissModal(modal1, root, root, new CommandListenerAdapter());
        assertGone(modalsLayout);
    }


}
