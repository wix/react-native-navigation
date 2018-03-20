package com.reactnativenavigation.react;

import android.annotation.SuppressLint;
import android.content.Context;

import com.facebook.react.ReactInstanceManager;

@SuppressLint("ViewConstructor")
public class TopBarReactButtonView extends ReactView {

    public TopBarReactButtonView(Context context, ReactInstanceManager reactInstanceManager, String componentId, String componentName) {
        super(context, reactInstanceManager, componentId, componentName);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(
                getChildCount() > 0 ? MeasureSpec.makeMeasureSpec(getChildAt(0).getWidth(), MeasureSpec.EXACTLY) : widthMeasureSpec,
                heightMeasureSpec
        );
    }
}
