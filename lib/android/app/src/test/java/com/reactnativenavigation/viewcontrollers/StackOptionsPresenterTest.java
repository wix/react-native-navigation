package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.graphics.Typeface;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.TestComponentLayout;
import com.reactnativenavigation.mocks.TestReactView;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.OrientationOptions;
import com.reactnativenavigation.parse.SubtitleOptions;
import com.reactnativenavigation.parse.TitleOptions;
import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.parse.params.Color;
import com.reactnativenavigation.parse.params.Fraction;
import com.reactnativenavigation.parse.params.Number;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.presentation.StackOptionsPresenter;
import com.reactnativenavigation.views.topbar.TopBar;

import org.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StackOptionsPresenterTest extends BaseTest {

    private StackOptionsPresenter uut;
    private TestComponentLayout child;
    private Activity activity;
    private TopBar topBar;

    @Override
    public void beforeEach() {
        activity = spy(newActivity());
        uut = spy(new StackOptionsPresenter(activity, new Options()));
        topBar = mockTopBar();
        uut.bindView(topBar);
        child = spy(new TestComponentLayout(activity, new TestReactView(activity)));
    }

    @Test
    public void mergeOrientation() throws Exception {
        Options options = new Options();
        uut.mergeChildOptions(options, child);
        verify(uut, times(0)).applyOrientation(any());

        JSONObject orientation = new JSONObject().put("orientation", "landscape");
        options.layout.orientation = OrientationOptions.parse(orientation);
        uut.mergeChildOptions(options, child);
        verify(uut, times(1)).applyOrientation(options.layout.orientation);
    }

    @Test
    public void mergeButtons() {
        Options options = new Options();
        uut.mergeChildOptions(options, child);
        verify(topBar, times(0)).setRightButtons(any());
        verify(topBar, times(0)).setLeftButtons(any());

        options.topBar.buttons.right = new ArrayList<>();
        uut.mergeChildOptions(options, child);
        verify(topBar, times(1)).setRightButtons(any());

        options.topBar.buttons.left = new ArrayList<>();
        uut.mergeChildOptions(options, child);
        verify(topBar, times(1)).setLeftButtons(any());
    }

    @Test
    public void mergeTopBarOptions() {
        Options options = new Options();
        uut.mergeChildOptions(options, child);
        assertTopBarOptions(options, 0);

        TitleOptions title = new TitleOptions();
        title.text = new Text("abc");
        title.component.name = new Text("someComponent");
        title.component.componentId = new Text("compId");
        title.color = new Color(0);
        title.fontSize = new Fraction(1.0f);
        title.fontFamily = Typeface.DEFAULT_BOLD;
        options.topBar.title = title;
        SubtitleOptions subtitleOptions = new SubtitleOptions();
        subtitleOptions.text = new Text("Sub");
        subtitleOptions.color = new Color(1);
        options.topBar.subtitle = subtitleOptions;
        options.topBar.background.color = new Color(0);
        options.topBar.testId = new Text("test123");
        options.topBar.animate = new Bool(false);
        options.topBar.visible = new Bool(false);
        options.topBar.drawBehind = new Bool(false);
        options.topBar.hideOnScroll = new Bool(false);
        uut.mergeChildOptions(options, child);

        assertTopBarOptions(options, 1);

        options.topBar.drawBehind = new Bool(true);
        uut.mergeChildOptions(options, child);
        verify(child, times(1)).drawBehindTopBar();
    }

    @Test
    public void mergeTopTabsOptions() {
        Options options = new Options();
        uut.mergeChildOptions(options, child);
        verify(topBar, times(0)).applyTopTabsColors(any(), any());
        verify(topBar, times(0)).applyTopTabsFontSize(any());
        verify(topBar, times(0)).setTopTabsVisible(anyBoolean());

        options.topTabs.selectedTabColor = new Color(1);
        options.topTabs.unselectedTabColor = new Color(1);
        options.topTabs.fontSize = new Number(1);
        options.topTabs.visible = new Bool(true);
        uut.mergeChildOptions(options, child);
        verify(topBar, times(1)).applyTopTabsColors(options.topTabs.selectedTabColor, options.topTabs.unselectedTabColor);
        verify(topBar, times(1)).applyTopTabsFontSize(options.topTabs.fontSize);
        verify(topBar, times(1)).setTopTabsVisible(anyBoolean());
    }

    @Test
    public void mergeTopTabOptions() {
        Options options = new Options();
        uut.mergeChildOptions(options, child);

        verify(topBar, times(0)).setTopTabFontFamily(anyInt(), any());

        options.topTabOptions.tabIndex = 1;
        options.topTabOptions.fontFamily = Typeface.DEFAULT_BOLD;
        uut.mergeChildOptions(options, child);

        verify(topBar, times(1)).setTopTabFontFamily(1, Typeface.DEFAULT_BOLD);
    }

    private void assertTopBarOptions(Options options, int t) {
        if (options.topBar.title.component.hasValue()) {
            verify(topBar, times(0)).setTitle(any());
            verify(topBar, times(0)).setSubtitle(any());
        } else {
            verify(topBar, times(t)).setTitle(any());
            verify(topBar, times(t)).setSubtitle(any());
        }
        verify(topBar, times(t)).setTitleComponent(any());
        verify(topBar, times(t)).setBackgroundColor(any());
        verify(topBar, times(t)).setTitleTextColor(anyInt());
        verify(topBar, times(t)).setTitleFontSize(anyDouble());
        verify(topBar, times(t)).setTitleTypeface(any());
        verify(topBar, times(t)).setSubtitleColor(anyInt());
        verify(topBar, times(t)).setTestId(any());
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
