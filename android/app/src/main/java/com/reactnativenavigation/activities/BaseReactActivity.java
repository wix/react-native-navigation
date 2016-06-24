package com.reactnativenavigation.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.CallSuper;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.common.logging.FLog;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactPackage;
import com.facebook.react.ReactRootView;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.ReactConstants;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.facebook.react.shell.MainReactPackage;
import com.reactnativenavigation.BuildConfig;
import com.reactnativenavigation.controllers.ModalController;
import com.reactnativenavigation.core.RctManager;
import com.reactnativenavigation.core.objects.Button;
import com.reactnativenavigation.core.objects.Screen;
import com.reactnativenavigation.modal.RnnModal;
import com.reactnativenavigation.packages.RnnPackage;
import com.reactnativenavigation.utils.ContextProvider;
import com.reactnativenavigation.utils.StyleHelper;
import com.reactnativenavigation.views.RnnToolBar;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

/**
 * Base Activity for React Native applications.
 */
public abstract class BaseReactActivity extends AppCompatActivity implements DefaultHardwareBackBtnHandler {

    protected static final String KEY_ANIMATED = "animated";
    protected static final String KEY_BADGE = "badge";
    protected static final String KEY_HIDDEN = "hidden";
    protected static final String KEY_SIDE = "side";
    protected static final String KEY_TAB_INDEX = "tabIndex";
    protected static final String KEY_TITLE = "title";
    protected static final String KEY_TO = "to";
    private static final String TAG = "BaseReactActivity";
    private static final String REDBOX_PERMISSION_MESSAGE =
            "Overlay permissions needs to be granted in order for react native apps to run in dev mode";

    @Nullable
    protected ReactInstanceManager mReactInstanceManager;
    private boolean mDoRefresh = false;
    private Menu mMenu;
    protected RnnToolBar mToolbar;
    protected ActionBarDrawerToggle mDrawerToggle;

    /**
     * Returns the name of the bundle in assets. If this is null, and no file path is specified for
     * the bundle, the app will only work with {@code getUseDeveloperSupport} enabled and will
     * always try to load the JS bundle from the packager server.
     * e.g. "index.android.bundle"
     */
    @Nullable
    public String getBundleAssetName() {
        return "index.android.bundle";
    }

    /**
     * Returns a custom path of the bundle file. This is used in cases the bundle should be loaded
     * from a custom path. By default it is loaded from Android assets, from a path specified
     * by {getBundleAssetName}.
     * e.g. "file://sdcard/myapp_cache/index.android.bundle"
     */
    @Nullable
    public String getJSBundleFile() {
        return null;
    }

    /**
     * Returns the name of the main module. Determines the URL used to fetch the JS bundle
     * from the packager server. It is only used when dev support is enabled.
     * This is the first file to be executed once the {@link ReactInstanceManager} is created.
     * e.g. "index.android"
     */
    public String getJSMainModuleName() {
        return "index.android";
    }

    /**
     * Returns the launchOptions which will be passed to the {@link ReactInstanceManager}
     * when the application is started. By default, this will return null and an empty
     * object will be passed to your top level component as its initial props.
     * If your React Native application requires props set outside of JS, override
     * this method to return the Android.os.Bundle of your desired initial props.
     */
    @Nullable
    protected Bundle getLaunchOptions() {
        return null;
    }

    /**
     * Returns the name of the main component registered from JavaScript.
     * This is used to schedule rendering of the component.
     * e.g. "MoviesApp"
     */
    protected String getMainComponentName() {
        return "";
    }

    /**
     * Returns whether dev mode should be enabled. This enables e.g. the dev menu.
     */
    public boolean getUseDeveloperSupport() {
        return BuildConfig.DEBUG;
    }

    /**
     * Returns a list of {@link ReactPackage} used by the app.
     * You'll most likely want to return at least the {@code MainReactPackage}.
     * If your app uses additional views or modules besides the default ones,
     * you'll want to include more packages here.
     */
    public List<ReactPackage> getPackages() {
        return Arrays.asList(
                new MainReactPackage(),
                new RnnPackage()
        );
    }

