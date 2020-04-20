package com.reactnativenavigation.utils

import android.animation.Animator
import android.animation.TimeInterpolator

fun Animator.withStartDelay(delay: Long): Animator {
    startDelay = delay
    return this
}

fun Animator.withDuration(duration: Long): Animator {
    this.duration = duration
    return this
}

fun Animator.withInterpolator(interpolator: TimeInterpolator): Animator {
    this.interpolator = interpolator
    return this
}