package com.reactnativenavigation.viewcontrollers.stack.topbar

import android.view.View
import android.view.ViewGroup
import com.reactnativenavigation.viewcontrollers.viewcontroller.ScrollEventListener
import com.reactnativenavigation.views.stack.topbar.TopBar

class TopBarCollapseBehavior(private val topBar: TopBar) : ScrollEventListener.OnScrollListener, ScrollEventListener.OnDragListener {
    private var scrollEventListener: ScrollEventListener? = null
    private val animator: TopBarAnimator = TopBarAnimator(topBar)

    fun enableCollapse(scrollEventListener: ScrollEventListener?) {
        this.scrollEventListener = scrollEventListener
        this.scrollEventListener!!.register(topBar, this, this)
    }

    fun disableCollapse() {
        if (scrollEventListener != null) {
            scrollEventListener!!.unregister()
            topBar.visibility = View.VISIBLE
            topBar.translationY = 0f
        }
    }

    override fun onScrollUp(nextTranslation: Float) {
        val measuredHeight = topBar.measuredHeight
        if (nextTranslation < -measuredHeight && topBar.visibility == View.VISIBLE) {
            topBar.visibility = View.GONE
            topBar.translationY = -measuredHeight.toFloat()
        } else if (nextTranslation > -measuredHeight && nextTranslation <= 0) {
            topBar.translationY = nextTranslation
        }
    }

    override fun onScrollDown(nextTranslation: Float) {
        val measuredHeight = topBar.measuredHeight
        if (topBar.visibility == View.GONE && nextTranslation > -measuredHeight) {
            topBar.visibility = View.VISIBLE
            topBar.translationY = nextTranslation
        } else if (nextTranslation <= 0 && nextTranslation >= -measuredHeight) {
            topBar.translationY = nextTranslation
        }
    }

    override fun onShow() {
        animator.show(translationStartDy = topBar.translationY)
    }

    override fun onHide() {
        animator.hideOnScroll(topBar.translationY, (topBar.layoutParams as ViewGroup.MarginLayoutParams).topMargin.toFloat())
    }

}