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
import org.mockito.InOrder;
import org.mockito.Mockito;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
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
    private ModalAnimator animator;
    private ViewController root;
    private CoordinatorLayout modalsLayout;

    @Override
    public void beforeEach() {
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
                assertThat(modal1.getView().getParent()).isEqualTo(modalsLayout);
                verify(modal1, times(1)).onViewWillAppear();
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
        JSONObject disabledShowModalAnimation = new JSONObject().put("enabled", false);
        defaultOptions.animations.showModal = new AnimationOptions(disabledShowModalAnimation);

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
        verify(modal1).addOnAppearedListener(any());
        verifyZeroInteractions(animator);
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
        uut.showModal(modal1, root, new CommandListenerAdapter());
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
                verify(modal1, times(1)).onViewDisappear();
                verify(modal1, times(1)).destroy();
            }
        });

        verify(animator).dismiss(eq(modal1.getView()), any(), any());
    }

    @Test
    public void dismissModal_previousViewIsAddedAtIndex0() {
        disableShowModalAnimation(modal1);
        FrameLayout spy = spy(new FrameLayout(newActivity()));
        uut.setRootLayout(spy);

        uut.showModal(modal1, root, new CommandListenerAdapter());
        uut.dismissModal(modal1, root, root, new CommandListenerAdapter());

        verify(spy).addView(root.getView(), 0);
    }

    @Test
    public void dismissModal_noAnimation() {
        disableShowModalAnimation(modal1);
        disableDismissModalAnimation(modal1);

        uut.showModal(modal1, root, new CommandListenerAdapter());
        uut.dismissModal(modal1, root, root, new CommandListenerAdapter());
        verify(modal1, times(1)).onViewDisappear();
        verify(modal1, times(1)).destroy();
        verify(animator, times(0)).dismiss(any(), eq(modal1.options.animations.dismissModal), any());
    }

    @Test
    public void dismissModal_previousModalIsAddedBackToHierarchy() {
        disableShowModalAnimation(modal1, modal2);

        uut.showModal(modal1, root, new CommandListenerAdapter());
        verify(modal1).onViewWillAppear();
        uut.showModal(modal2, modal1, new CommandListenerAdapter());
        assertThat(modal1.getView().getParent()).isNull();
        uut.dismissModal(modal2, modal1, root, new CommandListenerAdapter());
        assertThat(modal1.getView().getParent()).isNotNull();
        verify(modal1, times(2)).onViewWillAppear();
    }

    @Test
    public void dismissModal_previousControllerIsNotAddedIfDismissedModalIsNotTop() {
        disableShowModalAnimation(modal1, modal2);
        disableDismissModalAnimation(modal1, modal2);

        uut.showModal(modal1, root, new CommandListenerAdapter());
        uut.showModal(modal2, modal1, new CommandListenerAdapter());
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
        disableShowModalAnimation(modal1);
        disableDismissModalAnimation(modal1);
        CommandListenerAdapter listener = Mockito.mock(CommandListenerAdapter.class);
        ViewController modal = spy(modal1);
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
