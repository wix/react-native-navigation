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

        int finalHeightSpec = createHeightSpec(component.height);
        if (component.width.hasValue()) {
            super.onMeasure(createExactSpec(component.width), finalHeightSpec);
            return;
        }

        // First discover the content width without forcing every custom button to actionBarSize.
        super.onMeasure(makeMeasureSpec(resolveAvailableWidth(widthMeasureSpec), AT_MOST), finalHeightSpec);

        // Then give RN/Yoga a stable exact final box for compatibility with centered button layouts.
        super.onMeasure(makeMeasureSpec(Math.max(getMeasuredWidth(), 1), EXACTLY), finalHeightSpec);
    }

    private int createHeightSpec(Number dimension) {
        if (dimension.hasValue()) {
            return createExactSpec(dimension);
        }
        return makeMeasureSpec(Math.max(resolveActionBarSize(), 1), EXACTLY);
    }

    private int resolveAvailableWidth(int measureSpec) {
        int availableSize = MeasureSpec.getSize(measureSpec);
        return availableSize > 0 ? availableSize : Math.max(getResources().getDisplayMetrics().widthPixels, 1);
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
