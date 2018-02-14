package com.reactnativenavigation.views;

import android.content.Context;
import android.widget.RelativeLayout;

import com.github.clans.fab.FloatingActionMenu;
import com.reactnativenavigation.R;
import com.reactnativenavigation.anim.FabAnimator;
import com.reactnativenavigation.anim.FabCollapseBehaviour;
import com.reactnativenavigation.interfaces.ScrollEventListener;
import com.reactnativenavigation.parse.FabOptions;

import java.util.HashSet;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.RelativeLayout.ALIGN_PARENT_BOTTOM;
import static android.widget.RelativeLayout.ALIGN_PARENT_LEFT;
import static android.widget.RelativeLayout.ALIGN_PARENT_RIGHT;
import static android.widget.RelativeLayout.ALIGN_PARENT_TOP;
import static com.reactnativenavigation.parse.Options.BooleanOptions.False;
import static com.reactnativenavigation.parse.Options.BooleanOptions.True;


public class FabMenu extends FloatingActionMenu implements FabAnimator {

    public interface FabClickListener {
        void onFabClicked(String id);
    }

    private HashSet<Fab> actions = new HashSet<>();

    private FabCollapseBehaviour collapseBehaviour;

    public FabMenu(Context context) {
        super(context);
        collapseBehaviour = new FabCollapseBehaviour(this);
        onFinishInflate();
        setOnMenuButtonClickListener(v -> toggle(true));
    }

    public void applyIcon(String icon) {
        //can not apply icon for now
    }

    @Override
    public void show() {
        showMenu(true);
    }

    @Override
    public void hide() {
        hideMenu(true);
    }

    public void enableCollapse(ScrollEventListener scrollEventListener) {
        collapseBehaviour.enableCollapse(scrollEventListener);
    }

    public void disableCollapse() {
        collapseBehaviour.disableCollapse();
    }

    public HashSet<Fab> getActions() {
        return actions;
    }
}
