package com.reactnativenavigation.activities;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.widget.FrameLayout;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.reactnativenavigation.R;
import com.reactnativenavigation.core.RctManager;
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
    private int mCurrentStackPosition = 0;

    @Override
    protected void handleOnCreate() {
        mReactInstanceManager = RctManager.getInstance().getReactInstanceManager();

        setContentView(R.layout.bottom_tab_activity);
        mToolbar = (RnnToolBar) findViewById(R.id.toolbar);
        mBottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_tab_bar);
        mContentFrame = (FrameLayout) findViewById(R.id.contentFrame);

        ArrayList<Screen> screens = (ArrayList<Screen>) getIntent().getSerializableExtra(EXTRA_SCREENS);
        mBottomNavigation.setForceTint(true);
        setupToolbar(screens);
        setupTabs(getIntent().getExtras());
        setupPages(screens);
    }

    private void setupPages(ArrayList<Screen> screens) {
        new SetupTabsTask(this, screens).execute();
    }

    private void setupToolbar(ArrayList<Screen> screens) {
        Screen initialScreen = screens.get(0);
        mToolbar.setScreens(screens);
        mToolbar.setTitle(initialScreen.title);
        setSupportActionBar(mToolbar);
        setNavigationStyle(initialScreen);
    }

    @Override
    public void setNavigationStyle(Screen screen) {
        super.setNavigationStyle(screen);
        mToolbar.setTitle(screen.title);
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
        mScreenStacks.get(mCurrentStackPosition).push(screen);
    }

    @Override
    public Screen pop(String navigatorId) {
        super.pop(navigatorId);
        Screen screen = mScreenStacks.get(mCurrentStackPosition).pop();
        setNavigationStyle(getCurrentScreen());
        return screen;
    }

    @Override
    public Screen getCurrentScreen() {
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
        mToolbar.setupToolbarButtonsAsync(getCurrentScreen());
        setNavigationStyle(getCurrentScreen());
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
