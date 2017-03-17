package com.reactnativenavigation.controllers;

import android.support.v7.app.AppCompatActivity;

import com.facebook.react.bridge.Callback;
import com.reactnativenavigation.events.EventBus;
import com.reactnativenavigation.events.ModalDismissedEvent;
import com.reactnativenavigation.layouts.ScreenStackContainer;
import com.reactnativenavigation.params.ContextualMenuParams;
import com.reactnativenavigation.params.FabParams;
import com.reactnativenavigation.params.ScreenParams;
import com.reactnativenavigation.params.SlidingOverlayParams;
import com.reactnativenavigation.params.TitleBarButtonParams;
import com.reactnativenavigation.params.TitleBarLeftButtonParams;

import java.util.List;
import java.util.Stack;

class ModalController implements ScreenStackContainer, Modal.OnModalDismissedListener {
    private final AppCompatActivity activity;
    private Stack<Modal> stack = new Stack<>();

    ModalController(AppCompatActivity activity) {
        this.activity = activity;
    }

    boolean containsNavigator(String navigatorId) {
        for (Modal modal : stack) {
            if (modal.containsNavigator(navigatorId)) {
                return true;
            }
        }
        return false;
    }

    void showModal(ScreenParams screenParams) {
        Modal modal = new Modal(activity, this, screenParams);
        modal.show();
        stack.add(modal);
    }

    void dismissTopModal() {
        if (isShowing()) {
            stack.pop().dismiss();
        }
    }

    void dismissAllModals() {
        for (Modal modal : stack) {
            modal.dismiss();
        }
        stack.clear();
    }

    boolean isShowing() {
        return !stack.empty();
    }

    public void push(ScreenParams params) {
        stack.peek().push(params);
    }

    @Override
    public void pop(ScreenParams screenParams) {
        stack.peek().pop(screenParams);
    }

    @Override
    public void popToRoot(ScreenParams params) {
        stack.peek().popToRoot(params);
    }

    @Override
    public void newStack(ScreenParams params) {
        stack.peek().newStack(params);
    }

    @Override
    public void destroy() {
        for (Modal modal : stack) {
            modal.destroy();
            modal.dismiss();
        }
        stack.clear();
    }

    @Override
    public void onModalDismissed(Modal modal) {
        stack.remove(modal);
        if (isShowing()) {
            stack.peek().onModalDismissed();
        }
        EventBus.instance.post(new ModalDismissedEvent());
    }

    public void setTopBarVisible(String screenInstanceId, boolean hidden, boolean animated) {
        for (Modal modal : stack) {
            modal.setTopBarVisible(screenInstanceId, hidden, animated);
        }
    }

    void setTitleBarTitle(String screenInstanceId, String title) {
        for (Modal modal : stack) {
            modal.setTitleBarTitle(screenInstanceId, title);
        }
    }

    void setTitleBarSubtitle(String screenInstanceId, String subtitle) {
        for (Modal modal : stack) {
            modal.setTitleBarSubtitle(screenInstanceId, subtitle);
        }
    }

    void setTitleBarRightButtons(String screenInstanceId, String navigatorEventId, List<TitleBarButtonParams> titleBarButtons) {
        for (Modal modal : stack) {
            modal.setTitleBarRightButtons(screenInstanceId, navigatorEventId, titleBarButtons);
        }
    }

    void setTitleBarLeftButton(String screenInstanceId, String navigatorEventId, TitleBarLeftButtonParams titleBarLeftButton) {
        for (Modal modal : stack) {
            modal.setTitleBarLeftButton(screenInstanceId, navigatorEventId, titleBarLeftButton);
        }
    }

    void setFab(String screenInstanceId, String navigatorEventId, FabParams fab) {
        for (Modal modal : stack) {
            modal.setFab(screenInstanceId, navigatorEventId, fab);
        }
    }

    public void showContextualMenu(String screenInstanceId, ContextualMenuParams params, Callback onButtonClicked) {
        for (Modal modal : stack) {
            modal.showContextualMenu(screenInstanceId, params, onButtonClicked);
        }
    }

    public void dismissContextualMenu(String screenInstanceId) {
        for (Modal modal : stack) {
            modal.dismissContextualMenu(screenInstanceId);
        }
    }

    @Override
    public boolean onTitleBarBackButtonClick() {
        // Do nothing and let the layout handle the back button click
        return false;
    }

    @Override
    public void onSideMenuButtonClick() {
        // Do nothing and let the layout handle the click
    }

    void showSlidingOverlay(SlidingOverlayParams params) {
        stack.peek().showSlidingOverlay(params);
    }

    void hideSlidingOverlay() {
        stack.peek().hideSlidingOverlay();
    }
}
