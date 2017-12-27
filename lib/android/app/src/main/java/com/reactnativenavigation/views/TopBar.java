package com.reactnativenavigation.views;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.facebook.react.uimanager.events.EventDispatcher;
import com.reactnativenavigation.anim.StackAnimator;
import com.reactnativenavigation.interfaces.ScrollEventListener;
import com.reactnativenavigation.viewcontrollers.toptabs.TopTabsViewPager;

public class TopBar extends AppBarLayout {

    private final Toolbar titleBar;
    private TopTabs topTabs;

    private EventDispatcher eventDispatcher;
    private ScrollEventListener scrollEventListener;

    public TopBar(Context context) {
        this(context, null);
    }

    public TopBar(final Context context, EventDispatcher eventDispatcher) {
        super(context);
        this.eventDispatcher = eventDispatcher;
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

    public void enableCollapse() {
        final DecelerateInterpolator interpolator = new DecelerateInterpolator();
        scrollEventListener = (new ScrollEventListener(new ScrollEventListener.OnVerticalScrollListener() {
            @Override
            public void onVerticalScroll(int scrollY, int oldScrollY) {
                int diff = scrollY - oldScrollY;
                float interpolation = interpolator.getInterpolation((float) diff / getMeasuredHeight());
                float nextTranslation = getTranslationY() - getMeasuredHeight() * interpolation;
                Log.i("NIGA", "translation = " + getTranslationY() + " next translation = " + nextTranslation);
                if (diff < 0) {
                    if (getVisibility() == View.GONE) {
                        setVisibility(View.VISIBLE);
                    } else if (nextTranslation <= 0) {
                        setTranslationY(nextTranslation);
                    }
                } else {
                    if (nextTranslation < -getMeasuredHeight() && getVisibility() == View.VISIBLE) {
                        setVisibility(View.GONE);
                    } else if (nextTranslation > -getMeasuredHeight()) {
                        setTranslationY(nextTranslation);
                    }
                }
            }

            @Override
            public void onDrag(boolean started) {
                if (!started) {
                    Log.i("NIGA", "end translation = " + getTranslationY());
                    if (getTranslationY() > (-getMeasuredHeight() / 2) && getTranslationY() < 0) {
                        new StackAnimator(getContext()).animateShowTopBar(TopBar.this, null, getTranslationY());
                    } else if (getTranslationY() < (-getMeasuredHeight() / 2) && getTranslationY() > -getMeasuredHeight()) {
                        new StackAnimator(getContext()).animateHideTopBar(TopBar.this, null, getTranslationY());
                    }
                }

            }
        }));
        if (eventDispatcher != null) {
            eventDispatcher.addListener(scrollEventListener);
        }
    }

    public void disableCollapse() {
        if (eventDispatcher != null) {
            eventDispatcher.removeListener(scrollEventListener);
        }
        setVisibility(View.VISIBLE);
        setTranslationY(0);
    }
}
