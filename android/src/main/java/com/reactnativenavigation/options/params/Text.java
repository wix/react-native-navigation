package com.reactnativenavigation.options.params;

import androidx.annotation.NonNull;

public class Text extends Param<String> {
    public Text(String value) {
        super(value);
    }

    public int length() {
        return hasValue() ? value.length() : 0;
    }

    @NonNull
    @Override
    public String toString() {
        return hasValue() ? value : "No Value";
    }
}
