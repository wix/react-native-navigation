package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.ViewTreeObserver;

import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.utils.CompatUtils;
import com.reactnativenavigation.utils.StringUtils;
import com.reactnativenavigation.utils.Task;

public abstract class ViewController implements ViewTreeObserver.OnGlobalLayoutListener {

    public Options options;

	private final Activity activity;
	private final String id;
	private View view;
	private ParentController parentController;
	private boolean isShown = false;
    private boolean isDestroyed;

	public ViewController(Activity activity, String id) {
		this.activity = activity;
		this.id = id;
	}

	protected abstract View createView();

	@SuppressWarnings("WeakerAccess")
    @VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
	public void ensureViewIsCreated() {
		getView();
	}

	public boolean handleBack() {
		return false;
	}

    public void applyOptions(Options options) {

    }

	public Activity getActivity() {
		return activity;
	}

	protected void applyOnParentStack(Task<ParentController> task) {
        if (parentController != null) {
            task.run(parentController);
        }
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
		    if (isDestroyed) throw new RuntimeException("Tried to create view after it has already been destroyed");
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
            isDestroyed = true;
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
        return !isDestroyed && getView().isShown();
	}
}
