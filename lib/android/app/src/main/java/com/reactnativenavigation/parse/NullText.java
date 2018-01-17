package com.reactnativenavigation.parse;

public class NullText extends Text {
    public NullText() {
        super("");
    }

    @Override
    public boolean hasValue() {
        return false;
    }
}
