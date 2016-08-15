package com.reactnativenavigation.activities;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.facebook.react.bridge.ReadableMap;
import com.reactnativenavigation.R;
import com.reactnativenavigation.core.RctManager;
import com.reactnativenavigation.core.objects.Drawer;
import com.reactnativenavigation.core.objects.Screen;
import com.reactnativenavigation.utils.StyleHelper;
import com.reactnativenavigation.views.BottomNavigation;
import com.reactnativenavigation.views.RnnToolBar;
import com.reactnativenavigation.views.ScreenStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BottomTabActivity extends BaseReactActivity implements AHBottomNavigation.OnTabSelectedListener {
    private static final String TAG = "BottomTabActivity";
    public static final String DRAWER_PARAMS = "drawerParams";
    public static final String EXTRA_SCREENS = "extraScreens";

    private static final String TAB_STYLE_BUTTON_COLOR = "tabBarButtonColor";
    private static final String TAB_STYLE_SELECTED_COLOR = "tabBarSelectedButtonColor";
    private static final String TAB_STYLE_BAR_BG_COLOR = "tabBarBackgroundColor";
    private static final String TAB_STYLE_INACTIVE_TITLES = "tabShowInactiveTitles";

    private static int DEFAULT_TAB_BAR_BG_COLOR = 0xFFFFFFFF;
    private static int DEFAULT_TAB_BUTTON_COLOR = Color.GRAY;
    private static int DEFAULT_TAB_SELECTED_COLOR = 0xFF0000FF;
    private static boolean DEFAULT_TAB_INACTIVE_TITLES = true;

    private BottomNavigation mBottomNavigation;
    private CoordinatorLayout mContentFrame;
    private ArrayList<ScreenStack> mScreenStacks;
    private int mCurrentStackPosition = -1;

    @Override
    protected void handleOnCreate() {
        super.handleOnCreate();
        mReactInstanceManager = RctManager.getInstance().getReactInstanceManager();

        setContentView(R.layout.bottom_tab_activity);
        mToolbar = (RnnToolBar) findViewById(R.id.toolbar);
        mBottomNavigation = (BottomNavigation) findViewById(R.id.bottom_tab_bar);
        mContentFrame = (CoordinatorLayout) findViewById(R.id.contentFrame);

        final ArrayList<Screen> screens = (ArrayList<Screen>) getIntent().getSerializableExtra(EXTRA_SCREENS);
        final Drawer drawer = (Drawer) getIntent().getSerializableExtra(DRAWER_PARAMS);
        mBottomNavigation.setForceTint(true);
        setupDrawer(screens.get(0), drawer, R.id.drawerFrame, R.id.drawerLayout);
        setupTabs(getIntent().getExtras());
        setupPages(screens);

        // Setup Toolbar after it's measured since icon height is dependent on Toolbar height
        mContentFrame.post(new Runnable() {
            @Override
            public void run() {
                setupToolbar(screens);
            }
        });
    }

    private void setupPages(ArrayList<Screen> screens) {
        new SetupTabsTask(this, mToolbar, screens).execute();
    }

    private void setupToolbar(ArrayList<Screen> screens) {
        mToolbar.setScreens(screens);
        Screen initialScreen = screens.get(0);
        mToolbar.update(initialScreen);
        StyleHelper.updateStyles(mToolbar, initialScreen);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mScreenStacks != null) {
            StyleHelper.updateStyles(mToolbar, getCurrentScreen());
        }
    }

    private void setupTabs(Bundle style) {
        mBottomNavigation.setForceTitlesDisplay(style.getBoolean(TAB_STYLE_INACTIVE_TITLES, DEFAULT_TAB_INACTIVE_TITLES));
        mBottomNavigation.setForceTint(true);
        mBottomNavigation.setDefaultBackgroundColor(getColor(style, TAB_STYLE_BAR_BG_COLOR, DEFAULT_TAB_BAR_BG_COLOR));
        mBottomNavigation.setInactiveColor(getColor(style, TAB_STYLE_BUTTON_COLOR, DEFAULT_TAB_BUTTON_COLOR));
        mBottomNavigation.setAccentColor(getColor(style, TAB_STYLE_SELECTED_COLOR, DEFAULT_TAB_SELECTED_COLOR));
    }

    private static int getColor(Bundle bundle, String key, int defaultColor) {
        if (bundle.containsKey(key)) {
            return Color.parseColor(bundle.getString(key));
        } else {
            return defaultColor;
        }
    }

    @Override
    public void push(Screen screen) {
        for (ScreenStack stack : mScreenStacks) {
            if (stack.peek().navigatorId.equals(screen.navigatorId)) {
                stack.push(screen);
            }
        }
        StyleHelper.updateStyles(mToolbar, getCurrentScreen());

        if (shouldToggleTabs(screen)) {
            mBottomNavigation.toggleTabs(screen.bottomTabsHidden, false);
        }
    }

    @Override
    public Screen pop(String navigatorId) {
        for (ScreenStack stack : mScreenStacks) {
            if (stack.peek().navigatorId.equals(navigatorId)) {
                Screen popped = stack.pop();
                Screen currentScreen = getCurrentScreen();
                StyleHelper.updateStyles(mToolbar, currentScreen);

                if (shouldToggleTabs(currentScreen)) {
                    mBottomNavigation.toggleTabs(currentScreen.bottomTabsHidden, false);
                }

                return popped;
            }
        }
        return null;
    }

    @Override
    public Screen popToRoot(String navigatorId) {
        for (ScreenStack stack : mScreenStacks) {
            if (stack.peek().navigatorId.equals(navigatorId)) {
                Screen popped = stack.popToRoot();
                Screen currentScreen = getCurrentScreen();
                StyleHelper.updateStyles(mToolbar, currentScreen);

                if (shouldToggleTabs(currentScreen)) {
                    mBottomNavigation.toggleTabs(currentScreen.bottomTabsHidden, false);
                }

                return popped;
            }
        }
        return null;
    }

    @Override
    public Screen getCurrentScreen() {
        Screen currentScreen = super.getCurrentScreen();
        if (currentScreen != null) {
            return currentScreen;
        }

        return mScreenStacks != null ? mScreenStacks.get(mCurrentStackPosition).peek() : null;
    }

    @Override
    public Screen resetTo(Screen screen) {
        StyleHelper.updateStyles(mToolbar, screen);
        return mScreenStacks.get(mCurrentStackPosition).resetTo(screen);
    }

    @Override
    protected String getCurrentNavigatorId() {
        return mScreenStacks.get(mCurrentStackPosition).peek().navigatorId;
    }

    @Override
    public int getScreenStackSize() {
        return mScreenStacks.get(mCurrentStackPosition).getStackSize();
    }

    @Override
    public void onTabSelected(int position, boolean wasSelected) {
        if (wasSelected) {
            return;
        }

        // Remove current ScreenStack
        if (mCurrentStackPosition >= 0) {
            mScreenStacks.get(mCurrentStackPosition).removeFromScreen(mContentFrame);
        }

        // Add new ScreenStack
        mScreenStacks.get(position).addToScreen(mContentFrame);

        mCurrentStackPosition = position;
        StyleHelper.updateStyles(mToolbar, getCurrentScreen());
    }

    public void setTabBadge(ReadableMap params) {
        // Badge comes across as int, but if it's 0 clear the notification
        int badgeCount = params.getInt(KEY_BADGE);
        String badge = (badgeCount > 0) ? Integer.toString(badgeCount) : "";

        // Tab index is optional, so default to current tab
        int tabIndex = mBottomNavigation.getCurrentItem();
        if (params.hasKey(KEY_TAB_INDEX)) {
            tabIndex = params.getInt(KEY_TAB_INDEX);
        }

        mBottomNavigation.setNotification(badge, tabIndex);
    }

    public void switchToTab(ReadableMap params) {
        Integer tabIndex;
        if (params.hasKey(KEY_TAB_INDEX)) {
            tabIndex = params.getInt(KEY_TAB_INDEX);
        } else {
            final String navigatorId = params.getString(KEY_NAVIGATOR_ID);
            tabIndex = findNavigatorTabIndex(navigatorId);
        }
        mBottomNavigation.setCurrentItem(tabIndex);
    }

    public void toggleTabs(ReadableMap params) {
        boolean hide = params.getBoolean(KEY_HIDDEN);
        boolean animated = params.getBoolean(KEY_ANIMATED);
        mBottomNavigation.toggleTabs(hide, animated);
    }

    private boolean shouldToggleTabs(Screen newScreen) {
        return mBottomNavigation.isShown() == newScreen.bottomTabsHidden;
    }

    public void onScrollChanged(int direction) {
        mBottomNavigation.onScroll(direction);
    }

    private static class SetupTabsTask extends AsyncTask<Void, Void, Map<Screen, Drawable>> {
        private BottomTabActivity mActivity;
        private RnnToolBar mToolBar;
        private ArrayList<Screen> mScreens;

        public SetupTabsTask(BottomTabActivity context, RnnToolBar toolBar, ArrayList<Screen> screens) {
            mActivity = context;
            mToolBar = toolBar;
            mScreens = screens;
        }

        @Override
        protected Map<Screen, Drawable> doInBackground(Void... params) {
            Map<Screen, Drawable> icons = new HashMap<>();
            for (Screen screen : mScreens) {
                if (screen.icon != null) {
                    icons.put(screen, screen.getIcon(this.mActivity));
                }
            }
            return icons;
        }

        @Override
        protected void onPostExecute(Map<Screen, Drawable> icons) {
            mActivity.setTabsWithIcons(mScreens, icons);
            StyleHelper.updateStyles(mToolBar, mActivity.getCurrentScreen());
        }
    }

    protected Integer findNavigatorTabIndex(String navigatorId) {
        for (int i = 0; i < mScreenStacks.size(); i++) {
            ScreenStack stack = mScreenStacks.get(i);
            if (!stack.isEmpty() && stack.peek().navigatorId.equals(navigatorId)) {
                return i;
            }
        }

        return null;
    }

    private void setTabsWithIcons(ArrayList<Screen> screens, Map<Screen, Drawable> icons) {
        mScreenStacks = new ArrayList<>();
        for (int i = 0; i < screens.size(); i++) {
            final Screen screen = screens.get(i);
            ScreenStack stack = new ScreenStack(this);
            stack.push(screen);
            mScreenStacks.add(stack);
            AHBottomNavigationItem item = new AHBottomNavigationItem(screen.label, icons.get(screen), Color.GRAY);
            mBottomNavigation.addItem(item);
            mBottomNavigation.setOnTabSelectedListener(this);
        }
        this.onTabSelected(0, false);
    }

    @Override
    protected void removeAllReactViews() {
        for (ScreenStack screenStack : mScreenStacks) {
            screenStack.removeAllReactViews();
        }
    }
}
