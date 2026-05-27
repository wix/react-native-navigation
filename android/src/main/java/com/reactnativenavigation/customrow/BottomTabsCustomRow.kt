package com.reactnativenavigation.customrow

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Outline
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.FrameLayout
import com.reactnativenavigation.views.bottomtabs.BottomTabs
import com.reactnativenavigation.views.bottomtabs.CustomBottomTabItemView

/**
 * Floating row that hosts the React-rendered `CustomBottomTabItemView`
 * cells produced by RNN's existing custom-tab path. Mimics the iOS
 * `RNNBottomTabsCustomRow` (Approach B): the underlying `BottomTabs` view
 * is kept for state but its visuals are hidden; this view is the only
 * thing the user sees.
 *
 * Strict zero-touch on the existing tabs implementation: only public APIs
 * of `BottomTabs` (`hasCustomItemViews`, `getCustomItemView`,
 * `getItemsCount`, `setCurrentItem`) are used, plus standard `View`
 * methods.
 */
@SuppressLint("ViewConstructor")
class BottomTabsCustomRow(
    context: Context,
    private val bottomTabs: BottomTabs,
) : FrameLayout(context) {

    private val cells = mutableListOf<Cell>()
    private var currentOptions: BottomTabsCustomRowOptions = BottomTabsCustomRowConfigStore.get()
    private var selectedIndex: Int = bottomTabs.currentItem
    private var safeBottomInsetPx: Int = 0

    /**
     * Visible chrome (background colour, rounded corners, shadow) that also
     * hosts the cell views as children. Lives as a dedicated child of the
     * row so it can be inset from the row's bottom by
     * `safeBottom + bottomMargin` and only paint over the content area —
     * mirrors iOS's `backgroundColorView` / `backgroundEffectView`. Hosting
     * the cells inside it ensures cells render *above* the chrome regardless
     * of elevation.
     */
    private val backgroundView: FrameLayout = FrameLayout(context).apply {
        clipToOutline = true
        clipChildren = true
        outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                val r = effectiveCornerRadiusPx()
                outline.setRoundRect(0, 0, view.width, view.height, r)
            }
        }
    }

    private val configListener: (BottomTabsCustomRowOptions) -> Unit = { opts ->
        post { applyOptions(opts) }
    }

    init {
        setWillNotDraw(true)
        clipChildren = false
        clipToPadding = false
        addView(backgroundView, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
        BottomTabsCustomRowConfigStore.addListener(configListener)
        applyOptions(currentOptions)
        rebuildCells()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        BottomTabsCustomRowConfigStore.removeListener(configListener)
    }

    fun rebuildCells() {
        for (cell in cells) {
            (cell.parent as? ViewGroup)?.removeView(cell)
        }
        cells.clear()
        if (!bottomTabs.hasCustomItemViews()) return

        val count = bottomTabs.itemsCount
        for (i in 0 until count) {
            val itemView = bottomTabs.getCustomItemView(i) ?: continue
            (itemView.parent as? ViewGroup)?.removeView(itemView)
            val cell = Cell(context, i, itemView).also {
                it.setOnClickListener { _ ->
                    bottomTabs.setCurrentItem(i, true)
                    setSelectedIndex(i)
                }
            }
            backgroundView.addView(
                cell,
                FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
            )
            cells.add(cell)
        }
        setSelectedIndex(selectedIndex)
        requestLayout()
    }

    fun setSelectedIndex(index: Int) {
        selectedIndex = index
        for (cell in cells) cell.itemView.setItemSelected(cell.index == index)
    }

    fun applyOptions(options: BottomTabsCustomRowOptions) {
        currentOptions = options

        val solidColor = options.backgroundColor
        val effect = options.backgroundEffect
        val backgroundColorToUse = solidColor
            ?: if (effect == BottomTabsCustomRowOptions.BackgroundEffect.None)
                Color.TRANSPARENT
            else
                materialChromeColor()

        backgroundView.setBackgroundColor(backgroundColorToUse)

        // NOTE: Android does not expose a true blur-behind API for arbitrary
        // views (the `RenderEffect.createBlurEffect` API blurs the view's own
        // rendered content, which would smear the cells we host). For
        // `glass` / `blur` we therefore render an opaque-ish chrome material
        // colour; a future enhancement can swap this for `eightbitlab/
        // BlurView` to get true blur-behind on Android.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            backgroundView.setRenderEffect(null)
        }

        // iOS gets a soft visual lift from `UIGlassEffect`'s built-in ring +
        // highlight. Android has no equivalent, so we emulate it with a
        // low-elevation shadow tinted at low alpha. Hard `elevation = 8` (the
        // Material default) looks much heavier than the iOS reference, so
        // we stay subtle: ~3dp elevation, ~30% / 12% shadow alpha.
        backgroundView.elevation = dp(3f)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            backgroundView.outlineSpotShadowColor = Color.argb(0x4C, 0, 0, 0)
            backgroundView.outlineAmbientShadowColor = Color.argb(0x1E, 0, 0, 0)
        }
        backgroundView.invalidateOutline()
        requestLayout()
    }

    private fun materialChromeColor(): Int {
        val typed = TypedValue()
        val resolved = context.theme.resolveAttribute(
            android.R.attr.colorBackground, typed, true
        )
        val base = if (resolved && typed.type >= TypedValue.TYPE_FIRST_COLOR_INT &&
            typed.type <= TypedValue.TYPE_LAST_COLOR_INT
        ) typed.data else Color.WHITE
        return alphaOver(base, 0xF0)
    }

    private fun alphaOver(color: Int, alpha: Int): Int {
        return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color))
    }

    private fun effectiveCornerRadiusPx(): Float {
        val dp = currentOptions.cornerRadius ?: 28f
        return dp(dp)
    }

    /**
     * Content-area height (where cells live), excluding the bottom safe-area
     * inset and bottomMargin.
     */
    fun effectiveContentHeightPx(nativeBarHeightPx: Int): Int {
        val heightOption = currentOptions.height
        if (heightOption != null) {
            val configured = dp(heightOption).toInt()
            // JS height targets iOS floating chrome; keep Android shell tight so
            // the pill sits on the tab bar without a tall empty box above nav.
            return minOf(configured, nativeBarHeightPx.coerceAtLeast(dp(52f).toInt()))
        }
        // Default: a touch taller than the native bar's content area to give
        // icon + label + pill enough room — mirrors iOS's +18pt default.
        val nativeContent = (nativeBarHeightPx - safeBottomInsetPx).coerceAtLeast(0)
        return (nativeContent + dp(18f)).toInt()
    }

    /**
     * Total row height including the bottom safe-area inset and bottomMargin
     * — mirrors iOS's `desiredRowHeightForNativeTabBarHeight:safeBottom:`.
     */
    fun effectiveTotalHeightPx(nativeBarHeightPx: Int): Int =
        effectiveContentHeightPx(nativeBarHeightPx) + safeBottomInsetPx + effectiveBottomMarginPx()

    fun effectiveHorizontalMarginPx(): Int = dp(currentOptions.horizontalMargin ?: 16f).toInt()
    fun effectiveBottomMarginPx(): Int = dp(currentOptions.bottomMargin ?: 0f).toInt()

    fun setSafeBottomInsetPx(insetPx: Int) {
        if (safeBottomInsetPx == insetPx) return
        safeBottomInsetPx = insetPx
        requestLayout()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        val w = width
        // Visible chrome only covers the content area — never the safe-area
        // strip below (matches iOS where `backgroundEffectView.frame =
        // content` excludes `safe.bottom + bottomMargin`).
        val contentBottom = (height - safeBottomInsetPx - effectiveBottomMarginPx()).coerceAtLeast(0)
        val bgSpec = MeasureSpec.makeMeasureSpec(w, MeasureSpec.EXACTLY)
        val bgHeightSpec = MeasureSpec.makeMeasureSpec(contentBottom, MeasureSpec.EXACTLY)
        backgroundView.measure(bgSpec, bgHeightSpec)
        backgroundView.layout(0, 0, w, contentBottom)
        backgroundView.invalidateOutline()
        layoutCells(w, contentBottom)
    }

    private fun layoutCells(parentWidth: Int, parentHeight: Int) {
        if (cells.isEmpty() || parentWidth <= 0 || parentHeight <= 0) return
        val per = parentWidth.toFloat() / cells.size.toFloat()
        for (i in cells.indices) {
            val cell = cells[i]
            val l = (i * per).toInt()
            val r = ((i + 1) * per).toInt()
            val widthSpec = MeasureSpec.makeMeasureSpec(r - l, MeasureSpec.EXACTLY)
            val heightSpec = MeasureSpec.makeMeasureSpec(parentHeight, MeasureSpec.EXACTLY)
            cell.measure(widthSpec, heightSpec)
            // Cells are children of `backgroundView`, laid out in its local
            // coordinate space (which is already 0..contentBottom).
            cell.layout(l, 0, r, parentHeight)
        }
    }

    private fun dp(value: Float): Float =
        value * resources.displayMetrics.density

    @SuppressLint("ViewConstructor")
    private class Cell(
        context: Context,
        val index: Int,
        val itemView: CustomBottomTabItemView,
    ) : FrameLayout(context) {
        init {
            isClickable = true
            isFocusable = true
            // Host the React item view inside this cell so it inherits taps
            // we don't consume.
            addView(itemView, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
        }

        override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
            // Bypass the React view's pass-through and let this Cell receive
            // the click event directly.
            return onTouchEvent(ev) || super.dispatchTouchEvent(ev)
        }
    }
}
