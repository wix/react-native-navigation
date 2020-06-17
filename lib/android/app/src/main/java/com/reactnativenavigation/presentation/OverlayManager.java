package com.reactnativenavigation.presentation;

import android.view.View;
import android.view.ViewGroup;

import com.reactnativenavigation.utils.CommandListener;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.views.BehaviourDelegate;

import java.util.HashMap;

import static com.reactnativenavigation.utils.CollectionUtils.*;
import static com.reactnativenavigation.utils.CoordinatorLayoutUtils.matchParentWithBehaviour;

public class OverlayManager {
    private final HashMap<String, ViewController> overlayRegistry = new HashMap<>();

    public void show(ViewGroup overlaysContainer, ViewController overlay, CommandListener listener) {
        overlaysContainer.setVisibility(View.VISIBLE);
        overlayRegistry.put(overlay.getId(), overlay);
        overlay.addOnAppearedListener(() -> {
            overlay.onViewDidAppear();
            listener.onSuccess(overlay.getId());
        });
        overlaysContainer.addView(overlay.getView(), matchParentWithBehaviour(new BehaviourDelegate(overlay)));
    }

    public void dismiss(ViewGroup overlaysContainer, String componentId, CommandListener listener) {
        ViewController overlay = overlayRegistry.get(componentId);
        if (overlay == null) {
            listener.onError("Could not dismiss Overlay. Overlay with id " + componentId + " was not found.");
        } else {
            destroyOverlay(overlaysContainer, overlay);
            listener.onSuccess(componentId);
        }
    }

    public void destroy(ViewGroup overlaysContainer) {
        forEach(overlayRegistry.values(), overlay -> destroyOverlay(overlaysContainer, overlay));
    }

    public int size() {
        return overlayRegistry.size();
    }

    public ViewController findControllerById(String id) {
        return overlayRegistry.get(id);
    }

    private void destroyOverlay(ViewGroup overlaysContainer, ViewController overlay) {
        overlay.destroy();
        overlayRegistry.remove(overlay.getId());
        if (isEmpty()) overlaysContainer.setVisibility(View.GONE);
    }

    private boolean isEmpty() {
        return size() == 0;
    }
}
