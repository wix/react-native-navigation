package com.reactnativenavigation.mocks

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.drawable.Drawable

import com.reactnativenavigation.utils.ImageLoader
import com.reactnativenavigation.utils.ImageLoader.ImagesLoadingListener
import org.mockito.Mockito.doAnswer
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.*

object ImageLoaderMock {
    val mockDrawable: Drawable = object : Drawable() {
        override fun draw(canvas: Canvas) {}
        override fun setAlpha(alpha: Int) {}
        override fun setColorFilter(colorFilter: ColorFilter?) {}
        override fun getOpacity(): Int {
            return 0
        }
    }
    private val backIcon: Drawable = BackDrawable()

    @JvmStatic
    fun mock(): ImageLoader {
        return this.mock(mockDrawable)
    }

    @JvmStatic
    fun mock(returnDrawable: Drawable = mockDrawable): ImageLoader {
        val imageLoader = mock<ImageLoader>()
        doAnswer { invocation ->
            val urlCount = (invocation.arguments[1] as Collection<*>).size
            val drawables = Collections.nCopies(urlCount, returnDrawable)
            (invocation.arguments[2] as ImagesLoadingListener).onComplete(drawables)
            null
        }.`when`(imageLoader).loadIcons(any(), any(), any())

        doAnswer { invocation ->
            (invocation.arguments[2] as ImagesLoadingListener).onComplete(returnDrawable)
            null
        }.`when`(imageLoader).loadIcon(any(), any(), any())

        whenever(imageLoader.getBackButtonIcon(any())).thenReturn(backIcon)
        return imageLoader
    }
}