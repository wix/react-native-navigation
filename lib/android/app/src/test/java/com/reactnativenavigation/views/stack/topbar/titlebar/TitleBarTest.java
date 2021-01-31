package com.reactnativenavigation.views.stack.topbar.titlebar;

import android.app.Activity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.TestUtils;
import com.reactnativenavigation.utils.UiUtils;

import org.junit.Test;

import androidx.appcompat.widget.ActionMenuView;
import androidx.appcompat.widget.Toolbar;

import static com.reactnativenavigation.utils.Assertions.assertNotNull;
import static com.reactnativenavigation.utils.ViewUtils.findChildByClass;
import static com.reactnativenavigation.views.stack.topbar.titlebar.TitleBar.DEFAULT_LEFT_MARGIN;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TitleBarTest extends BaseTest {
    private static final int UUT_WIDTH = 1000;
    private static final int UUT_HEIGHT = 100;
    private static final int PARENT_WIDTH = 1200;
    private static final int PARENT_HEIGHT = 100;
    private static final int COMPONENT_WIDTH = 100;
    private static final int COMPONENT_HEIGHT = 40;

    private TitleBar uut;
    private Activity activity;

    @Override
    public void beforeEach() {
        activity = newActivity();
        uut = spy(new TitleBar(activity));
    }

    @Test
    public void onLayout_alignsTitleOnLayout() {
        uut.setTitle("Title");
        TextView title = new TextView(activity);
        uut.addView(title);
        when(uut.findTitleTextView()).thenReturn(title);

        uut.onLayout(true, 0, 0, UUT_WIDTH, UUT_HEIGHT);
        verify(uut).alignTextView(any(), eq(title));
    }

    @Test
    public void setComponent_addsComponentToTitleBar() {
        View component = new View(activity);
        uut.setComponent(component);
        verify(uut).addView(component);
    }

    @Test
    public void setComponent_doesNothingIfComponentIsAlreadyAdded() {
        View component = new View(activity);
        uut.setComponent(component);

        uut.setComponent(component);
        verify(uut).addView(component);
    }

    @Test
    public void setComponent_alignedStartAfterMeasureWithDefaultMargin() {
        uut.layout(0, 0, UUT_WIDTH, UUT_HEIGHT);

        View component = new View(activity);
        component.layout(0, 0, COMPONENT_WIDTH, COMPONENT_HEIGHT);
        component.setLayoutParams(new Toolbar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.START));
        uut.setComponent(component);
        uut.onLayout(true, 0, 0, 0, 0);
        int defaultLeftMarginPx = UiUtils.dpToPx(activity, DEFAULT_LEFT_MARGIN);
        assertThat(component.getX()).isEqualTo(defaultLeftMarginPx);
    }

    @Test
    public void setComponent_alignedStartRTLAfterMeasureWithDefaultMargin() {
        ViewGroup parent = new FrameLayout(activity);
        parent.layout(0, 0, UUT_WIDTH, UUT_HEIGHT);
        when(uut.getParent()).thenReturn(parent);
        uut.layout(0, 0, UUT_WIDTH, UUT_HEIGHT);
        when(uut.getLayoutDirection()).thenReturn(View.LAYOUT_DIRECTION_RTL);
        View component = new View(activity);
        component.layout(0, 0, COMPONENT_WIDTH, COMPONENT_HEIGHT);
        component.setLayoutParams(new Toolbar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.START));
        uut.setComponent(component);
        uut.onLayout(true, 0, 0, 0, 0);
        int defaultLeftMarginPx = UiUtils.dpToPx(activity, DEFAULT_LEFT_MARGIN);
        assertThat(component.getX()).isEqualTo(UUT_WIDTH - COMPONENT_WIDTH - defaultLeftMarginPx);
    }

    @Test
    public void setComponent_alignedCenterAfterMeasure() {
        uut.layout(0, 0, UUT_WIDTH, UUT_HEIGHT);
        View component = new View(activity);
        component.layout(0, 0, COMPONENT_WIDTH, COMPONENT_HEIGHT);
        component.setLayoutParams(new Toolbar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        uut.setComponent(component);
        uut.onLayout(true, 0, 0, 0, 0);
        assertThat(component.getX()).isEqualTo((UUT_WIDTH - COMPONENT_WIDTH) / 2f);
    }

    @Test
    public void onLayout_centersComponent_ltr() {
        uut.layout(0, 0, UUT_WIDTH, UUT_HEIGHT);

        View component = new View(activity);
        component.layout(0, 0, COMPONENT_WIDTH, COMPONENT_HEIGHT);
        uut.setComponent(component);
        ((Toolbar.LayoutParams) component.getLayoutParams()).gravity = Gravity.CENTER;

        uut.onLayout(true, 0, 0, 0, 0);
        assertThat(component.getX()).isEqualTo((UUT_WIDTH - COMPONENT_WIDTH) / 2f);
    }

    @Test
    public void onLayout_centersComponent_rtl() {
        ViewGroup parent = new FrameLayout(activity);
        parent.layout(0, 0, PARENT_WIDTH, PARENT_WIDTH);
        parent.addView(uut);

        when(uut.getLayoutDirection()).thenReturn(View.LAYOUT_DIRECTION_RTL);
        uut.layout(0, 0, UUT_WIDTH, UUT_HEIGHT);

        View component = new View(activity);
        uut.setComponent(component);
        ((Toolbar.LayoutParams) component.getLayoutParams()).gravity = Gravity.CENTER;

        uut.onLayout(true, 0, 0, 0, 0);
        component.layout(0, 0, COMPONENT_WIDTH, COMPONENT_HEIGHT);
        dispatchOnGlobalLayout(component);
        assertThat(component.getX()).isEqualTo((UUT_WIDTH - COMPONENT_WIDTH + (PARENT_WIDTH - UUT_WIDTH)) / 2f);
    }

    @Test
    public void addView_overflowIsEnabledForButtonsContainer() {
        ActionMenuView buttonsContainer = mock(ActionMenuView.class);
        uut.addView(buttonsContainer);
        verify(buttonsContainer).setClipChildren(false);
    }

    @Test
    public void clear() {
        View title = new View(activity);
        uut.setComponent(title);
        verify(uut).addView(title);

        uut.clear();
        assertThat(uut.getTitle()).isNullOrEmpty();
        assertThat(uut.getMenu().size()).isZero();
        assertThat(uut.getNavigationIcon()).isNull();
        assertThat(title.getParent()).isNull();
    }

    @Test
    public void setLayoutDirection_directionIsExplicitlyAppliedToButtonsContainer() {
        ActionMenuView buttonsContainer = findChildByClass(uut, ActionMenuView.class);
        assertNotNull(buttonsContainer);
        ActionMenuView spy = TestUtils.spyOn(buttonsContainer);
        uut.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        verify(spy).setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
    }

    @Test
    public void setSubtitleFontSize_usesDpInsteadofSp() {
        TextView mockSubtitleView = mock(TextView.class);

        when(uut.findSubtitleTextView()).thenReturn(mockSubtitleView);
        uut.setSubtitleFontSize(10);

        verify(mockSubtitleView).setTextSize(eq(TypedValue.COMPLEX_UNIT_DIP), eq(10f));
    }

    @Test
    public void setTitleFontSize_usesDpInsteadofSp() {
        TextView mockTitleView = mock(TextView.class);

        when(uut.findTitleTextView()).thenReturn(mockTitleView);
        uut.setTitleFontSize(10);

        verify(mockTitleView).setTextSize(eq(TypedValue.COMPLEX_UNIT_DIP), eq(10f));
    }
}
