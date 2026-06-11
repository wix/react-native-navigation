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
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // This is a workaround, ReactNative throws exception when views have ids, On android MenuItems
        // With ActionViews like this got an id, see #7253
        if (!this.isAttachedToWindow()) {
            this.setId(View.NO_ID);
        }

        // Measure the hosted React surface with bounded (AT_MOST) specs and let it size itself to its
        // content. Under Fabric, ReactSurfaceView reports max-of-children under AT_MOST (i.e. the
        // laid-out content size) and pushes these constraints to the async Fabric layout. We must NOT
        // follow this with a forced EXACTLY pass: re-pushing a forced box (which, before the content
        // has laid out, is collapsed to ~1px) makes Fabric lay the content out into that collapsed box
        // and the button never recovers (#8320/#8326 attempted exactly that and regressed under the
        // New Architecture). When Fabric mounts/sizes the content, ReactSurfaceView re-requests layout
        // and this measure runs again against the now-non-zero content.
        int widthSpec = component.width.hasValue()
                ? createExactSpec(component.width)
                : makeMeasureSpec(resolveAvailableWidth(widthMeasureSpec), AT_MOST);
        int heightSpec = createHeightSpec(heightMeasureSpec, component.height);
        super.onMeasure(widthSpec, heightSpec);
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
