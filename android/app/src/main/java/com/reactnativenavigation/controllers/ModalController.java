package com.reactnativenavigation.controllers;

import android.support.v7.app.AppCompatActivity;

import com.reactnativenavigation.events.EventBus;
import com.reactnativenavigation.events.ModalDismissedEvent;
import com.reactnativenavigation.layouts.ScreenStackContainer;
import com.reactnativenavigation.params.ScreenParams;
import com.reactnativenavigation.params.TitleBarButtonParams;
import com.reactnativenavigation.params.TitleBarLeftButtonParams;

import java.util.List;
import java.util.Stack;

public class ModalController implements ScreenStackContainer, Modal.OnModalDismissedListener {
    private final AppCompatActivity activity;
    private Stack<Modal> stack = new Stack<>();

    public ModalController(AppCompatActivity activity) {
        this.activity = activity;
    }

    public boolean containsNavigator(String navigatorId) {
        for (Modal modal : stack) {
            if (modal.containsNavigator(navigatorId)) {
                return true;
            }
        }
        return false;
    }

    public void showModal(ScreenParams screenParams) {
        Modal modal = new Modal(activity, this, screenParams);
        modal.show();
        stack.add(modal);
    }

    public void dismissTopModal() {
        if (isShowing()) {
            stack.pop().dismiss();
        }
    }

    public void dismissAllModals() {
        for (Modal modal : stack) {
            modal.dismiss();
        }
        stack.clear();
    }

    public boolean isShowing() {
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

    public void setTitleBarTitle(String screenInstanceId, String title) {
        for (Modal modal : stack) {
            modal.setTitleBarTitle(screenInstanceId, title);
        }
    }

    public void setTitleBarSubtitle(String screenInstanceId, String subtitle) {
        for (Modal modal : stack) {
            modal.setTitleBarSubtitle(screenInstanceId, subtitle);
        }
    }

    public void setTitleBarRightButtons(String screenInstanceId, String navigatorEventId, List<TitleBarButtonParams> titleBarButtons) {
        for (Modal modal : stack) {
            modal.setTitleBarRightButtons(screenInstanceId, navigatorEventId, titleBarButtons);
        }
    }

    public void setTitleBarLeftButton(String screenInstanceId, String navigatorEventId, TitleBarLeftButtonParams titleBarLeftButton) {
        for (Modal modal : stack) {
            modal.setTitleBarLeftButton(screenInstanceId, navigatorEventId, titleBarLeftButton);
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
}
