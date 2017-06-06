package com.reactnativenavigation.utils;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import com.reactnativenavigation.NavigationApplication;

public class KeyboardVisibilityDetector {
    // 0.15 ratio is perhaps enough to determine keypad height.
    public static final double KEYBOARD_VISIBLE_RATIO = 0.15;

    private final KeyboardVisibilityLayoutListener keyboardVisibilityListener;
    private final View screen;
    private Runnable keyboardCloseListener;

    public KeyboardVisibilityDetector(final View screen) {
        this.screen = screen;
        keyboardVisibilityListener = new KeyboardVisibilityLayoutListener(this);
        screen.getViewTreeObserver().addOnGlobalLayoutListener(keyboardVisibilityListener);
    }

    public boolean isKeyboardVisible() {
        return keyboardVisibilityListener.isKeyboardVisible();
    }

    public void setKeyboardCloseListener(Runnable keyboardCloseListener) {
        this.keyboardCloseListener = keyboardCloseListener;
    }

    public void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) screen.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    private static class KeyboardVisibilityLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        public static final int KEYBOARD_CLOSE_DURATION = 100;
        private View screen;
        private boolean isVisible = false;
        private KeyboardVisibilityDetector detector;

        public KeyboardVisibilityLayoutListener(KeyboardVisibilityDetector detector) {
            this.detector = detector;
            this.screen = detector.screen;
        }

        public boolean isKeyboardVisible() {
            return isVisible;
        }

        @Override
        public void onGlobalLayout() {
            int screenHeight = screen.getRootView().getHeight();
            int screenBottomY = getScreenBottomY(screen);

            int keyboardHeight = screenHeight - screenBottomY;
            if (isKeyboardVisible(screenHeight, keyboardHeight)) {
                isVisible = true;
            } else {
                if (isVisible && detector.keyboardCloseListener != null) {
                    NavigationApplication.instance.runOnMainThread(detector.keyboardCloseListener, KEYBOARD_CLOSE_DURATION);
                }
                isVisible = false;
            }
        }

        private boolean isKeyboardVisible(int screenHeight, int keypadHeight) {
            return keypadHeight > screenHeight * KEYBOARD_VISIBLE_RATIO;
        }

        private int getScreenBottomY(View screen) {
            Rect r = new Rect();
            screen.getWindowVisibleDisplayFrame(r);
            return r.bottom;
        }
    }
}
