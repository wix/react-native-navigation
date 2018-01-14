package com.reactnativenavigation.views;

import android.content.Context;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.reactnativenavigation.params.NavigationParams;
import com.reactnativenavigation.views.utils.Constants;
import com.reactnativenavigation.views.utils.ViewMeasurer;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class TitleBarButtonComponent extends ContentView {

    public TitleBarButtonComponent(Context context, String componentName, Bundle passProps) {
        super(context, componentName, NavigationParams.EMPTY, passProps);
        setLayoutParams(new FrameLayout.LayoutParams(WRAP_CONTENT, Constants.TOOLBAR_BUTTON_SIZE));
        setViewMeasurer(new ViewMeasurer() {
            @Override
            public int getMeasuredWidth(int widthMeasureSpec) {
                return getChildCount() > 0 ? getChildAt(0).getWidth() : 0;
            }
        });
    }
}
