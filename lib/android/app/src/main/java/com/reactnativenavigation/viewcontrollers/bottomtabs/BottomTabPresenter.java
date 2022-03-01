package com.reactnativenavigation.viewcontrollers.bottomtabs;

import static com.reactnativenavigation.utils.CollectionUtils.forEach;
import static com.reactnativenavigation.utils.UiUtils.dpToPx;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

import com.aurelhubert.ahbottomnavigation.AHTextView;
import com.aurelhubert.ahbottomnavigation.notification.AHNotification;
import com.reactnativenavigation.options.BottomTabOptions;
import com.reactnativenavigation.options.DotIndicatorOptions;
import com.reactnativenavigation.options.Options;
import com.reactnativenavigation.options.params.ThemeColour;
import com.reactnativenavigation.options.parsers.TypefaceLoader;
import com.reactnativenavigation.utils.ImageLoader;
import com.reactnativenavigation.utils.ImageLoadingListenerAdapter;
import com.reactnativenavigation.utils.LateInit;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController;
import com.reactnativenavigation.views.bottomtabs.BottomTabs;

import java.util.List;

public class BottomTabPresenter {
    private final Context context;
    private final ImageLoader imageLoader;
    private final TypefaceLoader typefaceLoader;
    private final Typeface defaultTypeface;
    private Options defaultOptions;
    private final BottomTabFinder bottomTabFinder;
    private final LateInit<BottomTabs> bottomTabs = new LateInit<>();
    private final List<ViewController<?>> tabs;
    private final int defaultDotIndicatorSize;

    public BottomTabPresenter(Context context, List<ViewController<?>> tabs, ImageLoader imageLoader,  TypefaceLoader typefaceLoader, Options defaultOptions) {
        this.tabs = tabs;
        this.context = context;
        this.bottomTabFinder = new BottomTabFinder(tabs);
        this.imageLoader = imageLoader;
        this.typefaceLoader = typefaceLoader;
        this.defaultTypeface = typefaceLoader.getDefaultTypeFace();
        this.defaultOptions = defaultOptions;
        defaultDotIndicatorSize = dpToPx(context, 6);
    }

    public void setDefaultOptions(Options defaultOptions) {
        this.defaultOptions = defaultOptions;
    }

    public void bindView(BottomTabs bottomTabs) {
        this.bottomTabs.set(bottomTabs);
    }

    public void applyOptions() {
        bottomTabs.perform(bottomTabs -> {
            for (int i = 0; i < tabs.size(); i++) {
                BottomTabOptions tab = tabs.get(i).resolveCurrentOptions(defaultOptions).bottomTabOptions;
                bottomTabs.setIconWidth(i, tab.iconWidth.get(null));
                bottomTabs.setIconHeight(i, tab.iconHeight.get(null));
                bottomTabs.setTitleTypeface(i, tab.font.getTypeface(typefaceLoader, defaultTypeface));
                if (tab.selectedIconColor.canApplyValue()) bottomTabs.setIconActiveColor(i, tab.selectedIconColor.get(null));
                if (tab.iconColor.canApplyValue()) bottomTabs.setIconInactiveColor(i, tab.iconColor.get(null));
                bottomTabs.setTitleActiveColor(i, tab.selectedTextColor.get(null));
                bottomTabs.setTitleInactiveColor(i, tab.textColor.get(null));
                if (tab.fontSize.hasValue()) bottomTabs.setTitleInactiveTextSizeInSp(i, Float.valueOf(tab.fontSize.get()));
                if (tab.selectedFontSize.hasValue()) bottomTabs.setTitleActiveTextSizeInSp(i, Float.valueOf(tab.selectedFontSize.get()));
                if (tab.testId.hasValue()) bottomTabs.setTag(i, tab.testId.get());
                if (shouldApplyDot(tab)) applyDotIndicator(i, tab.dotIndicator);
                if (tab.badge.hasValue()) applyBadge(i, tab);
            }
        });
    }

    public void mergeOptions(Options options) {
        bottomTabs.perform(bottomTabs -> {
            bottomTabs.disableItemsCreation();
            forEach(tabs, tab -> mergeChildOptions(options, tab));
            bottomTabs.enableItemsCreation();
        });
    }

