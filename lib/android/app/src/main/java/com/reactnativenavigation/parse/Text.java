package com.reactnativenavigation.parse;

public class Text extends Param<String> {
    public Text(String value) {
        super(value);
    }

    @Override
    public String toString() {
        return hasValue() ? value : "No Value";
    }
}
