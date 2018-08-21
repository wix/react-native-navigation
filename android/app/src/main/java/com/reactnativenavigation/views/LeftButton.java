package com.reactnativenavigation.views;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.facebook.react.modules.i18nmanager.I18nUtil;
import com.reactnativenavigation.NavigationApplication;
import com.reactnativenavigation.params.TitleBarButtonParams;
import com.reactnativenavigation.params.TitleBarLeftButtonParams;
import com.reactnativenavigation.utils.ViewUtils;

class LeftButton extends MaterialMenuDrawable implements View.OnClickListener {

    private static int getColor(TitleBarButtonParams params) {
        return params != null && params.color.hasColor() ?
                params.color.getColor() :
                Color.BLACK;
    }

    private TitleBarLeftButtonParams params;
    private final LeftButtonOnClickListener onClickListener;
    private String navigatorEventId;
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
        setRTLEnabled(I18nUtil.getInstance().isRTL(context));
        setInitialState();
        setColor();
    }

    void setIconState(TitleBarLeftButtonParams params) {
        this.params = params;
        setColor();
        animateIconState(params.iconState);
    }

    void setCustomIcon(TitleBarLeftButtonParams params) {
        this.params = params;
        setColor();
    }

    @Override
    public void onClick(View v) {
        if (isRunning()) {
            return;
        }
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
            if (params.iconState != null) {
                setIconState(params.iconState);
            }
        } else {
            setVisible(false);
        }
    }

    private void setColor() {
        if (!params.color.hasColor()) {
            return;
        }
        if (params.hasDefaultIcon()) {
            setColor(params.color.getColor());
        } else if (params.hasCustomIcon()) {
            ViewUtils.tintDrawable(params.icon, params.color.getColor(), true);
        }
    }

    @Override
    public void setColor(int color) {
        if (params.hasDefaultIcon()) {
            super.setColor(color);
        } else {
            ViewUtils.tintDrawable(params.icon, color, true );
        }
    }

    private boolean isBackButton() {
        return params.hasDefaultIcon() && getIconState() == IconState.ARROW;
    }

    private boolean isSideMenuButton() {
        return params.hasDefaultIcon() && getIconState() == IconState.BURGER;
    }

    private void sendClickEvent() {
        NavigationApplication.instance.getEventEmitter().sendNavigatorEvent(params.eventId, navigatorEventId);
    }

    void updateNavigatorEventId(String navigatorEventId) {
        this.navigatorEventId = navigatorEventId;
    }
}
