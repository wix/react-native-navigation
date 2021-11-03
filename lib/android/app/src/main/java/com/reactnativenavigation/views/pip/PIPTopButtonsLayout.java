package com.reactnativenavigation.views.pip;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.reactnativenavigation.R;
import com.reactnativenavigation.utils.ILogger;

public class PIPTopButtonsLayout extends FrameLayout {
    private IPIPButtonListener pipButtonListener;
    private boolean shouldVisible = false;
    private ILogger logger;
    private String TAG = "PIPTopButtonsLayout";

    public PIPTopButtonsLayout(Context context) {
        super(context);
        initView(context);
    }

    public PIPTopButtonsLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PIPTopButtonsLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public PIPTopButtonsLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        addView(LayoutInflater.from(context).inflate(R.layout.pip_buttons_layout, null));
        FrameLayout.LayoutParams buttonParams = new FrameLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT);
        buttonParams.setMargins(0, 0, 0, 0);
        setLayoutParams(buttonParams);
        findViewById(R.id.fullScreenButton).setOnClickListener(v -> {
            if (pipButtonListener != null)
                pipButtonListener.onFullScreenClick();
        });
        findViewById(R.id.closeButton).setOnClickListener(v -> {
            if (pipButtonListener != null)
                pipButtonListener.onCloseClick();
        });
    }

    public void setPIPHeight(int pipHeight) {
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = pipHeight / 2;
        setLayoutParams(params);
    }

    public void makeShortTimeVisible() {
        shouldVisible = false;
        setVisibility(VISIBLE);
        setBackgroundResource(R.drawable.pip_button_background);
        if (getHandler() != null) {
            getHandler().postDelayed(() -> {
                if (!shouldVisible) {
                    setVisibility(GONE);
                }
            }, 5000);
        }
    }

    public void makePermanentVisible() {
        shouldVisible = true;
        setBackgroundColor(Color.TRANSPARENT);
        setVisibility(VISIBLE);
    }

    public void hide() {
        shouldVisible = false;
        setVisibility(GONE);
    }

    public boolean isWithinBounds(MotionEvent ev) {
        int xPoint = Math.round(ev.getRawX());
        int yPoint = Math.round(ev.getRawY());
        return isChildViewInBounds(findViewById(R.id.fullScreenButton), xPoint, yPoint) || isChildViewInBounds(findViewById(R.id.closeButton), xPoint, yPoint);
    }

    private boolean isChildViewInBounds(View view, int x, int y) {
        Rect outRect = new Rect();
        int[] location = new int[2];
        view.getDrawingRect(outRect);
        view.getLocationOnScreen(location);
        outRect.offset(location[0], location[1]);
        return outRect.contains(x, y);
    }

    public void setPipButtonListener(IPIPButtonListener pipButtonListener) {
        this.pipButtonListener = pipButtonListener;
    }

    public interface IPIPButtonListener {
        void onFullScreenClick();

        void onCloseClick();
    }
}