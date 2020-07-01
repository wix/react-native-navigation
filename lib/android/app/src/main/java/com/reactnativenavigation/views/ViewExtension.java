package com.reactnativenavigation.views;

import android.util.Property;
import android.view.View;

public class ViewExtension {
    public static class HeightProperty extends Property<View, Float> {

        public HeightProperty() {
            super(Float.class, "height");
        }

        @Override
        public Float get(View view) {
            return (float) view.getHeight();
        }

        @Override
        public void set(View view, Float value) {
            view.getLayoutParams().height = value.intValue();
            view.setLayoutParams(view.getLayoutParams());
        }
    }

    public static class WidthProperty extends Property<View, Float> {

        public WidthProperty() {
            super(Float.class, "width");
        }

        @Override
        public Float get(View view) {
            return (float) view.getWidth();
        }

        @Override
        public void set(View view, Float value) {
            view.getLayoutParams().width = value.intValue();
            view.setLayoutParams(view.getLayoutParams());
        }
    }

    public static float getWidth(View view) {
        return view.getWidth();
    }

    public static float getHeight(View view) {
        return view.getHeight();
    }

    public static final Property<View, Float> HEIGHT = new HeightProperty();
    public static final Property<View, Float> WIDTH = new WidthProperty();
}
