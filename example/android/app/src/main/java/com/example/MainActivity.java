package com.example;


import com.reactnativenavigation.activities.RootActivity;

public class MainActivity extends RootActivity {

    @Override
    protected String getMainComponentName() {
        return "App";
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
