package com.reactnativenavigation.viewcontrollers.modal

import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.reactnativenavigation.R
import com.reactnativenavigation.options.ModalOptions
import com.reactnativenavigation.options.ModalSheetDetent
import com.reactnativenavigation.views.stack.topbar.TopBar

object ModalBottomSheetPresenter {

    @JvmStatic
    fun createContainer(context: Context): FrameLayout {
        val container = FrameLayout(context)
        container.id = View.generateViewId()
        return container
    }

    @JvmStatic
    fun configure(container: FrameLayout, modal: ModalOptions) {
        val behavior = behaviorFor(container)
        container.setTag(R.id.modal_bottom_sheet_behavior, behavior)

        if (modal.detents.isNotEmpty()) {
            applyDetents(behavior, modal.detents, container.context)
        }

        if (modal.swipeToDismiss.hasValue()) {
            behavior.isHideable = modal.swipeToDismiss.get()
        } else {
            behavior.isHideable = true
        }

        if (modal.prefersGrabberVisible.hasValue() && modal.prefersGrabberVisible.get()) {
            ensureGrabber(container)
            attachGrabberTracking(container, behavior)
        }

        if (modal.selectedDetent.hasValue()) {
            container.post {
                applySelectedDetent(behavior, container.context, modal.selectedDetent.get(), modal.detents)
                updateGrabberPosition(container, behavior)
            }
        }
    }

