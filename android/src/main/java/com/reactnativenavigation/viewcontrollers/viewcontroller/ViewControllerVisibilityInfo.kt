package com.reactnativenavigation.viewcontrollers.viewcontroller

data class TopBarVisibilityInfo(val isShown: Boolean, val solidColor: Int?)

data class ViewControllerVisibilityInfo(val topBarVisibilityInfo: TopBarVisibilityInfo?)
