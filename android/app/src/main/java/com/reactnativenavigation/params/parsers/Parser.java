package com.reactnativenavigation.params.parsers;

import android.os.Bundle;

import com.reactnativenavigation.params.StyleParams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Parser {
    protected static boolean hasKey(Bundle bundle, String key) {
        return bundle.keySet().contains(key);
    }

    protected static void assertKeyExists(Bundle bundle, String key) {
        if (!hasKey(bundle, key)) {
            throw new KeyDoesNotExistsException(key);
        }
    }

    public static class KeyDoesNotExistsException extends RuntimeException {
        public KeyDoesNotExistsException(String key) {
            super(key);
        }
    }

    protected interface ParseStrategy<T> {
        T parse(Bundle params);
    }

    protected <T> List<T> parseBundle(Bundle params, ParseStrategy<T> strategy) {
        ArrayList<T> result = new ArrayList<>(Collections.nCopies(params.keySet().size(), (T) null));
        for (String key : params.keySet()) {
            result.set(Integer.parseInt(key), strategy.parse(params.getBundle(key)));
        }
        return result;
    }

    protected StyleParams.Color getColor(Bundle bundle, String key, StyleParams.Color defaultColor) {
        StyleParams.Color color = StyleParams.Color.parse(bundle.getString(key));
        return color.hasColor() || defaultColor == null ? color : defaultColor;
    }
}
