package com.reactnativenavigation.views.stack.topbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.VisibleForTesting;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.reactnativenavigation.R;
import com.reactnativenavigation.options.Alignment;
import com.reactnativenavigation.options.FontOptions;
import com.reactnativenavigation.options.LayoutDirection;
import com.reactnativenavigation.options.SubtitleOptions;
import com.reactnativenavigation.options.TitleOptions;
import com.reactnativenavigation.options.params.Number;
import com.reactnativenavigation.options.params.ThemeColour;
import com.reactnativenavigation.options.parsers.TypefaceLoader;
import com.reactnativenavigation.utils.CompatUtils;
import com.reactnativenavigation.utils.ContextKt;
import com.reactnativenavigation.utils.UiUtils;
import com.reactnativenavigation.viewcontrollers.stack.topbar.TopBarCollapseBehavior;
import com.reactnativenavigation.viewcontrollers.stack.topbar.button.ButtonController;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ScrollEventListener;
import com.reactnativenavigation.views.stack.topbar.titlebar.ButtonBar;
import com.reactnativenavigation.views.stack.topbar.titlebar.TitleAndButtonsContainer;
import com.reactnativenavigation.views.toptabs.TopTabs;

import org.jetbrains.annotations.NotNull;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

@SuppressLint("ViewConstructor")
public class TopBar extends AppBarLayout implements ScrollEventListener.ScrollAwareView {
    public final static int DEFAULT_TITLE_COLOR = Color.BLACK;
    public final static int DEFAULT_SUBTITLE_COLOR = Color.GRAY;

    private final TopBarCollapseBehavior collapsingBehavior;
    private TopTabs topTabs;
    private FrameLayout root;
    private View border;
    private View component;
    private float elevation = -1;
    private final TitleAndButtonsContainer titleAndButtonsContainer;

    @Nullable
    public Drawable getNavigationIcon() {
        return titleAndButtonsContainer.getLeftButtonBar().getNavigationIcon();
    }

    public TopBar(final Context context) {
        super(context);
        context.setTheme(R.style.TopBar);
        setId(CompatUtils.generateViewId());
        this.titleAndButtonsContainer = new TitleAndButtonsContainer(getContext());
        collapsingBehavior = new TopBarCollapseBehavior(this);
        topTabs = new TopTabs(getContext());
        createLayout();
    }

    private void createLayout() {
        setId(CompatUtils.generateViewId());
        topTabs = createTopTabs();
        border = createBorder();
        LinearLayout content = createContentLayout();

        root = new FrameLayout(getContext());
        root.setId(CompatUtils.generateViewId());
        content.addView(titleAndButtonsContainer, new MarginLayoutParams(MATCH_PARENT, UiUtils.getTopBarHeight(getContext())));
        content.addView(topTabs);
        root.addView(content);
        root.addView(border);
        addView(root, MATCH_PARENT, WRAP_CONTENT);
    }

    private LinearLayout createContentLayout() {
        LinearLayout content = new LinearLayout(getContext());
        content.setOrientation(VERTICAL);
        return content;
    }

    @NonNull
    private TopTabs createTopTabs() {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        lp.addRule(RelativeLayout.BELOW, titleAndButtonsContainer.getId());
        TopTabs topTabs = new TopTabs(getContext());
        topTabs.setLayoutParams(lp);
        topTabs.setVisibility(GONE);
        return topTabs;
    }

    private View createBorder() {
        View border = new View(getContext());
        border.setBackgroundColor(Color.TRANSPARENT);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(MATCH_PARENT, 0);
        lp.gravity = Gravity.BOTTOM;
        border.setLayoutParams(lp);
        return border;
    }

    public void setHeight(int height) {
        int pixelHeight = UiUtils.dpToPx(getContext(), height);
        if (pixelHeight == getLayoutParams().height) return;
        ViewGroup.LayoutParams lp = getLayoutParams();
        lp.height = pixelHeight;
        setLayoutParams(lp);
    }

    public void setTopPadding(int padding) {
        setPadding(0, padding, 0, 0);
    }

