package com.reactnativenavigation.presentation;

import android.view.ViewGroup;

import com.reactnativenavigation.utils.CommandListener;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.views.BehaviourDelegate;

import java.util.HashMap;

import static com.reactnativenavigation.utils.CoordinatorLayoutUtils.matchParentWithBehaviour;

public class OverlayManager {
    private final HashMap<String, ViewController> overlayRegistry = new HashMap<>();

    public void show(ViewGroup overlaysContainer, ViewController overlay, CommandListener listener) {
        overlayRegistry.put(overlay.getId(), overlay);
        overlay.addOnAppearedListener(() -> listener.onSuccess(overlay.getId()));
        overlaysContainer.addView(overlay.getView(), matchParentWithBehaviour(new BehaviourDelegate(overlay)));
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

    public ViewController findControllerById(String id) {
        return overlayRegistry.get(id);
    }
}
