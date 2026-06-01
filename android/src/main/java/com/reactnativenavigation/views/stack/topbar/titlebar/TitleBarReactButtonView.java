package com.reactnativenavigation.views.stack.topbar.titlebar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.TypedValue;
import android.view.View;

import com.reactnativenavigation.options.ComponentOptions;
import com.reactnativenavigation.options.params.Number;
import com.reactnativenavigation.react.ReactView;

import static android.view.View.MeasureSpec.AT_MOST;
import static android.view.View.MeasureSpec.EXACTLY;
import static android.view.View.MeasureSpec.makeMeasureSpec;
import static com.reactnativenavigation.utils.UiUtils.dpToPx;

@SuppressLint("ViewConstructor")
public class TitleBarReactButtonView extends ReactView {
    private final ComponentOptions component;

    public TitleBarReactButtonView(Context context, ComponentOptions component) {
        super(context, component.componentId.get(), component.name.get());
        this.component = component;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // This is a workaround, ReactNative throws exception when views have ids, On android MenuItems
        // With ActionViews like this got an id, see #7253
        if (!this.isAttachedToWindow()) {
            this.setId(View.NO_ID);
        }

        super.onMeasure(
                createWidthSpec(widthMeasureSpec, component.width),
                createHeightSpec(heightMeasureSpec, component.height)
        );
    }

    private int createWidthSpec(int measureSpec, Number dimension) {
        return createSpec(measureSpec, dimension, Math.max(getResources().getDisplayMetrics().widthPixels, 1));
    }

    private int createHeightSpec(int measureSpec, Number dimension) {
        if (dimension.hasValue()) {
            return createExactSpec(dimension);
        }
        return makeMeasureSpec(Math.max(resolveActionBarSize(), 1), EXACTLY);
    }

    private int createSpec(int measureSpec, Number dimension, int fallbackSize) {
        if (dimension.hasValue()) {
            return createExactSpec(dimension);
        } else {
            // Use bounded wrap-content width to avoid RN/Yoga RTL padding issues caused by
            // UNSPECIFIED, without forcing every custom button to actionBarSize width.
            int availableSize = MeasureSpec.getSize(measureSpec);
            return makeMeasureSpec(availableSize > 0 ? availableSize : fallbackSize, AT_MOST);
        }
    }

    private int createExactSpec(Number dimension) {
        return makeMeasureSpec(MeasureSpec.getSize(dpToPx(getContext(), dimension.get())), EXACTLY);
    }

    private int resolveActionBarSize() {
        TypedValue tv = new TypedValue();
        if (getContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data, getContext().getResources().getDisplayMetrics());
        }
        return (int) dpToPx(getContext(), 48f);
    }
}
