package com.reactnativenavigation.mocks

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.drawable.Drawable
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import com.reactnativenavigation.utils.ImageLoader
import com.reactnativenavigation.utils.ImageLoader.ImagesLoadingListener
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.invocation.InvocationOnMock
import java.util.*

object ImageLoaderMock {
    private val mockDrawable: Drawable = object : Drawable() {
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
        val imageLoader = Mockito.mock(ImageLoader::class.java)
        Mockito.doAnswer { invocation: InvocationOnMock ->
            val urlCount = (invocation.arguments[1] as Collection<*>).size
            val drawables = Collections.nCopies(urlCount, mockDrawable)
            (invocation.arguments[2] as ImagesLoadingListener).onComplete(drawables)
            null
        }.`when`(imageLoader).loadIcons(any(), ArgumentMatchers.anyList(), any())

        Mockito.doAnswer { invocation: InvocationOnMock ->
            (invocation.arguments[2] as ImagesLoadingListener).onComplete(mockDrawable)
            null
        }.`when`(imageLoader).loadIcon(any(), any(), any())

        whenever(imageLoader.getBackButtonIcon(any())).thenReturn(backIcon)
        return imageLoader
    }
}