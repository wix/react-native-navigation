package com.reactnativenavigation.modal;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.reactnativenavigation.R;
import com.reactnativenavigation.activities.BaseReactActivity;
import com.reactnativenavigation.controllers.ModalController;
import com.reactnativenavigation.core.objects.Screen;
import com.reactnativenavigation.utils.SdkSupports;
import com.reactnativenavigation.utils.StyleHelper;
import com.reactnativenavigation.views.RctView;
import com.reactnativenavigation.views.RnnToolBar;
import com.reactnativenavigation.views.ScreenStack;

/**
 * Created by guyc on 02/05/16.
 */
public class RnnModal extends Dialog implements DialogInterface.OnDismissListener {

    private ScreenStack mScreenStack;
    private View mContentView;
    private Screen mOriginalScreen;
    private Screen mScreen;
    private RnnToolBar mToolBar;
    private BaseReactActivity mContext;

    public RnnModal(BaseReactActivity context, Screen screen) {
        super(context, R.style.Modal);
        mScreen = screen;
        mOriginalScreen = context.getCurrentScreen();
        mContext = context;
        ModalController.getInstance().add(this);
        init(context);
    }

    private void updateToolbar(Screen screen) {
        mToolBar.setTitle(screen.title);
        mContext.setSupportActionBar(mToolBar);
        mToolBar.setStyle(screen);
        mToolBar.setupToolbarButtonsAsync(screen);
    }

    @SuppressLint("InflateParams")
    private void init(final Context context) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContentView = LayoutInflater.from(context).inflate(R.layout.modal_layout, null, false);
        mToolBar = (RnnToolBar) mContentView.findViewById(R.id.toolbar);
        mScreenStack = (ScreenStack) mContentView.findViewById(R.id.screenStack);
        setContentView(mContentView);
        updateToolbar(mScreen);
        mScreenStack.push(mScreen, new RctView.OnDisplayedListener() {
            @Override
            public void onDisplayed() {
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_up);
                mContentView.setAnimation(animation);
                mContentView.animate();
            }
        });

        // Set navigation colors
        if (SdkSupports.lollipop()) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            StyleHelper.setWindowStyle(window, context.getApplicationContext(), mScreen);
        }
    }

    public void push(Screen screen) {
        mScreenStack.push(screen);
    }

    public Screen pop() {
        return mScreenStack.pop();
    }

    @Override
    public void onBackPressed() {
        if (mScreenStack.getStackSize() > 1) {
            mScreenStack.pop();
        } else {
            ModalController.getInstance().remove();
            super.onBackPressed();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        ModalController.getInstance().remove();
    }

    @Override
    public void dismiss(){
        super.dismiss();
        if (mContext != null && !mContext.isFinishing()) {
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateToolbar(mOriginalScreen);
                }
            });
        }
    }
}
