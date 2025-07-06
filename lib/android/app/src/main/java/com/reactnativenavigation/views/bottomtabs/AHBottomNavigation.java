package com.reactnativenavigation.views.bottomtabs;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.aurelhubert.ahbottomnavigation.AHHelper.fill;
import static com.aurelhubert.ahbottomnavigation.AHHelper.map;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Px;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation.OnNavigationPositionListener;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation.OnTabSelectedListener;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationBehavior;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationFABBehavior;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.AHHelper;
import com.aurelhubert.ahbottomnavigation.AHTextView;
import com.aurelhubert.ahbottomnavigation.R;
import com.aurelhubert.ahbottomnavigation.notification.AHNotification;
import com.aurelhubert.ahbottomnavigation.notification.AHNotificationHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.reactnativenavigation.utils.StubAnimationListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * AHBottomNavigationLayout
 * Material Design guidelines : https://www.google.com/design/spec/components/bottom-navigation.html
 */
public class AHBottomNavigation extends FrameLayout {

	// Constant
	public static final int CURRENT_ITEM_NONE = -1;
	public static final int UPDATE_ALL_NOTIFICATIONS = -1;

	// Title state
	public enum TitleState {
		SHOW_WHEN_ACTIVE,
		SHOW_WHEN_ACTIVE_FORCE,
		ALWAYS_SHOW,
		ALWAYS_HIDE
	}

	// Static
	private static String TAG = "AHBottomNavigation";
    private static final String EXCEPTION_INDEX_OUT_OF_BOUNDS = "The position (%d) is out of bounds of the items (%d elements)";
	private static final int MIN_ITEMS = 3;
	private static final int MAX_ITEMS = 5;

	// Listener
	private com.aurelhubert.ahbottomnavigation.AHBottomNavigation.OnTabSelectedListener tabSelectedListener;
	private com.aurelhubert.ahbottomnavigation.AHBottomNavigation.OnNavigationPositionListener navigationPositionListener;

	// Variables
	private Context context;
	private Resources resources;
	private ArrayList<AHBottomNavigationItem> items = new ArrayList<>();
	private ArrayList<View> views = new ArrayList<>();
	private AHBottomNavigationBehavior<AHBottomNavigation> bottomNavigationBehavior;
	private ViewGroup linearLayoutContainer;
	private View backgroundColorView;
	private Animator circleRevealAnim;
	private boolean colored = false;
	private boolean selectedBackgroundVisible = false;
	private boolean translucentNavigationEnabled;
	private List<AHNotification> notifications = AHNotification.generateEmptyList(MAX_ITEMS);
	private Boolean[] itemsEnabledStates = {true, true, true, true, true};
	private boolean isBehaviorTranslationSet = false;
	private int currentItem = 0;
	private int currentColor = 0;
	private boolean behaviorTranslationEnabled = true;
	private boolean needHideBottomNavigation = false;
	private boolean hideBottomNavigationWithAnimation = false;
	private boolean soundEffectsEnabled = true;

    // Variables (Styles)
    private ArrayList<Typeface> titleTypeface = new ArrayList<>();
	private int defaultBackgroundColor = Color.WHITE;
	private int defaultBackgroundResource = 0;
	private int activePaddingTop;
	private ArrayList<Integer> iconActiveColor = new ArrayList<>(MAX_ITEMS);
    private ArrayList<Integer> iconInactiveColor = new ArrayList<>(MAX_ITEMS);

    private ArrayList<Integer> iconHeight = new ArrayList<>(MAX_ITEMS);
    private ArrayList<Integer> iconWidth = new ArrayList<>(MAX_ITEMS);


    private ArrayList<Integer> titleActiveColor = new ArrayList<>(MAX_ITEMS);
    private ArrayList<Integer> titleInactiveColor = new ArrayList<>(MAX_ITEMS);

    private ArrayList<Integer> iconDisableColor = new ArrayList<>(MAX_ITEMS);
    private ArrayList<Integer> titleDisableColor = new ArrayList<>(MAX_ITEMS);

	private ArrayList<Integer> coloredTitleColorActive = new ArrayList<>(MAX_ITEMS);
    private ArrayList<Integer> coloredTitleColorInactive = new ArrayList<>(MAX_ITEMS);
    private ArrayList<Float> titleActiveTextSize = new ArrayList<>(MAX_ITEMS);
    private ArrayList<Float> titleInactiveTextSize = new ArrayList<>(MAX_ITEMS);
	private int bottomNavigationHeight, navigationBarHeight = 0;
	private float selectedItemWidth, notSelectedItemWidth;
	private boolean forceTint = false;
    private boolean preferLargeIcons = false;
	private TitleState titleState = TitleState.SHOW_WHEN_ACTIVE;
    private int activeMarginTop;
    private float widthDifference;
    private int defaultIconHeight;
    private int defaultIconWidth;

	// Notifications
	private @ColorInt int notificationTextColor;
	private @ColorInt int notificationBackgroundColor;
	private Drawable notificationBackgroundDrawable;
	private Typeface notificationTypeface;
	private int notificationActiveMarginLeft, notificationInactiveMarginLeft;
	private int notificationActiveMarginTop, notificationInactiveMarginTop;
	private long notificationAnimationDuration;
	private int defaultNotificationElevation;
	private boolean animateTabSelection = true;

	/**
	 * Constructors
	 */
	public AHBottomNavigation(Context context) {
		super(context);
		init(context, null);
	}

