package com.reactnativenavigation.params;

import android.graphics.drawable.Drawable;
import android.view.MenuItem;

public class BaseTitleBarButtonParams {
    public enum ShowAsAction {
        IfRoom(MenuItem.SHOW_AS_ACTION_IF_ROOM),
        Always(MenuItem.SHOW_AS_ACTION_ALWAYS),
        Never(MenuItem.SHOW_AS_ACTION_NEVER),
        WithText(MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        public final int action;

        ShowAsAction(int action) {
            this.action = action;
        }
    }

    public String eventId;
    public String label;
    public Drawable icon;
    public StyleParams.Color color;
    public StyleParams.Color disabledColor;
    public ShowAsAction showAsAction;
    public boolean enabled = true;

    public void setColorFromScreenStyle(StyleParams.Color titleBarButtonColor) {
        if (!color.hasColor() && titleBarButtonColor.hasColor()) {
            color = titleBarButtonColor;
        }
    }
}
