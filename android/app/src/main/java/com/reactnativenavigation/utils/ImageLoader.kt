package com.reactnativenavigation.utils

import android.app.Activity
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.StrictMode
import androidx.core.content.ContextCompat
import com.facebook.react.views.imagehelper.ResourceDrawableIdHelper
import com.reactnativenavigation.R
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.util.*

open class ImageLoader {
    interface ImagesLoadingListener {
        fun onComplete(drawable: List<Drawable>)
        fun onComplete(drawable: Drawable)
        fun onError(error: Throwable?)
    }

    open fun getBackButtonIcon(context: Activity): Drawable? {
        val isRTL = context.window.decorView.isRTL()
        return ContextCompat.getDrawable(context, if (isRTL) R.drawable.ic_arrow_back_black_rtl_24dp else R.drawable.ic_arrow_back_black_24dp)
    }

    open fun loadIcon(context: Context, uri: String?): Drawable? {
        if (uri == null) return null
        try {
            return getDrawable(context, uri)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    open fun loadIcon(context: Context, uri: String, listener: ImagesLoadingListener) {
        try {
            listener.onComplete(getDrawable(context, uri))
        } catch (e: IOException) {
            listener.onError(e)
        }
    }

    open fun loadIcons(context: Context, uris: List<String>, listener: ImagesLoadingListener) {
        try {
            val drawables: MutableList<Drawable> = ArrayList()
            for (uri in uris) {
                val drawable = getDrawable(context, uri)
                drawables.add(drawable)
            }
            listener.onComplete(drawables)
        } catch (e: IOException) {
            listener.onError(e)
        }
    }

    @Throws(IOException::class)
    private fun getDrawable(context: Context, source: String): Drawable {
        var drawable: Drawable?
        if (isLocalFile(Uri.parse(source))) {
            drawable = loadFile(context, source)
        } else {
            drawable = loadResource(context, source)
            if (drawable == null && context.isDebug()) {
                drawable = readJsDevImage(context, source)
            }
        }
        if (drawable == null) throw RuntimeException("Could not load image $source")
        return drawable.mutate()
    }

    @Throws(IOException::class)
    private fun readJsDevImage(context: Context, source: String): Drawable {
        val threadPolicy = adjustThreadPolicyDebug(context)
        val `is` = openStream(context, source)
        val bitmap = BitmapFactory.decodeStream(`is`)
        restoreThreadPolicyDebug(context, threadPolicy)
        return BitmapDrawable(context.resources, bitmap)
    }

    private fun isLocalFile(uri: Uri): Boolean {
        return FILE_SCHEME == uri.scheme
    }

    private fun loadFile(context: Context, uri: String): Drawable {
        val bitmap = BitmapFactory.decodeFile(Uri.parse(uri).path)
        return BitmapDrawable(context.resources, bitmap)
    }

    private fun adjustThreadPolicyDebug(context: Context): StrictMode.ThreadPolicy? {
        var threadPolicy: StrictMode.ThreadPolicy? = null
        if (context.isDebug()) {
            threadPolicy = StrictMode.getThreadPolicy()
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitNetwork().build())
        }
        return threadPolicy
    }

    private fun restoreThreadPolicyDebug(context: Context, threadPolicy: StrictMode.ThreadPolicy?) {
        if (context.isDebug() && threadPolicy != null) {
            StrictMode.setThreadPolicy(threadPolicy)
        }
    }

    companion object {
        private const val FILE_SCHEME = "file"
        private fun loadResource(context: Context, iconSource: String): Drawable? {
            return ResourceDrawableIdHelper.getInstance().getResourceDrawable(context, iconSource)
        }

        @Throws(IOException::class)
        private fun openStream(context: Context, uri: String): InputStream? {
            return if (uri.contains("http")) remoteUrl(uri) else localFile(context, uri)
        }

        @Throws(IOException::class)
        private fun remoteUrl(uri: String): InputStream {
            return URL(uri).openStream()
        }

        @Throws(FileNotFoundException::class)
        private fun localFile(context: Context, uri: String): InputStream? {
            return context.contentResolver.openInputStream(Uri.parse(uri))
        }
    }
}