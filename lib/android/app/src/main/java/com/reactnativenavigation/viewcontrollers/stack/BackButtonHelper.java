package com.reactnativenavigation.viewcontrollers.stack;

import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.viewcontrollers.ViewController;

public class BackButtonHelper {
    public void addToPushedChild(ViewController child) {
        if (child.options.topBar.buttons.left != null || child.options.topBar.buttons.back.visible.isFalse()) return;
        Options options = new Options();
        options.topBar.buttons.back.setVisible();
        child.mergeOptions(options);
    }

    public void clear(ViewController child) {
        if (!child.options.topBar.buttons.back.hasValue()) {
            child.options.topBar.buttons.back.visible = new Bool(false);
        }
    }
}
