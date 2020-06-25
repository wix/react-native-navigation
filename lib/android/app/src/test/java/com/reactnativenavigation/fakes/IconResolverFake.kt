package com.reactnativenavigation.fakes

import android.app.Activity
import com.reactnativenavigation.mocks.ImageLoaderMock
import com.reactnativenavigation.viewcontrollers.button.IconResolver

class IconResolverFake(activity: Activity) : IconResolver(activity, ImageLoaderMock.mock())