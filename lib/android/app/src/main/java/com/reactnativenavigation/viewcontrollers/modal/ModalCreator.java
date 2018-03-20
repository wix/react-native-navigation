package com.reactnativenavigation.viewcontrollers.modal;

import com.reactnativenavigation.viewcontrollers.ViewController;

public class ModalCreator {
    public Modal create(ViewController viewController, ModalListener dismissListener) {
        return new Modal(viewController, dismissListener);
    }
}
