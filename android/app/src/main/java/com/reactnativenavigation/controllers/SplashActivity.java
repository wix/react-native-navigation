package com.reactnativenavigation.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.reactnativenavigation.react.ReactDevPermission;

public abstract class SplashActivity extends AppCompatActivity {
    public static boolean isResumed = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, NavigationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra("SplashLayout", getSplashLayout());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isResumed = true;

        if (ReactDevPermission.shouldAskPermission()) {
            ReactDevPermission.askPermission(this);
            return;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isResumed = false;
    }

    /**
     * @return xml layout res id
     */
    @LayoutRes
    public int getSplashLayout() {
        return 0;
    }
}
