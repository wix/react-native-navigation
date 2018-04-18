package com.reactnativenavigation.viewcontrollers.modal;

import android.view.ViewGroup;

import com.reactnativenavigation.utils.CommandListenerAdapter;
import com.reactnativenavigation.viewcontrollers.Navigator.CommandListener;
import com.reactnativenavigation.viewcontrollers.ViewController;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

import javax.annotation.Nullable;

public class ModalStack2 {
    private List<ViewController> modals = new ArrayList<>();
    private ViewGroup root;

    public void setContentLayout(ViewGroup contentLayout) {
        this.root = contentLayout;
    }

    public void showModal(ViewController viewController, CommandListener listener) {
        ViewController toRemove = isEmpty() ? null : peek();
        modals.add(viewController);
        root.addView(viewController.getView());
        if (toRemove != null) root.removeView(toRemove.getView());
        listener.onSuccess(viewController.getId());
    }

    public void dismissModal(String componentId, CommandListener listener) {
        ViewController modal = findModalByComponentId(componentId);
        if (modal != null) {
            if (size() > 1 && peek().equals(modal)) {
                ViewController toAdd = get(size() - 2);
                root.addView(toAdd.getView());
            }

            modals.remove(modal);
            modal.destroy();
            listener.onSuccess(componentId);
        } else {
            listener.onError("Nothing to dismiss");
        }
    }

    public void dismissAll(CommandListener listener) {
        while (!modals.isEmpty()) {
            dismissModal(modals.get(0).getId(), new CommandListenerAdapter());
        }
        listener.onSuccess("");
    }

    public boolean handleBack(CommandListener listener, Runnable onModalWillDismiss) {
        if (isEmpty()) return false;
        if (peek().handleBack(listener)) {
            return true;
        }
        onModalWillDismiss.run();
        dismissModal(peek().getId(), listener);
        return true;
    }

    public ViewController findModalByComponentId(String componentId) {
        for (ViewController modal : modals) {
            if (modal.findControllerById(componentId) != null) {
                return modal;
            }
        }
        return null;
    }


    @Nullable
    public ViewController findControllerById(String componentId) {
        for (ViewController modal : modals) {
            ViewController controllerById = modal.findControllerById(componentId);
            if (controllerById != null) {
                return controllerById;
            }
        }
        return null;
    }

    public ViewController peek() {
        if (modals.isEmpty()) throw new EmptyStackException();
        return modals.get(modals.size() - 1);
    }

    public ViewController get(int index) {
        return modals.get(index);
    }

    public boolean isEmpty() {
        return modals.isEmpty();
    }

    public int size() {
        return modals.size();
    }
}
