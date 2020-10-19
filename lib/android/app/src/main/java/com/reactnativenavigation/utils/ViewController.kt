package com.reactnativenavigation.utils

import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun ViewController<*>.awaitRender() = suspendCoroutine<Unit> { cont ->
    addOnAppearedListener(object : Runnable {
        override fun run() {
            removeOnAppearedListener(this)
            cont.resume(Unit)
        }
    })
}