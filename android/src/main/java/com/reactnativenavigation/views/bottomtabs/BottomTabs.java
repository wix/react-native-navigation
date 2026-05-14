package com.reactnativenavigation.views.bottomtabs;

import static com.reactnativenavigation.utils.CollectionUtils.forEach;
import static com.reactnativenavigation.utils.ViewUtils.findChildByClass;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.IntRange;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.reactnativenavigation.R;
import com.reactnativenavigation.options.LayoutDirection;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ViewConstructor")
public class BottomTabs extends AHBottomNavigation {
    private boolean itemsCreationEnabled = true;
    private boolean shouldCreateItems = true;
    private List<Runnable> onItemCreationEnabled = new ArrayList<>();
    private final List<CustomBottomTabItemView> customItemViews = new ArrayList<>();

    public BottomTabs(Context context) {
        super(context);
        setId(R.id.bottomTabs);
        setDefaultBackgroundColor(Color.TRANSPARENT);
    }

    public void disableItemsCreation() {
        itemsCreationEnabled = false;
    }

    public void enableItemsCreation() {
        itemsCreationEnabled = true;
        if (shouldCreateItems) {
            shouldCreateItems = false;
            createItems();
            forEach(onItemCreationEnabled, Runnable::run);
            onItemCreationEnabled.clear();
        }
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (hasItemsAndIsMeasured(w, h, oldw, oldh)) createItems();
    }

    @Override
    protected void createItems() {
        if (itemsCreationEnabled) {
            superCreateItems();
        } else {
            shouldCreateItems = true;
        }
    }

    // TODO Find a better way to do this
    public void superCreateItems() {
        super.createItems();
    }

    @Override
    public void setCurrentItem(@IntRange(from = 0) int position) {
        setCurrentItem(position, true);
    }

    @Override
    public void setCurrentItem(@IntRange(from = 0) int position, boolean useCallback) {
        if (itemsCreationEnabled) {
            super.setCurrentItem(position, useCallback);
        } else {
            onItemCreationEnabled.add(() -> super.setCurrentItem(position, useCallback));
        }
    }

    @Override
    public void setTitleState(TitleState titleState) {
        if (getTitleState() != titleState) super.setTitleState(titleState);
    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
        if (getDefaultBackgroundColor() != color) setDefaultBackgroundColor(color);
    }

    @Override
    public void restoreBottomNavigation(boolean withAnimation) {
        super.restoreBottomNavigation(withAnimation);
        if (!withAnimation) setVisibility(View.VISIBLE);
    }

    @Override
    public void hideBottomNavigation(boolean withAnimation) {
        super.hideBottomNavigation(withAnimation);
        if (!withAnimation) setVisibility(View.GONE);
    }

    public void setText(int index, String text) {
        AHBottomNavigationItem item = getItem(index);
        if (!item.getTitle(getContext()).equals(text)) {
            item.setTitle(text);
            refresh();
        }
    }

    public void setIcon(int index, Drawable icon) {
        AHBottomNavigationItem item = getItem(index);
        if (!item.getDrawable(getContext()).equals(icon)) {
            item.setIcon(icon);
            refresh();
        }
    }

    public void setSelectedIcon(int index, Drawable icon) {
        AHBottomNavigationItem item = getItem(index);
        if (!item.getDrawable(getContext()).equals(icon)) {
            item.setSelectedIcon(icon);
            refresh();
        }
    }

    public void setLayoutDirection(LayoutDirection direction) {
        LinearLayout tabsContainer = findChildByClass(this, LinearLayout.class);
        if (tabsContainer != null) tabsContainer.setLayoutDirection(direction.get());
    }

    /**
     * Replace the visual content of every tab cell with the provided custom
     * views. The custom view is attached as a child of the AHBottomNavigation
     * cell view so taps continue to be handled by the native cell. Pass an
     * empty list to remove all overlays.
     */
    public void setCustomItemViews(List<CustomBottomTabItemView> customViews) {
        clearCustomItemViews();
        if (customViews == null || customViews.isEmpty()) return;

        customItemViews.addAll(customViews);
        attachCustomItemViews();
    }

    public void onCustomItemViewSelectionChanged(int selectedIndex) {
        for (int i = 0; i < customItemViews.size(); i++) {
            customItemViews.get(i).setItemSelected(i == selectedIndex);
        }
    }

    public CustomBottomTabItemView getCustomItemView(int index) {
        if (index < 0 || index >= customItemViews.size()) return null;
        return customItemViews.get(index);
    }

    public boolean hasCustomItemViews() {
        return !customItemViews.isEmpty();
    }

    private void clearCustomItemViews() {
        for (CustomBottomTabItemView view : customItemViews) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) parent.removeView(view);
        }
        customItemViews.clear();
    }

    private void attachCustomItemViews() {
        for (int i = 0; i < customItemViews.size(); i++) {
            View cell = getViewAtPosition(i);
            if (!(cell instanceof ViewGroup)) continue;
            CustomBottomTabItemView itemView = customItemViews.get(i);
            ViewGroup parent = (ViewGroup) itemView.getParent();
            if (parent != null && parent != cell) parent.removeView(itemView);
            if (itemView.getParent() == null) {
                ((ViewGroup) cell).addView(itemView, new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT));
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed && !customItemViews.isEmpty()) {
            attachCustomItemViews();
        }
    }

    private boolean hasItemsAndIsMeasured(int w, int h, int oldw, int oldh) {
        return w != 0 && h != 0 && (w != oldw || h != oldh) && getItemsCount() > 0;
    }
}
