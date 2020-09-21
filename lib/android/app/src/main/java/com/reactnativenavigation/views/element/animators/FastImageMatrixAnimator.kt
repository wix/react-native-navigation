package com.reactnativenavigation.views.element.animators

import android.animation.Animator
import android.animation.ObjectAnimator
import android.graphics.Matrix
import android.view.View
import android.widget.ImageView
import androidx.core.animation.doOnEnd
import com.facebook.react.views.image.ReactImageView
import com.google.android.material.animation.MatrixEvaluator
import com.reactnativenavigation.options.SharedElementTransitionOptions
import com.reactnativenavigation.utils.ViewUtils
import kotlin.math.min
import kotlin.math.roundToInt

class FastImageMatrixAnimator(from: View, to: View) : PropertyAnimatorCreator<ImageView>(from, to) {
    override fun shouldAnimateProperty(fromChild: ImageView, toChild: ImageView): Boolean {
        return !ViewUtils.areDimensionsEqual(from, to)
                && fromChild !is ReactImageView
                && toChild !is ReactImageView
    }

    override fun create(options: SharedElementTransitionOptions): Animator {
        from as ImageView; to as ImageView

        val originalScaleType = to.scaleType
        val originalWidth = to.width
        val originalHeight = to.height

        val fromMatrix = createTransformMatrix(from, to)
        val toMatrix = createTransformMatrix(to, to)
        to.scaleType = ImageView.ScaleType.MATRIX

        return ObjectAnimator.ofObject(
                object : MatrixEvaluator() {
                    override fun evaluate(fraction: Float, startValue: Matrix, endValue: Matrix): Matrix {
                        return super.evaluate(fraction, startValue, endValue).apply {
                            to.imageMatrix = this
                        }
                    }
                },
                fromMatrix,
                toMatrix
        ).apply {
            doOnEnd {
                to.layoutParams.width = originalWidth
                to.layoutParams.height = originalHeight
                to.scaleType = originalScaleType
                to.invalidate()
            }
        }
    }

    private fun createTransformMatrix(from: ImageView, to: ImageView): Matrix = when (from.scaleType) {
        ImageView.ScaleType.CENTER_CROP -> centerCropMatrix(from, to)
        ImageView.ScaleType.FIT_XY -> fitXYMatrix(from)
        ImageView.ScaleType.FIT_CENTER -> fitCenterMatrix(from, to)
        else -> throw RuntimeException("Unsupported ScaleType ${from.scaleType}")
    }

    /**
     * Calculates the image transformation matrix for an ImageView with ScaleType FIT_XY. This
     * needs to be manually calculated as the platform does not give us the value for this case.
     */
    private fun fitXYMatrix(view: ImageView): Matrix {
        val image = view.drawable
        val matrix = Matrix()
        matrix.postScale(
                view.width.toFloat() / image.intrinsicWidth,
                view.height.toFloat() / image.intrinsicHeight)
        return matrix
    }

    /**
     * Calculates the image transformation matrix for an ImageView with ScaleType CENTER_CROP. This
     * needs to be manually calculated for consistent behavior across all the API levels.
     */
    private fun centerCropMatrix(from: ImageView, to: ImageView): Matrix {
        val image = to.drawable
        val imageWidth = image.intrinsicWidth
        val imageViewWidth = from.width
        val scaleX = imageViewWidth.toFloat() / imageWidth

        val imageHeight = image.intrinsicHeight
        val imageViewHeight = from.height
        val scaleY = imageViewHeight.toFloat() / imageHeight

        val maxScale = scaleX.coerceAtLeast(scaleY)
        val width = imageWidth * maxScale
        val height = imageHeight * maxScale
        val tx = ((imageViewWidth - width) / 2f).roundToInt()
        val ty = ((imageViewHeight - height) / 2f).roundToInt()

        return Matrix().apply {
            postScale(maxScale, maxScale)
            postTranslate(tx.toFloat(), ty.toFloat())
        }
    }

    private fun fitCenterMatrix(from: ImageView, to: ImageView): Matrix {
        val image = to.drawable
        val imageWidth = image.intrinsicWidth
        val imageViewWidth = from.width
        val scaleX = imageViewWidth.toFloat() / imageWidth

        val imageHeight = image.intrinsicHeight
        val imageViewHeight = from.height
        val scaleY = imageViewHeight.toFloat() / imageHeight

        val minScale = min(scaleX, scaleY)
        val width = imageWidth * minScale
        val height = imageHeight * minScale
        val tx = ((imageViewWidth - width) / 2f).roundToInt()
        val ty = ((imageViewHeight - height) / 2f).roundToInt()

        return Matrix().apply {
            postScale(minScale, minScale)
            postTranslate(tx.toFloat(), ty.toFloat())
        }
    }
}