	public AHBottomNavigation(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public AHBottomNavigation(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	@Override
	public void setSoundEffectsEnabled(final boolean soundEffectsEnabled) {
		super.setSoundEffectsEnabled(soundEffectsEnabled);
		this.soundEffectsEnabled = soundEffectsEnabled;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		createItems();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (!isBehaviorTranslationSet) {
			//The translation behavior has to be set up after the super.onMeasure has been called.
			setBehaviorTranslationEnabled(behaviorTranslationEnabled);
			isBehaviorTranslationSet = true;
		}
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		Bundle bundle = new Bundle();
		bundle.putParcelable("superState", super.onSaveInstanceState());
		bundle.putInt("current_item", currentItem);
        bundle.putParcelableArrayList("notifications", new ArrayList<> (notifications));
		return bundle;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		if (state instanceof Bundle) {
			Bundle bundle = (Bundle) state;
			currentItem = bundle.getInt("current_item");
            notifications = bundle.getParcelableArrayList("notifications");
			state = bundle.getParcelable("superState");
		}
		super.onRestoreInstanceState(state);
	}

	/////////////
	// PRIVATE //
	/////////////

	/**
	 * Init
	 */
	private void init(Context context, AttributeSet attrs) {
        this.context = context;
		resources = this.context.getResources();
        defaultNotificationElevation = resources.getDimensionPixelSize(R.dimen.bottom_navigation_notification_elevation);
        activePaddingTop = (int) resources.getDimension(R.dimen.bottom_navigation_margin_top_active);
        activeMarginTop = (int) resources.getDimension(R.dimen.bottom_navigation_small_margin_top_active);
        widthDifference = resources.getDimension(R.dimen.bottom_navigation_small_selected_width_difference);
        defaultIconHeight = resources.getDimensionPixelSize(R.dimen.bottom_navigation_icon);
        defaultIconWidth = resources.getDimensionPixelSize(R.dimen.bottom_navigation_icon);

        // Icon colors
        fill(iconActiveColor, MAX_ITEMS, null);
        fill(iconInactiveColor, MAX_ITEMS, null);
        fill(iconDisableColor, MAX_ITEMS, null);
        // Title colors
        fill(titleActiveColor, MAX_ITEMS, null);
        fill(titleInactiveColor, MAX_ITEMS, null);
        fill(titleDisableColor, MAX_ITEMS, null);

        fill(iconWidth, MAX_ITEMS, null);
        fill(iconHeight, MAX_ITEMS, null);

        fill(titleTypeface, MAX_ITEMS, null);
        fill(titleActiveTextSize, MAX_ITEMS, null);
        fill(titleInactiveTextSize, MAX_ITEMS, null);

		// Colors for colored bottom navigation
        fill(coloredTitleColorActive, MAX_ITEMS, ContextCompat.getColor(context, R.color.colorBottomNavigationActiveColored));
        fill(coloredTitleColorInactive, MAX_ITEMS, ContextCompat.getColor(context, R.color.colorBottomNavigationInactiveColored));

		if (attrs != null) {
			TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AHBottomNavigationBehavior_Params, 0, 0);
			try {
				selectedBackgroundVisible = ta.getBoolean(R.styleable.AHBottomNavigationBehavior_Params_selectedBackgroundVisible, false);
				translucentNavigationEnabled = ta.getBoolean(R.styleable.AHBottomNavigationBehavior_Params_translucentNavigationEnabled, false);
				
				map(titleActiveColor, ignored -> ta.getColor(R.styleable.AHBottomNavigationBehavior_Params_accentColor, ContextCompat.getColor(context, R.color.colorBottomNavigationAccent)));
				map(titleInactiveColor, ignored -> ta.getColor(R.styleable.AHBottomNavigationBehavior_Params_inactiveColor, ContextCompat.getColor(context, R.color.colorBottomNavigationInactive)));
				map(iconDisableColor, ignored -> ta.getColor(R.styleable.AHBottomNavigationBehavior_Params_disableColor, ContextCompat.getColor(context, R.color.colorBottomNavigationDisable)));

                map(coloredTitleColorActive, ignored -> ta.getColor(R.styleable.AHBottomNavigationBehavior_Params_coloredActive, ContextCompat.getColor(context, R.color.colorBottomNavigationActiveColored)));
                map(coloredTitleColorInactive, ignored -> ta.getColor(R.styleable.AHBottomNavigationBehavior_Params_coloredInactive, ContextCompat.getColor(context, R.color.colorBottomNavigationInactiveColored)));

				colored = ta.getBoolean(R.styleable.AHBottomNavigationBehavior_Params_colored, false);
				
			} finally {
				ta.recycle();
			}
		}

		notificationTextColor = ContextCompat.getColor(context, android.R.color.white);
		bottomNavigationHeight = (int) resources.getDimension(R.dimen.bottom_navigation_height);
		
		// Notifications
		notificationActiveMarginLeft = (int) resources.getDimension(R.dimen.bottom_navigation_notification_margin_left_active);
		notificationInactiveMarginLeft = (int) resources.getDimension(R.dimen.bottom_navigation_notification_margin_left);
		notificationActiveMarginTop = (int) resources.getDimension(R.dimen.bottom_navigation_notification_margin_top_active);
		notificationInactiveMarginTop = (int) resources.getDimension(R.dimen.bottom_navigation_notification_margin_top);
		notificationAnimationDuration = 150;

		ViewCompat.setElevation(this, resources.getDimension(R.dimen.bottom_navigation_elevation));
		setClipToPadding(false);

		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(MATCH_PARENT, bottomNavigationHeight);
		setLayoutParams(params);
	}

	/**
	 * Create the items in the bottom navigation
	 */
	protected void createItems() {
		if (items.size() < MIN_ITEMS) {
			Log.w(TAG, "The items list should have at least 3 items");
		} else if (items.size() > MAX_ITEMS) {
			Log.w(TAG, "The items list should not have more than 5 items");
		}

		int layoutHeight = (int) resources.getDimension(R.dimen.bottom_navigation_height);

		removeAllViews();
		views.clear();

		linearLayoutContainer = createContainerWithItems();
		backgroundColorView = createItemsContainerBkgView(linearLayoutContainer, layoutHeight);
		bottomNavigationHeight = layoutHeight;

		LayoutParams backgroundLayoutParams = new LayoutParams(MATCH_PARENT, calculateHeight(layoutHeight));
		addView(backgroundColorView, backgroundLayoutParams);

		// Force a request layout after all the items have been created
		post(this::requestLayout);
	}

	@NonNull
	private ViewGroup createContainerWithItems() {
		LinearLayout container = new LinearLayout(context);
		container.setOrientation(LinearLayout.HORIZONTAL);
		container.setGravity(Gravity.CENTER);

		if (isClassic()) {
			createClassicItems(container);
		} else {
			createSmallItems(container);
		}
		return container;
	}

	@NonNull
	private View createItemsContainerBkgView(ViewGroup itemsContainer, int layoutHeight) {
		int width = (getLayoutParams() == null ? MATCH_PARENT : getLayoutParams().width);

		// Brain-f* moment explained:
		// In order to really adhere to MATCH_PARENT, we must add an actual *view* (not view-group)
		// which requires real-estate -- the background-view in this case; This is mandatory since
		// the tab items' width calc has to be based on the total width, which would otherwise
		// be measured and remeasured as 0.
		// In WRAP_CONTENT, however, things work exactly the other way around. We need the
		// background-view to act as a mere "placeholder" in the form of a frame-layout with the
		// actual tab-items' content stating its desired width.
		View backgroundView;
		if (width == MATCH_PARENT) {
			backgroundView = new View(context);
			LayoutParams layoutParams = new LayoutParams(MATCH_PARENT, layoutHeight);
			this.addView(itemsContainer, layoutParams);
		} else if (width == WRAP_CONTENT) {
			backgroundView = new FrameLayout(context);
			((ViewGroup) backgroundView).addView(itemsContainer);
		} else {
			throw new IllegalStateException("Specific width specs (" + width + "px) are not supported");
		}
		return backgroundView;
	}

    /**
     * Check if items must be classic
     *
     * @return true if classic (icon + title)
     */
    private boolean isClassic() {
        if (preferLargeIcons && items.size() == MIN_ITEMS) return true;
        return titleState != TitleState.ALWAYS_HIDE &&
               titleState != TitleState.SHOW_WHEN_ACTIVE_FORCE &&
               (items.size() == MIN_ITEMS || titleState == TitleState.ALWAYS_SHOW);
    }

	@SuppressLint({"NewApi", "ResourceType"})
	private int calculateHeight(int layoutHeight) {
		if(!translucentNavigationEnabled) return layoutHeight;

		int resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
		if (resourceId > 0) navigationBarHeight = resources.getDimensionPixelSize(resourceId);

		int[] attrs = {android.R.attr.fitsSystemWindows, android.R.attr.windowTranslucentNavigation};
		TypedArray typedValue = getContext().getTheme().obtainStyledAttributes(attrs);

		@SuppressWarnings("ResourceType")
		boolean translucentNavigation = typedValue.getBoolean(1, true);

		if(hasImmersive() && translucentNavigation) layoutHeight += navigationBarHeight;

		typedValue.recycle();
		return layoutHeight;
	}

	@SuppressLint("NewApi")
	public boolean hasImmersive() {
		Display d = ((WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

		DisplayMetrics realDisplayMetrics = new DisplayMetrics();
		d.getRealMetrics(realDisplayMetrics);

		int realHeight = realDisplayMetrics.heightPixels;
		int realWidth = realDisplayMetrics.widthPixels;

		DisplayMetrics displayMetrics = new DisplayMetrics();
		d.getMetrics(displayMetrics);

		int displayHeight = displayMetrics.heightPixels;
		int displayWidth = displayMetrics.widthPixels;

		return (realWidth > displayWidth) || (realHeight > displayHeight);
	}

	/**
	 * Create classic items (only 3 items in the bottom navigation)
	 *
	 * @param linearLayout The layout where the items are added
	 */
	private void createClassicItems(LinearLayout linearLayout) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (items.isEmpty()) {
			return;
		}

		Integer itemWidth = calcItemLayoutWidth();
		if (itemWidth == null) {
			return;
		}

		float height = resources.getDimension(R.dimen.bottom_navigation_height);

		for (int i = 0; i < items.size(); i++) {
			final boolean current = (currentItem == i);
			final int itemIndex = i;

			AHBottomNavigationItem item = items.get(itemIndex);

			View view = inflater.inflate(R.layout.bottom_navigation_item, this, false);
			FrameLayout container = view.findViewById(R.id.bottom_navigation_container);
			ImageView icon = view.findViewById(R.id.bottom_navigation_item_icon);
			AHTextView title = view.findViewById(R.id.bottom_navigation_item_title);
            AHTextView notification = view.findViewById(R.id.bottom_navigation_notification);

            icon.getLayoutParams().width = getIconWidth(itemIndex);
            icon.getLayoutParams().height = getIconHeight(itemIndex);
			icon.setImageDrawable(item.getDrawable(context));

			if (titleState == TitleState.ALWAYS_HIDE || item.getTitle(context).isEmpty()) {
			    title.setVisibility(View.GONE);
                if (!animateTabSelection) {
			        AHHelper.updateMargin(icon, 0, 0, 0, 0);
                }
                ((LayoutParams) icon.getLayoutParams()).gravity = Gravity.CENTER;
                ((MarginLayoutParams) notification.getLayoutParams()).topMargin = (bottomNavigationHeight - getIconHeight(itemIndex)) / 2 - dpToPx(4);
            } else {
                title.setText(item.getTitle(context));
            }

            title.setTypeface(titleTypeface.get(i));

			if (titleState == TitleState.ALWAYS_SHOW && items.size() > MIN_ITEMS) {
				container.setPadding(0, container.getPaddingTop(), 0, container.getPaddingBottom());
			}

			if (current) {
				if (selectedBackgroundVisible) {
					view.setSelected(true);
				}
				icon.setSelected(true);
				// Update margins (icon & notification)
				if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams && animateTabSelection) {
					ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) icon.getLayoutParams();
					p.setMargins(p.leftMargin, activePaddingTop, p.rightMargin, p.bottomMargin);

					ViewGroup.MarginLayoutParams paramsNotification = (ViewGroup.MarginLayoutParams) notification.getLayoutParams();
					paramsNotification.setMargins(notificationActiveMarginLeft, paramsNotification.topMargin, paramsNotification.rightMargin, paramsNotification.bottomMargin);

					view.requestLayout();
				}
			} else {
				icon.setSelected(false);
				ViewGroup.MarginLayoutParams paramsNotification = (ViewGroup.MarginLayoutParams) notification.getLayoutParams();
				paramsNotification.setMargins(notificationInactiveMarginLeft, paramsNotification.topMargin, paramsNotification.rightMargin, paramsNotification.bottomMargin);
			}

			if (colored) {
				if (current) {
					setBackgroundColor(item.getColor(context));
					currentColor = item.getColor(context);
				}
			} else {
				if (defaultBackgroundResource != 0) {
					setBackgroundResource(defaultBackgroundResource);
				} else {
					setBackgroundColor(defaultBackgroundColor);
				}
			}
			
			title.setTextSize(TypedValue.COMPLEX_UNIT_PX, current ? getActiveTextSize(i) : getInactiveTextSize(i));
			
			if (itemsEnabledStates[i]) {
				view.setOnClickListener(v -> updateItems(itemIndex, true));
				icon.setImageDrawable(AHHelper.getTintDrawable(items.get(i).getDrawable(context), current ? iconActiveColor.get(i) : iconInactiveColor.get(i), forceTint));
				title.setTextColor(current ? titleActiveColor.get(i) : titleInactiveColor.get(i));
				view.setSoundEffectsEnabled(soundEffectsEnabled);
			} else {
				icon.setImageDrawable(AHHelper.getTintDrawable(items.get(i).getDrawable(context), iconDisableColor.get(i), forceTint));
				title.setTextColor(titleDisableColor.get(i));
			}

            if (item.getTag() != null) view.setTag(item.getTag());

            LayoutParams params = new LayoutParams(itemWidth, (int) height);
			linearLayout.addView(view, params);
			views.add(view);
            setTabAccessibilityLabel(itemIndex, currentItem);
		}

		updateNotifications(true, UPDATE_ALL_NOTIFICATIONS);
	}

