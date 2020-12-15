package com.reactnativenavigation.views.pip;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.reactnativenavigation.R;

public class PIPButtonsLayout extends FrameLayout {
    private IPIPButtonListener pipButtonListener;
    private boolean shouldVisible = false;

    public PIPButtonsLayout(Context context) {
        super(context);
        initView(context);
    }

    public PIPButtonsLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PIPButtonsLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public PIPButtonsLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        addView(LayoutInflater.from(context).inflate(R.layout.pip_buttons_layout, null));
        FrameLayout.LayoutParams buttonParams = new FrameLayout.LayoutParams(CoordinatorLayout.LayoutParams.WRAP_CONTENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT);
        buttonParams.setMargins(0, 8, 0, 0);
        setLayoutParams(buttonParams);
        findViewById(R.id.fullScreenButton).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pipButtonListener != null)
                    pipButtonListener.onFullScreenClick();
            }
        });
        findViewById(R.id.closeButton).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pipButtonListener != null)
                    pipButtonListener.onCloseClick();
            }
        });
    }

    public void makeShortTimeVisible() {
        shouldVisible = false;
        setVisibility(VISIBLE);
        if (getHandler() != null) {
            getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!shouldVisible) {
                        setVisibility(GONE);
                    }
                }
            }, 5000);
        }
    }

    public void makePermanentVisible() {
        shouldVisible = true;
        setVisibility(VISIBLE);
    }

    public void hide() {
        shouldVisible = false;
        setVisibility(GONE);
    }

    public boolean isWithinBounds(MotionEvent ev) {
        int xPoint = Math.round(ev.getRawX());
        int yPoint = Math.round(ev.getRawY());
        int[] l = new int[2];
        getLocationOnScreen(l);
        int x = l[0];
        int y = l[1];
        int w = getWidth();
        int h = getHeight();
        return !(xPoint < x || xPoint > x + w || yPoint < y || yPoint > y + h);
    }

    public void setPipButtonListener(IPIPButtonListener pipButtonListener) {
        this.pipButtonListener = pipButtonListener;
    }

    public interface IPIPButtonListener {
        void onFullScreenClick();

        void onCloseClick();
    }
}