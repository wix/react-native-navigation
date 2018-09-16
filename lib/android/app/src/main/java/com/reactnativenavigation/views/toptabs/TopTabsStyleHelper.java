package com.reactnativenavigation.views.toptabs;

import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.view.ViewGroup;
import android.widget.TextView;

import com.reactnativenavigation.parse.params.Colour;
import com.reactnativenavigation.parse.params.Number;
import com.reactnativenavigation.utils.Task;
import com.reactnativenavigation.utils.ViewUtils;

class TopTabsStyleHelper {
    private TopTabs topTabs;

    TopTabsStyleHelper(TopTabs topTabs) {
        this.topTabs = topTabs;
    }

    void applyTopTabsFontSize(Number fontSize) {
        if (!fontSize.hasValue()) return;
        for (int i = 0; i < topTabs.getTabCount(); i++) {
            applyOnTabTitle(i, (title) -> title.setTextSize(fontSize.get()));
        }
    }

    void applyTopTabsColors(Colour selected, Colour unselected) {
        ColorStateList originalColors = topTabs.getTabTextColors();
        int selectedTabColor = originalColors != null ? originalColors.getColorForState(topTabs.getSelectedTabColors(), -1) : -1;
        int tabTextColor = originalColors != null ? originalColors.getColorForState(topTabs.getDefaultTabColors(), -1) : -1;

        if (selected.hasValue()) {
            selectedTabColor = selected.get();
        }

        if (unselected.hasValue()) {
            tabTextColor = unselected.get();
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
