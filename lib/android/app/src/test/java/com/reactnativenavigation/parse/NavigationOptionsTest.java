package com.reactnativenavigation.parse;

import android.support.annotation.NonNull;

import com.reactnativenavigation.BaseTest;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static com.reactnativenavigation.parse.NavigationOptions.BooleanOptions.True;
import static org.assertj.core.api.Java6Assertions.assertThat;

public class NavigationOptionsTest extends BaseTest {

    private static final String TITLE = "the title";
    private static final int TOP_BAR_BACKGROUND_COLOR = 0xff123456;
    private static final int TOP_BAR_TEXT_COLOR = 0xff123456;
    private static final int TOP_BAR_FONT_SIZE = 18;
    private static final String TOP_BAR_FONT_FAMILY = "HelveticaNeue-CondensedBold";
    private static final NavigationOptions.BooleanOptions TOP_BAR_HIDDEN = True;
    private static final NavigationOptions.BooleanOptions BOTTOM_TABS_ANIMATE_HIDE = True;
    private static final NavigationOptions.BooleanOptions BOTTOM_TABS_HIDDEN = True;
    private static final int BOTTOM_TABS_BADGE = 3;
    private static final String BOTTOM_TABS_CURRENT_TAB_ID = "ContainerId";
    private static final int BOTTOM_TABS_CURRENT_TAB_INDEX = 1;

    @Test
	public void parsesNullAsDefaultEmptyOptions() throws Exception {
		assertThat(NavigationOptions.parse(null)).isNotNull();
	}

	@Test
	public void parsesJson() throws Exception {
		JSONObject json = new JSONObject()
                .put("topBar", createTopBar())
                .put("tabBar", createTabBar());
		NavigationOptions result = NavigationOptions.parse(json);
        assertResult(result);
	}

    private void assertResult(NavigationOptions result) {
        assertThat(result.topBarOptions.title).isEqualTo(TITLE);
        assertThat(result.topBarOptions.backgroundColor).isEqualTo(TOP_BAR_BACKGROUND_COLOR);
        assertThat(result.topBarOptions.textColor).isEqualTo(TOP_BAR_TEXT_COLOR);
        assertThat(result.topBarOptions.textFontSize).isEqualTo(TOP_BAR_FONT_SIZE);
        assertThat(result.topBarOptions.textFontFamily).isEqualTo(TOP_BAR_FONT_FAMILY);
        assertThat(result.topBarOptions.hidden).isEqualTo(TOP_BAR_HIDDEN);
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
            .put("hidden", TOP_BAR_HIDDEN);
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
        json.put("tabBar", createTabBar());
        NavigationOptions defaultOptions = NavigationOptions.parse(json);
        NavigationOptions options = new NavigationOptions();

        options.mergeWith(defaultOptions);
        assertResult(options);
    }

    @Test
    public void mergedDefaultOptionsDontOverrideGivenOptions() throws Exception {
        JSONObject defaultJson = new JSONObject()
            .put("topBar", createOtherTopBar())
            .put("tabBar", createOtherTabBar());
        NavigationOptions defaultOptions = NavigationOptions.parse(defaultJson);

        JSONObject json = new JSONObject()
                .put("topBar", createTopBar())
                .put("tabBar", createTabBar());
        NavigationOptions options = NavigationOptions.parse(json);
        options.withDefaultOptions(defaultOptions);
        assertResult(options);
    }

	@Test
	public void defaultEmptyOptions() throws Exception {
		NavigationOptions uut = new NavigationOptions();
		assertThat(uut.topBarOptions.title).isEmpty();
	}
}
