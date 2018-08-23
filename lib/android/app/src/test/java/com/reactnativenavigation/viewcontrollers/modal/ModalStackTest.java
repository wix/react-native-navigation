package com.reactnativenavigation.viewcontrollers.modal;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.anim.ModalAnimator;
import com.reactnativenavigation.mocks.SimpleViewController;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.react.EventEmitter;
import com.reactnativenavigation.utils.CommandListener;
import com.reactnativenavigation.utils.CommandListenerAdapter;
import com.reactnativenavigation.viewcontrollers.ChildControllersRegistry;
import com.reactnativenavigation.viewcontrollers.ViewController;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.EmptyStackException;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class ModalStackTest extends BaseTest {
    private static final String MODAL_ID_1 = "modalId1";
    private static final String MODAL_ID_2 = "modalId2";
    private static final String MODAL_ID_3 = "modalId3";

    private ModalStack uut;
    private ViewController modal1;
    private ViewController modal2;
    private ViewController modal3;
    private Activity activity;
    private ChildControllersRegistry childRegistry;
    private ModalPresenter presenter;
    private ModalAnimator animator;
    private ViewController rootController;

    @Override
    public void beforeEach() {
        activity = newActivity();
        childRegistry = new ChildControllersRegistry();

        this.rootController = new SimpleViewController(activity, childRegistry, "root", new Options());
        FrameLayout activityContentView = new FrameLayout(activity);
        activityContentView.addView(rootController.getView());
        activity.setContentView(activityContentView);

        animator = spy(new ModalAnimatorMock(activity));
        presenter = spy(new ModalPresenter(animator));
        uut = new ModalStack(presenter);
        uut.setContentLayout(activityContentView);
        uut.setEventEmitter(Mockito.mock(EventEmitter.class));
        modal1 = spy(new SimpleViewController(activity, childRegistry, MODAL_ID_1, new Options()));
        modal2 = spy(new SimpleViewController(activity, childRegistry, MODAL_ID_2, new Options()));
        modal3 = spy(new SimpleViewController(activity, childRegistry, MODAL_ID_3, new Options()));
    }

    @Test
    public void modalRefIsSaved() {
        disableShowModalAnimation(modal1);
        CommandListener listener = spy(new CommandListenerAdapter());
        uut.showModal(modal1, rootController, listener);
        verify(listener, times(1)).onSuccess(modal1.getId());
        assertThat(findModal(MODAL_ID_1)).isNotNull();
    }

    @Test
    public void showModal() {
        CommandListener listener = spy(new CommandListenerAdapter());
        uut.showModal(modal1, rootController, listener);
        verify(listener, times(1)).onSuccess(modal1.getId());
        assertThat(uut.size()).isOne();
        verify(presenter, times(1)).showModal(modal1, rootController, listener);
        assertThat(findModal(MODAL_ID_1)).isNotNull();
    }

    @SuppressWarnings("Convert2Lambda")
    @Test
    public void dismissModal() {
        uut.showModal(modal1, rootController, new CommandListenerAdapter());
        CommandListener listener = spy(new CommandListenerAdapter());
        uut.dismissModal(modal1.getId(), rootController, listener);
        assertThat(findModal(modal1.getId())).isNull();
        verify(presenter, times(1)).dismissTopModal(eq(modal1), eq(rootController), any());
        verify(listener).onSuccess(modal1.getId());
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
        uut.dismissModal(MODAL_ID_1, rootController, listener);
        verify(onModalWillDismiss, times(0)).run();
        verify(listener, times(1)).onError(anyString());
        verifyZeroInteractions(listener);
    }

    @Test
    public void dismissModal_dismissDeepModal() {
        disableShowModalAnimation(modal1, modal2, modal3);
        disableDismissModalAnimation(modal1, modal2, modal3);

        uut.showModal(modal1, rootController, new CommandListenerAdapter());
        uut.showModal(modal2, rootController, new CommandListenerAdapter());
        uut.showModal(modal3, rootController, new CommandListenerAdapter());

        assertThat(rootController.getView().getParent()).isNull();
        uut.dismissModal(modal1.getId(), rootController, new CommandListenerAdapter());
        assertThat(rootController.getView().getParent()).isNull();

        uut.dismissModal(modal3.getId(), rootController, new CommandListenerAdapter());
        uut.dismissModal(modal2.getId(), rootController, new CommandListenerAdapter());
        assertThat(rootController.getView().getParent()).isNotNull();
        assertThat(rootController.getView().isShown()).isTrue();
    }

    @Test
    public void dismissAllModals() {
        uut.showModal(modal1, rootController, new CommandListenerAdapter());
        uut.showModal(modal2, rootController, new CommandListenerAdapter());
        CommandListener listener = spy(new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                assertThat(findModal(modal1.getId())).isNull();
                assertThat(findModal(modal2.getId())).isNull();
                assertThat(uut.isEmpty()).isTrue();
            }
        });
        uut.dismissAllModals(listener, rootController);
        verify(listener, times(1)).onSuccess(anyString());
        verifyZeroInteractions(listener);
    }

    @Test
    public void dismissAllModals_rejectIfEmpty() {
        CommandListener spy = spy(new CommandListenerAdapter());
        uut.dismissAllModals(spy, rootController);
        verify(spy, times(1)).onError(any());
    }

    @SuppressWarnings("Convert2Lambda")
    @Test
    public void dismissAllModals_onlyTopModalIsAnimated() {
        uut.showModal(modal1, rootController, new CommandListenerAdapter());
        uut.showModal(modal2, rootController, new CommandListenerAdapter());

        ViewGroup view1 = modal1.getView();
        ViewGroup view2 = modal2.getView();
        CommandListener listener = spy(new CommandListenerAdapter());
        uut.dismissAllModals(listener, rootController);
        verify(presenter, times(1)).dismissTopModal(eq(modal2), eq(rootController), any());
        verify(listener).onSuccess(modal2.getId());
        verify(animator, times(0)).dismiss(eq(view1), eq(modal1.options.animations.dismissModal), any());
        verify(animator, times(1)).dismiss(eq(view2), eq(modal2.options.animations.dismissModal), any());
        assertThat(uut.size()).isEqualTo(0);
    }

    @Test
    public void dismissAllModals_bottomModalsAreDestroyed() {
        uut.showModal(modal1, rootController, new CommandListenerAdapter());
        uut.showModal(modal2, rootController, new CommandListenerAdapter());

        uut.dismissAllModals(new CommandListenerAdapter(), rootController);

        verify(modal1, times(1)).destroy();
        verify(modal1, times(1)).onViewDisappear();
        assertThat(uut.size()).isEqualTo(0);
    }

    @Test
    public void isEmpty() {
        assertThat(uut.isEmpty()).isTrue();
        uut.showModal(modal1, rootController, new CommandListenerAdapter());
        assertThat(uut.isEmpty()).isFalse();
        uut.dismissAllModals(new CommandListenerAdapter(), rootController);
        assertThat(uut.isEmpty()).isTrue();
    }

    @Test
    public void peek() {
        assertThat(uut.isEmpty()).isTrue();
        assertThatThrownBy(() -> uut.peek()).isInstanceOf(EmptyStackException.class);
        uut.showModal(modal1, rootController, new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                assertThat(uut.peek()).isEqualTo(modal1);
            }
        });
    }

    @Test
    public void onDismiss_onViewAppearedInvokedOnPreviousModal() {
        disableShowModalAnimation(modal1, modal2);

        uut.showModal(modal1, rootController, new CommandListenerAdapter());
        uut.showModal(modal2, rootController, new CommandListenerAdapter());
        uut.dismissModal(modal2.getId(), rootController, new CommandListenerAdapter());
        verify(modal1, times(2)).onViewAppeared();
    }

    @Test
    public void onDismiss_dismissModalInTheMiddleOfStack() {
        disableShowModalAnimation(modal1, modal2, modal3);
        disableDismissModalAnimation(modal1, modal2, modal3);

        uut.showModal(modal1, rootController, new CommandListenerAdapter());
        uut.showModal(modal2, rootController, new CommandListenerAdapter());
        uut.showModal(modal3, rootController, new CommandListenerAdapter());

        uut.dismissModal(modal2.getId(), rootController, new CommandListenerAdapter());
        assertThat(uut.size()).isEqualTo(2);
        verify(modal2, times(1)).onViewDisappear();
        verify(modal2, times(1)).destroy();
        assertThat(modal1.getView().getParent()).isNull();
    }

    @Test
    public void handleBack_doesNothingIfModalStackIsEmpty() {
        assertThat(uut.isEmpty()).isTrue();
        assertThat(uut.handleBack(new CommandListenerAdapter(), rootController)).isFalse();
    }

    @Test
    public void handleBack_dismissModal() {
        disableDismissModalAnimation(modal1);
        uut.showModal(modal1, rootController, new CommandListenerAdapter());
        assertThat(uut.handleBack(new CommandListenerAdapter(), rootController)).isTrue();
        verify(modal1, times(1)).onViewDisappear();

    }

    @Test
    public void handleBack_ViewControllerTakesPrecedenceOverModal() {
        ViewController backHandlingModal = spy(new SimpleViewController(activity, childRegistry, "stack", new Options()){
            @Override
            public boolean handleBack(CommandListener listener) {
                return true;
            }
        });
        uut.showModal(backHandlingModal, rootController, new CommandListenerAdapter());

        rootController.getView().getViewTreeObserver().dispatchOnGlobalLayout();

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

    private ViewController findModal(String id) {
        return uut.findControllerById(id);
    }
}
