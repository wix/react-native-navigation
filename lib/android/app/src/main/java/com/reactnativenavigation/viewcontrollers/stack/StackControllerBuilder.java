package com.reactnativenavigation.viewcontrollers.stack;

import android.app.Activity;

import com.reactnativenavigation.anim.StackAnimator;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.presentation.FabPresenter;
import com.reactnativenavigation.presentation.Presenter;
import com.reactnativenavigation.presentation.StackPresenter;
import com.reactnativenavigation.react.events.EventEmitter;
import com.reactnativenavigation.viewcontrollers.ChildControllersRegistry;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarController;
import com.reactnativenavigation.views.element.ElementTransitionManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StackControllerBuilder {
    private Activity activity;
    private ChildControllersRegistry childRegistry;
    private TopBarController topBarController;
    private String id;
    private Options initialOptions = new Options();
    private StackAnimator animator;
    private BackButtonHelper backButtonHelper = new BackButtonHelper();
    private Presenter presenter;
    private StackPresenter stackPresenter;
    private List<ViewController> children = new ArrayList<>();
    private EventEmitter eventEmitter;
    private FabPresenter fabPresenter = new FabPresenter();

    public StackControllerBuilder(Activity activity, EventEmitter eventEmitter) {
        this.activity = activity;
        this.eventEmitter = eventEmitter;
        presenter = new Presenter(activity, new Options());
        animator = new StackAnimator(activity, new ElementTransitionManager());
    }

    public StackControllerBuilder setEventEmitter(EventEmitter eventEmitter) {
        this.eventEmitter = eventEmitter;
        return this;
    }

    public StackControllerBuilder setChildren(ViewController... children) {
        return setChildren(Arrays.asList(children));
    }

    public StackControllerBuilder setChildren(List<ViewController> children) {
        this.children = children;
        return this;
    }

    public StackControllerBuilder setStackPresenter(StackPresenter stackPresenter) {
        this.stackPresenter = stackPresenter;
        return this;
    }

    public StackControllerBuilder setPresenter(Presenter presenter) {
        this.presenter = presenter;
        return this;
    }

    public StackControllerBuilder setChildRegistry(ChildControllersRegistry childRegistry) {
        this.childRegistry = childRegistry;
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

    public StackControllerBuilder setAnimator(StackAnimator animator) {
        this.animator = animator;
        return this;
    }

    StackControllerBuilder setBackButtonHelper(BackButtonHelper backButtonHelper) {
        this.backButtonHelper = backButtonHelper;
        return this;
    }

    public StackControllerBuilder setFabPresenter(FabPresenter fabPresenter) {
        this.fabPresenter = fabPresenter;
        return this;
    }

    public StackController build() {
        return new StackController(activity,
                children,
                childRegistry,
                eventEmitter,
                topBarController,
                animator,
                id,
                initialOptions,
                backButtonHelper,
                stackPresenter,
                presenter,
                fabPresenter
        );
    }
}