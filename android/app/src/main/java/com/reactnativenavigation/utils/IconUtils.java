package com.reactnativenavigation.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.DisplayMetrics;

import com.reactnativenavigation.BuildConfig;

import java.net.URL;

/**
 * Created by yedidyak on 29/05/2016.
 */
public class IconUtils {
    public static final String LOCAL_RESOURCE_URI_SCHEME = "res";
    private static ResourceDrawableIdHelper sResDrawableIdHelper = new ResourceDrawableIdHelper();

    public static Drawable getIcon(Context ctx, String iconSource) {
        return getIcon(ctx, iconSource, -1);
    }

    /**
     * @param iconSource Icon source. In release builds this would be a path in assets, In debug it's
     *                   a url and the image needs to be decoded from input stream.
     * @param dimensions The requested icon dimensions
     */
    public static Drawable getIcon(Context ctx, String iconSource, int dimensions) {
        if (iconSource == null) {
            return null;
        }

        try {
            Drawable icon;
            Uri iconUri = getIconUri(ctx, iconSource);

            if (LOCAL_RESOURCE_URI_SCHEME.equals(iconUri.getScheme())) {
                icon = sResDrawableIdHelper.getResourceDrawable(ctx, iconSource);
            } else {
                URL url = new URL(iconUri.toString());
                Bitmap bitmap = BitmapFactory.decodeStream(url.openStream());
                bitmap = dimensions > 0 ?
                        Bitmap.createScaledBitmap(bitmap, dimensions, dimensions, false) : bitmap;
                icon = new BitmapDrawable(ctx.getResources(), bitmap);
            }
            return icon;
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static Uri getIconUri(Context context, String iconSource) {
        Uri ret = null;
        if (iconSource != null) {
            try {
                ret = Uri.parse(iconSource);
                // Verify scheme is set, so that relative uri (used by static resources) are not handled.
                if (ret.getScheme() == null) {
                    ret = null;
                }
            } catch (Exception e) {
                // Ignore malformed uri, then attempt to extract resource ID.
            }
            if (ret == null) {
                ret = sResDrawableIdHelper.getResourceDrawableUri(context, iconSource);
            }
        }
        return ret;
    }

}
