package com.reactnativenavigation.parse;

import android.graphics.Typeface;
import android.support.annotation.Nullable;

import com.reactnativenavigation.utils.TypefaceLoader;

import org.json.JSONObject;

public class TopTabOptions implements DEFAULT_VALUES {
    public String title = NO_VALUE;
    @Nullable public Typeface fontFamily;
    public int tabIndex;

    public static TopTabOptions parse(TypefaceLoader typefaceManager, JSONObject json) {
        TopTabOptions result = new TopTabOptions();
        if (json == null) return result;

        result.title = json.optString("title", NO_VALUE);
        result.fontFamily = typefaceManager.getTypeFace(json.optString("titleFontFamily"));
        return result;
    }

    void mergeWith(TopTabOptions topTabsOptions) {

    }

    void mergeWithDefault(TopTabOptions topTabsOptions) {

    }
}
