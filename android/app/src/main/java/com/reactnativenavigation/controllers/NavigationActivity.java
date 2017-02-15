package com.reactnativenavigation.controllers;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.facebook.react.bridge.Callback;
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
import com.reactnativenavigation.params.ScreenParams;
import com.reactnativenavigation.params.SlidingOverlayParams;
import com.reactnativenavigation.params.SnackbarParams;
import com.reactnativenavigation.params.TitleBarButtonParams;
import com.reactnativenavigation.params.TitleBarLeftButtonParams;
import com.reactnativenavigation.react.JsDevReloadHandler;
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

        if (!NavigationApplication.instance.isReactContextInitialized()) {
            NavigationApplication.instance.startReactContextOnceInBackgroundAndExecuteJS();
            return;
        }

        activityParams = NavigationCommandsHandler.parseActivityParams(getIntent());

        disableActivityShowAnimationIfNeeded();
        createLayout();
        createModalController();
        NavigationApplication.instance.getActivityCallbacks().onActivityCreated(this, savedInstanceState);
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
        NavigationApplication.instance.getReactGateway().onResumeActivity(this, this);
        NavigationApplication.instance.getActivityCallbacks().onActivityResumed(this);
        EventBus.instance.register(this);
        IntentDataHandler.onPostResume(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        NavigationApplication.instance.getReactGateway().onNewIntent(intent);
        NavigationApplication.instance.getActivityCallbacks().onNewIntent(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        currentActivity = null;
        IntentDataHandler.onPause(getIntent());
        NavigationApplication.instance.getReactGateway().onPauseActivity();
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
            NavigationApplication.instance.getReactGateway().onDestroyApp();
        }
    }

    @Override
    public void invokeDefaultOnBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (layout != null && !layout.onBackPressed()) {
            NavigationApplication.instance.getReactGateway().onBackPressed();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        NavigationApplication.instance.getReactGateway().onActivityResult(requestCode, resultCode, data);
        NavigationApplication.instance.getActivityCallbacks().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return JsDevReloadHandler.onKeyUp(getCurrentFocus(), keyCode) || super.onKeyUp(keyCode, event);
    }

    void push(ScreenParams params) {
        if (modalController.containsNavigator(params.getNavigatorId())) {
            modalController.push(params);
        } else {
            layout.push(params);
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
        modalController.showModal(screenParams);
    }

    void dismissTopModal() {
        modalController.dismissTopModal();
    }

    void dismissAllModals() {
        modalController.dismissAllModals();
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
    }

    public void toggleSideMenuVisible(boolean animated, Side side) {
        layout.toggleSideMenuVisible(animated, side);
    }

    public void setSideMenuVisible(boolean animated, boolean visible, Side side) {
        layout.setSideMenuVisible(animated, visible, side);
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

    public void showSlidingOverlay(SlidingOverlayParams params) {
        layout.showSlidingOverlay(params);
    }

    public void hideSlidingOverlay() {
        layout.hideSlidingOverlay();
    }

    public void showSnackbar(SnackbarParams params) {
        layout.showSnackbar(params);
    }

    public void dismissSnackbar() {
        layout.dismissSnackbar();
    }

    public void showContextualMenu(String screenInstanceId, ContextualMenuParams params, Callback onButtonClicked) {
        layout.showContextualMenu(screenInstanceId, params, onButtonClicked);
        modalController.showContextualMenu(screenInstanceId, params, onButtonClicked);
    }

    public void dismissContextualMenu(String screenInstanceId) {
        layout.dismissContextualMenu(screenInstanceId);
        modalController.dismissContextualMenu(screenInstanceId);
    }

    @Override
    public void onEvent(Event event) {
        if (event.getType().equals(ModalDismissedEvent.TYPE)) {
            handleModalDismissedEvent();
        } else if (event.getType().equals(JsDevReloadEvent.TYPE)) {
            handleJsDevReloadEvent();
        }
    }

    private void handleModalDismissedEvent() {
        if (!modalController.isShowing()) {
            layout.onModalDismissed();
        }
    }

    private void handleJsDevReloadEvent() {
        modalController.destroy();
        layout.destroy();
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissions(String[] permissions, int requestCode, PermissionListener listener) {
        mPermissionListener = listener;
        requestPermissions(permissions, requestCode);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        NavigationApplication.instance.getActivityCallbacks().onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mPermissionListener != null && mPermissionListener.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            mPermissionListener = null;
        }
    }
}
