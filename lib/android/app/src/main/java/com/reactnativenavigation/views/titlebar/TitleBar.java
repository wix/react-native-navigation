package com.reactnativenavigation.views.titlebar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.reactnativenavigation.parse.Alignment;
import com.reactnativenavigation.parse.params.Colour;
import com.reactnativenavigation.utils.StringUtils;
import com.reactnativenavigation.utils.UiUtils;
import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.viewcontrollers.TitleBarButtonController;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.ActionMenuView;
import androidx.appcompat.widget.Toolbar;

import static com.reactnativenavigation.utils.ObjectUtils.perform;
import static com.reactnativenavigation.utils.UiUtils.runOnPreDrawOnce;
import static com.reactnativenavigation.utils.ViewUtils.findChildByClass;
import static com.reactnativenavigation.utils.ViewUtils.findChildrenByClass;
import static com.reactnativenavigation.utils.ViewUtils.removeFromParent;

@SuppressLint("ViewConstructor")
public class TitleBar extends Toolbar {
    public static final int DEFAULT_LEFT_MARGIN = 16;

    private TitleBarButtonController leftButtonController;
    private View component;
    private Alignment titleAlignment;
    private Alignment subtitleAlignment;
    private Boolean isTitleChanged = false;
    private Boolean isSubtitleChanged = false;

    public MenuItem getRightButton(int index) {
        return getMenu().getItem(index);
    }

    public int getRightButtonsCount() {
        return getMenu().size();
    }

    public List<MenuItem> getRightButtons() {
        List<MenuItem> items = new ArrayList<>();
        for (int i = 0; i < getRightButtonsCount(); i++) {
            items.add(i, getRightButton(i));
        }
        return items;
    }

    public TitleBar(Context context) {
        super(context);
        getMenu();
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        enableOverflowForReactButtonViews(child);
    }

    @Override
    public void setTitle(CharSequence title) {
        clearComponent();
        super.setTitle(title);
        isTitleChanged = true;
    }

    @Override
    public void setSubtitle(CharSequence title) {
        super.setSubtitle(title);
        isSubtitleChanged = true;
    }

    public String getTitle() {
        return super.getTitle() == null ? "" : (String) super.getTitle();
    }

    public void setComponent(View component) {
        if (this.component == component) return;
        clearTitle();
        clearSubtitle();
        this.component = component;
        addView(component);
    }

    public void setBackgroundColor(Colour color) {
        if (color.hasValue()) setBackgroundColor(color.get());
    }

