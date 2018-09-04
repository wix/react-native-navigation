package com.reactnativenavigation.playground;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.reactnativenavigation.NavigationActivity;

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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        dismissSystemAlertsToPreventDetoxFromTimingOut(hasFocus);
    }

    private void dismissSystemAlertsToPreventDetoxFromTimingOut(boolean hasFocus) {
        if (! hasFocus) {
            sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        }
    }
}
