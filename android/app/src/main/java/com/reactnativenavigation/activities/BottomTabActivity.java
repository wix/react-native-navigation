package com.reactnativenavigation.activities;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.widget.FrameLayout;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.facebook.react.bridge.ReadableMap;
import com.reactnativenavigation.R;
import com.reactnativenavigation.core.RctManager;
import com.reactnativenavigation.core.objects.Drawer;
import com.reactnativenavigation.core.objects.Screen;
import com.reactnativenavigation.views.RnnToolBar;
import com.reactnativenavigation.views.ScreenStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Created by guyc on 02/04/16.
 */
public class BottomTabActivity extends BaseReactActivity implements AHBottomNavigation.OnTabSelectedListener {
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

    private AHBottomNavigation mBottomNavigation;
    private FrameLayout mContentFrame;
    private ArrayList<ScreenStack> mScreenStacks;
    private ScreenStack mDrawerStack;
    private DrawerLayout mDrawerLayout;
    private int mCurrentStackPosition = 0;

    @Override
    protected void handleOnCreate() {
        mReactInstanceManager = RctManager.getInstance().getReactInstanceManager();

        setContentView(R.layout.bottom_tab_activity);
        mToolbar = (RnnToolBar) findViewById(R.id.toolbar);
        mBottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_tab_bar);
        mContentFrame = (FrameLayout) findViewById(R.id.contentFrame);

        ArrayList<Screen> screens = (ArrayList<Screen>) getIntent().getSerializableExtra(EXTRA_SCREENS);
        Drawer drawer = (Drawer) getIntent().getSerializableExtra(DRAWER_PARAMS);
        mBottomNavigation.setForceTint(true);
        setupToolbar(screens);
        setupDrawer(drawer, screens.get(0));
        setupTabs(getIntent().getExtras());
        setupPages(screens);
    }

    protected void setupDrawer(Drawer drawer, Screen screen) {
        if (drawer == null || drawer.left == null) {
            return;
        }

        mDrawerStack = new ScreenStack(this);
        FrameLayout drawerFrame = (FrameLayout) findViewById(R.id.drawerFrame);
        drawerFrame.addView(mDrawerStack);
        mDrawerStack.push(drawer.left);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerToggle = mToolbar.setupDrawer(mDrawerLayout, drawer.left, screen);
    }

    private void setupPages(ArrayList<Screen> screens) {
        new SetupTabsTask(this, screens).execute();
    }

    private void setupToolbar(ArrayList<Screen> screens) {
        mToolbar.setScreens(screens);
        Screen initialScreen = screens.get(0);
        mToolbar.update(initialScreen);
        setNavigationStyle(initialScreen);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mScreenStacks != null) {
            updateStyles();
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
        }
        else {
            return defaultColor;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean ret = super.onCreateOptionsMenu(menu);
        mToolbar.handleOnCreateOptionsMenuAsync();
        return ret;
    }

    @Override
    public void push(Screen screen) {
        super.push(screen);
        for (ScreenStack stack : mScreenStacks) {
            if (stack.peek().navigatorId.equals(screen.navigatorId)) {
                stack.push(screen);
            }
        }
        updateStyles();
    }

    @Override
    public Screen pop(String navigatorId) {
        super.pop(navigatorId);
        for (ScreenStack stack: mScreenStacks) {
            if (stack.peek().navigatorId.equals(navigatorId)) {
                Screen popped = stack.pop();
                updateStyles();
                return popped;
            }
        }
        return null;
    }

    @Override
    public Screen popToRoot(String navigatorId) {
        super.popToRoot(navigatorId);
        for (ScreenStack stack: mScreenStacks) {
            if (stack.peek().navigatorId.equals(navigatorId)) {
                Screen popped = stack.popToRoot();
                updateStyles();
                return popped;
            }
        }
        return null;
    }

    @Override
    public Screen resetTo(Screen screen) {
        super.resetTo(screen);
        Screen popped = mScreenStacks.get(mCurrentStackPosition).resetTo(screen);
        updateStyles();
        return popped;
    }

    @Override
    protected Screen getCurrentScreen() {
        return mScreenStacks.get(mCurrentStackPosition).peek();
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
        mContentFrame.removeAllViews();
        mContentFrame.addView(mScreenStacks.get(position), new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        mCurrentStackPosition = position;
        updateStyles();
    }

    @Override
    public void setNavigationTitle(ReadableMap title) {
        super.setNavigationTitle(title);

        AHBottomNavigationItem currentItem = mBottomNavigation.getItem(mBottomNavigation.getCurrentItem());
        currentItem.setTitle(title.getString(KEY_TITLE));
        mBottomNavigation.refresh();
    }

    @Override
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

    @Override
    public void switchToTab(ReadableMap params) {
        int tabIndex = params.getInt(KEY_TAB_INDEX);
        mBottomNavigation.setCurrentItem(tabIndex);
    }

    @Override
    public void toggleTabs(ReadableMap params) {
        boolean hide = params.getBoolean(KEY_HIDDEN);
        boolean animated = params.getBoolean(KEY_ANIMATED);
        if (hide) {
            mBottomNavigation.hideBottomNavigation(animated);
        } else {
            mBottomNavigation.restoreBottomNavigation(animated);
        }
    }

    private static class SetupTabsTask extends AsyncTask<Void, Void, Map<Screen, Drawable>> {
        private BottomTabActivity mActivity;
        private ArrayList<Screen> mScreens;

        public SetupTabsTask(BottomTabActivity context, ArrayList<Screen> screens) {
            mActivity = context;
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
            mActivity.updateStyles();
        }
    }

    private void setTabsWithIcons(ArrayList<Screen> screens, Map<Screen, Drawable> icons) {
        mScreenStacks = new ArrayList<>();
        for(Screen screen: screens) {
            ScreenStack stack = new ScreenStack(this);
            stack.push(screen);
            mScreenStacks.add(stack);
            AHBottomNavigationItem item = new AHBottomNavigationItem(screen.label, icons.get(screen), Color.GRAY);
            mBottomNavigation.addItem(item);
            mBottomNavigation.setOnTabSelectedListener(this);
        }
        this.onTabSelected(0, false);
    }
}
