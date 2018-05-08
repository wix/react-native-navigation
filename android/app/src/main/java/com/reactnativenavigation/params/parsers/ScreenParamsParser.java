package com.reactnativenavigation.params.parsers;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.reactnativenavigation.params.AppStyle;
import com.reactnativenavigation.params.NavigationParams;
import com.reactnativenavigation.params.PageParams;
import com.reactnativenavigation.params.ScreenParams;
import com.reactnativenavigation.react.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class ScreenParamsParser extends Parser {
    private static final String KEY_TITLE = "title";
    private static final String KEY_SUBTITLE = "subtitle";
    private static final String KEY_SCREEN_ID = "screenId";
    private static final String KEY_TIMESTAMP = "timestamp";
    private static final String KEY_NAVIGATION_PARAMS = "navigationParams";
    private static final String STYLE_PARAMS = "styleParams";
    private static final String TOP_TABS = "topTabs";
    private static final String FRAGMENT_CREATOR_CLASS_NAME = "fragmentCreatorClassName";
    private static final String FRAGMENT_CREATOR_PASS_PROPS = "fragmentCreatorPassProps";
    private static final String OVERRIDE_BACK_PRESS = "overrideBackPress";
    private static final String ANIMATION_TYPE = "animationType";

    @SuppressWarnings("ConstantConditions")
    public static ScreenParams parse(Bundle params) {
        ScreenParams result = new ScreenParams();
        result.screenId = params.getString(KEY_SCREEN_ID);
        result.timestamp = params.getDouble(KEY_TIMESTAMP);
        assertKeyExists(params, KEY_NAVIGATION_PARAMS);
        result.navigationParams = new NavigationParams(params.getBundle(KEY_NAVIGATION_PARAMS));

        result.styleParams = new StyleParamsParser(params.getBundle(STYLE_PARAMS)).parse();

        result.title = params.getString(KEY_TITLE);
        result.subtitle = params.getString(KEY_SUBTITLE);
        result.rightButtons = ButtonParser.parseRightButton(params);
        result.overrideBackPressInJs = params.getBoolean(OVERRIDE_BACK_PRESS, false);
        result.leftButton = ButtonParser.parseLeftButton(params);

        result.topTabParams = parseTopTabs(params);
        if (hasKey(params, "screens")) {
            result.screens = parseScreens(params.getBundle("screens"));
        }

        if (hasKey(params, FRAGMENT_CREATOR_CLASS_NAME)) {
            result.fragmentCreatorClassName = params.getString(FRAGMENT_CREATOR_CLASS_NAME);
            result.fragmentCreatorPassProps = params.getBundle(FRAGMENT_CREATOR_PASS_PROPS);
        }

        result.fabParams = ButtonParser.parseFab(params, result.navigationParams.navigatorEventId, result.navigationParams.screenInstanceId);

        result.tabLabel = getTabLabel(params);
        result.tabIcon = new TabIconParser(params).parse();

        result.animateScreenTransitions = new AnimationParser(params).parse();
        result.sharedElementsTransitions = getSharedElementsTransitions(params);

        result.animationType = params.getString(ANIMATION_TYPE, AppStyle.appStyle == null ? "" : AppStyle.appStyle.screenAnimationType);

        return result;
    }

    private static List<String> getSharedElementsTransitions(Bundle params) {
        Bundle sharedElements = params.getBundle("sharedElements");
        if (sharedElements == null) {
            return new ArrayList<>();
        }
        List<String> result = new ArrayList<>();
        for (String key : sharedElements.keySet()) {
            result.add(sharedElements.getString(key));
        }
        return result;
    }

    private static Drawable getTabIcon(Bundle params) {
        Drawable tabIcon = null;
        if (hasKey(params, "icon")) {
            tabIcon = ImageLoader.loadImage(params.getString("icon"));
        }
        return tabIcon;
    }

    private static String getTabLabel(Bundle params) {
        String tabLabel = null;
        if (hasKey(params, "label")) {
            tabLabel = params.getString("label");
        }
        return tabLabel;
    }

    private static List<PageParams> parseTopTabs(Bundle params) {
        List<PageParams> topTabParams = null;
        if (hasKey(params, TOP_TABS)) {
            topTabParams = new TopTabParamsParser().parse(params.getBundle(TOP_TABS));
        }
        return topTabParams;
    }

    List<ScreenParams> parseTabs(Bundle params) {
        return parseBundle(params, new ParseStrategy<ScreenParams>() {
            @Override
            public ScreenParams parse(Bundle screen) {
                return ScreenParamsParser.parse(screen);
            }
        });
    }

    private static List<ScreenParams> parseScreens(Bundle screens) {
        return new Parser().parseBundle(screens, new ParseStrategy<ScreenParams>() {
            @Override
            public ScreenParams parse(Bundle screen) {
                return ScreenParamsParser.parse(screen);
            }
        });
    }
}
