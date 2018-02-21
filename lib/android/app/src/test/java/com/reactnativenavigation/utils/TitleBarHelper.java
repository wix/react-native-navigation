package com.reactnativenavigation.utils;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class TitleBarHelper {
    public static View getRightButton(Toolbar toolbar, int index) {
        return (View) ViewUtils.findChildrenByClassRecursive(toolbar, TextView.class).get(index);
    }
}
