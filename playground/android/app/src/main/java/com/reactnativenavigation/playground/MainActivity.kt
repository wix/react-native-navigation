package com.reactnativenavigation.playground

import android.os.Bundle
import android.widget.ImageView
import com.reactnativenavigation.NavigationActivity

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
