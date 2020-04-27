package com.reactnativenavigation.presentation;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.reactnativenavigation.parse.BottomTabOptions;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.params.Param;
import com.reactnativenavigation.utils.ImageLoader;
import com.reactnativenavigation.utils.ImageLoadingListenerAdapter;
import com.reactnativenavigation.utils.LateInit;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.viewcontrollers.bottomtabs.BottomTabFinder;
import com.reactnativenavigation.views.BottomTabs;

import java.util.List;

import static com.reactnativenavigation.utils.CollectionUtils.forEach;
import static com.reactnativenavigation.utils.UiUtils.dpToPx;

public class BottomTabPresenter {
    private final Context context;
    private ImageLoader imageLoader;
    private Options defaultOptions;
    private final BottomTabFinder bottomTabFinder;
    private LateInit<BottomTabs> bottomTabs = new LateInit<>();
    private final List<ViewController> tabs;
    private TextView tv;

    public BottomTabPresenter(Context context, List<ViewController> tabs, ImageLoader imageLoader, Options defaultOptions) {
        this.tabs = tabs;
        this.context = context;
        this.bottomTabFinder = new BottomTabFinder(tabs);
        this.imageLoader = imageLoader;
        this.defaultOptions = defaultOptions;
        this.tv = new TextView(context);
    }

    public void setDefaultOptions(Options defaultOptions) {
        this.defaultOptions = defaultOptions;
    }

    public void bindView(BottomTabs bottomTabs) {
        this.bottomTabs.set(bottomTabs);
    }

