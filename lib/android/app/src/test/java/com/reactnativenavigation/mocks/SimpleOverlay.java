package com.reactnativenavigation.mocks;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.reactnativenavigation.interfaces.ScrollEventListener;
import com.reactnativenavigation.viewcontrollers.IReactView;
import com.reactnativenavigation.views.element.Element;

import java.util.Collections;
import java.util.List;

public class SimpleOverlay extends RelativeLayout implements IReactView {
    public SimpleOverlay(Context context) {
        super(context);
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public View asView() {
        FrameLayout root = new FrameLayout(getContext());
        FrameLayout overlay = new FrameLayout(getContext());
        root.addView(overlay);
        return root;
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

    @Override
    public boolean isRendered() {
        return getChildCount() >= 1;
    }

    @Override
    public List<Element> getElements() {
        return Collections.EMPTY_LIST;
    }
}
