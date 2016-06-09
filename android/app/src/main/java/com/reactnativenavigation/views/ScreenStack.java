package com.reactnativenavigation.views;

import android.animation.LayoutTransition;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.reactnativenavigation.activities.BaseReactActivity;
import com.reactnativenavigation.core.RctManager;
import com.reactnativenavigation.core.objects.Screen;
import com.reactnativenavigation.utils.ReflectionUtils;

import java.util.Stack;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class ScreenStack extends FrameLayout {

    private static class ScreenView {
        Screen screen;
        RctView view;

        public ScreenView(Screen screen, RctView view) {
            this.screen = screen;
            this.view = view;
        }
    }

    private final Stack<ScreenView> mStack = new Stack<>();
    private final ReactInstanceManager mReactInstanceManager =
            RctManager.getInstance().getReactInstanceManager();
    private BaseReactActivity mReactActivity;

    public ScreenStack(BaseReactActivity context) {
        super(context);
        init(context);
    }

    public ScreenStack(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mReactActivity = (BaseReactActivity) context;
        setLayoutTransition(new LayoutTransition());
    }

    public void push(Screen screen) {
        push(screen, null);
    }

    public void push(Screen screen, RctView.OnDisplayedListener onDisplayed) {
        RctView oldView = null;
        if (!mStack.isEmpty()) {
            oldView = mStack.peek().view;
        }
        RctView view = new RctView(mReactActivity, mReactInstanceManager, screen, onDisplayed);
        addView(view, MATCH_PARENT, MATCH_PARENT);
        if (oldView != null) {
            ReactRootView reactRootView = oldView.getReactRootView();
            ReflectionUtils.setBooleanField(reactRootView, "mAttachScheduled", true);
            removeView(oldView);
        }
        mStack.push(new ScreenView(screen, view));
    }

    public Screen pop() {
        if (mStack.isEmpty() || getStackSize() == 1) {
            return null;
        }

        ScreenView popped = mStack.pop();
        addView(mStack.peek().view, 0);

        ReflectionUtils.setBooleanField(popped.view.getReactRootView(), "mAttachScheduled", false);
        removeView(popped.view);
        return popped.screen;
    }

    public Screen popToRoot() {
        if (mStack.isEmpty() || getStackSize() <= 1) {
            return null;
        }

        ScreenView oldScreenView = null;
        while (getStackSize() > 1) {
            ScreenView popped = mStack.pop();
            ReflectionUtils.setBooleanField(popped.view.getReactRootView(), "mAttachScheduled", false);
            removeView(popped.view);
            if (oldScreenView == null) {
                oldScreenView = popped;
            }
        }

        if (!mStack.isEmpty()) {
            addView(mStack.peek().view, 0);
        }

        return oldScreenView.screen;
    }

    public Screen resetTo(Screen screen) {
        return resetTo(screen, null);
    }

    public Screen resetTo(Screen screen, RctView.OnDisplayedListener onDisplayed) {
        RctView view = new RctView(mReactActivity, mReactInstanceManager, screen, onDisplayed);
        addView(view, MATCH_PARENT, MATCH_PARENT);

        ScreenView oldScreenView = null;
        if (!mStack.isEmpty()) {
            while (getStackSize() > 0) {
                ScreenView screenView = mStack.pop();
                ReflectionUtils.setBooleanField(screenView.view.getReactRootView(), "mAttachScheduled", false);
                removeView(screenView.view);
                if (oldScreenView == null) {
                    oldScreenView = screenView;
                }
            }
        }

        // Add screen to stack after it's clear
        mStack.push(new ScreenView(screen, view));

        if (oldScreenView == null) {
            return null;
        }

        return oldScreenView.screen;
    }

    public boolean isEmpty() {
        return mStack.isEmpty();
    }

    public int getStackSize() {
        return mStack.size();
    }

    public Screen peek() {
        return mStack.peek().screen;
    }
}
