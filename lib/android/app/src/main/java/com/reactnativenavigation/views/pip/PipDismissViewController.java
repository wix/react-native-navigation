package com.reactnativenavigation.views.pip;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;

import com.reactnativenavigation.R;

public class PipDismissViewController {
    // This delay controls how long to wait before we show the target when the user first moves
    // the PIP, to prevent the target from animating if the user just wants to fling the PIP
    private static final int SHOW_TARGET_DELAY = 100;
    private static final int SHOW_TARGET_DURATION = 350;
    private static final int HIDE_TARGET_DURATION = 225;
    private Context mContext;
    private WindowManager mWindowManager;
    private View mDismissView;

    public PipDismissViewController(Context context) {
        mContext = context;
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

    /**
     * Creates the dismiss target for showing via {@link #showDismissTarget()}.
     */
    public void createDismissTarget() {
        if (mDismissView == null) {
            // Determine sizes for the view
            final Rect stableInsets = new Rect();
            final Point windowSize = new Point();
            mWindowManager.getDefaultDisplay().getRealSize(windowSize);
            final int gradientHeight = mContext.getResources().getDimensionPixelSize(
                    R.dimen.pip_dismiss_gradient_height);
            final int bottomMargin = mContext.getResources().getDimensionPixelSize(
                    R.dimen.pip_dismiss_text_bottom_margin);
            // Create a new view for the dismiss target
            LayoutInflater inflater = LayoutInflater.from(mContext);
            mDismissView = inflater.inflate(R.layout.pip_dismiss_view, null);
            mDismissView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            // Set the gradient background
            Drawable gradient = new ColorDrawable(Color.BLACK);
            gradient.setAlpha((int) (255 * 0.85f));
            mDismissView.setBackground(gradient);
            // Adjust bottom margins of the text
            View text = mDismissView.findViewById(R.id.pip_dismiss_text);
            FrameLayout.LayoutParams tlp = (FrameLayout.LayoutParams) text.getLayoutParams();
            tlp.bottomMargin = stableInsets.bottom + bottomMargin;
            // Add the target to the window
            LayoutParams lp = new LayoutParams(
                    LayoutParams.MATCH_PARENT, gradientHeight,
                    0, windowSize.y - gradientHeight,
                    LayoutParams.TYPE_BASE_APPLICATION,
                    LayoutParams.FLAG_LAYOUT_IN_SCREEN
                            | LayoutParams.FLAG_NOT_TOUCHABLE
                            | LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
            lp.setTitle("pip-dismiss-overlay");
            lp.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
            mWindowManager.addView(mDismissView, lp);
        }
        mDismissView.animate().cancel();
    }

    /**
     * Shows the dismiss target.
     */
    public void showDismissTarget() {
        mDismissView.animate()
                .alpha(1f)
                .setInterpolator(Interpolators.LINEAR)
                .setStartDelay(SHOW_TARGET_DELAY)
                .setDuration(SHOW_TARGET_DURATION)
                .start();
    }

    /**
     * Hides and destroys the dismiss target.
     */
    public void destroyDismissTarget() {
        if (mDismissView != null) {
            mDismissView.animate()
                    .alpha(0f)
                    .setInterpolator(Interpolators.LINEAR)
                    .setStartDelay(0)
                    .setDuration(HIDE_TARGET_DURATION)
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            mWindowManager.removeViewImmediate(mDismissView);
                            mDismissView = null;
                        }
                    })
                    .start();
        }
    }
}