package com.reactnativenavigation.mocks

import android.view.ViewGroup
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.reactnativenavigation.options.Options
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController

object Mocks {
    fun viewController(): ViewController<*> {
        val mock = mock<ViewController<*>>()
        whenever(mock.resolveCurrentOptions()).thenReturn(Options.EMPTY)
        val view = mock<ViewGroup>()
        whenever(mock.view).thenReturn(view)
        return mock
    }
}