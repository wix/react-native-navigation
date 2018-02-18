package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.facebook.react.bridge.Promise;
import com.reactnativenavigation.anim.NavigationAnimator;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.utils.NoOpPromise;
import com.reactnativenavigation.views.ReactComponent;
import com.reactnativenavigation.views.StackLayout;
import com.reactnativenavigation.views.TopBar;

import java.util.Collection;
import java.util.Iterator;

public class StackController extends ParentController <StackLayout> {

    private static final NoOpPromise NO_OP = new NoOpPromise();
    private final IdStack<ViewController> stack = new IdStack<>();
    private final NavigationAnimator animator;
    private StackLayout stackLayout;

    public StackController(final Activity activity, String id, Options initialOptions) {
		super(activity, id, initialOptions);
        animator = new NavigationAnimator(activity);
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    TopBar getTopBar() {
        return stackLayout.getTopBar();
    }

    @Override
    public void applyOptions(Options options, ReactComponent component) {
        super.applyOptions(options, component);
        stackLayout.applyOptions(this.options, component);
        applyOnParentController(parentController ->
                ((ParentController) parentController).applyOptions(this.options.copy().clearTopBarOptions(), component)
        );
    }

    @Override
    void clearOptions() {
        super.clearOptions();
        stackLayout.clearOptions();
    }

    public void push(ViewController child, final Promise promise) {
        final ViewController toRemove = stack.peek();

        child.setParentController(this);
        stack.push(child.getId(), child);
        View enteringView = child.getView();
        getView().addView(enteringView);

        if (toRemove != null) {
            getView().removeView(toRemove.getView());
        }
        promise.resolve(child.getId());
    }

    public void animatePush(final ViewController child, final Promise promise) {
		final ViewController toRemove = stack.peek();

		child.setParentController(this);
		stack.push(child.getId(), child);
		View enteringView = child.getView();
		getView().addView(enteringView);

		if (toRemove != null) {
            animator.animatePush(enteringView, () -> {
                getView().removeView(toRemove.getView());
                promise.resolve(child.getId());
            });
		} else {
			promise.resolve(child.getId());
		}
	}

    void pop(final Promise promise) {
        if (!canPop()) {
            Navigator.rejectPromise(promise);
            return;
        }

        final ViewController poppedTop = stack.pop();
        final ViewController newTop = stack.peek();
        popInternal(poppedTop, newTop);

        finishPopping(poppedTop.getView(), poppedTop, promise);
    }

	private void animatePop(final Promise promise) {
		if (!canPop()) {
			Navigator.rejectPromise(promise);
			return;
		}

		final ViewController poppedTop = stack.pop();
        final ViewController newTop = stack.peek();
        popInternal(poppedTop, newTop);

        animator.animatePop(poppedTop.getView(), () -> finishPopping(poppedTop.getView(), poppedTop, promise));
	}

    private void popInternal(ViewController poppedTop, ViewController newTop) {
        poppedTop.onViewWillDisappear();
        newTop.onViewWillAppear();
        View enteringView = newTop.getView();
        getView().addView(enteringView, getView().getChildCount() - 1);
    }

    boolean canPop() {
        return stack.size() > 1;
    }

	private void finishPopping(View exitingView, ViewController poppedTop, Promise promise) {
		getView().removeView(exitingView);
		poppedTop.destroy();
        promise.resolve(poppedTop.getId());
	}

	void popSpecific(final ViewController childController, Promise promise) {
		if (stack.isTop(childController.getId())) {
			animatePop(promise);
		} else {
			stack.remove(childController.getId());
			childController.destroy();
            promise.resolve(childController.getId());
		}
	}

	void popTo(final ViewController viewController, Promise promise) {
		if (!stack.containsId(viewController.getId())) {
			Navigator.rejectPromise(promise);
			return;
		}

		Iterator<String> iterator = stack.iterator();
		String currentControlId = iterator.next();
		while (!viewController.getId().equals(currentControlId)) {
			String nextControlId = iterator.next();
			boolean animate = nextControlId.equals(viewController.getId());
			if (animate) {
			    animatePop(promise);
            } else {
			    pop(NO_OP);
            }
			currentControlId = nextControlId;
		}
	}

	void popToRoot(Promise promise) {
		while (canPop()) {
			boolean animate = stack.size() == 2; // First element is root
            if (animate) {
                animatePop(promise);
            } else {
                pop(NO_OP);
            }
		}
	}

	ViewController peek() {
		return stack.peek();
	}

	public int size() {
		return stack.size();
	}

	public boolean isEmpty() {
		return stack.isEmpty();
	}

	@Override
	public boolean handleBack() {
		if (canPop()) {
			animatePop(NO_OP);
			return true;
		}
        return false;
	}

    @NonNull
    @Override
    protected StackLayout createView() {
        stackLayout = new StackLayout(getActivity());
        return stackLayout;
    }

	@NonNull
	@Override
	public Collection<ViewController> getChildControllers() {
		return stack.values();
	}

    @Override
    public void setupTopTabsWithViewPager(ViewPager viewPager) {
        stackLayout.initTopTabs(viewPager);
    }

    @Override
    public void clearTopTabs() {
        stackLayout.clearTopTabs();
    }
}
