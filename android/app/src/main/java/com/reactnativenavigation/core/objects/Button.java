package com.reactnativenavigation.core.objects;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.MenuItem;

import com.facebook.react.bridge.ReadableMap;
import com.reactnativenavigation.utils.IconUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by guyc on 08/04/16.
 */
public class Button extends JsonObject implements Serializable {
    private static final long serialVersionUID = -570145217281069067L;

    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_ICON = "icon";
    private static final String KEY_DISABLED = "disabled";

    public String id;
    public String title;
    private String mIconSource;
    public boolean disabled;

    private static final AtomicInteger sAtomicIdGenerator = new AtomicInteger();
    private static final Map<String, Integer> sStringToNumericId = new HashMap<>();

    public Button(ReadableMap button) {
        id = getString(button, KEY_ID);
        title = getString(button, KEY_TITLE, "");
        mIconSource = getString(button, KEY_ICON);
        disabled = getBoolean(button, KEY_DISABLED);
    }

    public boolean hasIcon() {
        return mIconSource != null;
    }

    /**
     * @param dimensions The requested icon dimensions
     */
    public Drawable getIcon(Context ctx, int dimensions) {
       return IconUtils.getIcon(ctx, mIconSource, dimensions);
    }

    public int getItemId() {
        if (sStringToNumericId.containsKey(id)) {
            return sStringToNumericId.get(id);
        }

        int itemId = sAtomicIdGenerator.addAndGet(1);
        sStringToNumericId.put(id, itemId);
        return itemId;
    }

    /**
     * Each button has a string id, defined in JS, which is used to identify the button when
     * handling events.
     * @param item Toolbar button
     * @return Returns the event id associated with the given menu item
     */
    public static String getButtonEventId(MenuItem item) {
        for (Map.Entry<String, Integer> entry : sStringToNumericId.entrySet()) {
            if (entry.getValue() == item.getItemId()) {
                return entry.getKey();
            }
        }

        return null;
    }
}
