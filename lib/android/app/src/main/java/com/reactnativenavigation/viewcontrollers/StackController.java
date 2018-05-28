package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.v4.view.ViewPager;

import com.reactnativenavigation.anim.NavigationAnimator;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.params.Button;
import com.reactnativenavigation.presentation.OptionsPresenter;
import com.reactnativenavigation.react.Constants;
import com.reactnativenavigation.utils.CommandListener;
import com.reactnativenavigation.utils.CommandListenerAdapter;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarBackgroundViewController;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarController;
import com.reactnativenavigation.views.Component;
import com.reactnativenavigation.views.ReactComponent;
import com.reactnativenavigation.views.StackLayout;
import com.reactnativenavigation.views.titlebar.TitleBarReactViewCreator;
import com.reactnativenavigation.views.topbar.TopBar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class StackController extends ParentController<StackLayout> {

    private final IdStack<ViewController> stack = new IdStack<>();
    private final NavigationAnimator animator;
    private final ReactViewCreator topBarButtonCreator;
    private final TitleBarReactViewCreator titleBarReactViewCreator;
    private TopBarBackgroundViewController topBarBackgroundViewController;
    private TopBarController topBarController;

    public StackController(Activity activity, ChildControllersRegistry childRegistry, ReactViewCreator topBarButtonCreator, TitleBarReactViewCreator titleBarReactViewCreator, TopBarBackgroundViewController topBarBackgroundViewController, TopBarController topBarController, NavigationAnimator animator, String id, Options initialOptions) {
        super(activity, childRegistry, id, new OptionsPresenter(activity), initialOptions);
        this.topBarController = topBarController;
        this.topBarButtonCreator = topBarButtonCreator;
        this.titleBarReactViewCreator = titleBarReactViewCreator;
        this.topBarBackgroundViewController = topBarBackgroundViewController;
        this.animator = animator;
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
        animator.setOptions(options.animations);
    }

    @Override
    public void mergeChildOptions(Options options, Component child) {
        super.mergeChildOptions(options, child);
        getView().mergeChildOptions(options, child);
        animator.mergeOptions(options.animations);
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
        if (size() <= 1 || child.options.topBar.leftButtons != null) return;
        Options options = new Options();
        Button back = new Button();
        back.id = Constants.BACK_BUTTON_ID;
        options.topBar.leftButtons = new ArrayList<>(Collections.singleton(back));
        child.mergeOptions(options);
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
        getView().onChildWillAppear(appearing, disappearing);

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
    public boolean handleBack(CommandListener listener) {
        if (canPop()) {
            pop(listener);
            return true;
        }
        return false;
    }

    boolean canPop() {
        return stack.size() > 1;
    }

    @NonNull
    @Override
    protected StackLayout createView() {
        return new StackLayout(getActivity(),
                topBarButtonCreator,
                titleBarReactViewCreator,
                topBarBackgroundViewController,
                topBarController,
                this::onNavigationButtonPressed,
                getId()
        );
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
    TopBar getTopBar() {
        return topBarController.getView();
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    StackLayout getStackLayout() {
        return getView();
    }
}
