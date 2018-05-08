package com.reactnativenavigation.presentation;

import android.view.ViewGroup;

import com.reactnativenavigation.utils.CommandListener;
import com.reactnativenavigation.viewcontrollers.ViewController;

import java.util.HashMap;

public class OverlayManager {
    private final HashMap<String, ViewController> overlayRegistry = new HashMap<>();

    public void show(ViewGroup root, ViewController overlay, CommandListener listener) {
        overlayRegistry.put(overlay.getId(), overlay);
        root.addView(overlay.getView());
        listener.onSuccess(overlay.getId());
    }

    public void dismiss(ViewGroup root, String componentId, CommandListener listener) {
        ViewGroup overlay = overlayRegistry.get(componentId).getView();
        if (overlay.getParent() == root) {
            root.removeView(overlay);
            listener.onSuccess(componentId);
        } else {
            listener.onError("Overlay is not attached");
        }
    }

    public void destroy() {
        for (ViewController view : overlayRegistry.values()) {
            view.destroy();
        }
        overlayRegistry.clear();
    }
}
