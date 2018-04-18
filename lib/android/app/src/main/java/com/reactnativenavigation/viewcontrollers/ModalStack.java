package com.reactnativenavigation.viewcontrollers;

import android.support.annotation.Nullable;

import com.reactnativenavigation.utils.CommandListenerAdapter;
import com.reactnativenavigation.utils.Task;
import com.reactnativenavigation.viewcontrollers.Navigator.CommandListener;
import com.reactnativenavigation.viewcontrollers.modal.Modal;
import com.reactnativenavigation.viewcontrollers.modal.ModalCreator;
import com.reactnativenavigation.viewcontrollers.modal.ModalListener;

import java.util.ArrayList;
import java.util.List;

class ModalStack implements ModalListener {

    private List<Modal> modals = new ArrayList<>();
    private ModalCreator creator;
    private ModalListener modalListener;

    ModalStack(ModalCreator creator, ModalListener modalListener) {
        this.creator = creator;
        this.modalListener = modalListener;
    }

    void showModal(final ViewController viewController, CommandListener listener) {
        Modal modal = creator.create(viewController, this);
        modals.add(modal);
        modal.show();
        listener.onSuccess(viewController.getId());
    }

    void dismissModal(final String componentId, CommandListener listener) {
        applyOnModal(componentId, (modal) -> modal.dismiss(listener), () -> listener.onError("Nothing to dismiss"));
    }

    void dismissAll(CommandListener listener) {
        for (Modal modal : modals) {
            modal.dismiss(size() == 1 ? listener : new CommandListenerAdapter());
        }
        modals.clear();
    }

    boolean isEmpty() {
        return modals.isEmpty();
    }

    public int size() {
        return modals.size();
    }

    @Nullable
    Modal findModalByComponentId(String componentId) {
        for (Modal modal : modals) {
            if (modal.containsDeepComponentId(componentId)) {
                return modal;
            }
        }
        return null;
    }

    @Nullable
    ViewController findControllerById(String id) {
        Modal modal = findModalByComponentId(id);
        return modal != null ? modal.viewController.findControllerById(id) : null;
    }

    @Override
    public void onModalDismiss(Modal modal) {
        if (peek() == modal) {
            modals.remove(modal);
            applyOnModal(peek(), peek -> peek.viewController.onViewAppeared(), null);
        } else {
            modals.remove(modal);
        }
        modalListener.onModalDismiss(modal);
    }

    @Override
    public void onModalDisplay(Modal modal) {
        modalListener.onModalDisplay(modal);
    }

    private Modal peek() {
        return isEmpty() ? null : modals.get(modals.size() - 1);
    }

    public boolean handleBack(CommandListener listener) {
        return !modals.isEmpty() && peek().handleBack(listener);
    }

    private void applyOnModal(String componentId, Task<Modal> accept, Runnable reject) {
        Modal modal = findModalByComponentId(componentId);
        if (modal != null) {
            if (accept != null) accept.run(modal);
        } else {
            if (reject != null) reject.run();
        }
    }

    private void applyOnModal(Modal modal, Task<Modal> accept, Runnable reject) {
        if (modal != null) {
            if (accept != null) accept.run(modal);
        } else {
            if (reject != null) reject.run();
        }
    }
}
