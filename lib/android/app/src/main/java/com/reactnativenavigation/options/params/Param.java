package com.reactnativenavigation.options.params;

import static com.reactnativenavigation.utils.ObjectUtils.equalsNotNull;

public abstract class Param<T> {
    protected T value;
    private boolean consumed;

    Param(T value) {
        this.value = value;
    }

    public T getAndConsume() {
        T value = get();
        consumed = true;
        return value;
    }

    public void consume() {
        consumed = true;
    }

    public T get() {
        if (hasValue()) {
            return value;
        }
        throw new RuntimeException("Tried to get null value!");
    }

    public T get(T defaultValue) {
        return hasValue() ? value : defaultValue;
    }

    public boolean hasValue() {
        return value != null && !consumed;
    }

    public boolean canApplyValue() {
        return true;
    }

    public boolean equals(Param other) {
        return value == other.value || equalsNotNull(value, other.value);
    }
}
