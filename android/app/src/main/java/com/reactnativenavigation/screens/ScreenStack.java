package com.reactnativenavigation.screens;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.reactnativenavigation.NavigationApplication;
import com.reactnativenavigation.params.ContextualMenuParams;
import com.reactnativenavigation.params.FabParams;
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

    public void newStack(final ScreenParams params, LayoutParams layoutParams) {
        final Screen nextScreen = ScreenFactory.create(activity, params, leftButtonOnClickListener);
        final Screen previousScreen = stack.peek();
        if (isStackVisible) {
            pushScreenToVisibleStack(layoutParams, nextScreen, previousScreen, null, new Screen.OnDisplayListener() {
                @Override
                public void onDisplay() {
                    removeElementsBelowTop();
                }
            });
        } else {
            pushScreenToInvisibleStack(layoutParams, nextScreen, previousScreen, null);
            removeElementsBelowTop();
        }
    }

    private void removeElementsBelowTop() {
        while (stack.size() > 1) {
            Screen screen = stack.get(0);
            parent.removeView(screen);
            screen.destroy();
            stack.remove(0);
        }
    }

    public void pushInitialModalScreenWithAnimation(final ScreenParams initialScreenParams, LayoutParams params) {
        isStackVisible = true;
        pushInitialScreen(initialScreenParams, params);
        final Screen screen = stack.peek();
        screen.setOnDisplayListener(new Screen.OnDisplayListener() {
            @Override
            public void onDisplay() {
                screen.show(initialScreenParams.animateScreenTransitions, NavigationType.ShowModal);
                screen.setStyle();
            }
        });
    }

    public void pushInitialScreen(ScreenParams initialScreenParams, LayoutParams params) {
        Screen initialScreen = ScreenFactory.create(activity, initialScreenParams, leftButtonOnClickListener);
        initialScreen.setVisibility(View.INVISIBLE);
        removeCurrentScreen();
        addScreen(initialScreen, params);
    }

    private void removeCurrentScreen() {
        if (!stack.empty()) parent.removeView(peek());
    }

    public void push(final ScreenParams params, LayoutParams layoutParams, Promise onPushComplete) {
        Screen nextScreen = ScreenFactory.create(activity, params, leftButtonOnClickListener);
        final Screen previousScreen = stack.peek();
        if (isStackVisible) {
            if (nextScreen.screenParams.sharedElementsTransitions.isEmpty()) {
                pushScreenToVisibleStack(layoutParams, nextScreen, previousScreen, onPushComplete);
            } else {
                pushScreenToVisibleStackWithSharedElementTransition(layoutParams, nextScreen, previousScreen, onPushComplete);
            }
        } else {
            pushScreenToInvisibleStack(layoutParams, nextScreen, previousScreen, onPushComplete);
        }
    }

    private void pushScreenToVisibleStack(LayoutParams layoutParams, final Screen nextScreen,
                                          final Screen previousScreen, Promise onPushComplete) {
        pushScreenToVisibleStack(layoutParams, nextScreen, previousScreen, onPushComplete, null);
    }

    private void pushScreenToVisibleStack(LayoutParams layoutParams,
                                          final Screen nextScreen,
                                          final Screen previousScreen,
                                          @Nullable final Promise onPushComplete,
                                          @Nullable final Screen.OnDisplayListener onDisplay) {
        nextScreen.setVisibility(View.INVISIBLE);
        addScreen(nextScreen, layoutParams);
        NavigationApplication.instance.getEventEmitter().sendWillDisappearEvent(previousScreen.getScreenParams(), NavigationType.Push);
        nextScreen.setOnDisplayListener(new Screen.OnDisplayListener() {
            @Override
            public void onDisplay() {
                nextScreen.show(nextScreen.screenParams.animateScreenTransitions, new Runnable() {
                    @Override
                    public void run() {
                        if (onDisplay != null) onDisplay.onDisplay();
                        if (onPushComplete != null) onPushComplete.resolve(null);
                        NavigationApplication.instance.getEventEmitter().sendDidDisappearEvent(previousScreen.getScreenParams(), NavigationType.Push);
                        parent.removeView(previousScreen);
                    }
                }, NavigationType.Push);
            }
        });
    }

    private void pushScreenToVisibleStackWithSharedElementTransition(LayoutParams layoutParams, final Screen nextScreen,
                                                                     final Screen previousScreen, @Nullable final Promise onPushComplete) {
        nextScreen.setVisibility(View.INVISIBLE);
        nextScreen.setOnDisplayListener(new Screen.OnDisplayListener() {
            @Override
            public void onDisplay() {
                nextScreen.showWithSharedElementsTransitions(previousScreen.sharedElements.getToElements(), new Runnable() {
                    @Override
                    public void run() {
                        if (onPushComplete != null) onPushComplete.resolve(null);
                        parent.removeView(previousScreen);
                    }
                });
            }
        });
        addScreen(nextScreen, layoutParams);
    }

    private void pushScreenToInvisibleStack(LayoutParams layoutParams, Screen nextScreen, Screen previousScreen,
                                            @Nullable final Promise onPushComplete) {
        nextScreen.setVisibility(View.INVISIBLE);
        nextScreen.setOnDisplayListener(new Screen.OnDisplayListener() {
            @Override
            public void onDisplay() {
                if (onPushComplete != null) onPushComplete.resolve(null);
            }
        });
        addScreen(nextScreen, layoutParams);
        parent.removeView(previousScreen);
    }

    private void addScreen(Screen screen, LayoutParams layoutParams) {
        addScreenBeforeSnackbarAndFabLayout(screen, layoutParams);
        stack.push(screen);
    }

    private void addScreenBeforeSnackbarAndFabLayout(Screen screen, LayoutParams layoutParams) {
        parent.addView(screen, parent.getChildCount() - 1, layoutParams);
    }

    public void pop(boolean animated, double jsPopTimestamp) {
        pop(animated, jsPopTimestamp, null);
    }

    public void pop(final boolean animated, final double jsPopTimestamp, @Nullable final OnScreenPop onScreenPop) {
        if (!canPop()) {
            return;
        }
        if (keyboardVisibilityDetector.isKeyboardVisible()) {
            keyboardVisibilityDetector.setKeyboardCloseListener(new Runnable() {
                @Override
                public void run() {
                    keyboardVisibilityDetector.setKeyboardCloseListener(null);
                    popInternal(animated, jsPopTimestamp, onScreenPop);
                }
            });
            keyboardVisibilityDetector.closeKeyboard();
        } else {
            popInternal(animated, jsPopTimestamp, onScreenPop);
        }
    }

    private void popInternal(final boolean animated, double jsPopTimestamp, @Nullable final OnScreenPop onScreenPop) {
        final Screen toRemove = stack.pop();
        final Screen previous = stack.peek();
        previous.screenParams.timestamp = jsPopTimestamp;
        swapScreens(animated, toRemove, previous, onScreenPop);
    }

    private void swapScreens(boolean animated, final Screen toRemove, Screen previous, OnScreenPop onScreenPop) {
        readdPrevious(previous);
        previous.setStyle();
        hideScreen(animated, toRemove, previous);
        if (onScreenPop != null) {
            onScreenPop.onScreenPopAnimationEnd();
        }
    }

    private void hideScreen(boolean animated, final Screen toRemove, final Screen previous) {
        NavigationApplication.instance.getEventEmitter().sendWillAppearEvent(previous.getScreenParams(), NavigationType.Pop);
        Runnable onAnimationEnd = new Runnable() {
            @Override
            public void run() {
                toRemove.destroy();
                parent.removeView(toRemove);
                NavigationApplication.instance.getEventEmitter().sendDidAppearEvent(previous.getScreenParams(), NavigationType.Pop);
            }
        };
        if (animated) {
            toRemove.animateHide(previous.sharedElements.getToElements(), onAnimationEnd, NavigationType.Pop);
        } else {
            toRemove.hide(previous.sharedElements.getToElements(), onAnimationEnd, NavigationType.Pop);
        }
    }

    public Screen peek() {
        return stack.peek();
    }

    private void readdPrevious(Screen previous) {
        previous.setVisibility(View.VISIBLE);
        parent.addView(previous, 0);
    }

    public void popToRoot(final boolean animated, final double jsPopTimestamp, @Nullable final OnScreenPop onScreenPop) {
        if (keyboardVisibilityDetector.isKeyboardVisible()) {
            keyboardVisibilityDetector.setKeyboardCloseListener(new Runnable() {
                @Override
                public void run() {
                    keyboardVisibilityDetector.setKeyboardCloseListener(null);
                    popToRootInternal(animated, jsPopTimestamp, onScreenPop);
                }
            });
            keyboardVisibilityDetector.closeKeyboard();
        } else {
            popToRootInternal(animated, jsPopTimestamp, onScreenPop);
        }
    }

    private void popToRootInternal(final boolean animated, double jsPopTimestamp, @Nullable final OnScreenPop onScreenPop) {
        while (canPop()) {
            if (stack.size() == 2) {
                popInternal(animated, jsPopTimestamp, onScreenPop);
            } else {
                popInternal(animated, jsPopTimestamp, null);
            }
        }
    }

    public void destroy() {
        for (Screen screen : stack) {
            screen.destroy();
            parent.removeView(screen);
        }
        stack.clear();
    }

    public boolean canPop() {
        return stack.size() > 1 && !isPreviousScreenAttachedToWindow();
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

    public void setScreenTitleBarSubtitle(String screenInstanceId, final String subtitle) {
        performOnScreen(screenInstanceId, new Task<Screen>() {
            @Override
            public void run(Screen param) {
                param.setTitleBarSubtitle(subtitle);
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
            public void run(Screen screen) {
                screen.setTitleBarLeftButton(navigatorEventId, leftButtonOnClickListener, titleBarLeftButtonParams);
            }
        });
    }

    public void setFab(String screenInstanceId, final FabParams fabParams) {
        performOnScreen(screenInstanceId, new Task<Screen>() {
            @Override
            public void run(Screen screen) {
                screen.setFab(fabParams);
            }
        });
    }

    public void updateScreenStyle(String screenInstanceId, final Bundle styleParams) {
        performOnScreen(screenInstanceId, new Task<Screen>() {
            @Override
            public void run(Screen screen) {
                if (isScreenVisible(screen)) {
                    screen.updateVisibleScreenStyle(styleParams);
                } else {
                    screen.updateInvisibleScreenStyle(styleParams);
                }
            }
        });
    }

    private boolean isScreenVisible(Screen screen) {
        return isStackVisible && peek() == screen;
    }

    public void showContextualMenu(String screenInstanceId, final ContextualMenuParams params, final Callback onButtonClicked) {
        performOnScreen(screenInstanceId, new Task<Screen>() {
            @Override
            public void run(Screen screen) {
                screen.showContextualMenu(params, onButtonClicked);
            }
        });
    }

    public void dismissContextualMenu(String screenInstanceId) {
        performOnScreen(screenInstanceId, new Task<Screen>() {
            @Override
            public void run(Screen screen) {
                screen.dismissContextualMenu();
            }
        });
    }

    public void selectTopTabByTabIndex(String screenInstanceId, final int index) {
        performOnScreen(screenInstanceId, new Task<Screen>() {
            @Override
            public void run(Screen screen) {
                if (screen.screenParams.hasTopTabs()) {
                    ((ViewPagerScreen) screen).selectTopTabByTabIndex(index);
                }
            }
        });
    }

    public void selectTopTabByScreen(final String screenInstanceId) {
        performOnScreen(screenInstanceId, new Task<Screen>() {
            @Override
            public void run(Screen screen) {
                ((ViewPagerScreen) screen).selectTopTabByTabByScreen(screenInstanceId);
            }
        });
    }

    public StyleParams getCurrentScreenStyleParams() {
        return stack.peek().getStyleParams();
    }

    public boolean handleBackPressInJs() {
        ScreenParams currentScreen = stack.peek().screenParams;
        if (currentScreen.overrideBackPressInJs) {
            NavigationApplication.instance.getEventEmitter().sendNavigatorEvent("backPress", currentScreen.getNavigatorEventId());
            return true;
        }
        return false;
    }

    private void performOnScreen(String screenInstanceId, Task<Screen> task) {
        if (stack.isEmpty()) {
            return;
        }
        for (Screen screen : stack) {
            if (screen.hasScreenInstance(screenInstanceId)) {
                task.run(screen);
                return;
            }
        }
    }

    public void show(NavigationType type) {
        isStackVisible = true;
        stack.peek().setStyle();
        stack.peek().setVisibility(View.VISIBLE);
        sendScreenAppearEvent(type, stack.peek());
    }

    private void sendScreenAppearEvent(final NavigationType type, final Screen screen) {
        if (type == NavigationType.InitialScreen) {
            sendInitialScreenAppearEvent(type, screen);
        } else {
            sendScreenAppearEvent(screen, type);
        }
    }

    private void sendInitialScreenAppearEvent(final NavigationType type, final Screen screen) {
        screen.setOnDisplayListener(new Screen.OnDisplayListener() {
            @Override
            public void onDisplay() {
                sendScreenAppearEvent(screen, type);
            }
        });
    }

    private void sendScreenAppearEvent(Screen screen, NavigationType type) {
        screen.getScreenParams().timestamp = System.currentTimeMillis();
        NavigationApplication.instance.getEventEmitter().sendWillAppearEvent(screen.getScreenParams(), type);
        NavigationApplication.instance.getEventEmitter().sendDidAppearEvent(screen.getScreenParams(), type);
    }


    public void hide(NavigationType type) {
        NavigationApplication.instance.getEventEmitter().sendWillDisappearEvent(stack.peek().getScreenParams(), type);
        NavigationApplication.instance.getEventEmitter().sendDidDisappearEvent(stack.peek().getScreenParams(), type);
        isStackVisible = false;
        stack.peek().setVisibility(View.INVISIBLE);
    }
}
