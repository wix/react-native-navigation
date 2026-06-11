package com.reactnativenavigation.views;

import static android.view.View.MeasureSpec.AT_MOST;
import static android.view.View.MeasureSpec.EXACTLY;
import static android.view.View.MeasureSpec.getMode;
import static android.view.View.MeasureSpec.getSize;
import static android.view.View.MeasureSpec.makeMeasureSpec;
import static org.assertj.core.api.Java6Assertions.assertThat;

import android.app.Activity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.options.ComponentOptions;
import com.reactnativenavigation.options.params.Number;
import com.reactnativenavigation.options.params.Text;
import com.reactnativenavigation.utils.UiUtils;
import com.reactnativenavigation.views.stack.topbar.titlebar.TitleBarReactButtonView;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TitleBarReactButtonViewTest extends BaseTest {
    private static final int PARENT_WIDTH = 200;
    private static final int PARENT_HEIGHT = 100;
    private static final int CHILD_WIDTH = 24;
    private static final int CHILD_HEIGHT = 16;

    // Without explicit dimensions the button measures the hosted React surface ONCE with bounded
    // (AT_MOST) specs and lets it size itself to its content. It deliberately does not follow up with
    // a forced EXACTLY pass — under the New Architecture that re-pushes a (initially collapsed) box to
    // the async Fabric layout and the button never recovers its real size.
    @Test
    public void missingDimensionsMeasureToContentWithASingleBoundedPass() {
        Activity activity = newActivity();
        TitleBarReactButtonView uut = createView(activity, new ComponentOptions());
        RecordingContentView child = new RecordingContentView(activity);
        setContentView(uut, child);

        uut.measure(makeMeasureSpec(PARENT_WIDTH, AT_MOST), makeMeasureSpec(PARENT_HEIGHT, AT_MOST));

        assertThat(uut.getMeasuredWidth()).isEqualTo(CHILD_WIDTH);
        assertThat(uut.getMeasuredHeight()).isEqualTo(CHILD_HEIGHT);
        assertThat(child.widthMeasureSpecs.size()).isEqualTo(1);
        assertThat(getMode(child.widthMeasureSpecs.get(0))).isEqualTo(AT_MOST);
        assertThat(getSize(child.widthMeasureSpecs.get(0))).isEqualTo(PARENT_WIDTH);
        assertThat(getMode(child.heightMeasureSpecs.get(0))).isEqualTo(AT_MOST);
        assertThat(getSize(child.heightMeasureSpecs.get(0))).isEqualTo(PARENT_HEIGHT);
    }

    @Test
    public void explicitDimensionsMeasureExactly() {
        Activity activity = newActivity();
        ComponentOptions component = new ComponentOptions();
        component.width = new Number(72);
        component.height = new Number(32);
        TitleBarReactButtonView uut = createView(activity, component);
        RecordingContentView child = new RecordingContentView(activity);
        setContentView(uut, child);

        uut.measure(makeMeasureSpec(PARENT_WIDTH, AT_MOST), makeMeasureSpec(PARENT_HEIGHT, AT_MOST));

        assertThat(uut.getMeasuredWidth()).isEqualTo(UiUtils.dpToPx(activity, 72));
        assertThat(uut.getMeasuredHeight()).isEqualTo(UiUtils.dpToPx(activity, 32));
        assertThat(child.widthMeasureSpecs.size()).isEqualTo(1);
        assertThat(getMode(child.widthMeasureSpecs.get(0))).isEqualTo(EXACTLY);
        assertThat(getSize(child.widthMeasureSpecs.get(0))).isEqualTo(UiUtils.dpToPx(activity, 72));
        assertThat(getMode(child.heightMeasureSpecs.get(0))).isEqualTo(EXACTLY);
        assertThat(getSize(child.heightMeasureSpecs.get(0))).isEqualTo(UiUtils.dpToPx(activity, 32));
    }

    @Test
    public void zeroParentSpecsFallbackToBoundedAtMostSpecs() {
        Activity activity = newActivity();
        TitleBarReactButtonView uut = createView(activity, new ComponentOptions());
        RecordingContentView child = new RecordingContentView(activity);
        setContentView(uut, child);

        uut.measure(makeMeasureSpec(0, AT_MOST), makeMeasureSpec(0, AT_MOST));

        assertThat(child.widthMeasureSpecs.size()).isEqualTo(1);
        assertThat(getMode(child.widthMeasureSpecs.get(0))).isEqualTo(AT_MOST);
        assertThat(getSize(child.widthMeasureSpecs.get(0)))
                .isEqualTo(Math.max(activity.getResources().getDisplayMetrics().widthPixels, 1));
        assertThat(getMode(child.heightMeasureSpecs.get(0))).isEqualTo(AT_MOST);
        assertThat(getSize(child.heightMeasureSpecs.get(0))).isEqualTo(Math.max(resolveActionBarSize(activity), 1));
    }

    @Test
    public void rtlMissingDimensionsUseBoundedSpecs() {
        Activity activity = newActivity();
        TitleBarReactButtonView uut = createView(activity, new ComponentOptions());
        RecordingContentView child = new RecordingContentView(activity);
        uut.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        setContentView(uut, child);

        uut.measure(makeMeasureSpec(PARENT_WIDTH, AT_MOST), makeMeasureSpec(PARENT_HEIGHT, AT_MOST));

        assertThat(child.widthMeasureSpecs.size()).isEqualTo(1);
        assertThat(getMode(child.widthMeasureSpecs.get(0))).isEqualTo(AT_MOST);
        assertThat(getSize(child.widthMeasureSpecs.get(0))).isEqualTo(PARENT_WIDTH);
        assertThat(getMode(child.heightMeasureSpecs.get(0))).isEqualTo(AT_MOST);
        assertThat(getSize(child.heightMeasureSpecs.get(0))).isEqualTo(PARENT_HEIGHT);
    }

    @Test
    public void contentRemainsCenteredWhenMenuCellLaysButtonOutTallerThanMeasuredHeight() {
        Activity activity = newActivity();
        TitleBarReactButtonView uut = createView(activity, new ComponentOptions());
        RecordingContentView child = new RecordingContentView(activity);
        setContentView(uut, child);

        uut.measure(makeMeasureSpec(PARENT_WIDTH, AT_MOST), makeMeasureSpec(PARENT_HEIGHT, AT_MOST));
        uut.layout(0, 0, uut.getMeasuredWidth(), PARENT_HEIGHT);

        // The button hugs the content height, and when a taller cell lays it out the content stays
        // vertically centered via the CENTER_VERTICAL gravity applied in onViewAdded.
        assertThat(uut.getMeasuredHeight()).isEqualTo(CHILD_HEIGHT);
        assertThat(child.getTop()).isEqualTo((PARENT_HEIGHT - CHILD_HEIGHT) / 2);
        assertThat(child.getBottom()).isEqualTo((PARENT_HEIGHT + CHILD_HEIGHT) / 2);
    }

    @Test
    public void contentCenteringReplacesExistingVerticalGravityOnly() {
        Activity activity = newActivity();
        TitleBarReactButtonView uut = createView(activity, new ComponentOptions());
        RecordingContentView child = new RecordingContentView(activity);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        params.gravity = Gravity.TOP | Gravity.RIGHT;

        uut.addView(child, params);

        FrameLayout.LayoutParams updatedParams = (FrameLayout.LayoutParams) child.getLayoutParams();
        assertThat(updatedParams.gravity & Gravity.VERTICAL_GRAVITY_MASK).isEqualTo(Gravity.CENTER_VERTICAL);
        assertThat(updatedParams.gravity & Gravity.HORIZONTAL_GRAVITY_MASK).isEqualTo(Gravity.RIGHT);
    }

    private TitleBarReactButtonView createView(Activity activity, ComponentOptions component) {
        component.name = new Text("ButtonComponent");
        component.componentId = new Text("ButtonComponentId");
        return new TitleBarReactButtonView(activity, component);
    }

    private void setContentView(TitleBarReactButtonView uut, View child) {
        uut.removeAllViews();
        uut.addView(child, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
    }

    private int resolveActionBarSize(Activity activity) {
        TypedValue tv = new TypedValue();
        if (activity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data, activity.getResources().getDisplayMetrics());
        }
        return UiUtils.dpToPx(activity, 48);
    }

    private static class RecordingContentView extends View {
        final List<Integer> widthMeasureSpecs = new ArrayList<>();
        final List<Integer> heightMeasureSpecs = new ArrayList<>();

        RecordingContentView(Activity activity) {
            super(activity);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            widthMeasureSpecs.add(widthMeasureSpec);
            heightMeasureSpecs.add(heightMeasureSpec);

            int measuredWidth = getMode(widthMeasureSpec) == EXACTLY
                    ? getSize(widthMeasureSpec)
                    : Math.min(CHILD_WIDTH, getSize(widthMeasureSpec));
            int measuredHeight = getMode(heightMeasureSpec) == EXACTLY
                    ? getSize(heightMeasureSpec)
                    : Math.min(CHILD_HEIGHT, getSize(heightMeasureSpec));
            setMeasuredDimension(measuredWidth, measuredHeight);
        }
    }
}
