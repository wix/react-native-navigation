package com.reactnativenavigation.viewcontrollers.component;

import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import androidx.viewpager.widget.ViewPager;

import androidx.annotation.NonNull;

public class ComponentPresenterBase {
    public void applyTopInsets(@NonNull View view, int topInsets) {
        if(view.getLayoutParams() instanceof ViewPager.LayoutParams){
            ViewPager parent = (ViewPager) view.getParent();
            MarginLayoutParams lp = (MarginLayoutParams) parent.getLayoutParams();
            if (lp != null && lp.topMargin != topInsets) {
                lp.topMargin = topInsets;
                parent.requestLayout();
            }
        } else {
            MarginLayoutParams lp = (MarginLayoutParams) view.getLayoutParams();
            if (lp != null && lp.topMargin != topInsets) {
                lp.topMargin = topInsets;
                view.requestLayout();
            }
        }
       
    }

    public void applyBottomInset(@NonNull View view, int bottomInset) {
        MarginLayoutParams lp = (MarginLayoutParams) view.getLayoutParams();
        if (lp != null && lp.bottomMargin!= bottomInset) {
            lp.bottomMargin = bottomInset;
            view.requestLayout();
        }
    }
}
