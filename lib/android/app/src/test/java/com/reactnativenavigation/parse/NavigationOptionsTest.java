package com.reactnativenavigation.parse;

import android.graphics.*;
import android.support.annotation.*;

import com.reactnativenavigation.*;
import com.reactnativenavigation.mocks.*;
import com.reactnativenavigation.utils.*;

import org.json.*;
import org.junit.*;
import org.mockito.*;

import static com.reactnativenavigation.parse.Options.BooleanOptions.*;
import static org.assertj.core.api.Java6Assertions.*;

public class NavigationOptionsTest extends BaseTest {

    private static final String TITLE = "the title";
    private static final int TOP_BAR_BACKGROUND_COLOR = 0xff123456;
    private static final int TOP_BAR_TEXT_COLOR = 0xff123456;
    private static final int TOP_BAR_FONT_SIZE = 18;
    private static final String TOP_BAR_FONT_FAMILY = "HelveticaNeue-CondensedBold";
    private static final Typeface TOP_BAR_TYPEFACE = Typeface.create("HelveticaNeue-CondensedBold", Typeface.BOLD);
    private static final Options.BooleanOptions TOP_BAR_HIDDEN = True;
    private static final Options.BooleanOptions TOP_BAR_DRAW_BEHIND = True;
    private static final Options.BooleanOptions TOP_BAR_HIDE_ON_SCROLL = True;
    private static final Options.BooleanOptions BOTTOM_TABS_ANIMATE_HIDE = True;
    private static final Options.BooleanOptions BOTTOM_TABS_HIDDEN = True;
    private static final int BOTTOM_TABS_BADGE = 3;
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
                .put("topBar", createTopBar())
                .put("bottomTabs", createTabBar());
        Options result = Options.parse(mockLoader, json);
        assertResult(result);
    }

    private void assertResult(Options result) {
        assertThat(result.topBarOptions.title).isEqualTo(TITLE);
        assertThat(result.topBarOptions.backgroundColor).isEqualTo(TOP_BAR_BACKGROUND_COLOR);
        assertThat(result.topBarOptions.textColor).isEqualTo(TOP_BAR_TEXT_COLOR);
        assertThat(result.topBarOptions.textFontSize).isEqualTo(TOP_BAR_FONT_SIZE);
        assertThat(result.topBarOptions.textFontFamily).isEqualTo(TOP_BAR_TYPEFACE);
        assertThat(result.topBarOptions.hidden).isEqualTo(TOP_BAR_HIDDEN);
        assertThat(result.topBarOptions.drawBehind).isEqualTo(TOP_BAR_DRAW_BEHIND);
        assertThat(result.topBarOptions.hideOnScroll).isEqualTo(TOP_BAR_HIDE_ON_SCROLL);
        assertThat(result.bottomTabsOptions.animateHide).isEqualTo(BOTTOM_TABS_ANIMATE_HIDE);
        assertThat(result.bottomTabsOptions.hidden).isEqualTo(BOTTOM_TABS_HIDDEN);
        assertThat(result.bottomTabsOptions.tabBadge).isEqualTo(BOTTOM_TABS_BADGE);
        assertThat(result.bottomTabsOptions.currentTabId).isEqualTo(BOTTOM_TABS_CURRENT_TAB_ID);
        assertThat(result.bottomTabsOptions.currentTabIndex).isEqualTo(BOTTOM_TABS_CURRENT_TAB_INDEX);
    }

    @NonNull
    private JSONObject createTabBar() throws JSONException {
        return new JSONObject()
                .put("currentTabId", BOTTOM_TABS_CURRENT_TAB_ID)
                .put("currentTabIndex", BOTTOM_TABS_CURRENT_TAB_INDEX)
                .put("hidden", BOTTOM_TABS_HIDDEN)
                .put("animateHide", BOTTOM_TABS_ANIMATE_HIDE)
                .put("tabBadge", BOTTOM_TABS_BADGE);
    }

    @NonNull
    private JSONObject createTopBar() throws JSONException {
        return new JSONObject()
                .put("title", TITLE)
                .put("backgroundColor", TOP_BAR_BACKGROUND_COLOR)
                .put("textColor", TOP_BAR_TEXT_COLOR)
                .put("textFontSize", TOP_BAR_FONT_SIZE)
                .put("textFontFamily", TOP_BAR_FONT_FAMILY)
                .put("hidden", TOP_BAR_HIDDEN)
                .put("drawBehind", TOP_BAR_DRAW_BEHIND)
                .put("hideOnScroll", TOP_BAR_HIDE_ON_SCROLL);
    }

    @NonNull
    private JSONObject createOtherTopBar() throws JSONException {
        return new JSONObject()
                .put("title", TITLE)
                .put("backgroundColor", TOP_BAR_BACKGROUND_COLOR)
                .put("textColor", TOP_BAR_TEXT_COLOR)
                .put("textFontSize", TOP_BAR_FONT_SIZE)
                .put("textFontFamily", TOP_BAR_FONT_FAMILY)
                .put("hidden", TOP_BAR_HIDDEN);
    }

    @NonNull
    private JSONObject createOtherTabBar() throws JSONException {
        return new JSONObject()
                .put("currentTabId", BOTTOM_TABS_CURRENT_TAB_ID)
                .put("currentTabIndex", BOTTOM_TABS_CURRENT_TAB_INDEX)
                .put("hidden", BOTTOM_TABS_HIDDEN)
                .put("animateHide", BOTTOM_TABS_ANIMATE_HIDE)
                .put("tabBadge", BOTTOM_TABS_BADGE);
    }

    @Test
    public void mergeDefaultOptions() throws Exception {
        JSONObject json = new JSONObject();
        json.put("topBar", createTopBar());
        json.put("bottomTabs", createTabBar());
        Options defaultOptions = Options.parse(mockLoader, json);
        Options options = new Options();

        options.mergeWith(defaultOptions);
        assertResult(options);
    }

    @Test
    public void mergedDefaultOptionsDontOverrideGivenOptions() throws Exception {
        JSONObject defaultJson = new JSONObject()
                .put("topBar", createOtherTopBar())
                .put("bottomTabs", createOtherTabBar());
        Options defaultOptions = Options.parse(mockLoader, defaultJson);

        JSONObject json = new JSONObject()
                .put("topBar", createTopBar())
                .put("bottomTabs", createTabBar());
        Options options = Options.parse(mockLoader, json);
        options.withDefaultOptions(defaultOptions);
        assertResult(options);
    }

    @Test
    public void defaultEmptyOptions() throws Exception {
        Options uut = new Options();
        assertThat(uut.topBarOptions.title).isEmpty();
    }
}
