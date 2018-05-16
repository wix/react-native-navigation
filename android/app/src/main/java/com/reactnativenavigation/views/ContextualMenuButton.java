package com.reactnativenavigation.views;

import android.support.v7.widget.ActionMenuView;
import android.view.Menu;
import android.view.MenuItem;

import com.reactnativenavigation.params.ContextualMenuButtonParams;

class ContextualMenuButton extends TitleBarButton {
    private ContextualMenuButtonParams contextualMenuButtonParams;
    private ContextualButtonClickListener contextualButtonClickListener;

    interface ContextualButtonClickListener {
        void onClick(int index);
    }

    ContextualMenuButton(Menu menu, ActionMenuView parent, ContextualMenuButtonParams contextualMenuButtonParams, ContextualButtonClickListener contextualButtonClickListener) {
        super(menu, parent, contextualMenuButtonParams, null);
        this.contextualMenuButtonParams = contextualMenuButtonParams;
        this.contextualButtonClickListener = contextualButtonClickListener;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        contextualButtonClickListener.onClick(contextualMenuButtonParams.index);
        return true;
    }
}
