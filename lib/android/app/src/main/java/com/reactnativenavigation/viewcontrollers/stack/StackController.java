package com.reactnativenavigation.viewcontrollers.stack;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.reactnativenavigation.anim.StackAnimator;
import com.reactnativenavigation.parse.NestedAnimationsOptions;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.presentation.FabPresenter;
import com.reactnativenavigation.presentation.Presenter;
import com.reactnativenavigation.presentation.StackPresenter;
import com.reactnativenavigation.react.Constants;
import com.reactnativenavigation.react.events.EventEmitter;
import com.reactnativenavigation.utils.CommandListener;
import com.reactnativenavigation.utils.CommandListenerAdapter;
import com.reactnativenavigation.utils.CompatUtils;
import com.reactnativenavigation.viewcontrollers.ChildControllersRegistry;
import com.reactnativenavigation.viewcontrollers.IdStack;
import com.reactnativenavigation.viewcontrollers.ParentController;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarController;
import com.reactnativenavigation.views.Component;
import com.reactnativenavigation.views.Fab;
import com.reactnativenavigation.views.FabMenu;
import com.reactnativenavigation.views.StackLayout;
import com.reactnativenavigation.views.stack.StackBehaviour;
import com.reactnativenavigation.views.topbar.TopBar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;
import androidx.annotation.Size;
import androidx.annotation.VisibleForTesting;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;

import static com.reactnativenavigation.utils.CollectionUtils.*;
import static com.reactnativenavigation.utils.CoordinatorLayoutUtils.matchParentWithBehaviour;
import static com.reactnativenavigation.utils.CoordinatorLayoutUtils.updateBottomMargin;
import static com.reactnativenavigation.utils.ObjectUtils.perform;

public class StackController extends ParentController<StackLayout> {

    private IdStack<ViewController> stack = new IdStack<>();
    private final StackAnimator animator;
    private final EventEmitter eventEmitter;
    private TopBarController topBarController;
    private BackButtonHelper backButtonHelper;
    private final StackPresenter presenter;
    private final FabPresenter fabPresenter;

    public StackController(Activity activity, List<ViewController> children, ChildControllersRegistry childRegistry, EventEmitter eventEmitter, TopBarController topBarController, StackAnimator animator, String id, Options initialOptions, BackButtonHelper backButtonHelper, StackPresenter stackPresenter, Presenter presenter, FabPresenter fabPresenter) {
        super(activity, childRegistry, id, presenter, initialOptions);
        this.eventEmitter = eventEmitter;
        this.topBarController = topBarController;
        this.animator = animator;
        this.backButtonHelper = backButtonHelper;
        this.presenter = stackPresenter;
        this.fabPresenter = fabPresenter;
        stackPresenter.setButtonOnClickListener(this::onNavigationButtonPressed);
        for (ViewController child : children) {
            child.setParentController(this);
            stack.push(child.getId(), child);
            if (size() > 1) backButtonHelper.addToPushedChild(child);
        }
    }

