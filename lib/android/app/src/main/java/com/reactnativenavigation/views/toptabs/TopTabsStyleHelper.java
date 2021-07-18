package com.reactnativenavigation.views.toptabs;

import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.view.ViewGroup;
import android.widget.TextView;

import com.reactnativenavigation.options.params.Colour;
import com.reactnativenavigation.options.params.Number;
import com.reactnativenavigation.options.params.ThemeColour;
import com.reactnativenavigation.utils.Functions.Func1;
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

    void applyTopTabsColors(ThemeColour selected, ThemeColour unselected) {
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

    private void applyOnTabTitle(int tabIndex, Func1<TextView> action) {
        TextView title = ViewUtils.findChildByClass(getTabView(tabIndex), TextView.class);
        if (title != null) {
            action.run(title);
        }
    }

    private ViewGroup getTabView(int tabIndex) {
        return (ViewGroup) ((ViewGroup) topTabs.getChildAt(0)).getChildAt(tabIndex);
    }
}
