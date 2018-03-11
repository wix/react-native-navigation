package com.reactnativenavigation.parse;

import android.graphics.*;
import android.support.annotation.*;

import com.reactnativenavigation.*;
import com.reactnativenavigation.mocks.*;
import com.reactnativenavigation.parse.params.*;
import com.reactnativenavigation.parse.params.Number;
import com.reactnativenavigation.utils.*;

import org.json.*;
import org.junit.*;
import org.mockito.*;

import static org.assertj.core.api.Java6Assertions.*;

public class OptionsTest extends BaseTest {

    private static final String TITLE = "the title";
    private static final String FAB_ID = "FAB";
    private static final String FAB_ALIGN_HORIZONTALLY = "right";
    private static final String FAB_ALIGN_VERTICALLY = "bottom";
    private static final int TOP_BAR_BACKGROUND_COLOR = 0xff123456;
    private static final int FAB_BACKGROUND_COLOR = android.graphics.Color.BLUE;
    private static final int FAB_CLICK_COLOR = android.graphics.Color.RED;
    private static final int FAB_RIPPLE_COLOR = android.graphics.Color.GREEN;
    private static final Boolean FAB_VISIBLE = true;
    private static final Boolean FAB_HIDE_ON_SCROLL = true;
    private static final int TOP_BAR_TEXT_COLOR = 0xff123456;
    private static final int TOP_BAR_FONT_SIZE = 18;
    private static final String TOP_BAR_FONT_FAMILY = "HelveticaNeue-CondensedBold";
    private static final Typeface TOP_BAR_TYPEFACE = Typeface.create("HelveticaNeue-CondensedBold", Typeface.BOLD);
    private static final Bool TOP_BAR_VISIBLE = new Bool(true);
    private static final Bool TOP_BAR_DRAW_BEHIND = new Bool(true);
    private static final Bool TOP_BAR_HIDE_ON_SCROLL = new Bool(true);
    private static final Bool BOTTOM_TABS_ANIMATE = new Bool(true);
    private static final Bool BOTTOM_TABS_VISIBLE = new Bool(true);
    private static final String BOTTOM_TABS_BADGE = "3";
    private static final String BOTTOM_TABS_CURRENT_TAB_ID = "ComponentId";
    private static final Number BOTTOM_TABS_CURRENT_TAB_INDEX = new Number(1);
    private TypefaceLoader mockLoader;

    @Override
    public void beforeEach() {
        mockLoader = Mockito.mock(TypefaceLoaderMock.class);
        Mockito.doReturn(TOP_BAR_TYPEFACE).when(mockLoader).getTypeFace(TOP_BAR_FONT_FAMILY);
    }

    @Test
    public void parsesNullAsDefaultEmptyOptions() throws Exception {
        assertThat(Options.parse(mockLoader, null)).isNotNull();
    }

    @Test
    public void parsesJson() throws Exception {
        JSONObject json = new JSONObject()
                .put("topBar", createTopBar(TOP_BAR_VISIBLE.get()))
                .put("fab", createFab())
                .put("bottomTabs", createBottomTabs());
        Options result = Options.parse(mockLoader, json);
        assertResult(result);
    }

