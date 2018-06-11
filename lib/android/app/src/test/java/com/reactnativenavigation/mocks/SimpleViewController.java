package com.reactnativenavigation.mocks;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.reactnativenavigation.interfaces.ScrollEventListener;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.presentation.OptionsPresenter;
import com.reactnativenavigation.viewcontrollers.ChildController;
import com.reactnativenavigation.viewcontrollers.ChildControllersRegistry;
import com.reactnativenavigation.views.ReactComponent;
import com.reactnativenavigation.views.topbar.TopBar;

public class SimpleViewController extends ChildController<SimpleViewController.SimpleView> {

    private SimpleView simpleView;

    public SimpleViewController(Activity activity, ChildControllersRegistry childRegistry, String id, Options options) {
        this(activity, childRegistry, id, new OptionsPresenter(activity), options);
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

        }

        @Override
        public void drawBelowTopBar(TopBar topBar) {

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
