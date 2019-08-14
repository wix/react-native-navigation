package com.reactnativenavigation.utils;

import com.reactnativenavigation.utils.Functions.Func1;

import androidx.annotation.NonNull;

public class LateInit<T> {
    private T value;

    public T get() {
        return value;
    }

    public void set(@NonNull T value) {
        this.value = value;
    }

    public void perform(Func1<T> task) {
        if (value != null) task.run(value);
    }
}
