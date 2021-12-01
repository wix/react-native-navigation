package com.reactnativenavigation.mocks

import android.view.ViewGroup

import com.reactnativenavigation.options.Options
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

object Mocks {
    fun viewController(): ViewController<*> {
        val mock = mock<ViewController<*>>()
        whenever(mock.resolveCurrentOptions()).thenReturn(Options.EMPTY)
        val view = mock<ViewGroup>()
        whenever(mock.view).thenReturn(view)
        return mock
    }
}