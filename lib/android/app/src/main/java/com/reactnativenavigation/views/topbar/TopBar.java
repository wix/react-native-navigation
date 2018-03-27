package com.reactnativenavigation.views.topbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.RestrictTo;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.reactnativenavigation.anim.AnimationListener;
import com.reactnativenavigation.anim.TopBarAnimator;
import com.reactnativenavigation.anim.TopBarCollapseBehavior;
import com.reactnativenavigation.interfaces.ScrollEventListener;
import com.reactnativenavigation.parse.AnimationOptions;
import com.reactnativenavigation.parse.TitleOptions;
import com.reactnativenavigation.parse.params.Button;
import com.reactnativenavigation.parse.params.Color;
import com.reactnativenavigation.parse.params.Fraction;
import com.reactnativenavigation.parse.params.Number;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.utils.CompatUtils;
import com.reactnativenavigation.viewcontrollers.ReactViewCreator;
import com.reactnativenavigation.viewcontrollers.TopBarButtonController;
import com.reactnativenavigation.views.StackLayout;
import com.reactnativenavigation.views.toptabs.TopTabs;
import com.reactnativenavigation.views.titlebar.TitleBar;
import com.reactnativenavigation.views.titlebar.TitleBarReactViewCreator;

import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

@SuppressLint("ViewConstructor")
public class TopBar extends AppBarLayout implements ScrollEventListener.ScrollAwareView {
    private TitleBar titleBar;
    private final TopBarCollapseBehavior collapsingBehavior;
    private TopBarAnimator animator;
    private TopTabs topTabs;
    private RelativeLayout root;
    private StackLayout parentView;
    private TopBarBackgroundViewCreator topBarBackgroundViewCreator;
    private TopBarBackgroundView backgroundView;

    public TopBar(final Context context, ReactViewCreator buttonCreator, TitleBarReactViewCreator titleBarReactViewCreator, TopBarBackgroundViewCreator topBarBackgroundViewCreator, TopBarButtonController.OnClickListener onClickListener, StackLayout parentView) {
        super(context);
        collapsingBehavior = new TopBarCollapseBehavior(this);
        this.topBarBackgroundViewCreator = topBarBackgroundViewCreator;
        this.parentView = parentView;
        topTabs = new TopTabs(getContext());
        animator = new TopBarAnimator(this, parentView.getStackId());
        createLayout(buttonCreator, titleBarReactViewCreator, onClickListener);
    }

    private void createLayout(ReactViewCreator buttonCreator, TitleBarReactViewCreator titleBarReactViewCreator, TopBarButtonController.OnClickListener onClickListener) {
        topTabs = new TopTabs(getContext());
        titleBar = createTitleBar(getContext(), buttonCreator, titleBarReactViewCreator, onClickListener);
        titleBar.setId(CompatUtils.generateViewId());
        root = new RelativeLayout(getContext());
        root.addView(titleBar, MATCH_PARENT, WRAP_CONTENT);
        addView(root, MATCH_PARENT, WRAP_CONTENT);
        setContentDescription("TopBar");
    }

    protected TitleBar createTitleBar(Context context, ReactViewCreator buttonCreator, TitleBarReactViewCreator reactViewCreator, TopBarButtonController.OnClickListener onClickListener) {
        return new TitleBar(context, buttonCreator, reactViewCreator, onClickListener);
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

    public void setTitleComponent(String componentName, TitleOptions.Alignment alignment) {
        titleBar.setComponent(componentName, alignment);
    }

    public void setBackgroundComponent(Text component) {
        if (component.hasValue()) {
            backgroundView = topBarBackgroundViewCreator.create((Activity) getContext(), String.valueOf(CompatUtils.generateViewId()), component.get());
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(MATCH_PARENT, getHeight());
            root.addView(backgroundView, 0, lp);
        }
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

    public void setLeftButtons(List<Button> leftButtons) {
        titleBar.setLeftButtons(leftButtons);
    }

    public void setRightButtons(List<Button> rightButtons) {
        titleBar.setRightButtons(rightButtons);
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
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        lp.addRule(RelativeLayout.BELOW, titleBar.getId());
        root.addView(topTabs, lp);
    }

    public void enableCollapse(ScrollEventListener scrollEventListener) {
        collapsingBehavior.enableCollapse(scrollEventListener);
    }

    public void disableCollapse() {
        collapsingBehavior.disableCollapse();
    }

    public void show() {
        if (visible()) return;
        setVisibility(View.VISIBLE);
    }

    private boolean visible() {
        return getVisibility() == View.VISIBLE;
    }

    public void showAnimate(AnimationOptions options) {
        if (visible()) return;
        animator.show(options);
    }

    public void hide() {
        setVisibility(View.GONE);
    }

    public void hideAnimate(AnimationOptions options) {
        hideAnimate(options, null);
    }

    public void hideAnimate(AnimationOptions options, AnimationListener listener) {
        animator.hide(options, listener);
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
        if (backgroundView != null) {
            backgroundView.destroy();
            root.removeView(backgroundView);
            backgroundView = null;
        }
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
