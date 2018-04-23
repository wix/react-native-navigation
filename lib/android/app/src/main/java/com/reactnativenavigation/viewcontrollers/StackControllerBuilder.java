package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;

import com.reactnativenavigation.anim.NavigationAnimator;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarBackgroundViewController;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarController;
import com.reactnativenavigation.views.titlebar.TitleBarReactViewCreator;

public class StackControllerBuilder {
    private Activity activity;
    private ReactViewCreator topBarButtonCreator;
    private TitleBarReactViewCreator titleBarReactViewCreator;
    private TopBarBackgroundViewController topBarBackgroundViewController;
    private TopBarController topBarController;
    private String id;
    private Options initialOptions = new Options();
    private NavigationAnimator animator;

    public StackControllerBuilder(Activity activity) {
        this.activity = activity;
        animator = new NavigationAnimator(activity);
    }

    public StackControllerBuilder setTopBarButtonCreator(ReactViewCreator topBarButtonCreator) {
        this.topBarButtonCreator = topBarButtonCreator;
        return this;
    }

    public StackControllerBuilder setTitleBarReactViewCreator(TitleBarReactViewCreator titleBarReactViewCreator) {
        this.titleBarReactViewCreator = titleBarReactViewCreator;
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

    public StackController createStackController() {
        return new StackController(activity, topBarButtonCreator, titleBarReactViewCreator, topBarBackgroundViewController, topBarController, animator, id, initialOptions);
    }
}