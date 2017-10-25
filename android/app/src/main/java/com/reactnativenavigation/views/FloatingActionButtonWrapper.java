package com.reactnativenavigation.views;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.support.design.widget.FloatingActionButton;


import android.view.ViewPropertyAnimator;
import android.widget.TextView;

import com.reactnativenavigation.R;


public class FloatingActionButtonWrapper extends FloatingActionButton {
    private String mTitle;

    public FloatingActionButtonWrapper(Context context) {
        super(context);
    }

    public void setTitle(String title) {
        mTitle = title;

        if (hasLabel()) {
            getLabelView().setText(title);
        }
    }

    FloatingActionButtonLabel getLabelView() {
        return (FloatingActionButtonLabel) getTag(R.id.fab_label);
    }

    public String getTitle() {
        return mTitle;
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    private void setBackgroundCompat(Drawable drawable) {
        if (Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
            setBackground(drawable);
        } else {
            setBackgroundDrawable(drawable);
        }
    }

    @Override
    public void setVisibility(int visibility) {
        if (hasLabel()) {
            getLabelView().setVisibility(visibility);
        }

        super.setVisibility(visibility);
    }

    @Override
    public void setAlpha(float alpha) {
        super.setAlpha(alpha);

        if(hasLabel()) {
            getLabelView().setAlpha(alpha);
        }
    }

    @Override
    public void setY(float y) {
        super.setY(y);

        if(hasLabel()) {
            TextView label = getLabelView();
            label.setY(y + ((getHeight()/2) - (label.getHeight())/2)); // Center label about fab Y position
        }
    }


    public ViewPropertyAnimator animate(int alpha, int scaleX, int scaleY, int duration) {
        if(hasLabel()) {
            getLabelView()
                    .animate()
                    .alpha(alpha)
                    .scaleX(scaleX)
                    .scaleY(scaleY)
                    .setDuration(duration)
                    .start();
        }

        return super.animate();
    }

    private boolean hasLabel(){
        return getTag(R.id.fab_label) != null;
    }
}