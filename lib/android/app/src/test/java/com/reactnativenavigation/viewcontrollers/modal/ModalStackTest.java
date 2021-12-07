package com.reactnativenavigation.viewcontrollers.modal;

import android.app.Activity;
import android.widget.FrameLayout;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.TestUtils;
import com.reactnativenavigation.mocks.SimpleViewController;
import com.reactnativenavigation.options.Options;
import com.reactnativenavigation.react.CommandListener;
import com.reactnativenavigation.react.CommandListenerAdapter;
import com.reactnativenavigation.react.events.EventEmitter;
import com.reactnativenavigation.viewcontrollers.child.ChildControllersRegistry;
import com.reactnativenavigation.viewcontrollers.stack.StackController;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController;
import com.reactnativenavigation.options.TransitionAnimationOptions;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.EmptyStackException;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class ModalStackTest extends BaseTest {
    private static final String MODAL_ID_1 = "modalId1";
    private static final String MODAL_ID_2 = "modalId2";
    private static final String MODAL_ID_3 = "modalId3";
    private static final String MODAL_ID_4 = "modalId4";

    private ModalStack uut;
    private ViewController<?> modal1;
    private ViewController<?> modal2;
    private ViewController<?> modal3;
    private ViewController<?> modal4;
    private StackController stack;
    private Activity activity;
    private ChildControllersRegistry childRegistry;
    private ModalPresenter presenter;
    private ModalAnimator animator;
    private ViewController<?> root;
    private EventEmitter emitter;

    @Override
    public void beforeEach() {
        super.beforeEach();
        activity = newActivity();
        childRegistry = new ChildControllersRegistry();
        root = new SimpleViewController(activity, childRegistry, "root", new Options());

        FrameLayout rootLayout = new FrameLayout(activity);
        CoordinatorLayout modalsLayout = new CoordinatorLayout(activity);
        FrameLayout contentLayout = new FrameLayout(activity);
        contentLayout.addView(rootLayout);
        contentLayout.addView(modalsLayout);
        activity.setContentView(contentLayout);

        animator = spy(new ModalAnimatorMock(activity));
        presenter = spy(new ModalPresenter(animator));
        uut = new ModalStack(activity, presenter);
        uut.setModalsLayout(modalsLayout);
        uut.setRootLayout(rootLayout);
        emitter = Mockito.mock(EventEmitter.class);
        uut.setEventEmitter(emitter);
        modal1 = spy(new SimpleViewController(activity, childRegistry, MODAL_ID_1, new Options()));
        modal2 = spy(new SimpleViewController(activity, childRegistry, MODAL_ID_2, new Options()));
        modal3 = spy(new SimpleViewController(activity, childRegistry, MODAL_ID_3, new Options()));
        modal4 = spy(new SimpleViewController(activity, childRegistry, MODAL_ID_4, new Options()));
        stack = TestUtils.newStackController(activity)
                .setChildren(modal4)
                .build();
    }

    @Test
    public void showModal_DidAppearEventShouldBeCallled(){
        CommandListener listener = spy(new CommandListenerAdapter());
        uut.showModal(modal1, root, listener);
        verify(listener).onSuccess(modal1.getId());
        idleMainLooper();
        verify(modal1).onViewDidAppear();
    }

    @Test
    public void modalRefIsSaved() {
        disableShowModalAnimation(modal1);
        CommandListener listener = spy(new CommandListenerAdapter());
        uut.showModal(modal1, root, listener);
        verify(listener, times(1)).onSuccess(modal1.getId());
        assertThat(findModal(MODAL_ID_1)).isNotNull();
    }

    @Test
    public void showModal() {
        CommandListener listener = spy(new CommandListenerAdapter());
        uut.showModal(modal1, root, listener);
        idleMainLooper();
        verify(listener).onSuccess(modal1.getId());
        verify(modal1).onViewDidAppear();
        assertThat(uut.size()).isOne();
        verify(presenter).showModal(eq(modal1), eq(root), any());
        assertThat(findModal(MODAL_ID_1)).isNotNull();
    }

    @Test
    public void showModal_canShowModalBeforeRootIsSet() {
        CommandListener listener = spy(new CommandListenerAdapter());
        uut.showModal(modal1, null, listener);
        verify(listener).onSuccess(modal1.getId());
    }

    @Test
    public void dismissModal() {
        uut.showModal(modal1, root, new CommandListenerAdapter());
        CommandListener listener = spy(new CommandListenerAdapter());
        uut.dismissModal(modal1.getId(), root, listener);
        assertThat(findModal(modal1.getId())).isNull();
        verify(presenter).dismissModal(eq(modal1), eq(root), eq(root), any());
        verify(listener).onSuccess(modal1.getId());
    }

    @Test
    public void dismissModal_listenerAndEmitterAreInvokedWithRootViewControllerId() {
        uut.showModal(stack, root, new CommandListenerAdapter());
        CommandListener listener = spy(new CommandListenerAdapter());
        uut.dismissModal(modal4.getId(), root, listener);
        verify(listener).onSuccess(stack.getId());
        verify(emitter).emitModalDismissed(stack.getId(), modal4.getCurrentComponentName(), 1);
    }

    @SuppressWarnings("Convert2Lambda")
    @Test
    public void dismissModal_rejectIfModalNotFound() {
        CommandListener listener = spy(new CommandListenerAdapter());
        Runnable onModalWillDismiss = spy(new Runnable() {
            @Override
            public void run() {

            }
        });
        uut.dismissModal(MODAL_ID_1, root, listener);
        verify(onModalWillDismiss, times(0)).run();
        verify(listener, times(1)).onError(anyString());
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void dismissModal_dismissDeepModal() {
        disableShowModalAnimation(modal1, modal2, modal3);
        disableDismissModalAnimation(modal1, modal2, modal3);

        uut.showModal(modal1, root, new CommandListenerAdapter());
        uut.showModal(modal2, root, new CommandListenerAdapter());
        uut.showModal(modal3, root, new CommandListenerAdapter());

        assertThat(root.getView().getParent()).isNull();
        uut.dismissModal(modal1.getId(), root, new CommandListenerAdapter());
        assertThat(root.getView().getParent()).isNull();

        uut.dismissModal(modal3.getId(), root, new CommandListenerAdapter());
        uut.dismissModal(modal2.getId(), root, new CommandListenerAdapter());
        assertThat(root.getView().getParent()).isNotNull();
        assertThat(root.getView().isShown()).isTrue();
    }

    @Test
    public void dismissAllModals() {
        uut.showModal(modal1, root, new CommandListenerAdapter());
        uut.showModal(modal2, root, new CommandListenerAdapter());
        CommandListener listener = spy(new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                assertThat(findModal(modal1.getId())).isNull();
                assertThat(findModal(modal2.getId())).isNull();
                assertThat(uut.isEmpty()).isTrue();
            }
        });
        uut.dismissAllModals(root, Options.EMPTY, listener);
        verify(listener, times(1)).onSuccess(anyString());
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void dismissAllModal_resolvesPromiseSuccessfullyWhenCalledBeforeRootIsSet() {
        CommandListenerAdapter spy = spy(new CommandListenerAdapter());
        uut.dismissAllModals(null, Options.EMPTY, spy);
        verify(spy).onSuccess("");
    }

    @Test
    public void dismissAllModals_resolveSuccessfullyIfEmpty() {
        CommandListener spy = spy(new CommandListenerAdapter());
        uut.dismissAllModals(root, Options.EMPTY, spy);
        verify(spy, times(1)).onSuccess(root.getId());
    }

    @Test
    public void dismissAllModals_optionsAreMergedOnTopModal() {
        uut.showModal(modal1, root, new CommandListenerAdapter());
        uut.showModal(modal2, root, new CommandListenerAdapter());
        uut.showModal(modal3, root, new CommandListenerAdapter());

        Options mergeOptions = new Options();
        uut.dismissAllModals(root, mergeOptions, new CommandListenerAdapter());
        verify(modal3).mergeOptions(mergeOptions);
        verify(modal1, times(0)).mergeOptions(mergeOptions);
        verify(modal2, times(0)).mergeOptions(mergeOptions);
    }

    @Test
    public void dismissAllModals_onlyTopModalIsAnimated() {
        modal2 = spy(modal2);
        Options defaultOptions = new Options();
        uut.setDefaultOptions(defaultOptions);
        Options resolvedOptions = new Options();
        when(modal2.resolveCurrentOptions(defaultOptions)).then(invocation -> resolvedOptions);
        uut.showModal(modal1, root, new CommandListenerAdapter());
        uut.showModal(modal2, root, new CommandListenerAdapter());

        CommandListener listener = spy(new CommandListenerAdapter());
        uut.dismissAllModals(root, Options.EMPTY, listener);
        verify(presenter).dismissModal(eq(modal2), eq(root), eq(root), any());
        verify(listener).onSuccess(modal2.getId());
        verify(animator, never()).dismiss(eq(modal1), eq(modal2), eq(modal1.options.animations.dismissModal), any());
        verify(animator).dismiss(eq(root), eq(modal2), eq(resolvedOptions.animations.dismissModal), any());
        assertThat(uut.size()).isEqualTo(0);
    }

    @Test
    public void dismissAllModals_bottomModalsAreDestroyed() {
        disableModalAnimations(modal1, modal2);
        uut.showModal(modal1, root, new CommandListenerAdapter());
        idleMainLooper();
        uut.showModal(modal2, root, new CommandListenerAdapter());

        uut.dismissAllModals(root, Options.EMPTY, new CommandListenerAdapter());

        verify(modal1).destroy();
        verify(modal1).onViewDisappear();
        assertThat(uut.size()).isEqualTo(0);
    }

    @Test
    public void isEmpty() {
        assertThat(uut.isEmpty()).isTrue();
        uut.showModal(modal1, root, new CommandListenerAdapter());
        assertThat(uut.isEmpty()).isFalse();
        uut.dismissAllModals(root, Options.EMPTY, new CommandListenerAdapter());
        assertThat(uut.isEmpty()).isTrue();
    }

    @Test
    public void peek() {
        assertThat(uut.isEmpty()).isTrue();
        assertThatThrownBy(() -> uut.peek()).isInstanceOf(EmptyStackException.class);
        uut.showModal(modal1, root, new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                assertThat(uut.peek()).isEqualTo(modal1);
            }
        });
    }

    @Test
    public void onDismiss_onViewAppearedInvokedOnPreviousModal() {
        disableShowModalAnimation(modal1, modal2);

        uut.showModal(modal1, root, new CommandListenerAdapter());
        idleMainLooper();
        uut.showModal(modal2, root, new CommandListenerAdapter());
        idleMainLooper();
        uut.dismissModal(modal2.getId(), root, new CommandListenerAdapter());
        idleMainLooper();
        verify(modal1, times(2)).onViewWillAppear();
    }

    @Test
    public void onDismiss_dismissModalInTheMiddleOfStack() {
        disableShowModalAnimation(modal1, modal2, modal3);
        disableDismissModalAnimation(modal1, modal2, modal3);

        uut.showModal(modal1, root, new CommandListenerAdapter());
        uut.showModal(modal2, root, new CommandListenerAdapter());
        idleMainLooper();
        uut.showModal(modal3, root, new CommandListenerAdapter());

        uut.dismissModal(modal2.getId(), root, new CommandListenerAdapter());
        idleMainLooper();
        assertThat(uut.size()).isEqualTo(2);
        verify(modal2).onViewDisappear();
        verify(modal2).destroy();
        assertThat(modal1.getView().getParent()).isNull();
    }

    @Test
    public void handleBack_doesNothingIfModalStackIsEmpty() {
        assertThat(uut.isEmpty()).isTrue();
        assertThat(uut.handleBack(new CommandListenerAdapter(), root)).isFalse();
    }

    @Test
    public void handleBack_dismissModal() {
        disableDismissModalAnimation(modal1);
        uut.showModal(modal1, root, new CommandListenerAdapter());
        idleMainLooper();
        assertThat(uut.handleBack(new CommandListenerAdapter(), root)).isTrue();
        verify(modal1).onViewDisappear();
    }

    @Test
    public void handleBack_ViewControllerTakesPrecedenceOverModal() {
        ViewController<?> backHandlingModal = spy(new SimpleViewController(activity, childRegistry, "stack", new Options()){
            @Override
            public boolean handleBack(CommandListener listener) {
                return true;
            }
        });
        uut.showModal(backHandlingModal, root, new CommandListenerAdapter());

        root.getView().getViewTreeObserver().dispatchOnGlobalLayout();

        assertThat(uut.handleBack(new CommandListenerAdapter(), any())).isTrue();
        verify(backHandlingModal, times(1)).handleBack(any());
        verify(backHandlingModal, times(0)).onViewDisappear();
    }

    @Test
    public void setDefaultOptions() {
        Options defaultOptions = new Options();
        uut.setDefaultOptions(defaultOptions);
        verify(presenter).setDefaultOptions(defaultOptions);
    }

    @Test
    public void destroy() {
        showModalsWithoutAnimation(modal1, modal2);
        uut.destroy();
        verify(modal1).destroy();
        verify(modal2).destroy();
    }

    private ViewController<?> findModal(String id) {
        return uut.findControllerById(id);
    }

    private void showModalsWithoutAnimation(ViewController<?>... modals) {
        for (ViewController<?> modal : modals) {
            showModalWithoutAnimation(modal);
        }
    }

    private void showModalWithoutAnimation(ViewController<?> modal) {
        disableShowModalAnimation(modal);
        uut.showModal(modal, root, new CommandListenerAdapter());
    }
}