    @JvmStatic
    fun applySelectedDetent(
        behavior: BottomSheetBehavior<FrameLayout>,
        context: Context,
        selectedDetent: String,
        detents: List<ModalSheetDetent>
    ) {
        val normalized = selectedDetent.lowercase()
        when (normalized) {
            "large" -> {
                behavior.isFitToContents = false
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
            "medium" -> {
                behavior.isFitToContents = false
                behavior.halfExpandedRatio = HALF_EXPANDED_RATIO
                behavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            }
            else -> {
                val custom = detents.firstOrNull {
                    it.type == ModalSheetDetent.Type.CUSTOM && it.customId.equals(normalized, ignoreCase = true)
                }
                if (custom != null) {
                    behavior.isFitToContents = false
                    behavior.skipCollapsed = false
                    behavior.peekHeight = detentHeightPx(context, custom.height)
                    behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            }
        }
    }

    private fun applyDetents(behavior: BottomSheetBehavior<FrameLayout>, detents: List<ModalSheetDetent>, context: Context) {
        if (detents.isEmpty()) {
            return
        }

        var hasMedium = false
        var hasLarge = false
        var smallestCustom: ModalSheetDetent? = null

        for (detent in detents) {
            when (detent.type) {
                ModalSheetDetent.Type.SYSTEM -> when (detent.systemId) {
                    "medium" -> hasMedium = true
                    "large" -> hasLarge = true
                }
                ModalSheetDetent.Type.CUSTOM -> {
                    if (smallestCustom == null || detent.height < smallestCustom.height) {
                        smallestCustom = detent
                    }
                }
            }
        }

        smallestCustom?.let {
            behavior.peekHeight = detentHeightPx(context, it.height)
            behavior.skipCollapsed = false
        } ?: run {
            behavior.skipCollapsed = true
        }

        if (hasMedium) {
            behavior.isFitToContents = false
            behavior.halfExpandedRatio = HALF_EXPANDED_RATIO
        } else if (hasLarge) {
            behavior.isFitToContents = true
        }
    }

    private fun behaviorFor(container: FrameLayout): BottomSheetBehavior<FrameLayout> {
        (container.getTag(R.id.modal_bottom_sheet_behavior) as? BottomSheetBehavior<FrameLayout>)?.let {
            return it
        }
        if (container.parent is CoordinatorLayout) {
            return BottomSheetBehavior.from(container)
        }
        throw IllegalStateException("Sheet container must be attached to a CoordinatorLayout before configuring")
    }

    private fun detentHeightPx(context: Context, heightDp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            heightDp,
            context.resources.displayMetrics
        ).toInt()
    }

    private fun ensureGrabber(container: FrameLayout) {
        if (container.findViewWithTag<View>(GRABBER_WRAPPER_TAG) != null) {
            return
        }
        if (container.childCount != 1) {
            return
        }

        val content = container.getChildAt(0)
        container.removeView(content)

        val metrics = container.resources.displayMetrics
        val topPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f, metrics).toInt()

        clearStatusBarInsetForSheet(content)

        val wrapper = FrameLayout(container.context).apply {
            tag = GRABBER_WRAPPER_TAG
        }

        wrapper.addView(
            content,
            FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )

        val handleWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 36f, metrics).toInt()
        val handleHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f, metrics).toInt()
        val handle = View(container.context).apply {
            tag = GRABBER_TAG
            background = ContextCompat.getDrawable(context, R.drawable.modal_sheet_grabber)
            importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_YES
            contentDescription = "Drag handle"
            elevation = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1f, metrics)
        }
        val handleParams = FrameLayout.LayoutParams(handleWidth, handleHeight).apply {
            gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
            topMargin = topPadding
        }
        wrapper.addView(handle, handleParams)

        container.addView(
            wrapper,
            FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
    }

    private fun attachGrabberTracking(container: FrameLayout, behavior: BottomSheetBehavior<FrameLayout>) {
        if (container.findViewWithTag<View>(GRABBER_TAG) == null) {
            return
        }
        (container.getTag(R.id.modal_bottom_sheet_grabber_callback) as? BottomSheetCallback)?.let {
            behavior.removeBottomSheetCallback(it)
        }
        val callback = object : BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                updateGrabberPosition(container, behavior)
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                updateGrabberPosition(container, behavior)
            }
        }
        container.setTag(R.id.modal_bottom_sheet_grabber_callback, callback)
        behavior.addBottomSheetCallback(callback)
        container.post { updateGrabberPosition(container, behavior) }
    }

    private fun updateGrabberPosition(container: FrameLayout, behavior: BottomSheetBehavior<FrameLayout>) {
        val parent = container.parent as? View ?: return
        val wrapper = container.findViewWithTag<FrameLayout>(GRABBER_WRAPPER_TAG) ?: return
        val handle = wrapper.findViewWithTag<View>(GRABBER_TAG) ?: return
        val metrics = container.resources.displayMetrics
        val padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f, metrics).toInt()

        val content = if (wrapper.childCount > 0) wrapper.getChildAt(0) else return
        val topBar = findTopBar(content)
        val topMargin = if (topBar != null) {
            val topBarLocation = IntArray(2)
            val wrapperLocation = IntArray(2)
            topBar.getLocationInWindow(topBarLocation)
            wrapper.getLocationInWindow(wrapperLocation)
            (topBarLocation[1] - wrapperLocation[1] + padding).coerceAtLeast(padding)
        } else {
            when (behavior.state) {
                BottomSheetBehavior.STATE_EXPANDED -> container.top
                BottomSheetBehavior.STATE_HALF_EXPANDED ->
                    (parent.height * (1f - behavior.halfExpandedRatio)).toInt()
                BottomSheetBehavior.STATE_COLLAPSED ->
                    parent.height - behavior.peekHeight
                else -> parent.height - behavior.peekHeight
            } + padding
        }

        val lp = handle.layoutParams as FrameLayout.LayoutParams
        lp.topMargin = topMargin
        handle.layoutParams = lp
    }

    private fun clearStatusBarInsetForSheet(content: View) {
        val topBar = findTopBar(content) ?: return
        val layoutParams = topBar.layoutParams as? MarginLayoutParams ?: return
        if (layoutParams.topMargin != 0) {
            layoutParams.topMargin = 0
            topBar.requestLayout()
        }
    }

    private fun findTopBar(view: View): TopBar? {
        if (view is TopBar) {
            return view
        }
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                findTopBar(view.getChildAt(i))?.let { return it }
            }
        }
        return null
    }

    private const val GRABBER_WRAPPER_TAG = "modal_sheet_grabber_wrapper"
    private const val GRABBER_TAG = "modal_sheet_grabber"
    private const val HALF_EXPANDED_RATIO = 0.5f
}
