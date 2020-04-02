package com.reactnativenavigation.viewcontrollers.topbar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.MenuItem;
import android.view.View;

import com.reactnativenavigation.anim.TopBarAnimator;
import com.reactnativenavigation.parse.AnimationOptions;
import com.reactnativenavigation.viewcontrollers.TitleBarButtonController;
import com.reactnativenavigation.viewcontrollers.TitleBarReactViewController;
import com.reactnativenavigation.views.StackLayout;
import com.reactnativenavigation.views.titlebar.TitleBar;
import com.reactnativenavigation.views.topbar.TopBar;

import java.util.List;

import androidx.annotation.VisibleForTesting;
import androidx.viewpager.widget.ViewPager;

import static com.reactnativenavigation.utils.CollectionUtils.*;
import static com.reactnativenavigation.utils.ObjectUtils.perform;
import static com.reactnativenavigation.utils.ViewUtils.isVisible;


public class TopBarController {
    private TopBar topBar;
    private TitleBar titleBar;
    private TopBarAnimator animator;

    public MenuItem getRightButton(int index) {
        return titleBar.getRightButton(index);
    }

    public TopBar getView() {
        return topBar;
    }

    public int getHeight() {
        return perform(topBar, 0, View::getHeight);
    }

    public int getRightButtonsCount() {
        return topBar.getRightButtonsCount();
    }

    public Drawable getLeftButton() {
        return titleBar.getNavigationIcon();
    }

    @VisibleForTesting
    public void setAnimator(TopBarAnimator animator) {
        this.animator = animator;
    }

    public TopBarController() {
        animator = new TopBarAnimator();
    }

    public TopBar createView(Context context, StackLayout parent) {
        if (topBar == null) {
            topBar = createTopBar(context, parent);
            titleBar = topBar.getTitleBar();
            animator.bindView(topBar, parent);
        }
        return topBar;
    }

    protected TopBar createTopBar(Context context, StackLayout stackLayout) {
        return new TopBar(context);
    }

    public void initTopTabs(ViewPager viewPager) {
        topBar.initTopTabs(viewPager);
    }

    public void clearTopTabs() {
        topBar.clearTopTabs();
    }

    public void show() {
        if (isVisible(topBar) || animator.isAnimatingShow()) return;
        topBar.setVisibility(View.VISIBLE);
    }

    public void showAnimate(AnimationOptions options, int translationDy) {
        if (isVisible(topBar) || animator.isAnimatingShow()) return;
        animator.show(options, translationDy);
    }

    public void hide() {
        if (!animator.isAnimatingHide()) {
            topBar.setVisibility(View.GONE);
        }
    }

    public void hideAnimate(AnimationOptions options, float translationStart, float translationEnd) {
        hideAnimate(options, () -> {}, translationStart, translationEnd);
    }

    private void hideAnimate(AnimationOptions options, Runnable onAnimationEnd, float translationStart, float translationEnd) {
        if (!isVisible(topBar)) return;
        animator.hide(options, onAnimationEnd, translationStart, translationEnd);
    }

    public void resetViewProperties() {
        topBar.setTranslationY(0);
        topBar.setTranslationX(0);
        topBar.setAlpha(1);
        topBar.setScaleY(1);
        topBar.setScaleX(1);
        topBar.setRotationX(0);
        topBar.setRotationY(0);
        topBar.setRotation(0);
    }

    public void setTitleComponent(TitleBarReactViewController component) {
        topBar.setTitleComponent(component.getView());
    }

    public void applyRightButtons(List<TitleBarButtonController> toAdd) {
        topBar.clearRightButtons();
        forEachIndexed(toAdd, (b, i) -> b.addToMenu(titleBar, (toAdd.size() - i) * 10));
    }

    public void mergeRightButtons(List<TitleBarButtonController> toAdd, List<TitleBarButtonController> toRemove) {
        forEach(toRemove, btn -> topBar.removeRightButton(btn));
        forEachIndexed(toAdd, (b, i) -> b.addToMenu(titleBar, (toAdd.size() - i) * 10));
    }

    public void setLeftButtons(List<TitleBarButtonController> leftButtons) {
        titleBar.setLeftButtons(leftButtons);
    }
}
