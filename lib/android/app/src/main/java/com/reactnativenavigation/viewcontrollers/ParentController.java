package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.views.Component;

import java.util.Collection;

public abstract class ParentController<T extends ViewGroup> extends ViewController {

	public ParentController(final Activity activity, final String id, Options initialOptions) {
		super(activity, id, initialOptions);
	}

	@NonNull
	@Override
	public T getView() {
		return (T) super.getView();
	}

	@NonNull
	@Override
	protected abstract T createView();

    @NonNull
	public abstract Collection<? extends ViewController> getChildControllers();

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
    public boolean containsComponent(Component component) {
        if (super.containsComponent(component)) {
            return true;
        }
        for (ViewController child : getChildControllers()) {
            if (child.containsComponent(component)) return true;
        }
        return false;
    }

    @CallSuper
    public void applyOptions(Options options, Component childComponent) {
        mergeChildOptions(options);
    }

    private void mergeChildOptions(Options options) {
        this.options = this.options.mergeWith(options);
    }

	@Override
	public void destroy() {
		super.destroy();
		for (ViewController child : getChildControllers()) {
			child.destroy();
		}
	}

	@CallSuper
    void clearOptions() {
	    applyOnParentController(parent -> ((ParentController) parent).clearOptions());
        options = initialOptions.copy();
    }

    public void setupTopTabsWithViewPager(ViewPager viewPager) {

    }

    public void clearTopTabs() {

    }
}
