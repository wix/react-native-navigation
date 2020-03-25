package com.reactnativenavigation.viewcontrollers.topbar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.reactnativenavigation.anim.TopBarAnimator;
import com.reactnativenavigation.parse.AnimationOptions;
import com.reactnativenavigation.utils.CollectionUtils;
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
        return getMenu().size();
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
        if (CollectionUtils.isNullOrEmpty(toAdd)) return;
        int size = toAdd.size();
        for (int i = 0; i < size; i++) {
            TitleBarButtonController button = toAdd.get(i);
            button.addToMenu(titleBar, (size - i) * 10000);
            button.applyButtonOptions(titleBar);
        }
    }

    public void mergeRightButtons(List<TitleBarButtonController> toAdd, List<TitleBarButtonController> toRemove) {
        forEach(toRemove, btn -> getMenu().removeItem(btn.getButtonIntId()));
        forEachIndexed(toAdd, (button, i) -> {
            if (findRightButton(button) == null) addButtonToMenu(toAdd, button, i);
            button.applyButtonOptions(titleBar);
        });
    }

    private void addButtonToMenu(List<TitleBarButtonController> toAdd, TitleBarButtonController button, Integer i) {
        int order = (toAdd.size() - i) * 10000;
        if (i > 0 && i < getMenu().size()) {
            MenuItem next = getMenu().getItem(i);
            MenuItem prev = getMenu().getItem(i - 1);
            if (next != null) {
                Log.w("TitleBar", "next: " + next.getOrder());
                order = (next.getOrder() + prev.getOrder()) / 2;
            }
        } else if (i == 0) {
            MenuItem first = getMenu().getItem(getMenu().size() - 1);
            Log.e("TitleBar", "first: " + first.getOrder());
            order = first.getOrder() * 2;
        } else if (i == getMenu().size()) {
            MenuItem last = getMenu().getItem(0);
            Log.v("TitleBar", "last: " + last.getOrder());
            order = last.getOrder() / 2;
        }
        Log.i("TitleBar", "adding at index " + i + ", order: " + order + " [" + toAdd.get(i).getId() + "]");
        button.addToMenu(titleBar, order);
    }

    public void setLeftButtons(List<TitleBarButtonController> leftButtons) {
        titleBar.setLeftButtons(leftButtons);
    }

    public MenuItem findRightButton(TitleBarButtonController button) {
        return getMenu().findItem(button.getButtonIntId());
    }

    private Menu getMenu() {
        return topBar.getTitleBar().getMenu();
    }
}
