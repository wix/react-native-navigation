package com.reactnativenavigation.viewcontrollers.modal;

import com.reactnativenavigation.viewcontrollers.ViewController;

public class ModalCreator {
    public Modal create(ViewController viewController) {
        return new Modal(viewController);
    }
}
