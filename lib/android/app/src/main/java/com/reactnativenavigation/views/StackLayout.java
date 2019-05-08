package com.reactnativenavigation.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.RelativeLayout;

import com.reactnativenavigation.utils.UiUtils;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarController;
import com.reactnativenavigation.views.topbar.TopBar;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

@SuppressLint("ViewConstructor")
public class StackLayout extends RelativeLayout implements Component {
    private String stackId;

    public StackLayout(Context context, TopBarController topBarController, String stackId) {
        super(context);
        this.stackId = stackId;
        createLayout(topBarController);
        setContentDescription("StackLayout");
    }

    private void createLayout(TopBarController topBarController) {
        addView(topBarController.createView(getContext(), this),
                MATCH_PARENT,
                UiUtils.getTopBarHeight(getContext())
        );
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
