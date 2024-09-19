package com.reactnativenavigation.utils

fun sleepSafe(ms: Long) {
    try {
        Thread.sleep(ms)
    } catch (e: InterruptedException) {
        e.printStackTrace()
    }
}
