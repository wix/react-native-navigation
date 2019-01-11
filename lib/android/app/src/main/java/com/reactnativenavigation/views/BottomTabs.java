package com.reactnativenavigation.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.view.View;
import android.graphics.Canvas;
import android.widget.ImageView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.R;
import com.reactnativenavigation.utils.CompatUtils;

import java.util.List;

@SuppressLint("ViewConstructor")
public class BottomTabs extends AHBottomNavigation {
    private boolean itemsCreationEnabled = true;
    private boolean shouldCreateItems = true;

    private List<Drawable> selectedIconsDrawable;

    public void disableItemsCreation() {
        itemsCreationEnabled = false;
    }

    public void enableItemsCreation() {
        itemsCreationEnabled = true;
        if (shouldCreateItems) createItems();
    }

    public BottomTabs(Context context) {
        super(context);
        setId(CompatUtils.generateViewId());
        setContentDescription("BottomTabs");
    }

    @Override
    protected void createItems() {
        if (itemsCreationEnabled) {
            superCreateItems();
        } else {
            shouldCreateItems = true;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

    }

    public void superCreateItems() {
        super.createItems();
    }

    public void setBadge(int bottomTabIndex, String badge) {
        setNotification(badge, bottomTabIndex);
    }

    public void setBadgeColor(@ColorInt Integer color) {
        if (color == null) return;
        setNotificationBackgroundColor(color);
    }

    @Override
    public void setCurrentItem(@IntRange(from = 0) int position) {
        super.setCurrentItem(position);
    }

    @Override
    public void setTitleState(TitleState titleState) {
        if (getTitleState() != titleState) super.setTitleState(titleState);
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
            item.setDrawable(icon);
            refresh();
        }
    }

    public void setSelectedIcon(int bottomTabIndex) {
        View view = this.getViewAtPosition(bottomTabIndex);
        if(view != null) {
            ImageView itemIcon = view.findViewById(R.id.bottom_navigation_item_icon);
            ImageView smallItemIcon = view.findViewById(R.id.bottom_navigation_small_item_icon);
            Drawable drawable = this.selectedIconsDrawable.get(bottomTabIndex);

            if(drawable != null) {
                if(itemIcon != null) {
                    itemIcon.setImageDrawable(drawable);
                } else if( smallItemIcon != null) {
                    smallItemIcon.setImageDrawable(drawable);
                }
            }
        }
    }

    protected void onDraw(Canvas canvas){
        setSelectedIcon(getCurrentItem());
    }

    public void addSelectedIconsDrawable(List<Drawable> icons) {
        this.selectedIconsDrawable = icons;
    }
}
