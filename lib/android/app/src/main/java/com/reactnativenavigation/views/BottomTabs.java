package com.reactnativenavigation.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.view.ViewCompat;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.google.android.material.internal.BaselineLayout;
import com.reactnativenavigation.R;
import com.reactnativenavigation.parse.LayoutDirection;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.parse.params.TitleDisplayMode;

import static com.reactnativenavigation.utils.ViewUtils.findChildByClass;

@SuppressLint("ViewConstructor")
public class BottomTabs extends BottomNavigationView {

    private int textStateUnselected = Color.GRAY;
    private int textStateSelected = Color.BLACK;
    private int unselectedIconColor = Color.GRAY;
    private int selectedIconColor = Color.BLACK;
    private boolean isHidden = false;

    public BottomTabs(Context context) {
        super(context);
        setId(R.id.bottomTabs);
        setItemHorizontalTranslationEnabled(false);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // NOOP - don't recreate views on size change
    }

    public void setTitleState(Text titleState) {
        switch(titleState.get()) {
            case "alwaysHide":
                setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED);
                break;
            case "alwaysShow":
                setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
                break;
            case "showWhenActive":
                setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_SELECTED);
                break;
            default:
                break;
        }
    }

    public TitleDisplayMode getTitleState() {
        switch (super.getLabelVisibilityMode()) {
            case LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED:
                return TitleDisplayMode.ALWAYS_HIDE;
            case LabelVisibilityMode.LABEL_VISIBILITY_LABELED:
                return TitleDisplayMode.ALWAYS_SHOW;
            case LabelVisibilityMode.LABEL_VISIBILITY_SELECTED:
                return TitleDisplayMode.SHOW_WHEN_ACTIVE;
            case LabelVisibilityMode.LABEL_VISIBILITY_AUTO:
                return TitleDisplayMode.ALWAYS_SHOW;
            default:
                return TitleDisplayMode.UNDEFINED;
        }
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setText(int index, String text) {
        MenuItem item = getMenu().getItem(index);
        if (!item.getTitle().equals(text)) {
            item.setTitle(text);
        }
    }

    public void setIcon(int index, Drawable icon) {
        MenuItem item = getMenu().getItem(index);
        if (!item.getIcon().equals(icon)) {
            item.setIcon(icon);
        }
    }

    public void setSelectedIcon(int index, Drawable icon) {
        MenuItem item = getMenu().getItem(index);
        if (!item.getIcon().equals(icon)) {
            item.setIcon(icon);
        }
    }

    public void setLayoutDirection(LayoutDirection direction) {
        LinearLayout tabsContainer = findChildByClass(this, LinearLayout.class);
        if (tabsContainer != null) tabsContainer.setLayoutDirection(direction.get());
    }

    public void show() {

        isHidden = false;

        // Show bottom navigation
        ViewCompat.animate(this)
                .translationY(0)
                .setInterpolator(new LinearOutSlowInInterpolator())
                .setDuration(300)
                .start();
    }

    public void hide() {

        isHidden = true;

        // Hide bottom navigation
        ViewCompat.animate(this)
                .translationY(this.getHeight())
                .setInterpolator(new LinearOutSlowInInterpolator())
                .setDuration(300)
                .start();
    }

    public void setItemTypeface(Typeface tf, int index) {
        BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) getChildAt(0);
        BottomNavigationItemView item = (BottomNavigationItemView) bottomNavigationMenuView.getChildAt(index);
        View itemTitle = item.getChildAt(1);
        ((TextView) ((BaselineLayout) itemTitle).getChildAt(0)).setTypeface(tf);
        ((TextView) ((BaselineLayout) itemTitle).getChildAt(1)).setTypeface(tf);
    }

    private void buildTextColors() {
        ColorStateList itemTextColors = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked},
                        new int[]{android.R.attr.state_checked}
                },
                new int[]{
                        textStateUnselected,
                        textStateSelected
                });
        setItemTextColor(itemTextColors);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void buildIconColors(int index) {
        ColorStateList itemIconColors = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked},
                        new int[]{android.R.attr.state_checked}
                },
                new int[]{
                        unselectedIconColor,
                        selectedIconColor
                });

        getMenu().getItem(index).setIconTintList(itemIconColors);

    }

    public void setItemTextColor(int color) {
        textStateUnselected = color;
        buildTextColors();
    }

    public void setItemTextColorSelected(int color) {
        textStateSelected = color;
        buildTextColors();
    }

    public void setIconSelectedColor(int color, int index) {
        selectedIconColor = color;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            buildIconColors(index);
        }
    }

    public void setIconUnselectedColor(int color, int index) {
        unselectedIconColor = color;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            buildIconColors(index);
        }
    }
}
