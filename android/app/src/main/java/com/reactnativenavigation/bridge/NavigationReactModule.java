package com.reactnativenavigation.bridge;

import android.support.annotation.Nullable;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.reactnativenavigation.controllers.NavigationCommandsHandler;
import com.reactnativenavigation.params.ContextualMenuParams;
import com.reactnativenavigation.params.FabParams;
import com.reactnativenavigation.params.LightBoxParams;
import com.reactnativenavigation.params.SlidingOverlayParams;
import com.reactnativenavigation.params.SnackbarParams;
import com.reactnativenavigation.params.TitleBarButtonParams;
import com.reactnativenavigation.params.TitleBarLeftButtonParams;
import com.reactnativenavigation.params.parsers.ContextualMenuParamsParser;
import com.reactnativenavigation.params.parsers.FabParamsParser;
import com.reactnativenavigation.params.parsers.LightBoxParamsParser;
import com.reactnativenavigation.params.parsers.ScreenParamsParser;
import com.reactnativenavigation.params.parsers.SlidingOverlayParamsParser;
import com.reactnativenavigation.params.parsers.SnackbarParamsParser;
import com.reactnativenavigation.params.parsers.TitleBarButtonParamsParser;
import com.reactnativenavigation.params.parsers.TitleBarLeftButtonParamsParser;
import com.reactnativenavigation.views.SideMenu.Side;

import java.util.List;

/**
 * The basic abstract components we will expose:
 * BottomTabs (app) - boolean
 * TopBar (per screen)
 * - TitleBar
 * - - RightButtons
 * - - LeftButton
 * - TopTabs (segmented control / view pager tabs)
 * DeviceStatusBar (app) (colors are per screen)
 * AndroidNavigationBar (app) (colors are per screen)
 * SideMenu (app) - boolean, (menu icon is screen-based)
 */
public class NavigationReactModule extends ReactContextBaseJavaModule {
    public static final String NAME = "NavigationReactModule";

    public NavigationReactModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @ReactMethod
    public void startApp(final ReadableMap params, final @Nullable Promise promise) {
        NavigationCommandsHandler.startApp(BundleConverter.toBundle(params), promise);
    }

    @ReactMethod
    public void setScreenTitleBarTitle(String screenInstanceId, String title) {
        NavigationCommandsHandler.setScreenTitleBarTitle(screenInstanceId, title);
    }

    @ReactMethod
    public void setScreenTitleBarSubtitle(String screenInstanceId, String subtitle) {
        NavigationCommandsHandler.setScreenTitleBarSubtitle(screenInstanceId, subtitle);
    }

    @ReactMethod
    public void setScreenButtons(String screenInstanceId, String navigatorEventId,
                                 ReadableArray rightButtonsParams, ReadableMap leftButtonParams, ReadableMap fab) {
        if (rightButtonsParams != null) {
            setScreenTitleBarRightButtons(screenInstanceId, navigatorEventId, rightButtonsParams);
        }
        if (leftButtonParams != null) {
            setScreenTitleBarLeftButton(screenInstanceId, navigatorEventId, leftButtonParams);
        }
        if (fab != null) {
            setScreenFab(screenInstanceId, navigatorEventId, fab);
        }
    }

    private void setScreenTitleBarRightButtons(String screenInstanceId, String navigatorEventId, ReadableArray rightButtonsParams) {
        List<TitleBarButtonParams> rightButtons = new TitleBarButtonParamsParser()
                .parseButtons(BundleConverter.toBundle(rightButtonsParams));
        NavigationCommandsHandler.setScreenTitleBarRightButtons(screenInstanceId, navigatorEventId, rightButtons);
    }

    private void setScreenTitleBarLeftButton(String screenInstanceId, String navigatorEventId, ReadableMap leftButtonParams) {
        TitleBarLeftButtonParams leftButton = new TitleBarLeftButtonParamsParser()
                .parseSingleButton(BundleConverter.toBundle(leftButtonParams));
        NavigationCommandsHandler.setScreenTitleBarLeftButtons(screenInstanceId, navigatorEventId, leftButton);
    }

    private void setScreenFab(String screenInstanceId, String navigatorEventId, ReadableMap fab) {
        FabParams fabParams = new FabParamsParser().parse(BundleConverter.toBundle(fab), navigatorEventId, screenInstanceId);
        NavigationCommandsHandler.setScreenFab(screenInstanceId, navigatorEventId, fabParams);
    }

    @ReactMethod
    public void setScreenStyle(String screenInstanceId, ReadableMap style) {
        NavigationCommandsHandler.setScreenStyle(screenInstanceId, BundleConverter.toBundle(style));
    }

    @ReactMethod
    public void setBottomTabBadgeByIndex(Integer index, String badge) {
        NavigationCommandsHandler.setBottomTabBadgeByIndex(index, badge);
    }

    @ReactMethod
    public void setBottomTabBadgeByNavigatorId(String navigatorId, String badge) {
        NavigationCommandsHandler.setBottomTabBadgeByNavigatorId(navigatorId, badge);
    }

    @ReactMethod
    public void setBottomTabButtonByIndex(Integer index, final ReadableMap params) {
        NavigationCommandsHandler.setBottomTabButtonByIndex(index, BundleConverter.toBundle(params));
    }

    @ReactMethod
    public void setBottomTabButtonByNavigatorId(String navigatorId, final ReadableMap params) {
        NavigationCommandsHandler.setBottomTabButtonByNavigatorId(navigatorId, BundleConverter.toBundle(params));
    }