    public void mergeChildOptions(Options options, ViewController<?> child) {
        bottomTabs.perform(bottomTabs -> {
            int index = bottomTabFinder.findByControllerId(child.getId());
            if (index >= 0) {
                BottomTabOptions tab = options.bottomTabOptions;
                if (tab.iconWidth.hasValue()) bottomTabs.setIconWidth(index, tab.iconWidth.get(null));
                if (tab.iconHeight.hasValue()) bottomTabs.setIconHeight(index, tab.iconHeight.get(null));
                if (tab.font.hasValue()) bottomTabs.setTitleTypeface(index, tab.font.getTypeface(typefaceLoader, defaultTypeface));
                if (canMergeColor(tab.selectedIconColor)) bottomTabs.setIconActiveColor(index, tab.selectedIconColor.get());
                if (canMergeColor(tab.iconColor)) bottomTabs.setIconInactiveColor(index, tab.iconColor.get());
                if (tab.selectedTextColor.hasValue()) bottomTabs.setTitleActiveColor(index, tab.selectedTextColor.get());
                if (tab.textColor.hasValue()) bottomTabs.setTitleInactiveColor(index, tab.textColor.get());
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
                if (tab.testId.hasValue()) bottomTabs.setTag(index, tab.testId.get());
                if (shouldApplyDot(tab)) mergeDotIndicator(index, tab.dotIndicator); else mergeBadge(index, tab);
            }
        });
    }

    private void applyDotIndicator(int tabIndex, DotIndicatorOptions dotIndicator) {
        if(dotIndicator.visible.isFalse()) return;
        AHNotification.Builder builder = new AHNotification.Builder()
                .setText("")
                .setBackgroundColor(dotIndicator.color.get(null))
                .setSize(dotIndicator.size.get(defaultDotIndicatorSize))
                .animate(dotIndicator.animate.get(false));
        bottomTabs.perform(bottomTabs -> bottomTabs.setNotification(builder.build(), tabIndex));
    }

    private void applyBadge(int tabIndex, BottomTabOptions tab) {
        if (bottomTabs == null) return;
        AHNotification.Builder builder = new AHNotification.Builder()
                .setText(tab.badge.get(""))
                .setBackgroundColor(tab.badgeColor.get(null))
                .animate(tab.animateBadge.get(false));
        bottomTabs.perform(bottomTabs -> bottomTabs.setNotification(builder.build(), tabIndex));
    }

    private void mergeBadge(int index, BottomTabOptions tab) {
        if (bottomTabs == null || !tab.badge.hasValue()) return;
        AHNotification.Builder builder = new AHNotification.Builder();
        if (tab.badge.hasValue()) builder.setText(tab.badge.get());
        if (tab.badgeColor.hasValue()) builder.setBackgroundColor(tab.badgeColor.get());
        if (tab.animateBadge.hasValue()) builder.animate(tab.animateBadge.get());
        bottomTabs.perform(bottomTabs -> bottomTabs.setNotification(builder.build(), index));
    }

    private void mergeDotIndicator(int index, DotIndicatorOptions dotIndicator) {
        if (bottomTabs == null) return;
        AHNotification.Builder builder = new AHNotification.Builder();
        if (dotIndicator.color.hasValue()) builder.setBackgroundColor(dotIndicator.color.get());
        builder.setSize(dotIndicator.visible.isFalse() ? 0 : dotIndicator.size.get(defaultDotIndicatorSize));
        if (dotIndicator.animate.hasValue()) builder.animate(dotIndicator.animate.get());
        AHNotification notification = builder.build();
        if (notification.hasValue()) bottomTabs.perform(bottomTabs -> bottomTabs.setNotification(notification, index));
    }

    private boolean shouldApplyDot(BottomTabOptions tab) {
        return tab.dotIndicator.visible.hasValue() && !tab.badge.hasValue();
    }

    private boolean canMergeColor(ThemeColour p) {
        return p.hasValue() && p.canApplyValue();
    }

    public void onConfigurationChanged(Options options) {
        bottomTabs.perform(bottomTabs -> {
            for (int i = 0; i < tabs.size(); i++) {
                BottomTabOptions tab = tabs.get(i).resolveCurrentOptions(defaultOptions).bottomTabOptions;
                if (tab.selectedIconColor.canApplyValue()) bottomTabs.setIconActiveColor(i, tab.selectedIconColor.get(null));
                if (tab.iconColor.canApplyValue()) bottomTabs.setIconInactiveColor(i, tab.iconColor.get(null));
                bottomTabs.setTitleActiveColor(i, tab.selectedTextColor.get(null));
                bottomTabs.setTitleInactiveColor(i, tab.textColor.get(null));
                if (shouldApplyDot(tab)) applyDotIndicator(i, tab.dotIndicator);
                if (tab.badge.hasValue()) applyBadge(i, tab);
            }
        });
    }
}
