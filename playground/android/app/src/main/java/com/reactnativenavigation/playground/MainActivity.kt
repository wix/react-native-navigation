package com.reactnativenavigation.playground

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import com.facebook.react.ReactActivity
import com.facebook.react.ReactActivityDelegate
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint.fabricEnabled
import com.facebook.react.defaults.DefaultReactActivityDelegate
import com.reactnativenavigation.NavigationActivity
import com.reactnativenavigation.NavigationApplication

class MainActivity : NavigationActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSplashLayout()
    }

    private fun setSplashLayout() {
        val img = ImageView(this)
        img.setImageDrawable(getDrawable(R.drawable.ic_android))
        setContentView(img)
    }

}
