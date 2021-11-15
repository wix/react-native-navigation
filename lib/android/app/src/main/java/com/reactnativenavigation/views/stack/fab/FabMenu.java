package com.reactnativenavigation.views.stack.fab;

import android.annotation.SuppressLint;
import android.content.Context;

import com.github.clans.fab.FloatingActionMenu;
import com.reactnativenavigation.viewcontrollers.stack.FabAnimator;
import com.reactnativenavigation.viewcontrollers.stack.FabCollapseBehaviour;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ScrollEventListener;

import java.util.HashSet;


@SuppressLint("ViewConstructor")
public class FabMenu extends FloatingActionMenu implements FabAnimator {

    private final String id;
    private final HashSet<Fab> actions = new HashSet<>();

    private final FabCollapseBehaviour collapseBehaviour;

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
