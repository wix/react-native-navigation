package com.reactnativenavigation.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.widget.RelativeLayout;

import com.github.clans.fab.FloatingActionButton;
import com.reactnativenavigation.R;
import com.reactnativenavigation.anim.FabAnimator;
import com.reactnativenavigation.anim.FabCollapseBehaviour;
import com.reactnativenavigation.interfaces.ScrollEventListener;
import com.reactnativenavigation.parse.FabOptions;
import com.reactnativenavigation.utils.ImageLoader;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.RelativeLayout.ALIGN_PARENT_BOTTOM;
import static android.widget.RelativeLayout.ALIGN_PARENT_LEFT;
import static android.widget.RelativeLayout.ALIGN_PARENT_RIGHT;
import static android.widget.RelativeLayout.ALIGN_PARENT_TOP;
import static com.reactnativenavigation.parse.Options.BooleanOptions.False;
import static com.reactnativenavigation.parse.Options.BooleanOptions.True;


public class Fab extends FloatingActionButton implements FabAnimator {

    private String id = "";
    private FabCollapseBehaviour collapseBehaviour;

    public Fab(Context context, String id) {
        super(context);
        collapseBehaviour = new FabCollapseBehaviour(this);
        this.id = id;
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

    @Override
    public void show() {
        show(true);
    }

    @Override
    public void hide() {
        hide(true);
    }

    public void enableCollapse(@NonNull ScrollEventListener scrollEventListener) {
        collapseBehaviour.enableCollapse(scrollEventListener);
    }

    public void disableCollapse() {
        collapseBehaviour.disableCollapse();
    }
}