    private void assertResult(Options result) {
        assertThat(result.topBarOptions.title.get()).isEqualTo(TITLE);
        assertThat(result.topBarOptions.backgroundColor.get()).isEqualTo(TOP_BAR_BACKGROUND_COLOR);
        assertThat(result.topBarOptions.textColor.get()).isEqualTo(TOP_BAR_TEXT_COLOR);
        assertThat(result.topBarOptions.textFontSize.get()).isEqualTo(TOP_BAR_FONT_SIZE);
        assertThat(result.topBarOptions.textFontFamily).isEqualTo(TOP_BAR_TYPEFACE);
        assertThat(result.topBarOptions.visible.get()).isEqualTo(TOP_BAR_VISIBLE.get());
        assertThat(result.topBarOptions.drawBehind.get()).isEqualTo(TOP_BAR_DRAW_BEHIND.get());
        assertThat(result.topBarOptions.hideOnScroll.get()).isEqualTo(TOP_BAR_HIDE_ON_SCROLL.get());
        assertThat(result.bottomTabsOptions.animate.get()).isEqualTo(BOTTOM_TABS_ANIMATE.get());
        assertThat(result.bottomTabsOptions.visible.get()).isEqualTo(BOTTOM_TABS_VISIBLE.get());
        assertThat(result.bottomTabsOptions.currentTabId.get()).isEqualTo(BOTTOM_TABS_CURRENT_TAB_ID);
        assertThat(result.bottomTabsOptions.currentTabIndex.get()).isEqualTo(BOTTOM_TABS_CURRENT_TAB_INDEX.get());
        assertThat(result.fabOptions.id.get()).isEqualTo(FAB_ID);
        assertThat(result.fabOptions.backgroundColor.get()).isEqualTo(FAB_BACKGROUND_COLOR);
        assertThat(result.fabOptions.clickColor.get()).isEqualTo(FAB_CLICK_COLOR);
        assertThat(result.fabOptions.rippleColor.get()).isEqualTo(FAB_RIPPLE_COLOR);
        assertThat(result.fabOptions.visible.get()).isEqualTo(FAB_VISIBLE);
        assertThat(result.fabOptions.hideOnScroll.get()).isEqualTo(FAB_HIDE_ON_SCROLL);
        assertThat(result.fabOptions.alignVertically.get()).isEqualTo(FAB_ALIGN_VERTICALLY);
        assertThat(result.fabOptions.alignHorizontally.get()).isEqualTo(FAB_ALIGN_HORIZONTALLY);
    }

    @NonNull
    private JSONObject createBottomTabs() throws JSONException {
        return new JSONObject()
                .put("currentTabId", BOTTOM_TABS_CURRENT_TAB_ID)
                .put("currentTabIndex", BOTTOM_TABS_CURRENT_TAB_INDEX.get())
                .put("visible", BOTTOM_TABS_VISIBLE.get())
                .put("animate", BOTTOM_TABS_ANIMATE.get());
    }

    @NonNull
    private JSONObject createTopBar(boolean visible) throws JSONException {
        return new JSONObject()
                .put("title", "the title")
                .put("backgroundColor", TOP_BAR_BACKGROUND_COLOR)
                .put("textColor", TOP_BAR_TEXT_COLOR)
                .put("textFontSize", TOP_BAR_FONT_SIZE)
                .put("textFontFamily", TOP_BAR_FONT_FAMILY)
                .put("visible", visible)
                .put("drawBehind", TOP_BAR_DRAW_BEHIND.get())
                .put("hideOnScroll", TOP_BAR_HIDE_ON_SCROLL.get());
    }

    @NonNull
    private JSONObject createFab() throws JSONException {
        return new JSONObject()
                .put("id", FAB_ID)
                .put("backgroundColor", FAB_BACKGROUND_COLOR)
                .put("clickColor", FAB_CLICK_COLOR)
                .put("rippleColor", FAB_RIPPLE_COLOR)
                .put("alignHorizontally", FAB_ALIGN_HORIZONTALLY)
                .put("alignVertically", FAB_ALIGN_VERTICALLY)
                .put("hideOnScroll", FAB_HIDE_ON_SCROLL)
                .put("visible", FAB_VISIBLE);
    }

    @NonNull
    private JSONObject createOtherFab() throws JSONException {
        return new JSONObject()
                .put("id", "FAB")
                .put("backgroundColor", FAB_BACKGROUND_COLOR)
                .put("clickColor", FAB_CLICK_COLOR)
                .put("rippleColor", FAB_RIPPLE_COLOR)
                .put("alignHorizontally", FAB_ALIGN_HORIZONTALLY)
                .put("alignVertically", FAB_ALIGN_VERTICALLY)
                .put("hideOnScroll", FAB_HIDE_ON_SCROLL)
                .put("visible", FAB_VISIBLE);
    }

    @NonNull
    private JSONObject createOtherTopBar() throws JSONException {
        return new JSONObject()
                .put("title", "the title")
                .put("backgroundColor", TOP_BAR_BACKGROUND_COLOR)
                .put("textColor", TOP_BAR_TEXT_COLOR)
                .put("textFontSize", TOP_BAR_FONT_SIZE)
                .put("textFontFamily", TOP_BAR_FONT_FAMILY)
                .put("visible", TOP_BAR_VISIBLE);
    }

