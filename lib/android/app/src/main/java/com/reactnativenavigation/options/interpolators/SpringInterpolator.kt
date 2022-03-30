package com.reactnativenavigation.options.interpolators

import android.animation.TimeInterpolator
import kotlin.math.*

class SpringInterpolator(private val mass: Float = 3f, private val damping: Float = 500f, private val stiffness: Float = 200f, private val allowsOverdamping: Boolean = false, initialVelocity: Float = 0f) : TimeInterpolator {
    private val velocity: Float = initialVelocity

    override fun getInterpolation(t: Float): Float {
        val b = damping
        val m = mass
        val k = stiffness
        val v0 = velocity

        var beta = b / (2 * m)
        val omega0 = sqrt(k / m)
        val omega1 = sqrt((omega0 * omega0) - (beta * beta))
        val omega2 = sqrt((beta * beta) - (omega0 * omega0))

        val x0 = -1

        if (!this.allowsOverdamping && beta > omega0) beta = omega0
        if (t == 1f) {
            return 1f
        }
        return when {
            beta < omega0 -> {
                // Underdamped
                val envelope = exp(-beta * t)
                -x0 + envelope * (x0 * cos(omega1 * t) + ((beta * x0 + v0) / omega1) * sin(omega1 * t))
            }
            beta == omega0 -> {
                val envelope = exp(-beta * t)
                -x0 + envelope * (x0 + (beta * x0 + v0) * t)
            }
            else -> {
                val envelope = exp(-beta * t)
                -x0 + envelope * (x0 * cosh(omega2 * t) + ((beta * x0 + v0) / omega2) * sinh(omega2 * t))
            }
        }
    }

}
