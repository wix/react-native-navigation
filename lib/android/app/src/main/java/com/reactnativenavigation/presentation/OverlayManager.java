package com.reactnativenavigation.presentation;

import android.view.ViewGroup;

import com.reactnativenavigation.viewcontrollers.ViewController;

public class OverlayManager {
    public void show(ViewGroup root, ViewController overlay) {
        root.addView(overlay.getView());
    }

    public void dismiss(String componentId) {

    }
}
