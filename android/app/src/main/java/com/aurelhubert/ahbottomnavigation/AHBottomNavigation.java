package com.aurelhubert.ahbottomnavigation;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
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
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.notification.AHNotification;
import com.aurelhubert.ahbottomnavigation.notification.AHNotificationHelper;
import com.reactnativenavigation.R;

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
		ALWAYS_SHOW,
		ALWAYS_HIDE
	}

	// Static
	private static String TAG = "AHBottomNavigation";
    private static final String EXCEPTION_INDEX_OUT_OF_BOUNDS = "The position (%d) is out of bounds of the items (%d elements)";
	private static final int MIN_ITEMS = 3;
	private static final int MAX_ITEMS = 5;

	// Listener
	private OnTabSelectedListener tabSelectedListener;
	private OnNavigationPositionListener navigationPositionListener;

	// Variables
	private Context context;
	private Resources resources;
	private ArrayList<AHBottomNavigationItem> items = new ArrayList<>();
	private ArrayList<View> views = new ArrayList<>();
	private AHBottomNavigationBehavior<AHBottomNavigation> bottomNavigationBehavior;
	private LinearLayout linearLayoutContainer;
	private View backgroundColorView;
	private Animator circleRevealAnim;
	private boolean colored = false;
	private boolean selectedBackgroundVisible = false;
	private boolean translucentNavigationEnabled;
	private List<AHNotification> notifications = AHNotification.generateEmptyList(MAX_ITEMS);
	private boolean isBehaviorTranslationSet = false;
	private int currentItem = 0;
	private int currentColor = 0;
	private boolean behaviorTranslationEnabled = true;
	private boolean needHideBottomNavigation = false;
	private boolean hideBottomNavigationWithAnimation = false;
	private boolean soundEffectsEnabled = true;

	// Variables (Styles)
	private Typeface titleTypeface;
	private int defaultBackgroundColor = Color.WHITE;
	private int defaultBackgroundResource = 0;
	private @ColorInt int itemActiveColor;
	private @ColorInt int itemInactiveColor;
	private @ColorInt int titleColorActive;
	private @ColorInt int titleColorInactive;
	private @ColorInt int coloredTitleColorActive;
	private @ColorInt int coloredTitleColorInactive;
	private float titleActiveTextSize, titleInactiveTextSize;
	private int bottomNavigationHeight, navigationBarHeight = 0;
	private float selectedItemWidth, notSelectedItemWidth;
	private boolean forceTint = false;
	private TitleState titleState = TitleState.SHOW_WHEN_ACTIVE;

	// Notifications
	private @ColorInt int notificationTextColor;
	private @ColorInt int notificationBackgroundColor;
	private Drawable notificationBackgroundDrawable;
	private Typeface notificationTypeface;
	private int notificationActiveMarginLeft, notificationInactiveMarginLeft;
	private int notificationActiveMarginTop, notificationInactiveMarginTop;

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
	 *
	 * @param context
	 */
	private void init(Context context, AttributeSet attrs) {
		this.context = context;
		resources = this.context.getResources();

		if (attrs != null) {
			TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AHBottomNavigationBehavior_Params, 0, 0);
			try {
				selectedBackgroundVisible = ta.getBoolean(R.styleable.AHBottomNavigationBehavior_Params_selectedBackgroundVisible, false);
				translucentNavigationEnabled = ta.getBoolean(R.styleable.AHBottomNavigationBehavior_Params_translucentNavigationEnabled, false);
			} finally {
				ta.recycle();
			}
		}

		notificationTextColor = ContextCompat.getColor(context, android.R.color.white);
		bottomNavigationHeight = (int) resources.getDimension(R.dimen.bottom_navigation_height);

		// Item colors
		titleColorActive = ContextCompat.getColor(context, R.color.colorBottomNavigationAccent);
		titleColorInactive = ContextCompat.getColor(context, R.color.colorBottomNavigationInactive);
		// Colors for colored bottom navigation
		coloredTitleColorActive = ContextCompat.getColor(context, R.color.colorBottomNavigationActiveColored);
		coloredTitleColorInactive = ContextCompat.getColor(context, R.color.colorBottomNavigationInactiveColored);

		itemActiveColor = titleColorActive;
		itemInactiveColor = titleColorInactive;

		// Notifications
		notificationActiveMarginLeft = (int) resources.getDimension(R.dimen.bottom_navigation_notification_margin_left_active);
		notificationInactiveMarginLeft = (int) resources.getDimension(R.dimen.bottom_navigation_notification_margin_left);
		notificationActiveMarginTop = (int) resources.getDimension(R.dimen.bottom_navigation_notification_margin_top_active);
		notificationInactiveMarginTop = (int) resources.getDimension(R.dimen.bottom_navigation_notification_margin_top);

		ViewCompat.setElevation(this, resources.getDimension(R.dimen.bottom_navigation_elevation));
		setClipToPadding(false);

		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, bottomNavigationHeight);
		setLayoutParams(params);
	}

	/**
	 * Create the items in the bottom navigation
	 */
	private void createItems() {
		if (items.size() < MIN_ITEMS) {
			Log.w(TAG, "The items list should have at least 3 items");
		} else if (items.size() > MAX_ITEMS) {
			Log.w(TAG, "The items list should not have more than 5 items");
		}

		int layoutHeight = (int) resources.getDimension(R.dimen.bottom_navigation_height);

		removeAllViews();
		views.clear();
		backgroundColorView = new View(context);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			LayoutParams backgroundLayoutParams = new LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, calculateHeight(layoutHeight));
			addView(backgroundColorView, backgroundLayoutParams);
			bottomNavigationHeight = layoutHeight;
		}

		linearLayoutContainer = new LinearLayout(context);
		linearLayoutContainer.setOrientation(LinearLayout.HORIZONTAL);
		linearLayoutContainer.setGravity(Gravity.CENTER);

		LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, layoutHeight);
		addView(linearLayoutContainer, layoutParams);

		if (titleState != TitleState.ALWAYS_HIDE &&
				(items.size() == MIN_ITEMS || titleState == TitleState.ALWAYS_SHOW)) {
			createClassicItems(linearLayoutContainer);
		} else {
			createSmallItems(linearLayoutContainer);
		}

		// Force a request layout after all the items have been created
		post(new Runnable() {
			@Override
			public void run() {
				requestLayout();
			}
		});
	}

	@SuppressLint("NewApi")
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	private int calculateHeight(int layoutHeight) {
		if(!translucentNavigationEnabled) return layoutHeight;

		int resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
		if (resourceId > 0) {
			navigationBarHeight = resources.getDimensionPixelSize(resourceId);
		}

		int[] attrs = {android.R.attr.fitsSystemWindows, android.R.attr.windowTranslucentNavigation};
		TypedArray typedValue = getContext().getTheme().obtainStyledAttributes(attrs);

		@SuppressWarnings("ResourceType")
		boolean fitWindow = typedValue.getBoolean(0, false);

		@SuppressWarnings("ResourceType")
		boolean translucentNavigation = typedValue.getBoolean(1, true);

		if(hasImmersive() /*&& !fitWindow*/ && translucentNavigation) {
			layoutHeight += navigationBarHeight;
		}

		typedValue.recycle();

		return layoutHeight;
	}

	@SuppressLint("NewApi")
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
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

	// updated

    /**
     * Check if items must be classic
     *
     * @return true if classic (icon + title)
     */
    private boolean isClassic() {
        return titleState == TitleState.ALWAYS_SHOW || items.size() <= MIN_ITEMS && titleState != TitleState.ALWAYS_SHOW;
    }

	/**
	 * Create classic items (only 3 items in the bottom navigation)
	 *
	 * @param linearLayout The layout where the items are added
	 */
	private void createClassicItems(LinearLayout linearLayout) {

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		float height = resources.getDimension(R.dimen.bottom_navigation_height);
		float minWidth = resources.getDimension(R.dimen.bottom_navigation_min_width);
		float maxWidth = resources.getDimension(R.dimen.bottom_navigation_max_width);

		if (titleState == TitleState.ALWAYS_SHOW && items.size() > MIN_ITEMS) {
			minWidth = resources.getDimension(R.dimen.bottom_navigation_small_inactive_min_width);
			maxWidth = resources.getDimension(R.dimen.bottom_navigation_small_inactive_max_width);
		}

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

		float activeSize = resources.getDimension(R.dimen.bottom_navigation_text_size_active);
		float inactiveSize = resources.getDimension(R.dimen.bottom_navigation_text_size_inactive);
		int activePaddingTop = (int) resources.getDimension(R.dimen.bottom_navigation_margin_top_active);

		if (titleActiveTextSize != 0 && titleInactiveTextSize != 0) {
			activeSize = titleActiveTextSize;
			inactiveSize = titleInactiveTextSize;
		} else if (titleState == TitleState.ALWAYS_SHOW && items.size() > MIN_ITEMS) {
			activeSize = resources.getDimension(R.dimen.bottom_navigation_text_size_forced_active);
			inactiveSize = resources.getDimension(R.dimen.bottom_navigation_text_size_forced_inactive);
		}

		for (int i = 0; i < items.size(); i++) {
			final boolean current = currentItem == i;
			final int itemIndex = i;
			AHBottomNavigationItem item = items.get(itemIndex);

			View view = inflater.inflate(R.layout.bottom_navigation_item, this, false);
			FrameLayout container = (FrameLayout) view.findViewById(R.id.bottom_navigation_container);
			ImageView icon = (ImageView) view.findViewById(R.id.bottom_navigation_item_icon);
			TextView title = (TextView) view.findViewById(R.id.bottom_navigation_item_title);
			TextView notification = (TextView) view.findViewById(R.id.bottom_navigation_notification);

			icon.setImageDrawable(item.getDrawable(context));
			title.setText(item.getTitle(context));

			if (titleTypeface != null) {
				title.setTypeface(titleTypeface);
			}

			if (titleState == TitleState.ALWAYS_SHOW && items.size() > MIN_ITEMS) {
				container.setPadding(0, container.getPaddingTop(), 0, container.getPaddingBottom());
			}

			if (current) {
				if (selectedBackgroundVisible) {
					view.setSelected(true);
				}
				icon.setSelected(true);
				// Update margins (icon & notification)
				if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
					ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) icon.getLayoutParams();
					p.setMargins(p.leftMargin, activePaddingTop, p.rightMargin, p.bottomMargin);

					ViewGroup.MarginLayoutParams paramsNotification = (ViewGroup.MarginLayoutParams)
							notification.getLayoutParams();
					paramsNotification.setMargins(notificationActiveMarginLeft, paramsNotification.topMargin,
							paramsNotification.rightMargin, paramsNotification.bottomMargin);

					view.requestLayout();
				}
			} else {
				icon.setSelected(false);
				ViewGroup.MarginLayoutParams paramsNotification = (ViewGroup.MarginLayoutParams)
						notification.getLayoutParams();
				paramsNotification.setMargins(notificationInactiveMarginLeft, paramsNotification.topMargin,
						paramsNotification.rightMargin, paramsNotification.bottomMargin);
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

			if (forceTint) {
				icon.setImageDrawable(AHHelper.getTintDrawable(items.get(i).getDrawable(context),
						current ? itemActiveColor : itemInactiveColor, forceTint));
			} else {
				if (current) {
					icon.setImageDrawable(AHHelper.getTintDrawable(items.get(i).getSelectedDrawable(context)));
				}
			}

			title.setTextColor(current ? itemActiveColor : itemInactiveColor);
			title.setTextSize(TypedValue.COMPLEX_UNIT_PX, current ? activeSize : inactiveSize);
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					updateItems(itemIndex, true);
				}
			});
			view.setSoundEffectsEnabled(soundEffectsEnabled);

			LayoutParams params = new LayoutParams((int) itemWidth, (int) height);
			linearLayout.addView(view, params);
			views.add(view);
		}

		updateNotifications(true, UPDATE_ALL_NOTIFICATIONS);
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

		int activeMarginTop = (int) resources.getDimension(R.dimen.bottom_navigation_small_margin_top_active);
		float difference = resources.getDimension(R.dimen.bottom_navigation_small_selected_width_difference);

		selectedItemWidth = itemWidth + items.size() * difference;
		itemWidth -= difference;
		notSelectedItemWidth = itemWidth;


		for (int i = 0; i < items.size(); i++) {
			final boolean current = currentItem == i;
			final int itemIndex = i;
			AHBottomNavigationItem item = items.get(itemIndex);

			View view = inflater.inflate(R.layout.bottom_navigation_small_item, this, false);
			ImageView icon = (ImageView) view.findViewById(R.id.bottom_navigation_small_item_icon);
			TextView title = (TextView) view.findViewById(R.id.bottom_navigation_small_item_title);
			TextView notification = (TextView) view.findViewById(R.id.bottom_navigation_notification);
			icon.setImageDrawable(item.getDrawable(context));

			if (titleState != TitleState.ALWAYS_HIDE) {
				title.setText(item.getTitle(context));
			}

			if (titleActiveTextSize != 0) {
				title.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleActiveTextSize);
			}

			if (titleTypeface != null) {
				title.setTypeface(titleTypeface);
			}

			if (current) {
				if (selectedBackgroundVisible) {
					view.setSelected(true);
				}
				icon.setSelected(true);
				// Update margins (icon & notification)

				if (titleState != TitleState.ALWAYS_HIDE) {
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
				ViewGroup.MarginLayoutParams paramsNotification = (ViewGroup.MarginLayoutParams)
						notification.getLayoutParams();
				paramsNotification.setMargins(notificationInactiveMarginLeft, notificationInactiveMarginTop,
						paramsNotification.rightMargin, paramsNotification.bottomMargin);
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

			if (forceTint) {
				icon.setImageDrawable(AHHelper.getTintDrawable(items.get(i).getDrawable(context),
						current ? itemActiveColor : itemInactiveColor, forceTint));
			} else {
				if (current) {
					icon.setImageDrawable(AHHelper.getTintDrawable(items.get(i).getSelectedDrawable(context)));
				}
			}

			title.setTextColor(current ? itemActiveColor : itemInactiveColor);
			title.setAlpha(current ? 1 : 0);
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					updateSmallItems(itemIndex, true);
				}
			});
			view.setSoundEffectsEnabled(soundEffectsEnabled);

			int width = current ? (int) selectedItemWidth :
					(int) itemWidth;

			if (titleState == TitleState.ALWAYS_HIDE) {
				width = (int) (itemWidth * 1.16);
			}

			LayoutParams params = new LayoutParams(width, (int) height);
			linearLayout.addView(view, params);
			views.add(view);
		}

		updateNotifications(true, UPDATE_ALL_NOTIFICATIONS);
	}


	/**
	 * Update Items UI
	 *
	 * @param itemIndex   int: Selected item position
	 * @param useCallback boolean: Use or not the callback
	 */
	private void updateItems(final int itemIndex, boolean useCallback) {

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
		float activeSize = resources.getDimension(R.dimen.bottom_navigation_text_size_active);
		float inactiveSize = resources.getDimension(R.dimen.bottom_navigation_text_size_inactive);

		if (titleActiveTextSize != 0 && titleInactiveTextSize != 0) {
			activeSize = titleActiveTextSize;
			inactiveSize = titleInactiveTextSize;
		} else if (titleState == TitleState.ALWAYS_SHOW && items.size() > MIN_ITEMS) {
			activeSize = resources.getDimension(R.dimen.bottom_navigation_text_size_forced_active);
			inactiveSize = resources.getDimension(R.dimen.bottom_navigation_text_size_forced_inactive);
		}

		for (int i = 0; i < views.size(); i++) {

			final View view = views.get(i);
			if (selectedBackgroundVisible) {
				view.setSelected(i == itemIndex);
			}

			if (i == itemIndex) {

				final TextView title = (TextView) view.findViewById(R.id.bottom_navigation_item_title);
				final ImageView icon = (ImageView) view.findViewById(R.id.bottom_navigation_item_icon);
				final TextView notification = (TextView) view.findViewById(R.id.bottom_navigation_notification);

				icon.setSelected(true);
				AHHelper.updateTopMargin(icon, inactiveMarginTop, activeMarginTop);
				AHHelper.updateLeftMargin(notification, notificationInactiveMarginLeft, notificationActiveMarginLeft);
				AHHelper.updateTextColor(title, itemInactiveColor, itemActiveColor);
				AHHelper.updateTextSize(title, inactiveSize, activeSize);
				if (forceTint) {
					AHHelper.updateDrawableColor(context, items.get(itemIndex).getDrawable(context), icon,
							itemInactiveColor, itemActiveColor, forceTint);
				} else {
					AHHelper.updateDrawable(context, items.get(itemIndex).getSelectedDrawable(context), icon);
				}

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && colored) {

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
				} else if (colored) {
					AHHelper.updateViewBackgroundColor(this, currentColor,
							items.get(itemIndex).getColor(context));
				} else {
					if (defaultBackgroundResource != 0) {
						setBackgroundResource(defaultBackgroundResource);
					} else {
						setBackgroundColor(defaultBackgroundColor);
					}
					backgroundColorView.setBackgroundColor(Color.TRANSPARENT);
				}

			} else if (i == currentItem) {

				final TextView title = (TextView) view.findViewById(R.id.bottom_navigation_item_title);
				final ImageView icon = (ImageView) view.findViewById(R.id.bottom_navigation_item_icon);
				final TextView notification = (TextView) view.findViewById(R.id.bottom_navigation_notification);

				icon.setSelected(false);
				AHHelper.updateTopMargin(icon, activeMarginTop, inactiveMarginTop);
				AHHelper.updateLeftMargin(notification, notificationActiveMarginLeft, notificationInactiveMarginLeft);
				AHHelper.updateTextColor(title, itemActiveColor, itemInactiveColor);
				AHHelper.updateTextSize(title, activeSize, inactiveSize);
				if (forceTint) {
					AHHelper.updateDrawableColor(context, items.get(currentItem).getDrawable(context), icon,
							itemActiveColor, itemInactiveColor, forceTint);
				} else {
					AHHelper.updateDrawable(context, items.get(currentItem).getDrawable(context), icon);
				}
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

				final FrameLayout container = (FrameLayout) view.findViewById(R.id.bottom_navigation_small_container);
				final TextView title = (TextView) view.findViewById(R.id.bottom_navigation_small_item_title);
				final ImageView icon = (ImageView) view.findViewById(R.id.bottom_navigation_small_item_icon);
				final TextView notification = (TextView) view.findViewById(R.id.bottom_navigation_notification);

				icon.setSelected(true);

				if (titleState != TitleState.ALWAYS_HIDE) {
					AHHelper.updateTopMargin(icon, inactiveMargin, activeMarginTop);
					AHHelper.updateLeftMargin(notification, notificationInactiveMarginLeft, notificationActiveMarginLeft);
					AHHelper.updateTopMargin(notification, notificationInactiveMarginTop, notificationActiveMarginTop);
					AHHelper.updateTextColor(title, itemInactiveColor, itemActiveColor);
					AHHelper.updateWidth(container, notSelectedItemWidth, selectedItemWidth);
				}

				AHHelper.updateAlpha(title, 0, 1);
				if (forceTint) {
					AHHelper.updateDrawableColor(context, items.get(itemIndex).getDrawable(context), icon,
							itemInactiveColor, itemActiveColor, forceTint);
				} else {
					AHHelper.updateDrawable(context, items.get(itemIndex).getSelectedDrawable(context), icon);
				}

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && colored) {
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
				} else if (colored) {
					AHHelper.updateViewBackgroundColor(this, currentColor,
							items.get(itemIndex).getColor(context));
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
				final TextView title = (TextView) view.findViewById(R.id.bottom_navigation_small_item_title);
				final ImageView icon = (ImageView) view.findViewById(R.id.bottom_navigation_small_item_icon);
				final TextView notification = (TextView) view.findViewById(R.id.bottom_navigation_notification);

				icon.setSelected(false);

				if (titleState != TitleState.ALWAYS_HIDE) {
					AHHelper.updateTopMargin(icon, activeMarginTop, inactiveMargin);
					AHHelper.updateLeftMargin(notification, notificationActiveMarginLeft, notificationInactiveMarginLeft);
					AHHelper.updateTopMargin(notification, notificationActiveMarginTop, notificationInactiveMarginTop);
					AHHelper.updateTextColor(title, itemActiveColor, itemInactiveColor);
					AHHelper.updateWidth(container, selectedItemWidth, notSelectedItemWidth);
				}

				AHHelper.updateAlpha(title, 1, 0);
				if (forceTint) {
					AHHelper.updateDrawableColor(context, items.get(currentItem).getDrawable(context), icon,
							itemActiveColor, itemInactiveColor, forceTint);
				} else {
					AHHelper.updateDrawable(context, items.get(currentItem).getDrawable(context), icon);
				}
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

			if (itemPosition != UPDATE_ALL_NOTIFICATIONS && itemPosition != i) {
				continue;
			}

			final AHNotification notificationItem = notifications.get(i);
			final int currentTextColor = AHNotificationHelper.getTextColor(notificationItem, notificationTextColor);
			final int currentBackgroundColor = AHNotificationHelper.getBackgroundColor(notificationItem, notificationBackgroundColor);

			TextView notification = (TextView) views.get(i).findViewById(R.id.bottom_navigation_notification);

			String currentValue = notification.getText().toString();
			boolean animate = !currentValue.equals(String.valueOf(notificationItem.getText()));

			if (updateStyle) {
				notification.setTextColor(currentTextColor);
				if (notificationTypeface != null) {
					notification.setTypeface(notificationTypeface);
				} else {
					notification.setTypeface(null, Typeface.BOLD);
				}

				if (notificationBackgroundDrawable != null) {
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
						Drawable drawable = notificationBackgroundDrawable.getConstantState().newDrawable();
						notification.setBackground(drawable);
					} else {
						notification.setBackgroundDrawable(notificationBackgroundDrawable);
					}

				} else if (currentBackgroundColor != 0) {
					Drawable defautlDrawable = ContextCompat.getDrawable(context, R.drawable.notification_background);
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
						notification.setBackground(AHHelper.getTintDrawable(defautlDrawable,
								currentBackgroundColor, forceTint));
					} else {
						notification.setBackgroundDrawable(AHHelper.getTintDrawable(defautlDrawable,
								currentBackgroundColor, forceTint));
					}
				}
			}

			if (notificationItem.isEmpty() && notification.getText().length() > 0) {
				notification.setText("");
				if (animate) {
					notification.animate()
							.scaleX(0)
							.scaleY(0)
							.alpha(0)
							.setInterpolator(new AccelerateInterpolator())
							.setDuration(150)
							.start();
				}
			} else if (!notificationItem.isEmpty()) {
				notification.setText(String.valueOf(notificationItem.getText()));
				if (animate) {
					notification.setScaleX(0);
					notification.setScaleY(0);
					notification.animate()
							.scaleX(1)
							.scaleY(1)
							.alpha(1)
							.setInterpolator(new OvershootInterpolator())
							.setDuration(150)
							.start();
				}
			}
		}
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
		this.itemActiveColor = colored ? coloredTitleColorActive : titleColorActive;
		this.itemInactiveColor = colored ? coloredTitleColorInactive : titleColorInactive;
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

	/**
	 * Get the accent color (used when the view contains 3 items)
	 *
	 * @return The default accent color
	 */
	public int getAccentColor() {
		return itemActiveColor;
	}

	/**
	 * Set the accent color (used when the view contains 3 items)
	 *
	 * @param accentColor The new accent color
	 */
	public void setAccentColor(int accentColor) {
		this.titleColorActive = accentColor;
		this.itemActiveColor = accentColor;
		createItems();
	}

	/**
	 * Get the inactive color (used when the view contains 3 items)
	 *
	 * @return The inactive color
	 */
	public int getInactiveColor() {
		return itemInactiveColor;
	}

	/**
	 * Set the inactive color (used when the view contains 3 items)
	 *
	 * @param inactiveColor The inactive color
	 */
	public void setInactiveColor(int inactiveColor) {
		this.titleColorInactive = inactiveColor;
		this.itemInactiveColor = inactiveColor;
		createItems();
	}

	/**
	 * Set the colors used when the bottom bar uses the colored mode
	 *
	 * @param colorActive   The active color
	 * @param colorInactive The inactive color
	 */
	public void setColoredModeColors(@ColorInt int colorActive, @ColorInt int colorInactive) {
		this.coloredTitleColorActive = colorActive;
		this.coloredTitleColorInactive = colorInactive;
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
	public void setTitleTypeface(Typeface typeface) {
		this.titleTypeface = typeface;
		createItems();
	}

	/**
	 * Set title text size in pixels
	 *
	 * @param activeSize
	 * @param inactiveSize
	 */
	public void setTitleTextSize(float activeSize, float inactiveSize) {
		this.titleActiveTextSize = activeSize;
		this.titleInactiveTextSize = inactiveSize;
		createItems();
	}

	/**
	 * Set title text size in SP
	 *
	 +	 * @param activeSize in sp
	 +	 * @param inactiveSize in sp
	 */
	public void setTitleTextSizeInSp(float activeSize, float inactiveSize) {
		this.titleActiveTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, activeSize, resources.getDisplayMetrics());
		this.titleInactiveTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, inactiveSize, resources.getDisplayMetrics());
		createItems();
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

		if (titleState != TitleState.ALWAYS_HIDE &&
				(items.size() == MIN_ITEMS || titleState == TitleState.ALWAYS_SHOW)) {
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
	public void setOnTabSelectedListener(OnTabSelectedListener tabSelectedListener) {
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
	public void setOnNavigationPositionListener(OnNavigationPositionListener navigationPositionListener) {
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

	/**
	 * Set notification text color
	 *
	 * @param textColor int
	 */
	public void setNotificationTextColor(@ColorInt int textColor) {
		this.notificationTextColor = textColor;
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
		this.notificationBackgroundColor = color;
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

	/**
	 * Set the notification margin left
	 *
	 * @param activeMargin
	 * @param inactiveMargin
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
		ViewCompat.setElevation(this, useElevation ?
				resources.getDimension(R.dimen.bottom_navigation_elevation) : 0);
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
		return bottomNavigationBehavior.isHidden();
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

	////////////////
	// INTERFACES //
	////////////////

	/**
	 *
	 */
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

}
