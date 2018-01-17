package com.reactnativenavigation.presentation;

import android.view.View;
import android.view.ViewGroup;

import com.reactnativenavigation.viewcontrollers.ViewController;

import java.util.HashMap;

public class OverlayManager {
    private final HashMap<String, Integer> overlayRegistry = new HashMap<>();

    public void show(ViewGroup root, ViewController overlay) {
        View view = overlay.getView();
        overlayRegistry.put(overlay.getId(), view.getId());
        root.addView(view);
    }

    public void dismiss(ViewGroup root, String componentId) {
        root.removeView(root.findViewById(overlayRegistry.get(componentId)));
    }
}
