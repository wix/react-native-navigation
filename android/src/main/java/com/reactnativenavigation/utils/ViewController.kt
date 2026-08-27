package com.reactnativenavigation.utils

import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController
import kotlin.coroutines.resume
import kotlinx.coroutines.suspendCancellableCoroutine

suspend fun ViewController<*>.awaitRender() = suspendCancellableCoroutine<Unit> { cont ->
    val listener = object : Runnable {
        override fun run() {
            removeOnAppearedListener(this)
            if (cont.isActive) cont.resume(Unit)
        }
    }
    cont.invokeOnCancellation { removeOnAppearedListener(listener) }
    if (cont.isActive) addOnAppearedListener(listener)
}
