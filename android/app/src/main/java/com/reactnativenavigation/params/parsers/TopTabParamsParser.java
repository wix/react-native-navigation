package com.reactnativenavigation.params.parsers;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.reactnativenavigation.params.NavigationParams;
import com.reactnativenavigation.params.PageParams;

import java.util.List;

public class TopTabParamsParser extends Parser {
    private static final String KEY_SCREEN_ID = "screenId";
    private static final String KEY_TITLE = "title";
    private static final String NAVIGATION_PARAMS = "navigationParams";

    @SuppressWarnings("ConstantConditions")
    public List<PageParams> parse(Bundle params) {
        return parseBundle(params, new ParseStrategy<PageParams>() {
            @Override
            public PageParams parse(Bundle topTabs) {
                return parseItem(topTabs);
            }
        });
    }

    @NonNull
    private static PageParams parseItem(Bundle params) {
        PageParams result = new PageParams();
        result.screenId = params.getString(KEY_SCREEN_ID);
        result.title = params.getString(KEY_TITLE);
        result.navigationParams = new NavigationParams(params.getBundle(NAVIGATION_PARAMS));
        result.leftButton = ButtonParser.parseLeftButton(params);
        result.rightButtons = ButtonParser.parseRightButton(params);
        result.fabParams = ButtonParser.parseFab(params, result.navigationParams.navigatorEventId);
        return result;
    }
}
