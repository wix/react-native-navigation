package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import java.util.Collection;

public abstract class ParentController extends ViewController {

	public ParentController(final Activity activity, final String id) {
		super(activity, id);
	}

	@NonNull
	@Override
	public ViewGroup getView() {
		return (ViewGroup) super.getView();
	}

	@NonNull
	@Override
	protected abstract ViewGroup createView();

	@NonNull
	public abstract Collection<ViewController> getChildControllers();

	@Nullable
	@Override
	public ViewController findControllerById(final String id) {
		ViewController fromSuper = super.findControllerById(id);
		if (fromSuper != null) return fromSuper;

		for (ViewController child : getChildControllers()) {
			ViewController fromChild = child.findControllerById(id);
			if (fromChild != null) return fromChild;
		}

		return null;
	}

	@Override
	public void destroy() {
		super.destroy();
		for (ViewController child : getChildControllers()) {
			child.destroy();
		}
	}
}
