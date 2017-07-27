package com.reactnativenavigation.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;
import com.reactnativenavigation.params.BaseScreenParams;
import com.reactnativenavigation.params.BaseTitleBarButtonParams;
import com.reactnativenavigation.params.StyleParams;
import com.reactnativenavigation.params.TitleBarButtonParams;
import com.reactnativenavigation.params.TitleBarLeftButtonParams;
import com.reactnativenavigation.utils.ViewUtils;

import java.util.List;

public class TitleBar extends Toolbar {
    private static final int TITLE_VISIBILITY_ANIMATION_DURATION = 320;
    private LeftButton leftButton;
    private ActionMenuView actionMenuView;
    private List<TitleBarButtonParams> rightButtons;

    public TitleBar(Context context) {
        super(context);
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        if (child instanceof ActionMenuView) {
            actionMenuView = (ActionMenuView) child;
        }
    }

    public void setRightButtons(List<TitleBarButtonParams> rightButtons, String navigatorEventId) {
        this.rightButtons = rightButtons;
        Menu menu = getMenu();
        menu.clear();
        if (rightButtons == null) {
            return;
        }
        addButtonsToTitleBar(navigatorEventId, menu);
    }

    public int getRightButtonsWidth() {
        return actionMenuView.getWidth();
    }

    public void setLeftButton(TitleBarLeftButtonParams leftButtonParams,
                              LeftButtonOnClickListener leftButtonOnClickListener,
                              String navigatorEventId,
                              boolean overrideBackPressInJs) {
        if (shouldSetLeftButton(leftButtonParams)) {
            createAndSetLeftButton(leftButtonParams, leftButtonOnClickListener, navigatorEventId, overrideBackPressInJs);
        } else if (hasLeftButton()) {
            if (leftButtonParams.hasDefaultIcon() || leftButtonParams.hasCustomIcon()) {
                updateLeftButton(leftButtonParams);
            } else {
                removeLeftButton();
            }
        }
    }

    private void removeLeftButton() {
        setNavigationIcon(null);
        leftButton = null;
    }

    public void setStyle(StyleParams params) {
        setVisibility(params.titleBarHidden);
        setTitleTextColor(params);
        setTitleTextFont(params);
        setTitleTextFontSize(params);
        setSubtitleTextColor(params);
        colorOverflowButton(params);
        setBackground(params);
        centerTitle(params);
    }

    public void setVisibility(boolean titleBarHidden) {
        setVisibility(titleBarHidden ? GONE : VISIBLE);
    }

