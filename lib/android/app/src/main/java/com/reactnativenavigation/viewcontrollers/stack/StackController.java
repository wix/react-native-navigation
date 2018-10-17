package com.reactnativenavigation.viewcontrollers.stack;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.annotation.VisibleForTesting;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.reactnativenavigation.anim.NavigationAnimator;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.presentation.OptionsPresenter;
import com.reactnativenavigation.presentation.StackPresenter;
import com.reactnativenavigation.react.Constants;
import com.reactnativenavigation.utils.CommandListener;
import com.reactnativenavigation.utils.CommandListenerAdapter;
import com.reactnativenavigation.viewcontrollers.ChildControllersRegistry;
import com.reactnativenavigation.viewcontrollers.IdStack;
import com.reactnativenavigation.viewcontrollers.ParentController;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarBackgroundViewController;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarController;
import com.reactnativenavigation.views.Component;
import com.reactnativenavigation.views.ReactComponent;
import com.reactnativenavigation.views.StackLayout;
import com.reactnativenavigation.views.topbar.TopBar;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class StackController extends ParentController<StackLayout> {

    private final IdStack<ViewController> stack = new IdStack<>();
    private final NavigationAnimator animator;
    private TopBarBackgroundViewController topBarBackgroundViewController;
    private TopBarController topBarController;
    private BackButtonHelper backButtonHelper;
    private final StackPresenter presenter;

    public StackController(Activity activity, List<ViewController> children, ChildControllersRegistry childRegistry, TopBarBackgroundViewController topBarBackgroundViewController, TopBarController topBarController, NavigationAnimator animator, String id, Options initialOptions, BackButtonHelper backButtonHelper, StackPresenter stackPresenter, OptionsPresenter presenter) {
        super(activity, childRegistry, id, presenter, initialOptions);
        this.topBarController = topBarController;
        this.topBarBackgroundViewController = topBarBackgroundViewController;
        this.animator = animator;
        this.backButtonHelper = backButtonHelper;
        this.presenter = stackPresenter;
        stackPresenter.setButtonOnClickListener(this::onNavigationButtonPressed);
        for (ViewController child : children) {
            stack.push(child.getId(), child);
            child.setParentController(this);
            if (size() > 1) backButtonHelper.addToPushedChild(child);
        }
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
    public void mergeOptions(Options options) {
        presenter.mergeOptions(options, (Component) getCurrentChild().getView());
        super.mergeOptions(options);
    }

    @Override
    public void applyChildOptions(Options options, Component child) {
        super.applyChildOptions(options, child);
        presenter.applyChildOptions(resolveCurrentOptions(), child);
        if (child instanceof ReactComponent) {
            fabOptionsPresenter.applyOptions(this.options.fabOptions, (ReactComponent) child, getView());
        }
        performOnParentController(parentController ->
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
    }

    @Override
    public void mergeChildOptions(Options options, ViewController childController, Component child) {
        super.mergeChildOptions(options, childController, child);
        if (childController.isViewShown()) {
            presenter.mergeChildOptions(options, resolveCurrentOptions(), child);
            if (options.fabOptions.hasValue() && child instanceof ReactComponent) {
                fabOptionsPresenter.mergeOptions(options.fabOptions, (ReactComponent) child, getView());
            }
        }
        performOnParentController(parentController ->
                ((ParentController) parentController).mergeChildOptions(
                        options.copy()
                                .clearTopBarOptions()
                                .clearAnimationOptions()
                                .clearFabOptions()
                                .clearTopTabOptions()
                                .clearTopTabsOptions(),
                        childController,
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

    @Override
    public void onChildDestroyed(Component child) {
        super.onChildDestroyed(child);
        presenter.onChildDestroyed(child);
    }

    public void push(ViewController child, CommandListener listener) {
        final ViewController toRemove = stack.peek();
        if (size() > 0) backButtonHelper.addToPushedChild(child);
        child.setParentController(this);
        stack.push(child.getId(), child);
        Options resolvedOptions = resolveCurrentOptions(presenter.getDefaultOptions());
        addChildToStack(child, child.getView(), resolvedOptions);

        if (toRemove != null) {
            if (resolvedOptions.animations.push.enable.isTrueOrUndefined()) {
                if (resolvedOptions.animations.push.waitForRender.isTrue()) {
                    child.getView().setAlpha(0);
                    child.setOnAppearedListener(() -> animator.push(child.getView(), resolvedOptions.animations.push, resolvedOptions.transitions, toRemove.getElements(), child.getElements(), () -> {
                        getView().removeView(toRemove.getView());
                        listener.onSuccess(child.getId());
                    }));
                } else {
                    animator.push(child.getView(), resolvedOptions.animations.push, () -> {
                        if (!toRemove.equals(peek())) {
                            getView().removeView(toRemove.getView());
                        }
                        listener.onSuccess(child.getId());
                    });
                }
            } else {
                getView().removeView(toRemove.getView());
                listener.onSuccess(child.getId());
            }
        } else {
            listener.onSuccess(child.getId());
        }
    }

    private void addChildToStack(ViewController child, View view, Options resolvedOptions) {
        view.setLayoutParams(new RelativeLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        child.setWaitForRender(resolvedOptions.animations.push.waitForRender);
        presenter.applyLayoutParamsOptions(resolvedOptions, view);
        if (size() == 1) presenter.applyInitialChildLayoutOptions(resolvedOptions);
        getView().addView(view, getView().getChildCount() - 1);
    }

    public void setRoot(ViewController child, CommandListener listener) {
        backButtonHelper.clear(child);
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

    public void pop(Options mergeOptions, CommandListener listener) {
        if (!canPop()) {
            listener.onError("Nothing to pop");
            return;
        }

        final ViewController disappearing = stack.pop();
        final ViewController appearing = stack.peek();
        disappearing.mergeOptions(mergeOptions);
        disappearing.onViewWillDisappear();
        appearing.onViewWillAppear();
        Options resolvedOptions = resolveCurrentOptions();
        ViewGroup appearingView = appearing.getView();
        if (appearingView.getLayoutParams() == null) {
            appearingView.setLayoutParams(new RelativeLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
            presenter.applyLayoutParamsOptions(resolvedOptions, appearingView);
        }
        if (appearingView.getParent() == null) {
            getView().addView(appearingView, 0);
        }
        presenter.onChildWillAppear(appearing.options, disappearing.options);
        if (disappearing.options.animations.pop.enable.isTrueOrUndefined()) {
            animator.pop(disappearing.getView(), resolvedOptions.animations.pop, () -> finishPopping(disappearing, listener));
        } else {
            finishPopping(disappearing, listener);
        }
    }

    private void finishPopping(ViewController disappearing, CommandListener listener) {
        disappearing.destroy();
        listener.onSuccess(disappearing.getId());
    }

    public void popTo(ViewController viewController, Options mergeOptions, CommandListener listener) {
        if (!stack.containsId(viewController.getId()) || peek().equals(viewController)) {
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

        pop(mergeOptions, listener);
    }

    public void popToRoot(Options mergeOptions, CommandListener listener) {
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

        pop(mergeOptions, listener);
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
            pop(Options.EMPTY, listener);
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
                topBarBackgroundViewController,
                topBarController,
                getId()
        );
        presenter.bindView(topBarController.getView());
        addInitialChild(stackLayout);
        return stackLayout;
    }

    private void addInitialChild(StackLayout stackLayout) {
        if (isEmpty()) return;
        ViewGroup child = peek().getView();
        child.setLayoutParams(new RelativeLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        Options options = resolveCurrentOptions();
        presenter.applyLayoutParamsOptions(options, child);
        presenter.applyInitialChildLayoutOptions(options);
        stackLayout.addView(child, 0);
    }

    private void onNavigationButtonPressed(String buttonId) {
        if (Constants.BACK_BUTTON_ID.equals(buttonId)) {
            pop(Options.EMPTY, new CommandListenerAdapter());
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
