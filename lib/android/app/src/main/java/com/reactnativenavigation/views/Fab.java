package com.reactnativenavigation.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.widget.RelativeLayout;

import com.github.clans.fab.FloatingActionButton;
import com.reactnativenavigation.R;
import com.reactnativenavigation.parse.FabOptions;
import com.reactnativenavigation.utils.ImageLoader;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.RelativeLayout.ALIGN_PARENT_BOTTOM;
import static android.widget.RelativeLayout.ALIGN_PARENT_LEFT;
import static android.widget.RelativeLayout.ALIGN_PARENT_RIGHT;
import static android.widget.RelativeLayout.ALIGN_PARENT_TOP;
import static com.reactnativenavigation.parse.Options.BooleanOptions.False;
import static com.reactnativenavigation.parse.Options.BooleanOptions.True;


public class Fab extends FloatingActionButton {

    private String id = "";

    public Fab(Context context) {
        super(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        layoutParams.addRule(ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(ALIGN_PARENT_RIGHT);
        layoutParams.bottomMargin = (int) context.getResources().getDimension(R.dimen.margin);
        layoutParams.rightMargin = (int) context.getResources().getDimension(R.dimen.margin);
        setLayoutParams(layoutParams);
    }

    public Fab(Context context, FabOptions options) {
        this(context);
        applyOptions(options);
    }

    public void applyOptions(FabOptions options) {
        id = options.id.get();
        if (options.hidden == True) {
            hide(true);
        }
        if (options.hidden == False) {
            show(true);
        }
        if (options.backgroundColor.hasValue()) {
            setColorNormal(options.backgroundColor.get());
        }
        if (options.clickColor.hasValue()) {
            setColorPressed(options.clickColor.get());
        }
        if (options.rippleColor.hasValue()) {
            setColorRipple(options.rippleColor.get());
        }
        if (options.icon.hasValue()) {
            applyIcon(options.icon.get());
        }
        if (options.alignHorizontally.hasValue()) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
            if ("right".equals(options.alignHorizontally.get())) {
                layoutParams.removeRule(ALIGN_PARENT_LEFT);
                layoutParams.addRule(ALIGN_PARENT_RIGHT);
            }
            if ("left".equals(options.alignHorizontally.get())) {
                layoutParams.removeRule(ALIGN_PARENT_RIGHT);
                layoutParams.addRule(ALIGN_PARENT_LEFT);
            }
            setLayoutParams(layoutParams);
        }
        if (options.alignVertically.hasValue()) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
            if ("top".equals(options.alignVertically.get())) {
                layoutParams.removeRule(ALIGN_PARENT_BOTTOM);
                layoutParams.addRule(ALIGN_PARENT_TOP);
            }
            if ("bottom".equals(options.alignVertically.get())) {
                layoutParams.removeRule(ALIGN_PARENT_TOP);
                layoutParams.addRule(ALIGN_PARENT_BOTTOM);
            }
            setLayoutParams(layoutParams);
        }
        if (options.size.hasValue()) {
            setButtonSize("mini".equals(options.size.get()) ? SIZE_MINI : SIZE_NORMAL);
        }
    }

    public void applyIcon(String icon) {
        new ImageLoader().loadIcon(getContext(), icon, new ImageLoader.ImageLoadingListener() {
            @Override
            public void onComplete(@NonNull Drawable drawable) {
                setImageDrawable(drawable);
            }

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        });
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Fab fab = (Fab) o;

        return id.equals(fab.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
