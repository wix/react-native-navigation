package com.reactnativenavigation.views.sharedElementTransition;

import android.content.Context;
import android.support.annotation.Keep;
import android.text.SpannableString;
import android.text.SpannedString;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.react.views.image.ReactImageView;
import com.reactnativenavigation.params.parsers.SharedElementTransitionParams;
import com.reactnativenavigation.react.ReactViewHacks;
import com.reactnativenavigation.screens.Screen;
import com.reactnativenavigation.utils.Task;
import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.views.utils.ColorUtils;
import com.reactnativenavigation.views.utils.PathPoint;
import com.reactnativenavigation.views.utils.Point;

public class SharedElementTransition extends FrameLayout {
    public ViewGroup.LayoutParams childLayoutParams;

    public SharedElementTransitionParams showTransitionParams;
    public SharedElementTransitionParams hideTransitionParams;
    private View child;
    private int childLeft;
    private int childTop;
    private int childWidth = -1;
    private int childHeight = -1;
    private int index = 0;
    private SpannableString spannableText;
    private SpannedString spannedText;

    public View getSharedView() {
        return child;
    }

    public SharedElementTransition(Context context) {
        super(context);
        setContentDescription("SET");
    }

    public void registerSharedElementTransition(final String key) {
        ViewUtils.runOnPreDraw(this, new Runnable() {
            @Override
            public void run() {
                ViewUtils.performOnParentScreen(SharedElementTransition.this, new Task<Screen>() {
                    @Override
                    public void run(Screen screen) {
                        screen.registerSharedView(SharedElementTransition.this, key);
                    }
                });
            }
        });
    }

    @Override
    public void onViewAdded(final View child) {
        this.child = child;
        if (child instanceof ReactImageView && index++ % 2 == 0) {
            ReactViewHacks.disableReactImageViewRemoteImageFadeInAnimation((ReactImageView) child);
        }
        if (child instanceof TextView) {
            saveTextViewSpannedText((TextView) child);
        }
        super.onViewAdded(child);
    }

    private void saveTextViewSpannedText(final TextView view) {
        ViewUtils.runOnPreDraw(view, new Runnable() {
            @Override
            public void run() {
                if (spannableText == null) {
                    spannedText = (SpannedString) view.getText();
                }
                if (spannableText == null) {
                    spannableText = new SpannableString(spannedText);
                }
            }
        });
    }

    @Override
    public void onViewRemoved(View child) {
        super.onViewRemoved(child);
    }

    @Keep
    public void setCurvedMotion(PathPoint xy) {
        child.setTranslationX(xy.mX);
        child.setTranslationY(xy.mY);
    }

    @Keep
    public void setTextColor(double[] color) {
        if (child instanceof TextView) {
            ViewUtils.setSpanColor(spannableText, ColorUtils.labToColor(color));
            ((TextView) child).setText(spannableText);
        }
    }

    public void attachChildToScreen() {
        ViewUtils.performOnParentScreen(this, new Task<Screen>() {
            @Override
            public void run(Screen screen) {
                saveChildParams(child);
                Point childLocationInScreen = ViewUtils.getLocationOnScreen(child);

                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(childWidth, childHeight);
                removeView(child);
                lp.leftMargin = childLocationInScreen.x;
                lp.topMargin = childLocationInScreen.y;

                screen.addView(child, lp);
            }

            private void saveChildParams(final View child) {
                childLayoutParams = child.getLayoutParams();
                childLeft = child.getLeft();
                childTop = child.getTop();
                if (childWidth == -1) {
                    childWidth = child.getWidth();
                }
                if (childHeight == -1) {
                    childHeight = child.getHeight();
                }
            }
        });
    }

    public void attachChildToSelf() {
        ((ViewManager) child.getParent()).removeView(child);
        child.setLeft(childLeft);
        child.setTop(childTop);
        restoreTextViewSpannedText();
        addView(child, new LayoutParams(childLayoutParams));
    }

    private void restoreTextViewSpannedText() {
        if (child instanceof TextView) {
            ((TextView) child).setText(spannedText);
            spannedText = null;
            spannableText = null;
        }
    }

    public void hideChild() {
        child.setVisibility(View.INVISIBLE);
    }

    public void showChild() {
        child.post(new Runnable() {
            @Override
            public void run() {
                child.setVisibility(View.VISIBLE);
            }
        });
    }
}
