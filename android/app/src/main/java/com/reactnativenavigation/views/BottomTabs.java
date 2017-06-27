package com.reactnativenavigation.views;

import android.animation.LayoutTransition;
import android.content.Context;
import android.graphics.Color;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.reactnativenavigation.params.AppStyle;
import com.reactnativenavigation.params.ScreenParams;
import com.reactnativenavigation.params.StyleParams;
import com.reactnativenavigation.utils.ViewUtils;

import java.util.List;

public class BottomTabs extends AHBottomNavigation {

    public BottomTabs(Context context) {
        super(context);

        setForceTint(true);
        setId(ViewUtils.generateViewId());
        setStyle();
        setFontFamily();
    }

    public void addTabs(List<ScreenParams> params, OnTabSelectedListener onTabSelectedListener) {
        for (ScreenParams screenParams : params) {
            AHBottomNavigationItem item = new AHBottomNavigationItem(screenParams.tabLabel, screenParams.tabIcon,
                    Color.GRAY);
            addItem(item);
            setOnTabSelectedListener(onTabSelectedListener);
        }
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
        if (params.bottomTabsTextFontSize > 0) {
			setTitleTextSizeInSp(params.bottomTabsTextFontSize, params.bottomTabsTextFontSize);
		}

        setVisibility(params.bottomTabsHidden, false);
    }

    public void setTabButton(ScreenParams params, Integer index) {
        if (params.tabIcon != null) {
            AHBottomNavigationItem item = this.getItem(index);
            item.setDrawable(params.tabIcon);
            refresh();
        }
    }

    public void setVisibility(final boolean hidden, boolean animated) {
        if (animated) {
			int toVisibility = hidden ? GONE : VISIBLE;
			if (getVisibility() == toVisibility) return;

			if (hidden)
			{
				TranslateAnimation animate = new TranslateAnimation(0, 0, 0, getHeight());
				animate.setDuration(300);
				animate.setFillAfter(false);
				startAnimation(animate);
				setVisibility(GONE);
			} else {
				TranslateAnimation animate = new TranslateAnimation(0, 0, getHeight(), 0);
				animate.setDuration(300);
				animate.setAnimationListener(new Animation.AnimationListener()
				{
					@Override
					public void onAnimationStart(Animation animation)
					{
					}

					@Override
					public void onAnimationEnd(Animation animation)
					{
						setVisibility(VISIBLE);
					}

					@Override
					public void onAnimationRepeat(Animation animation)
					{
					}
				});
				startAnimation(animate);
			}
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

    private void setStyle() {
		setNavigationBarHeight(AppStyle.appStyle.bottomTabsTabBarHeight);

		if (AppStyle.appStyle.bottomTabFontSize > 0) {
			setTitleTextSizeInSp(AppStyle.appStyle.bottomTabFontSize, AppStyle.appStyle.bottomTabFontSize);
		}
	}

    private void setFontFamily() {
        if (AppStyle.appStyle.bottomTabFontFamily.hasFont()) {
            setTitleTypeface(AppStyle.appStyle.bottomTabFontFamily.get());
        }
    }
}
