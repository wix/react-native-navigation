package com.reactnativenavigation.mocks;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.reactnativenavigation.interfaces.ScrollEventListener;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.views.ReactComponent;
import com.reactnativenavigation.views.TopBar;

public class SimpleViewController extends ViewController<FrameLayout> {

    public SimpleViewController(final Activity activity, String id) {
        super(activity, id);
        options = new Options();
    }

    @Override
    protected FrameLayout createView() {
        return new SimpleView(getActivity());
    }

    @Override
    public String toString() {
        return "SimpleViewController " + getId();
    }

    public class SimpleView extends FrameLayout implements ReactComponent {

        public SimpleView(@NonNull Context context) {
            super(context);
        }

        @Override
        public void applyOptions(Options options) {

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
