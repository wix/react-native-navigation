package com.reactnativenavigation.options.abstraction

interface IOptions<in T : IOptions<T>>{
    fun merge(toMerge:T?, defaults:T?)
    fun hasValue():Boolean
}