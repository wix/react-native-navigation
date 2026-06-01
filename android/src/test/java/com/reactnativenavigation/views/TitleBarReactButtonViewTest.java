package com.reactnativenavigation.views;

import static android.view.View.MeasureSpec.AT_MOST;
import static android.view.View.MeasureSpec.EXACTLY;
import static android.view.View.MeasureSpec.getMode;
import static android.view.View.MeasureSpec.getSize;
import static android.view.View.MeasureSpec.makeMeasureSpec;
import static org.assertj.core.api.Java6Assertions.assertThat;

import android.app.Activity;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.options.ComponentOptions;
import com.reactnativenavigation.options.params.Number;
import com.reactnativenavigation.options.params.Text;
import com.reactnativenavigation.utils.UiUtils;
import com.reactnativenavigation.views.stack.topbar.titlebar.TitleBarReactButtonView;

import org.junit.Test;

public class TitleBarReactButtonViewTest extends BaseTest {
    private static final int PARENT_WIDTH = 200;
    private static final int PARENT_HEIGHT = 100;
    private static final int CHILD_WIDTH = 24;
    private static final int CHILD_HEIGHT = 16;

    @Test
    public void missingDimensionsMeasureToContentWithinParentBounds() {
        Activity activity = newActivity();
        TitleBarReactButtonView uut = createView(activity, new ComponentOptions());
        uut.addView(new FixedSizeView(activity), new ViewGroup.LayoutParams(CHILD_WIDTH, CHILD_HEIGHT));

        uut.measure(makeMeasureSpec(PARENT_WIDTH, AT_MOST), makeMeasureSpec(PARENT_HEIGHT, AT_MOST));

        assertThat(uut.getMeasuredWidth()).isEqualTo(CHILD_WIDTH);
        assertThat(uut.getMeasuredHeight()).isEqualTo(resolveActionBarSize(activity));
    }

    @Test
    public void explicitDimensionsMeasureExactly() {
        Activity activity = newActivity();
        ComponentOptions component = new ComponentOptions();
        component.width = new Number(72);
        component.height = new Number(32);
        TitleBarReactButtonView uut = createView(activity, component);
        uut.addView(new FixedSizeView(activity), new ViewGroup.LayoutParams(CHILD_WIDTH, CHILD_HEIGHT));

        uut.measure(makeMeasureSpec(PARENT_WIDTH, AT_MOST), makeMeasureSpec(PARENT_HEIGHT, AT_MOST));

        assertThat(uut.getMeasuredWidth()).isEqualTo(UiUtils.dpToPx(activity, 72));
        assertThat(uut.getMeasuredHeight()).isEqualTo(UiUtils.dpToPx(activity, 32));
    }

    @Test
    public void zeroParentWidthFallbacksToBoundedAtMostSpecAndHeightUsesActionBarSize() {
        Activity activity = newActivity();
        TitleBarReactButtonView uut = createView(activity, new ComponentOptions());
        RecordingView child = new RecordingView(activity);
        uut.addView(child, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));

        uut.measure(makeMeasureSpec(0, AT_MOST), makeMeasureSpec(0, AT_MOST));

        assertThat(getMode(child.lastWidthMeasureSpec)).isEqualTo(AT_MOST);
        assertThat(getSize(child.lastWidthMeasureSpec))
                .isEqualTo(Math.max(activity.getResources().getDisplayMetrics().widthPixels, 1));
        assertThat(getMode(child.lastHeightMeasureSpec)).isEqualTo(EXACTLY);
        assertThat(getSize(child.lastHeightMeasureSpec)).isEqualTo(Math.max(resolveActionBarSize(activity), 1));
    }

    @Test
    public void rtlMissingDimensionsUseBoundedSpecs() {
        Activity activity = newActivity();
        TitleBarReactButtonView uut = createView(activity, new ComponentOptions());
        RecordingView child = new RecordingView(activity);
        uut.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        uut.addView(child, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));

        uut.measure(makeMeasureSpec(PARENT_WIDTH, AT_MOST), makeMeasureSpec(PARENT_HEIGHT, AT_MOST));

        assertThat(getMode(child.lastWidthMeasureSpec)).isEqualTo(AT_MOST);
        assertThat(getSize(child.lastWidthMeasureSpec)).isEqualTo(PARENT_WIDTH);
        assertThat(getMode(child.lastHeightMeasureSpec)).isEqualTo(EXACTLY);
        assertThat(getSize(child.lastHeightMeasureSpec)).isEqualTo(resolveActionBarSize(activity));
    }

    private TitleBarReactButtonView createView(Activity activity, ComponentOptions component) {
        component.name = new Text("ButtonComponent");
        component.componentId = new Text("ButtonComponentId");
        return new TitleBarReactButtonView(activity, component);
    }

    private int resolveActionBarSize(Activity activity) {
        TypedValue tv = new TypedValue();
        if (activity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data, activity.getResources().getDisplayMetrics());
        }
        return UiUtils.dpToPx(activity, 48);
    }

    private static class FixedSizeView extends View {
        FixedSizeView(Activity activity) {
            super(activity);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            setMeasuredDimension(CHILD_WIDTH, CHILD_HEIGHT);
        }
    }

    private static class RecordingView extends View {
        int lastWidthMeasureSpec;
        int lastHeightMeasureSpec;

        RecordingView(Activity activity) {
            super(activity);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            lastWidthMeasureSpec = widthMeasureSpec;
            lastHeightMeasureSpec = heightMeasureSpec;
            setMeasuredDimension(getSize(widthMeasureSpec), getSize(heightMeasureSpec));
        }
    }
}
