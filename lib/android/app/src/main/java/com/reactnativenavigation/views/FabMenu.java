package com.reactnativenavigation.views;

import android.content.Context;

import com.github.clans.fab.FloatingActionMenu;
import com.reactnativenavigation.anim.FabAnimator;
import com.reactnativenavigation.anim.FabCollapseBehaviour;
import com.reactnativenavigation.interfaces.ScrollEventListener;

import java.util.HashSet;


public class FabMenu extends FloatingActionMenu implements FabAnimator {

    private String id = "";
    private HashSet<Fab> actions = new HashSet<>();

    private FabCollapseBehaviour collapseBehaviour;

    public FabMenu(Context context, String id) {
        super(context);
        this.id = id;
        collapseBehaviour = new FabCollapseBehaviour(this);
        onFinishInflate();
        setOnMenuButtonClickListener(v -> toggle(true));
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

    public String getFabId() {
        return id;
    }
}
