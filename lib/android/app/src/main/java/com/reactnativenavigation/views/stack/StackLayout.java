package com.reactnativenavigation.views.stack;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;

import com.reactnativenavigation.utils.UiUtils;
import com.reactnativenavigation.viewcontrollers.stack.topbar.TopBarController;
import com.reactnativenavigation.views.component.Component;
import com.reactnativenavigation.views.component.Renderable;
import com.reactnativenavigation.views.stack.topbar.ScrollDIsabledBehavior;
import com.reactnativenavigation.views.overlay.AttachedOverlayContainer;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

@SuppressLint("ViewConstructor")
public class StackLayout extends CoordinatorLayout implements Component {
    private String stackId;
    final private AttachedOverlayContainer attachedOverlayContainer ;

    public StackLayout(Context context, TopBarController topBarController, String stackId) {
        super(context);
        this.stackId = stackId;
        attachedOverlayContainer =  new AttachedOverlayContainer(getContext());
        createLayout(topBarController);
    }

    public AttachedOverlayContainer getAttachedOverlayContainer() {
        return attachedOverlayContainer;
    }

    private void createLayout(TopBarController topBarController) {
        View topBar = topBarController.createView(getContext(), this);

        CoordinatorLayout.LayoutParams lp = new LayoutParams(MATCH_PARENT, UiUtils.getTopBarHeight(getContext()));
        lp.setBehavior(new ScrollDIsabledBehavior());
        addView(topBar, lp);
        addView(attachedOverlayContainer,LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);

    }

    public String getStackId() {
        return stackId;
    }

    @Override
    public boolean isRendered() {
        return getChildCount() >= 3 &&
                getChildAt(2) instanceof Renderable &&
                ((Renderable) getChildAt(2)).isRendered();
    }
}