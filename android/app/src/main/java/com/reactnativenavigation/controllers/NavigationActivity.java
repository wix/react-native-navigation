package com.reactnativenavigation.controllers;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Window;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.facebook.react.modules.core.PermissionAwareActivity;
import com.facebook.react.modules.core.PermissionListener;
import com.reactnativenavigation.NavigationApplication;
import com.reactnativenavigation.events.Event;
import com.reactnativenavigation.events.EventBus;
import com.reactnativenavigation.events.JsDevReloadEvent;
import com.reactnativenavigation.events.ModalDismissedEvent;
import com.reactnativenavigation.events.Subscriber;
import com.reactnativenavigation.layouts.BottomTabsLayout;
import com.reactnativenavigation.layouts.Layout;
import com.reactnativenavigation.layouts.LayoutFactory;
import com.reactnativenavigation.params.ActivityParams;
import com.reactnativenavigation.params.AppStyle;
import com.reactnativenavigation.params.ContextualMenuParams;
import com.reactnativenavigation.params.FabParams;
import com.reactnativenavigation.params.LightBoxParams;
import com.reactnativenavigation.params.ScreenParams;
import com.reactnativenavigation.params.SlidingOverlayParams;
import com.reactnativenavigation.params.SnackbarParams;
import com.reactnativenavigation.params.TitleBarButtonParams;
import com.reactnativenavigation.params.TitleBarLeftButtonParams;
import com.reactnativenavigation.react.ReactGateway;
import com.reactnativenavigation.screens.NavigationType;
import com.reactnativenavigation.screens.Screen;
import com.reactnativenavigation.utils.OrientationHelper;
import com.reactnativenavigation.views.SideMenu.Side;

import java.util.List;

public class NavigationActivity extends AppCompatActivity implements DefaultHardwareBackBtnHandler, Subscriber, PermissionAwareActivity {

    /**
     * Although we start multiple activities, we make sure to pass Intent.CLEAR_TASK | Intent.NEW_TASK
     * So that we actually have only 1 instance of the activity running at one time.
     * We hold the currentActivity (resume->pause) so we know when we need to destroy the javascript context
     * (when currentActivity is null, ie pause and destroy was called without resume).
     * This is somewhat weird, and in the future we better use a single activity with changing contentView similar to ReactNative impl.
     * Along with that, we should handle commands from the bridge using onNewIntent
     */
    static NavigationActivity currentActivity;

