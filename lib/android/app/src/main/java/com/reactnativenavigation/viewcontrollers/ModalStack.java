package com.reactnativenavigation.viewcontrollers;

import android.app.Dialog;
import android.support.annotation.Nullable;
import android.view.View;

import com.facebook.react.bridge.Promise;
import com.reactnativenavigation.R;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.MeasureSpec.EXACTLY;
import static android.view.View.MeasureSpec.makeMeasureSpec;

public class ModalStack {

	private List<Modal> modals = new ArrayList<>();

	public void showModal(final ViewController viewController, Promise promise) {
		Modal modal = new Modal(viewController);
		modals.add(modal);
		modal.show();
		if (promise != null) {
			promise.resolve(viewController.getId());
		}
	}

	public void dismissModal(final String containerId, Promise promise) {
		Modal modal = findModalByContainerId(containerId);
		if (modal != null) {
			modal.dismiss();
			modals.remove(modal);
			if (promise != null) {
				promise.resolve(containerId);
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
	private Modal findModalByContainerId(String containerId) {
		for (Modal modal : modals) {
			if (modal.containsDeepContainerId(containerId)) {
				return modal;
			}
		}
		return null;
	}

	private static class Modal {
		private final ViewController viewController;
		private final Dialog dialog;

		Modal(final ViewController viewController) {
			this.viewController = viewController;
			dialog = new Dialog(viewController.getActivity(), R.style.Modal);
		}

		void show() {
			preMeasureView();
			dialog.setContentView(viewController.getView());
			dialog.show();
		}

		void dismiss() {
			dialog.dismiss();
		}

		boolean containsDeepContainerId(String containerId) {
			return viewController.findControllerById(containerId) != null;
		}

		private void preMeasureView() {
			View decorView = viewController.getActivity().getWindow().getDecorView();
			viewController.getView().measure(makeMeasureSpec(decorView.getMeasuredWidth(), EXACTLY), makeMeasureSpec(decorView.getMeasuredHeight(), EXACTLY));
		}
	}
}
