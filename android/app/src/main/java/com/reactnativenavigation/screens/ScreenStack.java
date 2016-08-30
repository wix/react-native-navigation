package com.reactnativenavigation.screens;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.reactnativenavigation.NavigationApplication;
import com.reactnativenavigation.params.ScreenParams;
import com.reactnativenavigation.params.StyleParams;
import com.reactnativenavigation.params.TitleBarButtonParams;
import com.reactnativenavigation.params.TitleBarLeftButtonParams;
import com.reactnativenavigation.utils.KeyboardVisibilityDetector;
import com.reactnativenavigation.utils.Task;
import com.reactnativenavigation.views.LeftButtonOnClickListener;

import java.util.List;
import java.util.Stack;

public class ScreenStack {
    private static final String TAG = "ScreenStack";

    public interface OnScreenPop {
        void onScreenPopAnimationEnd();
    }

    private final AppCompatActivity activity;
    private RelativeLayout parent;
    private LeftButtonOnClickListener leftButtonOnClickListener;
    private Stack<Screen> stack = new Stack<>();
    private final KeyboardVisibilityDetector keyboardVisibilityDetector;
    private boolean isStackVisible = false;
    private final String navigatorId;

    public String getNavigatorId() {
        return navigatorId;
    }

    public ScreenStack(AppCompatActivity activity,
                       RelativeLayout parent,
                       String navigatorId,
                       LeftButtonOnClickListener leftButtonOnClickListener) {
        this.activity = activity;
        this.parent = parent;
        this.navigatorId = navigatorId;
        this.leftButtonOnClickListener = leftButtonOnClickListener;
        keyboardVisibilityDetector = new KeyboardVisibilityDetector(parent);
    }

    public void pushInitialScreenWithAnimation(final ScreenParams initialScreenParams, RelativeLayout.LayoutParams params) {
        pushInitialScreen(initialScreenParams, params);
        final Screen screen = stack.peek();
        screen.setOnDisplayListener(new Screen.OnDisplayListener() {
            @Override
            public void onDisplay() {
                screen.show(initialScreenParams.animateScreenTransitions);
                screen.setStyle();
            }
        });
    }

    public void pushInitialScreen(ScreenParams initialScreenParams, RelativeLayout.LayoutParams params) {
        Screen initialScreen = ScreenFactory.create(activity, initialScreenParams, leftButtonOnClickListener);
        initialScreen.setVisibility(View.INVISIBLE);
        addScreen(initialScreen, params);
    }

    public void push(final ScreenParams params, RelativeLayout.LayoutParams layoutParams) {
        Screen nextScreen = ScreenFactory.create(activity, params, leftButtonOnClickListener);
        final Screen previousScreen = stack.peek();
        if (isStackVisible) {
            pushScreenToVisibleStack(params, layoutParams, nextScreen, previousScreen);
        } else {
            pushScreenToInvisibleStack(layoutParams, nextScreen, previousScreen);
        }
    }

    private void pushScreenToVisibleStack(final ScreenParams params, RelativeLayout.LayoutParams layoutParams,
                                          final Screen nextScreen, final Screen previousScreen) {
        nextScreen.setVisibility(View.INVISIBLE);
        addScreen(nextScreen, layoutParams);
        nextScreen.setOnDisplayListener(new Screen.OnDisplayListener() {
            @Override
            public void onDisplay() {
                nextScreen.show(params.animateScreenTransitions, new Runnable() {
                    @Override
                    public void run() {
                        removePreviousWithoutUnmount(previousScreen);
                    }
                });
            }
        });
    }

    private void pushScreenToInvisibleStack(RelativeLayout.LayoutParams layoutParams, Screen nextScreen,
                                            Screen previousScreen) {
        nextScreen.setVisibility(View.INVISIBLE);
        addScreen(nextScreen, layoutParams);
        removePreviousWithoutUnmount(previousScreen);
    }

    private void addScreen(Screen screen, RelativeLayout.LayoutParams layoutParams) {
        parent.addView(screen, layoutParams);
        stack.push(screen);
    }

    private void removePreviousWithoutUnmount(Screen previous) {
        previous.preventUnmountOnDetachedFromWindow();
        parent.removeView(previous);
    }

    public void pop(boolean animated) {
        pop(animated, null);
    }

