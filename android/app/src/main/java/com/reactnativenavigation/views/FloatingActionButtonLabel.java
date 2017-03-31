package com.reactnativenavigation.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.reactnativenavigation.R;
import com.reactnativenavigation.utils.ViewUtils;

/**
 * Created by jsierra on 31/03/17.
 */

public class FloatingActionButtonLabel extends TextView {
    private int mLabelsStyle;
    private Context mContext = null;
    private int mVerticalPadding = (int) ViewUtils.convertDpToPixel(4);
    private int mHorizontalPadding = (int) ViewUtils.convertDpToPixel(8);

    public FloatingActionButtonLabel(Context context) {
        super(context);
        this.init(context);
    }

    private void init(Context context) {
        mContext = new ContextThemeWrapper(context, mLabelsStyle);
        TypedArray attr = context.obtainStyledAttributes(null, R.styleable.FloatingActionsMenu, 0, 0);
        mLabelsStyle = attr.getResourceId(R.styleable.FloatingActionsMenu_fab_labelStyle, 0);

        setDefaultStyles(context);
    }

    private void setDefaultStyles(Context context) {
        setTextAppearance(context, mLabelsStyle);

        setHeight((int) ViewUtils.convertDpToPixel(24));
        setBackgroundColor(Color.WHITE);
        setPadding(mHorizontalPadding, mVerticalPadding, mHorizontalPadding, mVerticalPadding);
    }
}
