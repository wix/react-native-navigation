package com.reactnativenavigation.options;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.options.params.Bool;
import com.reactnativenavigation.options.params.Colour;
import com.reactnativenavigation.options.params.NullText;
import com.reactnativenavigation.options.params.Number;
import com.reactnativenavigation.options.params.ThemeColour;
import com.reactnativenavigation.options.params.Text;
import com.reactnativenavigation.options.parsers.TypefaceLoader;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import androidx.annotation.NonNull;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.when;

public class OptionsTest extends BaseTest {

    private static final String TITLE = "the title";
    private static final Number TITLE_HEIGHT = new Number(100);
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
    private static final String BACK_BUTTON_ACCESSIBILITY_LABEL = "Go Back My Accessibility Label";
    private static final int SUBTITLE_FONT_SIZE = 14;
    private static final int SUBTITLE_TEXT_COLOR = 0xff123457;
    private static final int SCREEN_BACKGROUND_COLOR = 0xff123458;
    private static final String SUBTITLE_FONT_FAMILY = "HelveticaNeue-Condensed";
    private static final Typeface SUBTITLE_TYPEFACE = Typeface.create("HelveticaNeue-Condensed", Typeface.NORMAL);
    private static final String SUBTITLE_ALIGNMENT = "center";
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
    private Activity activity;

    @Override
    public void beforeEach() {
        super.beforeEach();
        activity = newActivity();
        mockLoader = Mockito.mock(TypefaceLoader.class);
        when(mockLoader.getTypeFace("HelveticaNeue-Condensed", null, null, null)).then((Answer<Typeface>) invocation -> SUBTITLE_TYPEFACE);
        when(mockLoader.getTypeFace("HelveticaNeue-CondensedBold", null, null, null)).then((Answer<Typeface>) invocation -> TOP_BAR_TYPEFACE);
        Mockito.doReturn(TOP_BAR_TYPEFACE).when(mockLoader).getTypeFace(TOP_BAR_FONT_FAMILY, "", "", null);
        Mockito.doReturn(SUBTITLE_TYPEFACE).when(mockLoader).getTypeFace(SUBTITLE_FONT_FAMILY, "", "", null);
    }

    @Test
    public void parsesNullAsDefaultEmptyOptions() {
        assertThat(Options.parse(null, mockLoader, null)).isNotNull();
    }

    @Test
    public void parsesJson() throws Exception {
        JSONObject layout = new JSONObject()
                .put("backgroundColor", createColor(SCREEN_BACKGROUND_COLOR,SCREEN_BACKGROUND_COLOR));
        JSONObject json = new JSONObject()
                .put("topBar", createTopBar(TOP_BAR_VISIBLE.get()))
                .put("fab", createFab())
                .put("bottomTabs", createBottomTabs())
                .put("layout", layout);
        Options result = Options.parse(activity, mockLoader, json);
        assertResult(result);
    }

    private void assertResult(Options result) {
        assertThat(result.topBar.title.text.get()).isEqualTo(TITLE);
        assertThat(result.topBar.background.color.get()).isEqualTo(TOP_BAR_BACKGROUND_COLOR);
        assertThat(result.topBar.title.height.get()).isEqualTo(TITLE_HEIGHT.get());
        assertThat(result.topBar.title.color.get()).isEqualTo(TOP_BAR_TEXT_COLOR);
        assertThat(result.topBar.title.fontSize.get()).isEqualTo(TOP_BAR_FONT_SIZE);
        assertThat(result.topBar.title.font.getTypeface(mockLoader)).isEqualTo(TOP_BAR_TYPEFACE);
        assertThat(result.topBar.subtitle.color.get()).isEqualTo(SUBTITLE_TEXT_COLOR);
        assertThat(result.topBar.subtitle.fontSize.get()).isEqualTo(SUBTITLE_FONT_SIZE);
        assertThat(result.topBar.subtitle.alignment).isEqualTo(Alignment.fromString(SUBTITLE_ALIGNMENT));
        assertThat(result.topBar.subtitle.font.getTypeface(mockLoader)).isEqualTo(SUBTITLE_TYPEFACE);
        assertThat(result.topBar.visible.get()).isEqualTo(TOP_BAR_VISIBLE.get());
        assertThat(result.topBar.drawBehind.get()).isEqualTo(TOP_BAR_DRAW_BEHIND.get());
        assertThat(result.topBar.hideOnScroll.get()).isEqualTo(TOP_BAR_HIDE_ON_SCROLL.get());
        assertThat(result.topBar.buttons.back.accessibilityLabel.get()).isEqualTo(BACK_BUTTON_ACCESSIBILITY_LABEL);
        assertThat(result.bottomTabsOptions.animate.get()).isEqualTo(BOTTOM_TABS_ANIMATE.get());
        assertThat(result.bottomTabsOptions.visible.get()).isEqualTo(BOTTOM_TABS_VISIBLE.get());
        assertThat(result.fabOptions.id.get()).isEqualTo(FAB_ID);
        assertThat(result.fabOptions.backgroundColor.get()).isEqualTo(FAB_BACKGROUND_COLOR);
        assertThat(result.fabOptions.clickColor.get()).isEqualTo(FAB_CLICK_COLOR);
        assertThat(result.fabOptions.rippleColor.get()).isEqualTo(FAB_RIPPLE_COLOR);
        assertThat(result.fabOptions.visible.get()).isEqualTo(FAB_VISIBLE);
        assertThat(result.fabOptions.hideOnScroll.get()).isEqualTo(FAB_HIDE_ON_SCROLL);
        assertThat(result.fabOptions.alignVertically.get()).isEqualTo(FAB_ALIGN_VERTICALLY);
        assertThat(result.fabOptions.alignHorizontally.get()).isEqualTo(FAB_ALIGN_HORIZONTALLY);
        assertThat(result.layout.backgroundColor.get()).isEqualTo(SCREEN_BACKGROUND_COLOR);
    }

