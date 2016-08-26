package com.reactnativenavigation.controllers;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.reactnativenavigation.R;
import com.reactnativenavigation.layouts.Layout;
import com.reactnativenavigation.layouts.ModalScreenLayout;
import com.reactnativenavigation.layouts.ScreenStackContainer;
import com.reactnativenavigation.params.ScreenParams;
import com.reactnativenavigation.params.TitleBarButtonParams;
import com.reactnativenavigation.params.TitleBarLeftButtonParams;

import java.util.List;

public class Modal extends Dialog implements DialogInterface.OnDismissListener, ScreenStackContainer {

    private final AppCompatActivity activity;
    private final OnModalDismissedListener onModalDismissedListener;
    private final ScreenParams screenParams;
    private Layout layout;

    public void setTopBarVisible(String screenInstanceId, boolean hidden, boolean animated) {
        layout.setTopBarVisible(screenInstanceId, hidden, animated);
    }

    public void setTitleBarTitle(String screenInstanceId, String title) {
        layout.setTitleBarTitle(screenInstanceId, title);
    }

    public void setTitleBarRightButtons(String screenInstanceId, String navigatorEventId, List<TitleBarButtonParams> titleBarButtons) {
        layout.setTitleBarRightButtons(screenInstanceId, navigatorEventId, titleBarButtons);
    }

    public void setTitleBarLeftButton(String screenInstanceId, String navigatorEventId, TitleBarLeftButtonParams titleBarLeftButton) {
        layout.setTitleBarLeftButton(screenInstanceId, navigatorEventId, titleBarLeftButton);
    }

    @Override
    public boolean onTitleBarBackButtonClick() {
        if (!layout.onBackPressed()) {
            onBackPressed();
        }
        return true;
    }

    public void onSideMenuButtonClick() {
    }

    public interface OnModalDismissedListener {
        void onModalDismissed(Modal modal);
    }

    public Modal(AppCompatActivity activity, OnModalDismissedListener onModalDismissedListener, ScreenParams screenParams) {
        super(activity, R.style.Modal);
        this.activity = activity;
        this.onModalDismissedListener = onModalDismissedListener;
        this.screenParams = screenParams;
        createContent();
    }

    public AppCompatActivity getActivity() {
        return activity;
    }

    private void createContent() {
        setCancelable(true);
        setOnDismissListener(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        layout = new ModalScreenLayout(getActivity(), null, screenParams, this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(layout.asView());
    }

    @Override
    public void push(ScreenParams params) {
        layout.push(params);
    }

    @Override
    public void pop(ScreenParams screenParams) {
        layout.pop(screenParams);
    }

    @Override
    public void popToRoot(ScreenParams params) {
        layout.popToRoot(params);
    }

    @Override
    public void newStack(ScreenParams params) {
        layout.newStack(params);
    }

    @Override
    public void destroy() {
        layout.destroy();
    }

    @Override
    public void onBackPressed() {
        if (!layout.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        destroy();
        onModalDismissedListener.onModalDismissed(this);
    }
}
