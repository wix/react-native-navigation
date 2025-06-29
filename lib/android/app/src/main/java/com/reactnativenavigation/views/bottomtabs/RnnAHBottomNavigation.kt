package com.reactnativenavigation.views.bottomtabs

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.WindowManager
import android.view.animation.AccelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.Px
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.animation.addListener
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationBehavior
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationFABBehavior
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.aurelhubert.ahbottomnavigation.AHHelper
import com.aurelhubert.ahbottomnavigation.AHHelper.fill
import com.aurelhubert.ahbottomnavigation.AHHelper.map
import com.aurelhubert.ahbottomnavigation.AHTextView
import com.aurelhubert.ahbottomnavigation.R
import com.aurelhubert.ahbottomnavigation.R.color.colorBottomNavigationAccent
import com.aurelhubert.ahbottomnavigation.R.color.colorBottomNavigationActiveColored
import com.aurelhubert.ahbottomnavigation.R.color.colorBottomNavigationDisable
import com.aurelhubert.ahbottomnavigation.R.color.colorBottomNavigationInactive
import com.aurelhubert.ahbottomnavigation.R.color.colorBottomNavigationInactiveColored
import com.aurelhubert.ahbottomnavigation.R.dimen.bottom_navigation_icon
import com.aurelhubert.ahbottomnavigation.R.dimen.bottom_navigation_margin_top_active
import com.aurelhubert.ahbottomnavigation.R.dimen.bottom_navigation_notification_elevation
import com.aurelhubert.ahbottomnavigation.R.dimen.bottom_navigation_small_margin_top_active
import com.aurelhubert.ahbottomnavigation.R.dimen.bottom_navigation_small_selected_width_difference
import com.aurelhubert.ahbottomnavigation.R.styleable.AHBottomNavigationBehavior_Params
import com.aurelhubert.ahbottomnavigation.R.styleable.AHBottomNavigationBehavior_Params_accentColor
import com.aurelhubert.ahbottomnavigation.R.styleable.AHBottomNavigationBehavior_Params_disableColor
import com.aurelhubert.ahbottomnavigation.R.styleable.AHBottomNavigationBehavior_Params_inactiveColor
import com.aurelhubert.ahbottomnavigation.R.styleable.AHBottomNavigationBehavior_Params_selectedBackgroundVisible
import com.aurelhubert.ahbottomnavigation.R.styleable.AHBottomNavigationBehavior_Params_translucentNavigationEnabled
import com.aurelhubert.ahbottomnavigation.notification.AHNotification
import com.aurelhubert.ahbottomnavigation.notification.AHNotificationHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Locale
import kotlin.math.max

/**
 * AHBottomNavigationLayout
 * Material Design guidelines : https://www.google.com/design/spec/components/bottom-navigation.html
 */
open class RnnAHBottomNavigation : FrameLayout {
    // Title state
    enum class TitleState {
        SHOW_WHEN_ACTIVE,
        SHOW_WHEN_ACTIVE_FORCE,
        ALWAYS_SHOW,
        ALWAYS_HIDE
    }

    // Listener
    private var tabSelectedListener: OnTabSelectedListener? = null
    private var navigationPositionListener: OnNavigationPositionListener? = null

    // Variables
    private lateinit var context: Context
    private lateinit var resources: Resources
    private val items = ArrayList<AHBottomNavigationItem>()
    private val views = ArrayList<View>()
    private var bottomNavigationBehavior: AHBottomNavigationBehavior<RnnAHBottomNavigation>? = null
    private var linearLayoutContainer: LinearLayout? = null
    private var backgroundColorView: ViewGroup? = null
    private var circleRevealAnim: Animator? = null
    private var colored = false
    private var selectedBackgroundVisible = false
    /**
     * Return if the translucent navigation is enabled
     */
    /**
     * Set the translucent navigation value
     */
    private var isTranslucentNavigationEnabled = false
    private var notifications: MutableList<AHNotification>? = AHNotification.generateEmptyList(MAX_ITEMS)
    private val itemsEnabledStates = arrayOf(true, true, true, true, true)
    private var isBehaviorTranslationSet = false
    private var currentItem = 0
    private var currentColor = 0
    private var behaviorTranslationEnabled = true
    private var needHideBottomNavigation = false
    private var hideBottomNavigationWithAnimation = false
    private var soundEffectsEnabled = true

    // Variables (Styles)
    private val titleTypeface = ArrayList<Typeface?>()
    private var defaultBackgroundColor = Color.WHITE
    private var defaultBackgroundResource = 0
    private var activePaddingTop = 0
    private var iconActiveColor = ArrayList<Int?>(MAX_ITEMS)
    private var iconInactiveColor = ArrayList<Int?>(MAX_ITEMS)

    private val iconHeight = ArrayList<Int?>(MAX_ITEMS)
    private val iconWidth = ArrayList<Int?>(MAX_ITEMS)


    private val titleActiveColor = ArrayList<Int?>(MAX_ITEMS)
    private val titleInactiveColor = ArrayList<Int?>(MAX_ITEMS)

    private val iconDisableColor = ArrayList<Int?>(MAX_ITEMS)
    private val titleDisableColor = ArrayList<Int?>(MAX_ITEMS)

    private val coloredTitleColorActive = ArrayList<Int?>(MAX_ITEMS)
    private val coloredTitleColorInactive = ArrayList<Int?>(MAX_ITEMS)
    private val titleActiveTextSize = ArrayList<Float>(MAX_ITEMS)
    private val titleInactiveTextSize = ArrayList<Float>(MAX_ITEMS)
    private var bottomNavigationHeight = 0
    private var navigationBarHeight = 0
    private var selectedItemWidth = 0f
    private var notSelectedItemWidth = 0f
    private var forceTint = false
    private var preferLargeIcons = false
    private var titleState = TitleState.SHOW_WHEN_ACTIVE

    private var activeMarginTop = 0
    private var widthDifference = 0f
    private var defaultIconHeight = 0
    private var defaultIconWidth = 0

    // Notifications
    @ColorInt
    private var notificationTextColor = 0

    @ColorInt
    private var notificationBackgroundColor = 0
    private var notificationBackgroundDrawable: Drawable? = null
    private var notificationTypeface: Typeface? = null
    private var notificationActiveMarginLeft = 0
    private var notificationInactiveMarginLeft = 0
    private var notificationActiveMarginTop = 0
    private var notificationInactiveMarginTop = 0
    private var notificationAnimationDuration: Long = 0
    private var defaultNotificationElevation = 0
    private var animateTabSelection = true

    /**
     * Constructors
     */
    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    override fun setSoundEffectsEnabled(soundEffectsEnabled: Boolean) {
        super.setSoundEffectsEnabled(soundEffectsEnabled)
        this.soundEffectsEnabled = soundEffectsEnabled
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        createItems()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (!isBehaviorTranslationSet) {
            //The translation behavior has to be set up after the super.onMeasure has been called.
            setBehaviorTranslationEnabled(behaviorTranslationEnabled)
            isBehaviorTranslationSet = true
        }
    }

