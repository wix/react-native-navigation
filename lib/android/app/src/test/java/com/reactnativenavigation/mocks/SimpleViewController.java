package com.reactnativenavigation.mocks;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.reactnativenavigation.interfaces.ScrollEventListener;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.presentation.OptionsPresenter;
import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.viewcontrollers.ChildController;
import com.reactnativenavigation.viewcontrollers.ChildControllersRegistry;
import com.reactnativenavigation.views.ReactComponent;
import com.reactnativenavigation.views.topbar.TopBar;

public class SimpleViewController extends ChildController<SimpleViewController.SimpleView> {

    private SimpleView simpleView;

    public SimpleViewController(Activity activity, ChildControllersRegistry childRegistry, String id, Options options) {
        this(activity, childRegistry, id, new OptionsPresenter(activity, new Options()), options);
    }

    public SimpleViewController(Activity activity, ChildControllersRegistry childRegistry, String id, OptionsPresenter presenter, Options options) {
        super(activity, childRegistry, id, presenter, options);
    }

    @Override
    protected SimpleView createView() {
        simpleView = new SimpleView(getActivity());
        return simpleView;
    }

    @Override
    public void sendOnNavigationButtonPressed(String buttonId) {
        simpleView.sendOnNavigationButtonPressed(buttonId);
    }

    @Override
    public String toString() {
        return "SimpleViewController " + getId();
    }

    @Override
    public void mergeOptions(Options options) {
        applyOnParentController(parentController -> parentController.mergeChildOptions(options, getView()));
        super.mergeOptions(options);
    }

    public static class SimpleView extends FrameLayout implements ReactComponent {

        public SimpleView(@NonNull Context context) {
            super(context);
        }

        @Override
        public void drawBehindTopBar() {
            if (getLayoutParams() instanceof RelativeLayout.LayoutParams) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
                if (layoutParams.topMargin == 0) return;
                layoutParams.topMargin = 0;
                setLayoutParams(layoutParams);
            }
        }

        @Override
        public void drawBelowTopBar(TopBar topBar) {
            if (getLayoutParams() instanceof RelativeLayout.LayoutParams) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
                if (layoutParams.topMargin == ViewUtils.getPreferredHeight(topBar)) return;
                layoutParams.topMargin = ViewUtils.getPreferredHeight(topBar);
//                setLayoutParams(layoutParams);
            }
        }

        @Override
        public boolean isRendered() {
            return getChildCount() >= 1;
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public View asView() {
            return null;
        }

        @Override
        public void destroy() {

        }

        @Override
        public void sendComponentStart() {

        }

        @Override
        public void sendComponentStop() {

        }

        @Override
        public void sendOnNavigationButtonPressed(String buttonId) {

        }

        @Override
        public ScrollEventListener getScrollEventListener() {
            return null;
        }

        @Override
        public void dispatchTouchEventToJs(MotionEvent event) {

        }
    }
}
