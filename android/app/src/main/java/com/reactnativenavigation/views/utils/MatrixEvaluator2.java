package com.reactnativenavigation.views.utils;

import android.animation.TypeEvaluator;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.Property;
import android.widget.ImageView;

import com.reactnativenavigation.views.sharedElementTransition.AnimatorValuesResolver;

public class MatrixEvaluator2 implements TypeEvaluator<Matrix> {
    private static final String TAG = "MatrixEvaluator";
    private AnimatorValuesResolver resolver;

    public MatrixEvaluator2(AnimatorValuesResolver resolver) {
        this.resolver = resolver;
    }

    public static TypeEvaluator<Matrix> NULL_MATRIX_EVALUATOR = new TypeEvaluator<Matrix>() {
        @Override
        public Matrix evaluate(float fraction, Matrix startValue, Matrix endValue) {
            return null;
        }
    };

    /**
     * This property is passed to ObjectAnimator when we are animating image matrix of ImageView
     */
    public static final Property<ImageView, Matrix> ANIMATED_TRANSFORM_PROPERTY = new Property<ImageView, Matrix>(Matrix.class, "animatedTransform") {
        @Override
        public void set(ImageView imageView, Matrix matrix) {
            Drawable drawable = imageView.getDrawable();
            if (drawable == null) {
                return;
            }
            if (matrix == null) {
                drawable.setBounds(0, 0, imageView.getWidth(), imageView.getHeight());
            } else {
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                Matrix drawMatrix = imageView.getImageMatrix();
                if (drawMatrix == null) {
                    drawMatrix = new Matrix();
                    imageView.setImageMatrix(drawMatrix);
                }
                imageView.setImageMatrix(matrix);
            }
            imageView.invalidate();
        }

        @Override
        public Matrix get(ImageView object) {
            return null;
        }
    };

    private float[] mTempStartValues = new float[9];

    private float[] mTempEndValues = new float[9];

    private Matrix mTempMatrix = new Matrix();

    @Override
    public Matrix evaluate(float fraction, Matrix startValue, Matrix endValue) {
        Log.i(TAG, "evaluate: " + fraction);
        startValue.getValues(mTempStartValues);
        endValue.getValues(mTempEndValues);
        for (int i = 0; i < 9; i++) {
            float diff = mTempEndValues[i] - mTempStartValues[i];
            mTempEndValues[i] = mTempStartValues[i] + (fraction * diff);
        }
        mTempMatrix.setValues(mTempEndValues);

        return mTempMatrix;
    }
}