    private int getIconHeight(int index) {
        return this.iconHeight.get(index) == null ?
                defaultIconHeight :
                dpToPx(this.iconHeight.get(index));
    }

    private int getIconWidth(int index) {
        return iconWidth.get(index) == null ?
                defaultIconWidth :
                dpToPx(iconWidth.get(index));
    }

    private float getInactiveTextSize(int index) {
	    if (titleInactiveTextSize.get(index) != null) return titleInactiveTextSize.get(index);
        if (titleState == TitleState.ALWAYS_SHOW && items.size() > MIN_ITEMS) {
            return resources.getDimension(R.dimen.bottom_navigation_text_size_forced_inactive);
        } else {
            return resources.getDimension(R.dimen.bottom_navigation_text_size_inactive);
        }
    }

    private float getActiveTextSize(int index) {
	    if (titleActiveTextSize.get(index) != null) return titleActiveTextSize.get(index).floatValue();
        if (titleState == TitleState.ALWAYS_SHOW && items.size() > MIN_ITEMS) {
            return resources.getDimension(R.dimen.bottom_navigation_text_size_forced_active);
        } else {
            return resources.getDimension(R.dimen.bottom_navigation_text_size_active);
        }
    }

	private Integer calcItemLayoutWidth() {
		ViewGroup.LayoutParams lp = getLayoutParams();
		int width = (lp == null ? MATCH_PARENT : lp.width);

		if (width == WRAP_CONTENT) {
			return WRAP_CONTENT;
		}

		if (width == MATCH_PARENT) {
			float layoutWidth = getWidth();
			if (layoutWidth == 0f) {
				return null;
			}

			float minWidth = resources.getDimension(R.dimen.bottom_navigation_min_width);
			float maxWidth = resources.getDimension(R.dimen.bottom_navigation_max_width);

			if (titleState == TitleState.ALWAYS_SHOW && items.size() > MIN_ITEMS) {
				minWidth = resources.getDimension(R.dimen.bottom_navigation_small_inactive_min_width);
				maxWidth = resources.getDimension(R.dimen.bottom_navigation_small_inactive_max_width);
			}

			float itemWidth = layoutWidth / items.size();
			itemWidth = Math.min(itemWidth, maxWidth);
			itemWidth = Math.max(itemWidth, minWidth);
			return (int) itemWidth;
		}

		throw new IllegalStateException("Specific width specs ("+width+"px) are not supported");
	}

	/**
	 * Create small items (more than 3 items in the bottom navigation)
	 *
	 * @param linearLayout The layout where the items are added
	 */
	private void createSmallItems(LinearLayout linearLayout) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		float height = resources.getDimension(R.dimen.bottom_navigation_height);
		float minWidth = resources.getDimension(R.dimen.bottom_navigation_small_inactive_min_width);
		float maxWidth = resources.getDimension(R.dimen.bottom_navigation_small_inactive_max_width);

		int layoutWidth = getWidth();
		if (layoutWidth == 0 || items.size() == 0) {
			return;
		}

		float itemWidth = layoutWidth / items.size();

		if (itemWidth < minWidth) {
			itemWidth = minWidth;
		} else if (itemWidth > maxWidth) {
			itemWidth = maxWidth;
		}

		selectedItemWidth = itemWidth + items.size() * widthDifference;
		itemWidth -= widthDifference;
		notSelectedItemWidth = itemWidth;


