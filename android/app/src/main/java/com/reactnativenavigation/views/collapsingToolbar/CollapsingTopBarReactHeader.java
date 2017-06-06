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
import com.reactnativenavigation.screens.Screen;
import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.views.ContentView;

public class CollapsingTopBarReactHeader extends ContentView implements CollapsingTopBarReactHeaderAnimator.OnVisibleListener, CollapsingTopBarReactHeaderAnimator.OnHiddenListener, Screen.OnDisplayListener {
    private ScrollListener listener;
    private Screen.OnDisplayListener onDisplayListener;
    private boolean mIsScrolling;
    private int mTouchSlop;
    private int touchDown = -1;
    private final JSTouchDispatcher mJSTouchDispatcher = new JSTouchDispatcher(this);
    private CollapsingTopBarReactHeaderAnimator visibilityAnimator;
    private CollapsingTopBarReactHeaderAnimator.OnVisibleListener onVisibleListener;
    private CollapsingTopBarReactHeaderAnimator.OnHiddenListener onHiddenListener;

    public void setOnVisibleListener(CollapsingTopBarReactHeaderAnimator.OnVisibleListener onVisibleListener) {
        this.onVisibleListener = onVisibleListener;
    }

    public void setOnHiddenListener(CollapsingTopBarReactHeaderAnimator.OnHiddenListener onHiddenListener) {
        this.onHiddenListener = onHiddenListener;
    }

    public CollapsingTopBarReactHeader(Context context, CollapsingTopBarParams params, NavigationParams navigationParams, ScrollListener scrollListener, Screen.OnDisplayListener onDisplayListener) {
        super(context, params.reactViewId, navigationParams);
        listener = scrollListener;
        this.onDisplayListener = onDisplayListener;
        ViewConfiguration vc = ViewConfiguration.get(context);
        mTouchSlop = vc.getScaledTouchSlop();
        setViewMeasurer(new CollapsingReactHeaderMeasurer(this));
        setOnDisplayListener(this);
    }

    @Override
    public void onDisplay() {
        createVisibilityAnimator();
        onDisplayListener.onDisplay();
    }

    private void createVisibilityAnimator() {
        ViewUtils.runOnPreDraw(this, new Runnable() {
            @Override
            public void run() {
                final CollapsingTopBarReactHeader header = CollapsingTopBarReactHeader.this;
                float height = getHeight();
                visibilityAnimator = new CollapsingTopBarReactHeaderAnimator(header, height * 0.6f, height * 0.60f);
                visibilityAnimator.setOnHiddenListener(header);
                visibilityAnimator.setOnVisibleListener(header);
            }
        });
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
        listener.onTouch(ev);
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

    @Override
    public void onVisible() {
        if (onVisibleListener != null) {
            onVisibleListener.onVisible();
        }
    }

    @Override
    public void onHidden() {
        if (onHiddenListener != null) {
            onHiddenListener.onHidden();
        }
    }
}
