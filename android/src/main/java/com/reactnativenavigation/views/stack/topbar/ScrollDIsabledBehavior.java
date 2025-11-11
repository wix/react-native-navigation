package com.reactnativenavigation.views.stack.topbar;

import com.google.android.material.appbar.AppBarLayout;

import androidx.annotation.NonNull;

public class ScrollDIsabledBehavior extends AppBarLayout.Behavior {
    public ScrollDIsabledBehavior() {
        setDragCallback(new DragCallback() {
            @Override
            public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                return false;
            }
        });
    }
}
