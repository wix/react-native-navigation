package com.reactnativenavigation.fakes

import android.app.Activity
import com.reactnativenavigation.mocks.ImageLoaderMock
import com.reactnativenavigation.controllers.stack.topbar.button.IconResolver

class IconResolverFake(activity: Activity) : IconResolver(activity, ImageLoaderMock.mock())