package com.reactnativenavigation.playground;

import android.os.Bundle;
import android.widget.ImageView;

import com.reactnativenavigation.NavigationActivity;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class MainActivity extends NavigationActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSplashLayout();
    }

    private void setSplashLayout() {
        ImageView img = new ImageView(this);
        img.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_android));
        setContentView(img);
    }
}
