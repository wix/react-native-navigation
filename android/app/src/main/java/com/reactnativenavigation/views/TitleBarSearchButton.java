package com.reactnativenavigation.views;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.reactnativenavigation.NavigationApplication;
import com.reactnativenavigation.R;
import com.reactnativenavigation.params.TitleBarButtonParams;
import com.reactnativenavigation.utils.ReflectionUtils;
import com.reactnativenavigation.utils.ViewUtils;

class TitleBarSearchButton extends TitleBarButton implements SearchView.OnQueryTextListener, View.OnFocusChangeListener, View.OnClickListener {
    static final String BUTTON_ID = "searchView";
    private SearchView searchView;

    TitleBarSearchButton(Menu menu, View parent, TitleBarButtonParams buttonParams, @Nullable String navigatorEventId) {
        super(menu, parent, buttonParams, navigatorEventId);
    }

    MenuItem addToMenu(int index) {
        ((Activity) parent.getContext()).getMenuInflater().inflate(R.menu.search_item, menu);
        MenuItem item = menu.findItem(R.id.toolbar_action_search);
        item.setOnMenuItemClickListener(this);
        if (buttonParams.icon != null) {
            item.setIcon(buttonParams.icon);
        }
        searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setQueryHint(buttonParams.hint);
        searchView.setOnQueryTextFocusChangeListener(this);
        searchView.setOnQueryTextListener(this);
        searchView.setOnSearchClickListener(this);
        setColor();
        return item;
    }

    private void setColor() {
        EditText searchEditText = ViewUtils.findChildByClass(searchView, EditText.class);
        if (searchEditText != null) {
            if (buttonParams.color.hasColor()) {
                searchEditText.setTextColor(buttonParams.color.getColor());
                searchEditText.setHintTextColor(buttonParams.color.getColor());
            }
            colorCloseButton(searchEditText);
            setImagePlateColor();
        }
    }

    private void colorCloseButton(EditText searchEditText) {
        ViewUtils.performOnChildren((ViewGroup) searchEditText.getParent(), new ViewUtils.PerformOnViewTask() {
            @Override
            public void runOnView(View view) {
                if (view instanceof ImageView) {
                    if (buttonParams.color.hasColor()) {
                        ((ImageView) view).setColorFilter(buttonParams.color.getColor());
                    }
                }
            }
        });
    }

    private void setImagePlateColor() {
        if (buttonParams.color.hasColor()) {
            Object mSearchPlate = ReflectionUtils.getDeclaredField(searchView, "mSearchPlate");
            if (mSearchPlate != null) {
                Drawable background = ((View) mSearchPlate).getBackground();
                if (background != null) {
                    ViewUtils.tintDrawable(background, buttonParams.color.getColor(), true);
                }
            }
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        setupBackButtonAfterSearchViewIsExpended();
        return false;
    }

    private void setupBackButtonAfterSearchViewIsExpended() {
        ViewUtils.runOnPreDraw(searchView, new Runnable() {
            @Override
            public void run() {
                Object backButton = ViewUtils.findChildByClass((ViewGroup) searchView.getParent(), ImageButton.class);
                if (backButton != null) {
                    setBackButtonClickListener((View) backButton);
                    colorBackButton((ImageView) backButton);
                }
            }

            private void colorBackButton(ImageView backButton) {
                if (buttonParams.color.hasColor()) {
                    ViewUtils.tintDrawable(backButton.getDrawable(), buttonParams.color.getColor(), true);
                }
            }

            private void setBackButtonClickListener(View backButton) {
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((Toolbar) searchView.getParent()).collapseActionView();
                        sendEvent("searchViewHidden");
                    }
                });
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        WritableMap arguments = Arguments.createMap();
        arguments.putString("query", query);
        sendEvent("searchQuerySubmit", arguments);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        WritableMap arguments = Arguments.createMap();
        arguments.putString("query", newText);
        sendEvent("searchQueryChange", arguments);
        return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        WritableMap arguments = Arguments.createMap();
        arguments.putBoolean("hasFocus", hasFocus);
        sendEvent("searchFocusChange", arguments);
    }

    @Override
    public void onClick(View v) {
        sendEvent("searchViewShown");
    }

    private void sendEvent(String eventId, WritableMap arguments) {
        NavigationApplication.instance.getEventEmitter().sendNavigatorEvent(eventId, navigatorEventId, arguments);
    }

    private void sendEvent(String eventId) {
        NavigationApplication.instance.getEventEmitter().sendNavigatorEvent(eventId, navigatorEventId);
    }
}
