package com.reactnativenavigation.views.stack.topbar.titlebar;

import android.annotation.SuppressLint;
import android.content.Context;

import com.facebook.react.ReactInstanceManager;
import com.reactnativenavigation.options.ComponentOptions;
import com.reactnativenavigation.options.params.Number;
import com.reactnativenavigation.react.ReactView;

import static android.view.View.MeasureSpec.EXACTLY;
import static android.view.View.MeasureSpec.UNSPECIFIED;
import static android.view.View.MeasureSpec.makeMeasureSpec;
import static com.reactnativenavigation.utils.UiUtils.dpToPx;

@SuppressLint("ViewConstructor")
public class TitleBarReactButtonView extends ReactView {
    private final ComponentOptions component;

    public TitleBarReactButtonView(Context context, ReactInstanceManager reactInstanceManager, ComponentOptions component) {
        super(context, reactInstanceManager, component.componentId.get(), component.name.get());
        this.component = component;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(createSpec(widthMeasureSpec, component.width), createSpec(widthMeasureSpec, component.height));
    }

    private int createSpec(int measureSpec, Number dimension) {
        if (dimension.hasValue()) {
            return makeMeasureSpec(MeasureSpec.getSize(dpToPx(getContext(), dimension.get())), EXACTLY);
        } else {
            return makeMeasureSpec(MeasureSpec.getSize(measureSpec), UNSPECIFIED);
        }
    }
}
