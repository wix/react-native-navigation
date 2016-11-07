package com.reactnativenavigation.views;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.reactnativenavigation.NavigationApplication;
import com.reactnativenavigation.params.TitleBarButtonParams;
import com.reactnativenavigation.utils.ViewUtils;

import java.util.ArrayList;

class TitleBarButton implements MenuItem.OnMenuItemClickListener {

    protected final Menu menu;
    protected final View parent;
    protected TitleBarButtonParams buttonParams;
    @Nullable protected String navigatorEventId;

    TitleBarButton(Menu menu, View parent, TitleBarButtonParams buttonParams, @Nullable String navigatorEventId) {
        this.menu = menu;
        this.parent = parent;
        this.buttonParams = buttonParams;
        this.navigatorEventId = navigatorEventId;
    }

    MenuItem addToMenu(int index) {
        MenuItem item = menu.add(Menu.NONE, Menu.NONE, index, buttonParams.label);
        item.setShowAsAction(buttonParams.showAsAction.action);
        item.setEnabled(buttonParams.enabled);
        setIcon(item);
        setColor();
        item.setOnMenuItemClickListener(this);
        return item;
    }


    private void setIcon(MenuItem item) {
        if (hasIcon()) {
            item.setIcon(buttonParams.icon);
        }
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
            ((TextView) button).setTextColor(buttonParams.color.getColor());
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
}
