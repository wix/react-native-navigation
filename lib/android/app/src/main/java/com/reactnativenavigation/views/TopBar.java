package com.reactnativenavigation.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.RestrictTo;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.reactnativenavigation.anim.TopBarAnimator;
import com.reactnativenavigation.anim.TopBarCollapseBehavior;
import com.reactnativenavigation.interfaces.ScrollEventListener;
import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.parse.params.Button;
import com.reactnativenavigation.parse.params.Color;
import com.reactnativenavigation.parse.params.Fraction;
import com.reactnativenavigation.parse.params.Number;
import com.reactnativenavigation.viewcontrollers.ReactViewCreator;
import com.reactnativenavigation.viewcontrollers.TopBarButtonController;

import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

@SuppressLint("ViewConstructor")
public class TopBar extends AppBarLayout implements ScrollEventListener.ScrollAwareView {
    private final TitleBar titleBar;
    private final TopBarCollapseBehavior collapsingBehavior;
    private TopBarAnimator animator;
    private TopTabs topTabs;
    private StackLayout parentView;

    public TopBar(final Context context, ReactViewCreator buttonCreator, TopBarButtonController.OnClickListener onClickListener, StackLayout parentView) {
        super(context);
        collapsingBehavior = new TopBarCollapseBehavior(this);
        topTabs = new TopTabs(getContext());
        animator = new TopBarAnimator(this);
        this.parentView = parentView;
        titleBar = createTitleBar(context, buttonCreator, onClickListener);
        addView(titleBar);
        setContentDescription("TopBar");
    }

    protected TitleBar createTitleBar(Context context, ReactViewCreator buttonCreator, TopBarButtonController.OnClickListener onClickListener) {
        return new TitleBar(context, buttonCreator, onClickListener);
    }

    public void setTitle(String title) {
        titleBar.setTitle(title);
    }

    public String getTitle() {
        return titleBar.getTitle();
    }

    public void setTestId(String testId) {
        setTag(testId);
    }

    public void setTitleTextColor(Color color) {
        titleBar.setTitleTextColor(color);
    }

    public void setTitleFontSize(Fraction size) {
        titleBar.setTitleFontSize(size);
    }

    public void setTitleTypeface(Typeface typeface) {
        titleBar.setTitleTypeface(typeface);
    }

    public void setTopTabFontFamily(int tabIndex, Typeface fontFamily) {
        topTabs.setFontFamily(tabIndex, fontFamily);
    }

    public void applyTopTabsColors(Color selectedTabColor, Color unselectedTabColor) {
        topTabs.applyTopTabsColors(selectedTabColor, unselectedTabColor);
    }

    public void applyTopTabsFontSize(Number fontSize) {
        topTabs.applyTopTabsFontSize(fontSize);
    }

    public void setTopTabsVisible(boolean visible) {
        topTabs.setVisibility(this, visible);
    }

    public void setButtons(List<Button> leftButtons, List<Button> rightButtons) {
        titleBar.setButtons(leftButtons, rightButtons);
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public TextView getTitleTextView() {
        return titleBar.getTitleTextView();
    }

    public void setBackgroundColor(Color color) {
        titleBar.setBackgroundColor(color);
    }

    public Toolbar getTitleBar() {
        return titleBar;
    }

    public void initTopTabs(ViewPager viewPager) {
        topTabs = new TopTabs(getContext());
        topTabs.init(viewPager);
        addView(topTabs);
    }

    public void enableCollapse(ScrollEventListener scrollEventListener) {
        collapsingBehavior.enableCollapse(scrollEventListener);
    }

    public void disableCollapse() {
        collapsingBehavior.disableCollapse();
    }

    public void show(Bool animated) {
        if (getVisibility() == View.VISIBLE) {
            return;
        }
        if (animated.isTrueOrUndefined()) {
            animator.show();
        } else if (!animator.isRunning()) {
            setVisibility(View.VISIBLE);
        }
    }

    public void hide(Bool animated) {
        if (getVisibility() == View.GONE) {
            return;
        }
        if (animated.isTrueOrUndefined()) {
            animator.hide();
        } else if (!animator.isRunning()){
            setVisibility(View.GONE);
        }
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == View.GONE) {
            this.parentView.removeView(this);
        } else if (visibility == View.VISIBLE && this.getParent() == null) {
            this.parentView.addView(this, MATCH_PARENT, WRAP_CONTENT);
        }
    }

    public void clear() {
        titleBar.clear();
    }

    public void clearTopTabs() {
        topTabs.clear(this);
    }

    @VisibleForTesting
    public TopTabs getTopTabs() {
        return topTabs;
    }

    @VisibleForTesting
    public void setAnimator(TopBarAnimator animator) {
        this.animator = animator;
    }
}