    public void pop(final boolean animated, @Nullable final OnScreenPop onScreenPop) {
        if (!canPop()) {
            return;
        }

        final Screen toRemove = stack.pop();
        final Screen previous = stack.peek();

        if (keyboardVisibilityDetector.isKeyboardVisible()) {
            keyboardVisibilityDetector.setKeyboardCloseListener(new Runnable() {
                @Override
                public void run() {
                    keyboardVisibilityDetector.setKeyboardCloseListener(null);
                    swapScreens(animated, toRemove, previous, onScreenPop);
                }
            });
            keyboardVisibilityDetector.closeKeyboard();
        } else {
            swapScreens(animated, toRemove, previous, onScreenPop);
        }
    }

    private void swapScreens(boolean animated, final Screen toRemove, Screen previous, OnScreenPop onScreenPop) {
        readdPrevious(previous);
        previous.setStyle();
        toRemove.hide(animated, new Runnable() {
            @Override
            public void run() {
                toRemove.ensureUnmountOnDetachedFromWindow();
                parent.removeView(toRemove);
            }
        });

        if (onScreenPop != null) {
            onScreenPop.onScreenPopAnimationEnd();
        }
    }

    public Screen peek() {
        return stack.peek();
    }

    private void readdPrevious(Screen previous) {
        previous.setVisibility(View.VISIBLE);
        parent.addView(previous, 0);
        previous.preventMountAfterReattachedToWindow();
    }

    public void popToRoot(boolean animated) {
        while (canPop()) {
            pop(animated);
        }
    }

    public void destroy() {
        for (Screen screen : stack) {
            screen.ensureUnmountOnDetachedFromWindow();
            parent.removeView(screen);
        }
        stack.clear();
    }

    public int getStackSize() {
        return stack.size();
    }

    public boolean canPop() {
        return getStackSize() > 1 && !isPreviousScreenAttachedToWindow();
    }

    private boolean isPreviousScreenAttachedToWindow() {
        Screen previousScreen = stack.get(stack.size() - 2);
        if (previousScreen.getParent() != null) {
            Log.w(TAG, "Can't pop stack. reason: previous screen is already attached");
            return true;
        }
        return false;
    }

    public void setScreenTopBarVisible(String screenInstanceId, final boolean visible, final boolean animate) {
        performOnScreen(screenInstanceId, new Task<Screen>() {
            @Override
            public void run(Screen param) {
                param.setTopBarVisible(visible, animate);
            }
        });
    }

    public void setScreenTitleBarTitle(String screenInstanceId, final String title) {
        performOnScreen(screenInstanceId, new Task<Screen>() {
            @Override
            public void run(Screen param) {
                param.setTitleBarTitle(title);
            }
        });
    }

    public void setScreenTitleBarRightButtons(String screenInstanceId, final String navigatorEventId, final List<TitleBarButtonParams> titleBarButtons) {
        performOnScreen(screenInstanceId, new Task<Screen>() {
            @Override
            public void run(Screen param) {
                param.setTitleBarRightButtons(navigatorEventId, titleBarButtons);
            }
        });
    }

    public void setScreenTitleBarLeftButton(String screenInstanceId, final String navigatorEventId, final TitleBarLeftButtonParams titleBarLeftButtonParams) {
        performOnScreen(screenInstanceId, new Task<Screen>() {
            @Override
            public void run(Screen param) {
                param.setTitleBarLeftButton(navigatorEventId, leftButtonOnClickListener, titleBarLeftButtonParams);
            }
        });
    }

    public StyleParams getCurrentScreenStyleParams() {
        return stack.peek().getStyleParams();
    }

    public boolean handleBackPressInJs() {
        ScreenParams currentScreen = stack.peek().screenParams;
        if (currentScreen.overrideBackPressInJs) {
            NavigationApplication.instance.sendNavigatorEvent("backPress", currentScreen.getNavigatorEventId());
            return true;
        }
        return false;
    }

    private void performOnScreen(String screenInstanceId, Task<Screen> task) {
        if (stack.isEmpty()) {
            return;
        }

        for (Screen screen : stack) {
            if (screen.getScreenInstanceId().equals(screenInstanceId)) {
                task.run(screen);
                return;
            }
        }
    }

    public void show() {
        isStackVisible = true;
        stack.peek().setStyle();
        stack.peek().setVisibility(View.VISIBLE);
    }

    public void hide() {
        isStackVisible = false;
        stack.peek().setVisibility(View.INVISIBLE);
    }
}
