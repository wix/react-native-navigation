package com.reactnativenavigation.views;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.reactnativenavigation.NavigationApplication;
import com.reactnativenavigation.params.TitleBarButtonParams;
import com.reactnativenavigation.params.TitleBarLeftButtonParams;

class LeftButton extends MaterialMenuDrawable implements View.OnClickListener {

    private static int getColor(TitleBarButtonParams params) {
        return params != null && params.color.hasColor() ?
                params.color.getColor() :
                Color.BLACK;
    }

    private TitleBarLeftButtonParams params;
    private final LeftButtonOnClickListener onClickListener;
    private final String navigatorEventId;
    private final boolean overrideBackPressInJs;

    LeftButton(Context context,
               TitleBarLeftButtonParams params,
               LeftButtonOnClickListener onClickListener,
               String navigatorEventId,
               boolean overrideBackPressInJs) {
        super(context, getColor(params), Stroke.THIN);
        this.params = params;
        this.onClickListener = onClickListener;
        this.navigatorEventId = navigatorEventId;
        this.overrideBackPressInJs = overrideBackPressInJs;
        setInitialState();
    }

    void setIconState(TitleBarLeftButtonParams params) {
        this.params = params;
        if (params.color.hasColor()) {
            setColor(params.color.getColor());
        }
        animateIconState(params.iconState);
    }

    @Override
    public void onClick(View v) {
        if (isBackButton()) {
            handleBackButtonClick();
        } else if (isSideMenuButton()) {
            onClickListener.onSideMenuButtonClick();
        } else {
            sendClickEvent();
        }
    }

    private void handleBackButtonClick() {
        if (overrideBackPressInJs) {
            NavigationApplication.instance.getEventEmitter().sendNavigatorEvent("backPress", navigatorEventId);
        } else {
            onClickListener.onTitleBarBackButtonClick();
        }
    }

    private void setInitialState() {
        if (params != null) {
            setIconState(params.iconState);
        } else {
            setVisible(false);
        }
    }

    private boolean isBackButton() {
        return getIconState() == IconState.ARROW;
    }

    private boolean isSideMenuButton() {
        return getIconState() == IconState.BURGER;
    }

    private void sendClickEvent() {
        NavigationApplication.instance.getEventEmitter().sendNavigatorEvent(params.eventId, navigatorEventId);
    }
}
