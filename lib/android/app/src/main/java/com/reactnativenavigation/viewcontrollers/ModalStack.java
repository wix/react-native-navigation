package com.reactnativenavigation.viewcontrollers;

import android.support.annotation.Nullable;

import com.facebook.react.bridge.Promise;
import com.reactnativenavigation.viewcontrollers.modal.Modal;
import com.reactnativenavigation.viewcontrollers.modal.ModalCreator;

import java.util.ArrayList;
import java.util.List;

public class ModalStack {

	private List<Modal> modals = new ArrayList<>();
    private ModalCreator creator;

    public ModalStack(ModalCreator creator) {
        this.creator = creator;
    }

    public void showModal(final ViewController viewController, Promise promise) {
        Modal modal = creator.create(viewController);
        modals.add(modal);
		modal.show();
		if (promise != null) {
			promise.resolve(viewController.getId());
		}
	}

	public void dismissModal(final String componentId, Promise promise) {
		Modal modal = findModalByComponentId(componentId);
		if (modal != null) {
			modal.dismiss();
			modals.remove(modal);
			if (promise != null) {
				promise.resolve(componentId);
			}
		} else {
			Navigator.rejectPromise(promise);
		}
	}

	public void dismissAll(Promise promise) {
		for (Modal modal : modals) {
			modal.dismiss();
		}
		modals.clear();
		if (promise != null) {
			promise.resolve(true);
		}
	}

	@Nullable
	public Modal findModalByComponentId(String componentId) {
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
}
