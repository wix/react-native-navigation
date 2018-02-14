package com.reactnativenavigation.views;

import android.content.Context;

import com.github.clans.fab.FloatingActionMenu;
import com.reactnativenavigation.anim.FabAnimator;
import com.reactnativenavigation.anim.FabCollapseBehaviour;
import com.reactnativenavigation.interfaces.ScrollEventListener;

import java.util.HashSet;


public class FabMenu extends FloatingActionMenu implements FabAnimator {

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
