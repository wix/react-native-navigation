package com.reactnativenavigation.parse;

import android.support.annotation.Nullable;

import com.reactnativenavigation.parse.params.Button;
import com.reactnativenavigation.utils.CollectionUtils;
import com.reactnativenavigation.utils.TypefaceLoader;

import org.json.JSONObject;

import java.util.ArrayList;

public class TopBarButtons {

    public static TopBarButtons parse(TypefaceLoader typefaceLoader, JSONObject json) {
        TopBarButtons result = new TopBarButtons();
        if (json == null) return result;

        result.right = parseButtons(typefaceLoader, json, "rightButtons");
        result.left = parseButtons(typefaceLoader, json, "leftButtons");
        result.back = BackButton.parse(json.optJSONObject("backButton"));

        return result;
    }

    @Nullable
    private static ArrayList<Button> parseButtons(TypefaceLoader typefaceLoader, JSONObject json, String buttons) {
        return Button.parse(json, buttons, typefaceLoader);
    }

    public BackButton back = new BackButton();
    @Nullable public ArrayList<Button> left;
    @Nullable public ArrayList<Button> right;

    void mergeWith(TopBarButtons other) {
        if (other.left != null) left = mergeLeftButton(other.left);
        if (other.right != null) right = other.right;
        back.mergeWith(other.back);
    }

    private ArrayList<Button> mergeLeftButton(ArrayList<Button> other) {
        if (!other.isEmpty() && !CollectionUtils.isNullOrEmpty(left)) {
            Button otherLeft = other.get(0);
            if (otherLeft.id == null) {
                left.get(0).mergeWith(otherLeft);
                return left;
            }
        }
        return other;
    }

    void mergeWithDefault(TopBarButtons defaultOptions) {
        if (left == null) {
            left = defaultOptions.left;
        } else if (!CollectionUtils.isNullOrEmpty(defaultOptions.left)){
            for (Button button : left) {
                button.mergeWithDefault(defaultOptions.left.get(0));
            }
        }
        if (right == null) {
            right = defaultOptions.right;
        } else if (!CollectionUtils.isNullOrEmpty(defaultOptions.right)) {
            for (Button button : right) {
                button.mergeWithDefault(defaultOptions.right.get(0));
            }
        }
        back.mergeWithDefault(defaultOptions.back);
    }

    public boolean hasLeftButtons() {
        return !CollectionUtils.isNullOrEmpty(left) && left.get(0).id != null;
    }
}
