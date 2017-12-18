package com.reactnativenavigation.views;

import android.content.Context;
import android.view.MotionEvent;

import com.facebook.react.uimanager.TouchTargetHelper;
import com.reactnativenavigation.params.NavigationParams;

/**
 * Created by khmelev on 28/10/2017.
 */

public class ContentOverlayView extends ContentView {

    public ContentOverlayView(Context context, String screenId, NavigationParams navigationParams) {
        super(context, screenId, navigationParams);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        super.onTouchEvent(ev);

        int tag = TouchTargetHelper.findTargetTagForTouch(ev.getX(), ev.getY(), this);
        return tag != this.getId();
    }
}
