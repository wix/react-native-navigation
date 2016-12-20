package com.reactnativenavigation.views.collapsingToolbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.reactnativenavigation.params.CollapsingTopBarParams;
import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.views.Scrim;

import static android.widget.FrameLayout.LayoutParams.MATCH_PARENT;

public class CollapsingTopBarBackground extends FrameLayout {
    public static final float MAX_HEIGHT = ViewUtils.convertDpToPixel(256);
    private final CollapsingTopBarParams params;
    private SimpleDraweeView backdrop;
    private Scrim scrim;
    private int topBarHeight = -1;

    public CollapsingTopBarBackground(Context context, CollapsingTopBarParams params) {
        super(context);
        this.params = params;
        setFitsSystemWindows(true);
        createBackDropImage();
        createScrim();
        setWillNotDraw(false);
    }

    private void createBackDropImage() {
        backdrop = new SimpleDraweeView(getContext());
        setImageSource();
        backdrop.setScaleType(ImageView.ScaleType.CENTER_CROP);
        backdrop.setFitsSystemWindows(true);
        addView(backdrop, new LayoutParams(MATCH_PARENT, MATCH_PARENT));
    }

    private void setImageSource() {
        if (params.imageUri != null) {
            backdrop.setImageURI(params.imageUri);
        }
    }

    private void createScrim() {
        scrim = new Scrim(getContext(), params.scrimColor, MAX_HEIGHT / 2);
        addView(scrim);
    }

    public int getCollapsedTopBarHeight() {
        if (topBarHeight > -1) {
            return topBarHeight;
        }
        calculateTopBarHeight();
        return topBarHeight;
    }
    private void calculateTopBarHeight() {
        int[] attrs = new int[] {android.R.attr.actionBarSize};
        TypedArray ta = getContext().obtainStyledAttributes(attrs);
        topBarHeight = ta.getDimensionPixelSize(0, -1);
        ta.recycle();
    }

    public void collapse(float collapse) {
        scrim.collapse(collapse);
    }
}
