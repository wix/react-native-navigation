package com.reactnativenavigation.views.collapsingToolbar;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.JSTouchDispatcher;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.uimanager.events.EventDispatcher;
import com.reactnativenavigation.NavigationApplication;
import com.reactnativenavigation.params.CollapsingTopBarParams;
import com.reactnativenavigation.params.NavigationParams;
import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.views.ContentView;

public class CollapsingTopBarReactHeader extends ContentView {
    private ScrollListener listener;
    private boolean mIsScrolling;
    private int mTouchSlop;
    private int touchDown = -1;
    private final JSTouchDispatcher mJSTouchDispatcher = new JSTouchDispatcher(this);
    private CollapsingTopBarReactHeaderAnimator visibilityAnimator;

    public CollapsingTopBarReactHeader(Context context, CollapsingTopBarParams params, NavigationParams navigationParams, ScrollListener scrollListener) {
        super(context, params.reactViewId, navigationParams);
        listener = scrollListener;
        ViewConfiguration vc = ViewConfiguration.get(context);
        mTouchSlop = vc.getScaledTouchSlop();
        visibilityAnimator = createVisibilityAnimator(params.reactViewHeight);
    }

    private CollapsingTopBarReactHeaderAnimator createVisibilityAnimator(int reactViewHeight) {
        float height = ViewUtils.convertDpToPixel(reactViewHeight);
        return new CollapsingTopBarReactHeaderAnimator(this, height / 2, height / 2);
    }

    public void collapse(float amount) {
        visibilityAnimator.collapse(amount);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);
        switch (action) {
            case MotionEvent.ACTION_CANCEL:
                releaseScroll(ev);
                break;
            case MotionEvent.ACTION_UP:
                releaseScroll(ev);
                break;
            case MotionEvent.ACTION_DOWN:
                onTouchDown(ev);
                break;
            case MotionEvent.ACTION_MOVE: {
                if (mIsScrolling) {
                    return true;
                }
                if (calculateDistanceY(ev) > mTouchSlop) {
                    mIsScrolling = true;
                    return true;
                }
                break;
            }
            default:
                break;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        listener.onTouch(ev);
        int action = ev.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_UP:
                releaseScroll(ev);
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void onTouchDown(MotionEvent ev) {
        if (touchDown == -1) {
            touchDown = (int) ev.getRawY();
        }
        dispatchTouchEventToJs(ev);
    }

    private int calculateDistanceY(MotionEvent ev) {
        return (int) Math.abs(ev.getRawY() - touchDown);
    }

    private void releaseScroll(MotionEvent ev) {
        mIsScrolling = false;
        touchDown = -1;
        dispatchTouchEventToJs(ev);
    }

    private void dispatchTouchEventToJs(MotionEvent event) {
        ReactContext reactContext = NavigationApplication.instance.getReactGateway().getReactInstanceManager().getCurrentReactContext();
        EventDispatcher eventDispatcher = reactContext.getNativeModule(UIManagerModule.class).getEventDispatcher();
        mJSTouchDispatcher.handleTouchEvent(event, eventDispatcher);
    }
}
