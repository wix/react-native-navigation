package com.reactnativenavigation.react;// Copyright 2004-present Facebook. All Rights Reserved.

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.facebook.common.util.UriUtil;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

/**
 * Direct copy paste from react-native, because they made that class package scope. -_-"
 * Can be deleted in react-native ^0.29
 */
public class ResourceDrawableIdHelper {
    public static final ResourceDrawableIdHelper instance = new ResourceDrawableIdHelper();

    private Map<String, Integer> mResourceDrawableIdMap;

    public ResourceDrawableIdHelper() {
        mResourceDrawableIdMap = new HashMap<>();
    }

    public int getResourceDrawableId(Context context, @Nullable String name) {
        if (name == null || name.isEmpty()) {
            return 0;
        }
        name = name.toLowerCase().replace("-", "_");
        if (mResourceDrawableIdMap.containsKey(name)) {
            return mResourceDrawableIdMap.get(name);
        }
        int id = context.getResources().getIdentifier(
                name,
                "drawable",
                context.getPackageName());
        mResourceDrawableIdMap.put(name, id);
        return id;
    }

    @Nullable
    public Drawable getResourceDrawable(Context context, @Nullable String name) {
        int resId = getResourceDrawableId(context, name);
        return resId > 0 ? context.getResources().getDrawable(resId) : null;
    }

    public Uri getResourceDrawableUri(Context context, @Nullable String name) {
        int resId = getResourceDrawableId(context, name);
        return resId > 0 ? new Uri.Builder()
                .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                .path(String.valueOf(resId))
                .build() : Uri.EMPTY;
    }
}
