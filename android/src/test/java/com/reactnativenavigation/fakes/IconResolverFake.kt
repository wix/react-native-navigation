package com.reactnativenavigation.fakes

import android.app.Activity
import com.reactnativenavigation.mocks.ImageLoaderMock
import com.reactnativenavigation.utils.ImageLoader
import com.reactnativenavigation.viewcontrollers.stack.topbar.button.IconResolver

class IconResolverFake(activity: Activity, imageLoader: ImageLoader = ImageLoaderMock.mock()) : IconResolver(activity, imageLoader)