		for (int i = 0; i < items.size(); i++) {
			final int itemIndex = i;
            final boolean current = currentItem == i;
			AHBottomNavigationItem item = items.get(itemIndex);

			View view = inflater.inflate(R.layout.bottom_navigation_small_item, this, false);
			ImageView icon = view.findViewById(R.id.bottom_navigation_small_item_icon);
            AHTextView title = view.findViewById(R.id.bottom_navigation_small_item_title);
            AHTextView notification = view.findViewById(R.id.bottom_navigation_notification);
			icon.setImageDrawable(item.getDrawable(context));

            icon.getLayoutParams().width = getIconWidth(itemIndex);
            icon.getLayoutParams().height = getIconHeight(itemIndex);

			if (titleState != TitleState.ALWAYS_HIDE) {
				title.setText(item.getTitle(context));
			}
			if (
			        (titleState == TitleState.ALWAYS_HIDE || item.getTitle(context).isEmpty()) &&
                    titleState != TitleState.SHOW_WHEN_ACTIVE &&
                    titleState != TitleState.SHOW_WHEN_ACTIVE_FORCE
            ) {
                title.setVisibility(View.GONE);
                ((LayoutParams) icon.getLayoutParams()).gravity = Gravity.CENTER;
                AHHelper.updateMargin(icon, 0, 0, 0, 0);
            }

            float activeTextSize = getActiveTextSize(i);
            if (activeTextSize != 0) {
				title.setTextSize(TypedValue.COMPLEX_UNIT_PX, activeTextSize);
			}

            title.setTypeface(titleTypeface.get(i));

			if (current) {
				if (selectedBackgroundVisible) {
					view.setSelected(true);
				}
				icon.setSelected(true);
				// Update margins (icon & notification)

				if (titleState != TitleState.ALWAYS_HIDE && animateTabSelection) {
					if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
						ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) icon.getLayoutParams();
						p.setMargins(p.leftMargin, activeMarginTop, p.rightMargin, p.bottomMargin);

						ViewGroup.MarginLayoutParams paramsNotification = (ViewGroup.MarginLayoutParams)
								notification.getLayoutParams();
						paramsNotification.setMargins(notificationActiveMarginLeft, notificationActiveMarginTop,
								paramsNotification.rightMargin, paramsNotification.bottomMargin);

						view.requestLayout();
					}
				}
			} else {
				icon.setSelected(false);
                if (animateTabSelection) {
                    ViewGroup.MarginLayoutParams paramsNotification = (ViewGroup.MarginLayoutParams) notification.getLayoutParams();
                    paramsNotification.setMargins(
                            notificationInactiveMarginLeft,
                            notificationInactiveMarginTop,
                            paramsNotification.rightMargin,
                            paramsNotification.bottomMargin
                    );
                }
			}

			if (colored) {
				if (i == currentItem) {
					setBackgroundColor(item.getColor(context));
					currentColor = item.getColor(context);
				}
			} else {
				if (defaultBackgroundResource != 0) {
					setBackgroundResource(defaultBackgroundResource);
				} else {
					setBackgroundColor(defaultBackgroundColor);
				}
			}

			if (itemsEnabledStates[i]) {
				icon.setImageDrawable(AHHelper.getTintDrawable(items.get(i).getDrawable(context), currentItem == i ? iconActiveColor.get(i) : iconInactiveColor.get(i), forceTint));
				title.setTextColor(currentItem == i ? titleActiveColor.get(i) : titleInactiveColor.get(i));
				title.setAlpha(currentItem == i ? 1 : 0);
				view.setOnClickListener(v -> updateSmallItems(itemIndex, true));
				view.setSoundEffectsEnabled(soundEffectsEnabled);
			} else {
				icon.setImageDrawable(AHHelper.getTintDrawable(items.get(i).getDrawable(context), iconDisableColor.get(i), forceTint));
				title.setTextColor(titleDisableColor.get(i));
				title.setAlpha(0);
			}
			
			int width = i == currentItem ? (int) selectedItemWidth :
					(int) itemWidth;

			if (titleState == TitleState.ALWAYS_HIDE) {
				width = (int) (itemWidth * 1.16);
			}

            if (item.getTag() != null) view.setTag(item.getTag());

			LayoutParams params = new LayoutParams(width, (int) height);
			linearLayout.addView(view, params);
			views.add(view);
            setTabAccessibilityLabel(itemIndex, currentItem);
        }

		updateNotifications(true, UPDATE_ALL_NOTIFICATIONS);
	}

    private void setTabAccessibilityLabel(int itemIndex, int currentItem) {
        AHBottomNavigationItem item = items.get(itemIndex);
        String contentDescription = currentItem == itemIndex ? "selected, " : "";
        if (item.getTitle(context) != null) contentDescription += (item.getTitle(context) + ", ");
        if (AHHelper.isInteger(notifications.get(itemIndex).getReadableText())) {
            int num = Integer.parseInt(notifications.get(itemIndex).getReadableText());
            contentDescription += (num + " new item" + (num == 1 ? "" : "s") + ", ");
        }
        contentDescription += "tab, " + (itemIndex + 1) + " out of " + getItemsCount();
        views.get(itemIndex).setContentDescription(contentDescription);
    }

	/**
	 * Update Items UI
	 *
	 * @param itemIndex   int: Selected item position
	 * @param useCallback boolean: Use or not the callback
	 */
	private void updateItems(final int itemIndex, boolean useCallback) {
        for (int i = 0; i < views.size(); i++) {
            setTabAccessibilityLabel(i, itemIndex);
        }
		if (currentItem == itemIndex) {
			if (tabSelectedListener != null && useCallback) {
				tabSelectedListener.onTabSelected(itemIndex, true);
			}
			return;
		}

		if (tabSelectedListener != null && useCallback) {
			boolean selectionAllowed = tabSelectedListener.onTabSelected(itemIndex, false);
			if (!selectionAllowed) return;
		}

		int activeMarginTop = (int) resources.getDimension(R.dimen.bottom_navigation_margin_top_active);
		int inactiveMarginTop = (int) resources.getDimension(R.dimen.bottom_navigation_margin_top_inactive);

		for (int i = 0; i < views.size(); i++) {

			final View view = views.get(i);
			if (selectedBackgroundVisible) {
				view.setSelected(i == itemIndex);
			}

			if (i == itemIndex) {
				final AHTextView title = view.findViewById(R.id.bottom_navigation_item_title);
				final ImageView icon = view.findViewById(R.id.bottom_navigation_item_icon);
				final AHTextView notification = view.findViewById(R.id.bottom_navigation_notification);

				icon.setSelected(true);
                if (animateTabSelection) {
                    AHHelper.updateTopMargin(icon, inactiveMarginTop, activeMarginTop);
                    AHHelper.updateLeftMargin(notification, notificationInactiveMarginLeft, notificationActiveMarginLeft);
				    AHHelper.updateTextSize(title, getInactiveTextSize(i), getActiveTextSize(i));
                }
				AHHelper.updateTextColor(title, titleInactiveColor.get(i), titleActiveColor.get(i));
				AHHelper.updateDrawableColor(items.get(itemIndex).getDrawable(context), icon, iconInactiveColor.get(i), iconActiveColor.get(i), forceTint);

				if (colored) {
					int finalRadius = Math.max(getWidth(), getHeight());
					int cx = (int) view.getX() + view.getWidth() / 2;
					int cy = view.getHeight() / 2;

					if (circleRevealAnim != null && circleRevealAnim.isRunning()) {
						circleRevealAnim.cancel();
						setBackgroundColor(items.get(itemIndex).getColor(context));
						backgroundColorView.setBackgroundColor(Color.TRANSPARENT);
					}

					circleRevealAnim = ViewAnimationUtils.createCircularReveal(backgroundColorView, cx, cy, 0, finalRadius);
					circleRevealAnim.setStartDelay(5);
					circleRevealAnim.addListener(new StubAnimationListener() {
						@Override
						public void onAnimationStart(@NonNull Animator animation) {
							backgroundColorView.setBackgroundColor(items.get(itemIndex).getColor(context));
						}

						@Override
						public void onAnimationEnd(Animator animation) {
							setBackgroundColor(items.get(itemIndex).getColor(context));
							backgroundColorView.setBackgroundColor(Color.TRANSPARENT);
						}
					});
					circleRevealAnim.start();
				} else {
					if (defaultBackgroundResource != 0) {
						setBackgroundResource(defaultBackgroundResource);
					} else {
						setBackgroundColor(defaultBackgroundColor);
					}
					backgroundColorView.setBackgroundColor(Color.TRANSPARENT);
				}

			} else if (i == currentItem) {
				final AHTextView title = view.findViewById(R.id.bottom_navigation_item_title);
				final ImageView icon = view.findViewById(R.id.bottom_navigation_item_icon);
				final AHTextView notification = view.findViewById(R.id.bottom_navigation_notification);

				icon.setSelected(false);
                if (animateTabSelection) {
                    AHHelper.updateTopMargin(icon, activeMarginTop, inactiveMarginTop);
                    AHHelper.updateLeftMargin(notification, notificationActiveMarginLeft, notificationInactiveMarginLeft);
				    AHHelper.updateTextSize(title, getActiveTextSize(i), getInactiveTextSize(i));
                }
				AHHelper.updateTextColor(title, titleActiveColor.get(i), titleInactiveColor.get(i));
				AHHelper.updateDrawableColor(items.get(currentItem).getDrawable(context), icon, iconActiveColor.get(i), iconInactiveColor.get(i), forceTint);
			}
		}

		currentItem = itemIndex;
		if (currentItem > 0 && currentItem < items.size()) {
			currentColor = items.get(currentItem).getColor(context);
		} else if (currentItem == CURRENT_ITEM_NONE) {
			if (defaultBackgroundResource != 0) {
				setBackgroundResource(defaultBackgroundResource);
			} else {
				setBackgroundColor(defaultBackgroundColor);
			}
			backgroundColorView.setBackgroundColor(Color.TRANSPARENT);
		}
	}

	/**
	 * Update Small items UI
	 *
	 * @param itemIndex   int: Selected item position
	 * @param useCallback boolean: Use or not the callback
	 */
	private void updateSmallItems(final int itemIndex, boolean useCallback) {

		if (currentItem == itemIndex) {
			if (tabSelectedListener != null && useCallback) {
				tabSelectedListener.onTabSelected(itemIndex, true);
			}
			return;
		}

		if (tabSelectedListener != null && useCallback) {
			boolean selectionAllowed = tabSelectedListener.onTabSelected(itemIndex, false);
			if (!selectionAllowed) return;
		}

		int activeMarginTop = (int) resources.getDimension(R.dimen.bottom_navigation_small_margin_top_active);
		int inactiveMargin = (int) resources.getDimension(R.dimen.bottom_navigation_small_margin_top);

		for (int i = 0; i < views.size(); i++) {

			final View view = views.get(i);
			if (selectedBackgroundVisible) {
				view.setSelected(i == itemIndex);
			}

			if (i == itemIndex) {

				final FrameLayout container = view.findViewById(R.id.bottom_navigation_small_container);
				final AHTextView title = view.findViewById(R.id.bottom_navigation_small_item_title);
				final ImageView icon = view.findViewById(R.id.bottom_navigation_small_item_icon);
				final AHTextView notification = view.findViewById(R.id.bottom_navigation_notification);

				icon.setSelected(true);

				if (titleState != TitleState.ALWAYS_HIDE) {
					AHHelper.updateTopMargin(icon, inactiveMargin, activeMarginTop);
					AHHelper.updateLeftMargin(notification, notificationInactiveMarginLeft, notificationActiveMarginLeft);
					AHHelper.updateTopMargin(notification, notificationInactiveMarginTop, notificationActiveMarginTop);
					AHHelper.updateTextColor(title, iconInactiveColor.get(i), iconActiveColor.get(i));
					AHHelper.updateWidth(container, notSelectedItemWidth, selectedItemWidth);
				}

				AHHelper.updateAlpha(title, 0, 1);
				AHHelper.updateDrawableColor(items.get(itemIndex).getDrawable(context), icon, iconInactiveColor.get(i), iconActiveColor.get(i), forceTint);

				if (colored) {
					int finalRadius = Math.max(getWidth(), getHeight());
					int cx = (int) views.get(itemIndex).getX() + views.get(itemIndex).getWidth() / 2;
					int cy = views.get(itemIndex).getHeight() / 2;

					if (circleRevealAnim != null && circleRevealAnim.isRunning()) {
						circleRevealAnim.cancel();
						setBackgroundColor(items.get(itemIndex).getColor(context));
						backgroundColorView.setBackgroundColor(Color.TRANSPARENT);
					}

					circleRevealAnim = ViewAnimationUtils.createCircularReveal(backgroundColorView, cx, cy, 0, finalRadius);
					circleRevealAnim.setStartDelay(5);
					circleRevealAnim.addListener(new Animator.AnimatorListener() {
						@Override
						public void onAnimationStart(Animator animation) {
							backgroundColorView.setBackgroundColor(items.get(itemIndex).getColor(context));
						}

						@Override
						public void onAnimationEnd(Animator animation) {
							setBackgroundColor(items.get(itemIndex).getColor(context));
							backgroundColorView.setBackgroundColor(Color.TRANSPARENT);
						}

						@Override
						public void onAnimationCancel(Animator animation) {
						}

						@Override
						public void onAnimationRepeat(Animator animation) {
						}
					});
					circleRevealAnim.start();
				} else {
					if (defaultBackgroundResource != 0) {
						setBackgroundResource(defaultBackgroundResource);
					} else {
						setBackgroundColor(defaultBackgroundColor);
					}
					backgroundColorView.setBackgroundColor(Color.TRANSPARENT);
				}

			} else if (i == currentItem) {
				final View container = view.findViewById(R.id.bottom_navigation_small_container);
				final AHTextView title = view.findViewById(R.id.bottom_navigation_small_item_title);
				final ImageView icon = view.findViewById(R.id.bottom_navigation_small_item_icon);
				final AHTextView notification = view.findViewById(R.id.bottom_navigation_notification);

				icon.setSelected(false);

				if (titleState != TitleState.ALWAYS_HIDE) {
					AHHelper.updateTopMargin(icon, activeMarginTop, inactiveMargin);
					AHHelper.updateLeftMargin(notification, notificationActiveMarginLeft, notificationInactiveMarginLeft);
					AHHelper.updateTopMargin(notification, notificationActiveMarginTop, notificationInactiveMarginTop);
					AHHelper.updateTextColor(title, iconActiveColor.get(i), iconInactiveColor.get(i));
					AHHelper.updateWidth(container, selectedItemWidth, notSelectedItemWidth);
				}

				AHHelper.updateAlpha(title, 1, 0);
				AHHelper.updateDrawableColor(items.get(currentItem).getDrawable(context), icon, iconActiveColor.get(i), iconInactiveColor.get(i), forceTint);
			}
		}

		currentItem = itemIndex;
		if (currentItem > 0 && currentItem < items.size()) {
			currentColor = items.get(currentItem).getColor(context);
		} else if (currentItem == CURRENT_ITEM_NONE) {
			if (defaultBackgroundResource != 0) {
				setBackgroundResource(defaultBackgroundResource);
			} else {
				setBackgroundColor(defaultBackgroundColor);
			}
			backgroundColorView.setBackgroundColor(Color.TRANSPARENT);
		}
	}

	/**
	 * Update notifications
	 */
	private void updateNotifications(boolean updateStyle, int itemPosition) {
		for (int i = 0; i < views.size(); i++) {
			if (i >= notifications.size()) {
				break;
			}
			
			if (itemPosition != UPDATE_ALL_NOTIFICATIONS && itemPosition != i) {
				continue;
			}

			final AHNotification notificationItem = notifications.get(i);
			final int currentTextColor = AHNotificationHelper.getTextColor(notificationItem, notificationTextColor);
			final int currentBackgroundColor = AHNotificationHelper.getBackgroundColor(notificationItem, notificationBackgroundColor);

			AHTextView notification = views.get(i).findViewById(R.id.bottom_navigation_notification);

			if (updateStyle) {
                notification.setElevation(notificationItem.isDot() ? 0 : defaultNotificationElevation);
                notification.setTextColor(currentTextColor);
				if (notificationTypeface != null) {
					notification.setTypeface(notificationTypeface);
				} else {
					notification.setTypeface(null, Typeface.BOLD);
				}

				if (notificationBackgroundDrawable != null) {
                    Drawable drawable = notificationBackgroundDrawable.getConstantState().newDrawable();
                    notification.setBackground(drawable);

                } else if (currentBackgroundColor != 0) {
					Drawable defaultDrawable = ContextCompat.getDrawable(context, R.drawable.notification_background);
                    notification.setBackground(AHHelper.getTintDrawable(defaultDrawable, currentBackgroundColor, forceTint));
                }
			}

			if (notificationItem.isEmpty()) {
                hideNotification(notificationItem, notification);
            } else {
                showNotification(notificationItem, notification);
            }
		}
	}

    private void showNotification(AHNotification notification, AHTextView notificationView) {
        notificationView.setText(notification.getReadableText());
        updateNotificationSize(notification, notificationView);
        if (notificationView.getAlpha() != 1) {
            if (shouldAnimateNotification(notification)) {
                animateNotificationShow(notificationView);
                notification.setAnimate(false);
            } else {
                notificationView.setScaleX(1);
                notificationView.setScaleY(1);
                notificationView.setAlpha(1);
            }
        }
    }

    private void animateNotificationShow(AHTextView notification) {
	    notification.setScaleX(0);
        notification.setScaleY(0);
        notification.setAlpha(0);
        notification.animate()
                .scaleX(1)
                .scaleY(1)
                .alpha(1)
                .setInterpolator(new OvershootInterpolator())
                .setDuration(notificationAnimationDuration)
                .start();
    }

    private void hideNotification(AHNotification notification, AHTextView notificationView) {
        if (notificationView.getAlpha() != 0) {
            if (shouldAnimateNotification(notification)) {
                animateHideNotification(notificationView);
                notification.setAnimate(false);
            } else {
                notificationView.setScaleX(0);
                notificationView.setScaleY(0);
                notificationView.setAlpha(0);
            }
        }
    }

    private void animateHideNotification(AHTextView notification) {
        notification.animate()
                .scaleX(0)
                .scaleY(0)
                .alpha(0)
                .setInterpolator(new AccelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (notification.getAlpha() == 0) notification.setText("");
                    }

                })
                .setDuration(notificationAnimationDuration)
                .start();
    }

    private void updateNotificationSize(AHNotification notificationItem, AHTextView notification) {
        ViewGroup.LayoutParams lp = notification.getLayoutParams();
        lp.width = notificationItem.getSize() >= 0 && !notificationItem.hasText() ? notificationItem.getSize() : ViewGroup.LayoutParams.WRAP_CONTENT;
        lp.height = notificationItem.getSize() >= 0 ? notificationItem.getSize() : getResources().getDimensionPixelSize(R.dimen.bottom_navigation_notification_height);
        notification.requestLayout();
    }

    private boolean shouldAnimateNotification(AHNotification notification) {
        return notification.shouldAnimate() && animateTabSelection;
    }


    ////////////
	// PUBLIC //
	////////////

	/**
	 * Add an item
	 */
	public void addItem(AHBottomNavigationItem item) {
		if (this.items.size() > MAX_ITEMS) {
			Log.w(TAG, "The items list should not have more than 5 items");
		}
		items.add(item);
		createItems();
	}

	/**
	 * Add all items
	 */
	public void addItems(List<AHBottomNavigationItem> items) {
		if (items.size() > MAX_ITEMS || (this.items.size() + items.size()) > MAX_ITEMS) {
			Log.w(TAG, "The items list should not have more than 5 items");
		}
		this.items.addAll(items);
		createItems();
	}

	/**
	 * Remove an item at the given index
	 */
	public void removeItemAtIndex(int index) {
		if (index < items.size()) {
			this.items.remove(index);
			createItems();
		}
	}

	/**
	 * Remove all items
	 */
	public void removeAllItems() {
		this.items.clear();
		createItems();
	}

	/**
	 * Refresh the AHBottomView
	 */
	public void refresh() {
		createItems();
	}

	/**
	 * Return the number of items
	 *
	 * @return int
	 */
	public int getItemsCount() {
		return items.size();
	}

	/**
	 * Return if the Bottom Navigation is colored
	 */
	public boolean isColored() {
		return colored;
	}

	/**
	 * Set if the Bottom Navigation is colored
	 */
	public void setColored(boolean colored) {
		this.colored = colored;
		this.iconActiveColor = colored ? coloredTitleColorActive : titleActiveColor;
		this.iconInactiveColor = colored ? coloredTitleColorInactive : titleInactiveColor;
		createItems();
	}

	/**
	 * Return the bottom navigation background color
	 *
	 * @return The bottom navigation background color
	 */
	public int getDefaultBackgroundColor() {
		return defaultBackgroundColor;
	}

	/**
	 * Set the bottom navigation background color
	 *
	 * @param defaultBackgroundColor The bottom navigation background color
	 */
	public void setDefaultBackgroundColor(@ColorInt int defaultBackgroundColor) {
		this.defaultBackgroundColor = defaultBackgroundColor;
		createItems();
	}

	/**
	 * Set the bottom navigation background resource
	 *
	 * @param defaultBackgroundResource The bottom navigation background resource
	 */
	public void setDefaultBackgroundResource(@DrawableRes int defaultBackgroundResource) {
		this.defaultBackgroundResource = defaultBackgroundResource;
		createItems();
	}

    public void setAnimateTabSelection(boolean animateTabSelection) {
        this.animateTabSelection = animateTabSelection;
    }

    /**
	 * Get the accent color (used when the view contains 3 items)
	 *
	 * @return The default accent color
	 */
	public @Nullable Integer getIconActiveColor(int index) {
		return iconActiveColor.get(index);
	}

	/**
	 * Set the accent color (used when the view contains 3 items)
	 *
	 * @param activeColor The new accent color
	 */
	public void setIconActiveColor(int index, @Nullable Integer activeColor) {
	    if (AHHelper.equals(iconActiveColor.get(index), activeColor)) return;
		iconActiveColor.set(index, activeColor);
		createItems();
	}

    public void setIconWidth(int index, Integer width) {
        if (AHHelper.equals(iconWidth.get(index), width)) return;
        iconWidth.set(index, width);
        createItems();
    }

    public void setIconHeight(int index, Integer height) {
        if (AHHelper.equals(iconHeight.get(index), height)) return;
        iconHeight.set(index, height);
        createItems();
    }

    /**
     * Set the accent color (used when the view contains 3 items)
     *
     * @param activeColor The new accent color
     */
    public void setTitleActiveColor(int index, @Nullable Integer activeColor) {
        if (AHHelper.equals(titleActiveColor.get(index), activeColor)) return;
        titleActiveColor.set(index, activeColor);
        createItems();
    }

	/**
	 * Get the inactive color (used when the view contains 3 items)
	 *
	 * @return The inactive color
	 */
	public @Nullable Integer getIconInactiveColor(int index) {
		return iconInactiveColor.get(index);
	}

    /**
     * Get the inactive color (used when the view contains 3 items)
     *
     * @return The inactive color
     */
    public @Nullable Integer getTitleInactiveColor(int index) {
        return titleInactiveColor.get(index);
    }

	/**
	 * Set the inactive color (used when the view contains 3 items)
	 *
	 * @param inactiveColor The inactive color
	 */
	public void setIconInactiveColor(int index, @Nullable Integer inactiveColor) {
	    if (AHHelper.equals(iconInactiveColor.get(index), inactiveColor)) return;
		iconInactiveColor.set(index, inactiveColor);
		createItems();
	}

    /**
     * Set the inactive color (used when the view contains 3 items)
     *
     * @param inactiveColor The inactive color
     */
    public void setTitleInactiveColor(int index, @Nullable Integer inactiveColor) {
        if (AHHelper.equals(titleInactiveColor.get(index), inactiveColor)) return;
        titleInactiveColor.set(index, inactiveColor);
        createItems();
    }

	/**
	 * Set the colors used when the bottom bar uses the colored mode
	 *
	 * @param colorActive   The active color
	 * @param colorInactive The inactive color
	 */
	public void setColoredModeColors(int index, @ColorInt int colorActive, @ColorInt int colorInactive) {
		coloredTitleColorActive.set(index, colorActive);
		coloredTitleColorInactive.set(index, colorInactive);
		createItems();
	}

	/**
	 * Set selected background visibility
     */
	public void setSelectedBackgroundVisible(boolean visible) {
		this.selectedBackgroundVisible = visible;
		createItems();
	}

	/**
	 * Set notification typeface
	 *
	 * @param typeface Typeface
	 */
	public void setTitleTypeface(int index, @Nullable Typeface typeface) {
	    if (titleTypeface.get(index) == typeface) return;
		titleTypeface.set(index, typeface);
		createItems();
	}

	/**
	 * Set title active text size in pixels
	 */
	public void setTitleActiveTextSize(int index, Float activeSize) {
        if (AHHelper.equals(titleActiveTextSize.get(index), activeSize)) return;
        titleActiveTextSize.set(index, activeSize);
		createItems();
	}

    /**
     * Set title inactive text size in pixels
     */
    public void setTitleInactiveTextSize(int index, Float inactiveSize) {
        if (AHHelper.equals(titleInactiveTextSize.get(index), inactiveSize)) return;
        titleInactiveTextSize.set(index, inactiveSize);
        createItems();
    }

	/**
	 * Set title text size in SP
	 *
	 * @param activeSize in sp
	 */
	public void setTitleActiveTextSizeInSp(int index, Float activeSize) {
        if (AHHelper.equals(titleActiveTextSize.get(index), activeSize)) return;
		this.titleActiveTextSize.set(index, (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, activeSize, resources.getDisplayMetrics())));
		createItems();
	}

    /**
     * Set title text size in SP
     *
     * @param inactiveSize in sp
     */
    public void setTitleInactiveTextSizeInSp(int index, Float inactiveSize) {
        if (AHHelper.equals(titleInactiveTextSize.get(index), inactiveSize)) return;
        this.titleInactiveTextSize.set(index, (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, inactiveSize, resources.getDisplayMetrics())));
        createItems();
    }

    public void setTag(int index, String tag) {
        if (index >= 0 && index < views.size()) views.get(index).setTag(tag);
    }

	/**
	 * Get item at the given index
	 *
	 * @param position int: item position
	 * @return The item at the given position
	 */
	public AHBottomNavigationItem getItem(int position) {
		if (position < 0 || position > items.size() - 1) {
			Log.w(TAG, "The position is out of bounds of the items (" + items.size() + " elements)");
			return null;
		}
		return items.get(position);
	}

	/**
	 * Get the current item
	 *
	 * @return The current item position
	 */
	public int getCurrentItem() {
		return currentItem;
	}

	/**
	 * Set the current item
	 *
	 * @param position int: position
	 */
	public void setCurrentItem(int position) {
		setCurrentItem(position, true);
	}

	/**
	 * Set the current item
	 *
	 * @param position    int: item position
	 * @param useCallback boolean: use or not the callback
	 */
	public void setCurrentItem(int position, boolean useCallback) {
		if (position >= items.size()) {
			Log.w(TAG, "The position is out of bounds of the items (" + items.size() + " elements)");
			return;
		}

        if (isClassic()) {
			updateItems(position, useCallback);
		} else {
			updateSmallItems(position, useCallback);
		}
	}

	/**
	 * Return if the behavior translation is enabled
	 *
	 * @return a boolean value
	 */
	public boolean isBehaviorTranslationEnabled() {
		return behaviorTranslationEnabled;
	}

	/**
	 * Set the behavior translation value
	 *
	 * @param behaviorTranslationEnabled boolean for the state
	 */
	public void setBehaviorTranslationEnabled(boolean behaviorTranslationEnabled) {
		this.behaviorTranslationEnabled = behaviorTranslationEnabled;
		if (getParent() instanceof CoordinatorLayout) {
			ViewGroup.LayoutParams params = getLayoutParams();
			if (bottomNavigationBehavior == null) {
				bottomNavigationBehavior = new AHBottomNavigationBehavior<>(behaviorTranslationEnabled, navigationBarHeight);
			} else {
				bottomNavigationBehavior.setBehaviorTranslationEnabled(behaviorTranslationEnabled, navigationBarHeight);
			}
			if (navigationPositionListener != null) {
				bottomNavigationBehavior.setOnNavigationPositionListener(navigationPositionListener);
			}
			((CoordinatorLayout.LayoutParams) params).setBehavior(bottomNavigationBehavior);
			if (needHideBottomNavigation) {
				needHideBottomNavigation = false;
				bottomNavigationBehavior.hideView(this, bottomNavigationHeight, hideBottomNavigationWithAnimation);
			}
		}
	}

	/**
	 * Manage the floating action button behavior with AHBottomNavigation
	 * @param fab Floating Action Button
	 */
	public void manageFloatingActionButtonBehavior(FloatingActionButton fab) {
		if (fab.getParent() instanceof CoordinatorLayout) {
			AHBottomNavigationFABBehavior fabBehavior = new AHBottomNavigationFABBehavior(navigationBarHeight);
			((CoordinatorLayout.LayoutParams) fab.getLayoutParams())
					.setBehavior(fabBehavior);
		}
	}

	/**
	 * Hide Bottom Navigation with animation
	 */
	public void hideBottomNavigation() {
		hideBottomNavigation(true);
	}

	/**
	 * Hide Bottom Navigation with or without animation
	 *
	 * @param withAnimation Boolean
	 */
	public void hideBottomNavigation(boolean withAnimation) {
		if (bottomNavigationBehavior != null) {
			bottomNavigationBehavior.hideView(this, bottomNavigationHeight, withAnimation);
		} else if (getParent() instanceof CoordinatorLayout) {
			needHideBottomNavigation = true;
			hideBottomNavigationWithAnimation = withAnimation;
		} else {
			// Hide bottom navigation
			ViewCompat.animate(this)
					.translationY(bottomNavigationHeight)
					.setInterpolator(new LinearOutSlowInInterpolator())
					.setDuration(withAnimation ? 300 : 0)
					.start();
		}
	}

	/**
	 * Restore Bottom Navigation with animation
	 */
	public void restoreBottomNavigation() {
		restoreBottomNavigation(true);
	}

	/**
	 * Restore Bottom Navigation with or without animation
	 *
	 * @param withAnimation Boolean
	 */
	public void restoreBottomNavigation(boolean withAnimation) {
		if (bottomNavigationBehavior != null) {
			bottomNavigationBehavior.resetOffset(this, withAnimation);
		} else {
			// Show bottom navigation
			ViewCompat.animate(this)
					.translationY(0)
					.setInterpolator(new LinearOutSlowInInterpolator())
					.setDuration(withAnimation ? 300 : 0)
					.start();
		}
	}

	/**
	 * Return if the translucent navigation is enabled
	 */
	public boolean isTranslucentNavigationEnabled() {
		return translucentNavigationEnabled;
	}

	/**
	 * Set the translucent navigation value
	 */
	public void setTranslucentNavigationEnabled(boolean translucentNavigationEnabled) {
		this.translucentNavigationEnabled = translucentNavigationEnabled;
	}

	/**
	 * Return if the tint should be forced (with setColorFilter)
	 *
	 * @return Boolean
	 */
	public boolean isForceTint() {
		return forceTint;
	}

	/**
	 * Set the force tint value
	 * If forceTint = true, the tint is made with drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
	 *
	 * @param forceTint Boolean
	 */
	public void setForceTint(boolean forceTint) {
		this.forceTint = forceTint;
		createItems();
	}

	/**
	 * Return the title state for display
	 *
	 * @return TitleState
	 */
	public TitleState getTitleState() {
		return titleState;
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
	public void setTitleState(TitleState titleState) {
		this.titleState = titleState;
		createItems();
	}

	/**
	 * Set AHOnTabSelectedListener
	 */
	public void setOnTabSelectedListener(com.aurelhubert.ahbottomnavigation.AHBottomNavigation.OnTabSelectedListener tabSelectedListener) {
		this.tabSelectedListener = tabSelectedListener;
	}

	/**
	 * Remove AHOnTabSelectedListener
	 */
	public void removeOnTabSelectedListener() {
		this.tabSelectedListener = null;
	}

	/**
	 * Set OnNavigationPositionListener
	 */
	public void setOnNavigationPositionListener(com.aurelhubert.ahbottomnavigation.AHBottomNavigation.OnNavigationPositionListener navigationPositionListener) {
		this.navigationPositionListener = navigationPositionListener;
		if (bottomNavigationBehavior != null) {
			bottomNavigationBehavior.setOnNavigationPositionListener(navigationPositionListener);
		}
	}

	/**
	 * Remove OnNavigationPositionListener()
	 */
	public void removeOnNavigationPositionListener() {
		this.navigationPositionListener = null;
		if (bottomNavigationBehavior != null) {
			bottomNavigationBehavior.removeOnNavigationPositionListener();
		}
	}

	/**
	 * Set the notification number
	 *
	 * @param nbNotification int
	 * @param itemPosition   int
	 */
	@Deprecated
	public void setNotification(int nbNotification, int itemPosition) {
		if (itemPosition < 0 || itemPosition > items.size() - 1) {
            throw new IndexOutOfBoundsException(String.format(Locale.US, EXCEPTION_INDEX_OUT_OF_BOUNDS, itemPosition, items.size()));
		}
        final String title = nbNotification == 0 ? "" : String.valueOf(nbNotification);
        notifications.set(itemPosition, AHNotification.justText(title));
		updateNotifications(false, itemPosition);
	}

	/**
	 * Set notification text
	 *
	 * @param title        String
	 * @param itemPosition int
	 */
	public void setNotification(String title, int itemPosition) {
        if (itemPosition < 0 || itemPosition > items.size() - 1) {
            throw new IndexOutOfBoundsException(String.format(Locale.US, EXCEPTION_INDEX_OUT_OF_BOUNDS, itemPosition, items.size()));
        }
        notifications.set(itemPosition, AHNotification.justText(title));
        updateNotifications(false, itemPosition);
    }

    /**
     * Set fully customized Notification
     *
     * @param notification AHNotification
     * @param itemPosition int
     */
	public void setNotification(AHNotification notification, int itemPosition) {
		if (itemPosition < 0 || itemPosition > items.size() - 1) {
            throw new IndexOutOfBoundsException(String.format(Locale.US, EXCEPTION_INDEX_OUT_OF_BOUNDS, itemPosition, items.size()));
		}
        if (notification == null) {
            notification = new AHNotification(); // instead of null, use empty notification
        }
		notifications.set(itemPosition, notification);
		updateNotifications(true, itemPosition);
	}

    public void setNotificationSize(int index, @Px Integer size) {
        if (AHHelper.equals(notifications.get(index).getSize(), size)) return;
        notifications.get(index).setSize(size);
        updateNotifications(true, index);
    }

	/**
	 * Set notification text color
	 *
	 * @param textColor int
	 */
	public void setNotificationTextColor(@ColorInt int textColor) {
	    if (notificationTextColor == textColor) return;
		notificationTextColor = textColor;
		updateNotifications(true, UPDATE_ALL_NOTIFICATIONS);
	}

	/**
	 * Set notification text color
	 *
	 * @param textColor int
	 */
	public void setNotificationTextColorResource(@ColorRes int textColor) {
		this.notificationTextColor = ContextCompat.getColor(context, textColor);
		updateNotifications(true, UPDATE_ALL_NOTIFICATIONS);
	}

	/**
	 * Set notification background resource
	 *
	 * @param drawable Drawable
	 */
	public void setNotificationBackground(Drawable drawable) {
		this.notificationBackgroundDrawable = drawable;
		updateNotifications(true, UPDATE_ALL_NOTIFICATIONS);
	}

	/**
	 * Set notification background color
	 *
	 * @param color int
	 */
	public void setNotificationBackgroundColor(@ColorInt int color) {
	    if (notificationBackgroundColor == color) return;
		notificationBackgroundColor = color;
		updateNotifications(true, UPDATE_ALL_NOTIFICATIONS);
	}

	/**
	 * Set notification background color
	 *
	 * @param color int
	 */
	public void setNotificationBackgroundColorResource(@ColorRes int color) {
		this.notificationBackgroundColor = ContextCompat.getColor(context, color);
		updateNotifications(true, UPDATE_ALL_NOTIFICATIONS);
	}

	/**
	 * Set notification typeface
	 *
	 * @param typeface Typeface
	 */
	public void setNotificationTypeface(Typeface typeface) {
		this.notificationTypeface = typeface;
		updateNotifications(true, UPDATE_ALL_NOTIFICATIONS);
	}

	public void setNotificationAnimationDuration(long notificationAnimationDuration) {
		this.notificationAnimationDuration = notificationAnimationDuration;
		updateNotifications(true, UPDATE_ALL_NOTIFICATIONS);
	}

	/**
	 * Set the notification margin left
	 */
	public void setNotificationMarginLeft(int activeMargin, int inactiveMargin) {
		this.notificationActiveMarginLeft = activeMargin;
		this.notificationInactiveMarginLeft = inactiveMargin;
		createItems();
	}

	/**
	 * Activate or not the elevation
	 *
	 * @param useElevation boolean
	 */
	public void setUseElevation(boolean useElevation) {
		ViewCompat.setElevation(this, useElevation ? resources.getDimension(R.dimen.bottom_navigation_elevation) : 0);
		setClipToPadding(false);
	}

	/**
	 * Activate or not the elevation, and set the value
	 *
	 * @param useElevation boolean
	 * @param elevation    float
	 */
	public void setUseElevation(boolean useElevation, float elevation) {
		ViewCompat.setElevation(this, useElevation ? elevation : 0);
		setClipToPadding(false);
	}

	/**
	 * Return if the Bottom Navigation is hidden or not
	 */
	public boolean isHidden() {
		if (bottomNavigationBehavior != null) {
			return bottomNavigationBehavior.isHidden();
		}
		return false;
	}

	/**
	 * Get the view at the given position
	 * @param position int
	 * @return The view at the position, or null
	 */
	public View getViewAtPosition(int position) {
		if (linearLayoutContainer != null && position >= 0
				&& position < linearLayoutContainer.getChildCount()) {
			return linearLayoutContainer.getChildAt(position);
		}
		return null;
	}
	
	/**
	 * Enable the tab item at the given position
	 * @param position int
	 */
	public void enableItemAtPosition(int position) {
		if (position < 0 || position > items.size() - 1) {
			Log.w(TAG, "The position is out of bounds of the items (" + items.size() + " elements)");
			return;
		}
		itemsEnabledStates[position] = true;
		createItems();
	}
	
	/**
	 * Disable the tab item at the given position
	 * @param position int
	 */
	public void disableItemAtPosition(int position) {
		if (position < 0 || position > items.size() - 1) {
			Log.w(TAG, "The position is out of bounds of the items (" + items.size() + " elements)");
			return;
		}
		itemsEnabledStates[position] = false;
		createItems();
	}
	
	/**
	 * Set the item disable color
	 * @param iconDisableColor int
	 */
	public void setIconDisableColor(int index, @ColorInt int iconDisableColor) {
		this.iconDisableColor.set(index, iconDisableColor);
	}
	
	////////////////
	// INTERFACES //
	////////////////

	public interface OnTabSelectedListener {
		/**
		 * Called when a tab has been selected (clicked)
		 *
		 * @param position    int: Position of the selected tab
		 * @param wasSelected boolean: true if the tab was already selected
		 * @return boolean: true for updating the tab UI, false otherwise
		 */
		boolean onTabSelected(int position, boolean wasSelected);
	}

	public interface OnNavigationPositionListener {
		/**
		 * Called when the bottom navigation position is changed
		 *
		 * @param y int: y translation of bottom navigation
		 */
		void onPositionChange(int y);
	}

    public void setPreferLargeIcons(boolean preferLargeIcons) {
        this.preferLargeIcons = preferLargeIcons;
    }

    private int dpToPx(int dp) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        return dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}
