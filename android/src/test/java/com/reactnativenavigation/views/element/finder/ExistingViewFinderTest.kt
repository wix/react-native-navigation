package com.reactnativenavigation.views.element.finder

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Looper
import android.widget.FrameLayout
import android.widget.ImageView
import com.reactnativenavigation.BaseTest
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewControllerScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.robolectric.Shadows.shadowOf
import java.util.concurrent.TimeUnit

class ExistingViewFinderTest : BaseTest() {
    @Test
    fun acceptsLoadedImagesLargerThanTheirView(): Unit = runBlocking {
        val image = ImageView(newActivity())
        image.layout(0, 0, 100, 100)
        image.setImageDrawable(BitmapDrawable(image.resources, Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888)))
        assertThat(ExistingViewFinder().awaitImage(image)).isSameAs(image)
    }

    @Test
    fun fallsBackWhenTheImageNeverLoads(): Unit = runBlocking {
        val scope = ViewControllerScope()
        try {
            val image = ImageView(newActivity())
            val result = scope.async { ExistingViewFinder().awaitImage(image) }
            shadowOf(Looper.getMainLooper()).idleFor(ExistingViewFinder.IMAGE_LOAD_TIMEOUT_MS + 1, TimeUnit.MILLISECONDS)
            assertThat(result.isCompleted).isTrue()
            assertThat(result.await()).isNull()
            image.viewTreeObserver.dispatchOnPreDraw()
        } finally { scope.cancel() }
    }

    @Test
    fun detachingTheImageCompletesTheWait(): Unit = runBlocking {
        val activity = newActivity()
        val root = FrameLayout(activity)
        val image = ImageView(activity)
        root.addView(image)
        activity.setContentView(root)
        val scope = ViewControllerScope()
        try {
            val result = scope.async { ExistingViewFinder().awaitImage(image) }
            root.removeView(image)
            assertThat(result.isCompleted).isTrue()
            assertThat(result.await()).isNull()
        } finally { scope.cancel() }
    }

    @Test
    fun cancellationDoesNotResumeOnALaterDraw() {
        val scope = ViewControllerScope()
        val image = ImageView(newActivity())
        val result = scope.async { ExistingViewFinder().awaitImage(image) }
        scope.cancel()
        image.layout(0, 0, 100, 100)
        image.setImageDrawable(BitmapDrawable(image.resources, Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888)))
        image.viewTreeObserver.dispatchOnPreDraw()
        assertThat(result.isCancelled).isTrue()
    }
}
