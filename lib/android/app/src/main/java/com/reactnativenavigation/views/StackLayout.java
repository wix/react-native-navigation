package com.reactnativenavigation.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.RelativeLayout;

import com.reactnativenavigation.viewcontrollers.ReactViewCreator;
import com.reactnativenavigation.viewcontrollers.TopBarButtonController;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarBackgroundViewController;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarController;
import com.reactnativenavigation.views.topbar.TopBar;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

@SuppressLint("ViewConstructor")
public class StackLayout extends RelativeLayout implements Component {
    private String stackId;

    public StackLayout(Context context, ReactViewCreator topBarButtonCreator, TopBarBackgroundViewController topBarBackgroundViewController, TopBarController topBarController, TopBarButtonController.OnClickListener topBarButtonClickListener, String stackId) {
        super(context);
        this.stackId = stackId;
        createLayout(topBarButtonCreator, topBarBackgroundViewController, topBarController, topBarButtonClickListener);
        setContentDescription("StackLayout");
    }

    private void createLayout(ReactViewCreator buttonCreator, TopBarBackgroundViewController topBarBackgroundViewController, TopBarController topBarController, TopBarButtonController.OnClickListener topBarButtonClickListener) {
        addView(topBarController.createView(getContext(), buttonCreator, topBarBackgroundViewController, topBarButtonClickListener, this), MATCH_PARENT, WRAP_CONTENT);
    }

    public String getStackId() {
        return stackId;
    }

    @Override
    public void drawBehindTopBar() {

    }

    @Override
    public void drawBelowTopBar(TopBar topBar) {

    }

    @Override
    public boolean isRendered() {
        return getChildCount() >= 2 &&
               getChildAt(1) instanceof Component &&
               ((Component) getChildAt(1)).isRendered();
    }
}
