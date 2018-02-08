package com.reactnativenavigation.mocks;

import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.viewcontrollers.modal.Modal;
import com.reactnativenavigation.viewcontrollers.modal.ModalCreator;

import static org.mockito.Mockito.spy;

public class ModalCreatorMock extends ModalCreator {
    @Override
    public Modal create(ViewController viewController) {
        return spy(new Modal(viewController));
    }
}
