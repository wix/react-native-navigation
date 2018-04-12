package com.reactnativenavigation.viewcontrollers;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.MockPromise;
import com.reactnativenavigation.mocks.ModalCreatorMock;
import com.reactnativenavigation.mocks.SimpleViewController;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.utils.CommandListenerAdapter;
import com.reactnativenavigation.viewcontrollers.modal.Modal;
import com.reactnativenavigation.viewcontrollers.modal.ModalListener;

import org.junit.Test;

import javax.annotation.Nullable;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ModalStackTest extends BaseTest {
    private static final String CONTROLLER_ID = "simpleController";
    private ModalStack uut;
    private SimpleViewController viewController;
    private ModalListener modalListener;

    @Override
    public void beforeEach() {
        createDismissListener();
        uut = spy(new ModalStack(new ModalCreatorMock(), modalListener));
        viewController = new SimpleViewController(newActivity(), CONTROLLER_ID, new Options());
    }

    @SuppressWarnings("Convert2Lambda")
    private void createDismissListener() {
        ModalListener dismissListener = new ModalListener() {
            @Override
            public void onModalDismiss(Modal modal) {

            }

            @Override
            public void onModalDisplay(Modal modal) {

            }
        };
        this.modalListener = spy(dismissListener);
    }

    @Test
    public void modalRefIsSaved() {
        uut.showModal(viewController, new MockPromise());
        assertThat(findModal(CONTROLLER_ID)).isNotNull();
    }

    @Test
    public void modalIsShown() {
        uut.showModal(viewController, new MockPromise() {
            @Override
            public void resolve(@Nullable Object value) {
                verify(findModal(CONTROLLER_ID), times(1)).show();
                verify(modalListener, times(1)).onModalDisplay(any());
            }
        });
    }

    @Test
    public void modalIsDismissed() {
        uut.showModal(viewController, new MockPromise());
        final Modal modal = findModal(CONTROLLER_ID);
        assertThat(modal).isNotNull();
        uut.dismissModal(CONTROLLER_ID, new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                assertThat(findModal(CONTROLLER_ID)).isNull();
                verify(uut, times(1)).onModalDismiss(modal);
            }
        });
    }

    @Test
    public void dismissAllModals() {
        uut.showModal(new SimpleViewController(newActivity(), "1", new Options()), new MockPromise());
        uut.showModal(new SimpleViewController(newActivity(), "2", new Options()), new MockPromise());
        uut.dismissAll(new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                assertThat(uut.isEmpty()).isTrue();
            }
        });
    }

    @Test
    public void isEmpty() {
        assertThat(uut.isEmpty()).isTrue();
        uut.showModal(viewController, new MockPromise());
        assertThat(uut.isEmpty()).isFalse();
        uut.dismissAll(new CommandListenerAdapter());
        assertThat(uut.isEmpty()).isTrue();
    }

    @Test
    public void onDismiss() {
        uut.showModal(viewController, new MockPromise());
        uut.showModal(new SimpleViewController(newActivity(), "otherComponent", new Options()), new MockPromise());
        uut.dismissAll(new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                verify(uut, times(2)).onModalDismiss(any());
            }
        });
    }

    @Test
    public void onDismiss_onViewAppearedInvokedOnPreviousModal() {
        SimpleViewController viewController = spy(new SimpleViewController(newActivity(), "otherComponent", new Options()));
        uut.showModal(viewController, new MockPromise());
        uut.showModal(this.viewController, new MockPromise());
        uut.dismissModal(CONTROLLER_ID, new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                verify(viewController, times(1)).onViewAppeared();
            }
        });
    }

    private Modal findModal(String id) {
        return uut.findModalByComponentId(id);
    }
}
