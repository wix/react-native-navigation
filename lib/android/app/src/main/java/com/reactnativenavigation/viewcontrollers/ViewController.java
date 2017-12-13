package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.ViewTreeObserver;

import com.reactnativenavigation.utils.CompatUtils;
import com.reactnativenavigation.utils.StringUtils;

public abstract class ViewController implements ViewTreeObserver.OnGlobalLayoutListener {

	private final Activity activity;
	private final String id;

	private View view;
	private StackController parentStackController;
	private boolean isShown = false;

	public ViewController(Activity activity, String id) {
		this.activity = activity;
		this.id = id;
	}

	protected abstract View createView();

	void ensureViewIsCreated() {
		getView();
	}

	public boolean handleBack() {
		return false;
	}

	public Activity getActivity() {
		return activity;
	}

	@Nullable
    StackController getParentStackController() {
		return parentStackController;
	}

	void setParentStackController(final StackController parentStackController) {
		this.parentStackController = parentStackController;
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
		//
	}

	public void onViewDisappear() {
		//
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
