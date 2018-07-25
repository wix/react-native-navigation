package com.reactnativenavigation.views.element;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.FrameLayout;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.DraweeView;

public class Element extends FrameLayout {
    private String elementId;

    public Element(@NonNull Context context) {
        super(context);
    }

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    public View getChild() {
        return getChildAt(0);
    }

    @Keep
    public void setClipBounds(Rect clipBounds) {
        getChild().setClipBounds(clipBounds);
    }

    @Keep
    public void setMatrixTransform(float value) {
        GenericDraweeHierarchy hierarchy = ((DraweeView<GenericDraweeHierarchy>) getChild()).getHierarchy();
        if (hierarchy.getActualImageScaleType() != null) {
            ((ScalingUtils.InterpolatingScaleType) hierarchy.getActualImageScaleType()).setValue(value);
            getChild().invalidate();
        }
    }
}
