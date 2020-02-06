package com.reactnativenavigation.presentation;

import android.view.View;
import android.view.ViewGroup;

import com.reactnativenavigation.utils.CommandListener;
import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.views.BehaviourDelegate;

import java.util.HashMap;

import androidx.annotation.Nullable;

import static com.reactnativenavigation.utils.CollectionUtils.*;
import static com.reactnativenavigation.utils.CoordinatorLayoutUtils.matchParentWithBehaviour;

public class OverlayManager {
    private final HashMap<String, ViewController> overlayRegistry = new HashMap<>();

    public void show(@Nullable ViewGroup contentLayout, ViewGroup overlaysContainer, ViewController overlay, CommandListener listener) {
        if (contentLayout == null) return;
        if (overlaysContainer.getParent() == null) contentLayout.addView(overlaysContainer);
        overlayRegistry.put(overlay.getId(), overlay);
        overlay.addOnAppearedListener(() -> listener.onSuccess(overlay.getId()));
        overlaysContainer.addView(overlay.getView(), matchParentWithBehaviour(new BehaviourDelegate(overlay)));
    }

    public void dismiss(String componentId, CommandListener listener) {
        ViewController overlay = overlayRegistry.get(componentId);
        if (overlay == null) {
            listener.onError("Could not dismiss Overlay. Overlay with id " + componentId + " was not found.");
        } else {
            destroyOverlay(overlay);
            listener.onSuccess(componentId);
        }
    }

    public void destroy() {
        forEach(overlayRegistry.values(), this::destroyOverlay);
    }

    public int size() {
        return overlayRegistry.size();
    }

    public ViewController findControllerById(String id) {
        return overlayRegistry.get(id);
    }

    private void destroyOverlay(ViewController overlay) {
        View parent = (View) overlay.getView().getParent();
        overlay.destroy();
        overlayRegistry.remove(overlay.getId());
        if (isEmpty()) ViewUtils.removeFromParent(parent);
    }

    private boolean isEmpty() {
        return size() == 0;
    }
}
