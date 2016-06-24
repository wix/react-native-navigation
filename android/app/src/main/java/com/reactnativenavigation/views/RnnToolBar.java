package com.reactnativenavigation.views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.reactnativenavigation.R;
import com.reactnativenavigation.activities.BaseReactActivity;
import com.reactnativenavigation.core.objects.Button;
import com.reactnativenavigation.core.objects.Screen;
import com.reactnativenavigation.utils.ContextProvider;
import com.reactnativenavigation.utils.IconUtils;
import com.reactnativenavigation.utils.ImageUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by guyc on 09/04/16.
 */
public class RnnToolBar extends Toolbar {

    private static final int ANIMATE_DURATION = 180;

    private List<Screen> mScreens;
    private AsyncTask mDrawerIconTask;
    private AsyncTask mSetupToolbarTask;
    private Drawable mBackground;
    private Drawable mDrawerIcon;
    private Screen mDrawerScreen;
    private Integer mButtonsTintColor;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayList<View> mMenuItems;

    public RnnToolBar(Context context) {
        super(context);
        init();
    }

    public RnnToolBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RnnToolBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mMenuItems = new ArrayList<>();
        mBackground = getBackground();
    }

    public void setScreens(List<Screen> screens) {
        mScreens = screens;
    }

    public void setStyle(Screen screen) {
        mButtonsTintColor = screen.navBarButtonColor;

        if (screen.toolBarColor != null) {
            setBackgroundColor(screen.toolBarColor);
        } else {
            resetBackground();
        }

        if (screen.navBarTextColor != null) {
            setTitleTextColor(screen.navBarTextColor);
        } else {
            resetTitleTextColor();
        }

        if (screen.toolBarHidden != null && screen.toolBarHidden) {
            hideToolbar();
        } else {
            showToolbar();
        }
    }

    private void resetBackground() {
        setBackground(mBackground);
    }

    private void resetTitleTextColor() {
        setTitleTextColor(ContextCompat.getColor(getContext(), android.R.color.primary_text_light));
    }

    public void handleOnCreateOptionsMenuAsync() {
        if (mScreens != null) {
            setupToolbarButtonsAsync(null, mScreens.get(0));
        }
    }

    public ActionBarDrawerToggle setupDrawer(DrawerLayout drawerLayout, Screen drawerScreen, Screen screen) {
        if (drawerLayout == null || drawerScreen == null) {
            return null;
        }

        mDrawerLayout = drawerLayout;
        mDrawerScreen = drawerScreen;
        mDrawerToggle = new ActionBarDrawerToggle(
            ContextProvider.getActivityContext(),
            mDrawerLayout,
            this,
            R.string.drawer_open,
            R.string.drawer_close
        );
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        setupDrawerIconAsync(drawerScreen.icon, screen);

        return mDrawerToggle;
    }

    public void setDrawerIcon(Drawable icon) {
        mDrawerIcon = icon;
    }

    public void showDrawer(boolean animated) {
        if (mDrawerLayout == null) {
            return;
        }

        mDrawerLayout.openDrawer(Gravity.LEFT);
    }

    public void hideDrawer(boolean animated) {
        if (mDrawerLayout == null) {
            return;
        }

        mDrawerLayout.closeDrawer(Gravity.LEFT);
    }

    public void toggleDrawer(boolean animated) {
        if (mDrawerLayout == null) {
            return;
        }

        boolean visible = mDrawerLayout.isDrawerOpen(Gravity.LEFT);
        if (visible) {
            hideDrawer(animated);
        } else {
            showDrawer(animated);
        }
    }

    public void setupDrawerIconAsync(String drawerIconSource, Screen screen) {
        if (mDrawerIconTask == null) {
            mDrawerIconTask = new SetupDrawerIconTask(this, drawerIconSource, screen).execute();
        }
    }

    public void setupToolbarButtonsAsync(Screen newScreen) {
        if (newScreen != null) {
            this.setupToolbarButtonsAsync(null, newScreen);
        }
    }

    public void setupToolbarButtonsAsync(Screen oldScreen, Screen newScreen) {
        if (mSetupToolbarTask == null) {
            mSetupToolbarTask = new SetupToolbarButtonsTask(this, oldScreen, newScreen).execute();
        }
    }

    public void showToolbar(boolean animated) {
        ActionBar actionBar = ((AppCompatActivity) getContext()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setShowHideAnimationEnabled(animated);
            // We hide the ToolBar's parent (AppBarLayout) since this animates the shadow added by AppBar as well
            ((View) getParent()).setVisibility(VISIBLE);
        }
    }

    public void hideToolbar(boolean animated) {
        ActionBar actionBar = ((AppCompatActivity) getContext()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setShowHideAnimationEnabled(animated);
            // We hide the ToolBar's parent (AppBarLayout) since this animates the shadow added by AppBar as well
            ((View) getParent()).setVisibility(GONE);
        }
    }

    private void showToolbar() {
        showToolbar(false);
    }

    private void hideToolbar() {
        hideToolbar(false);
    }

    public void setNavUpButton() {
        setNavUpButton(null);
    }

    @SuppressWarnings({"ConstantConditions"})
    public void setNavUpButton(Screen screen) {
        ActionBar actionBar = ContextProvider.getActivityContext().getSupportActionBar();
        if (actionBar == null) {
            return;
        }

        boolean isBack = screen != null;
        boolean hasDrawer = mDrawerToggle != null;

        Drawable navIcon = null;
        DrawerArrowDrawable navArrow = null;
        if (hasDrawer && mDrawerIcon == null) {
            navArrow = (DrawerArrowDrawable) this.getNavigationIcon();
        } else {
            if (isBack) {
                navArrow = new DrawerArrowDrawable(ContextProvider.getActivityContext());
            } else if (hasDrawer) {
                navIcon = mDrawerIcon;
            }
        }

        if (navArrow != null) {
            navArrow.setProgress(isBack ? 1.0f : 0.0f);
            if (mButtonsTintColor != null) {
                navArrow.setColor(mButtonsTintColor);
            }
            navIcon = navArrow;
        }

        actionBar.setHomeAsUpIndicator(navIcon);
        actionBar.setDisplayHomeAsUpEnabled(navIcon != null);
    }

    /**
     * Update the ToolBar from screen. This function sets any properties that are defined
     * in the screen.
     * @param screen The currently displayed screen
     */
    @UiThread
    public void update(@NonNull Screen screen) {
        ((AppCompatActivity) getContext()).setSupportActionBar(this);
        setTitle(screen.title);
        setStyle(screen);
    }

    public void updateAndSetButtons(Screen screen) {
        update(screen);
        setupToolbarButtonsAsync(screen);
    }

    private static class SetupDrawerIconTask extends AsyncTask<Void, Void, Drawable> {
        private final WeakReference<RnnToolBar> mToolbarWR;
        private final String mDrawerIconSource;
        private final Integer mTintColor;

        public SetupDrawerIconTask(RnnToolBar toolBar, String drawerIconSource, Screen screen) {
            mToolbarWR = new WeakReference<>(toolBar);
            mDrawerIconSource = drawerIconSource;
            mTintColor = screen.navBarButtonColor;
        }

        @Override
        protected Drawable doInBackground(Void... params) {
            Context context = ContextProvider.getActivityContext();
            if (context == null || mDrawerIconSource == null) {
                return null;
            }

            return IconUtils.getIcon(context, mDrawerIconSource);
        }

        @Override
        protected void onPostExecute(Drawable drawerIcon) {
            RnnToolBar toolBar = mToolbarWR.get();
            if (drawerIcon != null) {
                if (mTintColor != null) {
                    ImageUtils.tint(drawerIcon, mTintColor);
                }
                toolBar.setDrawerIcon(drawerIcon);
            }

            toolBar.setNavUpButton();
            mToolbarWR.clear();
        }
    }

    private static class SetupToolbarButtonsTask extends AsyncTask<Void, Void, Map<String, Drawable>> {
        private final List<Button> mOldButtons;
        private final List<Button> mNewButtons;
        private final WeakReference<RnnToolBar> mToolbarWR;
        @ColorInt private final Integer mTintColor;
        private final int mIconDimensions;

        public SetupToolbarButtonsTask(RnnToolBar toolBar, Screen oldScreen, Screen newScreen) {
            mToolbarWR = new WeakReference<>(toolBar);
            mOldButtons = oldScreen == null ? null : oldScreen.getButtons();
            mNewButtons = newScreen.getButtons();
            mTintColor = newScreen.navBarButtonColor;
            mIconDimensions = (int) (toolBar.getHeight() * 0.4f);
        }

        @Override
        protected Map<String, Drawable> doInBackground(Void... params) {
            Context context = ContextProvider.getActivityContext();
            if (context == null) {
                return null;
            }

            Map<String, Drawable> icons = new HashMap<>();
            for (Button button : mNewButtons) {
                if (button.hasIcon()) {
                    icons.put(button.id, button.getIcon(context, mIconDimensions));
                }
            }
            return icons;
        }

        @Override
        protected void onPostExecute(Map<String, Drawable> icons) {
            final Context context = ContextProvider.getActivityContext();
            if (context == null) {
                return;
            }

            Menu menu = ((BaseReactActivity) context).getMenu();
            if (menu == null) {
                RnnToolBar toolBar = mToolbarWR.get();
                if (toolBar != null) {
                    toolBar.mSetupToolbarTask = null;
                }
                mToolbarWR.clear();
                return;
            }

            // Remove prev screen buttons
            if(mOldButtons == null) {
                menu.clear();
            } else {
                for (Button btn : mOldButtons) {
                    menu.removeItem(btn.getItemId());
                }
            }

            // Add new screen buttons
            final List<String> textButtons = new ArrayList<>();
            final int size = mNewButtons.size();
            for (int i = 0; i < size; i++) {
                Button button = mNewButtons.get(i);
                MenuItem item = menu.add(Menu.NONE, button.getItemId(), size - i - 1, button.title);
                item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

                // Set button icon
                if (button.hasIcon()) {
                    Drawable icon = icons.get(button.id);
                    if (mTintColor != null) {
                        ImageUtils.tint(icon, mTintColor);
                    }
                    item.setIcon(icon);
                } else {
                    textButtons.add(button.title);
                }

                // Disable button if needed
                if (button.disabled) {
                    item.setEnabled(false);
                }
            }

            final RnnToolBar toolBar = mToolbarWR.get();
            if (toolBar != null) {
                // Tint overflow icon which appears when there's not enough space in Toolbar for icons
                if (mTintColor != null) {
                    ImageUtils.tint(toolBar.getOverflowIcon(), mTintColor);
                }

                // Tint text buttons
                if (textButtons.size() > 0 && mTintColor != null) {
                    final View decorView = ((Activity) context).getWindow().getDecorView();
                    decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            decorView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                            // Find TextViews
                            for (String text : textButtons) {
                                decorView.findViewsWithText(toolBar.mMenuItems, text, View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                            }

                            // Set text color
                            for (View button : toolBar.mMenuItems) {
                                ((TextView) button).setTextColor(mTintColor);
                            }

                            toolBar.mMenuItems.clear();
                        }
                    });
                }

                toolBar.mSetupToolbarTask = null;
                mToolbarWR.clear();
            }
        }
    }
}
