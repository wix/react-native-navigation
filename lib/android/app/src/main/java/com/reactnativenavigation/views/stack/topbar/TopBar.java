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
import com.reactnativenavigation.options.params.Colour;
import com.reactnativenavigation.options.params.Number;
import com.reactnativenavigation.options.parsers.TypefaceLoader;
import com.reactnativenavigation.utils.CompatUtils;
import com.reactnativenavigation.utils.UiUtils;
import com.reactnativenavigation.viewcontrollers.stack.topbar.TopBarCollapseBehavior;
import com.reactnativenavigation.viewcontrollers.stack.topbar.button.ButtonController;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ScrollEventListener;
import com.reactnativenavigation.views.stack.topbar.titlebar.LeftButtonsBar;
import com.reactnativenavigation.views.stack.topbar.titlebar.MainToolBar;
import com.reactnativenavigation.views.stack.topbar.titlebar.RightButtonsBar;
import com.reactnativenavigation.views.toptabs.TopTabs;

import org.jetbrains.annotations.NotNull;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

@SuppressLint("ViewConstructor")
public class TopBar extends AppBarLayout implements ScrollEventListener.ScrollAwareView {

    private final TopBarCollapseBehavior collapsingBehavior;
    private TopTabs topTabs;
    private FrameLayout root;
    private View border;
    private View component;
    private float elevation = -1;
    private final MainToolBar mainToolBar;

    public int getRightButtonsCount() {
        return mainToolBar.getRightButtonsBar().getButtonsCount();
    }

    @Nullable
    public Drawable getNavigationIcon() {
        return mainToolBar.getLeftButtonsBar().getNavigationIcon();
    }

    public TopBar(final Context context) {
        super(context);
        context.setTheme(R.style.TopBar);
        setId(CompatUtils.generateViewId());
        this.mainToolBar = new MainToolBar(getContext());
        collapsingBehavior = new TopBarCollapseBehavior(this);
        topTabs = new TopTabs(getContext());
        createLayout();
    }

    private void createLayout() {
        setId(CompatUtils.generateViewId());
        setFitsSystemWindows(true);
        topTabs = createTopTabs();
        border = createBorder();
        LinearLayout content = createContentLayout();

        root = new FrameLayout(getContext());
        root.setId(CompatUtils.generateViewId());
        content.addView(mainToolBar, MATCH_PARENT, UiUtils.getTopBarHeight(getContext()));
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
        lp.addRule(RelativeLayout.BELOW, mainToolBar.getId());
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

    public void setTitleHeight(int height) {
        mainToolBar.setHeight(height);
    }

    public void setTitleTopMargin(int topMargin) {
        mainToolBar.setTopMargin(topMargin);
    }

    public void setTitle(String title) {
        mainToolBar.setTitle(title);
    }

    public String getTitle() {
        return mainToolBar.getTitle();
    }

    public void setSubtitle(String subtitle) {
        mainToolBar.setSubtitle(subtitle);
    }

    public void setSubtitleColor(@ColorInt int color) {
        mainToolBar.setSubtitleColor(color);
    }

    public void setSubtitleTypeface(TypefaceLoader typefaceLoader, FontOptions font) {
        if (typefaceLoader != null)
            mainToolBar.setSubtitleTypeface(typefaceLoader, font);
    }

    public void setSubtitleFontSize(double size) {
        mainToolBar.setSubtitleFontSize((float) size);
    }

    public void setSubtitleAlignment(Alignment alignment) {
        mainToolBar.setSubTitleTextAlignment(alignment);
        mainToolBar.setTitleBarAlignment(alignment);
    }

    public void setTestId(String testId) {
        setTag(testId);
    }

    public void setTitleTextColor(@ColorInt int color) {
        mainToolBar.setTitleColor(color);
    }

    public void setTitleFontSize(double size) {
        mainToolBar.setTitleFontSize((float) size);
    }

    public void setTitleTypeface(TypefaceLoader typefaceLoader, FontOptions font) {
        if (typefaceLoader != null)
            mainToolBar.setTitleTypeface(typefaceLoader, font);
    }

    public void setTitleAlignment(Alignment alignment) {
        mainToolBar.setTitleBarAlignment(alignment);
        mainToolBar.setTitleTextAlignment(alignment);
    }

    public void setTitleComponent(View component, Alignment alignment) {
        mainToolBar.setComponent(component, alignment);
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

    public void applyTopTabsColors(Colour selectedTabColor, Colour unselectedTabColor) {
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
        mainToolBar.getLeftButtonsBar().setBackButton(backButton);
    }

    public void clearLeftButtons() {
        mainToolBar.getLeftButtonsBar().clearButtons();
    }

    public void clearBackButton() {
        mainToolBar.getLeftButtonsBar().clearBackButton();
    }

    public void clearRightButtons() {
        mainToolBar.getRightButtonsBar().clearButtons();
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

    public RightButtonsBar getRightButtonsBar() {
        return mainToolBar.getRightButtonsBar();
    }

    public LeftButtonsBar getLeftButtonsBar() {
        return mainToolBar.getLeftButtonsBar();
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
        mainToolBar.getRightButtonsBar().setOverflowButtonColor(color);
        mainToolBar.getLeftButtonsBar().setOverflowButtonColor(color);
    }

    public void setLayoutDirection(LayoutDirection direction) {
        mainToolBar.setLayoutDirection(direction.get());
    }

    public void removeRightButton(ButtonController button) {
        removeRightButton(button.getButtonIntId());
    }

    public void removeLeftButton(ButtonController button) {
        removeLeftButton(button.getButtonIntId());
    }

    public void removeRightButton(int buttonId) {
        mainToolBar.getRightButtonsBar().removeButton(buttonId);
    }

    public void removeLeftButton(int buttonId) {
        mainToolBar.getLeftButtonsBar().removeButton(buttonId);
    }

    public void alignTitleComponent(@NotNull Alignment alignment) {
        mainToolBar.setTitleBarAlignment(alignment);
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public MainToolBar getMainToolBar() {
        return mainToolBar;
    }

    public void applyTitleOptions(TitleOptions titleOptions, TypefaceLoader typefaceLoader) {
        final double DEFAULT_TITLE_FONT_SIZE = 18;
        final int DEFAULT_TITLE_COLOR = Color.BLACK;

        this.setTitle(titleOptions.text.get(""));
        this.setTitleFontSize(titleOptions.fontSize.get(DEFAULT_TITLE_FONT_SIZE));
        this.setTitleTextColor(titleOptions.color.get(DEFAULT_TITLE_COLOR));
        this.setTitleTypeface(typefaceLoader, titleOptions.font);
        this.setTitleAlignment(titleOptions.alignment);
    }

    public void applySubtitleOptions(SubtitleOptions subtitle, TypefaceLoader typefaceLoader) {
        final double DEFAULT_SUBTITLE_FONT_SIZE = 14;
        final int DEFAULT_SUBTITLE_COLOR = Color.GRAY;

        this.setSubtitle(subtitle.text.get(""));
        this.setSubtitleFontSize(subtitle.fontSize.get(DEFAULT_SUBTITLE_FONT_SIZE));
        this.setSubtitleColor(subtitle.color.get(DEFAULT_SUBTITLE_COLOR));
        this.setSubtitleTypeface(typefaceLoader, subtitle.font);
        this.setSubtitleAlignment(subtitle.alignment);
    }
}
