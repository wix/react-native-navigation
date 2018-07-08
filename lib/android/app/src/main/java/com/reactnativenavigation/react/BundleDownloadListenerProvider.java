package com.reactnativenavigation.react;

import com.facebook.react.devsupport.interfaces.DevBundleDownloadListener;

public interface BundleDownloadListenerProvider {
    void setBundleLoaderListener(DevBundleDownloadListener listener);
}
