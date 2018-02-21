package com.reactnativenavigation.anim;


import android.support.annotation.NonNull;

import com.reactnativenavigation.interfaces.ScrollEventListener;

public class FabCollapseBehaviour implements ScrollEventListener.OnScrollListener, ScrollEventListener.OnDragListener {

    private FabAnimator fabAnimator;
    private ScrollEventListener scrollEventListener;

    public FabCollapseBehaviour(FabAnimator fabAnimator) {
        this.fabAnimator = fabAnimator;
    }

    public void enableCollapse(@NonNull ScrollEventListener scrollEventListener) {
        this.scrollEventListener = scrollEventListener;
        this.scrollEventListener.register(null, this, this);
    }

    public void disableCollapse() {
        if (scrollEventListener != null) {
            scrollEventListener.unregister();
        }
    }

    @Override
    public void onScrollUp(float nextTranslation) {
        //empty
    }

    @Override
    public void onScrollDown(float nextTranslation) {
        //empty
    }

    @Override
    public void onShow() {
        fabAnimator.show();
    }

    @Override
    public void onHide() {
        fabAnimator.hide();
    }
}
