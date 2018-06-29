package com.reactnativenavigation.viewcontrollers.stack;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.annotation.VisibleForTesting;
import android.support.v4.view.ViewPager;

import com.reactnativenavigation.anim.NavigationAnimator;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.presentation.OptionsPresenter;
import com.reactnativenavigation.presentation.StackOptionsPresenter;
import com.reactnativenavigation.react.Constants;
import com.reactnativenavigation.utils.CommandListener;
import com.reactnativenavigation.utils.CommandListenerAdapter;
import com.reactnativenavigation.viewcontrollers.ChildControllersRegistry;
import com.reactnativenavigation.viewcontrollers.IdStack;
import com.reactnativenavigation.viewcontrollers.ParentController;
import com.reactnativenavigation.viewcontrollers.ReactViewCreator;
import com.reactnativenavigation.viewcontrollers.ViewController;
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
    private BackButtonHelper backButtonHelper;
    private final StackOptionsPresenter presenter;

    public StackController(Activity activity, ChildControllersRegistry childRegistry, ReactViewCreator topBarButtonCreator, TitleBarReactViewCreator titleBarReactViewCreator, TopBarBackgroundViewController topBarBackgroundViewController, TopBarController topBarController, NavigationAnimator animator, String id, Options initialOptions, BackButtonHelper backButtonHelper, StackOptionsPresenter stackPresenter, OptionsPresenter presenter) {
        super(activity, childRegistry, id, presenter, initialOptions);
        this.topBarController = topBarController;
        this.topBarButtonCreator = topBarButtonCreator;
        this.titleBarReactViewCreator = titleBarReactViewCreator;
        this.topBarBackgroundViewController = topBarBackgroundViewController;
        this.animator = animator;
        this.backButtonHelper = backButtonHelper;
        this.presenter = stackPresenter;
    }

    @Override
    public void setDefaultOptions(Options defaultOptions) {
        super.setDefaultOptions(defaultOptions);
        presenter.setDefaultOptions(defaultOptions);
    }

    @Override
    protected ViewController getCurrentChild() {
        return stack.peek();
    }

    @Override
    public void applyChildOptions(Options options, Component child) {
        super.applyChildOptions(options, child);
        presenter.applyChildOptions(options, child);
        if (child instanceof ReactComponent) {
            fabOptionsPresenter.applyOptions(this.options.fabOptions, (ReactComponent) child, getView());
        }
        applyOnParentController(parentController ->
                ((ParentController) parentController).applyChildOptions(
                        this.options.copy()
                                .clearTopBarOptions()
                                .clearAnimationOptions()
                                .clearFabOptions()
                                .clearTopTabOptions()
                                .clearTopTabsOptions(),
                        child
                )
        );
        animator.setOptions(options.animations);
    }

    @Override
    public void mergeChildOptions(Options options, Component child) {
        super.mergeChildOptions(options, child);
        presenter.mergeChildOptions(options, child);
        animator.mergeOptions(options.animations);
        if (options.fabOptions.hasValue() && child instanceof ReactComponent) {
            fabOptionsPresenter.mergeOptions(options.fabOptions, (ReactComponent) child, getView());
        }
        applyOnParentController(parentController ->
                ((ParentController) parentController).mergeChildOptions(
                        options.copy()
                                .clearTopBarOptions()
                                .clearAnimationOptions()
                                .clearFabOptions()
                                .clearTopTabOptions()
                                .clearTopTabsOptions(),
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
    public void clearOptions() {
        super.clearOptions();
        topBarController.clear();
    }

    public void push(ViewController child, CommandListener listener) {
        final ViewController toRemove = stack.peek();
        child.setParentController(this);
        stack.push(child.getId(), child);
        addBackButton(child);
        getView().addView(child.getView(), MATCH_PARENT, MATCH_PARENT);

        if (toRemove != null) {
            if (child.options.animations.push.enable.isTrueOrUndefined()) {
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

    private void addBackButton(ViewController child) {
        backButtonHelper.addToChild(this, child);
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

    public void pop(CommandListener listener) {
        if (!canPop()) {
            listener.onError("Nothing to pop");
            return;
        }

        final ViewController disappearing = stack.pop();
        final ViewController appearing = stack.peek();
        disappearing.onViewWillDisappear();
        appearing.onViewWillAppear();
        getView().addView(appearing.getView(), 0);
        presenter.onChildWillAppear(appearing.options, disappearing.options);
        if (disappearing.options.animations.pop.enable.isTrueOrUndefined()) {
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

    public void popSpecific(ViewController childController, CommandListener listener) {
        if (stack.isTop(childController.getId())) {
            pop(listener);
        } else {
            removeAndDestroyController(childController);
            listener.onSuccess(childController.getId());
        }
    }

    public void popTo(final ViewController viewController, CommandListener listener) {
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

    public void popToRoot(CommandListener listener) {
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

    public ViewController peek() {
        return stack.peek();
    }

    public int size() {
        return stack.size();
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }

    @Override
    public boolean handleBack(CommandListener listener) {
        if (canPop()) {
            pop(listener);
            return true;
        }
        return false;
    }

    @VisibleForTesting()
    public boolean canPop() {
        return stack.size() > 1;
    }

    @NonNull
    @Override
    protected StackLayout createView() {
        StackLayout stackLayout = new StackLayout(getActivity(),
                topBarButtonCreator,
                titleBarReactViewCreator,
                topBarBackgroundViewController,
                topBarController,
                this::onNavigationButtonPressed,
                getId()
        );
        presenter.bindView(topBarController.getView());
        return stackLayout;
    }

    private void onNavigationButtonPressed(String buttonId) {
        if (Constants.BACK_BUTTON_ID.equals(buttonId)) {
            pop(new CommandListenerAdapter());
        } else {
            sendOnNavigationButtonPressed(buttonId);
        }
    }

    @Override
    public void sendOnNavigationButtonPressed(String buttonId) {
        peek().sendOnNavigationButtonPressed(buttonId);
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

    @RestrictTo(RestrictTo.Scope.TESTS)
    public TopBar getTopBar() {
        return topBarController.getView();
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public StackLayout getStackLayout() {
        return getView();
    }
}
