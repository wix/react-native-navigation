package com.reactnativenavigation.viewcontrollers.modal;

import android.view.ViewGroup;

import com.reactnativenavigation.utils.CommandListener;
import com.reactnativenavigation.viewcontrollers.ViewController;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

import javax.annotation.Nullable;

public class ModalStack {
    private List<ViewController> modals = new ArrayList<>();
    private final ModalPresenter presenter;

    public ModalStack(ModalPresenter presenter) {
        this.presenter = presenter;
    }

    public void setContentLayout(ViewGroup contentLayout) {
        presenter.setContentLayout(contentLayout);
    }

    public void showModal(ViewController viewController, ViewController root, CommandListener listener) {
        ViewController toRemove = isEmpty() ? root : peek();
        modals.add(viewController);
        presenter.showModal(viewController, toRemove, listener);
    }

    public void dismissModal(String componentId, ViewController root, CommandListener listener) {
        ViewController toDismiss = findModalByComponentId(componentId);
        if (toDismiss != null) {
            ViewController toAdd = isTop(toDismiss) ? get(size() - 2) : root;
            modals.remove(toDismiss);
            presenter.dismissModal(toDismiss, toAdd, listener);
        } else {
            listener.onError("Nothing to dismiss");
        }
    }

    public void dismissAllModals(CommandListener listener, ViewController root) {
        if (modals.isEmpty()) {
            listener.onError("Nothing to dismiss");
            return;
        }

        while (!modals.isEmpty()) {
            if (modals.size() == 1) {
                dismissModal(modals.get(0).getId(), root, listener);
            } else {
                modals.get(0).destroy();
                modals.remove(0);
            }
        }
    }

    public boolean handleBack(CommandListener listener, ViewController root) {
        if (isEmpty()) return false;
        if (peek().handleBack(listener)) {
            return true;
        }
        dismissModal(peek().getId(), root, listener);
        return true;
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

    private boolean isTop(ViewController modal) {
        return size() > 1 && peek().equals(modal);
    }

    @Nullable
    private ViewController findModalByComponentId(String componentId) {
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
}
