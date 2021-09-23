package com.reactnativenavigation.utils;

public interface ILogger {
    boolean isLoggable(String tag, int level);

    int getLogLevel();

    void setLogLevel(int logLevel);

    void d(String tag, String text, Throwable throwable);

    void v(String tag, String text, Throwable throwable);

    void i(String tag, String text, Throwable throwable);

    void w(String tag, String text, Throwable throwable);

    void e(String tag, String text, Throwable throwable);

    void d(String tag, String text);

    void v(String tag, String text);

    void i(String tag, String text);

    void w(String tag, String text);

    void e(String tag, String text);

    void log(int priority, String tag, String msg);

    void log(int priority, String tag, String msg, boolean forceLog);
}
