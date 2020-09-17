package com.reactnativenavigation.utils

import android.animation.TimeInterpolator
import kotlin.math.*

class SpringInterpolator(private val _mass: Float, private val _damping: Float, private val _stiffness: Float) : TimeInterpolator {
    override fun getInterpolation(p: Float): Float {
        val v0 = 0
        val s0 = 1

        val lambda = _damping / (2 * _mass)
        val w0 = sqrt(_stiffness / _mass)

        val wd = sqrt(abs(w0.pow(2) - lambda.pow(2)))

        return when {
            lambda < w0 -> {
                abs(1 - exp(-lambda * p) * (s0 * cos(wd * p) + (v0 + s0 * lambda) / wd * sin(wd * p)))
            }
            lambda > w0 -> {
                abs(1 - exp(-lambda * p) * ((v0 + s0 * (lambda + wd)) / (2 * wd) * exp(wd * p) + (s0 - (v0 + s0 * (lambda + wd)) / (2 * wd)) * exp(-wd * p)))
            }
            else -> {
                abs(1 - exp(-lambda * p) * (s0 + (v0 + lambda * s0) * p))
            }
        }
    }
}