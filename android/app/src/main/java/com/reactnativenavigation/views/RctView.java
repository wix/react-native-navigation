package com.reactnativenavigation.views;

import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.reactnativenavigation.activities.BaseReactActivity;
import com.reactnativenavigation.core.objects.Screen;
import com.reactnativenavigation.utils.BridgeUtils;
import com.reactnativenavigation.utils.ReflectionUtils;

/**
 * Created by guyc on 10/03/16.
 */
public class RctView extends FrameLayout {

    private ReactRootView mReactRootView;

    /**
     * Interface used to run some code when the {@link ReactRootView} is visible.
     */
    public interface OnDisplayedListener {
        /**
         * This method will be invoked when the {@link ReactRootView} is visible.
         */
        void onDisplayed();
    }

    @SuppressWarnings("unchecked")
    public RctView(BaseReactActivity ctx, ReactInstanceManager rctInstanceManager, Screen screen,
                   final OnDisplayedListener onDisplayedListener) {
        super(ctx);
        setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        mReactRootView = new ReactRootView(ctx);
        mReactRootView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        String componentName = screen.screenId;
        Bundle passProps = new Bundle();
        passProps.putString(Screen.KEY_SCREEN_INSTANCE_ID, screen.screenInstanceId);
        passProps.putString(Screen.KEY_NAVIGATOR_ID, screen.navigatorId);
        passProps.putString(Screen.KEY_NAVIGATOR_EVENT_ID, screen.navigatorEventId);
        if (screen.passedProps != null) {
            BridgeUtils.addMapToBundle(screen.passedProps, passProps);
        }

        mReactRootView.startReactApplication(rctInstanceManager, componentName, passProps);

        if (onDisplayedListener != null) {
            mReactRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    onDisplayedListener.onDisplayed();
                    mReactRootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        }

        addView(mReactRootView);
    }

    /**
     * Must be called before view is removed from screen, but will be added again later. Setting mAttachScheduled
     * to true will prevent the component from getting unmounted once view is detached from screen.
     */
    public void onTemporallyRemovedFromScreen() {
        // Hack in order to prevent the react view from getting unmounted
        ReflectionUtils.setBooleanField(mReactRootView, "mAttachScheduled", true);
    }

    /**
     * Must be called before view is removed from screen inorder to ensure onDetachedFromScreen is properly
     * executed and componentWillUnmount is called
     */
    public void onRemoveFromScreen() {
        ReflectionUtils.setBooleanField(mReactRootView, "mAttachScheduled", false);
    }

    /**
     * Must be called when view is added again to screen inorder to ensure onDetachedFromScreen is properly
     * executed and componentWillUnmount is called
     */
    public void onReAddToScreen() {
        ReflectionUtils.setBooleanField(mReactRootView, "mAttachScheduled", false);
    }
}

