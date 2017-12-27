package com.reactnativenavigation.views;

import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.view.ViewGroup;
import android.widget.TextView;

import com.reactnativenavigation.utils.Task;
import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.views.style.Color;

class TopTabsStyleHelper {
    private TopTabs topTabs;

    TopTabsStyleHelper(TopTabs topTabs) {
        this.topTabs = topTabs;
    }

    void applyTopTabsColors(Color selected, Color unselected) {
        if (!selected.hasColor() && !unselected.hasColor()) return;

        ColorStateList originalColors = topTabs.getTabTextColors();
        int selectedTabColor = originalColors != null ? originalColors.getColorForState(topTabs.getSelectedTabColors(), -1) : -1;
        int tabTextColor = originalColors != null ? originalColors.getColorForState(topTabs.getDefaultTabColors(), -1) : -1;

        if (selected.hasColor()) {
            tabTextColor = selected.get();
        }

        if (unselected.hasColor()) {
            selectedTabColor = unselected.get();
        }

        topTabs.setTabTextColors(tabTextColor, selectedTabColor);
    }

    void setFontFamily(int tabIndex, Typeface fontFamily) {
        applyOnTabTitle(tabIndex, (title) -> title.setTypeface(fontFamily));
    }

    private void applyOnTabTitle(int tabIndex, Task<TextView> action) {
        TextView title = ViewUtils.findChildByClass(getTabView(tabIndex), TextView.class);
        if (title != null) {
            action.run(title);
        }
    }

    private ViewGroup getTabView(int tabIndex) {
        return (ViewGroup) ((ViewGroup) topTabs.getChildAt(0)).getChildAt(tabIndex);
    }
}
