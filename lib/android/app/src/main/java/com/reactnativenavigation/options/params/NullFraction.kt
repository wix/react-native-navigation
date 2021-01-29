package com.reactnativenavigation.options.params

object NullFraction : Fraction(0.0) {
    override fun hasValue(): Boolean {
        return false
    }
}