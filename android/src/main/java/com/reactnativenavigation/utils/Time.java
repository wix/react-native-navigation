package com.reactnativenavigation.utils;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("WeakerAccess")
public class Time {
    private static Map<String, Long> tagsToStartTime = new HashMap<>();
    private static Now now = new Now();
    private static AtomicInteger count = new AtomicInteger();

    public static <T> T log(String tag, Functions.Unit<T> unit) {
        int idx = count.incrementAndGet();
        log(tag + idx);
        T t = unit.get();
        log(tag + idx);
        return t;
    }

    public static void log(String tag, Functions.Func task) {
        int idx = count.incrementAndGet();
        log(tag + idx);
        task.run();
        log(tag + idx);
    }

    public static void log(String tag) {
        if (tagsToStartTime.containsKey(tag)) {
            Log.i(tag, "Elapsed: " + (now() - time(tag)) + "ms");
        } else {
            Log.v(tag, "logging start");
        }
        tagsToStartTime.put(tag, now());
    }

    private static long now() {
        return now.now();
    }

    private static long time(String tag) {
        return tagsToStartTime.get(tag);
    }
}
