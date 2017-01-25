package com.reactnativenavigation.views.slidingOverlay;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.widget.RelativeLayout;

import com.reactnativenavigation.animation.PeekingAnimator;
import com.reactnativenavigation.params.SlidingOverlayParams;
import com.reactnativenavigation.screens.Screen;
import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.views.ContentView;

public class SlidingOverlay {

    private final RelativeLayout parent;
    private final SlidingOverlayParams params;

    private SlidingListener listener;

    public interface SlidingListener {
        void onSlidingOverlayGone();
    }

    public SlidingOverlay(RelativeLayout parent, SlidingOverlayParams params) {
        this.parent = parent;
        this.params = params;
    }

    public void setSlidingListener(SlidingListener listener) {
        this.listener = listener;
    }

    public void show() {
        final ContentView view = createSlidingOverlayView(params);
        parent.addView(view);

        final PeekingAnimator animator = new PeekingAnimator(view);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animator) {
                onSlidingOverlayEnd(view);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                onSlidingOverlayEnd(view);
            }
        });
        view.setOnDisplayListener(new Screen.OnDisplayListener() {
            @Override
            public void onDisplay() {
                view.setVisibility(View.VISIBLE);
                animator.animate();
            }
        });
    }

    protected ContentView createSlidingOverlayView(SlidingOverlayParams params) {
        final float heightPixels = ViewUtils.convertDpToPixel(100);

        final RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) heightPixels);
        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);

        final ContentView view = new ContentView(parent.getContext(), params.screenInstanceId, params.navigationParams);
        view.setLayoutParams(lp);
        view.setVisibility(View.INVISIBLE);
        return view;
    }

    protected void onSlidingOverlayEnd(ContentView view) {
        view.unmountReactView();
        parent.removeView(view);

        if (listener != null) {
            listener.onSlidingOverlayGone();
        }
    }
}
