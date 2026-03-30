package com.reactnativenavigation.mocks

import android.view.ViewGroup
import com.reactnativenavigation.options.Options
import com.reactnativenavigation.viewcontrollers.parent.ParentController
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
    fun parentController(topMostParentController: ParentController<*>?=null):ParentController<ViewGroup>{
        val mock = mock<ParentController<ViewGroup>>()
        whenever(mock.topMostParent).thenReturn(topMostParentController?:mock)
        return mock
    }

    fun parentController(topMostParentController: ParentController<*>?=null,view:ViewGroup):ParentController<ViewGroup>{
        val mock = mock<ParentController<ViewGroup>>()
        whenever(mock.topMostParent).thenReturn(topMostParentController?:mock)
        whenever(mock.view).thenReturn(view)

        return mock
    }
}