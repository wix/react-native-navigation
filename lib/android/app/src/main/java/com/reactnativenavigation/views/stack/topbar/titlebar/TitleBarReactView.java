package com.reactnativenavigation.views.stack.topbar.titlebar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;

import com.facebook.react.ReactInstanceManager;
import com.reactnativenavigation.react.ReactView;

import androidx.appcompat.widget.Toolbar;

@SuppressLint("ViewConstructor")
public class TitleBarReactView extends ReactView {

    public TitleBarReactView(Context context, ReactInstanceManager reactInstanceManager, String componentId, String componentName) {
        super(context, reactInstanceManager, componentId, componentName);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(
                getWidthMeasureSpec(widthMeasureSpec),
                heightMeasureSpec
        );
    }

    private int getWidthMeasureSpec(int currentSpec) {
        return (isCenter() && getChildCount() > 0 && getChildAt(0).getWidth() > 0) ?
                MeasureSpec.makeMeasureSpec(getChildAt(0).getWidth(), MeasureSpec.EXACTLY) :
                currentSpec;
    }

    private boolean isCenter() {
        return ((Toolbar.LayoutParams) getLayoutParams()).gravity == Gravity.CENTER;
    }
}
