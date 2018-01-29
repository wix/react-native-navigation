package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.facebook.react.bridge.Promise;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.presentation.NavigationOptionsListener;
import com.reactnativenavigation.presentation.OverlayManager;
import com.reactnativenavigation.utils.CompatUtils;
import com.reactnativenavigation.utils.NoOpPromise;

import java.util.Collection;
import java.util.Collections;

public class Navigator extends ParentController {

    private static final NoOpPromise NO_OP = new NoOpPromise();
    private final ModalStack modalStack = new ModalStack();
	private ViewController root;
    private OverlayManager overlayManager = new OverlayManager();
    private Options defaultOptions = new Options();

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
		return root == null ? Collections.emptyList() : Collections.singletonList(root);
	}

	@Override
	public boolean handleBack() {
		return root != null && root.handleBack();
	}

	@Override
	public void destroy() {
		modalStack.dismissAll(NO_OP);
		super.destroy();
	}

	public void setRoot(final ViewController viewController, Promise promise) {
		if (root != null) {
			root.destroy();
		}

		root = viewController;
		getView().addView(viewController.getView());
        promise.resolve(viewController.getId());
	}

    public void setDefaultOptions(Options defaultOptions) {
        this.defaultOptions = defaultOptions;
    }

    public Options getDefaultOptions() {
        return defaultOptions;
    }

	public void setOptions(final String componentId, Options options) {
		ViewController target = findControllerById(componentId);
		if (target instanceof NavigationOptionsListener) {
			((NavigationOptionsListener) target).mergeOptions(options);
		}
		if (root instanceof NavigationOptionsListener) {
			((NavigationOptionsListener) root).mergeOptions(options);
		}
	}

	public void push(final String fromId, final ViewController viewController, Promise promise) {
		ViewController from = findControllerById(fromId);
		if (from != null) {
		    from.performOnParentStack(stack -> ((StackController) stack).animatePush(viewController, promise));
		}
	}

	void pop(final String fromId, Promise promise) {
		ViewController from = findControllerById(fromId);
		if (from != null) {
		    from.performOnParentStack(stack -> ((StackController) stack).pop(promise));
		}
	}

	public void popSpecific(final String id, Promise promise) {
		ViewController from = findControllerById(id);
		if (from != null) {
		    from.performOnParentStack(stack -> ((StackController) stack).popSpecific(from, promise), () -> rejectPromise(promise));
		} else {
			rejectPromise(promise);
		}
	}

	public void popToRoot(final String id, Promise promise) {
		ViewController from = findControllerById(id);
		if (from != null) {
		    from.performOnParentStack(stack -> ((StackController) stack).popToRoot(promise));
		}
	}

	public void popTo(final String componentId, Promise promise) {
		ViewController target = findControllerById(componentId);
		if (target != null) {
		    target.performOnParentStack(stack -> ((StackController) stack).popTo(target, promise), () -> rejectPromise(promise));
		} else {
			rejectPromise(promise);
		}
	}

	public void showModal(final ViewController viewController, Promise promise) {
		modalStack.showModal(viewController, promise);
	}

	public void dismissModal(final String componentId, Promise promise) {
		modalStack.dismissModal(componentId, promise);
	}

	public void dismissAllModals(Promise promise) {
		modalStack.dismissAll(promise);
	}

	public void showOverlay(ViewController overlay) {
        overlayManager.show(getView(), overlay);
	}

	public void dismissOverlay(final String componentId) {
		overlayManager.dismiss(getView(), componentId);
	}

	static void rejectPromise(Promise promise) {
        promise.reject(new Throwable("Nothing to pop"));
	}

    @Nullable
    @Override
    public ViewController findControllerById(String id) {
        ViewController controllerById = super.findControllerById(id);
        return controllerById != null ? controllerById : modalStack.findControllerById(id);
    }
}
