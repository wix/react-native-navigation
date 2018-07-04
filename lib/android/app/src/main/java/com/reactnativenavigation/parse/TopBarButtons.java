package com.reactnativenavigation.parse;

import android.support.annotation.Nullable;

import com.reactnativenavigation.parse.params.Button;
import com.reactnativenavigation.utils.TypefaceLoader;

import org.json.JSONObject;

import java.util.ArrayList;

public class TopBarButtons {

    public static TopBarButtons parse(TypefaceLoader typefaceLoader, JSONObject json) {
        TopBarButtons result = new TopBarButtons();
        if (json == null) return result;

        result.right = Button.parseJsonArray(json.optJSONArray("rightButtons"), typefaceLoader);
        result.left = Button.parseJsonArray(json.optJSONArray("leftButtons"), typefaceLoader);
        result.back = BackButton.parse(json.optJSONObject("backButton"));

        return result;
    }

    public BackButton back = new BackButton();
    @Nullable public ArrayList<Button> left;
    @Nullable public ArrayList<Button> right;

    void mergeWith(TopBarButtons other) {
        if (other.left != null) left = other.left;
        if (other.right != null) right = other.right;
        back.mergeWith(other.back);
    }

    void mergeWithDefault(TopBarButtons defaultOptions) {
        if (left == null) left = defaultOptions.left;
        if (right == null) right = defaultOptions.right;
        back.mergeWithDefault(defaultOptions.back);
    }
}
