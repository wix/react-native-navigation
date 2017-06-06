package com.reactnativenavigation.views;

import android.content.Context;
import android.view.Menu;
import android.view.ViewManager;

import com.facebook.react.bridge.Callback;
import com.reactnativenavigation.NavigationApplication;
import com.reactnativenavigation.events.ContextualMenuHiddenEvent;
import com.reactnativenavigation.events.EventBus;
import com.reactnativenavigation.params.ContextualMenuButtonParams;
import com.reactnativenavigation.params.ContextualMenuParams;
import com.reactnativenavigation.params.StyleParams;
import com.reactnativenavigation.params.TitleBarLeftButtonParams;

import java.util.List;

public class ContextualMenu extends TitleBar implements LeftButtonOnClickListener, ContextualMenuButton.ContextualButtonClickListener {
    private ContextualMenuParams params;
    private Callback onButtonClicked;
    private final String navigatorEventId;

    public ContextualMenu(Context context, ContextualMenuParams params, StyleParams styleParams, Callback onButtonClicked) {
        super(context);
        this.params = params;
        this.onButtonClicked = onButtonClicked;
        navigatorEventId = params.navigationParams.navigatorEventId;
        setStyle(styleParams);
        setButtons();
    }

    public void setStyle(StyleParams styleParams) {
        params.setButtonsColor(styleParams.contextualMenuButtonsColor);
        if (styleParams.contextualMenuBackgroundColor.hasColor()) {
            setBackgroundColor(styleParams.contextualMenuBackgroundColor.getColor());
        }
    }

    public void setButtons() {
        addButtonsToContextualMenu(params.buttons, getMenu());
        setBackButton(params.leftButton);
    }

    private void setBackButton(TitleBarLeftButtonParams leftButton) {
        setLeftButton(leftButton, this, null, false);
    }

    private void addButtonsToContextualMenu(List<ContextualMenuButtonParams> buttons, Menu menu) {
        for (int i = 0; i < buttons.size(); i++) {
            final TitleBarButton button = new ContextualMenuButton(menu, this, buttons.get(i), this);
            addButtonInReverseOrder(buttons, i, button);
        }
    }

    @Override
    public boolean onTitleBarBackButtonClick() {
        dismiss();
        EventBus.instance.post(new ContextualMenuHiddenEvent());
        return true;
    }

    @Override
    public void onSideMenuButtonClick() {
        // nothing
    }

    @Override
    public void onClick(int index) {
        dismiss();
        EventBus.instance.post(new ContextualMenuHiddenEvent());
        onButtonClicked.invoke(index);
    }

    public void dismiss() {
        hide(new Runnable() {
            @Override
            public void run() {
                ((ViewManager) getParent()).removeView(ContextualMenu.this);
            }
        });
        NavigationApplication.instance.getEventEmitter().sendNavigatorEvent("contextualMenuDismissed", navigatorEventId);
    }
}
