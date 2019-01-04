package com.reactnativenavigation.views.titlebar;

import android.annotation.SuppressLint;
import android.content.Context;

import com.facebook.react.ReactInstanceManager;
import com.reactnativenavigation.react.ReactView;

@SuppressLint("ViewConstructor")
public class TitleBarReactButtonView extends ReactView {

    public TitleBarReactButtonView(Context context, ReactInstanceManager reactInstanceManager, String componentId, String componentName) {
        super(context, reactInstanceManager, componentId, componentName);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.UNSPECIFIED), heightMeasureSpec);
    }
}
