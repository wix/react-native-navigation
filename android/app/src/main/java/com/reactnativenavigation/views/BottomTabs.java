package com.reactnativenavigation.views;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.reactnativenavigation.animation.VisibilityAnimator;
import com.reactnativenavigation.params.AppStyle;
import com.reactnativenavigation.params.ScreenParams;
import com.reactnativenavigation.params.StyleParams;
import com.reactnativenavigation.utils.ViewUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class BottomTabs extends AHBottomNavigation {

    private VisibilityAnimator visibilityAnimator;

    public BottomTabs(Context context) {
        super(context);
        setForceTint(true);
        setId(ViewUtils.generateViewId());
        createVisibilityAnimator();
        setStyle();
        setFontFamily(context);
    }

    public void addTabs(List<ScreenParams> params, OnTabSelectedListener onTabSelectedListener) {
        for (ScreenParams screenParams : params) {
            AHBottomNavigationItem item = new AHBottomNavigationItem(screenParams.tabLabel, screenParams.tabIcon,
                    Color.GRAY);
            addItem(item);
            setOnTabSelectedListener(onTabSelectedListener);
        }
        setTitlesDisplayState();
    }

    public void setStyleFromScreen(StyleParams params) {
        if (params.bottomTabsColor.hasColor()) {
            setBackgroundColor(params.bottomTabsColor);
        }
        if (params.bottomTabsButtonColor.hasColor()) {
            setInactiveColor(params.bottomTabsButtonColor.getColor());
        }
        if (params.selectedBottomTabsButtonColor.hasColor()) {
            setAccentColor(params.selectedBottomTabsButtonColor.getColor());
        }

        setVisibility(params.bottomTabsHidden, true);
    }

    private void setTitlesDisplayState() {
        if (AppStyle.appStyle.forceTitlesDisplay) {
            setTitleState(TitleState.ALWAYS_SHOW);
        } else if (hasTabsWithLabels()) {
            setTitleState(TitleState.SHOW_WHEN_ACTIVE);
        } else {
            setTitleState(TitleState.ALWAYS_HIDE);
        }
    }

    private boolean hasTabsWithLabels() {
        for (int i = 0; i < getItemsCount(); i++) {
            String title = getItem(0).getTitle(getContext());
            if (!TextUtils.isEmpty(title)) {
                return true;
            }
        }
        return false;
    }

    public void setVisibility(boolean hidden, boolean animated) {
        if (visibilityAnimator != null) {
            visibilityAnimator.setVisible(!hidden, animated);
        } else {
            setVisibility(hidden);
        }
    }

    private void setBackgroundColor(StyleParams.Color bottomTabsColor) {
        if (bottomTabsColor.hasColor()) {
            setDefaultBackgroundColor(bottomTabsColor.getColor());
        } else {
            setDefaultBackgroundColor(Color.WHITE);
        }
    }

    private void setVisibility(boolean bottomTabsHidden) {
        setVisibility(bottomTabsHidden ? GONE : VISIBLE);
    }

    private void createVisibilityAnimator() {
        ViewUtils.runOnPreDraw(this, new Runnable() {
            @Override
            public void run() {
                visibilityAnimator = new VisibilityAnimator(BottomTabs.this,
                        VisibilityAnimator.HideDirection.Down,
                        getHeight());
            }
        });
    }

    private void setStyle() {
        if (hasBadgeBackgroundColor()) {
            setNotificationBackgroundColor(AppStyle.appStyle.bottomTabBadgeBackgroundColor.getColor());
        }
        if (hasBadgeTextColor()) {
            setNotificationTextColor(AppStyle.appStyle.bottomTabBadgeTextColor.getColor());
        }
    }

    private boolean hasBadgeTextColor() {
        return AppStyle.appStyle.bottomTabBadgeTextColor != null &&
               AppStyle.appStyle.bottomTabBadgeTextColor.hasColor();
    }

    private boolean hasBadgeBackgroundColor() {
        return AppStyle.appStyle.bottomTabBadgeBackgroundColor != null &&
               AppStyle.appStyle.bottomTabBadgeBackgroundColor.hasColor();
    }

    private boolean hasBottomTabFontFamily() {
        return AppStyle.appStyle.bottomTabFontFamily != null;
    }

    private void setFontFamily(Context context) {
        if (hasBottomTabFontFamily()) {

            AssetManager assetManager = context.getAssets();
            String fontFamilyName = AppStyle.appStyle.bottomTabFontFamily;

            Typeface typeFace = null;
            try {
                boolean hasAsset = Arrays.asList(assetManager.list("fonts")).contains(fontFamilyName);
                typeFace = hasAsset ?
                        Typeface.createFromAsset(assetManager, "fonts/".concat(fontFamilyName))
                        :
                        Typeface.create(fontFamilyName, Typeface.NORMAL);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (typeFace != null) {
                setTitleTypeface(typeFace);
            }
        }
    }
}