    @NonNull
    private JSONObject createOtherBottomTabs() throws JSONException {
        return new JSONObject()
                .put("currentTabId", BOTTOM_TABS_CURRENT_TAB_ID)
                .put("currentTabIndex", BOTTOM_TABS_CURRENT_TAB_INDEX)
                .put("visible", BOTTOM_TABS_VISIBLE)
                .put("animate", BOTTOM_TABS_ANIMATE.get())
                .put("tabBadge", BOTTOM_TABS_BADGE);
    }

    @Test
    public void mergeDoesNotMutate() throws Exception {
        JSONObject json1 = new JSONObject();
        json1.put("topBar", createTopBar(true));
        Options options1 = Options.parse(mockLoader, json1);
        options1.topBarOptions.title = new Text("some title");

        JSONObject json2 = new JSONObject();
        json2.put("topBar", createTopBar(false));
        Options options2 = Options.parse(mockLoader, json2);
        options2.topBarOptions.title = new NullText();

        Options merged = options1.mergeWith(options2);
        assertThat(options1.topBarOptions.visible.get()).isTrue();
        assertThat(merged.topBarOptions.visible.get()).isFalse();
        assertThat(merged.topBarOptions.title.get()).isEqualTo("some title");
    }

    @Test
    public void mergeDefaultOptions() throws Exception {
        JSONObject json = new JSONObject()
                .put("topBar", createTopBar(TOP_BAR_VISIBLE.get()))
                .put("fab", createFab())
                .put("bottomTabs", createBottomTabs());
        Options defaultOptions = Options.parse(mockLoader, json);
        Options options = new Options();

        assertResult(options.mergeWith(defaultOptions));
    }

    @Test
    public void mergedDefaultOptionsDontOverrideGivenOptions() throws Exception {
        JSONObject defaultJson = new JSONObject()
                .put("topBar", createOtherTopBar())
                .put("fab", createOtherFab())
                .put("bottomTabs", createOtherBottomTabs());
        Options defaultOptions = Options.parse(mockLoader, defaultJson);

        JSONObject json = new JSONObject()
                .put("topBar", createTopBar(TOP_BAR_VISIBLE.get()))
                .put("bottomTabs", createBottomTabs());
        Options options = Options.parse(mockLoader, json);
        options.withDefaultOptions(defaultOptions);
        assertResult(options);
    }

    @Test
    public void defaultEmptyOptions() throws Exception {
        Options uut = new Options();
        assertThat(uut.topBarOptions.title.get("")).isEmpty();
    }

    @Test
    public void topBar_defaultOptions() throws Exception {
        Options uut = new Options();
        assertThat(uut.topBarOptions.visible.isFalseOrUndefined()).isTrue();
        assertThat(uut.topBarOptions.animate.isTrueOrUndefined()).isTrue();
    }

    @Test
    public void clear_topBarOptions() throws Exception {
        Options uut = new Options();
        uut.topBarOptions.title = new Text("some title");
        uut.clearTopBarOptions();
        assertThat(uut.topBarOptions.title.hasValue()).isFalse();
    }

    @Test
    public void clear_bottomTabsOptions() throws Exception {
        Options uut = new Options();
        uut.bottomTabsOptions.tabColor = new com.reactnativenavigation.parse.params.Color(android.graphics.Color.RED);
        uut.clearBottomTabsOptions();
        assertThat(uut.bottomTabsOptions.tabColor.hasValue()).isFalse();
    }

    @Test
    public void clear_topTabsOptions() throws Exception {
        Options uut = new Options();
        uut.topTabsOptions.fontSize = new Number(666);
        uut.clearTopTabsOptions();
        assertThat(uut.topTabsOptions.fontSize.hasValue()).isFalse();
    }

    @Test
    public void clear_topTabOptions() throws Exception {
        Options uut = new Options();
        uut.topTabOptions.title = new Text("some title");
        uut.clearTopTabOptions();
        assertThat(uut.topTabOptions.title.hasValue()).isFalse();
    }
}