    public void setTitleFontSize(double size) {
        TextView titleTextView = findTitleTextView();
        if (titleTextView != null) titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (float) size);
    }

    public void setTitleTypeface(Typeface typeface) {
        TextView titleTextView = findTitleTextView();
        if (titleTextView != null) titleTextView.setTypeface(typeface);
    }

    public void setTitleAlignment(Alignment alignment) {
        titleAlignment = alignment;
    }

    public void setSubtitleTypeface(Typeface typeface) {
        TextView subtitleTextView = findSubtitleTextView();
        if (subtitleTextView != null) subtitleTextView.setTypeface(typeface);
    }

    public void setSubtitleFontSize(double size) {
        TextView subtitleTextView = findSubtitleTextView();
        if (subtitleTextView != null) subtitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (float) size);
    }

    public void setSubtitleAlignment(Alignment alignment) {
        subtitleAlignment = alignment;
    }

    public boolean containsRightButton(@Nullable MenuItem menuItem, int order) {
        return menuItem != null &&
               getMenu().findItem(menuItem.getItemId()) != null &&
               menuItem.getOrder() == order;
    }

    public void alignTextView(Alignment alignment, TextView view) {
        if (StringUtils.isEmpty(view.getText())) return;
        int direction = view.getParent().getLayoutDirection();
        boolean isRTL = direction == View.LAYOUT_DIRECTION_RTL;

        if (alignment == Alignment.Center) {
            view.setX((getWidth() - view.getWidth()) / 2f);
        } else if (leftButtonController != null) {
            view.setX(isRTL ? (getWidth() - view.getWidth()) - getContentInsetStartWithNavigation() : getContentInsetStartWithNavigation());
        } else {
            view.setX(isRTL ? (getWidth() - view.getWidth()) - UiUtils.dpToPx(getContext(), DEFAULT_LEFT_MARGIN) : UiUtils.dpToPx(getContext(), DEFAULT_LEFT_MARGIN));
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (changed || isTitleChanged) {
            TextView title = findTitleTextView();
            if (title != null) this.alignTextView(titleAlignment, title);
            isTitleChanged = false;
        }

        if (changed || isSubtitleChanged) {
            TextView subtitle = findSubtitleTextView();
            if (subtitle != null) this.alignTextView(subtitleAlignment, subtitle);
            isSubtitleChanged = false;
        }
    }

    @Override
    public void setLayoutDirection(int layoutDirection) {
        super.setLayoutDirection(layoutDirection);
        perform(findChildByClass(this, ActionMenuView.class), buttonsContainer -> buttonsContainer.setLayoutDirection(layoutDirection));
    }

    @Nullable
    public TextView findTitleTextView() {
        List<TextView> children = findChildrenByClass(this, TextView.class, textView -> textView.getText().equals(getTitle()));
        return children.isEmpty() ? null : children.get(0);
    }

    @Nullable
    public TextView findSubtitleTextView() {
        List<TextView> children = findChildrenByClass(this, TextView.class, textView -> textView.getText().equals(getSubtitle()));
        return children.isEmpty() ? null : children.get(0);
    }

    public void clear() {
        clearTitle();
        clearSubtitle();
        clearRightButtons();
        clearLeftButton();
        clearComponent();
    }

    private void clearTitle() {
        setTitle(null);
    }

    private void clearSubtitle() {
        setSubtitle(null);
    }

    private void clearComponent() {
        if (component != null) {
            removeFromParent(component);
            component = null;
        }
    }

    private void clearLeftButton() {
        setNavigationIcon(null);
        if (leftButtonController != null) {
            leftButtonController.destroy();
            leftButtonController = null;
        }
    }

    public void clearRightButtons() {
        if (getMenu().size() > 0) getMenu().clear();
    }

    public void setBackButton(TitleBarButtonController button) {
        setLeftButton(button);
    }

    public void setLeftButtons(List<TitleBarButtonController> leftButtons) {
        if (leftButtons == null) return;
        if (leftButtons.isEmpty()) {
            clearLeftButton();
            return;
        }
        if (leftButtons.size() > 1) {
            Log.w("RNN", "Use a custom TopBar to have more than one left button");
        }
        setLeftButton(leftButtons.get(0));
    }

    private void setLeftButton(TitleBarButtonController button) {
        leftButtonController = button;
        runOnPreDrawOnce(findTitleTextView(), title -> alignTextView(titleAlignment, title));
        button.applyNavigationIcon(this);
    }

    public void setHeight(int height) {
        int pixelHeight = UiUtils.dpToPx(getContext(), height);
        if (pixelHeight == getLayoutParams().height) return;
        ViewGroup.LayoutParams lp = getLayoutParams();
        lp.height = pixelHeight;
        setLayoutParams(lp);
    }

    public void setTopMargin(int topMargin) {
        int pixelTopMargin = UiUtils.dpToPx(getContext(), topMargin);
        if (getLayoutParams() instanceof MarginLayoutParams) {
            MarginLayoutParams lp = (MarginLayoutParams) getLayoutParams();
            if (lp.topMargin == pixelTopMargin) return;
            lp.topMargin = pixelTopMargin;
            setLayoutParams(lp);
        }
    }

    public void setOverflowButtonColor(int color) {
        ActionMenuView actionMenuView = ViewUtils.findChildByClass(this, ActionMenuView.class);
        if (actionMenuView != null) {
            Drawable overflowIcon = actionMenuView.getOverflowIcon();
            if (overflowIcon != null) {
                overflowIcon.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
            }
        }
    }

    private void enableOverflowForReactButtonViews(View child) {
        if (child instanceof ActionMenuView) {
            ((ViewGroup) child).setClipChildren(false);
        }
    }

    public void removeRightButton(int buttonId) {
        getMenu().removeItem(buttonId);
    }

    public boolean containsRightButton(TitleBarButtonController button) {
        return getMenu().findItem(button.getButtonIntId()) != null;
    }
}