    @Override
    public boolean isRendered() {
        if (isEmpty()) return false;
        if (getCurrentChild().isDestroyed()) return false;
        ViewGroup currentChild = getCurrentChild().getView();
        if (currentChild instanceof Component) {
            return super.isRendered() && presenter.isRendered(currentChild);
        }
        return super.isRendered();
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
    public void onAttachToParent() {
        if (!isEmpty() && !getCurrentChild().isDestroyed() && !isViewShown()) {
            presenter.applyChildOptions(resolveCurrentOptions(), this, getCurrentChild());
        }
    }

    @Override
    public void mergeOptions(Options options) {
        if (isViewShown()) presenter.mergeOptions(options, this, getCurrentChild());
        super.mergeOptions(options);
    }

    @Override
    public void applyChildOptions(Options options, ViewController child) {
        super.applyChildOptions(options, child);
        presenter.applyChildOptions(resolveCurrentOptions(), this, child);
        fabPresenter.applyOptions(this.options.fabOptions, child, getView());
        performOnParentController(parent ->
                parent.applyChildOptions(
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
    public void mergeChildOptions(Options options, ViewController child) {
        super.mergeChildOptions(options, child);
        if (child.isViewShown() && peek() == child) {
            presenter.mergeChildOptions(options, resolveCurrentOptions(), this, child);
            if (options.fabOptions.hasValue()) {
                fabPresenter.mergeOptions(options.fabOptions, child, getView());
            }
        }
        performOnParentController(parent ->
                parent.mergeChildOptions(
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
    public void onChildDestroyed(ViewController child) {
        super.onChildDestroyed(child);
        presenter.onChildDestroyed(child);
    }

    public void push(ViewController child, CommandListener listener) {
        if (findController(child.getId()) != null) {
            listener.onError("A stack can't contain two children with the same id");
            return;
        }
        final ViewController toRemove = stack.peek();
        if (size() > 0) backButtonHelper.addToPushedChild(child);
        child.setParentController(this);
        stack.push(child.getId(), child);
        Options resolvedOptions = resolveCurrentOptions(presenter.getDefaultOptions());
        addChildToStack(child, resolvedOptions);

        if (toRemove != null) {
            NestedAnimationsOptions animation = resolvedOptions.animations.push;
            if (animation.enabled.isTrueOrUndefined()) {
                animator.push(child, toRemove, resolvedOptions, () -> onPushAnimationComplete(child, toRemove, listener));
            } else {
                child.onViewDidAppear();
                getView().removeView(toRemove.getView());
                listener.onSuccess(child.getId());
            }
        } else {
            listener.onSuccess(child.getId());
        }
    }

    private void onPushAnimationComplete(ViewController toAdd, ViewController toRemove, CommandListener listener) {
        toAdd.onViewDidAppear();
        if (!peek().equals(toRemove)) getView().removeView(toRemove.getView());
        listener.onSuccess(toAdd.getId());
    }

    private void addChildToStack(ViewController child, Options resolvedOptions) {
        child.setWaitForRender(resolvedOptions.animations.push.waitForRender);
        if (size() == 1) presenter.applyInitialChildLayoutOptions(resolvedOptions);
        getView().addView(child.getView(), getView().getChildCount() - 1, matchParentWithBehaviour(new StackBehaviour(this)));
    }

    public void setRoot(@Size(min = 1) List<ViewController> children, CommandListener listener) {
        animator.cancelPushAnimations();
        final ViewController toRemove = stack.peek();
        IdStack stackToDestroy = stack;
        stack = new IdStack<>();

        ViewController child = requireLast(children);
        if (children.size() == 1) {
            backButtonHelper.clear(child);
        } else {
            backButtonHelper.addToPushedChild(child);
        }

        child.setParentController(this);
        stack.push(child.getId(), child);
        Options resolvedOptions = resolveCurrentOptions(presenter.getDefaultOptions());
        addChildToStack(child, resolvedOptions);

        CommandListener listenerAdapter = new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                child.onViewDidAppear();
                destroyStack(stackToDestroy);
                if (children.size() > 1) {
                    for (int i = 0; i < children.size() - 1; i++) {
                        stack.set(children.get(i).getId(), children.get(i), i);
                        children.get(i).setParentController(StackController.this);
                        if (i == 0) {
                            backButtonHelper.clear(children.get(i));
                        } else {
                            backButtonHelper.addToPushedChild(children.get(i));
                        }
                    }
                    startChildrenBellowTopChild();
                }
                listener.onSuccess(childId);
            }
        };

        if (toRemove != null && resolvedOptions.animations.setStackRoot.enabled.isTrueOrUndefined()) {
            if (resolvedOptions.animations.setStackRoot.waitForRender.isTrue()) {
                child.getView().setAlpha(0);
                child.addOnAppearedListener(() -> animator.push(
                        child,
                        toRemove,
                        resolvedOptions,
                        () -> listenerAdapter.onSuccess(child.getId())
                    )
                );
            } else {
                animator.push(child, toRemove, resolvedOptions, () -> listenerAdapter.onSuccess(child.getId()));
            }
        } else {
            listenerAdapter.onSuccess(child.getId());
        }
    }

    private void destroyStack(IdStack stack) {
        for (String s : (Iterable<String>) stack) {
            ((ViewController) stack.get(s)).destroy();
        }
    }

    public void pop(Options mergeOptions, CommandListener listener) {
        if (!canPop()) {
            listener.onError("Nothing to pop");
            return;
        }

        peek().mergeOptions(mergeOptions);
        Options disappearingOptions = resolveCurrentOptions(presenter.getDefaultOptions());

        final ViewController disappearing = stack.pop();
        final ViewController appearing = stack.peek();

        disappearing.onViewWillDisappear();

        ViewGroup appearingView = appearing.getView();
        if (appearingView.getLayoutParams() == null) {
            appearingView.setLayoutParams(matchParentWithBehaviour(new StackBehaviour(this)));
        }
        if (appearingView.getParent() == null) {
            getView().addView(appearingView, 0);
        }
        presenter.onChildWillAppear(this, appearing, disappearing);
        if (disappearingOptions.animations.pop.enabled.isTrueOrUndefined()) {
            animator.pop(disappearing.getView(), disappearingOptions.animations.pop, () -> finishPopping(appearing, disappearing, listener));
        } else {
            finishPopping(appearing, disappearing, listener);
        }
    }

    private void finishPopping(ViewController appearing, ViewController disappearing, CommandListener listener) {
        appearing.onViewDidAppear();
        disappearing.destroy();
        listener.onSuccess(disappearing.getId());
        eventEmitter.emitScreenPoppedEvent(disappearing.getId());
    }

    public void popTo(ViewController viewController, Options mergeOptions, CommandListener listener) {
        if (!stack.containsId(viewController.getId()) || peek().equals(viewController)) {
            listener.onError("Nothing to pop");
            return;
        }

        animator.cancelPushAnimations();
        String currentControlId;
        for (int i = stack.size() - 2; i >= 0; i--) {
            currentControlId = stack.get(i).getId();
            if (currentControlId.equals(viewController.getId())) {
                break;
            }

            ViewController controller = stack.get(currentControlId);
            stack.remove(controller.getId());
            controller.destroy();
        }

        pop(mergeOptions, listener);
    }

    public void popToRoot(Options mergeOptions, CommandListener listener) {
        if (!canPop()) {
            listener.onError("Nothing to pop");
            return;
        }

        animator.cancelPushAnimations();
        Iterator<String> iterator = stack.iterator();
        iterator.next();
        while (stack.size() > 2) {
            ViewController controller = stack.get(iterator.next());
            if (!stack.isTop(controller.getId())) {
                stack.remove(iterator, controller.getId());
                controller.destroy();
            }
        }

        pop(mergeOptions, listener);
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
            pop(Options.EMPTY, listener);
            return true;
        }
        return false;
    }

    @VisibleForTesting()
    boolean canPop() {
        return stack.size() > 1;
    }

    @NonNull
    @Override
    protected StackLayout createView() {
        StackLayout stackLayout = new StackLayout(getActivity(), topBarController, getId());
        presenter.bindView(topBarController);
        addInitialChild(stackLayout);
        return stackLayout;
    }

    private void addInitialChild(StackLayout stackLayout) {
        if (isEmpty()) return;
        ViewGroup child = peek().getView();
        child.setId(CompatUtils.generateViewId());
        peek().addOnAppearedListener(this::startChildrenBellowTopChild);
        presenter.applyInitialChildLayoutOptions(resolveCurrentOptions());
        stackLayout.addView(child, 0, matchParentWithBehaviour(new StackBehaviour(this)));
    }

    private void startChildrenBellowTopChild() {
        ArrayList<ViewController> children = new ArrayList(getChildControllers());
        for (int i = children.size() - 2; i >= 0; i--) {
            children.get(i).start();
        }
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

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, ViewGroup child, View dependency) {
        perform(findController(child), controller -> {
            if (dependency instanceof TopBar) presenter.applyTopInsets(this, controller);
            if (dependency instanceof Fab || dependency instanceof FabMenu) updateBottomMargin(dependency, getBottomInset());
        });
        return false;
    }

    @Override
    public int getTopInset(ViewController child) {
        return presenter.getTopInset(resolveChildOptions(child));
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