    public void applyOptions() {
        bottomTabs.perform(bottomTabs -> {
            for (int index = 0; index < tabs.size(); index++) {
                BottomTabOptions tab = tabs.get(index).resolveCurrentOptions(defaultOptions).bottomTabOptions;
                if (tab.fontFamily != null) bottomTabs.setItemTypeface(tab.fontFamily, index);
                if (tab.iconColor.hasValue()) bottomTabs.setIconUnselectedColor(tab.iconColor.get(), index);
                if (tab.selectedIconColor.hasValue()) bottomTabs.setIconSelectedColor(tab.selectedIconColor.get(), index);
                if (tab.selectedTextColor.hasValue()) bottomTabs.setItemTextColorSelected(tab.selectedTextColor.get());
                if (tab.textColor.hasValue()) bottomTabs.setItemTextColor(tab.textColor.get());
                if (tab.text.hasValue()) bottomTabs.setText(index, tab.text.get());
//                bottomTabs.setTitleInactiveTextSizeInSp(i, tab.fontSize.hasValue() ? Float.valueOf(tab.fontSize.get()) : null);
//                bottomTabs.setTitleActiveTextSizeInSp(i, tab.selectedFontSize.hasValue() ? Float.valueOf(tab.selectedFontSize.get()) : null);
                BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) bottomTabs.getChildAt(0);
                BottomNavigationItemView item = (BottomNavigationItemView) bottomNavigationMenuView.getChildAt(index);
                View itemTitle = item.getChildAt(1);
                if (tab.testId.hasValue()) itemTitle.setTag(tab.testId.get());
                if (shouldApplyDot(tab)) mergeDotIndicator(index, tab); else mergeBadge(index, tab);
            }
        });
    }



    public void mergeOptions(Options options) {
        bottomTabs.perform(bottomTabs -> {
            forEach(tabs, tab -> mergeChildOptions(options, tab));
        });
    }

    public void mergeChildOptions(Options options, ViewController child) {
        bottomTabs.perform(bottomTabs -> {
            int index = bottomTabFinder.findByControllerId(child.getId());
            if (index >= 0) {
                BottomTabOptions tab = options.bottomTabOptions;
                if (tab.fontFamily != null) bottomTabs.setItemTypeface(tab.fontFamily, index);
                if (canMerge(tab.selectedIconColor)) bottomTabs.setIconSelectedColor(tab.selectedIconColor.get(), index);
                if (canMerge(tab.iconColor)) bottomTabs.setIconUnselectedColor(tab.iconColor.get(), index);
                if (tab.selectedTextColor.hasValue()) bottomTabs.setItemTextColorSelected(tab.selectedTextColor.get());
                if (tab.textColor.hasValue()) bottomTabs.setItemTextColor(tab.textColor.get());
                if (tab.text.hasValue()) bottomTabs.setText(index, tab.text.get());
                if (tab.icon.hasValue()) imageLoader.loadIcon(context, tab.icon.get(), new ImageLoadingListenerAdapter() {
                    @Override
                    public void onComplete(@NonNull Drawable drawable) {
                        bottomTabs.setIcon(index, drawable);
                    }
                });
                if (tab.selectedIcon.hasValue()) imageLoader.loadIcon(context, tab.selectedIcon.get(), new ImageLoadingListenerAdapter() {
                    @Override
                    public void onComplete(@NonNull Drawable drawable) {
                        bottomTabs.setSelectedIcon(index, drawable);
                    }
                });
                BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) bottomTabs.getChildAt(0);
                BottomNavigationItemView item = (BottomNavigationItemView) bottomNavigationMenuView.getChildAt(index);
                View itemTitle = item.getChildAt(1);
                if (tab.testId.hasValue()) itemTitle.setTag(tab.testId.get());
                if (shouldApplyDot(tab)) mergeDotIndicator(index, tab); else mergeBadge(index, tab);
            }
        });
    }

    private void mergeBadge(int index, BottomTabOptions tab) {
        if (bottomTabs == null) return;
        if (!tab.badge.hasValue()) return;

        doBadgeLogic(index, tab);
    }

    private void removeNotificationIconRemnants() {
        bottomTabs.perform(bottomTabs -> {
            BottomNavigationMenuView bottomNavigationMenuView =
                    (BottomNavigationMenuView) bottomTabs.getChildAt(0);

            for (int i = 0; i < bottomNavigationMenuView.getChildCount(); i++) {
                View v = bottomNavigationMenuView.getChildAt(i);
                BottomNavigationItemView itemView = (BottomNavigationItemView) v;
                itemView.removeView(tv);
            }
        });
    }

    private void doBadgeLogic(int index, BottomTabOptions tab) {
        if (tab.badge.hasValue()) {
            bottomTabs.perform(bottomTabs -> {
                if (tab.badge.get().isEmpty()) {
                    removeNotificationIconRemnants();
                } else {
                    createNotificationIcon(index, "badge", tab);
                }
            });
        }
    }

    private void mergeDotIndicator(int index, BottomTabOptions tab) {
        createNotificationIcon(index, "dot", tab);
    }

    private void createNotificationIcon(int index, String iconType, BottomTabOptions tab) {
        if (bottomTabs == null) return;

        BottomNavigationMenuView bottomNavigationMenuView =
                (BottomNavigationMenuView) bottomTabs.get().getChildAt(0);

        bottomTabs.perform(bottomTabs -> {
            removeNotificationIconRemnants();
            tv = new TextView(context);

            View v = bottomNavigationMenuView.getChildAt(index);
            BottomNavigationItemView itemView = (BottomNavigationItemView) v;

            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.CENTER_HORIZONTAL;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tv.setElevation(dpToPx(context, 1));
            }

            lp.setMargins(dpToPx(context, 16), dpToPx(context, 6), 0 ,0);
            GradientDrawable shape =  new GradientDrawable();
            shape.setCornerRadius( dpToPx(context, 8));
            int badgeColor = Color.parseColor("#F63D2B");
            if (tab.badgeColor.hasValue()) {
                badgeColor = tab.badgeColor.get();
            }
            shape.setColor(badgeColor);
            tv.setBackground(shape);

            if (iconType.equals("badge")) {
                SpannableString spanString = new SpannableString(tab.badge.get());
                spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
                tv.setTextColor(Color.parseColor("#FFFFFF"));
                tv.setGravity(Gravity.CENTER);
                tv.setText(spanString);
                tv.setHeight(dpToPx(context, 16));

                tv.setPadding(dpToPx(context, 3),dpToPx(context, 0),dpToPx(context, 3),dpToPx(context, 0));
            } else if (iconType.equals("dot")) {
                tv.setWidth(dpToPx(context, 6));
                tv.setHeight(dpToPx(context, 6));
            }

            itemView.addView(tv, lp);


        });
    }

    private boolean shouldApplyDot(BottomTabOptions tab) {
        return tab.dotIndicator.visible.hasValue() && !tab.badge.hasValue();
    }

    private boolean canMerge(Param p) {
        return p.hasValue() && p.canApplyValue();
    }
}
