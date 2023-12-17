package com.reactnativenavigation.utils

fun sleep(ms: Long) {
    try {
        Thread.sleep(ms)
    } catch (e: InterruptedException) {
        e.printStackTrace()
    }
}
