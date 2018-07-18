package com.reactnativenavigation.presentation;

import android.view.ViewGroup;

import com.reactnativenavigation.utils.CommandListener;
import com.reactnativenavigation.viewcontrollers.ViewController;

import java.util.HashMap;

public class OverlayManager {
    private final HashMap<String, ViewController> overlayRegistry = new HashMap<>();

    public void show(ViewGroup root, ViewController overlay, CommandListener listener) {
        overlayRegistry.put(overlay.getId(), overlay);
        overlay.setOnAppearedListener(() -> listener.onSuccess(overlay.getId()));
        root.addView(overlay.getView());
    }

    public void dismiss(String componentId, CommandListener listener) {
        ViewController overlay = overlayRegistry.get(componentId);
        if (overlay == null) {
            listener.onError("Could not dismiss Overlay. Overlay with id " + componentId + " was not found.");
        } else {
            overlay.destroy();
            overlayRegistry.remove(componentId);
            listener.onSuccess(componentId);
        }
    }

    public void destroy() {
        for (ViewController view : overlayRegistry.values()) {
            view.destroy();
        }
        overlayRegistry.clear();
    }

    public int size() {
        return overlayRegistry.size();
    }
}
