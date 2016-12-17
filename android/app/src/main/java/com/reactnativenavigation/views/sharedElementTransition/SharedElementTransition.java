package com.reactnativenavigation.views.sharedElementTransition;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;

import com.reactnativenavigation.screens.Screen;
import com.reactnativenavigation.utils.Task;
import com.reactnativenavigation.utils.ViewUtils;

public class SharedElementTransition extends ViewGroup {
    public SharedElementTransition(Context context) {
        super(context);
        setContentDescription("SET");
    }

    public void registerSharedElementTransition(final String key) {
        ViewUtils.runOnPreDraw(this, new Runnable() {
            @Override
            public void run() {
                ViewUtils.performOnParentScreen(SharedElementTransition.this, new Task<Screen>() {
                    @Override
                    public void run(Screen screen) {
                        screen.registerSharedView(SharedElementTransition.this, key);
                    }
                });
            }
        });
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {

    }
}
