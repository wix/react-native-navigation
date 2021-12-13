package com.reactnativenavigation.views.stack;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.widget.FrameLayout;

import com.reactnativenavigation.utils.UiUtils;
import com.reactnativenavigation.viewcontrollers.stack.topbar.TopBarController;
import com.reactnativenavigation.views.component.Component;
import com.reactnativenavigation.views.component.Renderable;
import com.reactnativenavigation.views.stack.topbar.ScrollDIsabledBehavior;
import com.reactnativenavigation.views.tooltips.TooltipsOverlay;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

@SuppressLint("ViewConstructor")
public class StackLayout extends CoordinatorLayout implements Component {
    private String stackId;
    private TooltipsOverlay overlay;

    public StackLayout(Context context, TopBarController topBarController, String stackId) {
        super(context);
        this.stackId = stackId;
        this.overlay = new TooltipsOverlay(context,"Stack Layout");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            this.overlay.setBackgroundColor(Color.argb(0.5f,0f,1f,0f));
        }
        createLayout(topBarController);
    }

    private void createLayout(TopBarController topBarController) {
        View topBar = topBarController.createView(getContext(), this);
        CoordinatorLayout.LayoutParams lp = new LayoutParams(MATCH_PARENT, UiUtils.getTopBarHeight(getContext()));
        lp.setBehavior(new ScrollDIsabledBehavior());
        addView(topBar, lp);
        addView(overlay,new LayoutParams(MATCH_PARENT, MATCH_PARENT));
    }

    public String getStackId() {
        return stackId;
    }

    @Override
    public boolean isRendered() {
        return getChildCount() >= 2 &&
               getChildAt(1) instanceof Renderable &&
               ((Renderable) getChildAt(1)).isRendered();
    }
}
