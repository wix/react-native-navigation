package com.reactnativenavigation.utils

import android.animation.Animator

fun Animator.withStartDelay(delay: Long): Animator {
    startDelay = delay
    return this
}

fun Animator.withDuration(duration: Long): Animator {
    this.duration = duration
    return this
}