package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.v4.view.ViewPager;

import com.reactnativenavigation.anim.NavigationAnimator;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.utils.CommandListenerAdapter;
import com.reactnativenavigation.viewcontrollers.Navigator.CommandListener;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarBackgroundViewController;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarController;
import com.reactnativenavigation.views.Component;
import com.reactnativenavigation.views.ReactComponent;
import com.reactnativenavigation.views.StackLayout;
import com.reactnativenavigation.views.titlebar.TitleBarReactViewCreator;
import com.reactnativenavigation.views.topbar.TopBar;

import java.util.Collection;
import java.util.Iterator;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class StackController extends ParentController<StackLayout> {

    private final IdStack<ViewController> stack = new IdStack<>();
    private final NavigationAnimator animator;
    private final ReactViewCreator topBarButtonCreator;
    private final TitleBarReactViewCreator titleBarReactViewCreator;
    private TopBarBackgroundViewController topBarBackgroundViewController;
    private TopBarController topBarController;

    public StackController(final Activity activity, ReactViewCreator topBarButtonCreator, TitleBarReactViewCreator titleBarReactViewCreator, TopBarBackgroundViewController topBarBackgroundViewController, TopBarController topBarController, String id, Options initialOptions) {
        super(activity, id, initialOptions);
        this.topBarController = topBarController;
        animator = createAnimator();
        this.topBarButtonCreator = topBarButtonCreator;
        this.titleBarReactViewCreator = titleBarReactViewCreator;
        this.topBarBackgroundViewController = topBarBackgroundViewController;
    }

    public void applyOptions(Options options) {
        super.applyOptions(options);
        getView().applyChildOptions(options);
    }

    @Override
    public void applyChildOptions(Options options, Component child) {
        super.applyChildOptions(options, child);
        getView().applyChildOptions(this.options, child);
        if (child instanceof ReactComponent) {
            fabOptionsPresenter.applyOptions(this.options.fabOptions, (ReactComponent) child, getView());
        }
        applyOnParentController(parentController ->
                ((ParentController) parentController).applyChildOptions(
                        this.options.copy().clearTopBarOptions().clearAnimationOptions().clearFabOptions(),
                        child
                )
        );
        animator.setOptions(options.animationsOptions);
    }

    @Override
    public void mergeChildOptions(Options options, Component child) {
        super.mergeChildOptions(options, child);
        getView().mergeChildOptions(options, child);
        animator.mergeOptions(options.animationsOptions);
        if (options.fabOptions.hasValue() && child instanceof ReactComponent) {
            fabOptionsPresenter.mergeOptions(options.fabOptions, (ReactComponent) child, getView());
        }
        applyOnParentController(parentController ->
                ((ParentController) parentController).mergeChildOptions(
                        options.copy().clearTopBarOptions().clearAnimationOptions().clearFabOptions(),
                        child
                )
        );
    }

    @Override
    public void destroy() {
        topBarController.clear();
        super.destroy();
    }

    @Override
    void clearOptions() {
        super.clearOptions();
        topBarController.clear();
    }

    public void push(ViewController child, CommandListener listener) {
        final ViewController toRemove = stack.peek();
        child.setParentController(this);
        stack.push(child.getId(), child);
        getView().addView(child.getView(), MATCH_PARENT, MATCH_PARENT);

        if (toRemove != null) {
            if (child.options.animated.isTrueOrUndefined()) {
                animator.push(child.getView(), () -> {
                    getView().removeView(toRemove.getView());
                    listener.onSuccess(child.getId());
                });
            } else {
                getView().removeView(toRemove.getView());
                listener.onSuccess(child.getId());
            }
        } else {
            listener.onSuccess(child.getId());
        }
    }

    public void setRoot(ViewController child, CommandListener listener) {
        push(child, new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                removeChildrenBellowTop();
                listener.onSuccess(childId);
            }
        });
    }

    private void removeChildrenBellowTop() {
        Iterator<String> iterator = stack.iterator();
        while (stack.size() > 1) {
            ViewController controller = stack.get(iterator.next());
            if (!stack.isTop(controller.getId())) {
                removeAndDestroyController(controller);
            }
        }
    }

    void pop(CommandListener listener) {
        if (!canPop()) {
            listener.onError("Nothing to pop");
            return;
        }

        final ViewController disappearing = stack.pop();
        final ViewController appearing = stack.peek();
        disappearing.onViewWillDisappear();
        appearing.onViewWillAppear();
        getView().addView(appearing.getView(), 0);
        getView().onChildWillDisappear(disappearing.options, appearing.options);

        if (disappearing.options.animated.isTrueOrUndefined()) {
            animator.pop(disappearing.getView(), () -> finishPopping(disappearing, listener));
        } else {
            finishPopping(disappearing, listener);
        }
    }

    private void finishPopping(ViewController disappearing, CommandListener listener) {
        getView().removeView(disappearing.getView());
        disappearing.destroy();
        listener.onSuccess(disappearing.getId());
    }

    void popSpecific(ViewController childController, CommandListener listener) {
        if (stack.isTop(childController.getId())) {
            pop(listener);
        } else {
            removeAndDestroyController(childController);
            listener.onSuccess(childController.getId());
        }
    }

    void popTo(final ViewController viewController, CommandListener listener) {
        if (!stack.containsId(viewController.getId())) {
            listener.onError("Nothing to pop");
            return;
        }

        Iterator<String> iterator = stack.iterator();
        String currentControlId = iterator.next();
        while (!viewController.getId().equals(currentControlId)) {
            if (stack.isTop(currentControlId)) {
                currentControlId = iterator.next();
                continue;
            }
            removeAndDestroyController(stack.get(currentControlId));
            currentControlId = iterator.next();
        }

        pop(listener);
    }

    void popToRoot(CommandListener listener) {
        if (!canPop()) {
            listener.onError("Nothing to pop");
            return;
        }

        Iterator<String> iterator = stack.iterator();
        while (stack.size() > 2) {
            ViewController controller = stack.get(iterator.next());
            if (!stack.isTop(controller.getId())) {
                removeAndDestroyController(controller);
            }
        }

        pop(listener);
    }

    private void removeAndDestroyController(ViewController controller) {
        stack.remove(controller.getId());
        controller.destroy();
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
            pop(new CommandListenerAdapter());
            return true;
        }
        return false;
    }

    boolean canPop() {
        return stack.size() > 1;
    }

    @Override
    public void sendOnNavigationButtonPressed(String buttonId) {
        peek().sendOnNavigationButtonPressed(buttonId);
    }

    @NonNull
    @Override
    protected StackLayout createView() {
        return new StackLayout(getActivity(), topBarButtonCreator, titleBarReactViewCreator, topBarBackgroundViewController, topBarController, this::sendOnNavigationButtonPressed, getId());
    }

    @NonNull
    @Override
    public Collection<ViewController> getChildControllers() {
        return stack.values();
    }

    @Override
    public void setupTopTabsWithViewPager(ViewPager viewPager) {
        topBarController.initTopTabs(viewPager);
    }

    @Override
    public void clearTopTabs() {
        topBarController.clearTopTabs();
    }

     NavigationAnimator createAnimator() {
        return new NavigationAnimator(getActivity());
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    TopBar getTopBar() {
        return topBarController.getView();
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    StackLayout getStackLayout() {
        return getView();
    }
}
