package com.reactnativenavigation.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.view.View;
import android.graphics.drawable.Drawable;
import android.graphics.Canvas;
import android.widget.ImageView;

import java.util.List;
import java.util.Map;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.R;
import com.reactnativenavigation.BuildConfig;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.utils.CompatUtils;

import static com.reactnativenavigation.utils.ObjectUtils.perform;

@SuppressLint("ViewConstructor")
public class BottomTabs extends AHBottomNavigation {
    
    private List<Drawable> drawables;
    private Map<String, Integer> drawablesMap;
    
    public BottomTabs(Context context) {
        super(context);
        setId(CompatUtils.generateViewId());
        setContentDescription("BottomTabs");
    }

    public void setTabTestId(int index, Text testId) {
        if (!testId.hasValue() ) return;
        perform(getViewAtPosition(index), view -> {
            view.setTag(testId.get());
            if (BuildConfig.DEBUG) view.setContentDescription(testId.get());
        });
    }

    public void setBadge(int bottomTabIndex, String badge) {
        setNotification(badge, bottomTabIndex);
    }

    public void setBadgeColor(@ColorInt Integer color) {
        if (color == null) return;
        setNotificationBackgroundColor(color);
    }
    
    public void setSelectedDrawables(List<Drawable> drawables, Map<String, Integer> drawablesMap) {
        this.drawables = drawables;
        this.drawablesMap = drawablesMap;
    }
    
    public void setSelectedIcon(int bottomTabIndex) {
        String key = "tab" + bottomTabIndex;
        if (this.drawablesMap.containsKey(key)) {
            View view = this.getViewAtPosition(bottomTabIndex);
            ImageView icon = view.findViewById(R.id.bottom_navigation_item_icon);
            Drawable drawable = this.drawables.get(this.drawablesMap.get(key));
            icon.setImageDrawable(drawable);
        }
    }

    protected void onDraw(Canvas canvas){
        setSelectedIcon(getCurrentItem());
    }

    @Override
    public void setCurrentItem(@IntRange(from = 0) int position) {
        super.setCurrentItem(position);
    }

    @Override
    public void setTitleState(TitleState titleState) {
        if (getTitleState() != titleState) super.setTitleState(titleState);
    }
}