    @NonNull
    private JSONObject createBottomTabs() throws JSONException {
        return new JSONObject()
                .put("currentTabId", BOTTOM_TABS_CURRENT_TAB_ID)
                .put("currentTabIndex", BOTTOM_TABS_CURRENT_TAB_INDEX.get())
                .put("visible", BOTTOM_TABS_VISIBLE.get())
                .put("animate", BOTTOM_TABS_ANIMATE.get());
    }

    private JSONObject createBackButton() throws JSONException {
        return new JSONObject()
                .put("accessibilityLabel", BACK_BUTTON_ACCESSIBILITY_LABEL);
    }

    @NonNull
    private JSONObject createTopBar(boolean visible) throws JSONException {
        return new JSONObject()
                .put("title", createTitle())
                .put("subtitle", createSubtitle())
                .put("background", createBackground())
                .put("visible", visible)
                .put("drawBehind", TOP_BAR_DRAW_BEHIND.get())
                .put("hideOnScroll", TOP_BAR_HIDE_ON_SCROLL.get())
                .put("backButton", createBackButton());
    }

    private JSONObject createBackground() throws JSONException {
        return new JSONObject()
                .put("color", createColor(TOP_BAR_BACKGROUND_COLOR, TOP_BAR_BACKGROUND_COLOR));
    }

    private JSONObject createColor(int light, int dark) throws JSONException {
        return new JSONObject().put("light", light).put("dark", dark);
    }

    private JSONObject createTitle() throws JSONException {
        return new JSONObject()
                .put("text", "the title")
                .put("height", TITLE_HEIGHT.get())
                .put("color", createColor(TOP_BAR_TEXT_COLOR,TOP_BAR_TEXT_COLOR))
                .put("fontSize", TOP_BAR_FONT_SIZE)
                .put("fontFamily", TOP_BAR_FONT_FAMILY);
    }

    private JSONObject createSubtitle() throws JSONException {
        return new JSONObject()
                .put("text", "the subtitle")
                .put("color", createColor(SUBTITLE_TEXT_COLOR,SUBTITLE_TEXT_COLOR))
                .put("fontSize", SUBTITLE_FONT_SIZE)
                .put("fontFamily", SUBTITLE_FONT_FAMILY)
                .put("alignment", SUBTITLE_ALIGNMENT);
    }

    @NonNull
    private JSONObject createFab() throws JSONException {
        return new JSONObject()
                .put("id", FAB_ID)
                .put("backgroundColor", createColor(FAB_BACKGROUND_COLOR,FAB_BACKGROUND_COLOR))
                .put("clickColor", createColor(FAB_CLICK_COLOR,FAB_CLICK_COLOR))
                .put("rippleColor", createColor(FAB_RIPPLE_COLOR,FAB_RIPPLE_COLOR))
                .put("alignHorizontally", FAB_ALIGN_HORIZONTALLY)
                .put("alignVertically", FAB_ALIGN_VERTICALLY)
                .put("hideOnScroll", FAB_HIDE_ON_SCROLL)
                .put("visible", FAB_VISIBLE);
    }

