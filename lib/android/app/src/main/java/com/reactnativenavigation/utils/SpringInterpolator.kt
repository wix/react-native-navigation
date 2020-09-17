package com.reactnativenavigation.utils

import android.animation.TimeInterpolator
import kotlin.math.*

class SpringInterpolator(private val mass: Float, private val damping: Float, private val stiffness: Float) : TimeInterpolator {
    override fun getInterpolation(p: Float): Float {
        // See: https://en.wikipedia.org/wiki/Harmonic_oscillator#Damped_harmonic_oscillator
        val v0 = 0
        val s0 = 1

        val lambda = damping / (2 * mass)
        val w0 = sqrt(stiffness / mass)

        val wd = sqrt(abs(w0.pow(2) - lambda.pow(2)))

        return when {
            lambda < w0 -> {
                // underdamped
                abs(1 - exp(-lambda * p) * (s0 * cos(wd * p) + (v0 + s0 * lambda) / wd * sin(wd * p)))
            }
            lambda > w0 -> {
                // overdamped
                abs(1 - exp(-lambda * p) * ((v0 + s0 * (lambda + wd)) / (2 * wd) * exp(wd * p) + (s0 - (v0 + s0 * (lambda + wd)) / (2 * wd)) * exp(-wd * p)))
            }
            else -> {
                // critically damped
                abs(1 - exp(-lambda * p) * (s0 + (v0 + lambda * s0) * p))
            }
        }
    }
}