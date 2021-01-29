package com.reactnativenavigation.options.params

open class Number(value: Int) : Param<Int>(value)

object NullNumber : Number(0) {
    override fun hasValue(): Boolean {
        return false
    }
}