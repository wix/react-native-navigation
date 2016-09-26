package com.exampleredux;

import android.graphics.Color;
import android.view.View;

import com.reactnativenavigation.controllers.SplashActivity;

public class MainActivity extends SplashActivity {

    @Override
    public View createSplashLayout() {
        View view = new View(this);
        view.setBackgroundColor(Color.BLUE);
        return view;
    }
}
