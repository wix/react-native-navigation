package com.reactnativenavigation.utils

import android.os.StrictMode
import com.reactnativenavigation.BaseTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock

class ImageLoaderTest : BaseTest() {
    @Test
    fun failedDevImageLoadRestoresThreadPolicy() {
        val originalPolicy = StrictMode.ThreadPolicy.Builder()
            .detectNetwork()
            .penaltyLog()
            .build()
        StrictMode.setThreadPolicy(originalPolicy)

        ImageLoader().loadIcon(
            newActivity(),
            "content://missing/image",
            mock<ImageLoader.ImagesLoadingListener>()
        )

        assertEquals(originalPolicy.toString(), StrictMode.getThreadPolicy().toString())
    }
}