    public void setTitleHeight(int height) {
        int pixelHeight = UiUtils.dpToPx(getContext(), height);
        ViewGroup.LayoutParams layoutParams = titleAndButtonsContainer.getLayoutParams();
        if (pixelHeight == layoutParams.height) return;
        layoutParams.height = pixelHeight;
        titleAndButtonsContainer.setLayoutParams(layoutParams);
    }

    public void setTitleTopMargin(int topMargin) {
        int marginPx = UiUtils.dpToPx(getContext(), topMargin);
        MarginLayoutParams layoutParams = (MarginLayoutParams) titleAndButtonsContainer.getLayoutParams();
        if (layoutParams.topMargin != topMargin) {
            layoutParams.topMargin = marginPx;
            titleAndButtonsContainer.setLayoutParams(layoutParams);
        }
    }

    public void setTitle(String title) {
        titleAndButtonsContainer.setTitle(title);
    }

    public String getTitle() {
        return titleAndButtonsContainer.getTitle();
    }

    public void setSubtitle(String subtitle) {
        titleAndButtonsContainer.setSubtitle(subtitle);
    }

    public void setSubtitleColor(@ColorInt int color) {
        titleAndButtonsContainer.setSubtitleColor(color);
    }

    public void setSubtitleTypeface(TypefaceLoader typefaceLoader, FontOptions font) {
        if (typefaceLoader != null)
            titleAndButtonsContainer.setSubtitleTypeface(typefaceLoader, font);
    }

    public void setSubtitleFontSize(double size) {
        titleAndButtonsContainer.setSubtitleFontSize((float) size);
    }

    public void animateRightButtons(boolean animate) {
        titleAndButtonsContainer.animateRightButtons(animate);
    }

    public void animateLeftButtons(boolean animate) {
        titleAndButtonsContainer.animateLeftButtons(animate);
    }

    public void setSubtitleAlignment(Alignment alignment) {
        titleAndButtonsContainer.setSubTitleTextAlignment(alignment);
    }

    public void setTestId(String testId) {
        setTag(testId);
    }

    public void setTitleTextColor(@ColorInt int color) {
        titleAndButtonsContainer.setTitleColor(color);
    }

    public void setTitleFontSize(double size) {
        titleAndButtonsContainer.setTitleFontSize((float) size);
    }

    public void setTitleTypeface(TypefaceLoader typefaceLoader, FontOptions font) {
        if (typefaceLoader != null)
            titleAndButtonsContainer.setTitleTypeface(typefaceLoader, font);
    }

    public void setTitleAlignment(Alignment alignment) {
        titleAndButtonsContainer.setTitleBarAlignment(alignment);
    }

    public void setTitleComponent(View component, Alignment alignment) {
        titleAndButtonsContainer.setComponent(component, alignment);
    }

    public void setTitleComponent(View component) {
        this.setTitleComponent(component, Alignment.Default);
    }

    public void setBackgroundComponent(View component) {
        if (this.component == component || component.getParent() != null) return;
        this.component = component;
        root.addView(component, 0);
    }

    public void setTopTabFontFamily(int tabIndex, Typeface fontFamily) {
        topTabs.setFontFamily(tabIndex, fontFamily);
    }

    public void applyTopTabsColors(ThemeColour selectedTabColor, ThemeColour unselectedTabColor) {
        topTabs.applyTopTabsColors(selectedTabColor, unselectedTabColor);
    }

    public void applyTopTabsFontSize(Number fontSize) {
        topTabs.applyTopTabsFontSize(fontSize);
    }

    public void setTopTabsVisible(boolean visible) {
        topTabs.setVisibility(this, visible);
    }

    public void setTopTabsHeight(int height) {
        if (topTabs.getLayoutParams().height == height) return;
        topTabs.getLayoutParams().height = height > 0 ? UiUtils.dpToPx(getContext(), height) : height;
        topTabs.setLayoutParams(topTabs.getLayoutParams());
    }

    public void setBackButton(ButtonController backButton) {
        titleAndButtonsContainer.getLeftButtonBar().setBackButton(backButton);
    }

    public void clearLeftButtons() {
        titleAndButtonsContainer.getLeftButtonBar().clearButtons();
    }

    public void clearBackButton() {
        titleAndButtonsContainer.getLeftButtonBar().clearBackButton();
    }

