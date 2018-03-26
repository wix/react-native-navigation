package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.graphics.Typeface;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.TestComponentLayout;
import com.reactnativenavigation.mocks.TestReactView;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.OrientationOptions;
import com.reactnativenavigation.parse.TitleOptions;
import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.parse.params.Color;
import com.reactnativenavigation.parse.params.Fraction;
import com.reactnativenavigation.parse.params.Number;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.presentation.OptionsPresenter;
import com.reactnativenavigation.views.topbar.TopBar;

import org.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OptionsMergingTest extends BaseTest {

    private OptionsPresenter uut;
    private TestComponentLayout child;
    private Activity activity;
    private TopBar topBar;

    @Override
    public void beforeEach() {
        activity = spy(newActivity());
        topBar = mockTopBar();
        uut = spy(new OptionsPresenter(topBar));
        child = spy(new TestComponentLayout(activity, new TestReactView(activity)));
    }

    @Test
    public void mergeOrientation() throws Exception {
        Options options = new Options();
        uut.mergeChildOptions(options, child);
        verify(uut, times(0)).applyOrientation(any());

        JSONObject orientation = new JSONObject().put("orientation", "landscape");
        options.orientationOptions = OrientationOptions.parse(orientation);
        uut.mergeChildOptions(options, child);
        verify(uut, times(1)).applyOrientation(options.orientationOptions);
    }

    @Test
    public void mergeButtons() throws Exception {
        Options options = new Options();
        uut.mergeChildOptions(options, child);
        verify(topBar, times(0)).setRightButtons(any());
        verify(topBar, times(0)).setLeftButtons(any());

        options.topBarOptions.rightButtons = new ArrayList<>();
        uut.mergeChildOptions(options, child);
        verify(topBar, times(1)).setRightButtons(any());

        options.topBarOptions.leftButtons = new ArrayList<>();
        uut.mergeChildOptions(options, child);
        verify(topBar, times(1)).setLeftButtons(any());
    }

    @Test
    public void mergeTopBarOptions() throws Exception {
        Options options = new Options();
        uut.mergeChildOptions(options, child);
        assertTopBarOptions(0);

        TitleOptions titleOptions = new TitleOptions();
        titleOptions.text = new Text("abc");
        titleOptions.component = new Text("someComponent");
        titleOptions.color = new Color(0);
        titleOptions.fontSize = new Fraction(1.0f);
        titleOptions.fontFamily = Typeface.DEFAULT_BOLD;
        options.topBarOptions.title = titleOptions;
        options.topBarOptions.background.color = new Color(0);
        options.topBarOptions.testId = new Text("test123");
        options.topBarOptions.animate = new Bool(false);
        options.topBarOptions.visible = new Bool(false);
        options.topBarOptions.drawBehind = new Bool(false);
        options.topBarOptions.hideOnScroll = new Bool(false);
        uut.mergeChildOptions(options, child);

        assertTopBarOptions(1);

        options.topBarOptions.drawBehind = new Bool(true);
        uut.mergeChildOptions(options, child);
        verify(child, times(1)).drawBehindTopBar();
    }

    @Test
    public void mergeTopTabsOptions() throws Exception {
        Options options = new Options();
        uut.mergeChildOptions(options, child);
        verify(topBar, times(0)).applyTopTabsColors(any(), any());
        verify(topBar, times(0)).applyTopTabsFontSize(any());
        verify(topBar, times(0)).setTopTabsVisible(anyBoolean());

        options.topTabsOptions.selectedTabColor = new Color(1);
        options.topTabsOptions.unselectedTabColor = new Color(1);
        options.topTabsOptions.fontSize = new Number(1);
        options.topTabsOptions.visible = new Bool(true);
        uut.mergeChildOptions(options, child);
        verify(topBar, times(1)).applyTopTabsColors(options.topTabsOptions.selectedTabColor, options.topTabsOptions.unselectedTabColor);
        verify(topBar, times(1)).applyTopTabsFontSize(options.topTabsOptions.fontSize);
        verify(topBar, times(1)).setTopTabsVisible(anyBoolean());
    }

    @Test
    public void mergeTopTabOptions() throws Exception {
        Options options = new Options();
        uut.mergeChildOptions(options, child);

        verify(topBar, times(0)).setTopTabFontFamily(anyInt(), any());

        options.topTabOptions.tabIndex = 1;
        options.topTabOptions.fontFamily = Typeface.DEFAULT_BOLD;
        uut.mergeChildOptions(options, child);

        verify(topBar, times(1)).setTopTabFontFamily(1, Typeface.DEFAULT_BOLD);
    }


    private void assertTopBarOptions(int t) {
        verify(topBar, times(t)).setTitle(any());
        verify(topBar, times(t)).setTitleComponent(any(), any());
        verify(topBar, times(t)).setBackgroundColor(any());
        verify(topBar, times(t)).setTitleTextColor(any());
        verify(topBar, times(t)).setTitleFontSize(any());
        verify(topBar, times(t)).setTestId(any());
        verify(topBar, times(t)).setTitleTypeface(any());
        verify(topBar, times(t)).hide();
        verify(child, times(t)).drawBelowTopBar(topBar);
        verify(child, times(0)).drawBehindTopBar();
    }

    private TopBar mockTopBar() {
        TopBar topBar = mock(TopBar.class);
        when(topBar.getContext()).then(invocation -> activity);
        return topBar;
    }
}
