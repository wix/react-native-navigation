package com.reactnativenavigation.options.params;

public class Bool extends Param<Boolean> {
    public Bool(Boolean value) {
        super(value);
    }

    public boolean isFalseOrUndefined() {
        return !hasValue() || !get();
    }

    public boolean isTrueOrUndefined() {
        return !hasValue() || get();
    }

    public boolean isTrue() {
        return hasValue() && get();
    }

    public boolean isFalse() {
        return hasValue() && !get();
    }
}
