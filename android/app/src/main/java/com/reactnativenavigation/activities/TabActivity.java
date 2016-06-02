package com.reactnativenavigation.activities;

import android.support.v4.view.ViewPager;
import android.view.Menu;

import com.reactnativenavigation.R;
import com.reactnativenavigation.adapters.ViewPagerAdapter;
import com.reactnativenavigation.core.RctManager;
import com.reactnativenavigation.core.objects.Screen;
import com.reactnativenavigation.views.RnnTabLayout;
import com.reactnativenavigation.views.RnnToolBar;

import java.util.ArrayList;

/**
 * Created by guyc on 02/04/16.
 */
public class TabActivity extends BaseReactActivity {
    public static final String EXTRA_SCREENS = "extraScreens";

    private RnnTabLayout mTabLayout;
    private ViewPager mViewPager;
    private ViewPagerAdapter mAdapter;

    @Override
    protected void handleOnCreate() {
        mReactInstanceManager = RctManager.getInstance().getReactInstanceManager();

        setContentView(R.layout.tab_activity);
        mToolbar = (RnnToolBar) findViewById(R.id.toolbar);
        mTabLayout = (RnnTabLayout) findViewById(R.id.tabLayout);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);

        ArrayList<Screen> screens = (ArrayList<Screen>) getIntent().getSerializableExtra(EXTRA_SCREENS);

        setupToolbar(screens);
        setupViewPager(screens);
    }

    private void setupToolbar(ArrayList<Screen> screens) {
        Screen initialScreen = screens.get(0);
        mToolbar.setScreens(screens);
        mToolbar.setTitle(initialScreen.title);
        mToolbar.setupToolbarButtonsAsync(initialScreen);
        setSupportActionBar(mToolbar);
        setNavigationStyle(initialScreen);
    }

    @Override
    public void setNavigationStyle(Screen screen) {
        super.setNavigationStyle(screen);
        mTabLayout.setStyle(screen);
    }

    private void setupViewPager(ArrayList<Screen> screens) {
        mAdapter = new ViewPagerAdapter(this, mViewPager, mToolbar, screens);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setOnTabSelectedListener(mAdapter);
        mAdapter.notifyDataSetChanged();
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
        mAdapter.push(screen);
    }

    @Override
    public Screen pop(String navigatorId) {
        super.pop(navigatorId);
        Screen screen = mAdapter.pop(navigatorId);
        setNavigationStyle(getCurrentScreen());
        return screen;
    }

    @Override
    public Screen getCurrentScreen() {
        return mAdapter.peek(getCurrentNavigatorId());
    }

    @Override
    protected String getCurrentNavigatorId() {
        return mAdapter.getNavigatorId(mViewPager.getCurrentItem());
    }

    @Override
    public int getScreenStackSize() {
        return mAdapter.getStackSizeForNavigatorId(getCurrentNavigatorId());
    }
}
