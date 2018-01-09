package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.facebook.react.bridge.Promise;
import com.reactnativenavigation.anim.NavigationAnimator;

import java.util.Collection;
import java.util.Iterator;

public class StackController extends ParentController {

	private final IdStack<ViewController> stack = new IdStack<>();
	private final NavigationAnimator animator;

	public StackController(final Activity activity, String id) {
		this(activity, id, new NavigationAnimator(activity));
	}

	public StackController(final Activity activity, String id, NavigationAnimator animator) {
		super(activity, id);
		this.animator = animator;
	}

	public void push(final ViewController child) {
		push(child, null);
	}

	public void push(final ViewController child, final Promise promise) {
		final ViewController previousTop = peek();

		child.setParentController(this);
		stack.push(child.getId(), child);
		View enteringView = child.getView();
		getView().addView(enteringView);

		//TODO animatePush only when needed
		if (previousTop != null) {
			animator.animatePush(enteringView, new NavigationAnimator.NavigationAnimationListener() {
				@Override
				public void onAnimationEnd() {
					getView().removeView(previousTop.getView());
					if (promise != null) {
						promise.resolve(child.getId());
					}
				}
			});
		} else if (promise != null) {
			promise.resolve(child.getId());
		}
	}

	boolean canPop() {
		return stack.size() > 1;
	}

	void pop(Promise promise) {
		pop(true, promise);
	}

	void pop() {
		pop(true, null);
	}

	private void pop(boolean animate, final Promise promise) {
		if (!canPop()) {
			Navigator.rejectPromise(promise);
			return;
		}

		final ViewController poppedTop = stack.pop();
		ViewController newTop = peek();

		View enteringView = newTop.getView();
		final View exitingView = poppedTop.getView();
		getView().addView(enteringView, getView().getChildCount() - 1);

		if (animate) {
			animator.animatePop(exitingView, new NavigationAnimator.NavigationAnimationListener() {
				@Override
				public void onAnimationEnd() {
					finishPopping(exitingView, poppedTop, promise);
				}
			});
		} else {
			finishPopping(exitingView, poppedTop, promise);
		}
	}

	private void finishPopping(View exitingView, ViewController poppedTop, Promise promise) {
		getView().removeView(exitingView);
		poppedTop.destroy();
		if (promise != null) {
			promise.resolve(poppedTop.getId());
		}
	}

	void popSpecific(final ViewController childController) {
		popSpecific(childController, null);
	}

	void popSpecific(final ViewController childController, Promise promise) {
		if (stack.isTop(childController.getId())) {
			pop(promise);
		} else {
			stack.remove(childController.getId());
			childController.destroy();
			if (promise != null) {
				promise.resolve(childController.getId());
			}
		}
	}

	void popTo(ViewController viewController) {
		popTo(viewController, null);
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
			pop(animate, animate ? promise : null);
			currentControlId = nextControlId;
		}
	}

	void popToRoot() {
		popToRoot(null);
	}

	void popToRoot(Promise promise) {
		while (canPop()) {
			boolean animate = stack.size() == 2; //first element is root
			pop(animate, animate ? promise : null);
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
			pop(null);
			return true;
		} else {
			return false;
		}
	}

	@NonNull
	@Override
	protected ViewGroup createView() {
		return new FrameLayout(getActivity());
	}

	@NonNull
	@Override
	public Collection<ViewController> getChildControllers() {
		return stack.values();
	}
}