    @ReactMethod
    public void selectBottomTabByTabIndex(Integer index) {
        NavigationCommandsHandler.selectBottomTabByTabIndex(index);
    }

    @ReactMethod
    public void selectBottomTabByNavigatorId(String navigatorId) {
        NavigationCommandsHandler.selectBottomTabByNavigatorId(navigatorId);
    }

    @ReactMethod
    public void selectTopTabByTabIndex(String screenInstanceId, int index) {
        NavigationCommandsHandler.selectTopTabByTabIndex(screenInstanceId, index);
    }

    @ReactMethod
    public void selectTopTabByScreen(String screenInstanceId) {
        NavigationCommandsHandler.selectTopTabByScreen(screenInstanceId);
    }

    @ReactMethod
    public void toggleSideMenuVisible(boolean animated, String side) {
        NavigationCommandsHandler.toggleSideMenuVisible(animated, Side.fromString(side));
    }

    @ReactMethod
    public void setSideMenuVisible(boolean animated, boolean visible, String side) {
        NavigationCommandsHandler.setSideMenuVisible(animated, visible, Side.fromString(side));
    }

    @ReactMethod
    public void setSideMenuEnabled(boolean enabled, String side) {
        NavigationCommandsHandler.setSideMenuEnabled(enabled, Side.fromString(side));
    }

    @ReactMethod
    public void toggleTopBarVisible(final ReadableMap params) {
    }

    @ReactMethod
    public void setTopBarVisible(String screenInstanceId, boolean hidden, boolean animated) {
        NavigationCommandsHandler.setTopBarVisible(screenInstanceId, hidden, animated);
    }

    @ReactMethod
    public void toggleBottomTabsVisible(final ReadableMap params) {
    }

    @ReactMethod
    public void setBottomTabsVisible(boolean hidden, boolean animated) {
        NavigationCommandsHandler.setBottomTabsVisible(hidden, animated);
    }

    @ReactMethod
    public void push(final ReadableMap params, Promise onPushComplete) {
        NavigationCommandsHandler.push(BundleConverter.toBundle(params), onPushComplete);
    }

    @ReactMethod
    public void pop(final ReadableMap params) {
        NavigationCommandsHandler.pop(BundleConverter.toBundle(params));
    }

    @ReactMethod
    public void popToRoot(final ReadableMap params) {
        NavigationCommandsHandler.popToRoot(BundleConverter.toBundle(params));
    }

    @ReactMethod
    public void newStack(final ReadableMap params) {
        NavigationCommandsHandler.newStack(BundleConverter.toBundle(params));
    }

    @ReactMethod
    public void showModal(final ReadableMap params) {
        NavigationCommandsHandler.showModal(BundleConverter.toBundle(params));
    }

    @ReactMethod
    public void showLightBox(final ReadableMap params) {
        LightBoxParams lbp = new LightBoxParamsParser(BundleConverter.toBundle(params)).parse();
        NavigationCommandsHandler.showLightBox(lbp);
    }

    @ReactMethod
    public void dismissLightBox() {
        NavigationCommandsHandler.dismissLightBox();
    }

    @ReactMethod
    public void dismissAllModals() {
        NavigationCommandsHandler.dismissAllModals();
    }

    @ReactMethod
    public void dismissTopModal(final ReadableMap params) {
        NavigationCommandsHandler.dismissTopModal(ScreenParamsParser.parse(BundleConverter.toBundle(params)));
    }

    @ReactMethod
    public void showSlidingOverlay(final ReadableMap params) {
        SlidingOverlayParams slidingOverlayParams = new SlidingOverlayParamsParser().parse(BundleConverter.toBundle(params));
        NavigationCommandsHandler.showSlidingOverlay(slidingOverlayParams);
    }

    @ReactMethod
    public void hideSlidingOverlay(final ReadableMap params) {
        NavigationCommandsHandler.hideSlidingOverlay();
    }

    @ReactMethod
    public void showSnackbar(final ReadableMap params) {
        SnackbarParams snackbarParams = new SnackbarParamsParser().parse(BundleConverter.toBundle(params));
        NavigationCommandsHandler.showSnackbar(snackbarParams);
    }

    @ReactMethod
    public void dismissSnackbar() {
        NavigationCommandsHandler.dismissSnackbar();
    }

    @ReactMethod
    public void showContextualMenu(final String screenInstanceId, final ReadableMap params, final Callback onButtonClicked) {
        ContextualMenuParams contextualMenuParams =
                new ContextualMenuParamsParser().parse(BundleConverter.toBundle(params));
        NavigationCommandsHandler.showContextualMenu(screenInstanceId, contextualMenuParams, onButtonClicked);
    }

    @ReactMethod
    public void dismissContextualMenu(String screenInstanceId) {
        NavigationCommandsHandler.dismissContextualMenu(screenInstanceId);
    }

    @ReactMethod
    public void getOrientation(Promise promise) {
        NavigationCommandsHandler.getOrientation(promise);
    }

    @ReactMethod
    public void isAppLaunched(Promise promise) {
        NavigationCommandsHandler.isAppLaunched(promise);
    }

    @ReactMethod
    public void isRootLaunched(Promise promise) {
        NavigationCommandsHandler.isRootLaunched(promise);
    }

    @ReactMethod
    public void getCurrentlyVisibleScreenId(Promise promise) {
        NavigationCommandsHandler.getCurrentlyVisibleScreenId(promise);
    }

    @ReactMethod
    public void getLaunchArgs(Promise promise) {
        NavigationCommandsHandler.getLaunchArgs(promise);
    }
}
