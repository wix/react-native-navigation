package com.reactnativenavigation.views;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.mikepenz.actionitembadge.library.ActionItemBadgeAdder;
import com.reactnativenavigation.NavigationApplication;
import com.reactnativenavigation.params.TitleBarButtonParams;
import com.reactnativenavigation.utils.ViewUtils;

import java.util.ArrayList;

class TitleBarButton implements MenuItem.OnMenuItemClickListener, ActionItemBadge.ActionItemBadgeListener {

    protected final Menu menu;
    protected final View parent;
    TitleBarButtonParams buttonParams;
    @Nullable protected String navigatorEventId;

    TitleBarButton(Menu menu, View parent, TitleBarButtonParams buttonParams, @Nullable String navigatorEventId) {
        this.menu = menu;
        this.parent = parent;
        this.buttonParams = buttonParams;
        this.navigatorEventId = navigatorEventId;
    }

    boolean addToMenu(int index) {
        setColor();
        if (hasIcon()) {
            createActionItem(index, buttonParams.label, buttonParams.showAsAction.action, buttonParams.icon, buttonParams.style, buttonParams.badgeCount);
        } else {
            createActionItem(index, buttonParams.label, buttonParams.showAsAction.action);
        }

        return true;
    }

    private void createActionItem(Integer index, String label, Integer showAsAction) {
        MenuItem item = menu.add(Menu.NONE, Menu.NONE, index, label);
        item.setShowAsAction(showAsAction);
        item.setEnabled(buttonParams.enabled);
        item.setOnMenuItemClickListener(this);
    }

    private void createActionItem(Integer index, String label, Integer showAsAction, Drawable icon, ActionItemBadge.BadgeStyles style, Integer badge) {
        new ActionItemBadgeAdder().act((Activity) parent.getContext()).menu(menu).itemDetails(Menu.NONE, Menu.NONE, index).title(label).showAsAction(showAsAction).add(icon, style.getStyle(), badge, this);
    }

    private void setColor() {
        if (!hasColor()) {
            return;
        }

        if (hasIcon()) {
            setIconColor();
        } else {
            setTextColor();
        }
    }

    private void setIconColor() {
        ViewUtils.tintDrawable(buttonParams.icon, buttonParams.color.getColor(), buttonParams.enabled);
    }

    private void setTextColor() {
        ViewUtils.runOnPreDraw(parent, new Runnable() {
            @Override
            public void run() {
                ArrayList<View> outViews = findActualTextViewInMenuByLabel();
                setTextColorForFoundButtonViews(outViews);
            }
        });
    }

    @NonNull
    private ArrayList<View> findActualTextViewInMenuByLabel() {
        ArrayList<View> outViews = new ArrayList<>();
        parent.findViewsWithText(outViews, buttonParams.label, View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
        return outViews;
    }

    private void setTextColorForFoundButtonViews(ArrayList<View> outViews) {
        for (View button : outViews) {
            ((TextView) button).setTextColor(buttonParams.getColor().getColor());
        }
    }

    private boolean hasIcon() {
        return buttonParams.icon != null;
    }

    private boolean hasColor() {
        return buttonParams.color.hasColor();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        NavigationApplication.instance.getEventEmitter().sendNavigatorEvent(buttonParams.eventId, navigatorEventId);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {
        return onMenuItemClick(menu);
    }

}
