package com.reactnativenavigation.controllers.viewcontroller

import android.content.Context
import android.view.View

class NoOpYellowBoxDelegate(context: Context) : YellowBoxDelegate(context) {
    override fun onChildViewAdded(parent: View, child: View?) {}
}