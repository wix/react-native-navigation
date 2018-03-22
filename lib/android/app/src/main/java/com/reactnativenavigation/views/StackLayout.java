package com.reactnativenavigation.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.RestrictTo;
import android.support.v4.view.ViewPager;
import android.widget.RelativeLayout;

import com.reactnativenavigation.interfaces.ChildDisappearListener;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.presentation.OptionsPresenter;
import com.reactnativenavigation.utils.CompatUtils;
import com.reactnativenavigation.viewcontrollers.ReactViewCreator;
import com.reactnativenavigation.viewcontrollers.TopBarButtonController;
import com.reactnativenavigation.views.titlebar.TitleBarReactViewCreator;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

@SuppressLint("ViewConstructor")
public class StackLayout extends RelativeLayout {
    private TopBar topBar;
    private final OptionsPresenter optionsPresenter;

    public StackLayout(Context context, ReactViewCreator topBarButtonCreator, TitleBarReactViewCreator titleBarReactViewCreator, TopBarButtonController.OnClickListener topBarButtonClickListener) {
        super(context);
        createLayout(topBarButtonCreator, titleBarReactViewCreator, topBarButtonClickListener);
        optionsPresenter = new OptionsPresenter(topBar);
        setContentDescription("StackLayout");
    }

    private void createLayout(ReactViewCreator topBarButtonCreator, TitleBarReactViewCreator titleBarReactViewCreator, TopBarButtonController.OnClickListener topBarButtonClickListener) {
        topBar = new TopBar(getContext(), topBarButtonCreator, titleBarReactViewCreator, topBarButtonClickListener, this);
        topBar.setId(CompatUtils.generateViewId());
        addView(topBar, MATCH_PARENT, WRAP_CONTENT);
    }

    public void applyChildOptions(Options options) {
        optionsPresenter.applyOrientation(options.orientationOptions);
    }

    public void applyChildOptions(Options options, Component child) {
        optionsPresenter.applyChildOptions(options, child);
    }

    public void onChildWillDisappear(Options disappearing, Options appearing, ChildDisappearListener childDisappearListener) {
        new OptionsPresenter(topBar).onChildWillDisappear(disappearing, appearing, childDisappearListener);
    }

    public void clearOptions() {
        topBar.clear();
    }

    public void initTopTabs(ViewPager viewPager) {
        topBar.initTopTabs(viewPager);
    }

    public void clearTopTabs() {
        topBar.clearTopTabs();
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public TopBar getTopBar() {
        return topBar;
    }
}