    /**
     * A subclass may override this method if it needs to use a custom {@link ReactRootView}.
     */
    protected ReactRootView createRootView() {
        return new ReactRootView(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContextProvider.setActivityContext(this);
        mReactInstanceManager = createReactInstanceManager();
        handleOnCreate();
    }

    /**
     * A subclass may override this method if it needs to use a custom instance.
     */
    protected ReactInstanceManager createReactInstanceManager() {
        return getReactInstanceManager();
    }

    protected ReactInstanceManager getReactInstanceManager() {
        RctManager rctManager = RctManager.getInstance();
        if (!rctManager.isInitialized()) {
            rctManager.init(this);
        }
        return rctManager.getReactInstanceManager();
    }

    protected void handleOnCreate() {
        if (getUseDeveloperSupport() && Build.VERSION.SDK_INT >= 23) {
            // Get permission to show redbox in dev builds.
            if (!Settings.canDrawOverlays(this)) {
                Intent serviceIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                startActivity(serviceIntent);
                FLog.w(ReactConstants.TAG, REDBOX_PERMISSION_MESSAGE);
                Toast.makeText(this, REDBOX_PERMISSION_MESSAGE, Toast.LENGTH_LONG).show();
            }
        }

        ReactRootView mReactRootView = createRootView();
        mReactRootView.startReactApplication(mReactInstanceManager, getMainComponentName(), getLaunchOptions());
        setContentView(mReactRootView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ContextProvider.setActivityContext(this);

        if (mReactInstanceManager != null) {
            mReactInstanceManager.onHostResume(this, this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mReactInstanceManager != null) {
            mReactInstanceManager.onHostPause();
        }

        ContextProvider.clearActivityContext();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Destroy react instance manager only if there are no resumed react activities
        BaseReactActivity activity = ContextProvider.getActivityContext();
        if (mReactInstanceManager != null && (activity == null || activity.isFinishing())) {
            Log.i(TAG, "Destroying ReactInstanceManager");
            mReactInstanceManager.onHostDestroy();
            RctManager.getInstance().onDestroy();
        } else {
            Log.d(TAG, "Not destroying ReactInstanceManager");
        }
    }

    @CallSuper
    public void push(Screen screen) {
        StyleHelper.updateStyles(mToolbar, screen);
        if (mToolbar != null) {
            mToolbar.update(screen);

            if (getCurrentNavigatorId().equals(screen.navigatorId) &&
                getScreenStackSize() >= 1) {
                mToolbar.setNavUpButton(screen);
            }
        }
    }

    @CallSuper
    public Screen pop(String navigatorId) {
        if (mToolbar != null &&
            getCurrentNavigatorId().equals(navigatorId) &&
            getScreenStackSize() <= 2) {
            mToolbar.setNavUpButton();
        }

        return null;
    }

    @CallSuper
    public Screen popToRoot(String navigatorId) {
        if (mToolbar != null) {
            mToolbar.setNavUpButton();
        }

        return null;
    }

    @CallSuper
    public Screen resetTo(Screen screen) {
        StyleHelper.updateStyles(mToolbar, screen);
        if (mToolbar != null) {
            mToolbar.setNavUpButton();
        }

        return null;
    }

    protected abstract String getCurrentNavigatorId();

    @CallSuper
    public Screen getCurrentScreen() {
        ModalController modalController = ModalController.getInstance();
        if (modalController.isModalDisplayed()) {
            RnnModal modal = modalController.get();
            assert modal != null;
            return modal.getCurrentScreen();
        }

        return null;
    }

    public abstract int getScreenStackSize();

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mDrawerToggle != null) {
            mDrawerToggle.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        Screen currentScreen = getCurrentScreen();
        if (mToolbar != null && currentScreen != null) {
            mToolbar.setupToolbarButtonsAsync(currentScreen);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle != null &&
            getScreenStackSize() == 1 &&
            mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else {
            String eventId = Button.getButtonEventId(item);
            assert eventId != null;

            WritableMap params = Arguments.createMap();
            RctManager.getInstance().sendEvent(eventId, getCurrentScreen(), params);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mDrawerToggle != null) {
            mDrawerToggle.syncState();
        }
    }

    public Menu getMenu() {
        return mMenu;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mReactInstanceManager != null) {
            mReactInstanceManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (mReactInstanceManager != null &&
            mReactInstanceManager.getDevSupportManager().getDevSupportEnabled()) {
            if (keyCode == KeyEvent.KEYCODE_MENU) {
                mReactInstanceManager.showDevOptionsDialog();
                return true;
            }
            if (keyCode == KeyEvent.KEYCODE_R && !(getCurrentFocus() instanceof EditText)) {
                // Enable double-tap-R-to-reload
                if (mDoRefresh) {
                    mReactInstanceManager.getDevSupportManager().handleReloadJS();
                    mDoRefresh = false;
                } else {
                    mDoRefresh = true;
                    new Handler().postDelayed(
                            new Runnable() {
                                @Override
                                public void run() {
                                    mDoRefresh = false;
                                }
                            },
                            200);
                }
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (getScreenStackSize() > 1) {
            pop(getCurrentNavigatorId());
        } else {
            if (mReactInstanceManager != null) {
                mReactInstanceManager.onBackPressed();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public void invokeDefaultOnBackPressed() {
        super.onBackPressed();
    }

    public void setNavigationButtons(ReadableMap buttons){
        if (mToolbar == null) {
            return;
        }
        getCurrentScreen().setButtons(buttons);
        mToolbar.setupToolbarButtonsAsync(getCurrentScreen());
    }

    public void setNavigationTitle(ReadableMap title) {
        if (mToolbar == null) {
            return;
        }

        mToolbar.setTitle(title.getString(KEY_TITLE));
    }

    public void toggleNavigationBar(ReadableMap params) {
        if (mToolbar == null) {
            return;
        }

        boolean hide = params.getBoolean(KEY_HIDDEN);
        boolean animated = params.getBoolean(KEY_ANIMATED);
        if (hide) {
            mToolbar.hideToolbar(animated);
        } else {
            mToolbar.showToolbar(animated);
        }
    }

    public void toggleDrawer(ReadableMap params) {
        if (mToolbar == null || mDrawerToggle == null) {
            return;
        }

        boolean animated = params.getBoolean(KEY_ANIMATED);
        String side = params.getString(KEY_SIDE);
        String to = params.getString(KEY_TO);
        switch (to) {
            case "open":
                mToolbar.showDrawer(animated);
                break;
            case "closed":
                mToolbar.hideDrawer(animated);
                break;
            default:
                mToolbar.toggleDrawer(animated);
                break;
        }
    }
}
