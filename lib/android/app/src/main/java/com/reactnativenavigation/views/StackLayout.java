package com.reactnativenavigation.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.RelativeLayout;

import com.reactnativenavigation.interfaces.ChildDisappearListener;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.presentation.OptionsPresenter;
import com.reactnativenavigation.viewcontrollers.ReactViewCreator;
import com.reactnativenavigation.viewcontrollers.TopBarButtonController;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarController;
import com.reactnativenavigation.views.titlebar.TitleBarReactViewCreator;
import com.reactnativenavigation.views.topbar.TopBarBackgroundViewCreator;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

@SuppressLint("ViewConstructor")
public class StackLayout extends RelativeLayout {
    private String stackId;
    private final OptionsPresenter optionsPresenter;

    public StackLayout(Context context, ReactViewCreator topBarButtonCreator, TitleBarReactViewCreator titleBarReactViewCreator, TopBarBackgroundViewCreator topBarBackgroundViewCreator, TopBarController topBarController, TopBarButtonController.OnClickListener topBarButtonClickListener, String stackId) {
        super(context);
        this.stackId = stackId;
        createLayout(topBarButtonCreator, titleBarReactViewCreator, topBarBackgroundViewCreator, topBarController, topBarButtonClickListener);
        optionsPresenter = new OptionsPresenter(topBarController.getTopBar());
        setContentDescription("StackLayout");
    }

    private void createLayout(ReactViewCreator buttonCreator, TitleBarReactViewCreator titleBarReactViewCreator, TopBarBackgroundViewCreator BackgroundViewCreator, TopBarController topBarController, TopBarButtonController.OnClickListener topBarButtonClickListener) {
        addView(topBarController.createTopBar(getContext(), buttonCreator, titleBarReactViewCreator, BackgroundViewCreator, topBarButtonClickListener, this), MATCH_PARENT, WRAP_CONTENT);
    }

    public void applyChildOptions(Options options) {
        optionsPresenter.applyOrientation(options.orientationOptions);
    }

    public void applyChildOptions(Options options, Component child) {
        optionsPresenter.applyChildOptions(options, child);
    }

    public void onChildWillDisappear(Options disappearing, Options appearing, ChildDisappearListener childDisappearListener) {
        optionsPresenter.onChildWillDisappear(disappearing, appearing, childDisappearListener);
    }

    public void mergeChildOptions(Options options, Component child) {
        optionsPresenter.mergeChildOptions(options, child);
    }

    public String getStackId() {
        return stackId;
    }
}