    public void clearRightButtons() {
        titleAndButtonsContainer.getRightButtonBar().clearButtons();
    }

    public void setElevation(Double elevation) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && getElevation() != elevation.floatValue()) {
            this.elevation = UiUtils.dpToPx(getContext(), elevation.floatValue());
            setElevation(this.elevation);
        }
    }

    @Override
    public void setElevation(float elevation) {
        if (elevation == this.elevation && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.setElevation(elevation);
        }
    }

    public ButtonBar getRightButtonBar() {
        return titleAndButtonsContainer.getRightButtonBar();
    }

    public ButtonBar getLeftButtonBar() {
        return titleAndButtonsContainer.getLeftButtonBar();
    }

    public void initTopTabs(ViewPager viewPager) {
        topTabs.setVisibility(VISIBLE);
        topTabs.init(viewPager);
    }

    public void enableCollapse(ScrollEventListener scrollEventListener) {
        collapsingBehavior.enableCollapse(scrollEventListener);
        ((AppBarLayout.LayoutParams) root.getLayoutParams()).setScrollFlags(LayoutParams.SCROLL_FLAG_SCROLL);
    }

    public void disableCollapse() {
        collapsingBehavior.disableCollapse();
        ((AppBarLayout.LayoutParams) root.getLayoutParams()).setScrollFlags(0);
    }

    public void clearBackgroundComponent() {
        if (component != null) {
            root.removeView(component);
            component = null;
        }
    }

    public void clearTopTabs() {
        topTabs.clear();
    }

    @VisibleForTesting
    public TopTabs getTopTabs() {
        return topTabs;
    }

    public void setBorderHeight(double height) {
        border.getLayoutParams().height = (int) UiUtils.dpToPx(getContext(), (float) height);
    }

    public void setBorderColor(int color) {
        border.setBackgroundColor(color);
    }

    public void setOverflowButtonColor(int color) {
        titleAndButtonsContainer.getRightButtonBar().setOverflowButtonColor(color);
        titleAndButtonsContainer.getLeftButtonBar().setOverflowButtonColor(color);
    }

    public void setLayoutDirection(LayoutDirection direction) {
        titleAndButtonsContainer.setLayoutDirection(direction.get());
    }

    public void removeRightButton(ButtonController button) {
        removeRightButton(button.getButtonIntId());
    }

    public void removeLeftButton(ButtonController button) {
        removeLeftButton(button.getButtonIntId());
    }

    public void removeRightButton(int buttonId) {
        titleAndButtonsContainer.getRightButtonBar().removeButton(buttonId);
    }

    public void removeLeftButton(int buttonId) {
        titleAndButtonsContainer.getLeftButtonBar().removeButton(buttonId);
    }

    public void alignTitleComponent(@NotNull Alignment alignment) {
        titleAndButtonsContainer.setTitleBarAlignment(alignment);
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public TitleAndButtonsContainer getTitleAndButtonsContainer() {
        return titleAndButtonsContainer;
    }

    public void applyTitleOptions(TitleOptions titleOptions, TypefaceLoader typefaceLoader) {
        final double DEFAULT_TITLE_FONT_SIZE = 18;
        this.setTitle(titleOptions.text.get(""));
        this.setTitleFontSize(titleOptions.fontSize.get(DEFAULT_TITLE_FONT_SIZE));
        this.setTitleTextColor(titleOptions.color.get(DEFAULT_TITLE_COLOR));
        this.setTitleTypeface(typefaceLoader, titleOptions.font);
        this.setTitleAlignment(titleOptions.alignment);
    }

    public void applySubtitleOptions(SubtitleOptions subtitle, TypefaceLoader typefaceLoader) {
        final double DEFAULT_SUBTITLE_FONT_SIZE = 14;

        this.setSubtitle(subtitle.text.get(""));
        this.setSubtitleFontSize(subtitle.fontSize.get(DEFAULT_SUBTITLE_FONT_SIZE));
        this.setSubtitleColor(subtitle.color.get(DEFAULT_SUBTITLE_COLOR));
        this.setSubtitleTypeface(typefaceLoader, subtitle.font);
        this.setSubtitleAlignment(subtitle.alignment);
    }
}
