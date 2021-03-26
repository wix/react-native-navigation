package com.reactnativenavigation.viewcontrollers.viewcontroller

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RestrictTo
import androidx.core.view.get
import com.reactnativenavigation.utils.isDebug
import java.util.*

open class YellowBoxDelegate(private val context: Context, private val yellowBoxHelper: YellowBoxHelper = YellowBoxHelper()) {
    constructor(context: Context) : this(context, YellowBoxHelper())

    var parent: ViewGroup? = null
        private set
    @get:RestrictTo(RestrictTo.Scope.TESTS)
    val yellowBoxes: List<View>
        get() = yellowBoxViews

    private var isDestroyed = false
    private val yellowBoxViews = ArrayList<View>()

    open fun onChildViewAdded(parent: View, child: View?) {
        if (!context.isDebug()) return
        if (yellowBoxHelper.isYellowBox(parent, child)) onYellowBoxAdded(parent)
    }

    fun onYellowBoxAdded(parent: View) {
        if (isDestroyed) return
        this.parent = parent as ViewGroup
        for (i in 1 until parent.childCount) {
            yellowBoxViews.add(parent[i])
            parent.removeView(parent[i])
            parent.addView(View(context), i)
        }
    }

    fun destroy() {
        isDestroyed = true
        if (yellowBoxViews.isNotEmpty()) yellowBoxViews.forEach { parent?.addView(it) }
    }
}