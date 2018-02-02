package com.reactnativenavigation.react; // Copyright 2004-present Facebook. All Rights Reserved.

import android.net.Uri;

import com.reactnativenavigation.NavigationApplication;

/**
 * Taken from com.facebook.react.views.imagehelper.ImageSource
 * Can be deleted in react-native ^0.29
 */

public class ResourceDrawableUriHelper {
    public static Uri computeUri(String mSource) {
        try {
            Uri uri = Uri.parse(mSource);
            // Verify scheme is set, so that relative uri (used by static resources) are not handled.
            return uri.getScheme() == null ? computeLocalUri(mSource) : uri;
        } catch (Exception e) {
            return computeLocalUri(mSource);
        }
    }

    private static Uri computeLocalUri(String mSource) {
        return ResourceDrawableIdHelper.instance.getResourceDrawableUri(NavigationApplication.instance, mSource);
    }
}
