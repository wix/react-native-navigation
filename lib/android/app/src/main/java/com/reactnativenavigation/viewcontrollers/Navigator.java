package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.facebook.react.bridge.Promise;
import com.reactnativenavigation.parse.NavigationOptions;
import com.reactnativenavigation.parse.OverlayOptions;
import com.reactnativenavigation.presentation.NavigationOptionsListener;
import com.reactnativenavigation.presentation.OverlayPresenter;
import com.reactnativenavigation.utils.CompatUtils;

import org.json.JSONObject;

import java.util.Collection;
import java.util.Collections;

public class Navigator extends ParentController {

	private final ModalStack modalStack = new ModalStack();
	private ViewController root;
	private OverlayPresenter overlayPresenter;

	public Navigator(final Activity activity) {
		super(activity, "navigator" + CompatUtils.generateViewId());
	}

	@NonNull
	@Override
	protected ViewGroup createView() {
		return new FrameLayout(getActivity());
	}

	@NonNull
	@Override
	public Collection<ViewController> getChildControllers() {
		return root == null ? Collections.<ViewController>emptyList() : Collections.singletonList(root);
	}

	@Override
	public boolean handleBack() {
		return root != null && root.handleBack();
	}

	@Override
	public void destroy() {
		modalStack.dismissAll(null);
		super.destroy();
	}

	/*
	 * Navigation methods
	 */

	public void setRoot(final ViewController viewController) {
		setRoot(viewController, null);
	}

	public void setRoot(final ViewController viewController, Promise promise) {
		if (root != null) {
			root.destroy();
		}

		root = viewController;
		getView().addView(viewController.getView());
		if (promise != null) {
			promise.resolve(viewController.getId());
		}
	}

	public void setOptions(final String containerId, NavigationOptions options) {
		ViewController target = findControllerById(containerId);
		if (target instanceof NavigationOptionsListener) {
			((NavigationOptionsListener) target).mergeNavigationOptions(options);
		}
		if (root instanceof NavigationOptionsListener) {
			((NavigationOptionsListener) root).mergeNavigationOptions(options);
		}
	}

	public void push(final String fromId, final ViewController viewController) {
		push(fromId, viewController, null);
	}

	public void push(final String fromId, final ViewController viewController, Promise promise) {
		ViewController from = findControllerById(fromId);
		if (from != null) {
			StackController parentStackController = from.getParentStackController();
			if (parentStackController != null) {
				parentStackController.push(viewController, promise);
			}
		}
	}

	public void pop(final String fromId) {
		pop(fromId, null);
	}

	public void pop(final String fromId, Promise promise) {
		ViewController from = findControllerById(fromId);
		if (from != null) {
			StackController parentStackController = from.getParentStackController();
			if (parentStackController != null) {
				parentStackController.pop(promise);
			}
		}
	}

	public void popSpecific(final String id) {
		popSpecific(id, null);
	}

	public void popSpecific(final String id, Promise promise) {
		ViewController from = findControllerById(id);
		if (from != null) {
			StackController parentStackController = from.getParentStackController();
			if (parentStackController != null) {
				parentStackController.popSpecific(from, promise);
			} else {
				rejectPromise(promise);
			}
		} else {
			rejectPromise(promise);
		}
	}

	public void popToRoot(final String id) {
		popToRoot(id, null);
	}

	public void popToRoot(final String id, Promise promise) {
		ViewController from = findControllerById(id);
		if (from != null) {
			StackController parentStackController = from.getParentStackController();
			if (parentStackController != null) {
				parentStackController.popToRoot(promise);
			}
		}
	}

	public void popTo(final String containerId) {
		popTo(containerId, null);
	}

	public void popTo(final String containerId, Promise promise) {
		ViewController target = findControllerById(containerId);
		if (target != null) {
			StackController parentStackController = target.getParentStackController();
			if (parentStackController != null) {
				parentStackController.popTo(target, promise);
			} else {
				rejectPromise(promise);
			}
		} else {
			rejectPromise(promise);
		}
	}

	public void showModal(final ViewController viewController, Promise promise) {
		modalStack.showModal(viewController, promise);
	}

	public void dismissModal(final String containerId, Promise promise) {
		modalStack.dismissModal(containerId, promise);
	}

	public void dismissAllModals(Promise promise) {
		modalStack.dismissAll(promise);
	}

	public void showOverlay(String type, OverlayOptions options, Promise promise) {
		overlayPresenter = new OverlayPresenter(root, type, options);
		overlayPresenter.show();
		promise.resolve(true);
	}

	public void dismissOverlay() {
		overlayPresenter.dismiss();
	}

	static void rejectPromise(Promise promise) {
		if (promise != null) {
			promise.reject(new Throwable("Nothing to pop"));
		}
	}
}
