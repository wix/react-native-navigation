package com.reactnativenavigation.views.sheet

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Outline
import android.graphics.Rect
import android.view.Gravity.CENTER_HORIZONTAL
import android.view.Gravity.TOP
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.doOnLayout
import com.facebook.react.bridge.UiThreadUtil
import com.facebook.react.uimanager.PixelUtil
import com.reactnativenavigation.NavigationActivity
import com.reactnativenavigation.options.ButtonOptions
import com.reactnativenavigation.options.Options
import com.reactnativenavigation.options.params.Bool
import com.reactnativenavigation.react.CommandListenerAdapter
import com.reactnativenavigation.react.ReactView
import com.reactnativenavigation.react.events.ComponentType
import com.reactnativenavigation.utils.CoordinatorLayoutUtils.matchParentLP
import com.reactnativenavigation.viewcontrollers.sheet.FixedBottomSheetBehavior
import com.reactnativenavigation.viewcontrollers.stack.topbar.button.ButtonController
import com.reactnativenavigation.viewcontrollers.viewcontroller.ScrollEventListener
import com.reactnativenavigation.views.component.ReactComponent
import com.reactnativenavigation.views.touch.OverlayTouchDelegate
import com.reactnativenavigation.views.touch.TouchDelegateLayout

@SuppressLint("ViewConstructor")
class SheetLayout(context: Context, private val reactView: ReactView) :
        FrameLayout(context),
        ReactComponent,
        ButtonController.OnClickListener,
        TouchDelegateLayout {

    private var willAppearSent = false

    private val touchDelegate: OverlayTouchDelegate

    private var screenHeight = 0

    var isPresented = false;

    private val coordinatorView: CoordinatorLayout

    private val behavior: FixedBottomSheetBehavior<FrameLayout>

    val bottomSheet: FrameLayout
    private var backdrop: View

    private var borderTopRadius = 12
    private var backdropOpacity = 0.5f

    private var activity: NavigationActivity? = null

    private var isChangingHeight = false;

    private var isSettling = false;

    private lateinit var rootView: View

    private lateinit var globalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener

    private val bottomSheetCallback = object : FixedBottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            isSettling = when(newState) {
                FixedBottomSheetBehavior.STATE_SETTLING -> true
                else -> false
            }

            if (newState == FixedBottomSheetBehavior.STATE_HIDDEN) {
                isPresented = false
                activity?.navigator?.dismissModal(reactView.componentId, CommandListenerAdapter())
            }

            if (!isPresented && newState == FixedBottomSheetBehavior.STATE_EXPANDED) {
                isPresented = true
            }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {
            if (!isChangingHeight) {
                onAnimationUpdateBackdrop(slideOffset)
            }
        }
    }

    init {
        coordinatorView = CoordinatorLayout(context).apply {
            layoutParams = CoordinatorLayout.LayoutParams(
                CoordinatorLayout.LayoutParams.MATCH_PARENT,
                CoordinatorLayout.LayoutParams.MATCH_PARENT
            )
        }

        addView(coordinatorView, matchParentLP())

        backdrop = View(context).apply {
            setBackgroundColor(Color.BLACK)
            alpha = 0f
            layoutParams = LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        backdrop.setOnClickListener {
            hide()
        }

        coordinatorView.addView(backdrop, matchParentLP())

        bottomSheet = FrameLayout(context).apply {
            layoutParams = CoordinatorLayout.LayoutParams(
                CoordinatorLayout.LayoutParams.MATCH_PARENT,
                CoordinatorLayout.LayoutParams.MATCH_PARENT
            ).apply {
                gravity = CENTER_HORIZONTAL or TOP
                behavior = FixedBottomSheetBehavior<FrameLayout>()
            }
        }

        coordinatorView.addView(bottomSheet)

        bottomSheet.addView(reactView)

        behavior = FixedBottomSheetBehavior.from(bottomSheet)
        behavior.addBottomSheetCallback(bottomSheetCallback)
        behavior.isFitToContents = true
        behavior.isHideable = true
        behavior.state = FixedBottomSheetBehavior.STATE_HIDDEN
        behavior.skipCollapsed = true
        behavior.saveFlags = FixedBottomSheetBehavior.SAVE_ALL

        // Update the usable sheet height
        screenHeight = toPixel(640.0)
        touchDelegate = OverlayTouchDelegate(this, reactView)

        if (context is NavigationActivity) {
            activity = context

            rootView = context.findViewById(android.R.id.content)
            globalLayoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {
                private var isKeyboardVisible = false

                override fun onGlobalLayout() {
                    val r = Rect()
                    rootView.getWindowVisibleDisplayFrame(r)
                    val screenHeight = rootView.rootView.height
                    val keypadHeight = screenHeight - r.bottom

                    if (keypadHeight > screenHeight * 0.15) {
                        if (!isKeyboardVisible) {
                            isKeyboardVisible = true
                            // Fix autoFocus for input inside content
                            if (isSettling && !isPresented) {
                                UiThreadUtil.runOnUiThread {
                                    present(bottomSheet.height)
                                }
                            }
                        }
                    } else {
                        if (isKeyboardVisible) {
                            isKeyboardVisible = false
                        }
                    }
                }
            }

            rootView.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        screenHeight = height
    }

    override fun hasOverlappingRendering() = true

    private fun onAnimationUpdateBackdrop(progress: Float) {
        if (progress < 0) {
            return
        }

        backdrop.alpha = interpolate(
            progress,
            0.0f to 1f,
            0.0f to backdropOpacity
        )
    }

    fun hide() {
        if (isPresented) {
            behavior.state = FixedBottomSheetBehavior.STATE_HIDDEN
        } else if (behavior.state == FixedBottomSheetBehavior.STATE_HIDDEN) {
            activity?.navigator?.dismissModal(reactView.componentId, CommandListenerAdapter())
        }
    }

    fun present(heightSheet: Int) {
        if (!isPresented) {
            if (height == 0) {
                behavior.makeFullscreen();
            } else {
                bottomSheet.roundTop(toPixel(borderTopRadius.toDouble()))
            }
        }

        var maxScreenHeight = screenHeight
        if (height != -1) {
            maxScreenHeight = screenHeight - toPixel(22.0)
        }

        val adjustedHeight = heightSheet.coerceAtMost(maxScreenHeight)

        if (adjustedHeight != bottomSheet.height || !isPresented) {
            val params = bottomSheet.layoutParams
            if (adjustedHeight > 0) {
                params.height = adjustedHeight
                bottomSheet.layoutParams = params
            } else {
                params.height = CoordinatorLayout.LayoutParams.MATCH_PARENT
                bottomSheet.layoutParams = params
            }

            bottomSheet.doOnLayout {
                behavior.state = FixedBottomSheetBehavior.STATE_EXPANDED
            }
        }
    }

    private fun interpolate(x: Float, inputRange: Pair<Float, Float>, outputRange: Pair<Float, Float>): Float {
        val (minX, maxX) = inputRange
        val (minY, maxY) = outputRange

        return (x - minX) * ((maxY - minY) / (maxX - minX)) + minY
    }

    override fun isReady(): Boolean {
        return reactView.isReady
    }

    override fun asView(): ViewGroup {
        return this
    }

    override fun destroy() {
        reactView.destroy()

        if (rootView != null) {
            rootView.viewTreeObserver.removeOnGlobalLayoutListener(globalLayoutListener)
        }
    }

    fun start() {
        reactView.start()
    }

    fun sendComponentWillStart() {
        if (!willAppearSent) {
            reactView.sendComponentWillStart(ComponentType.Component)
        }
        willAppearSent = true
    }

    fun sendComponentStart() {
        reactView.sendComponentStart(ComponentType.Component)
    }

    fun sendComponentStop() {
        willAppearSent = false
        reactView.sendComponentStop(ComponentType.Component)
    }

    override fun sendOnNavigationButtonPressed(buttonId: String) {
        reactView.sendOnNavigationButtonPressed(buttonId)
    }

    fun applyOptions(options: Options) {
        touchDelegate.interceptTouchOutside = options.overlayOptions.interceptTouchOutside
    }

    fun setInterceptTouchOutside(interceptTouchOutside: Bool?) {
        touchDelegate.interceptTouchOutside = interceptTouchOutside!!
    }

    override fun getScrollEventListener(): ScrollEventListener? {
        return reactView.scrollEventListener
    }

    override fun dispatchTouchEventToJs(event: MotionEvent?) {
        reactView.dispatchTouchEventToJs(event)
    }

    override fun isRendered(): Boolean {
        return reactView.isRendered
    }

    override fun onPress(button: ButtonOptions) {
        reactView.sendOnNavigationButtonPressed(button.id)
    }


    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return touchDelegate.onInterceptTouchEvent(ev!!)
    }

    override fun superOnInterceptTouchEvent(event: MotionEvent): Boolean {
        return super.onInterceptTouchEvent(event)
    }

    fun setBorderTopRadius(value: Int) {
        borderTopRadius = value
    }

    fun setBackdropOpacity(value: Float) {
        backdropOpacity = value
    }

    fun setSheetBackgroundColor(color: Int) {
        reactView.setBackgroundColor(color)
    }
}

fun toPixel(value: Double): Int = PixelUtil.toPixelFromDIP(value).toInt()

fun View.roundTop(radius: Int) {
    if (radius == 0) {
        outlineProvider = null
        clipToOutline = false
    } else {
        outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                outline.setRoundRect(0, 0, view.width, view.height + radius * 2, radius.toFloat())
            }
        }
        clipToOutline = true
    }
}
