package com.reactnativenavigation.views.topbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.annotation.RestrictTo;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.reactnativenavigation.anim.TopBarAnimator;
import com.reactnativenavigation.anim.TopBarCollapseBehavior;
import com.reactnativenavigation.interfaces.ScrollEventListener;
import com.reactnativenavigation.parse.Alignment;
import com.reactnativenavigation.parse.AnimationOptions;
import com.reactnativenavigation.parse.Component;
import com.reactnativenavigation.parse.params.Button;
import com.reactnativenavigation.parse.params.Color;
import com.reactnativenavigation.parse.params.Number;
import com.reactnativenavigation.utils.CompatUtils;
import com.reactnativenavigation.viewcontrollers.ReactViewCreator;
import com.reactnativenavigation.viewcontrollers.TopBarButtonController;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarBackgroundViewController;
import com.reactnativenavigation.views.StackLayout;
import com.reactnativenavigation.views.titlebar.TitleBar;
import com.reactnativenavigation.views.titlebar.TitleBarReactViewCreator;
import com.reactnativenavigation.views.toptabs.TopTabs;

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
    private TopBarBackgroundViewController topBarBackgroundViewController;

    public TopBar(final Context context, ReactViewCreator buttonCreator, TitleBarReactViewCreator titleBarReactViewCreator, TopBarBackgroundViewController topBarBackgroundViewController, TopBarButtonController.OnClickListener onClickListener, StackLayout parentView) {
        super(context);
        collapsingBehavior = new TopBarCollapseBehavior(this);
        this.topBarBackgroundViewController = topBarBackgroundViewController;
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

    public void setSubtitle(String subtitle) {
        titleBar.setSubtitle(subtitle);
    }

    public void setSubtitleColor(@ColorInt int color) {
        titleBar.setSubtitleTextColor(color);
    }

    public void setSubtitleFontFamily(Typeface fontFamily) {
        titleBar.setSubtitleTypeface(fontFamily);
    }

    public void setSubtitleFontSize(float size) {
        titleBar.setSubtitleFontSize(size);
    }

    public void setSubtitleAlignment(Alignment alignment) {
        titleBar.setSubtitleAlignment(alignment);
    }

    public void setTestId(String testId) {
        setTag(testId);
    }

    public void setTitleTextColor(@ColorInt int color) {
        titleBar.setTitleTextColor(color);
    }

    public void setTitleFontSize(float size) {
        titleBar.setTitleFontSize(size);
    }

    public void setTitleTypeface(Typeface typeface) {
        titleBar.setTitleTypeface(typeface);
    }

    public void setTitleAlignment(Alignment alignment) {
        titleBar.setTitleAlignment(alignment);
    }

    public void setTitleComponent(Component component) {
        titleBar.setComponent(component);
    }

    public void setBackgroundComponent(Component component) {
        if (component.hasValue()) {
            topBarBackgroundViewController.setComponent(component);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(MATCH_PARENT, getHeight());
            root.addView(topBarBackgroundViewController.getView(), 0, lp);
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
        if (visible() || animator.isAnimatingShow()) return;
        resetAnimationOptions();
        setVisibility(View.VISIBLE);
    }

    private boolean visible() {
        return getVisibility() == View.VISIBLE;
    }

    public void showAnimate(AnimationOptions options) {
        if (visible() || animator.isAnimatingShow()) return;
        animator.show(options);
    }

    public void hide() {
        if (!animator.isAnimatingHide()) {
            setVisibility(View.GONE);
        }
    }

    public void hideAnimate(AnimationOptions options) {
        hideAnimate(options, () -> {});
    }

    public void hideAnimate(AnimationOptions options, Runnable onAnimationEnd) {
        animator.hide(options, onAnimationEnd);
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
        topBarBackgroundViewController.destroy();
        topBarBackgroundViewController = new TopBarBackgroundViewController(topBarBackgroundViewController);
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

    @RestrictTo(RestrictTo.Scope.TESTS)
    public TextView getTitleTextView() {
        return titleBar.findTitleTextView();
    }

    public void resetAnimationOptions() {
        setTranslationY(0);
        setTranslationX(0);
        setAlpha(1);
        setScaleY(1);
        setScaleX(1);
        setRotationX(0);
        setRotationY(0);
        setRotation(0);
    }
}
