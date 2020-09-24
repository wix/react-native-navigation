package com.reactnativenavigation.playground;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.reactnativenavigation.NavigationActivity;

import androidx.annotation.Nullable;

public class MainActivity extends NavigationActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSplashLayout();
    }

    private void setSplashLayout() {
        View splash = LayoutInflater.from(this).inflate(R.layout.splash, null);
        splash.setId(R.id.splash_view);
        splash.findViewById(R.id.rnnLogo).setTag(com.reactnativenavigation.R.id.nativeId, "rnnLogo");
        setContentView(splash);
    }
}
