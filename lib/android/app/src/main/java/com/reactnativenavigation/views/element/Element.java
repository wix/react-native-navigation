package com.reactnativenavigation.views.element;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.FrameLayout;

public class Element extends FrameLayout {
    private String elementId;

    public Element(@NonNull Context context) {
        super(context);
    }

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    public View getChild() {
        return getChildAt(0);
    }
}
