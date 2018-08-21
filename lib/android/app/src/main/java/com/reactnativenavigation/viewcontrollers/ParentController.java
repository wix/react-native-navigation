package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.support.annotation.CallSuper;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.presentation.OptionsPresenter;
import com.reactnativenavigation.utils.CollectionUtils;
import com.reactnativenavigation.views.Component;

import java.util.Collection;

public abstract class ParentController<T extends ViewGroup> extends ChildController {

	public ParentController(Activity activity, ChildControllersRegistry childRegistry, String id, OptionsPresenter presenter, Options initialOptions) {
		super(activity, childRegistry, id, presenter, initialOptions);
	}

    @Override
    public void setWaitForRender(Bool waitForRender) {
        super.setWaitForRender(waitForRender);
        applyOnController(getCurrentChild(), controller -> ((ViewController) controller).setWaitForRender(waitForRender));
    }

    @Override
    public void setDefaultOptions(Options defaultOptions) {
	    super.setDefaultOptions(defaultOptions);
        Collection<? extends ViewController> children = getChildControllers();
        if (!CollectionUtils.isNullOrEmpty(children)) {
            for (ViewController child : children) {
                child.setDefaultOptions(defaultOptions);
            }
        }
    }

    @Override
    @CheckResult
    public Options resolveCurrentOptions() {
	    if (CollectionUtils.isNullOrEmpty(getChildControllers())) return options;
        return getCurrentChild()
                .resolveCurrentOptions()
                .copy()
                .mergeWith(options);
    }

    @Override
    @CheckResult
    public Options resolveCurrentOptions(Options defaultOptions) {
        return resolveCurrentOptions().withDefaultOptions(defaultOptions);
    }

    protected abstract ViewController getCurrentChild();

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
    public void applyChildOptions(Options options, Component child) {
        this.options = this.options.mergeWith(options);
        if (isRoot()) {
            presenter.applyRootOptions(getView(), options);
        }
    }

    @CallSuper
    public void mergeChildOptions(Options options, Component child) {

    }

	@Override
	public void destroy() {
		super.destroy();
		for (ViewController child : getChildControllers()) {
			child.destroy();
		}
	}

	@CallSuper
    protected void clearOptions() {
	    performOnParentController(parent -> ((ParentController) parent).clearOptions());
        options = initialOptions.copy().clearOneTimeOptions();
    }

    public void setupTopTabsWithViewPager(ViewPager viewPager) {

    }

    public void clearTopTabs() {

    }

    @Override
    public boolean isRendered() {
        return getCurrentChild() != null && getCurrentChild().isRendered();
    }

    public void onChildDestroyed(Component child) {

    }
}
