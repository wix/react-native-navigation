package com.reactnativenavigation.parse;

import android.graphics.*;
import android.support.annotation.*;

import com.reactnativenavigation.*;
import com.reactnativenavigation.mocks.*;
import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.utils.*;

import org.json.*;
import org.junit.*;
import org.mockito.*;

import static org.assertj.core.api.Java6Assertions.*;

public class OptionsTest extends BaseTest {

    private static final String TITLE = "the title";
    private static final int TOP_BAR_BACKGROUND_COLOR = 0xff123456;
    private static final int TOP_BAR_TEXT_COLOR = 0xff123456;
    private static final int TOP_BAR_FONT_SIZE = 18;
    private static final String TOP_BAR_FONT_FAMILY = "HelveticaNeue-CondensedBold";
    private static final Typeface TOP_BAR_TYPEFACE = Typeface.create("HelveticaNeue-CondensedBold", Typeface.BOLD);
    private static final Bool TOP_BAR_HIDDEN = new Bool(true);
    private static final Bool TOP_BAR_DRAW_BEHIND = new Bool(true);
    private static final Bool TOP_BAR_HIDE_ON_SCROLL = new Bool(true);
    private static final Bool BOTTOM_TABS_ANIMATE_HIDE = new Bool(true);
    private static final Bool BOTTOM_TABS_HIDDEN = new Bool(true);
    private static final String BOTTOM_TABS_BADGE = "3";
    private static final String BOTTOM_TABS_CURRENT_TAB_ID = "ComponentId";
    private static final int BOTTOM_TABS_CURRENT_TAB_INDEX = 1;
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
                .put("topBar", createTopBar(TOP_BAR_HIDDEN.get()))
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
        assertThat(result.topBarOptions.hidden.get()).isEqualTo(TOP_BAR_HIDDEN.get());
        assertThat(result.topBarOptions.drawBehind.get()).isEqualTo(TOP_BAR_DRAW_BEHIND.get());
        assertThat(result.topBarOptions.hideOnScroll.get()).isEqualTo(TOP_BAR_HIDE_ON_SCROLL.get());
        assertThat(result.bottomTabsOptions.animateHide.get()).isEqualTo(BOTTOM_TABS_ANIMATE_HIDE.get());
        assertThat(result.bottomTabsOptions.visible.get()).isEqualTo(BOTTOM_TABS_HIDDEN.get());
        assertThat(result.bottomTabsOptions.currentTabId.get()).isEqualTo(BOTTOM_TABS_CURRENT_TAB_ID);
        assertThat(result.bottomTabsOptions.currentTabIndex).isEqualTo(BOTTOM_TABS_CURRENT_TAB_INDEX);
    }

    @NonNull
    private JSONObject createBottomTabs() throws JSONException {
        return new JSONObject()
                .put("currentTabId", BOTTOM_TABS_CURRENT_TAB_ID)
                .put("currentTabIndex", BOTTOM_TABS_CURRENT_TAB_INDEX)
                .put("visible", BOTTOM_TABS_HIDDEN.get())
                .put("animateHide", BOTTOM_TABS_ANIMATE_HIDE.get());
    }

    @NonNull
    private JSONObject createTopBar(boolean hidden) throws JSONException {
        return new JSONObject()
                .put("title", "the title")
                .put("backgroundColor", TOP_BAR_BACKGROUND_COLOR)
                .put("textColor", TOP_BAR_TEXT_COLOR)
                .put("textFontSize", TOP_BAR_FONT_SIZE)
                .put("textFontFamily", TOP_BAR_FONT_FAMILY)
                .put("hidden", hidden)
                .put("drawBehind", TOP_BAR_DRAW_BEHIND.get())
                .put("hideOnScroll", TOP_BAR_HIDE_ON_SCROLL.get());
    }

    @NonNull
    private JSONObject createOtherTopBar() throws JSONException {
        return new JSONObject()
                .put("title", "the title")
                .put("backgroundColor", TOP_BAR_BACKGROUND_COLOR)
                .put("textColor", TOP_BAR_TEXT_COLOR)
                .put("textFontSize", TOP_BAR_FONT_SIZE)
                .put("textFontFamily", TOP_BAR_FONT_FAMILY)
                .put("visible", TOP_BAR_HIDDEN);
    }

    @NonNull
    private JSONObject createOtherTabBar() throws JSONException {
        return new JSONObject()
                .put("currentTabId", BOTTOM_TABS_CURRENT_TAB_ID)
                .put("currentTabIndex", BOTTOM_TABS_CURRENT_TAB_INDEX)
                .put("visible", BOTTOM_TABS_HIDDEN)
                .put("animateHide", BOTTOM_TABS_ANIMATE_HIDE)
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
        assertThat(options1.topBarOptions.hidden.get()).isTrue();
        assertThat(merged.topBarOptions.hidden.get()).isFalse();
        assertThat(merged.topBarOptions.title.get()).isEqualTo("some title");
    }

    @Test
    public void mergeDefaultOptions() throws Exception {
        JSONObject json = new JSONObject();
        json.put("topBar", createTopBar(TOP_BAR_HIDDEN.get()));
        json.put("bottomTabs", createBottomTabs());
        Options defaultOptions = Options.parse(mockLoader, json);
        Options options = new Options();

        assertResult(options.mergeWith(defaultOptions));
    }

    @Test
    public void mergedDefaultOptionsDontOverrideGivenOptions() throws Exception {
        JSONObject defaultJson = new JSONObject()
                .put("topBar", createOtherTopBar())
                .put("bottomTabs", createOtherTabBar());
        Options defaultOptions = Options.parse(mockLoader, defaultJson);

        JSONObject json = new JSONObject()
                .put("topBar", createTopBar(TOP_BAR_HIDDEN.get()))
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
        assertThat(uut.topBarOptions.hidden.isFalseOrUndefined()).isTrue();
        assertThat(uut.topBarOptions.animateHide.isTrueOrUndefined()).isTrue();
    }
}
