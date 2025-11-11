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
import com.reactnativenavigation.utils.areDimensionsWithInheritedScaleEqual
import com.reactnativenavigation.utils.computeInheritedScale
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

class FastImageMatrixAnimator(from: View, to: View) : PropertyAnimatorCreator<ImageView>(from, to) {
    override fun shouldAnimateProperty(fromChild: ImageView, toChild: ImageView): Boolean {
        return !areDimensionsWithInheritedScaleEqual(from, to)
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

        to.layoutParams.width = max(from.width, to.width)
        to.layoutParams.height = max(from.height, to.height)

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
        ImageView.ScaleType.FIT_XY -> fitXYMatrix(from, to)
        ImageView.ScaleType.FIT_CENTER -> fitCenterMatrix(from, to)
        ImageView.ScaleType.CENTER_INSIDE -> fitCenterMatrix(from, to)
        else -> throw RuntimeException("Unsupported ScaleType ${from.scaleType}")
    }

    private fun fitXYMatrix(from: ImageView, to: ImageView): Matrix {
        val (fromScaleX, fromScaleY) = computeInheritedScale(from)
        val image = to.drawable
        val matrix = Matrix()
        matrix.postScale(
                from.width * fromScaleX / image.intrinsicWidth,
                from.height * fromScaleY / image.intrinsicHeight
        )
        return matrix
    }

    private fun centerCropMatrix(from: ImageView, to: ImageView): Matrix {
        val (fromScaleX, fromScaleY) = computeInheritedScale(from)

        val image = to.drawable
        val imageWidth = image.intrinsicWidth
        val imageViewWidth = from.width * fromScaleX
        val scaleX = imageViewWidth / imageWidth

        val imageHeight = image.intrinsicHeight
        val imageViewHeight = from.height * fromScaleY
        val scaleY = imageViewHeight / imageHeight

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
        val (fromScaleX, fromScaleY) = computeInheritedScale(from)

        val image = to.drawable
        val imageWidth = image.intrinsicWidth
        val imageViewWidth = from.width * fromScaleX
        val scaleX = imageViewWidth / imageWidth

        val imageHeight = image.intrinsicHeight
        val imageViewHeight = from.height * fromScaleY
        val scaleY = imageViewHeight  / imageHeight

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
