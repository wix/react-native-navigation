package com.reactnativenavigation.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import com.reactnativenavigation.R;
import com.reactnativenavigation.params.LightBoxParams;
import com.reactnativenavigation.screens.Screen;
import com.reactnativenavigation.utils.ViewUtils;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class LightBox extends Dialog implements DialogInterface.OnDismissListener {

    private Runnable onDismissListener;
    private ContentView content;
    private RelativeLayout lightBox;
    private boolean cancelable;

    public LightBox(AppCompatActivity activity, Runnable onDismissListener, LightBoxParams params) {
        super(activity, R.style.LightBox);
        this.onDismissListener = onDismissListener;
        this.cancelable =!params.overrideBackPress; 
        setOnDismissListener(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        createContent(activity, params);
        setCancelable(cancelable);
        getWindow().setWindowAnimations(android.R.style.Animation);
        getWindow().setSoftInputMode(params.adjustSoftInput);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    private void createContent(final Context context, LightBoxParams params) {
        lightBox = new RelativeLayout(context);
        lightBox.setAlpha(0);
        content = new ContentView(context, params.screenId, params.navigationParams);
        content.setAlpha(0);
        content.setId(ViewUtils.generateViewId());
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT, content.getId());
        lightBox.setBackgroundColor(params.backgroundColor.getColor());
        lightBox.addView(content, lp);

        if (params.tapBackgroundToDismiss) {
            lightBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hide();
                }
            });
        }

        content.setOnDisplayListener(new Screen.OnDisplayListener() {
            @Override
            public void onDisplay() {
                content.getLayoutParams().height = content.getChildAt(0).getHeight();
                content.getLayoutParams().width = content.getChildAt(0).getWidth();
                content.setBackgroundColor(Color.TRANSPARENT);
                ViewUtils.runOnPreDraw(content, new Runnable() {
                    @Override
                    public void run() {
                        animateShow();

                    }
                });
            }
        });
        setContentView(lightBox, new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT));
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void hide() {
        animateHide();
    }

    @Override public void onBackPressed() {
        if (cancelable) {
            hide();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        onDismissListener.run();
    }

    public void destroy() {
        // content.unmountReactView();
        dismiss();
    }

    private void animateShow() {
        ObjectAnimator yTranslation = ObjectAnimator.ofFloat(content, View.TRANSLATION_Y, 80, 0).setDuration(400);
        yTranslation.setInterpolator(new FastOutSlowInInterpolator());
        yTranslation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                content.setAlpha(1);
            }
        });

        ObjectAnimator lightBoxAlpha = ObjectAnimator.ofFloat(lightBox, View.ALPHA, 0, 1).setDuration(70);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(lightBoxAlpha, yTranslation);
        animatorSet.start();
    }

    private void animateHide() {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(content, View.ALPHA, 0);
        ObjectAnimator yTranslation = ObjectAnimator.ofFloat(content, View.TRANSLATION_Y, 60);
        AnimatorSet contentAnimators = new AnimatorSet();
        contentAnimators.playTogether(alpha, yTranslation);
        contentAnimators.setDuration(150);

        ObjectAnimator lightBoxAlpha = ObjectAnimator.ofFloat(lightBox, View.ALPHA, 0).setDuration(100);

        AnimatorSet allAnimators = new AnimatorSet();
        allAnimators.playSequentially(contentAnimators, lightBoxAlpha);
        allAnimators.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                destroy();
            }
        });
        allAnimators.start();
    }
}
