package com.reactnativenavigation.viewcontrollers.viewcontroller

import android.content.Context
import android.view.View

class NoOpYellowBoxDelegate(context: Context) : YellowBoxDelegate(context) {
    override fun onChildViewAdded(parent: View, child: View?) {}
}