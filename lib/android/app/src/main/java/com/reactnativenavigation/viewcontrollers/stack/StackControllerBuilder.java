package com.reactnativenavigation.viewcontrollers.stack;

import android.app.Activity;

import com.reactnativenavigation.anim.NavigationAnimator;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.presentation.OptionsPresenter;
import com.reactnativenavigation.presentation.StackOptionsPresenter;
import com.reactnativenavigation.viewcontrollers.ChildControllersRegistry;
import com.reactnativenavigation.viewcontrollers.ReactViewCreator;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarBackgroundViewController;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarController;
import com.reactnativenavigation.views.element.ElementTransitionManager;

import java.util.ArrayList;
import java.util.List;

public class StackControllerBuilder {
    private Activity activity;
    private ChildControllersRegistry childRegistry;
    private ReactViewCreator topBarButtonCreator;
    private TopBarBackgroundViewController topBarBackgroundViewController;
    private TopBarController topBarController;
    private String id;
    private Options initialOptions = new Options();
    private NavigationAnimator animator;
    private BackButtonHelper backButtonHelper = new BackButtonHelper();
    private OptionsPresenter presenter;
    private StackOptionsPresenter stackPresenter;
    private List<ViewController> children = new ArrayList<>();

    public StackControllerBuilder(Activity activity) {
        this.activity = activity;
        presenter = new OptionsPresenter(activity, new Options());
        animator = new NavigationAnimator(activity, new ElementTransitionManager());
    }

    public StackControllerBuilder setChildren(List<ViewController> children) {
        this.children = children;
        return this;
    }

    public StackControllerBuilder setStackPresenter(StackOptionsPresenter stackPresenter) {
        this.stackPresenter = stackPresenter;
        return this;
    }

    public StackControllerBuilder setPresenter(OptionsPresenter presenter) {
        this.presenter = presenter;
        return this;
    }

    public StackControllerBuilder setChildRegistry(ChildControllersRegistry childRegistry) {
        this.childRegistry = childRegistry;
        return this;
    }

    public StackControllerBuilder setTopBarButtonCreator(ReactViewCreator topBarButtonCreator) {
        this.topBarButtonCreator = topBarButtonCreator;
        return this;
    }

    public StackControllerBuilder setTopBarBackgroundViewController(TopBarBackgroundViewController topBarBackgroundViewController) {
        this.topBarBackgroundViewController = topBarBackgroundViewController;
        return this;
    }

    public StackControllerBuilder setTopBarController(TopBarController topBarController) {
        this.topBarController = topBarController;
        return this;
    }

    public StackControllerBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public StackControllerBuilder setInitialOptions(Options initialOptions) {
        this.initialOptions = initialOptions;
        return this;
    }

    public StackControllerBuilder setAnimator(NavigationAnimator animator) {
        this.animator = animator;
        return this;
    }

    public StackControllerBuilder setBackButtonHelper(BackButtonHelper backButtonHelper) {
        this.backButtonHelper = backButtonHelper;
        return this;
    }

    public StackController build() {
        return new StackController(activity,
                children,
                childRegistry,
                topBarButtonCreator,
                topBarBackgroundViewController,
                topBarController,
                animator,
                id,
                initialOptions,
                backButtonHelper,
                stackPresenter,
                presenter
        );
    }
}