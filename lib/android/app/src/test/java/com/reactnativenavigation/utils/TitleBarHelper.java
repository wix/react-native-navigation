package com.reactnativenavigation.utils;

import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;

public class TitleBarHelper {
    public static ActionMenuItemView getRightButton(Toolbar toolbar, int index) {
        return (ActionMenuItemView) ViewUtils.findChildrenByClassRecursive(toolbar, ActionMenuItemView.class).get(toolbar.getMenu().size() - index - 1);
    }
}
