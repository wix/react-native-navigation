package com.reactnativenavigation.utils

fun busyRetry(times: Int, interval: Long, callback: (tries: Int) -> Boolean) {
    var tries = 0
    while (tries < times && !callback(tries)) {
        sleepSafe(interval)
        tries++
    }
}
