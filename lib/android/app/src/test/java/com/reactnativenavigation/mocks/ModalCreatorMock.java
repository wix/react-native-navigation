package com.reactnativenavigation.mocks;

import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.viewcontrollers.modal.Modal;
import com.reactnativenavigation.viewcontrollers.modal.ModalCreator;
import com.reactnativenavigation.viewcontrollers.modal.ModalListener;

import static org.mockito.Mockito.spy;

public class ModalCreatorMock extends ModalCreator {
    @Override
    public Modal create(ViewController viewController, ModalListener onDismissListener) {
        return spy(new Modal(viewController, onDismissListener));
    }
}
