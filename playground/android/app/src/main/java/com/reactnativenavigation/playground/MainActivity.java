package com.reactnativenavigation.playground;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.facebook.react.ReactActivityDelegate;
import com.facebook.react.ReactRootView;

import com.reactnativenavigation.NavigationActivity;

import androidx.annotation.Nullable;

public class MainActivity extends NavigationActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSplashLayout();
    }

    private void setSplashLayout() {
        ImageView img = new ImageView(this);
        img.setImageDrawable(getDrawable(R.drawable.ic_android));
        setContentView(img);
    }
}
