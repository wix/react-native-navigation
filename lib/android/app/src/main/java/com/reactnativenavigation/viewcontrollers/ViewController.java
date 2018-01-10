package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.ViewTreeObserver;

import com.reactnativenavigation.parse.NavigationOptions;
import com.reactnativenavigation.utils.CompatUtils;
import com.reactnativenavigation.utils.StringUtils;
import com.reactnativenavigation.utils.Task;

public abstract class ViewController implements ViewTreeObserver.OnGlobalLayoutListener {

	private final Activity activity;
	private final String id;

	private View view;
	private ParentController parentController;
	private boolean isShown = false;

	public ViewController(Activity activity, String id) {
		this.activity = activity;
		this.id = id;
	}

	protected abstract View createView();

	@VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
	public void ensureViewIsCreated() {
		getView();
	}

	public boolean handleBack() {
		return false;
	}

	public Activity getActivity() {
		return activity;
	}

    protected ViewController getParentController() {
	    return parentController;
    }

	@Nullable
    ParentController getParentStackController() {
		return parentController;
	}

	public void setParentController(final ParentController parentController) {
		this.parentController = parentController;
	}

    boolean performOnParentStack(Task<StackController> task) {
	    if (parentController instanceof StackController) {
            task.run((StackController) parentController);
            return true;
        }
        if (this instanceof StackController) {
	        task.run((StackController) this);
            return true;
        }
        return false;
    }

    void performOnParentStack(Task<StackController> accept, Runnable  reject) {
        if (!performOnParentStack(accept)) {
            reject.run();
        }
    }

	@NonNull
	public View getView() {
		if (view == null) {
			view = createView();
			view.setId(CompatUtils.generateViewId());
			view.getViewTreeObserver().addOnGlobalLayoutListener(this);
		}
		return view;
	}

	public String getId() {
		return id;
	}

	boolean isSameId(final String id) {
		return StringUtils.isEqual(this.id, id);
	}

	@Nullable
	public ViewController findControllerById(String id) {
		return isSameId(id) ? this : null;
	}

	public void onViewAppeared() {
        isShown = true;
    }

	public void onViewDisappear() {
        isShown = false;
    }

    public void applyOptions(NavigationOptions options) {

    }

	public void destroy() {
		if (isShown) {
			isShown = false;
			onViewDisappear();
		}
		if (view != null) {
			view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
			if (view.getParent() instanceof ViewGroup) {
				((ViewManager) view.getParent()).removeView(view);
			}
			view = null;
		}
	}

	@Override
	public void onGlobalLayout() {
		if (!isShown && isViewShown()) {
			isShown = true;
			onViewAppeared();
		} else if (isShown && !isViewShown()) {
			isShown = false;
			onViewDisappear();
		}
	}

	protected boolean isViewShown() {
		return getView().isShown();
	}
}
