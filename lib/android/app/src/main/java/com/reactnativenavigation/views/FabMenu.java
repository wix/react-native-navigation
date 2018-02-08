package com.reactnativenavigation.views;

import android.content.Context;
import android.util.Log;
import android.widget.RelativeLayout;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.reactnativenavigation.R;
import com.reactnativenavigation.parse.FabMenuOptions;
import com.reactnativenavigation.parse.FabOptions;

import java.util.HashSet;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.RelativeLayout.ALIGN_PARENT_BOTTOM;
import static android.widget.RelativeLayout.ALIGN_PARENT_RIGHT;
import static com.reactnativenavigation.parse.Options.BooleanOptions.True;


public class FabMenu extends FloatingActionMenu {

    HashSet<Fab> fabs = new HashSet<>();

    public FabMenu(Context context) {
        super(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        layoutParams.addRule(ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(ALIGN_PARENT_RIGHT);
        layoutParams.bottomMargin = (int) context.getResources().getDimension(R.dimen.margin);
        layoutParams.rightMargin = (int) context.getResources().getDimension(R.dimen.margin);
        setLayoutParams(layoutParams);
    }

    public FabMenu(Context context, FabMenuOptions options, OnClickListener clickListener) {
        this(context);
        applyOptions(options, clickListener);

        setOnMenuButtonClickListener(v -> toggle(true));
    }

    public void applyOptions(FabMenuOptions options, OnClickListener clickListener) {
        if (options.hidden == True) {
            hideMenu(true);
        } else {
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

//        for (Fab fabStored : fabs) {
//            removeMenuButton(fabStored);
//        }
//        fabs.clear();
        for (FabOptions fabOption : options.fabsArray) {
            Fab fab = new Fab(getContext(), fabOption);
            fab.setOnClickListener(clickListener);
            fab.setButtonSize(FloatingActionButton.SIZE_MINI);

            fabs.add(fab);
            addMenuButton(fab);
        }
    }

    public void applyIcon(String icon) {
        //can not apply icon for now
//        new ImageLoader().loadIcon(getContext(), icon, new ImageLoader.ImageLoadingListener() {
//            @Override
//            public void onComplete(@NonNull Drawable drawable) {
//                //can not apply icon for now
//            }
//
//            @Override
//            public void onError(Throwable error) {
//                error.printStackTrace();
//            }
//        });
    }
}
