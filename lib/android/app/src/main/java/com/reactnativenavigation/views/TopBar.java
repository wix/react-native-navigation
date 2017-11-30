package com.reactnativenavigation.views;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.reactnativenavigation.viewcontrollers.toptabs.TopTabsViewPager;

public class TopBar extends AppBarLayout {
	private final Toolbar titleBar;
	private TopTabs topTabs;

    public TopBar(final Context context) {
        super(context);
        titleBar = new Toolbar(context);
        addView(titleBar);
    }

    public void setTitle(String title) {
		titleBar.setTitle(title);
	}

	public String getTitle() {
		return titleBar.getTitle() != null ? titleBar.getTitle().toString() : "";
	}

	public void setTitleTextColor(@ColorInt int color) {
		titleBar.setTitleTextColor(color);
	}

	public void setTitleFontSize(float size) {
		TextView titleTextView = getTitleTextView();
		if (titleTextView != null) {
			titleTextView.setTextSize(size);
		}
	}

	public void setTitleTypeface(Typeface typeface) {
		TextView titleTextView = getTitleTextView();
		if (titleTextView != null) {
			titleTextView.setTypeface(typeface);
		}
	}

	public void setButtons(ArrayList<Button> leftButtons, ArrayList<Button> rightButtons) {
        Menu menu = getTitleBar().getMenu();
        if(rightButtons.isEmpty()) {
            menu.clear();
        } else {
            setRightButtons(getContext(), menu, rightButtons);
        }

    }

	public TextView getTitleTextView() {
		return findTextView(titleBar);
	}

	@Override
	public void setBackgroundColor(@ColorInt int color) {
		titleBar.setBackgroundColor(color);
	}

	@Nullable
	private TextView findTextView(ViewGroup root) {
		for (int i = 0; i < root.getChildCount(); i++) {
			View view = root.getChildAt(i);
			if (view instanceof TextView) {
				return (TextView) view;
			}
			if (view instanceof ViewGroup) {
				return findTextView((ViewGroup) view);
			}
		}
		return null;
	}

	private void setRightButtons(Context context, Menu menu, ArrayList<Button> rightButtons) {
		menu.clear();

		for (int i = 0; i < rightButtons.size(); i++){
	   		Button button = rightButtons.get(i);
			TitleBarButton titleBarButton = new TitleBarButton(this.containerView, this.titleBar, button);
			titleBarButton.addToMenu(context, menu);
       }
    }

	public Toolbar getTitleBar() {
		return titleBar;
	}

    public void setupTopTabsWithViewPager(TopTabsViewPager viewPager) {
        initTopTabs();
        topTabs.setupWithViewPager(viewPager);
    }

    private void initTopTabs() {
        topTabs = new TopTabs(getContext());
        addView(topTabs);
    }
}
