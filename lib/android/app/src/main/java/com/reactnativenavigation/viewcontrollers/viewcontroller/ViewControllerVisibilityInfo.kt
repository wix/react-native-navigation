package com.reactnativenavigation.viewcontrollers.viewcontroller

data class TopBarVisibilityInfo(val isShown: Boolean, val solidColor: Int?)

data class StatusBarVisibilityInfo(val isShown: Boolean, val solidColor: Int?, val isTranslucent: Boolean)

data class ViewControllerVisibilityInfo(val topBarVisibilityInfo: TopBarVisibilityInfo?, val statusBarVisibilityInfo: StatusBarVisibilityInfo?)
