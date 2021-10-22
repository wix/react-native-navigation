package com.reactnativenavigation.views.pip;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.reactnativenavigation.options.PIPActionButton;
import com.reactnativenavigation.utils.UiUtils;

public class PIPCenterButtonsLayout extends LinearLayout {
    private PIPCenterButtonsLayout.IPIPButtonListener pipButtonListener;

    public PIPCenterButtonsLayout(Context context) {
        super(context);
    }

    public PIPCenterButtonsLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PIPCenterButtonsLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PIPCenterButtonsLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setButtons(PIPActionButton[] buttons) {
        removeAllViews();
        setOrientation(HORIZONTAL);
        if (buttons != null) {
            boolean isFirst = true;
            int iconSize = UiUtils.dpToPx(getContext(), 28);
            int iconMargin = UiUtils.dpToPx(getContext(), 24);
            for (PIPActionButton button : buttons) {
                int id = getContext().getResources().getIdentifier(button.actionIcon.get(), "drawable", getContext().getPackageName());
                Icon icon = Icon.createWithResource(getContext(), id);
                ImageView imageView = new ImageView(getContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(iconSize, iconSize);
                imageView.setTag(button);
                imageView.setImageDrawable(icon.loadDrawable(getContext()));
                params.setMargins(isFirst ? 0 : iconMargin, 0, 0, 0);
                imageView.setLayoutParams(params);
                imageView.setOnClickListener(v -> {
                    pipButtonListener.onButtonClick((PIPActionButton) imageView.getTag());
                });
                addView(imageView);
                isFirst = false;
            }
        }
        setLayoutParamsImpl();
    }

    private void setLayoutParamsImpl() {
        ViewGroup.LayoutParams params = getLayoutParams();
        if (params == null) {
            params = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            ((FrameLayout.LayoutParams) params).gravity = Gravity.CENTER;
        }
        setLayoutParams(params);
    }

    public void updatePIPState(PIPStates pipState) {
        if (pipState == PIPStates.CUSTOM_EXPANDED) {
            setBackgroundColor(Color.TRANSPARENT);
            setVisibility(VISIBLE);
        } else {
            setVisibility(GONE);
        }
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

    public void setPipButtonListener(PIPCenterButtonsLayout.IPIPButtonListener pipButtonListener) {
        this.pipButtonListener = pipButtonListener;
    }

    public interface IPIPButtonListener {
        void onButtonClick(PIPActionButton button);
    }
}