    private ActivityParams activityParams;
    private ModalController modalController;
    private Layout layout;
    @Nullable private PermissionListener mPermissionListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!NavigationApplication.instance.getReactGateway().hasStartedCreatingContext()) {
            SplashActivity.start(this);
            finish();
            return;
        }

        activityParams = NavigationCommandsHandler.parseActivityParams(getIntent());
        disableActivityShowAnimationIfNeeded();
        setOrientation();
        createModalController();
        createLayout();
        NavigationApplication.instance.getActivityCallbacks().onActivityCreated(this, savedInstanceState);
    }

    private void setOrientation() {
        OrientationHelper.setOrientation(this, AppStyle.appStyle.orientation);
    }

    private void disableActivityShowAnimationIfNeeded() {
        if (!activityParams.animateShow) {
            overridePendingTransition(0, 0);
        }
    }

    private void createModalController() {
        modalController = new ModalController(this);
    }

    private void createLayout() {
        layout = LayoutFactory.create(this, activityParams);
        if (hasBackgroundColor()) {
            layout.asView().setBackgroundColor(AppStyle.appStyle.screenBackgroundColor.getColor());
        }
        setContentView(layout.asView());
    }

    private boolean hasBackgroundColor() {
        return AppStyle.appStyle.screenBackgroundColor != null &&
               AppStyle.appStyle.screenBackgroundColor.hasColor();
    }

    @Override
    protected void onStart() {
        super.onStart();
        NavigationApplication.instance.getActivityCallbacks().onActivityStarted(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFinishing() || !NavigationApplication.instance.isReactContextInitialized()) {
            return;
        }

        currentActivity = this;
        IntentDataHandler.onResume(getIntent());
        getReactGateway().onResumeActivity(this, this);
        NavigationApplication.instance.getActivityCallbacks().onActivityResumed(this);
        EventBus.instance.register(this);
        IntentDataHandler.onPostResume(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getReactGateway().onNewIntent(intent);
        NavigationApplication.instance.getActivityCallbacks().onNewIntent(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        currentActivity = null;
        IntentDataHandler.onPause(getIntent());
        getReactGateway().onPauseActivity(this);
        NavigationApplication.instance.getActivityCallbacks().onActivityPaused(this);
        EventBus.instance.unregister(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        NavigationApplication.instance.getActivityCallbacks().onActivityStopped(this);
    }

    @Override
    protected void onDestroy() {
        destroyLayouts();
        destroyJsIfNeeded();
        NavigationApplication.instance.getActivityCallbacks().onActivityDestroyed(this);
        super.onDestroy();
    }

    private void destroyLayouts() {
        if (modalController != null) {
            modalController.destroy();
        }
        if (layout != null) {
            layout.destroy();
            layout = null;
        }
    }

    private void destroyJsIfNeeded() {
        if (currentActivity == null || currentActivity.isFinishing()) {
            getReactGateway().onDestroyApp(this);
        }
    }

    @Override
    public void invokeDefaultOnBackPressed() {
        if (layout != null && !layout.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        if (layout != null && layout.handleBackInJs()) {
            return;
        }
        if (getReactGateway().isInitialized()) {
            getReactGateway().onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        getReactGateway().onActivityResult(requestCode, resultCode, data);
        NavigationApplication.instance.getActivityCallbacks().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        NavigationApplication.instance.getActivityCallbacks().onKeyUp(keyCode, event);
        return getReactGateway().onKeyUp(getCurrentFocus(), keyCode) || super.onKeyUp(keyCode, event);
    }

    private ReactGateway getReactGateway() {
        return NavigationApplication.instance.getReactGateway();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        OrientationHelper.onConfigurationChanged(newConfig);
        NavigationApplication.instance.getActivityCallbacks().onConfigurationChanged(newConfig);
        super.onConfigurationChanged(newConfig);
    }

    void push(ScreenParams params, Promise onPushComplete) {
        if (modalController.containsNavigator(params.getNavigatorId())) {
            modalController.push(params, onPushComplete);
        } else {
            layout.push(params, onPushComplete);
        }
    }

    void pop(ScreenParams params) {
        if (modalController.containsNavigator(params.getNavigatorId())) {
            modalController.pop(params);
        } else {
            layout.pop(params);
        }
    }

    void popToRoot(ScreenParams params) {
        if (modalController.containsNavigator(params.getNavigatorId())) {
            modalController.popToRoot(params);
        } else {
            layout.popToRoot(params);
        }
    }

    void newStack(ScreenParams params) {
        if (modalController.containsNavigator(params.getNavigatorId())) {
            modalController.newStack(params);
        } else {
            layout.newStack(params);
        }
    }

    void showModal(ScreenParams screenParams) {
        Screen previousScreen = layout.getCurrentScreen();
        NavigationApplication.instance.getEventEmitter().sendWillDisappearEvent(previousScreen.getScreenParams(), NavigationType.ShowModal);
        NavigationApplication.instance.getEventEmitter().sendDidDisappearEvent(previousScreen.getScreenParams(), NavigationType.ShowModal);
        modalController.showModal(screenParams);
    }

    void dismissTopModal(ScreenParams params) {
        modalController.dismissTopModal(params);
    }

    void dismissAllModals() {
        modalController.dismissAllModals();
    }

    public void showLightBox(LightBoxParams params) {
        layout.showLightBox(params);
    }

    public void dismissLightBox() {
        layout.dismissLightBox();
    }

    //TODO all these setters should be combined to something like setStyle
    void setTopBarVisible(String screenInstanceId, boolean hidden, boolean animated) {
        layout.setTopBarVisible(screenInstanceId, hidden, animated);
        modalController.setTopBarVisible(screenInstanceId, hidden, animated);
    }

    void setBottomTabsVisible(boolean hidden, boolean animated) {
        if (layout instanceof BottomTabsLayout) {
            ((BottomTabsLayout) layout).setBottomTabsVisible(hidden, animated);
        }
    }

    void setTitleBarTitle(String screenInstanceId, String title) {
        layout.setTitleBarTitle(screenInstanceId, title);
        modalController.setTitleBarTitle(screenInstanceId, title);
    }

    public void setTitleBarSubtitle(String screenInstanceId, String subtitle) {
        layout.setTitleBarSubtitle(screenInstanceId, subtitle);
        modalController.setTitleBarSubtitle(screenInstanceId, subtitle);
    }

    void setTitleBarButtons(String screenInstanceId, String navigatorEventId, List<TitleBarButtonParams> titleBarButtons) {
        layout.setTitleBarRightButtons(screenInstanceId, navigatorEventId, titleBarButtons);
        modalController.setTitleBarRightButtons(screenInstanceId, navigatorEventId, titleBarButtons);
    }

    void setTitleBarLeftButton(String screenInstanceId, String navigatorEventId, TitleBarLeftButtonParams titleBarLeftButton) {
        layout.setTitleBarLeftButton(screenInstanceId, navigatorEventId, titleBarLeftButton);
        modalController.setTitleBarLeftButton(screenInstanceId, navigatorEventId, titleBarLeftButton);
    }

    void setScreenFab(String screenInstanceId, String navigatorEventId, FabParams fab) {
        layout.setFab(screenInstanceId, navigatorEventId, fab);
        modalController.setFab(screenInstanceId, navigatorEventId, fab);
    }

    public void setScreenStyle(String screenInstanceId, Bundle styleParams) {
        layout.updateScreenStyle(screenInstanceId, styleParams);
        modalController.updateScreenStyle(screenInstanceId, styleParams);
    }

    public void toggleSideMenuVisible(boolean animated, Side side) {
        layout.toggleSideMenuVisible(animated, side);
    }

    public void setSideMenuVisible(boolean animated, boolean visible, Side side) {
        layout.setSideMenuVisible(animated, visible, side);
    }

    public void setSideMenuEnabled(boolean enabled, Side side) {
        layout.setSideMenuEnabled(enabled, side);
    }

    public void selectTopTabByTabIndex(String screenInstanceId, int index) {
        layout.selectTopTabByTabIndex(screenInstanceId, index);
        modalController.selectTopTabByTabIndex(screenInstanceId, index);
    }

    public void selectTopTabByScreen(String screenInstanceId) {
        layout.selectTopTabByScreen(screenInstanceId);
        modalController.selectTopTabByScreen(screenInstanceId);
    }

    public void selectBottomTabByTabIndex(Integer index) {
        if (layout instanceof BottomTabsLayout) {
            ((BottomTabsLayout) layout).selectBottomTabByTabIndex(index);
        }
    }

    public void selectBottomTabByNavigatorId(String navigatorId) {
        if (layout instanceof BottomTabsLayout) {
            ((BottomTabsLayout) layout).selectBottomTabByNavigatorId(navigatorId);
        }
    }

    public void setBottomTabBadgeByIndex(Integer index, String badge) {
        if (layout instanceof BottomTabsLayout) {
            ((BottomTabsLayout) layout).setBottomTabBadgeByIndex(index, badge);
        }
    }

    public void setBottomTabBadgeByNavigatorId(String navigatorId, String badge) {
        if (layout instanceof BottomTabsLayout) {
            ((BottomTabsLayout) layout).setBottomTabBadgeByNavigatorId(navigatorId, badge);
        }
    }

    public void setBottomTabButtonByIndex(Integer index, ScreenParams params) {
        if (layout instanceof BottomTabsLayout) {
            ((BottomTabsLayout) layout).setBottomTabButtonByIndex(index, params);
        }
    }

    public void setBottomTabButtonByNavigatorId(String navigatorId, ScreenParams params) {
        if (layout instanceof BottomTabsLayout) {
            ((BottomTabsLayout) layout).setBottomTabButtonByNavigatorId(navigatorId, params);
        }
    }

    public void showSlidingOverlay(SlidingOverlayParams params) {
        if (modalController.isShowing()) {
            modalController.showSlidingOverlay(params);
        } else {
            layout.showSlidingOverlay(params);
        }
    }

    public void hideSlidingOverlay() {
        if (modalController.isShowing()) {
            modalController.hideSlidingOverlay();
        } else {
            layout.hideSlidingOverlay();
        }
    }

    public void showSnackbar(SnackbarParams params) {
        layout.showSnackbar(params);
    }

    public void dismissSnackbar() {
        layout.dismissSnackbar();
    }

    public void showContextualMenu(String screenInstanceId, ContextualMenuParams params, Callback onButtonClicked) {
        if (modalController.isShowing()) {
            modalController.showContextualMenu(screenInstanceId, params, onButtonClicked);
        } else {
            layout.showContextualMenu(screenInstanceId, params, onButtonClicked);
        }
    }

    public void dismissContextualMenu(String screenInstanceId) {
        if (modalController.isShowing()) {
            modalController.dismissContextualMenu(screenInstanceId);
        } else {
            layout.dismissContextualMenu(screenInstanceId);
        }
    }

    @Override
    public void onEvent(Event event) {
        if (event.getType().equals(ModalDismissedEvent.TYPE)) {
            handleModalDismissedEvent();
        } else if (event.getType().equals(JsDevReloadEvent.TYPE)) {
            postHandleJsDevReloadEvent();
        }
    }

    private void handleModalDismissedEvent() {
        if (!modalController.isShowing()) {
            layout.onModalDismissed();
            OrientationHelper.setOrientation(this, AppStyle.appStyle.orientation);
        }
    }

    public Window getScreenWindow() {
        return modalController.isShowing() ? modalController.getWindow() : getWindow();
    }

    private void postHandleJsDevReloadEvent() {
        NavigationApplication.instance.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                layout.destroy();
                modalController.destroy();
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissions(String[] permissions, int requestCode, PermissionListener listener) {
        mPermissionListener = listener;
        requestPermissions(permissions, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        NavigationApplication.instance.getActivityCallbacks().onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mPermissionListener != null && mPermissionListener.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            mPermissionListener = null;
        }
    }

    public String getCurrentlyVisibleScreenId() {
        return modalController.isShowing() ? modalController.getCurrentlyVisibleScreenId() : layout.getCurrentlyVisibleScreenId();
    }
}
