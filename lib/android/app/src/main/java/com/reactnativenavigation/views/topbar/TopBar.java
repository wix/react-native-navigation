package com.reactnativenavigation.views.topbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.reactnativenavigation.BuildConfig;
import com.reactnativenavigation.R;
import com.reactnativenavigation.anim.TopBarAnimator;
import com.reactnativenavigation.anim.TopBarCollapseBehavior;
import com.reactnativenavigation.interfaces.ScrollEventListener;
import com.reactnativenavigation.parse.Alignment;
import com.reactnativenavigation.parse.AnimationOptions;
import com.reactnativenavigation.parse.BackButton;
import com.reactnativenavigation.parse.Component;
import com.reactnativenavigation.parse.params.Button;
import com.reactnativenavigation.parse.params.Colour;
import com.reactnativenavigation.parse.params.Number;
import com.reactnativenavigation.utils.CompatUtils;
import com.reactnativenavigation.utils.ImageLoader;
import com.reactnativenavigation.utils.UiUtils;
import com.reactnativenavigation.viewcontrollers.ReactViewCreator;
import com.reactnativenavigation.viewcontrollers.TopBarButtonController;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarBackgroundViewController;
import com.reactnativenavigation.views.StackLayout;
import com.reactnativenavigation.views.titlebar.TitleBar;
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
    private FrameLayout root;
    private TopBarBackgroundViewController topBarBackgroundViewController;
    private View border;
    private ImageLoader imageLoader;

    public TopBar(final Context context, ReactViewCreator buttonCreator, TopBarBackgroundViewController topBarBackgroundViewController, TopBarButtonController.OnClickListener onClickListener, StackLayout parentView, ImageLoader imageLoader) {
        super(context);
        context.setTheme(R.style.TopBar);
        this.imageLoader = imageLoader;
        collapsingBehavior = new TopBarCollapseBehavior(this);
        this.topBarBackgroundViewController = topBarBackgroundViewController;
        topTabs = new TopTabs(getContext());
        animator = new TopBarAnimator(this, parentView.getStackId());
        createLayout(buttonCreator, onClickListener);
    }

    private void createLayout(ReactViewCreator buttonCreator, TopBarButtonController.OnClickListener onClickListener) {
        setId(CompatUtils.generateViewId());
        titleBar = createTitleBar(getContext(), buttonCreator, onClickListener, imageLoader);
        topTabs = createTopTabs();
        border = createBorder();
        LinearLayout content = createContentLayout();

        root = new FrameLayout(getContext());
        root.setId(CompatUtils.generateViewId());
        content.addView(titleBar);
        content.addView(topTabs);
        root.addView(content);
        root.addView(border);
        addView(root, MATCH_PARENT, WRAP_CONTENT);
        if (BuildConfig.DEBUG) setContentDescription("TopBar");
    }

    private LinearLayout createContentLayout() {
        LinearLayout content = new LinearLayout(getContext());
        content.setOrientation(VERTICAL);
        return content;
    }

    @NonNull
    private TopTabs createTopTabs() {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        lp.addRule(RelativeLayout.BELOW, titleBar.getId());
        TopTabs topTabs = new TopTabs(getContext());
        topTabs.setLayoutParams(lp);
        topTabs.setVisibility(GONE);
        return topTabs;
    }

    private View createBorder() {
        View border = new View(getContext());
        border.setBackgroundColor(android.graphics.Color.TRANSPARENT);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(MATCH_PARENT, 0);
        lp.gravity = Gravity.BOTTOM;
        border.setLayoutParams(lp);
        return border;
    }

    protected TitleBar createTitleBar(Context context, ReactViewCreator buttonCreator, TopBarButtonController.OnClickListener onClickListener, ImageLoader imageLoader) {
        TitleBar titleBar = new TitleBar(context, buttonCreator, onClickListener, imageLoader);
        titleBar.setId(CompatUtils.generateViewId());
        return titleBar;
    }

    public void setHeight(int height) {
        if (height == getLayoutParams().height) return;
        ViewGroup.LayoutParams lp = getLayoutParams();
        lp.height = (int) UiUtils.dpToPx(getContext(), height);
        setLayoutParams(lp);
    }

    public void setTitleHeight(int height) {
        titleBar.setHeight(height);
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

    public void setSubtitleFontSize(double size) {
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

    public void setTitleFontSize(double size) {
        titleBar.setTitleFontSize(size);
    }

    public void setTitleTypeface(Typeface typeface) {
        titleBar.setTitleTypeface(typeface);
    }

    public void setTitleAlignment(Alignment alignment) {
        titleBar.setTitleAlignment(alignment);
    }

    public void setTitleComponent(View component) {
        titleBar.setComponent(component);
    }

    public void setBackgroundComponent(Component component) {
        if (component.hasValue()) {
            topBarBackgroundViewController.setComponent(component);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
            root.addView(topBarBackgroundViewController.getView(), 0, lp);
        }
    }

    public void setTopTabFontFamily(int tabIndex, Typeface fontFamily) {
        topTabs.setFontFamily(tabIndex, fontFamily);
    }

    public void applyTopTabsColors(Colour selectedTabColor, Colour unselectedTabColor) {
        topTabs.applyTopTabsColors(selectedTabColor, unselectedTabColor);
    }

    public void applyTopTabsFontSize(Number fontSize) {
        topTabs.applyTopTabsFontSize(fontSize);
    }

    public void setTopTabsVisible(boolean visible) {
        topTabs.setVisibility(this, visible);
    }

    public void setTopTabsHeight(int height) {
        if (topTabs.getLayoutParams().height == height) return;
        topTabs.getLayoutParams().height = height > 0 ? (int) UiUtils.dpToPx(getContext(), height) : height;
        topTabs.setLayoutParams(topTabs.getLayoutParams());
    }

    public void setBackButton(BackButton backButton) {
        titleBar.setBackButton(backButton);
    }

    public void setLeftButtons(List<Button> leftButtons) {
        titleBar.setLeftButtons(leftButtons);
    }

    public void setRightButtons(List<Button> rightButtons) {
        titleBar.setRightButtons(rightButtons);
    }

    public void setElevation(Double elevation) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                getElevation() != elevation.floatValue()) {
            setElevation(UiUtils.dpToPx(getContext(), elevation.floatValue()));
        }
    }

    public Toolbar getTitleBar() {
        return titleBar;
    }

    public void initTopTabs(ViewPager viewPager) {
        topTabs.setVisibility(VISIBLE);
        topTabs.init(viewPager);
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

    public void setBorderHeight(double height) {
        border.getLayoutParams().height = (int) UiUtils.dpToPx(getContext(), (float) height);
    }

    public void setBorderColor(int color) {
        border.setBackgroundColor(color);
    }
}
