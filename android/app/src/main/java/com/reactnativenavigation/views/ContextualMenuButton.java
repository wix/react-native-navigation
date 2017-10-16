package com.reactnativenavigation.views;

import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.reactnativenavigation.params.ContextualMenuButtonParams;

class ContextualMenuButton extends TitleBarButton {
    private ContextualMenuButtonParams contextualMenuButtonParams;
    private ContextualButtonClickListener contextualButtonClickListener;

    interface ContextualButtonClickListener {
        void onClick(int index);
    }

    ContextualMenuButton(Menu menu, ViewGroup parent, ContextualMenuButtonParams contextualMenuButtonParams, ContextualButtonClickListener contextualButtonClickListener) {
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
