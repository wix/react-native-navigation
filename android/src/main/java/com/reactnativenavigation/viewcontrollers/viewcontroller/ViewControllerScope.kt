package com.reactnativenavigation.viewcontrollers.viewcontroller

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

/** Lazily created by a controller and cancelled before its view is destroyed. */
class ViewControllerScope : CoroutineScope {
    override val coroutineContext = SupervisorJob() + Dispatchers.Main.immediate

    fun cancel() = coroutineContext.cancel()

    fun launchAnimation(set: AnimatorSet, onCancelled: () -> Unit, block: suspend CoroutineScope.() -> Unit) {
        val job = launch(start = CoroutineStart.LAZY, block = block)
        val listener = object : AnimatorListenerAdapter() {
            override fun onAnimationCancel(animation: Animator) { job.cancel() }
        }
        set.addListener(listener)
        job.invokeOnCompletion { cause ->
            set.removeListener(listener)
            if (cause != null) onCancelled()
        }
        job.start()
    }
}

/** AnimatorSet.cancel() does not notify listeners before start(), while preparation can suspend. */
fun AnimatorSet.cancelPendingOrRunning() {
    if (isStarted) {
        cancel()
    } else {
        val callbacks = listeners?.toList().orEmpty()
        callbacks.forEach { it.onAnimationCancel(this) }
        callbacks.forEach { it.onAnimationEnd(this) }
    }
}
