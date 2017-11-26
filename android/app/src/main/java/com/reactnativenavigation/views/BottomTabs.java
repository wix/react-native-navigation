package com.reactnativenavigation.views;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.reactnativenavigation.animation.VisibilityAnimator;
import com.reactnativenavigation.params.AppStyle;
import com.reactnativenavigation.params.ScreenParams;
import com.reactnativenavigation.params.StyleParams;
import com.reactnativenavigation.params.parsers.StyleParamsParser;
import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.views.utils.Constants;

import java.util.List;

public class BottomTabs extends AHBottomNavigation {

    private VisibilityAnimator visibilityAnimator;

    public BottomTabs(Context context) {
        super(context);
        setForceTint(true);
        setId(ViewUtils.generateViewId());
        createVisibilityAnimator();
        setStyle(AppStyle.appStyle);
        setFontFamily();
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

    public void updateTabStyle(Bundle styleParams) {
        StyleParams parsedStyleParams = new StyleParamsParser(styleParams).parse();
        this.setStyleFromScreen(parsedStyleParams);
    }

    public void setStyleFromScreen(StyleParams params) {
        this.setStyle(params);

        setVisibility(params.bottomTabsHidden, true);
    }

    public void setTabButton(ScreenParams params, Integer index) {
        if (params.tabIcon != null || params.tabLabel != null) {
            AHBottomNavigationItem item = this.getItem(index);
            boolean tabNeedsRefresh = false;

            if (params.tabIcon != null) {
                item.setDrawable(params.tabIcon);
                tabNeedsRefresh = true;
            }
            if (params.tabLabel != null) {
                item.setTitle(params.tabLabel);
                tabNeedsRefresh = true;
            }

            if (tabNeedsRefresh) {
                this.refresh();
            }
        }
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
            String title = getItem(i).getTitle(getContext());
            if (!TextUtils.isEmpty(title)) {
                return true;
            }
        }
        return false;
    }

    public void setVisibilityByInitialScreen(StyleParams styleParams) {
        setVisibility(styleParams.bottomTabsHidden, false);
    }

    public void setVisibility(boolean hidden, boolean animated) {
        if (visibilityAnimator != null) {
            visibilityAnimator.setVisible(!hidden, animated, null);
        } else {
            setVisibility(hidden);
        }
    }

    public void setCurrentItemWithoutInvokingTabSelectedListener(Integer index) {
        setCurrentItem(index, false);
    }

    private void setBackgroundColor(StyleParams.Color bottomTabsColor) {
        if (bottomTabsColor.hasColor()) {
            if (bottomTabsColor.getColor() != getDefaultBackgroundColor()) {
                setDefaultBackgroundColor(bottomTabsColor.getColor());
            }
        } else if (Color.WHITE != getDefaultBackgroundColor()){
            setDefaultBackgroundColor(Color.WHITE);
        }
    }

    private void setVisibility(boolean bottomTabsHidden) {
        setVisibility(bottomTabsHidden ? GONE : VISIBLE);
    }

    private void createVisibilityAnimator() {
        visibilityAnimator = new VisibilityAnimator(BottomTabs.this,
                VisibilityAnimator.HideDirection.Down,
                Constants.BOTTOM_TABS_HEIGHT);
    }

    private void setStyle(StyleParams params) {
        if (params.bottomTabBadgeBackgroundColor.hasColor()) {
            setNotificationBackgroundColor(AppStyle.appStyle.bottomTabBadgeBackgroundColor.getColor());
        }
        if (params.bottomTabBadgeTextColor.hasColor()) {
            setNotificationTextColor(AppStyle.appStyle.bottomTabBadgeTextColor.getColor());
        }

        if (params.bottomTabsColor.hasColor()) {
            setBackgroundColor(params.bottomTabsColor);
        }
        if (params.bottomTabsButtonColor.hasColor()) {
            if (getInactiveColor() != params.bottomTabsButtonColor.getColor()) {
                setInactiveColor(params.bottomTabsButtonColor.getColor());
            }
        }
        if (params.selectedBottomTabsButtonColor.hasColor()) {
            if (getAccentColor() != params.selectedBottomTabsButtonColor.getColor()) {
                setAccentColor(params.selectedBottomTabsButtonColor.getColor());
            }
        }
    }

    private void setFontFamily() {
        if (AppStyle.appStyle.bottomTabFontFamily.hasFont()) {
            setTitleTypeface(AppStyle.appStyle.bottomTabFontFamily.get());
        }
    }
}
