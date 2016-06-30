package com.reactnativenavigation.views;

import android.animation.LayoutTransition;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.facebook.react.ReactInstanceManager;
import com.reactnativenavigation.activities.BaseReactActivity;
import com.reactnativenavigation.core.RctManager;
import com.reactnativenavigation.core.objects.Screen;

import java.util.Stack;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class ScreenStack extends FrameLayout {

    private static final int DISAPPEAR_ANIMATION_DELAY = 200;

    private static class ScreenView {
        Screen screen;
        RctView view;

        public ScreenView(Screen screen, RctView view) {
            this.screen = screen;
            this.view = view;
        }
    }

    private final Stack<ScreenView> mStack = new Stack<>();
    private final ReactInstanceManager mReactInstanceManager = RctManager.getInstance().getReactInstanceManager();
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
        RctView oldView = mStack.isEmpty() ? null : mStack.peek().view;
        RctView view = new RctView(mReactActivity, mReactInstanceManager, screen, onDisplayed);
        if (oldView != null) {
            addView(view, MATCH_PARENT, MATCH_PARENT);

            oldView.onTemporallyRemovedFromScreen();
            getLayoutTransition().setStartDelay(LayoutTransition.DISAPPEARING, DISAPPEAR_ANIMATION_DELAY);
            removeView(oldView);
            getLayoutTransition().setStartDelay(LayoutTransition.DISAPPEARING, 0);
        } else {
            addView(view, MATCH_PARENT, MATCH_PARENT);
        }
        mStack.push(new ScreenView(screen, view));
    }

    public Screen pop() {
        if (mStack.isEmpty() || getStackSize() == 1) {
            return null;
        }

        ScreenView popped = mStack.pop();

        RctView newView = mStack.peek().view;
        addView(newView);
        newView.onReAddToScreen();

        popped.view.onRemoveFromScreen();
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
            popped.view.onRemoveFromScreen();
            removeView(popped.view);
            if (oldScreenView == null) {
                oldScreenView = popped;
            }
        }

        if (!mStack.isEmpty()) {
            addView(mStack.peek().view, 0);
        }

        return oldScreenView != null ? oldScreenView.screen : null;
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
                ScreenView popped = mStack.pop();
                popped.view.onRemoveFromScreen();
                removeView(popped.view);
                if (oldScreenView == null) {
                    oldScreenView = popped;
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

    /**
     * Remove the ScreenStack from {@code parent} while preventing all child react views from getting unmounted
     */
    public void removeFromScreen(ViewGroup parent) {
        mStack.peek().view.onTemporallyRemovedFromScreen();

        parent.removeView(this);
    }

    /**
     * Add ScreenStack to {@code parent}
     */
    public void addToScreen(ViewGroup parent) {
        mStack.peek().view.onReAddToScreen();

        parent.addView(this, new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
    }
}
