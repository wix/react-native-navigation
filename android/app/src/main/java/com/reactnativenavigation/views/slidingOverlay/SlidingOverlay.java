package com.reactnativenavigation.views.slidingOverlay;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.widget.RelativeLayout;

import com.reactnativenavigation.animation.PeekingAnimator;
import com.reactnativenavigation.params.SlidingOverlayParams;
import com.reactnativenavigation.params.SlidingOverlayParams.Position;
import com.reactnativenavigation.screens.Screen;
import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.views.ContentView;
import com.reactnativenavigation.views.utils.ViewMeasurer;

public class SlidingOverlay {

    private enum VisibilityState {
        Hidden, AnimateHide, Shown, AnimateShow
    }

    private ContentView view = null;
    private final RelativeLayout parent;
    private final SlidingOverlayParams params;

    private SlidingListener listener;
    private VisibilityState visibilityState = VisibilityState.Hidden;

    public interface SlidingListener {
        void onSlidingOverlayGone();
        void onSlidingOverlayShown();
    }

    public SlidingOverlay(RelativeLayout parent, SlidingOverlayParams params) {
        this.parent = parent;
        this.params = params;
    }

    public void setSlidingListener(SlidingListener listener) {
        this.listener = listener;
    }

    public Integer getAutoDismissTimerSec() {
        return params.autoDismissTimerSec;
    }

    public void show() {
        view = createSlidingOverlayView(params);
        parent.addView(view);

        view.setOnDisplayListener(new Screen.OnDisplayListener() {
            @Override
            public void onDisplay() {
                final PeekingAnimator animator = new PeekingAnimator(view, params.position, true);
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationCancel(Animator animator) {
                        onSlidingOverlayShown(view);
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        onSlidingOverlayShown(view);
                    }
                });

                view.setVisibility(View.VISIBLE);
                visibilityState = VisibilityState.AnimateShow;
                animator.animate();
            }
        });
    }

    public void hide() {
        final PeekingAnimator animator = new PeekingAnimator(view, params.position, false);
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

        visibilityState = VisibilityState.AnimateHide;
        animator.animate();
    }

    public void destroy() {
        visibilityState = VisibilityState.Hidden;
        view.unmountReactView();
        parent.removeView(view);
    }

    public boolean isShowing() {
        return VisibilityState.AnimateShow == visibilityState;
    }

    public boolean isVisible() {
        return VisibilityState.Shown == visibilityState;
    }

    public boolean isHiding() {
        return VisibilityState.AnimateHide == visibilityState;
    }

    protected ContentView createSlidingOverlayView(SlidingOverlayParams params) {
        final RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(params.position == Position.Top ? RelativeLayout.ALIGN_PARENT_TOP : RelativeLayout.ALIGN_PARENT_BOTTOM);

        final ContentView view = new ContentView(parent.getContext(), params.screenInstanceId, params.navigationParams);
        view.setViewMeasurer(new OverlayViewMeasurer(view));
        view.setLayoutParams(lp);
        view.setVisibility(View.INVISIBLE);
        return view;
    }

    protected void onSlidingOverlayShown(ContentView view) {
        visibilityState = VisibilityState.Shown;
        if (listener != null) {
            listener.onSlidingOverlayShown();
        }
    }

    protected void onSlidingOverlayEnd(ContentView view) {
        destroy();

        if (listener != null) {
            listener.onSlidingOverlayGone();
        }
    }
}
