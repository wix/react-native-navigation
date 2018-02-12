package com.reactnativenavigation.views;

import android.content.Context;
import android.util.Log;
import android.widget.RelativeLayout;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.reactnativenavigation.R;
import com.reactnativenavigation.anim.FabAnimator;
import com.reactnativenavigation.anim.FabCollapseBehaviour;
import com.reactnativenavigation.interfaces.ScrollEventListener;
import com.reactnativenavigation.parse.FabMenuOptions;
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

    private HashSet<Fab> fabs = new HashSet<>();

    private FabCollapseBehaviour collapseBehaviour;

    public FabMenu(Context context) {
        super(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        layoutParams.addRule(ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(ALIGN_PARENT_RIGHT);
        layoutParams.bottomMargin = (int) context.getResources().getDimension(R.dimen.margin);
        layoutParams.rightMargin = (int) context.getResources().getDimension(R.dimen.margin);
        layoutParams.leftMargin = (int) context.getResources().getDimension(R.dimen.margin);
        layoutParams.topMargin = (int) context.getResources().getDimension(R.dimen.margin);
        setLayoutParams(layoutParams);

        collapseBehaviour = new FabCollapseBehaviour(this);
    }

    public FabMenu(Context context, FabMenuOptions options, FabClickListener clickListener, ScrollEventListener scrollEventListener) {
        this(context);
        onFinishInflate();
        applyOptions(options, clickListener, scrollEventListener);
        setOnMenuButtonClickListener(v -> toggle(true));
    }

    public void applyOptions(FabMenuOptions options, FabClickListener clickListener, ScrollEventListener scrollEventListener) {
        if (options.hidden == True) {
            hideMenu(true);
        }
        if (options.hidden == False) {
            showMenu(true);
        }

        if (options.backgroundColor.hasValue()) {
            setMenuButtonColorNormal(options.backgroundColor.get());
        }
        if (options.clickColor.hasValue()) {
            setMenuButtonColorPressed(options.clickColor.get());
        }
        if (options.rippleColor.hasValue()) {
            setMenuButtonColorRipple(options.rippleColor.get());
        }
        if (options.icon.hasValue()) {
            applyIcon(options.icon.get());
        }

        for (Fab fabStored : fabs) {
            removeMenuButton(fabStored);
        }
        fabs.clear();
        for (FabOptions fabOption : options.fabsArray) {
            Fab fab = new Fab(getContext(), fabOption);
            if (clickListener != null) {
                fab.setOnClickListener(v -> clickListener.onFabClicked(fabOption.id.get()));
            }

            fabs.add(fab);
            addMenuButton(fab);
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
        if (options.hideOnScroll == True) {
            enableCollapse(scrollEventListener);
        }
        if (options.hideOnScroll == False) {
            disableCollapse();
        }
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
}
