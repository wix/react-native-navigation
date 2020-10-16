package com.reactnativenavigation.utils;

import android.util.Log;

public class DefaultLogger implements ILogger {
    private int logLevel;

    public DefaultLogger(int logLevel) {
        this.logLevel = logLevel;
    }

    public DefaultLogger() {
        this.logLevel = Log.INFO;
    }

    @Override
    public boolean isLoggable(String tag, int level) {
        return logLevel <= level;
    }

    @Override
    public int getLogLevel() {
        return logLevel;
    }

    @Override
    public void setLogLevel(int logLevel) {
        this.logLevel = logLevel;
    }

    @Override
    public void d(String tag, String text, Throwable throwable) {
        if (isLoggable(tag, Log.DEBUG)) {
            Log.d(tag, text, throwable);
        }
    }

    @Override
    public void v(String tag, String text, Throwable throwable) {
        if (isLoggable(tag, Log.VERBOSE)) {
            Log.v(tag, text, throwable);
        }
    }

    @Override
    public void i(String tag, String text, Throwable throwable) {
        if (isLoggable(tag, Log.INFO)) {
            Log.i(tag, text, throwable);
        }
    }

    @Override
    public void w(String tag, String text, Throwable throwable) {
        if (isLoggable(tag, Log.WARN)) {
            Log.w(tag, text, throwable);
        }
    }

    @Override
    public void e(String tag, String text, Throwable throwable) {
        if (isLoggable(tag, Log.ERROR)) {
            Log.e(tag, text, throwable);
        }
    }

    @Override
    public void d(String tag, String text) {
        d(tag, text, null);
    }

    @Override
    public void v(String tag, String text) {
        v(tag, text, null);
    }

    @Override
    public void i(String tag, String text) {
        i(tag, text, null);
    }

    @Override
    public void w(String tag, String text) {
        w(tag, text, null);
    }

    @Override
    public void e(String tag, String text) {
        e(tag, text, null);
    }

    @Override
    public void log(int priority, String tag, String msg) {
        log(priority, tag, msg, false);
    }

    @Override
    public void log(int priority, String tag, String msg, boolean forceLog) {
        if (forceLog || isLoggable(tag, priority)) {
            Log.println(priority, tag, msg);
        }
    }
}