    override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        bundle.putParcelable("superState", super.onSaveInstanceState())
        bundle.putInt("current_item", currentItem)
        bundle.putParcelableArrayList("notifications", ArrayList(notifications))
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        var state: Parcelable? = state
        if (state is Bundle) {
            val bundle = state
            currentItem = bundle.getInt("current_item")
            notifications = bundle.getParcelableArrayList("notifications") ?: AHNotification.generateEmptyList(
                MAX_ITEMS
            )
            state = bundle.getParcelable("superState")
        }
        super.onRestoreInstanceState(state)
    }

    /////////////
    // PRIVATE //
    /////////////
    /**
     * Init
     */
    private fun init(context: Context, attrs: AttributeSet?) {
        this.context = context
        resources = this.context.resources
        defaultNotificationElevation = resources.getDimensionPixelSize(
            bottom_navigation_notification_elevation
        )
        activePaddingTop = resources.getDimension(bottom_navigation_margin_top_active).toInt()
        activeMarginTop = resources.getDimension(bottom_navigation_small_margin_top_active).toInt()
        widthDifference = resources.getDimension(bottom_navigation_small_selected_width_difference)
        defaultIconHeight = resources.getDimensionPixelSize(bottom_navigation_icon)
        defaultIconWidth = resources.getDimensionPixelSize(bottom_navigation_icon)

        // Icon colors
        fill(iconActiveColor, MAX_ITEMS, null)
        fill(iconInactiveColor, MAX_ITEMS, null)
        fill(iconDisableColor, MAX_ITEMS, null)
        // Title colors
        fill(titleActiveColor, MAX_ITEMS, null)
        fill(titleInactiveColor, MAX_ITEMS, null)
        fill(titleDisableColor, MAX_ITEMS, null)

        fill(iconWidth, MAX_ITEMS, null)
        fill(iconHeight, MAX_ITEMS, null)

        fill(titleTypeface, MAX_ITEMS, null)
        fill(titleActiveTextSize, MAX_ITEMS, null)
        fill(titleInactiveTextSize, MAX_ITEMS, null)

        // Colors for colored bottom navigation
        fill(
            coloredTitleColorActive,
            MAX_ITEMS,
            ContextCompat.getColor(context, colorBottomNavigationActiveColored)
        )
        fill(
            coloredTitleColorInactive,
            MAX_ITEMS,
            ContextCompat.getColor(context, colorBottomNavigationInactiveColored)
        )

        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, AHBottomNavigationBehavior_Params, 0, 0)
            try {
                selectedBackgroundVisible = ta.getBoolean(
                    AHBottomNavigationBehavior_Params_selectedBackgroundVisible, false)
                isTranslucentNavigationEnabled = ta.getBoolean(
                    AHBottomNavigationBehavior_Params_translucentNavigationEnabled, false)

                map(titleActiveColor) {
                    ta.getColor(
                        AHBottomNavigationBehavior_Params_accentColor,
                        ContextCompat.getColor(context, colorBottomNavigationAccent)
                    )
                }
                map(titleInactiveColor) {
                    ta.getColor(
                        AHBottomNavigationBehavior_Params_inactiveColor,
                        ContextCompat.getColor(context, colorBottomNavigationInactive)
                    )
                }
                map(iconDisableColor) {
                    ta.getColor(
                        AHBottomNavigationBehavior_Params_disableColor,
                        ContextCompat.getColor(context, colorBottomNavigationDisable)
                    )
                }

                map(coloredTitleColorActive) {
                    ta.getColor(
                        R.styleable.AHBottomNavigationBehavior_Params_coloredActive,
                        ContextCompat.getColor(context, R.color.colorBottomNavigationActiveColored)
                    )
                }

                map(coloredTitleColorInactive) {
                    ta.getColor(
                        R.styleable.AHBottomNavigationBehavior_Params_coloredInactive,
                        ContextCompat.getColor(context, R.color.colorBottomNavigationInactiveColored)
                    )
                }

                colored = ta.getBoolean(R.styleable.AHBottomNavigationBehavior_Params_colored, false)
            } finally {
                ta.recycle()
            }
        }

        notificationTextColor = ContextCompat.getColor(context, android.R.color.white)
        bottomNavigationHeight = resources.getDimension(R.dimen.bottom_navigation_height).toInt()


        // Notifications
        notificationActiveMarginLeft = resources.getDimension(R.dimen.bottom_navigation_notification_margin_left_active).toInt()
        notificationInactiveMarginLeft = resources.getDimension(R.dimen.bottom_navigation_notification_margin_left).toInt()
        notificationActiveMarginTop = resources.getDimension(R.dimen.bottom_navigation_notification_margin_top_active).toInt()
        notificationInactiveMarginTop = resources.getDimension(R.dimen.bottom_navigation_notification_margin_top).toInt()
        notificationAnimationDuration = 150

        ViewCompat.setElevation(this, resources.getDimension(R.dimen.bottom_navigation_elevation))
        clipToPadding = false

        layoutParams = ViewGroup.LayoutParams(WRAP_CONTENT, bottomNavigationHeight)
    }

    /**
     * Create the items in the bottom navigation
     */
    protected open fun createItems() {
        if (items.size < MIN_ITEMS) {
            Log.w(TAG, "The items list should have at least 3 items")
        } else if (items.size > MAX_ITEMS) {
            Log.w(TAG, "The items list should not have more than 5 items")
        }

        removeAllViews()
        views.clear()

        val layoutHeight = resources.getDimension(R.dimen.bottom_navigation_height).toInt()

        linearLayoutContainer = LinearLayout(context).also {
            it.orientation = LinearLayout.HORIZONTAL
            it.layoutParams = LayoutParams(WRAP_CONTENT, layoutHeight)

            if (isClassic) {
                createClassicItems(it)
            } else {
                createSmallItems(it)
            }
        }

        backgroundColorView = FrameLayout(context).also {
            // AMITD core change: linearLayoutContainer is child of backgroundColorView, rather than the bottom tabs view (which has changed from View to FrameLayout)
            it.addView(linearLayoutContainer)

            addView(it, LayoutParams(WRAP_CONTENT, calculateHeight(layoutHeight)))
        }
        bottomNavigationHeight = layoutHeight

        // Force a request layout after all the items have been created
        post { this.requestLayout() }
    }

    private val isClassic: Boolean
        /**
         * Check if items must be classic
         *
         * @return true if classic (icon + title)
         */
        get() {
            if (preferLargeIcons && items.size == MIN_ITEMS) return true
            return titleState != TitleState.ALWAYS_HIDE && titleState != TitleState.SHOW_WHEN_ACTIVE_FORCE &&
                    (items.size == MIN_ITEMS || titleState == TitleState.ALWAYS_SHOW)
        }

    @SuppressLint("NewApi", "ResourceType")
    private fun calculateHeight(layoutHeight: Int): Int {
        if (!isTranslucentNavigationEnabled) return layoutHeight

        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        if (resourceId > 0) navigationBarHeight = resources.getDimensionPixelSize(resourceId)

        val attrs = intArrayOf(android.R.attr.fitsSystemWindows, android.R.attr.windowTranslucentNavigation)
        val typedValue = getContext().theme.obtainStyledAttributes(attrs)

        val translucentNavigation = typedValue.getBoolean(1, true)
        typedValue.recycle()

        return if (hasImmersive() && translucentNavigation) (layoutHeight + navigationBarHeight) else layoutHeight
    }

    @SuppressLint("NewApi")
    fun hasImmersive(): Boolean {
        val d = (getContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay

        val realDisplayMetrics = DisplayMetrics()
        d.getRealMetrics(realDisplayMetrics)

        val realHeight = realDisplayMetrics.heightPixels
        val realWidth = realDisplayMetrics.widthPixels

        val displayMetrics = DisplayMetrics()
        d.getMetrics(displayMetrics)

        val displayHeight = displayMetrics.heightPixels
        val displayWidth = displayMetrics.widthPixels

        return (realWidth > displayWidth) || (realHeight > displayHeight)
    }

    /**
     * Create classic items (only 3 items in the bottom navigation)
     *
     * @param linearLayout The layout where the items are added
     */
    private fun createClassicItems(linearLayout: LinearLayout) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val height = resources.getDimension(R.dimen.bottom_navigation_height)
        var minWidth = resources.getDimension(R.dimen.bottom_navigation_min_width)
        var maxWidth = resources.getDimension(R.dimen.bottom_navigation_max_width)

        if (titleState == TitleState.ALWAYS_SHOW && items.size > MIN_ITEMS) {
            minWidth = resources.getDimension(R.dimen.bottom_navigation_small_inactive_min_width)
            maxWidth = resources.getDimension(R.dimen.bottom_navigation_small_inactive_max_width)
        }

        val layoutWidth = width
        if (/*layoutWidth == 0 || */items.size == 0) { // AMITD Effectively blocked wrap-content layout ==> removed
            return
        }

        // AMITD no longer need
//        var itemWidth = (layoutWidth / items.size).toFloat()
//            .coerceIn(minWidth, maxWidth)

        for (i in items.indices) {
            val current = currentItem == i
            val itemIndex = i
            val item = items[itemIndex]

            val view = inflater.inflate(R.layout.bottom_navigation_item, this, false)
            val container = view.findViewById<FrameLayout>(R.id.bottom_navigation_container).also {
                it.layoutParams.width = WRAP_CONTENT
            }
            val icon = view.findViewById<ImageView>(R.id.bottom_navigation_item_icon)
            val title = view.findViewById<AHTextView>(R.id.bottom_navigation_item_title).also {
                it.layoutParams.width = WRAP_CONTENT
            }
            val notification = view.findViewById<AHTextView>(R.id.bottom_navigation_notification)

            icon.layoutParams.width = getIconWidth(itemIndex)
            icon.layoutParams.height = getIconHeight(itemIndex)

            icon.setImageDrawable(item.getDrawable(context))
            if (titleState == TitleState.ALWAYS_HIDE || item.getTitle(context).isEmpty()) {
                title.visibility = GONE
                if (!animateTabSelection) {
                    AHHelper.updateMargin(
                        icon,
                        0,
                        0,
                        0,
                        0
                    )
                }
                (icon.layoutParams as LayoutParams).gravity = Gravity.CENTER
                (notification.layoutParams as MarginLayoutParams).topMargin =
                    (bottomNavigationHeight - getIconHeight(itemIndex)) / 2 - dpToPx(4)
            } else {
                title.text = item.getTitle(context)
            }

            title.typeface = titleTypeface[i]

            if (titleState == TitleState.ALWAYS_SHOW && items.size > MIN_ITEMS) {
                container.setPadding(0, container.paddingTop, 0, container.paddingBottom)
            }

            if (current) {
                if (selectedBackgroundVisible) {
                    view.isSelected = true
                }
                icon.isSelected = true
                // Update margins (icon & notification)
                if (view.layoutParams is MarginLayoutParams && animateTabSelection) {
                    val p = icon.layoutParams as MarginLayoutParams
                    p.setMargins(p.leftMargin, activePaddingTop, p.rightMargin, p.bottomMargin)

                    val paramsNotification = notification.layoutParams as MarginLayoutParams
                    paramsNotification.setMargins(
                        notificationActiveMarginLeft,
                        paramsNotification.topMargin,
                        paramsNotification.rightMargin,
                        paramsNotification.bottomMargin
                    )

                    view.requestLayout()
                }
            } else {
                icon.isSelected = false
                val paramsNotification = notification.layoutParams as MarginLayoutParams
                paramsNotification.setMargins(
                    notificationInactiveMarginLeft,
                    paramsNotification.topMargin,
                    paramsNotification.rightMargin,
                    paramsNotification.bottomMargin
                )
            }

            if (colored) {
                if (current) {
                    setBackgroundColor(item.getColor(context))
                    currentColor = item.getColor(context)
                }
            } else {
                if (defaultBackgroundResource != 0) {
                    setBackgroundResource(defaultBackgroundResource)
                } else {
                    setBackgroundColor(defaultBackgroundColor)
                }
            }

            title.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                if (current) getActiveTextSize(i) else getInactiveTextSize(i)
            )

            if (itemsEnabledStates[i]) {
                view.setOnClickListener { v: View? -> updateItems(itemIndex, true) }
                icon.setImageDrawable(
                    AHHelper.getTintDrawable(
                        items[i].getDrawable(context),
                        if (current) iconActiveColor[i] else iconInactiveColor[i],
                        forceTint
                    )
                )
                title.setTextColor(if (current) titleActiveColor[i] else titleInactiveColor[i])
                view.isSoundEffectsEnabled = soundEffectsEnabled
            } else {
                icon.setImageDrawable(
                    AHHelper.getTintDrawable(
                        items[i].getDrawable(context),
                        iconDisableColor[i], forceTint
                    )
                )
                title.setTextColor(titleDisableColor[i])
            }

            if (item.tag != null) view.tag = item.tag

            // AMITD core change
            val params = LayoutParams(/*itemWidth*/WRAP_CONTENT, height.toInt())
            linearLayout.addView(view, params)
            views.add(view)
            setTabAccessibilityLabel(itemIndex, currentItem)
        }

        updateNotifications(true,
            UPDATE_ALL_NOTIFICATIONS
        )
    }

    private fun getIconHeight(index: Int): Int {
        val iconHeight = iconHeight[index]
        return if (iconHeight == null) defaultIconHeight else dpToPx(iconHeight)
    }

    private fun getIconWidth(index: Int): Int {
        val iconWidth = iconWidth[index]
        return if (iconWidth == null) defaultIconWidth else dpToPx(iconWidth)
    }

    private fun getInactiveTextSize(index: Int): Float {
        if (titleInactiveTextSize[index] != null) return titleInactiveTextSize[index]
        return if (titleState == TitleState.ALWAYS_SHOW && items.size > MIN_ITEMS) {
            resources.getDimension(R.dimen.bottom_navigation_text_size_forced_inactive)
        } else {
            resources.getDimension(R.dimen.bottom_navigation_text_size_inactive)
        }
    }

    private fun getActiveTextSize(index: Int): Float {
        if (titleActiveTextSize[index] != null) return titleActiveTextSize[index]
        return if (titleState == TitleState.ALWAYS_SHOW && items.size > MIN_ITEMS) {
            resources.getDimension(R.dimen.bottom_navigation_text_size_forced_active)
        } else {
            resources.getDimension(R.dimen.bottom_navigation_text_size_active)
        }
    }

    /**
     * Create small items (more than 3 items in the bottom navigation)
     *
     * @param linearLayout The layout where the items are added
     */
    private fun createSmallItems(linearLayout: LinearLayout) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val height = resources.getDimension(R.dimen.bottom_navigation_height)
        val minWidth = resources.getDimension(R.dimen.bottom_navigation_small_inactive_min_width)
        val maxWidth = resources.getDimension(R.dimen.bottom_navigation_small_inactive_max_width)

        val layoutWidth = width
        if (layoutWidth == 0 || items.size == 0) {
            return
        }

        var itemWidth = (layoutWidth / items.size).toFloat().coerceIn(minWidth .. maxWidth)

        selectedItemWidth = itemWidth + items.size * widthDifference
        itemWidth -= widthDifference
        notSelectedItemWidth = itemWidth


        for (i in items.indices) {
            val itemIndex = i
            val current = currentItem == i
            val item = items[itemIndex]

            val view = inflater.inflate(R.layout.bottom_navigation_small_item, this, false)
            val icon = view.findViewById<ImageView>(R.id.bottom_navigation_small_item_icon)
            val title = view.findViewById<AHTextView>(R.id.bottom_navigation_small_item_title)
            val notification = view.findViewById<AHTextView>(R.id.bottom_navigation_notification)
            icon.setImageDrawable(item.getDrawable(context))

            icon.layoutParams.width = getIconWidth(itemIndex)
            icon.layoutParams.height = getIconHeight(itemIndex)

            if (titleState != TitleState.ALWAYS_HIDE) {
                title.text = item.getTitle(context)
            }
            if ((titleState == TitleState.ALWAYS_HIDE || item.getTitle(context)
                    .isEmpty()) && titleState != TitleState.SHOW_WHEN_ACTIVE && titleState != TitleState.SHOW_WHEN_ACTIVE_FORCE
            ) {
                title.visibility = GONE
                (icon.layoutParams as LayoutParams).gravity = Gravity.CENTER
                AHHelper.updateMargin(
                    icon,
                    0,
                    0,
                    0,
                    0
                )
            }

            val activeTextSize = getActiveTextSize(i)
            if (activeTextSize != 0f) {
                title.setTextSize(TypedValue.COMPLEX_UNIT_PX, activeTextSize)
            }

            title.typeface = titleTypeface[i]

            if (current) {
                if (selectedBackgroundVisible) {
                    view.isSelected = true
                }
                icon.isSelected = true

                // Update margins (icon & notification)
                if (titleState != TitleState.ALWAYS_HIDE && animateTabSelection) {
                    if (view.layoutParams is MarginLayoutParams) {
                        val p = icon.layoutParams as MarginLayoutParams
                        p.setMargins(p.leftMargin, activeMarginTop, p.rightMargin, p.bottomMargin)

                        val paramsNotification = notification.layoutParams as MarginLayoutParams
                        paramsNotification.setMargins(
                            notificationActiveMarginLeft, notificationActiveMarginTop,
                            paramsNotification.rightMargin, paramsNotification.bottomMargin
                        )

                        view.requestLayout()
                    }
                }
            } else {
                icon.isSelected = false
                if (animateTabSelection) {
                    val paramsNotification = notification.layoutParams as MarginLayoutParams
                    paramsNotification.setMargins(
                        notificationInactiveMarginLeft,
                        notificationInactiveMarginTop,
                        paramsNotification.rightMargin,
                        paramsNotification.bottomMargin
                    )
                }
            }

            if (colored) {
                if (i == currentItem) {
                    setBackgroundColor(item.getColor(context))
                    currentColor = item.getColor(context)
                }
            } else {
                if (defaultBackgroundResource != 0) {
                    setBackgroundResource(defaultBackgroundResource)
                } else {
                    setBackgroundColor(defaultBackgroundColor)
                }
            }

            if (itemsEnabledStates[i]) {
                icon.setImageDrawable(
                    AHHelper.getTintDrawable(
                        items[i].getDrawable(context),
                        if (currentItem == i) iconActiveColor[i] else iconInactiveColor[i],
                        forceTint
                    )
                )
                title.setTextColor(if (currentItem == i) titleActiveColor[i] else titleInactiveColor[i])
                title.alpha = (if (currentItem == i) 1 else 0).toFloat()
                view.setOnClickListener {
                    updateSmallItems(itemIndex, true)
                }
                view.isSoundEffectsEnabled = soundEffectsEnabled
            } else {
                icon.setImageDrawable(
                    AHHelper.getTintDrawable(
                        items[i].getDrawable(context),
                        iconDisableColor[i], forceTint
                    )
                )
                title.setTextColor(titleDisableColor[i])
                title.alpha = 0f
            }

            var width = if (i == currentItem) selectedItemWidth.toInt() else itemWidth.toInt()

            if (titleState == TitleState.ALWAYS_HIDE) {
                width = (itemWidth * 1.16).toInt()
            }

            if (item.tag != null) view.tag = item.tag

            val params = LayoutParams(width, height.toInt())
            linearLayout.addView(view, params)
            views.add(view)
            setTabAccessibilityLabel(itemIndex, currentItem)
        }

        updateNotifications(true,
            UPDATE_ALL_NOTIFICATIONS
        )
    }

    private fun setTabAccessibilityLabel(itemIndex: Int, currentItem: Int) {
        val item = items[itemIndex]
        var contentDescription = if (currentItem == itemIndex) "selected, " else ""
        if (item.getTitle(context) != null) contentDescription += (item.getTitle(context) + ", ")
        if (AHHelper.isInteger(notifications!![itemIndex].readableText)) {
            val num = notifications!![itemIndex].readableText.toInt()
            contentDescription += (num.toString() + " new item" + (if (num == 1) "" else "s") + ", ")
        }
        contentDescription += "tab, " + (itemIndex + 1) + " out of " + itemsCount
        views[itemIndex].contentDescription = contentDescription
    }

    /**
     * Update Items UI
     *
     * @param itemIndex   int: Selected item position
     * @param useCallback boolean: Use or not the callback
     */
    private fun updateItems(itemIndex: Int, useCallback: Boolean) {
        for (i in views.indices) {
            setTabAccessibilityLabel(i, itemIndex)
        }
        if (currentItem == itemIndex) {
            if (tabSelectedListener != null && useCallback) {
                tabSelectedListener!!.onTabSelected(itemIndex, true)
            }
            return
        }

        if (tabSelectedListener != null && useCallback) {
            val selectionAllowed = tabSelectedListener!!.onTabSelected(itemIndex, false)
            if (!selectionAllowed) return
        }

        val activeMarginTop =
            resources.getDimension(R.dimen.bottom_navigation_margin_top_active).toInt()
        val inactiveMarginTop =
            resources.getDimension(R.dimen.bottom_navigation_margin_top_inactive).toInt()

        for (i in views.indices) {
            val view = views[i]
            if (selectedBackgroundVisible) {
                view.isSelected = i == itemIndex
            }

            if (i == itemIndex) {
                val title = view.findViewById<AHTextView>(
                    R.id.bottom_navigation_item_title
                )
                val icon = view.findViewById<ImageView>(R.id.bottom_navigation_item_icon)
                val notification =
                    view.findViewById<AHTextView>(
                        R.id.bottom_navigation_notification
                    )

                icon.isSelected = true
                if (animateTabSelection) {
                    AHHelper.updateTopMargin(
                        icon,
                        inactiveMarginTop,
                        activeMarginTop
                    )
                    AHHelper.updateLeftMargin(
                        notification,
                        notificationInactiveMarginLeft,
                        notificationActiveMarginLeft
                    )
                    AHHelper.updateTextSize(
                        title,
                        getInactiveTextSize(i),
                        getActiveTextSize(i)
                    )
                }
                AHHelper.updateTextColor(
                    title,
                    titleInactiveColor[i],
                    titleActiveColor[i]
                )
                AHHelper.updateDrawableColor(
                    items[itemIndex].getDrawable(context), icon,
                    iconInactiveColor[i], iconActiveColor[i], forceTint
                )

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && colored) {
                    val finalRadius =
                        max(width.toDouble(), height.toDouble()).toInt()
                    val cx = view.x.toInt() + view.width / 2
                    val cy = view.height / 2

                    if (circleRevealAnim != null && circleRevealAnim!!.isRunning) {
                        circleRevealAnim!!.cancel()
                        setBackgroundColor(items[itemIndex].getColor(context))
                        backgroundColorView!!.setBackgroundColor(Color.TRANSPARENT)
                    }

                    circleRevealAnim = ViewAnimationUtils.createCircularReveal(
                        backgroundColorView,
                        cx,
                        cy,
                        0f,
                        finalRadius.toFloat()
                    ).apply {
                        startDelay = 5

                        addListener {
                            doOnStart {
                                backgroundColorView!!.setBackgroundColor(
                                    items[itemIndex].getColor(
                                        context
                                    )
                                )
                            }

                            doOnEnd {
                                setBackgroundColor(items[itemIndex].getColor(context))
                                backgroundColorView!!.setBackgroundColor(Color.TRANSPARENT)
                            }
                        }
                        start()
                    }
                } else if (colored) {
                    AHHelper.updateViewBackgroundColor(
                        this, currentColor,
                        items[itemIndex].getColor(context)
                    )
                } else {
                    if (defaultBackgroundResource != 0) {
                        setBackgroundResource(defaultBackgroundResource)
                    } else {
                        setBackgroundColor(defaultBackgroundColor)
                    }
                    backgroundColorView!!.setBackgroundColor(Color.TRANSPARENT)
                }
            } else if (i == currentItem) {
                val title = view.findViewById<AHTextView>(
                    R.id.bottom_navigation_item_title
                )
                val icon = view.findViewById<ImageView>(R.id.bottom_navigation_item_icon)
                val notification =
                    view.findViewById<AHTextView>(
                        R.id.bottom_navigation_notification
                    )

                icon.isSelected = false
                if (animateTabSelection) {
                    AHHelper.updateTopMargin(
                        icon,
                        activeMarginTop,
                        inactiveMarginTop
                    )
                    AHHelper.updateLeftMargin(
                        notification,
                        notificationActiveMarginLeft,
                        notificationInactiveMarginLeft
                    )
                    AHHelper.updateTextSize(
                        title,
                        getActiveTextSize(i),
                        getInactiveTextSize(i)
                    )
                }
                AHHelper.updateTextColor(
                    title,
                    titleActiveColor[i],
                    titleInactiveColor[i]
                )
                AHHelper.updateDrawableColor(
                    items[currentItem].getDrawable(context), icon,
                    iconActiveColor[i], iconInactiveColor[i], forceTint
                )
            }
        }

        currentItem = itemIndex
        if (currentItem > 0 && currentItem < items.size) {
            currentColor = items[currentItem].getColor(context)
        } else if (currentItem == CURRENT_ITEM_NONE) {
            if (defaultBackgroundResource != 0) {
                setBackgroundResource(defaultBackgroundResource)
            } else {
                setBackgroundColor(defaultBackgroundColor)
            }
            backgroundColorView!!.setBackgroundColor(Color.TRANSPARENT)
        }
    }

    /**
     * Update Small items UI
     *
     * @param itemIndex   int: Selected item position
     * @param useCallback boolean: Use or not the callback
     */
    private fun updateSmallItems(itemIndex: Int, useCallback: Boolean) {
        if (currentItem == itemIndex) {
            if (tabSelectedListener != null && useCallback) {
                tabSelectedListener!!.onTabSelected(itemIndex, true)
            }
            return
        }

        if (tabSelectedListener != null && useCallback) {
            val selectionAllowed = tabSelectedListener!!.onTabSelected(itemIndex, false)
            if (!selectionAllowed) return
        }

        val activeMarginTop =
            resources.getDimension(R.dimen.bottom_navigation_small_margin_top_active).toInt()
        val inactiveMargin =
            resources.getDimension(R.dimen.bottom_navigation_small_margin_top).toInt()

        for (i in views.indices) {
            val view = views[i]
            if (selectedBackgroundVisible) {
                view.isSelected = i == itemIndex
            }

            if (i == itemIndex) {
                val container =
                    view.findViewById<FrameLayout>(R.id.bottom_navigation_small_container)
                val title = view.findViewById<AHTextView>(
                    R.id.bottom_navigation_small_item_title
                )
                val icon = view.findViewById<ImageView>(R.id.bottom_navigation_small_item_icon)
                val notification =
                    view.findViewById<AHTextView>(
                        R.id.bottom_navigation_notification
                    )

                icon.isSelected = true

                if (titleState != TitleState.ALWAYS_HIDE) {
                    AHHelper.updateTopMargin(
                        icon,
                        inactiveMargin,
                        activeMarginTop
                    )
                    AHHelper.updateLeftMargin(
                        notification,
                        notificationInactiveMarginLeft,
                        notificationActiveMarginLeft
                    )
                    AHHelper.updateTopMargin(
                        notification,
                        notificationInactiveMarginTop,
                        notificationActiveMarginTop
                    )
                    AHHelper.updateTextColor(
                        title,
                        iconInactiveColor[i],
                        iconActiveColor[i]
                    )
                    AHHelper.updateWidth(
                        container,
                        notSelectedItemWidth,
                        selectedItemWidth
                    )
                }

                AHHelper.updateAlpha(
                    title,
                    0f,
                    1f
                )
                AHHelper.updateDrawableColor(
                    items[itemIndex].getDrawable(context), icon,
                    iconInactiveColor[i], iconActiveColor[i], forceTint
                )

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && colored) {
                    val finalRadius =
                        max(width.toDouble(), height.toDouble()).toInt()
                    val cx = views[itemIndex].x.toInt() + views[itemIndex].width / 2
                    val cy = views[itemIndex].height / 2

                    if (circleRevealAnim != null && circleRevealAnim!!.isRunning) {
                        circleRevealAnim!!.cancel()
                        setBackgroundColor(items[itemIndex].getColor(context))
                        backgroundColorView!!.setBackgroundColor(Color.TRANSPARENT)
                    }

                    circleRevealAnim = ViewAnimationUtils.createCircularReveal(
                        backgroundColorView,
                        cx,
                        cy,
                        0f,
                        finalRadius.toFloat()
                    ).apply {
                        startDelay = 5
                        addListener {
                            doOnStart {
                                backgroundColorView!!.setBackgroundColor(
                                    items[itemIndex].getColor(
                                        context
                                    )
                                )
                            }

                            doOnEnd {
                                setBackgroundColor(items[itemIndex].getColor(context))
                                backgroundColorView!!.setBackgroundColor(Color.TRANSPARENT)

                            }
                        }
                        start()
                    }
                } else if (colored) {
                    AHHelper.updateViewBackgroundColor(
                        this, currentColor,
                        items[itemIndex].getColor(context)
                    )
                } else {
                    if (defaultBackgroundResource != 0) {
                        setBackgroundResource(defaultBackgroundResource)
                    } else {
                        setBackgroundColor(defaultBackgroundColor)
                    }
                    backgroundColorView!!.setBackgroundColor(Color.TRANSPARENT)
                }
            } else if (i == currentItem) {
                val container = view.findViewById<View>(R.id.bottom_navigation_small_container)
                val title = view.findViewById<AHTextView>(R.id.bottom_navigation_small_item_title)
                val icon = view.findViewById<ImageView>(R.id.bottom_navigation_small_item_icon)
                val notification =
                    view.findViewById<AHTextView>(R.id.bottom_navigation_notification)

                icon.isSelected = false

                if (titleState != TitleState.ALWAYS_HIDE) {
                    AHHelper.updateTopMargin(
                        icon,
                        activeMarginTop,
                        inactiveMargin
                    )
                    AHHelper.updateLeftMargin(
                        notification,
                        notificationActiveMarginLeft,
                        notificationInactiveMarginLeft
                    )
                    AHHelper.updateTopMargin(
                        notification,
                        notificationActiveMarginTop,
                        notificationInactiveMarginTop
                    )
                    AHHelper.updateTextColor(
                        title,
                        iconActiveColor[i],
                        iconInactiveColor[i]
                    )
                    AHHelper.updateWidth(
                        container,
                        selectedItemWidth,
                        notSelectedItemWidth
                    )
                }

                AHHelper.updateAlpha(title, 1f, 0f)
                AHHelper.updateDrawableColor(
                    items[currentItem].getDrawable(context), icon,
                    iconActiveColor[i], iconInactiveColor[i], forceTint
                )
            }
        }

        currentItem = itemIndex
        if (currentItem > 0 && currentItem < items.size) {
            currentColor = items[currentItem].getColor(context)
        } else if (currentItem == CURRENT_ITEM_NONE) {
            if (defaultBackgroundResource != 0) {
                setBackgroundResource(defaultBackgroundResource)
            } else {
                setBackgroundColor(defaultBackgroundColor)
            }
            backgroundColorView!!.setBackgroundColor(Color.TRANSPARENT)
        }
    }

    /**
     * Update notifications
     */
    private fun updateNotifications(updateStyle: Boolean, itemPosition: Int) {
        for (i in views.indices) {
            if (i >= notifications!!.size) {
                break
            }

            if (itemPosition != UPDATE_ALL_NOTIFICATIONS && itemPosition != i) {
                continue
            }

            val notificationItem = notifications!![i]
            val currentTextColor =
                AHNotificationHelper.getTextColor(notificationItem, notificationTextColor)
            val currentBackgroundColor = AHNotificationHelper.getBackgroundColor(
                notificationItem,
                notificationBackgroundColor
            )

            val notification =
                views[i].findViewById<AHTextView>(
                    R.id.bottom_navigation_notification
                )

            if (updateStyle) {
                notification.elevation = (if (notificationItem.isDot) 0 else defaultNotificationElevation).toFloat()
                notification.setTextColor(currentTextColor)
                if (notificationTypeface != null) {
                    notification.typeface = notificationTypeface
                } else {
                    notification.setTypeface(null, Typeface.BOLD)
                }

                if (notificationBackgroundDrawable != null) {
                    notification.background = notificationBackgroundDrawable!!.constantState!!.newDrawable()
                } else if (currentBackgroundColor != 0) {
                    val defaultDrawable = ContextCompat.getDrawable(context, R.drawable.notification_background)
                    notification.background =
                        AHHelper.getTintDrawable(
                            defaultDrawable,
                            currentBackgroundColor,
                            forceTint
                        )
                }
            }

            if (notificationItem.isEmpty) {
                hideNotification(notificationItem, notification)
            } else {
                showNotification(notificationItem, notification)
            }
        }
    }

    private fun showNotification(notification: AHNotification, notificationView: AHTextView) {
        notificationView.text = notification.readableText
        updateNotificationSize(notification, notificationView)
        if (notificationView.alpha != 1f) {
            if (shouldAnimateNotification(notification)) {
                animateNotificationShow(notificationView)
                notification.setAnimate(false)
            } else {
                notificationView.scaleX = 1f
                notificationView.scaleY = 1f
                notificationView.alpha = 1f
            }
        }
    }

    private fun animateNotificationShow(notification: AHTextView) {
        notification.scaleX = 0f
        notification.scaleY = 0f
        notification.alpha = 0f
        notification.animate()
            .scaleX(1f)
            .scaleY(1f)
            .alpha(1f)
            .setInterpolator(OvershootInterpolator())
            .setDuration(notificationAnimationDuration)
            .start()
    }

    private fun hideNotification(notification: AHNotification, notificationView: AHTextView) {
        if (notificationView.alpha != 0f) {
            if (shouldAnimateNotification(notification)) {
                animateHideNotification(notificationView)
                notification.setAnimate(false)
            } else {
                notificationView.scaleX = 0f
                notificationView.scaleY = 0f
                notificationView.alpha = 0f
            }
        }
    }

    private fun animateHideNotification(notification: AHTextView) {
        notification.animate()
            .scaleX(0f)
            .scaleY(0f)
            .alpha(0f)
            .setInterpolator(AccelerateInterpolator())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    if (notification.alpha == 0f) notification.text = ""
                }
            })
            .setDuration(notificationAnimationDuration)
            .start()
    }

    private fun updateNotificationSize(notificationItem: AHNotification, notification: AHTextView) {
        val lp = notification.layoutParams
        lp.width =
            if (notificationItem.size >= 0 && !notificationItem.hasText()) notificationItem.size else ViewGroup.LayoutParams.WRAP_CONTENT
        lp.height =
            if (notificationItem.size >= 0) notificationItem.size else getResources().getDimensionPixelSize(
                R.dimen.bottom_navigation_notification_height
            )
        notification.requestLayout()
    }

    private fun shouldAnimateNotification(notification: AHNotification): Boolean {
        return notification.shouldAnimate() && animateTabSelection
    }


    ////////////
    // PUBLIC //
    ////////////
    /**
     * Add an item
     */
    fun addItem(item: AHBottomNavigationItem) {
        if (items.size > MAX_ITEMS) {
            Log.w(TAG, "The items list should not have more than 5 items")
        }
        items.add(item)
        createItems()
    }

    /**
     * Add all items
     */
    fun addItems(items: List<AHBottomNavigationItem>) {
        if (items.size > MAX_ITEMS || (this.items.size + items.size) > MAX_ITEMS) {
            Log.w(TAG, "The items list should not have more than 5 items")
        }
        this.items.addAll(items)
        createItems()
    }

    /**
     * Remove an item at the given index
     */
    fun removeItemAtIndex(index: Int) {
        if (index < items.size) {
            items.removeAt(index)
            createItems()
        }
    }

    /**
     * Remove all items
     */
    fun removeAllItems() {
        items.clear()
        createItems()
    }

    /**
     * Refresh the AHBottomView
     */
    fun refresh() {
        createItems()
    }

    val itemsCount: Int
        /**
         * Return the number of items
         *
         * @return int
         */
        get() = items.size

    /**
     * Return if the Bottom Navigation is colored
     */
    fun isColored(): Boolean {
        return colored
    }

    /**
     * Set if the Bottom Navigation is colored
     */
    fun setColored(colored: Boolean) {
        this.colored = colored
        this.iconActiveColor = if (colored) coloredTitleColorActive else titleActiveColor
        this.iconInactiveColor = if (colored) coloredTitleColorInactive else titleInactiveColor
        createItems()
    }

    /**
     * Return the bottom navigation background color
     *
     * @return The bottom navigation background color
     */
    fun getDefaultBackgroundColor(): Int {
        return defaultBackgroundColor
    }

    /**
     * Set the bottom navigation background color
     *
     * @param defaultBackgroundColor The bottom navigation background color
     */
    fun setDefaultBackgroundColor(@ColorInt defaultBackgroundColor: Int) {
        this.defaultBackgroundColor = defaultBackgroundColor
        createItems()
    }

    /**
     * Set the bottom navigation background resource
     *
     * @param defaultBackgroundResource The bottom navigation background resource
     */
    fun setDefaultBackgroundResource(@DrawableRes defaultBackgroundResource: Int) {
        this.defaultBackgroundResource = defaultBackgroundResource
        createItems()
    }

    fun setAnimateTabSelection(animateTabSelection: Boolean) {
        this.animateTabSelection = animateTabSelection
    }

    /**
     * Get the accent color (used when the view contains 3 items)
     *
     * @return The default accent color
     */
    fun getIconActiveColor(index: Int): Int? {
        return iconActiveColor[index]
    }

    /**
     * Set the accent color (used when the view contains 3 items)
     *
     * @param activeColor The new accent color
     */
    fun setIconActiveColor(index: Int, activeColor: Int?) {
        if (AHHelper.equals(
                iconActiveColor[index],
                activeColor
            )
        ) return
        iconActiveColor[index] = activeColor
        createItems()
    }

    fun setIconWidth(index: Int, width: Int?) {
        if (AHHelper.equals(iconWidth[index], width)) return
        iconWidth[index] = width
        createItems()
    }

    fun setIconHeight(index: Int, height: Int?) {
        if (AHHelper.equals(iconHeight[index], height)) return
        iconHeight[index] = height
        createItems()
    }

    /**
     * Set the accent color (used when the view contains 3 items)
     *
     * @param activeColor The new accent color
     */
    fun setTitleActiveColor(index: Int, activeColor: Int?) {
        if (AHHelper.equals(titleActiveColor[index], activeColor)) return
        titleActiveColor[index] = activeColor
        createItems()
    }

    /**
     * Get the inactive color (used when the view contains 3 items)
     *
     * @return The inactive color
     */
    fun getIconInactiveColor(index: Int): Int? {
        return iconInactiveColor[index]
    }

    /**
     * Get the inactive color (used when the view contains 3 items)
     *
     * @return The inactive color
     */
    fun getTitleInactiveColor(index: Int): Int? {
        return titleInactiveColor[index]
    }

    /**
     * Set the inactive color (used when the view contains 3 items)
     *
     * @param inactiveColor The inactive color
     */
    fun setIconInactiveColor(index: Int, inactiveColor: Int?) {
        if (AHHelper.equals(
                iconInactiveColor[index],
                inactiveColor
            )
        ) return
        iconInactiveColor[index] = inactiveColor
        createItems()
    }

    /**
     * Set the inactive color (used when the view contains 3 items)
     *
     * @param inactiveColor The inactive color
     */
    fun setTitleInactiveColor(index: Int, inactiveColor: Int?) {
        if (AHHelper.equals(
                titleInactiveColor[index],
                inactiveColor
            )
        ) return
        titleInactiveColor[index] = inactiveColor
        createItems()
    }

    /**
     * Set the colors used when the bottom bar uses the colored mode
     *
     * @param colorActive   The active color
     * @param colorInactive The inactive color
     */
    fun setColoredModeColors(index: Int, @ColorInt colorActive: Int, @ColorInt colorInactive: Int) {
        coloredTitleColorActive[index] = colorActive
        coloredTitleColorInactive[index] = colorInactive
        createItems()
    }

    /**
     * Set selected background visibility
     */
    fun setSelectedBackgroundVisible(visible: Boolean) {
        this.selectedBackgroundVisible = visible
        createItems()
    }

    /**
     * Set notification typeface
     *
     * @param typeface Typeface
     */
    fun setTitleTypeface(index: Int, typeface: Typeface?) {
        if (titleTypeface[index] === typeface) return
        titleTypeface[index] = typeface
        createItems()
    }

    /**
     * Set title active text size in pixels
     */
    fun setTitleActiveTextSize(index: Int, activeSize: Float) {
        if (AHHelper.equals(
                titleActiveTextSize[index],
                activeSize
            )
        ) return
        titleActiveTextSize[index] = activeSize
        createItems()
    }

    /**
     * Set title inactive text size in pixels
     */
    fun setTitleInactiveTextSize(index: Int, inactiveSize: Float) {
        if (AHHelper.equals(
                titleInactiveTextSize[index],
                inactiveSize
            )
        ) return
        titleInactiveTextSize[index] = inactiveSize
        createItems()
    }

    /**
     * Set title text size in SP
     *
     * @param activeSize in sp
     */
    fun setTitleActiveTextSizeInSp(index: Int, activeSize: Float) {
        if (AHHelper.equals(
                titleActiveTextSize[index],
                activeSize
            )
        ) return
        titleActiveTextSize[index] = (TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            activeSize,
            resources.displayMetrics
        ))
        createItems()
    }

    /**
     * Set title text size in SP
     *
     * @param inactiveSize in sp
     */
    fun setTitleInactiveTextSizeInSp(index: Int, inactiveSize: Float) {
        if (AHHelper.equals(
                titleInactiveTextSize[index],
                inactiveSize
            )
        ) return
        titleInactiveTextSize[index] = (TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            inactiveSize,
            resources.displayMetrics
        ))
        createItems()
    }

    fun setTag(index: Int, tag: String?) {
        if (index >= 0 && index < views.size) views[index].tag = tag
    }

    /**
     * Get item at the given index
     *
     * @param position int: item position
     * @return The item at the given position
     */
    fun getItem(position: Int): AHBottomNavigationItem? {
        if (position < 0 || position > items.size - 1) {
            Log.w(TAG, "The position is out of bounds of the items (" + items.size + " elements)")
            return null
        }
        return items[position]
    }

    /**
     * Get the current item
     *
     * @return The current item position
     */
    fun getCurrentItem(): Int {
        return currentItem
    }

    /**
     * Set the current item
     *
     * @param position int: position
     */
    open fun setCurrentItem(position: Int) {
        setCurrentItem(position, true)
    }

    /**
     * Set the current item
     *
     * @param position    int: item position
     * @param useCallback boolean: use or not the callback
     */
    open fun setCurrentItem(position: Int, useCallback: Boolean) {
        if (position >= items.size) {
            Log.w(TAG, "The position is out of bounds of the items (" + items.size + " elements)")
            return
        }

        if (isClassic) {
            updateItems(position, useCallback)
        } else {
            updateSmallItems(position, useCallback)
        }
    }

    /**
     * Return if the behavior translation is enabled
     *
     * @return a boolean value
     */
    fun isBehaviorTranslationEnabled(): Boolean {
        return behaviorTranslationEnabled
    }

    /**
     * Set the behavior translation value
     *
     * @param behaviorTranslationEnabled boolean for the state
     */
    fun setBehaviorTranslationEnabled(behaviorTranslationEnabled: Boolean) {
        this.behaviorTranslationEnabled = behaviorTranslationEnabled
        if (parent is CoordinatorLayout) {
            val params = layoutParams
            if (bottomNavigationBehavior == null) {
                bottomNavigationBehavior =
                    AHBottomNavigationBehavior(
                        behaviorTranslationEnabled,
                        navigationBarHeight
                    )
            } else {
                bottomNavigationBehavior!!.setBehaviorTranslationEnabled(
                    behaviorTranslationEnabled,
                    navigationBarHeight
                )
            }
            if (navigationPositionListener != null) {
                bottomNavigationBehavior!!.setOnNavigationPositionListener { y ->
                    navigationPositionListener?.onPositionChange(y)
                }
            }
            (params as CoordinatorLayout.LayoutParams).behavior = bottomNavigationBehavior
            if (needHideBottomNavigation) {
                needHideBottomNavigation = false
                bottomNavigationBehavior!!.hideView(
                    this,
                    bottomNavigationHeight,
                    hideBottomNavigationWithAnimation
                )
            }
        }
    }

    /**
     * Manage the floating action button behavior with AHBottomNavigation
     * @param fab Floating Action Button
     */
    fun manageFloatingActionButtonBehavior(fab: FloatingActionButton) {
        if (fab.parent is CoordinatorLayout) {
            val fabBehavior = AHBottomNavigationFABBehavior(navigationBarHeight)
            (fab.layoutParams as CoordinatorLayout.LayoutParams).behavior = fabBehavior
        }
    }

    /**
     * Hide Bottom Navigation with animation
     */
    fun hideBottomNavigation() {
        hideBottomNavigation(true)
    }

    /**
     * Hide Bottom Navigation with or without animation
     *
     * @param withAnimation Boolean
     */
    open fun hideBottomNavigation(withAnimation: Boolean) {
        if (bottomNavigationBehavior != null) {
            bottomNavigationBehavior!!.hideView(this, bottomNavigationHeight, withAnimation)
        } else if (parent is CoordinatorLayout) {
            needHideBottomNavigation = true
            hideBottomNavigationWithAnimation = withAnimation
        } else {
            // Hide bottom navigation
            ViewCompat.animate(this)
                .translationY(bottomNavigationHeight.toFloat())
                .setInterpolator(LinearOutSlowInInterpolator())
                .setDuration((if (withAnimation) 300 else 0).toLong())
                .start()
        }
    }

    /**
     * Restore Bottom Navigation with animation
     */
    fun restoreBottomNavigation() {
        restoreBottomNavigation(true)
    }

    /**
     * Restore Bottom Navigation with or without animation
     *
     * @param withAnimation Boolean
     */
    open fun restoreBottomNavigation(withAnimation: Boolean) {
        if (bottomNavigationBehavior != null) {
            bottomNavigationBehavior!!.resetOffset(this, withAnimation)
        } else {
            // Show bottom navigation
            ViewCompat.animate(this)
                .translationY(0f)
                .setInterpolator(LinearOutSlowInInterpolator())
                .setDuration((if (withAnimation) 300 else 0).toLong())
                .start()
        }
    }

    /**
     * Return if the tint should be forced (with setColorFilter)
     *
     * @return Boolean
     */
    fun isForceTint(): Boolean {
        return forceTint
    }

    /**
     * Set the force tint value
     * If forceTint = true, the tint is made with drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
     *
     * @param forceTint Boolean
     */
    fun setForceTint(forceTint: Boolean) {
        this.forceTint = forceTint
        createItems()
    }

    /**
     * Return the title state for display
     *
     * @return TitleState
     */
    fun getTitleState(): TitleState {
        return titleState
    }

    /**
     * Sets the title state for each tab
     * SHOW_WHEN_ACTIVE: when a tab is focused
     * ALWAYS_SHOW: show regardless of which tab is in focus
     * ALWAYS_HIDE: never show tab titles
     * Note: Always showing the title is against Material Design guidelines
     *
     * @param titleState TitleState
     */
    open fun setTitleState(titleState: TitleState) {
        this.titleState = titleState
        createItems()
    }

    /**
     * Set AHOnTabSelectedListener
     */
    fun setOnTabSelectedListener(tabSelectedListener: OnTabSelectedListener?) {
        this.tabSelectedListener = tabSelectedListener
    }

    /**
     * Remove AHOnTabSelectedListener
     */
    fun removeOnTabSelectedListener() {
        this.tabSelectedListener = null
    }

    /**
     * Set OnNavigationPositionListener
     */
    fun setOnNavigationPositionListener(navigationPositionListener: OnNavigationPositionListener?) {
        this.navigationPositionListener = navigationPositionListener
        if (bottomNavigationBehavior != null) {
            bottomNavigationBehavior!!.setOnNavigationPositionListener { y: Int ->
                this.navigationPositionListener?.onPositionChange(y)
            }
        }
    }

    /**
     * Remove OnNavigationPositionListener()
     */
    fun removeOnNavigationPositionListener() {
        this.navigationPositionListener = null
        if (bottomNavigationBehavior != null) {
            bottomNavigationBehavior!!.removeOnNavigationPositionListener()
        }
    }

    /**
     * Set the notification number
     *
     * @param nbNotification int
     * @param itemPosition   int
     */
    @Deprecated("")
    fun setNotification(nbNotification: Int, itemPosition: Int) {
        if (itemPosition < 0 || itemPosition > items.size - 1) {
            throw IndexOutOfBoundsException(
                String.format(
                    Locale.US,
                    EXCEPTION_INDEX_OUT_OF_BOUNDS,
                    itemPosition,
                    items.size
                )
            )
        }
        val title = if (nbNotification == 0) "" else nbNotification.toString()
        notifications!![itemPosition] = AHNotification.justText(title)
        updateNotifications(false, itemPosition)
    }

    /**
     * Set notification text
     *
     * @param title        String
     * @param itemPosition int
     */
    fun setNotification(title: String?, itemPosition: Int) {
        if (itemPosition < 0 || itemPosition > items.size - 1) {
            throw IndexOutOfBoundsException(
                String.format(
                    Locale.US,
                    EXCEPTION_INDEX_OUT_OF_BOUNDS,
                    itemPosition,
                    items.size
                )
            )
        }
        notifications!![itemPosition] = AHNotification.justText(title)
        updateNotifications(false, itemPosition)
    }

    /**
     * Set fully customized Notification
     *
     * @param notification AHNotification
     * @param itemPosition int
     */
    fun setNotification(notification: AHNotification?, itemPosition: Int) {
        if (itemPosition < 0 || itemPosition > items.size - 1) {
            throw IndexOutOfBoundsException(
                String.format(
                    Locale.US,
                    EXCEPTION_INDEX_OUT_OF_BOUNDS,
                    itemPosition,
                    items.size
                )
            )
        }
        notifications!![itemPosition] = (notification ?: AHNotification())
        updateNotifications(true, itemPosition)
    }

    fun setNotificationSize(index: Int, @Px size: Int) {
        if (AHHelper.equals(
                notifications!![index].size,
                size
            )
        ) return
        notifications!![index].size = size
        updateNotifications(true, index)
    }

    /**
     * Set notification text color
     *
     * @param textColor int
     */
    fun setNotificationTextColor(@ColorInt textColor: Int) {
        if (notificationTextColor == textColor) return
        notificationTextColor = textColor
        updateNotifications(true, UPDATE_ALL_NOTIFICATIONS)
    }

    /**
     * Set notification text color
     *
     * @param textColor int
     */
    fun setNotificationTextColorResource(@ColorRes textColor: Int) {
        this.notificationTextColor = ContextCompat.getColor(context!!, textColor)
        updateNotifications(true, UPDATE_ALL_NOTIFICATIONS)
    }

    /**
     * Set notification background resource
     *
     * @param drawable Drawable
     */
    fun setNotificationBackground(drawable: Drawable?) {
        this.notificationBackgroundDrawable = drawable
        updateNotifications(true, UPDATE_ALL_NOTIFICATIONS)
    }

    /**
     * Set notification background color
     *
     * @param color int
     */
    fun setNotificationBackgroundColor(@ColorInt color: Int) {
        if (notificationBackgroundColor == color) return
        notificationBackgroundColor = color
        updateNotifications(true, UPDATE_ALL_NOTIFICATIONS)
    }

    /**
     * Set notification background color
     *
     * @param color int
     */
    fun setNotificationBackgroundColorResource(@ColorRes color: Int) {
        this.notificationBackgroundColor = ContextCompat.getColor(context!!, color)
        updateNotifications(true, UPDATE_ALL_NOTIFICATIONS)
    }

    /**
     * Set notification typeface
     *
     * @param typeface Typeface
     */
    fun setNotificationTypeface(typeface: Typeface?) {
        this.notificationTypeface = typeface
        updateNotifications(true, UPDATE_ALL_NOTIFICATIONS)
    }

    fun setNotificationAnimationDuration(notificationAnimationDuration: Long) {
        this.notificationAnimationDuration = notificationAnimationDuration
        updateNotifications(true, UPDATE_ALL_NOTIFICATIONS)
    }

    /**
     * Set the notification margin left
     */
    fun setNotificationMarginLeft(activeMargin: Int, inactiveMargin: Int) {
        this.notificationActiveMarginLeft = activeMargin
        this.notificationInactiveMarginLeft = inactiveMargin
        createItems()
    }

    /**
     * Activate or not the elevation
     *
     * @param useElevation boolean
     */
    fun setUseElevation(useElevation: Boolean) {
        ViewCompat.setElevation(
            this,
            if (useElevation) resources.getDimension(R.dimen.bottom_navigation_elevation) else 0f
        )
        clipToPadding = false
    }

    /**
     * Activate or not the elevation, and set the value
     *
     * @param useElevation boolean
     * @param elevation    float
     */
    fun setUseElevation(useElevation: Boolean, elevation: Float) {
        ViewCompat.setElevation(this, if (useElevation) elevation else 0f)
        clipToPadding = false
    }

    val isHidden: Boolean
        /**
         * Return if the Bottom Navigation is hidden or not
         */
        get() {
            if (bottomNavigationBehavior != null) {
                return bottomNavigationBehavior!!.isHidden
            }
            return false
        }

    /**
     * Get the view at the given position
     * @param position int
     * @return The view at the position, or null
     */
    fun getViewAtPosition(position: Int): View? {
        if (linearLayoutContainer != null && position >= 0 && position < linearLayoutContainer!!.childCount) {
            return linearLayoutContainer!!.getChildAt(position)
        }
        return null
    }

    /**
     * Enable the tab item at the given position
     * @param position int
     */
    fun enableItemAtPosition(position: Int) {
        if (position < 0 || position > items.size - 1) {
            Log.w(TAG, "The position is out of bounds of the items (" + items.size + " elements)")
            return
        }
        itemsEnabledStates[position] = true
        createItems()
    }

    /**
     * Disable the tab item at the given position
     * @param position int
     */
    fun disableItemAtPosition(position: Int) {
        if (position < 0 || position > items.size - 1) {
            Log.w(TAG, "The position is out of bounds of the items (" + items.size + " elements)")
            return
        }
        itemsEnabledStates[position] = false
        createItems()
    }

    /**
     * Set the item disable color
     * @param iconDisableColor int
     */
    fun setIconDisableColor(index: Int, @ColorInt iconDisableColor: Int) {
        this.iconDisableColor[index] = iconDisableColor
    }

    ////////////////
    // INTERFACES //
    ////////////////
    /**
     *
     */
    interface OnTabSelectedListener {
        /**
         * Called when a tab has been selected (clicked)
         *
         * @param position    int: Position of the selected tab
         * @param wasSelected boolean: true if the tab was already selected
         * @return boolean: true for updating the tab UI, false otherwise
         */
        fun onTabSelected(position: Int, wasSelected: Boolean): Boolean
    }

    interface OnNavigationPositionListener {
        /**
         * Called when the bottom navigation position is changed
         *
         * @param y int: y translation of bottom navigation
         */
        fun onPositionChange(y: Int)
    }

    fun setPreferLargeIcons(preferLargeIcons: Boolean) {
        this.preferLargeIcons = preferLargeIcons
    }

    private fun dpToPx(dp: Int): Int {
        val metrics = getResources().displayMetrics
        return dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
    }

    companion object {
        // Constant
        const val CURRENT_ITEM_NONE: Int = -1
        const val UPDATE_ALL_NOTIFICATIONS: Int = -1

        // Static
        private const val TAG = "AHBottomNavigation"
        private const val EXCEPTION_INDEX_OUT_OF_BOUNDS =
            "The position (%d) is out of bounds of the items (%d elements)"
        private const val MIN_ITEMS = 3
        private const val MAX_ITEMS = 5
    }
}
