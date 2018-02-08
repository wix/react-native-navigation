package com.reactnativenavigation.viewcontrollers;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.MockPromise;
import com.reactnativenavigation.mocks.ModalCreatorMock;
import com.reactnativenavigation.mocks.SimpleViewController;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.viewcontrollers.modal.Modal;

import org.junit.Test;

import javax.annotation.Nullable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ModalStackTest extends BaseTest {
    private static final String CONTROLLER_ID = "simpleController";
    private ModalStack uut;
    private SimpleViewController viewController;

    @Override
    public void beforeEach() {
        uut = spy(new ModalStack(new ModalCreatorMock()));
        viewController = new SimpleViewController(newActivity(), CONTROLLER_ID, new Options());
    }

    @Test
    public void modalRefIsSaved() throws Exception {
        uut.showModal(viewController, new MockPromise());
        assertThat(findModal()).isNotNull();
    }

    @Test
    public void modalIsShown() throws Exception {
        uut.showModal(viewController, new MockPromise() {
            @Override
            public void resolve(@Nullable Object value) {
                verify(findModal(), times(1)).show();
            }
        });
    }

    @Test
    public void modalIsDismissed() throws Exception {
        uut.showModal(viewController, new MockPromise());
        assertThat(findModal()).isNotNull();
        uut.dismissModal(CONTROLLER_ID, new MockPromise());
        assertThat(findModal()).isNull();
    }

    private Modal findModal() {
        return uut.findModalByComponentId("simpleController");
    }
}
