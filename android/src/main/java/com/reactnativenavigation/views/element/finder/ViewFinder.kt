package com.reactnativenavigation.views.element.finder

import android.view.View
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController

interface ViewFinder {
    suspend fun find(root: ViewController<*>, nativeId: String): View?
}