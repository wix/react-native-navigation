package com.reactnativenavigation.activities;

import com.facebook.react.ReactRootView;
import com.reactnativenavigation.core.objects.Screen;

/**
 * This activity is used to start the JS execution where we load our actual app/screens (index.android.js)
 * Triggering react context initialization execute global code before any {@link ReactRootView}
 * are displayed.
 * <p>Only your MainActivity or activities with {@code action.MAIN} and {@code category.LAUNCHER}
 * should extend this activity.
 * Created by guyc on 13/04/16.
 */
public class RootActivity extends BaseReactActivity {

    @Override
    protected void handleOnCreate() {
        // Trigger react context initialization, global javascript code will now execute
        getReactInstanceManager().createReactContextInBackground();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

     // No need to implement stack interface since this activity is only used to start other
    // activities such as TabActivity or SingleScreenActivity.
    @Override
    public Screen getCurrentScreen() {
        return null;
    }

    @Override
    public String getCurrentNavigatorId() {
        return null;
    }

    @Override
    public int getScreenStackSize() {
        return 0;
    }
}
