package com.reactnativenavigation.options.params;

public class NullText extends Text {
    public NullText() {
        super("");
    }

    @Override
    public boolean hasValue() {
        return false;
    }
}