    private Activity getActivity() {
        Context context = getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity)context;
            }
            context = ((ContextWrapper)context).getBaseContext();
        }
        return null;
    }

    private void centerTitle(final StyleParams params) {
        final View titleView = getTitleView();
        if (titleView == null) {
            return;
        }
        ViewUtils.runOnPreDraw(titleView, new Runnable() {
            @Override
            public void run() {
                if (params.titleBarTitleTextCentered) {
                    titleView.setX(ViewUtils.getWindowWidth(getActivity()) / 2 - titleView.getWidth() / 2);
                }
            }
        });
    }

    private void colorOverflowButton(StyleParams params) {
        Drawable overflowIcon = actionMenuView.getOverflowIcon();
        if (shouldColorOverflowButton(params, overflowIcon)) {
            ViewUtils.tintDrawable(overflowIcon, params.titleBarButtonColor.getColor(), true);
        }
    }

    protected void setBackground(StyleParams params) {
        setTranslucent(params);
    }

    protected void setTranslucent(StyleParams params) {
        if (params.topBarTranslucent) {
            setBackground(new TranslucentDrawable());
        }
    }

    private boolean shouldColorOverflowButton(StyleParams params, Drawable overflowIcon) {
        return overflowIcon != null && params.titleBarButtonColor.hasColor();
    }

    protected void setTitleTextColor(StyleParams params) {
        if (params.titleBarTitleColor.hasColor()) {
            setTitleTextColor(params.titleBarTitleColor.getColor());
        }
    }

    protected void setTitleTextFont(StyleParams params) {
        if (!params.titleBarTitleFont.hasFont()) {
            return;
        }
        View titleView = getTitleView();
        if (titleView instanceof TextView) {
            ((TextView) titleView).setTypeface(params.titleBarTitleFont.get());
        }
    }

    protected void setTitleTextFontSize(StyleParams params) {
        if (params.titleBarTitleFontSize > 0) {
            View titleView = getTitleView();
            if (titleView instanceof TextView) {
                ((TextView) titleView).setTextSize(((float) params.titleBarTitleFontSize));
            }
        }
    }

    protected void setSubtitleTextColor(StyleParams params) {
        if (params.titleBarSubtitleColor.hasColor()) {
            setSubtitleTextColor(params.titleBarSubtitleColor.getColor());
        }
    }

    private void addButtonsToTitleBar(String navigatorEventId, Menu menu) {
        for (int i = 0; i < rightButtons.size(); i++) {
            final TitleBarButton button = ButtonFactory.create(menu, this, rightButtons.get(i), navigatorEventId);
            addButtonInReverseOrder(rightButtons, i, button);
        }
    }

    protected void addButtonInReverseOrder(List<? extends BaseTitleBarButtonParams> buttons, int i, TitleBarButton button) {
        final int index = buttons.size() - i - 1;
        button.addToMenu(index);
    }

    private boolean hasLeftButton() {
        return leftButton != null;
    }

    private void updateLeftButton(TitleBarLeftButtonParams leftButtonParams) {
        if (leftButtonParams.hasDefaultIcon()) {
            leftButton.setIconState(leftButtonParams);
            setNavigationIcon(leftButton);
        } else if (leftButtonParams.hasCustomIcon()) {
            leftButton.setCustomIcon(leftButtonParams);
            setNavigationIcon(leftButtonParams.icon);
        }
    }

    private boolean shouldSetLeftButton(TitleBarLeftButtonParams leftButtonParams) {
        return leftButton == null && leftButtonParams != null && (leftButtonParams.hasDefaultIcon() || leftButtonParams.hasCustomIcon());
    }

    private void createAndSetLeftButton(TitleBarLeftButtonParams leftButtonParams,
                                        LeftButtonOnClickListener leftButtonOnClickListener,
                                        String navigatorEventId,
                                        boolean overrideBackPressInJs) {
        leftButton = new LeftButton(getContext(), leftButtonParams, leftButtonOnClickListener, navigatorEventId,
                overrideBackPressInJs);
        setNavigationOnClickListener(leftButton);

        if (leftButtonParams.hasCustomIcon()) {
            setNavigationIcon(leftButtonParams.icon);
        } else {
            setNavigationIcon(leftButton);
        }
    }

    public void hide() {
        hide(null);
    }

    public void hide(@Nullable final Runnable onHidden) {
        animate()
                .alpha(0)
                .setDuration(200)
                .setInterpolator(new AccelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (onHidden != null) {
                            onHidden.run();
                        }
                    }
                });
    }

    public void show() {
        this.show(null);
    }

    public void show(final @Nullable Runnable onDisplayed) {
        setAlpha(0);
        animate()
                .alpha(1)
                .setDuration(200)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (onDisplayed != null) {
                            onDisplayed.run();
                        }
                    }
                });
    }

    public void showTitle() {
        animateTitle(1);
    }

    public void hideTitle() {
        animateTitle(0);
    }

    private void animateTitle(int alpha) {
        View titleView = getTitleView();
        if (titleView != null) {
            titleView.animate()
                    .alpha(alpha)
                    .setDuration(TITLE_VISIBILITY_ANIMATION_DURATION);
        }
    }

    @Nullable
    protected View getTitleView() {
        return ViewUtils.findChildByClass(this, TextView.class, new ViewUtils.Matcher<TextView>() {
            @Override
            public boolean match(TextView child) {
                return child.getText().equals(getTitle());
            }
        });
    }

    public void setButtonColor(StyleParams.Color titleBarButtonColor) {
        if (!titleBarButtonColor.hasColor()) {
            return;
        }
        updateButtonColor(titleBarButtonColor);
        setLeftButtonColor(titleBarButtonColor);
        setButtonsIconColor();
        setButtonTextColor();
    }

    private void setLeftButtonColor(StyleParams.Color titleBarButtonColor) {
        if (leftButton != null) {
            leftButton.setColor(titleBarButtonColor.getColor());
        }
    }

    private void updateButtonColor(StyleParams.Color titleBarButtonColor) {
        if (rightButtons != null) {
            for (TitleBarButtonParams rightButton : rightButtons) {
                rightButton.color = titleBarButtonColor;
            }
        }
    }

    private void setButtonTextColor() {
        final ActionMenuView buttonsContainer = ViewUtils.findChildByClass(this, ActionMenuView.class);
        if (buttonsContainer != null) {
            for (int i = 0; i < buttonsContainer.getChildCount(); i++) {
                if (buttonsContainer.getChildAt(i) instanceof TextView) {
                    ((TextView) buttonsContainer.getChildAt(i)).setTextColor(getButton(i).getColor().getColor());
                }
            }
        }
    }

    private void setButtonsIconColor() {
        final Menu menu = getMenu();
        for (int i = 0; i < menu.size(); i++) {
            if (menu.getItem(i).getIcon() != null) {
                ViewUtils.tintDrawable(menu.getItem(i).getIcon(),
                        getButton(i).getColor().getColor(),
                        getButton(i).enabled);
            }
        }
    }

    BaseTitleBarButtonParams getButton(int index) {
        return rightButtons.get(rightButtons.size() - index - 1);
    }

    public void onViewPagerScreenChanged(BaseScreenParams screenParams) {
        if (hasLeftButton()) {
            leftButton.updateNavigatorEventId(screenParams.getNavigatorEventId());
        }
    }
}
