package com.reactnativenavigation.mocks;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.facebook.react.ReactInstanceManager;
import com.reactnativenavigation.interfaces.ScrollEventListener;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.presentation.OptionsPresenter;
import com.reactnativenavigation.react.ReactView;
import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.viewcontrollers.ChildController;
import com.reactnativenavigation.viewcontrollers.ChildControllersRegistry;
import com.reactnativenavigation.views.ReactComponent;
import com.reactnativenavigation.views.topbar.TopBar;

import org.mockito.Mockito;

public class SimpleViewController extends ChildController<SimpleViewController.SimpleView> {


    public SimpleViewController(Activity activity, ChildControllersRegistry childRegistry, String id, Options options) {
        this(activity, childRegistry, id, new OptionsPresenter(activity, new Options()), options);
    }

    public SimpleViewController(Activity activity, ChildControllersRegistry childRegistry, String id, OptionsPresenter presenter, Options options) {
        super(activity, childRegistry, id, presenter, options);
    }

    @Override
    protected SimpleView createView() {
        return new SimpleView(getActivity());
    }

    @Override
    public void sendOnNavigationButtonPressed(String buttonId) {
        getView().sendOnNavigationButtonPressed(buttonId);
    }

    @Override
    public void destroy() {
        if (!isDestroyed()) performOnParentController(parent -> parent.onChildDestroyed(getView()));
        super.destroy();
    }

    @Override
    public String toString() {
        return "SimpleViewController " + getId();
    }

    @Override
    public void mergeOptions(Options options) {
        performOnParentController(parentController -> parentController.mergeChildOptions(options, getView()));
        super.mergeOptions(options);
    }

    public static class SimpleView extends ReactView implements ReactComponent {

        public SimpleView(@NonNull Context context) {
            super(context, Mockito.mock(ReactInstanceManager.class), "compId", "compName");
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
        public ReactView asView() {
            return this;
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
