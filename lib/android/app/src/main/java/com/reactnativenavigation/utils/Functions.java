package com.reactnativenavigation.utils;

public class Functions {
    public interface Unit<T> {
        T get();
    }

    public interface Func {
        void run();
    }

    public interface Func1<T> {
        void run(T param);
    }

    public interface FuncR1<T, S> {
        S run(T param);
    }
}
