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

    public interface Func2<T, S> {
        void run(T param1, S param2);
    }

    public interface FuncR<T> {
        T run();
    }

    public interface FuncR1<T, S> {
        S run(T param);
    }

    public interface FuncR2<T, S, R> {
        R run(T param1, S param2);
    }
}
