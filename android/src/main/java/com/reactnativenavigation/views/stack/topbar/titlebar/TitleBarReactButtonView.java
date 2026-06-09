package com.reactnativenavigation.views.stack.topbar.titlebar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.reactnativenavigation.options.ComponentOptions;
import com.reactnativenavigation.options.params.Number;
import com.reactnativenavigation.react.ReactView;

import static android.view.View.MeasureSpec.AT_MOST;
import static android.view.View.MeasureSpec.EXACTLY;
import static android.view.View.MeasureSpec.makeMeasureSpec;
import static com.reactnativenavigation.utils.UiUtils.dpToPx;

@SuppressLint("ViewConstructor")
public class TitleBarReactButtonView extends ReactView {
    private static final float FINAL_WIDTH_PADDING_DP = 1f;
    private final ComponentOptions component;

    public TitleBarReactButtonView(Context context, ComponentOptions component) {
        super(context, component.componentId.get(), component.name.get());
        this.component = component;
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        if (child.getLayoutParams() instanceof FrameLayout.LayoutParams) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) child.getLayoutParams();
            layoutParams.gravity = layoutParams.gravity == -1
                    ? Gravity.CENTER_VERTICAL
                    : (layoutParams.gravity & ~Gravity.VERTICAL_GRAVITY_MASK) | Gravity.CENTER_VERTICAL;
        }

        // The hosted React content lays out asynchronously (off the native measure pass, especially
        // under Fabric), so the first onMeasure can observe a 0-sized child and freeze the button at
        // ~1px. When the content later reports a non-zero size, re-request layout so onMeasure re-runs
        // and sizes the button to the content. The size-changed guard makes this converge: once the
        // button re-measures to the content, the child's bounds stop changing and no further layout is
        // requested. The explicit-dimensions check is done lazily inside the listener rather than here
        // because onViewAdded fires from the superclass constructor (before `component` is assigned)
        // for the React surface view, which is the very child whose size we need to observe.
        child.addOnLayoutChangeListener((v, l, t, r, b, ol, ot, or, ob) -> {
            if (component != null && component.width.hasValue() && component.height.hasValue()) {
                return;
            }
            boolean sizeChanged = (r - l) != (or - ol) || (b - t) != (ob - ot);
            if (sizeChanged) {
                requestLayout();
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // This is a workaround, ReactNative throws exception when views have ids, On android MenuItems
        // With ActionViews like this got an id, see #7253
        if (!this.isAttachedToWindow()) {
            this.setId(View.NO_ID);
        }

        int initialWidthSpec = component.width.hasValue()
                ? createExactSpec(component.width)
                : makeMeasureSpec(resolveAvailableWidth(widthMeasureSpec), AT_MOST);
        int initialHeightSpec = createHeightSpec(heightMeasureSpec, component.height);

        // First discover the content size without forcing every custom button to actionBarSize.
        super.onMeasure(initialWidthSpec, initialHeightSpec);

        if (component.width.hasValue() && component.height.hasValue()) {
            return;
        }

        // Then give RN/Yoga a stable exact final box for compatibility with centered button layouts.
        // A small allowance avoids clipping implicit RN padding/subpixel layout while staying content-based.
        int finalWidth = component.width.hasValue()
                ? MeasureSpec.getSize(initialWidthSpec)
                : resolveFinalWidth(getMeasuredWidth());
        int finalHeight = component.height.hasValue()
                ? MeasureSpec.getSize(initialHeightSpec)
                : Math.max(getMeasuredHeight(), 1);
        super.onMeasure(makeMeasureSpec(finalWidth, EXACTLY), makeMeasureSpec(finalHeight, EXACTLY));
    }

    private int createHeightSpec(int measureSpec, Number dimension) {
        if (dimension.hasValue()) {
            return createExactSpec(dimension);
        }
        int availableSize = MeasureSpec.getSize(measureSpec);
        return makeMeasureSpec(availableSize > 0 ? availableSize : Math.max(resolveActionBarSize(), 1), AT_MOST);
    }

    private int resolveAvailableWidth(int measureSpec) {
        int availableSize = MeasureSpec.getSize(measureSpec);
        return availableSize > 0 ? availableSize : Math.max(getResources().getDisplayMetrics().widthPixels, 1);
    }

    private int resolveFinalWidth(int measuredContentWidth) {
        return Math.max(measuredContentWidth + (int) Math.ceil(dpToPx(getContext(), FINAL_WIDTH_PADDING_DP)), 1);
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
