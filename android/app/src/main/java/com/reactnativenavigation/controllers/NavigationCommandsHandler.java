package com.reactnativenavigation.controllers;

import android.content.Intent;
import android.os.Bundle;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.reactnativenavigation.NavigationApplication;
import com.reactnativenavigation.params.ActivityParams;
import com.reactnativenavigation.params.ContextualMenuParams;
import com.reactnativenavigation.params.FabParams;
import com.reactnativenavigation.params.LightBoxParams;
import com.reactnativenavigation.params.ScreenParams;
import com.reactnativenavigation.params.SlidingOverlayParams;
import com.reactnativenavigation.params.SnackbarParams;
import com.reactnativenavigation.params.TitleBarButtonParams;
import com.reactnativenavigation.params.TitleBarLeftButtonParams;
import com.reactnativenavigation.params.parsers.ActivityParamsParser;
import com.reactnativenavigation.params.parsers.ScreenParamsParser;
import com.reactnativenavigation.utils.OrientationHelper;
import com.reactnativenavigation.views.SideMenu.Side;

import java.util.List;

public class NavigationCommandsHandler {

    private static final String ACTIVITY_PARAMS_BUNDLE = "ACTIVITY_PARAMS_BUNDLE";

    static ActivityParams parseActivityParams(Intent intent) {
        return ActivityParamsParser.parse(intent.getBundleExtra(NavigationCommandsHandler.ACTIVITY_PARAMS_BUNDLE));
    }

    /**
     * start a new activity with CLEAR_TASK | NEW_TASK
     *
     * @param params ActivityParams as bundle
     */

    public static void startApp(Bundle params) {
        Intent intent = new Intent(NavigationApplication.instance, NavigationActivity.class);
        IntentDataHandler.onStartApp(intent);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(ACTIVITY_PARAMS_BUNDLE, params);
        intent.putExtra("animationType", params.getString("animationType"));
        NavigationApplication.instance.startActivity(intent);
    }

