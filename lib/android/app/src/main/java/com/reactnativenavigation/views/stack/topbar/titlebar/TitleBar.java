package com.reactnativenavigation.views.stack.topbar.titlebar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.reactnativenavigation.R;
import com.reactnativenavigation.options.Alignment;
import com.reactnativenavigation.options.FontOptions;
import com.reactnativenavigation.options.params.Colour;
import com.reactnativenavigation.options.parsers.TypefaceLoader;
import com.reactnativenavigation.utils.StringUtils;
import com.reactnativenavigation.utils.UiUtils;
import com.reactnativenavigation.utils.ViewUtils;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.ActionMenuView;
import androidx.appcompat.widget.Toolbar;

import static com.reactnativenavigation.utils.ObjectUtils.perform;
import static com.reactnativenavigation.utils.UiUtils.runOnMeasured;
import static com.reactnativenavigation.utils.ViewUtils.findChildByClass;
import static com.reactnativenavigation.utils.ViewUtils.findChildrenByClass;
import static com.reactnativenavigation.utils.ViewUtils.removeFromParent;

@SuppressLint("ViewConstructor")
public class TitleBar extends Toolbar {
    public static final int DEFAULT_LEFT_MARGIN = 16;

    private View component;
    private Alignment titleAlignment;
    private Alignment subtitleAlignment;
    private Boolean isTitleChanged = false;
    private Boolean isSubtitleChanged = false;

    public MenuItem getButton(int index) {
        return getMenu().getItem(index);
    }

    public int getButtonsCount() {
        return getMenu().size();
    }

    public TitleBar(Context context) {
        super(context);
        setTitleTextAppearance(context, R.style.TitleBarTitle);
        setSubtitleTextAppearance(context, R.style.TitleBarSubtitle);
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

    public void setTitleTypeface(TypefaceLoader typefaceLoader, FontOptions font) {
        TextView titleTextView = findTitleTextView();
        if (titleTextView != null) titleTextView.setTypeface(font.getTypeface(typefaceLoader, titleTextView.getTypeface()));
    }

    public void setTitleAlignment(Alignment alignment) {
        titleAlignment = alignment;
    }

    public void setSubtitleTypeface(TypefaceLoader typefaceLoader, FontOptions font) {
        TextView subtitleTextView = findSubtitleTextView();
        if (subtitleTextView != null) subtitleTextView.setTypeface(font.getTypeface(typefaceLoader, subtitleTextView.getTypeface()));
    }

    public void setSubtitleFontSize(double size) {
        TextView subtitleTextView = findSubtitleTextView();
        if (subtitleTextView != null) subtitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (float) size);
    }

    public void setSubtitleAlignment(Alignment alignment) {
        subtitleAlignment = alignment;
    }

    public boolean containsButton(@Nullable MenuItem menuItem, int order) {
        return menuItem != null &&
               getMenu().findItem(menuItem.getItemId()) != null &&
               menuItem.getOrder() == order;
    }

    public void alignTextView(Alignment alignment, TextView view) {
        if (StringUtils.isEmpty(view.getText())) return;
        if (alignment == Alignment.Center) {
            view.setX((getWidth() - view.getWidth() - getStart()) / 2f);
        }
    }

    private int getStart() {
        boolean isRTL = getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
        return isRTL ? ((View) getParent()).getWidth() - getWidth() : getLeft();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (component != null) {
            alignComponent();
        } else {
            if (changed || isTitleChanged) {
                TextView title = findTitleTextView();
                if (title != null) {
                    alignTextView(titleAlignment, title);
                }
                isTitleChanged = false;
            }

            if (changed || isSubtitleChanged) {
                TextView subtitle = findSubtitleTextView();
                if (subtitle != null) alignTextView(subtitleAlignment, subtitle);
                isSubtitleChanged = false;
            }
        }
    }

    private void alignComponent() {
        runOnMeasured(component, () -> {
            Toolbar.LayoutParams lp = (LayoutParams) component.getLayoutParams();
            if (lp.gravity == Gravity.CENTER) {
                boolean isRTL = getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
                int direction = isRTL ? -1 : 1;
                component.setX((getWidth() - component.getWidth() - direction * getStart()) / 2f);
            } else if (lp.gravity == Gravity.START) {
                boolean isRTL = getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
                int defaultLeftMarginPx = UiUtils.dpToPx(getContext(), DEFAULT_LEFT_MARGIN);
                component.setX(isRTL ? getWidth() - component.getWidth() - defaultLeftMarginPx : defaultLeftMarginPx);
            }
        });
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
        clearButtons();
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

    public void clearButtons() {
        if (getMenu().size() > 0) getMenu().clear();
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

    public void removeButton(int buttonId) {
        getMenu().removeItem(buttonId);
    }
}