    @NonNull
    private JSONObject createOtherFab() throws JSONException {
        return new JSONObject()
                .put("id", "FAB")
                .put("backgroundColor", createColor(FAB_BACKGROUND_COLOR,FAB_BACKGROUND_COLOR))
                .put("clickColor", createColor(FAB_CLICK_COLOR,FAB_CLICK_COLOR))
                .put("rippleColor", createColor(FAB_RIPPLE_COLOR,FAB_RIPPLE_COLOR))
                .put("alignHorizontally", FAB_ALIGN_HORIZONTALLY)
                .put("alignVertically", FAB_ALIGN_VERTICALLY)
                .put("hideOnScroll", FAB_HIDE_ON_SCROLL)
                .put("visible", FAB_VISIBLE);
    }

    @NonNull
    private JSONObject createOtherTopBar() throws JSONException {
        return new JSONObject()
                .put("title", createTitle())
                .put("subtitle", createSubtitle())
                .put("background", createBackground())
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
        Options options1 = Options.parse(activity, mockLoader, json1);
        options1.topBar.title.text = new Text("some title");

        JSONObject json2 = new JSONObject();
        json2.put("topBar", createTopBar(false));
        Options options2 = Options.parse(activity, mockLoader, json2);
        options2.topBar.title.text = new NullText();

        Options merged = options1.mergeWith(options2);
        assertThat(options1.topBar.visible.get()).isTrue();
        assertThat(merged.topBar.visible.get()).isFalse();
        assertThat(merged.topBar.title.text.get()).isEqualTo("some title");
    }

    @Test
    public void mergeDefaultOptions() throws Exception {
        JSONObject layout = new JSONObject()
                .put("backgroundColor", createColor(SCREEN_BACKGROUND_COLOR,SCREEN_BACKGROUND_COLOR));
        JSONObject json = new JSONObject()
                .put("topBar", createTopBar(TOP_BAR_VISIBLE.get()))
                .put("fab", createFab())
                .put("bottomTabs", createBottomTabs())
                .put("layout", layout);
        Options defaultOptions = Options.parse(activity, mockLoader, json);
        Options options = new Options();

        assertResult(options.mergeWith(defaultOptions));
    }

    @Test
    public void mergedDefaultOptionsDontOverrideGivenOptions() throws Exception {
        JSONObject layout = new JSONObject()
                .put("backgroundColor", createColor(SCREEN_BACKGROUND_COLOR,SCREEN_BACKGROUND_COLOR));
        JSONObject defaultJson = new JSONObject()
                .put("topBar", createOtherTopBar())
                .put("fab", createOtherFab())
                .put("bottomTabs", createOtherBottomTabs())
                .put("layout", layout);
        Options defaultOptions = Options.parse(activity, mockLoader, defaultJson);

        JSONObject json = new JSONObject()
                .put("topBar", createTopBar(TOP_BAR_VISIBLE.get()))
                .put("bottomTabs", createBottomTabs());
        Options options = Options.parse(activity, mockLoader, json);
        options.withDefaultOptions(defaultOptions);
        assertResult(options);
    }

    @Test
    public void defaultEmptyOptions() {
        Options uut = new Options();
        assertThat(uut.topBar.title.text.get("")).isEmpty();
        assertThat(uut.layout.backgroundColor.hasValue()).isFalse();

    }

    @Test
    public void topBar_defaultOptions() {
        Options uut = new Options();
        assertThat(uut.topBar.visible.isFalseOrUndefined()).isTrue();
        assertThat(uut.topBar.animate.isTrueOrUndefined()).isTrue();
    }

    @Test
    public void clear_topBarOptions() {
        Options uut = new Options();
        uut.topBar.title.text = new Text("some title");
        uut.clearTopBarOptions();
        assertThat(uut.topBar.title.text.hasValue()).isFalse();
    }

    @Test
    public void clear_bottomTabsOptions() {
        Options uut = new Options();
        uut.bottomTabsOptions.backgroundColor = new ThemeColour(new Colour(Color.RED), new Colour(Color.RED));
        uut.clearBottomTabsOptions();
        assertThat(uut.bottomTabsOptions.backgroundColor.hasValue()).isFalse();
    }

    @Test
    public void clear_topTabsOptions() {
        Options uut = new Options();
        uut.topTabs.fontSize = new Number(666);
        uut.clearTopTabsOptions();
        assertThat(uut.topTabs.fontSize.hasValue()).isFalse();
    }

    @Test
    public void clear_topTabOptions() {
        Options uut = new Options();
        uut.topTabOptions.title = new Text("some title");
        uut.clearTopTabOptions();
        assertThat(uut.topTabOptions.title.hasValue()).isFalse();
    }
}