    public static void push(Bundle screenParams) {
        final NavigationActivity currentActivity = NavigationActivity.currentActivity;
        if (currentActivity == null) {
            return;
        }

        final ScreenParams params = ScreenParamsParser.parse(screenParams);
        NavigationApplication.instance.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                currentActivity.push(params);
            }
        });
    }

    public static void pop(Bundle screenParams) {
        final NavigationActivity currentActivity = NavigationActivity.currentActivity;
        if (currentActivity == null) {
            return;
        }

        final ScreenParams params = ScreenParamsParser.parse(screenParams);
        NavigationApplication.instance.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                currentActivity.pop(params);
            }
        });
    }

    public static void popToRoot(Bundle screenParams) {
        final NavigationActivity currentActivity = NavigationActivity.currentActivity;
        if (currentActivity == null) {
            return;
        }

        final ScreenParams params = ScreenParamsParser.parse(screenParams);
        NavigationApplication.instance.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                currentActivity.popToRoot(params);
            }
        });
    }

    public static void newStack(Bundle screenParams) {
        final NavigationActivity currentActivity = NavigationActivity.currentActivity;
        if (currentActivity == null) {
            return;
        }

        final ScreenParams params = ScreenParamsParser.parse(screenParams);
        NavigationApplication.instance.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                currentActivity.newStack(params);
            }
        });
    }

    public static void setTopBarVisible(final String screenInstanceID, final boolean hidden, final boolean animated) {
        final NavigationActivity currentActivity = NavigationActivity.currentActivity;
        if (currentActivity == null) {
            return;
        }

        NavigationApplication.instance.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                currentActivity.setTopBarVisible(screenInstanceID, hidden, animated);
            }
        });
    }

    public static void setBottomTabsVisible(final boolean hidden, final boolean animated) {
        final NavigationActivity currentActivity = NavigationActivity.currentActivity;
        if (currentActivity == null) {
            return;
        }

        NavigationApplication.instance.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                currentActivity.setBottomTabsVisible(hidden, animated);
            }
        });
    }

    public static void setScreenTitleBarTitle(final String screenInstanceId, final String title) {
        final NavigationActivity currentActivity = NavigationActivity.currentActivity;
        if (currentActivity == null) {
            return;
        }

        NavigationApplication.instance.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                currentActivity.setTitleBarTitle(screenInstanceId, title);
            }
        });
    }

    public static void setScreenTitleBarSubtitle(final String screenInstanceId, final String subtitle) {
        final NavigationActivity currentActivity = NavigationActivity.currentActivity;
        if (currentActivity == null) {
            return;
        }

        NavigationApplication.instance.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                currentActivity.setTitleBarSubtitle(screenInstanceId, subtitle);
            }
        });
    }

    public static void showModal(final Bundle params) {
        final NavigationActivity currentActivity = NavigationActivity.currentActivity;
        if (currentActivity == null) {
            return;
        }

        NavigationApplication.instance.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                currentActivity.showModal(ScreenParamsParser.parse(params));
            }
        });
    }

    public static void showLightBox(final LightBoxParams params) {
        final NavigationActivity currentActivity = NavigationActivity.currentActivity;
        if (currentActivity == null) {
            return;
        }

        NavigationApplication.instance.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                currentActivity.showLightBox(params);
            }
        });
    }

    public static void dismissLightBox() {
        final NavigationActivity currentActivity = NavigationActivity.currentActivity;
        if (currentActivity == null) {
            return;
        }

        NavigationApplication.instance.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                currentActivity.dismissLightBox();
            }
        });
    }

    public static void setScreenTitleBarRightButtons(final String screenInstanceId,
                                                     final String navigatorEventId,
                                                     final List<TitleBarButtonParams> titleBarButtons) {
        final NavigationActivity currentActivity = NavigationActivity.currentActivity;
        if (currentActivity == null) {
            return;
        }

        NavigationApplication.instance.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                currentActivity.setTitleBarButtons(screenInstanceId, navigatorEventId, titleBarButtons);
            }
        });
    }

    public static void setScreenTitleBarLeftButtons(final String screenInstanceId,
                                                    final String navigatorEventId,
                                                    final TitleBarLeftButtonParams titleBarButtons) {
        final NavigationActivity currentActivity = NavigationActivity.currentActivity;
        if (currentActivity == null) {
            return;
        }

        NavigationApplication.instance.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                currentActivity.setTitleBarLeftButton(screenInstanceId, navigatorEventId, titleBarButtons);
            }
        });
    }

    public static void setScreenFab(final String screenInstanceId, final String navigatorEventId, final FabParams fab) {
        final NavigationActivity currentActivity = NavigationActivity.currentActivity;
        if (currentActivity == null) {
            return;
        }
        NavigationApplication.instance.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                currentActivity.setScreenFab(screenInstanceId, navigatorEventId, fab);
            }
        });
    }

    public static void setScreenStyle(final String screenInstanceId, final Bundle styleParams) {
        final NavigationActivity currentActivity = NavigationActivity.currentActivity;
        if (currentActivity == null) {
            return;
        }
        NavigationApplication.instance.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                currentActivity.setScreenStyle(screenInstanceId, styleParams);
            }
        });
    }

    public static void dismissTopModal() {
        final NavigationActivity currentActivity = NavigationActivity.currentActivity;
        if (currentActivity == null) {
            return;
        }

        NavigationApplication.instance.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                currentActivity.dismissTopModal();
            }
        });
    }

    public static void dismissAllModals() {
        final NavigationActivity currentActivity = NavigationActivity.currentActivity;
        if (currentActivity == null) {
            return;
        }

        NavigationApplication.instance.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                currentActivity.dismissAllModals();
            }
        });
    }

    public static void toggleSideMenuVisible(final boolean animated, final Side side) {
        final NavigationActivity currentActivity = NavigationActivity.currentActivity;
        if (currentActivity == null) {
            return;
        }

        NavigationApplication.instance.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                currentActivity.toggleSideMenuVisible(animated, side);
            }
        });
    }

    public static void setSideMenuVisible(final boolean animated, final boolean visible, final Side side) {
        final NavigationActivity currentActivity = NavigationActivity.currentActivity;
        if (currentActivity == null) {
            return;
        }

        NavigationApplication.instance.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                currentActivity.setSideMenuVisible(animated, visible, side);
            }
        });
    }

    public static void setSideMenuEnabled(final boolean enabled, final Side side) {
        final NavigationActivity currentActivity = NavigationActivity.currentActivity;
        if (currentActivity == null) {
            return;
        }

        NavigationApplication.instance.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                currentActivity.setSideMenuEnabled(enabled, side);
            }
        });
    }

    public static void selectTopTabByTabIndex(final String screenInstanceId, final int index) {
        final NavigationActivity currentActivity = NavigationActivity.currentActivity;
        if (currentActivity == null) {
            return;
        }

        NavigationApplication.instance.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                currentActivity.selectTopTabByTabIndex(screenInstanceId, index);
            }
        });
    }

    public static void selectTopTabByScreen(final String screenInstanceId) {
        final NavigationActivity currentActivity = NavigationActivity.currentActivity;
        if (currentActivity == null) {
            return;
        }
        NavigationApplication.instance.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                currentActivity.selectTopTabByScreen(screenInstanceId);
            }
        });
    }

    public static void selectBottomTabByTabIndex(final Integer index) {
        final NavigationActivity currentActivity = NavigationActivity.currentActivity;
        if (currentActivity == null) {
            return;
        }

        NavigationApplication.instance.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                currentActivity.selectBottomTabByTabIndex(index);
            }
        });
    }

    public static void selectBottomTabByNavigatorId(final String navigatorId) {
        final NavigationActivity currentActivity = NavigationActivity.currentActivity;
        if (currentActivity == null) {
            return;
        }

        NavigationApplication.instance.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                currentActivity.selectBottomTabByNavigatorId(navigatorId);
            }
        });
    }

    public static void setBottomTabBadgeByIndex(final Integer index, final String badge) {
        final NavigationActivity currentActivity = NavigationActivity.currentActivity;
        if (currentActivity == null) {
            return;
        }

        NavigationApplication.instance.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                currentActivity.setBottomTabBadgeByIndex(index, badge);
            }
        });
    }

    public static void setBottomTabBadgeByNavigatorId(final String navigatorId, final String badge) {
        final NavigationActivity currentActivity = NavigationActivity.currentActivity;
        if (currentActivity == null) {
            return;
        }

        NavigationApplication.instance.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                currentActivity.setBottomTabBadgeByNavigatorId(navigatorId, badge);
            }
        });
    }

    public static void setBottomTabButtonByIndex(final Integer index, final Bundle screenParams) {
        final NavigationActivity currentActivity = NavigationActivity.currentActivity;
        if (currentActivity == null) {
            return;
        }

        final ScreenParams params = ScreenParamsParser.parse(screenParams);
        NavigationApplication.instance.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                currentActivity.setBottomTabButtonByIndex(index, params);
            }
        });
    }

    public static void setBottomTabButtonByNavigatorId(final String navigatorId, final Bundle screenParams) {
        final NavigationActivity currentActivity = NavigationActivity.currentActivity;
        if (currentActivity == null) {
            return;
        }

        final ScreenParams params = ScreenParamsParser.parse(screenParams);
        NavigationApplication.instance.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                currentActivity.setBottomTabButtonByNavigatorId(navigatorId, params);
            }
        });
    }

    public static void showSlidingOverlay(final SlidingOverlayParams params) {
        final NavigationActivity currentActivity = NavigationActivity.currentActivity;
        if (currentActivity == null) {
            return;
        }

        NavigationApplication.instance.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                currentActivity.showSlidingOverlay(params);
            }
        });
    }

    public static void hideSlidingOverlay() {
        final NavigationActivity currentActivity = NavigationActivity.currentActivity;
        if (currentActivity == null) {
            return;
        }

        NavigationApplication.instance.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                currentActivity.hideSlidingOverlay();
            }
        });
    }

    public static void showSnackbar(final SnackbarParams params) {
        final NavigationActivity currentActivity = NavigationActivity.currentActivity;
        if (currentActivity == null) {
            return;
        }

        NavigationApplication.instance.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                currentActivity.showSnackbar(params);
            }
        });
    }

    public static void showContextualMenu(final String screenInstanceId, final ContextualMenuParams params, final Callback onButtonClicked) {
        final NavigationActivity currentActivity = NavigationActivity.currentActivity;
        if (currentActivity == null) {
            return;
        }

        NavigationApplication.instance.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                currentActivity.showContextualMenu(screenInstanceId, params, onButtonClicked);
            }
        });
    }

    public static void dismissContextualMenu(final String screenInstanceId) {
        final NavigationActivity currentActivity = NavigationActivity.currentActivity;
        if (currentActivity == null) {
            return;
        }

        NavigationApplication.instance.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                currentActivity.dismissContextualMenu(screenInstanceId);
            }
        });
    }

    public static void dismissSnackbar() {
        final NavigationActivity currentActivity = NavigationActivity.currentActivity;
        if (currentActivity == null) {
            return;
        }

        NavigationApplication.instance.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                currentActivity.dismissSnackbar();
            }
        });
    }

    public static void getOrientation(Promise promise) {
        final NavigationActivity currentActivity = NavigationActivity.currentActivity;
        if (currentActivity == null) {
            return;
        }
        promise.resolve(OrientationHelper.getOrientation(currentActivity));
    }
}
