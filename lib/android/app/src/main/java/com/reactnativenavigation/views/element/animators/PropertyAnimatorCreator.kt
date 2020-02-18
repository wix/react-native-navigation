package com.reactnativenavigation.views.element.animators

import android.animation.Animator
import android.view.View
import androidx.annotation.CallSuper
import com.reactnativenavigation.parse.SharedElementTransitionOptions
import java.lang.reflect.ParameterizedType

abstract class PropertyAnimatorCreator<T : View> internal constructor(protected var from: View, protected var to: View) {
    @CallSuper
    fun shouldAnimateProperty(): Boolean {
        val type = childClass
        return type.isInstance(from) &&
                type.isInstance(to) &&
                !excludedViews().contains(from.javaClass) &&
                !excludedViews().contains(to.javaClass) &&
                shouldAnimateProperty(from as T, to as T)
    }

    protected abstract fun shouldAnimateProperty(fromChild: T, toChild: T): Boolean
    protected open fun excludedViews() = emptyList<Class<*>>()

    abstract fun create(options: SharedElementTransitionOptions): Animator
    private val childClass: Class<T>
        get() = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<T>

}